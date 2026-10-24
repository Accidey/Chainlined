package com.xulai.chainlined.common;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.Nullable;

public final class ChainlinedHelper {
    public static final String REINFORCED_CHAIN_KEY = "reinforcedChain";

    private ChainlinedHelper() {
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
        ItemStack single = chainmail.copyWithCount(1);
        CompoundTag snapshot = (CompoundTag) single.save(registries);
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

    @Nullable
    public static ItemStack copyChainmailed(ItemStack source, HolderLookup.Provider registries) {
        CustomData data = source.get(DataComponents.CUSTOM_DATA);
        if (data == null || !data.contains(REINFORCED_CHAIN_KEY)) {
            return null;
        }
        CompoundTag snapshot = data.copyTag().getCompound(REINFORCED_CHAIN_KEY);
        return ItemStack.parseOptional(registries, snapshot);
    }
}