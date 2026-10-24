package com.xulai.chainlined.common;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.xulai.chainlined.Chainlined;
import com.xulai.chainlined.ChainlinedConfig;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = Chainlined.MODID)
public final class ChainlinedCommands {
    private ChainlinedCommands() {
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandBuildContext context = event.getBuildContext();
        event.getDispatcher().register(
                Commands.literal("chainlined")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("blacklist")
                                .then(Commands.literal("add")
                                        .executes(ctx -> applyHeld(ctx.getSource(), true))
                                        .then(Commands.argument("item", ResourceArgument.resource(context, Registries.ITEM))
                                                .executes(ctx -> apply(ctx.getSource(), argumentItem(ctx), true))))
                                .then(Commands.literal("remove")
                                        .executes(ctx -> applyHeld(ctx.getSource(), false))
                                        .then(Commands.argument("item", ResourceArgument.resource(context, Registries.ITEM))
                                                .executes(ctx -> apply(ctx.getSource(), argumentItem(ctx), false))))));
    }

    private static Item argumentItem(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return ResourceArgument.getResource(context, "item", Registries.ITEM).value();
    }

    private static int applyHeld(CommandSourceStack source, boolean add) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) {
            source.sendFailure(Component.translatable("commands.chainmailed.blacklist.empty"));
            return 0;
        }
        return apply(source, stack.getItem(), add);
    }

    private static int apply(CommandSourceStack source, Item item, boolean add) {
        boolean changed = add ? ChainlinedConfig.addToBlacklist(item) : ChainlinedConfig.removeFromBlacklist(item);
        String key;
        if (add) {
            key = changed ? "commands.chainmailed.blacklist.added" : "commands.chainmailed.blacklist.present";
        } else {
            key = changed ? "commands.chainmailed.blacklist.removed" : "commands.chainmailed.blacklist.absent";
        }
        Component message = Component.translatable(key, item.getDescription());
        if (changed) {
            source.sendSuccess(() -> message, true);
            return Command.SINGLE_SUCCESS;
        }
        source.sendFailure(message);
        return 0;
    }
}