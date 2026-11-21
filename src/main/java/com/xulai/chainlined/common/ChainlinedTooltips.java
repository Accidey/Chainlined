package com.xulai.chainlined.common;

import com.xulai.chainlined.Chainlined;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.tooltip.TooltipLocation;
import net.neoforged.neoforge.event.RegisterTooltipAppendersEvent;

@EventBusSubscriber(modid = Chainlined.MODID)
public final class ChainlinedTooltips {
    private ChainlinedTooltips() {
    }

    @SubscribeEvent
    public static void onRegisterTooltipAppenders(RegisterTooltipAppendersEvent event) {
        event.registerAppender(TooltipLocation.TAIL, ChainlinedTooltips::append);
    }

    private static void append(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Player player, TooltipFlag flag, Consumer<Component> tooltip) {
        EquipmentSlot slot = ChainlinedHelper.armorSlot(stack);
        if (slot == null) {
            return;
        }
        int bonus = ChainlinedEvents.armorForSlot(slot);
        if (ChainlinedHelper.isChainmail(stack)) {
            tooltip.accept(Component.translatable("armoritem.chainmailed.chainmail_hint", String.valueOf(bonus)).withStyle(ChatFormatting.GRAY));
            tooltip.accept(Component.translatable("armoritem.chainmailed.chainmail_hint_slot").withStyle(ChatFormatting.DARK_GRAY));
            tooltip.accept(Component.translatable("armoritem.chainmailed.chainmail_hint_cost").withStyle(ChatFormatting.DARK_GRAY));
        }
        if (ChainlinedHelper.hasChainmailed(stack)) {
            tooltip.accept(Component.translatable("armoritem.chainmailed.has_chainmailed").withStyle(ChatFormatting.GRAY));
            tooltip.accept(Component.translatable("armoritem.chainmailed.extra_armor", String.valueOf(bonus)).withStyle(ChatFormatting.GRAY));
        }
    }
}
