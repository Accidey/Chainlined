package com.xulai.chainlined.common;

import com.xulai.chainlined.Chainlined;
import com.xulai.chainlined.ChainlinedConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = Chainlined.MODID)
public final class ChainlinedEvents {
    private ChainlinedEvents() {
    }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        if (!(left.getItem() instanceof ArmorItem armor)) {
            return;
        }
        if (ChainlinedConfig.chainmailedBlacklist.contains(armor)) {
            return;
        }
        if (ChainlinedHelper.hasChainmailed(left)) {
            return;
        }
        if (armor.getMaterial().is(ArmorMaterials.CHAIN)) {
            return;
        }
        if (!(right.getItem() instanceof ArmorItem chainmail)
                || !chainmail.getMaterial().is(ArmorMaterials.CHAIN)
                || chainmail.getType().getSlot() != armor.getType().getSlot()) {
            return;
        }
        ItemStack output = left.copy();
        ChainlinedHelper.storeChainmailed(output, right, event.getPlayer().level().registryAccess());
        event.setOutput(output);
        event.setCost(ChainlinedConfig.anvilExperienceCost);
        event.setMaterialCost(1);
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack crafted = event.getCrafting();
        if (!(crafted.getItem() instanceof ArmorItem craftedArmor)) {
            return;
        }
        if (ChainlinedHelper.hasChainmailed(crafted)) {
            return;
        }
        if (ChainlinedConfig.chainmailedBlacklist.contains(craftedArmor)) {
            return;
        }
        Container matrix = event.getInventory();
        for (int i = 0; i < matrix.getContainerSize(); i++) {
            ItemStack ingredient = matrix.getItem(i);
            if (!(ingredient.getItem() instanceof ArmorItem ingredientArmor)) {
                continue;
            }
            if (ingredientArmor.getType().getSlot() != craftedArmor.getType().getSlot()) {
                continue;
            }
            if (!ChainlinedHelper.hasChainmailed(ingredient)) {
                continue;
            }
            ItemStack snapshot = ChainlinedHelper.copyChainmailed(ingredient, event.getEntity().level().registryAccess());
            if (snapshot.isEmpty()) {
                continue;
            }
            ChainlinedHelper.storeChainmailed(crafted, snapshot, event.getEntity().level().registryAccess());
            return;
        }
    }

    @SubscribeEvent
    public static void onItemAttributeModifier(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof ArmorItem armor)) {
            return;
        }
        boolean chainmail = armor.getMaterial().is(ArmorMaterials.CHAIN);
        if (!chainmail && !ChainlinedHelper.hasChainmailed(stack)) {
            return;
        }

        EquipmentSlot slot = armor.getType().getSlot();
        int bonus = armorForSlot(slot);
        int defense = chainmail ? bonus : armor.getDefense() + bonus;
        ResourceLocation id = ResourceLocation.withDefaultNamespace("armor." + armor.getType().getName());
        EquipmentSlotGroup group = EquipmentSlotGroup.bySlot(slot);

        event.removeAllModifiersFor(Attributes.ARMOR);
        event.addModifier(Attributes.ARMOR, new AttributeModifier(id, defense, AttributeModifier.Operation.ADD_VALUE), group);
    }

    public static int armorForSlot(EquipmentSlot slot) {
        int[] table = ChainlinedConfig.armorForSlotChainmailedBasic;
        int index = slot.getIndex();
        return index >= 0 && index < table.length ? table[index] : 0;
    }
}
