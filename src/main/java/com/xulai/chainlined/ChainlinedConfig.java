package com.xulai.chainlined;

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

@EventBusSubscriber(modid = Chainlined.MODID)
public final class ChainlinedConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SOUND_CHAINMAILED_BLOCK;

    public static final ModConfigSpec.DoubleValue IMPACT_RESISTANCE_CHAINMAILED_BASIC;

    public static final ModConfigSpec.ConfigValue<List<? extends String>> ARMOR_FOR_SLOT_CHAINMAILED_BASIC;

    public static final ModConfigSpec.ConfigValue<List<? extends String>> CHAINMAILED_BLACKLIST;

    public static final ModConfigSpec.IntValue ANVIL_EXPERIENCE_COST;

    static {
        SOUND_CHAINMAILED_BLOCK = BUILDER
                .comment(
                        "锁链甲格挡音效",
                        "Chainmail block sound",
                        "锁链甲挡下攻击时，是否播放锁链甲格挡音效。",
                        "Whether chainmail armor plays its clinking sound when it stops an attack.",
                        "关闭后减伤照常生效，只是不再发出声音。",
                        "Turning this off keeps the damage reduction, it only removes the sound.",
                        "Default: true / 默认：true")
                .define("soundChainmailedBlock", true);

        BUILDER.comment(" ");

        IMPACT_RESISTANCE_CHAINMAILED_BASIC = BUILDER
                .comment(
                        "冲击抗性（每件）",
                        "Impact Resistance per piece",
                        "每件锁链甲（或被加装锁链内衬的护甲）提供的冲击抗性点数。",
                        "How many Impact Resistance points each chainmail or lined armor piece grants.",
                        "受到近战攻击、玩家攻击或通用伤害时，先扣掉等同于该数值的伤害，但至少保留 1 点伤害，因此永远不会完全免伤。",
                        "Impact Resistance subtracts its value from melee, player and generic damage, but always leaves at least 1 point of damage, so it never makes you immune.",
                        "Default: 0.5 / 默认：0.5",
                        "Range: 0.1 ~ 2147483647")
                .defineInRange("impactResistanceChainmailedBasic", 0.5D, 0.1D, Double.MAX_VALUE);

        BUILDER.comment(" ");

        ARMOR_FOR_SLOT_CHAINMAILED_BASIC = BUILDER
                .comment(
                        "各部位基础护甲值",
                        "Base armor value per slot",
                        "各部位的基础护甲值，写成「部位:数值」，部位可取 head / chest / legs / feet。",
                        "Base armor value per slot, written as \"slot:value\" with slot being head / chest / legs / feet.",
                        "锁链甲自身的护甲值直接改用这里的数值；其它护甲加装锁链内衬后，在自身护甲值之上再叠加这里的数值。",
                        "Chainmail's own armor value is replaced by these numbers; other armor lined with chainmail gains these numbers on top of its own defense.",
                        "Default: [head:1, chest:2, legs:2, feet:1] / 默认：[head:1, chest:2, legs:2, feet:1]")
                .defineListAllowEmpty("armorForSlotChainmailedBasic", List.of("head:1", "chest:2", "legs:2", "feet:1"), ChainlinedConfig::validateSlotEntry);

        BUILDER.comment(" ");

        CHAINMAILED_BLACKLIST = BUILDER
                .comment(
                        "锁链内衬黑名单",
                        "Chainmail lining blacklist",
                        "无法加装锁链内衬的护甲黑名单，填写物品 ID，例如 minecraft:turtle_helmet。",
                        "Blacklist of armor pieces that cannot take a chainmail lining, written as item ids such as minecraft:turtle_helmet.",
                        "名单内的护甲在铁砧中不会产生加衬配方。",
                        "Armor on this list ignores the anvil interaction completely.",
                        "Default: [] / 默认：[]")
                .defineListAllowEmpty("chainmailedBlacklist", new ArrayList<>(), ChainlinedConfig::validateItemName);

        BUILDER.comment(" ");

        ANVIL_EXPERIENCE_COST = BUILDER
                .comment(
                        "铁砧加衬经验费用",
                        "Anvil lining experience cost",
                        "在铁砧上为护甲加装锁链内衬所需消耗的经验等级。",
                        "How many experience levels it costs to line an armor piece with chainmail on an anvil.",
                        "Default: 1 / 默认：1",
                        "Range: 0 ~ 10000")
                .defineInRange("anvilExperienceCost", 1, 0, 10000);
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean soundChainmailedBlock = true;
    public static double impactResistanceChainmailedBasic = 0.5D;
    public static int[] armorForSlotChainmailedBasic = new int[]{1, 2, 2, 1};
    public static List<Item> chainmailedBlacklist = List.of();
    public static int anvilExperienceCost = 1;

    private ChainlinedConfig() {
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
        anvilExperienceCost = ANVIL_EXPERIENCE_COST.get();

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
                Chainlined.LOGGER.warn("Ignoring malformed armorForSlotChainmailedBasic entry: {}", entry);
            }
        }
        return armor;
    }
}