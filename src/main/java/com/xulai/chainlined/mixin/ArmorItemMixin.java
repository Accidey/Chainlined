package com.xulai.chainlined.mixin;

import com.xulai.chainlined.common.ChainlinedEvents;
import com.xulai.chainlined.common.ChainlinedHelper;
import com.xulai.chainlined.common.api.IReinforcedChain;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ArmorItem.class)
public abstract class ArmorItemMixin extends Item implements IReinforcedChain {
    public ArmorItemMixin(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        ArmorItem armor = (ArmorItem) (Object) this;
        int bonus = ChainlinedEvents.armorForSlot(armor.getType().getSlot());
        if (armor.getMaterial().is(ArmorMaterials.CHAIN)) {
            tooltip.add(Component.translatable("armoritem.chainmailed.chainmail_hint", String.valueOf(bonus)).withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("armoritem.chainmailed.chainmail_hint_slot").withStyle(ChatFormatting.DARK_GRAY));
            tooltip.add(Component.translatable("armoritem.chainmailed.chainmail_hint_cost").withStyle(ChatFormatting.DARK_GRAY));
        }
        if (hasChainmailed(stack)) {
            tooltip.add(Component.translatable("armoritem.chainmailed.has_chainmailed").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("armoritem.chainmailed.extra_armor", String.valueOf(bonus)).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public boolean hasChainmailed(ItemStack stack) {
        return ChainlinedHelper.hasChainmailed(stack);
    }
}
