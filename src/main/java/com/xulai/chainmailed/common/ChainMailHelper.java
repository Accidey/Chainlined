package com.xulai.chainmailed.common;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.Nullable;

public final class ChainMailHelper {
    public static final String REINFORCED_CHAIN_KEY = "reinforcedChain";

    private ChainMailHelper() {
    }

    public static boolean hasChainmailed(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null || !data.contains(REINFORCED_CHAIN_KEY)) {
            return false;
        }
        return !data.copyTag().getCompound(REINFORCED_CHAIN_KEY).isEmpty();
    }

    public static void storeChainmailed(ItemStack target, ItemStack chainmail, HolderLookup.Provider registries) {
        if (chainmail.isEmpty()) {
            return;
        }
        CompoundTag snapshot = (CompoundTag) chainmail.save(registries);
        CustomData.update(DataComponents.CUSTOM_DATA, target, tag -> tag.put(REINFORCED_CHAIN_KEY, snapshot));
    }

    @Nullable
    public static ItemStack takeChainmailed(ItemStack target, HolderLookup.Provider registries) {
        CustomData data = target.get(DataComponents.CUSTOM_DATA);
        if (data == null || !data.contains(REINFORCED_CHAIN_KEY)) {
            return null;
        }
        CompoundTag snapshot = data.copyTag().getCompound(REINFORCED_CHAIN_KEY);
        CustomData.update(DataComponents.CUSTOM_DATA, target, tag -> tag.remove(REINFORCED_CHAIN_KEY));
        ItemStack chainmail = ItemStack.parseOptional(registries, snapshot);
        return chainmail.isEmpty() ? null : chainmail;
    }

    public static boolean isChainmailedPiece(ItemStack stack) {
        if (!(stack.getItem() instanceof ArmorItem armor)) {
            return false;
        }
        return hasChainmailed(stack) || armor.getMaterial().is(ArmorMaterials.CHAIN);
    }

    public static boolean hasFullChainmailedSet(LivingEntity entity) {
        for (ItemStack stack : entity.getArmorSlots()) {
            if (!isChainmailedPiece(stack)) {
                return false;
            }
        }
        return true;
    }
}
