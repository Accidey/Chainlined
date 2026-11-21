package com.xulai.chainlined.common;

import com.xulai.chainlined.Chainlined;
import com.xulai.chainlined.ChainlinedConfig;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = Chainlined.MODID)
public final class ChainlinedEvents {
    private static final Identifier LINING_ID = Identifier.fromNamespaceAndPath(Chainlined.MODID, "lining");

    private ChainlinedEvents() {
    }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        EquipmentSlot slot = ChainlinedHelper.armorSlot(left);
        if (slot == null) {
            return;
        }
        if (ChainlinedConfig.chainmailedBlacklist.contains(left.getItem())) {
            return;
        }
        if (ChainlinedHelper.hasChainmailed(left)) {
            return;
        }
        if (ChainlinedHelper.isChainmail(left)) {
            return;
        }
        if (!ChainlinedHelper.isChainmail(right)) {
            return;
        }
        if (ChainlinedHelper.armorSlot(right) != slot) {
            return;
        }
        ItemStack output = left.copy();
        ChainlinedHelper.storeChainmailed(output, right, event.getPlayer().level().registryAccess());
        event.setOutput(output);
        event.setXpCost(ChainlinedConfig.anvilExperienceCost);
        event.setMaterialCost(1);
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack crafted = event.getCrafting();
        EquipmentSlot craftedSlot = ChainlinedHelper.armorSlot(crafted);
        if (craftedSlot == null) {
            return;
        }
        if (ChainlinedHelper.hasChainmailed(crafted)) {
            return;
        }
        if (ChainlinedConfig.chainmailedBlacklist.contains(crafted.getItem())) {
            return;
        }
        Container matrix = event.getInventory();
        for (int i = 0; i < matrix.getContainerSize(); i++) {
            ItemStack ingredient = matrix.getItem(i);
            if (ChainlinedHelper.armorSlot(ingredient) != craftedSlot) {
                continue;
            }
            if (!ChainlinedHelper.hasChainmailed(ingredient)) {
                continue;
            }
            ItemStack snapshot = ChainlinedHelper.copyChainmailed(ingredient, event.getEntity().level().registryAccess());
            if (snapshot == null || snapshot.isEmpty()) {
                continue;
            }
            ChainlinedHelper.storeChainmailed(crafted, snapshot, event.getEntity().level().registryAccess());
            return;
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemAttributeModifier(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        EquipmentSlot slot = ChainlinedHelper.armorSlot(stack);
        if (slot == null) {
            return;
        }
        boolean chainmail = ChainlinedHelper.isChainmail(stack);
        if (!chainmail && !ChainlinedHelper.hasChainmailed(stack)) {
            return;
        }

        int bonus = armorForSlot(slot);
        if (bonus == 0) {
            return;
        }
        EquipmentSlotGroup group = EquipmentSlotGroup.bySlot(slot);

        if (chainmail) {
            Identifier id = Identifier.withDefaultNamespace("armor." + armorTypeName(slot));
            event.removeAllModifiersFor(Attributes.ARMOR);
            event.addModifier(Attributes.ARMOR, new AttributeModifier(id, bonus, AttributeModifier.Operation.ADD_VALUE), group);
            return;
        }

        List<Identifier> absorbed = new ArrayList<>();
        Identifier targetId = null;
        double total = bonus;
        for (ItemAttributeModifiers.Entry entry : event.getModifiers()) {
            if (!entry.attribute().equals(Attributes.ARMOR)) {
                continue;
            }
            AttributeModifier modifier = entry.modifier();
            if (modifier.operation() != AttributeModifier.Operation.ADD_VALUE) {
                continue;
            }
            total += modifier.amount();
            if (targetId == null) {
                targetId = modifier.id();
            } else {
                absorbed.add(modifier.id());
            }
        }
        for (Identifier absorbedId : absorbed) {
            event.removeModifier(Attributes.ARMOR, absorbedId);
        }
        if (targetId == null) {
            event.addModifier(Attributes.ARMOR, new AttributeModifier(LINING_ID, total, AttributeModifier.Operation.ADD_VALUE), group);
            return;
        }
        event.replaceModifier(Attributes.ARMOR, new AttributeModifier(targetId, total, AttributeModifier.Operation.ADD_VALUE), group);
    }

    public static int armorForSlot(EquipmentSlot slot) {
        int[] table = ChainlinedConfig.armorForSlotChainmailedBasic;
        int index = slot.getIndex();
        return index >= 0 && index < table.length ? table[index] : 0;
    }

    private static String armorTypeName(EquipmentSlot slot) {
        for (ArmorType type : ArmorType.values()) {
            if (type.getSlot() == slot) {
                return type.getName();
            }
        }
        return slot.getName();
    }
}
