package com.xulai.chainmailed;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = ChainMailed.MODID)
public final class ChainMailedConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue SOUND_CHAINMAILED_BLOCK = BUILDER.comment(
            "",
            " 锁链甲挡下攻击时，是否播放锁链甲格挡音效。",
            " 关闭后减伤照常生效，只是不再发出声音。",
            " 音效资源：chainmailed:chainmail_block",
            "",
            " Whether chainmail armor plays its clinking sound when it stops an attack.",
            " Turning this off keeps the damage reduction, it only removes the sound.",
            " Sound resource: chainmailed:chainmail_block").define("sound_chainmailed_block", true);

    private static final ModConfigSpec.DoubleValue IMPACT_RESISTANCE_CHAINMAILED_BASIC = BUILDER.comment(
            "",
            " 每件锁链甲（或被加装锁链内衬的护甲）提供的「冲击抗性」点数。",
            " 冲击抗性的作用：受到近战攻击、玩家攻击或通用伤害时，先扣掉等同于该数值的伤害，",
            " 但至少保留 1 点伤害，因此永远不会完全免伤。",
            " 1 点冲击抗性 = 每次近战伤害固定少 1 点。",
            "",
            " How many Impact Resistance points each chainmail or chainmailed armor piece grants.",
            " Impact Resistance subtracts its value from melee, player and generic damage,",
            " but always leaves at least 1 point of damage, so it never makes you immune.",
            " 1 point of Impact Resistance = 1 less damage taken from every melee hit.").defineInRange("impact_resistance_chainmailed_basic", 0.5D, 0.1D, Double.MAX_VALUE);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> ARMOR_FOR_SLOT_CHAINMAILED_BASIC = BUILDER.comment(
            "",
            " 各部位的基础护甲值，写成「部位:数值」，部位可取 head / chest / legs / feet。",
            " 它有两个用途：",
            "   1. 锁链甲自身的护甲值直接改用这里的数值（原版锁链甲为 head 2 / chest 5 / legs 4 / feet 1）。",
            "   2. 其它护甲加装锁链内衬后，在自身护甲值之上再叠加这里的数值。",
            " 例：chest:2 表示锁链胸甲的护甲值变为 2，而加衬的铁胸甲变成 6+2=8。",
            " 列表留空或缺少某个部位时，该部位按 0 处理。",
            "",
            " Base armor value per slot, written as \"slot:value\" with slot being head / chest / legs / feet.",
            " It is used twice:",
            "   1. Chainmail's own armor value is replaced by these numbers (vanilla chainmail is head 2 / chest 5 / legs 4 / feet 1).",
            "   2. Any other armor that takes a chainmail lining gains these numbers on top of its own defense.",
            " For example chest:2 makes the chainmail chestplate worth 2 armor, and an iron chestplate 6+2=8.",
            " An empty list, or a missing slot, is treated as 0 for that slot.").defineListAllowEmpty("armor_for_slot_chainmailed_basic", List.of("head:1", "chest:2", "legs:2", "feet:1"), ChainMailedConfig::validateSlotEntry);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> CHAINMAILED_BLACKLIST = BUILDER.comment(
            "",
            " 无法加装锁链内衬的护甲黑名单，填写物品 ID，例如 minecraft:turtle_helmet。",
            " 名单内的护甲右键时不会有任何反应，也不会消耗副手的锁链甲。",
            " 默认留空，即所有非锁链材质的护甲都可以加衬。",
            "",
            " Blacklist of armor pieces that cannot take a chainmail lining, written as item ids",
            " such as minecraft:turtle_helmet.",
            " Armor on this list ignores the interaction completely and does not consume the offhand chainmail.",
            " Empty by default, meaning every non-chainmail armor piece can be lined.").defineListAllowEmpty("chainmailed_blacklist", new ArrayList<>(), ChainMailedConfig::validateItemName);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean soundChainmailedBlock = true;
    public static double impactResistanceChainmailedBasic = 0.5D;
    public static int[] armorForSlotChainmailedBasic = new int[]{1, 2, 2, 1};
    public static List<Item> chainmailedBlacklist = List.of();

    private ChainMailedConfig() {
    }

    private static boolean validateItemName(Object candidate) {
        return candidate instanceof String name && BuiltInRegistries.ITEM.containsKey(ResourceLocation.tryParse(name));
    }

    private static boolean validateSlotEntry(Object candidate) {
        if (!(candidate instanceof String entry)) {
            return false;
        }
        int separator = entry.indexOf(':');
        if (separator <= 0 || separator == entry.length() - 1) {
            return false;
        }
        String slotName = entry.substring(0, separator);
        return slotName.equals("head") || slotName.equals("chest") || slotName.equals("legs") || slotName.equals("feet");
    }

    @SubscribeEvent
    static void onConfigLoad(ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) {
            return;
        }
        refresh();
    }

    public static boolean addToBlacklist(Item item) {
        String id = blacklistId(item);
        if (id == null) {
            return false;
        }
        List<String> entries = new ArrayList<>(CHAINMAILED_BLACKLIST.get());
        if (entries.contains(id)) {
            return false;
        }
        entries.add(id);
        writeBlacklist(entries);
        return true;
    }

    public static boolean removeFromBlacklist(Item item) {
        String id = blacklistId(item);
        if (id == null) {
            return false;
        }
        List<String> entries = new ArrayList<>(CHAINMAILED_BLACKLIST.get());
        if (!entries.remove(id)) {
            return false;
        }
        writeBlacklist(entries);
        return true;
    }

    @Nullable
    private static String blacklistId(Item item) {
        if (item == Items.AIR) {
            return null;
        }
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
        return key == null ? null : key.toString();
    }

    private static void writeBlacklist(List<String> entries) {
        CHAINMAILED_BLACKLIST.set(entries);
        CHAINMAILED_BLACKLIST.save();
        refresh();
    }

    private static void refresh() {
        soundChainmailedBlock = SOUND_CHAINMAILED_BLOCK.get();
        impactResistanceChainmailedBasic = IMPACT_RESISTANCE_CHAINMAILED_BASIC.get();
        armorForSlotChainmailedBasic = toArmorArray(ARMOR_FOR_SLOT_CHAINMAILED_BASIC.get());

        List<Item> blacklist = new ArrayList<>();
        for (String name : CHAINMAILED_BLACKLIST.get()) {
            ResourceLocation location = ResourceLocation.tryParse(name);
            if (location == null) {
                continue;
            }
            Item item = BuiltInRegistries.ITEM.get(location);
            if (item != Items.AIR) {
                blacklist.add(item);
            }
        }
        chainmailedBlacklist = List.copyOf(blacklist);
    }

    private static int[] toArmorArray(List<? extends String> entries) {
        int[] armor = new int[]{0, 0, 0, 0};
        for (String entry : entries) {
            int separator = entry.indexOf(':');
            if (separator <= 0 || separator == entry.length() - 1) {
                continue;
            }
            EquipmentSlot slot;
            try {
                slot = EquipmentSlot.byName(entry.substring(0, separator));
            } catch (IllegalArgumentException exception) {
                continue;
            }
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR || slot.getIndex() >= armor.length) {
                continue;
            }
            try {
                armor[slot.getIndex()] = Integer.parseInt(entry.substring(separator + 1).trim());
            } catch (NumberFormatException exception) {
                ChainMailed.LOGGER.warn("Ignoring malformed armor_for_slot_chainmailed_basic entry: {}", entry);
            }
        }
        return armor;
    }
}
