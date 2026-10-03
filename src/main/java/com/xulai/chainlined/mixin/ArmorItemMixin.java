package com.xulai.chainlined.mixin;

import com.xulai.chainlined.ChainlinedConfig;
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
        boolean chainmail = armor.getMaterial().is(ArmorMaterials.CHAIN);
        boolean lined = hasChainmailed(stack);
        if (chainmail) {
            tooltip.add(Component.translatable("armoritem.chainmailed.chainmail_base").withStyle(ChatFormatting.GRAY));
        }
        if (lined) {
            tooltip.add(Component.translatable("armoritem.chainmailed.has_chainmailed").withStyle(ChatFormatting.GRAY));
            int bonus = ChainlinedEvents.armorForSlot(armor.getType().getSlot());
            tooltip.add(Component.translatable("armoritem.chainmailed.extra_armor", String.valueOf(bonus)).withStyle(ChatFormatting.GRAY));
        }
        if (chainmail || lined) {
            tooltip.add(Component.translatable("armoritem.chainmailed.impact_resistance", formatImpact()).withStyle(ChatFormatting.BLUE));
        }
    }

    private String formatImpact() {
        double value = ChainlinedConfig.impactResistanceChainmailedBasic;
        if (value == Math.floor(value)) {
            return String.valueOf((int) value);
        }
        return String.valueOf(value);
    }

    @Override
    public boolean hasChainmailed(ItemStack stack) {
        return ChainlinedHelper.hasChainmailed(stack);
    }
}