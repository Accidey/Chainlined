package com.xulai.chainlined.mixin;

import com.xulai.chainlined.common.ChainlinedHelper;
import java.util.function.Consumer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(
            method = "applyDamage(ILnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V", shift = At.Shift.BEFORE))
    private void chainlined$returnReinforcedChain(int newDamage, LivingEntity holder, Consumer<ItemStack> onBreak, CallbackInfo callback) {
        if (holder == null) {
            return;
        }
        ItemStack self = (ItemStack) (Object) this;
        EquipmentSlot slot = ChainlinedHelper.armorSlot(self);
        if (slot == null) {
            return;
        }
        ItemStack replacement = ChainlinedHelper.takeChainmailed(self, holder.level().registryAccess());
        if (replacement == null || ChainlinedHelper.armorSlot(replacement) == null) {
            return;
        }
        holder.setItemSlot(slot, replacement);
    }
}
