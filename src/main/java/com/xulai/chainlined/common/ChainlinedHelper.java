package com.xulai.chainlined.common;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
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
        return !data.copyTag().getCompoundOrEmpty(REINFORCED_CHAIN_KEY).isEmpty();
    }

    public static void storeChainmailed(ItemStack target, ItemStack chainmail, RegistryAccess registries) {
        if (chainmail.isEmpty()) {
            return;
        }
        CompoundTag snapshot = toTag(chainmail.copyWithCount(1), registries);
        CustomData.update(DataComponents.CUSTOM_DATA, target, tag -> tag.put(REINFORCED_CHAIN_KEY, snapshot));
    }

    @Nullable
    public static ItemStack takeChainmailed(ItemStack target, RegistryAccess registries) {
        CustomData data = target.get(DataComponents.CUSTOM_DATA);
        if (data == null || !data.contains(REINFORCED_CHAIN_KEY)) {
            return null;
        }
        CompoundTag snapshot = data.copyTag().getCompoundOrEmpty(REINFORCED_CHAIN_KEY);
        CustomData.update(DataComponents.CUSTOM_DATA, target, tag -> tag.remove(REINFORCED_CHAIN_KEY));
        ItemStack chainmail = fromTag(snapshot, registries);
        return chainmail.isEmpty() ? null : chainmail;
    }

    @Nullable
    public static ItemStack copyChainmailed(ItemStack source, RegistryAccess registries) {
        CustomData data = source.get(DataComponents.CUSTOM_DATA);
        if (data == null || !data.contains(REINFORCED_CHAIN_KEY)) {
            return null;
        }
        CompoundTag snapshot = data.copyTag().getCompoundOrEmpty(REINFORCED_CHAIN_KEY);
        return fromTag(snapshot, registries);
    }

    @Nullable
    public static EquipmentSlot armorSlot(ItemStack stack) {
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        if (equippable == null || equippable.slot().getType() != EquipmentSlot.Type.HUMANOID_ARMOR) {
            return null;
        }
        ItemAttributeModifiers modifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
        for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
            if (entry.attribute().equals(Attributes.ARMOR)) {
                return equippable.slot();
            }
        }
        return null;
    }

    public static boolean isChainmail(ItemStack stack) {
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        if (equippable == null) {
            return false;
        }
        return equippable.assetId().map(EquipmentAssets.CHAINMAIL::equals).orElse(false);
    }

    private static CompoundTag toTag(ItemStack stack, RegistryAccess registries) {
        Tag encoded = ItemStack.CODEC.encodeStart(ops(registries), stack).getOrThrow();
        return encoded instanceof CompoundTag tag ? tag : new CompoundTag();
    }

    private static ItemStack fromTag(CompoundTag tag, RegistryAccess registries) {
        return ItemStack.CODEC.parse(ops(registries), tag).result().orElse(ItemStack.EMPTY);
    }

    private static RegistryOps<Tag> ops(RegistryAccess registries) {
        return registries.createSerializationContext(NbtOps.INSTANCE);
    }
}
