package com.xulai.chainlined.mixin;

import com.xulai.chainlined.common.ChainlinedHelper;
import java.util.function.Consumer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(
            method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V", shift = At.Shift.BEFORE))
    private void chainmailed$returnReinforcedChain(int amount, ServerLevel level, LivingEntity holder, Consumer<Item> onBroken, CallbackInfo callback) {
        if (holder == null) {
            return;
        }
        ItemStack replacement = ChainlinedHelper.takeChainmailed((ItemStack) (Object) this, level.registryAccess());
        if (replacement == null || !(replacement.getItem() instanceof ArmorItem armor)) {
            return;
        }
        holder.setItemSlot(armor.getEquipmentSlot(), replacement);
    }
}