package dev.fealtous.minorconvenience.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.fealtous.minorconvenience.convenience.WaypointsHandler;
import dev.fealtous.minorconvenience.dungeons.DungeonsHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;
import java.util.UUID;


public class ClientCommands {
    @SubscribeEvent
    public static void register(RegisterClientCommandsEvent e) {
        e.getDispatcher().register(Commands.literal("resetblaze").executes((x)-> {
            DungeonsHandler.resetBlazes();
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Successfully reset blazes."));

            return Command.SINGLE_SUCCESS;
        }));

        e.getDispatcher().register(Commands.literal("lookup")
                .then(Commands.argument("playername", EntityArgument.player()).executes(ctx -> {
                    Minecraft.getInstance().player.sendSystemMessage(Component.literal("Looking up " + EntityArgument.getPlayer(ctx, "playername").getUUID()));
                    return Command.SINGLE_SUCCESS;
                })));

        e.getDispatcher().register(Commands.literal("waypoint")
                .then(Commands.literal("create").executes((ctx) -> {
                    WaypointsHandler.addWP(ctx.getSource().getEntity().getOnPos(), "test");
                    return Command.SINGLE_SUCCESS;
                }))
                .then(Commands.literal("delete").executes((ctx) -> {
                    Minecraft.getInstance().player.sendSystemMessage(Component.literal("deleted waypoint"));
                    return Command.SINGLE_SUCCESS;
                })));
    }

    private static Optional<UUID> optionalUUIDOf(String player) {
        Optional<AbstractClientPlayer> p = Minecraft.getInstance().level.players().stream().filter((x) -> x.getDisplayName().getString().equals(player)).findFirst();
        return p.map(Entity::getUUID);
    }
}
