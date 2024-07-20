package dev.fealtous.minorconvenience.commands;


import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.fealtous.minorconvenience.convenience.WaypointsHandler;
import dev.fealtous.minorconvenience.dungeons.*;
import dev.fealtous.minorconvenience.utils.LocatorUtil;
import dev.fealtous.minorconvenience.utils.network.CustomReader;
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

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class ClientCommands {
    @SubscribeEvent
    public static void register(RegisterClientCommandsEvent e) {
        e.getDispatcher().register(Commands.literal("resetblaze").executes((x)-> {
            DungeonsHandler.resetBlazes();
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Successfully reset blazes."));

            return SINGLE_SUCCESS;
        }));

        e.getDispatcher().register(Commands.literal("lookup")
                .then(Commands.argument("playername", StringArgumentType.string())
                        .executes(ctx -> {
                            var pname = ctx.getArgument("playername", String.class);
                            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Looking up " + pname));
                            return SINGLE_SUCCESS;
                })));

        e.getDispatcher().register(Commands.literal("waypoint")
                .then(Commands.literal("create").executes((ctx) -> {
                    WaypointsHandler.addWP(ctx.getSource().getEntity().getOnPos(), "test");
                    return SINGLE_SUCCESS;
                }))
                .then(Commands.literal("delete").executes((ctx) -> {
                    Minecraft.getInstance().player.sendSystemMessage(Component.literal("deleted waypoint"));
                    return SINGLE_SUCCESS;
                })));
        e.getDispatcher().register(Commands.literal("coords")
                .executes((ctx) -> {
                    sayPosition();
                    return SINGLE_SUCCESS;
                }));
        e.getDispatcher().register(Commands.literal("readPos")
                .executes((ctx) -> {
                    DungeonRoomIdentifier.identifyRoom();
                    return SINGLE_SUCCESS;
                }));

        e.getDispatcher().register(Commands.literal("secret")
                .then(Commands.literal("start")
                        .then(Commands.argument("roomName", StringArgumentType.string())
                                .executes(ctx -> {
                                    RoomDefiner.create(ctx.getArgument("roomName", String.class), DungeonRoomIdentifier.currentRoomHash);
                                    return SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("build")
                        .executes(ctx -> {
                            RoomDefiner.build();
                            return SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("solved")
                        .then(Commands.argument("hash", IntegerArgumentType.integer())
                                .executes(ctx -> {
                                    RoomDefiner.solveVia(ctx.getArgument("hash", Integer.class));
                                    return SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("load")
                        .executes(ctx -> {
                            RoomDefiner.load();
                            return SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("save")
                        .executes(ctx -> {
                            RoomDefiner.save();
                            return SINGLE_SUCCESS;
                        }))
        );
        var secretCmd = Commands.literal("ss");
        for (POIType value : POIType.values()) {
            secretCmd = secretCmd.then(Commands.literal(value.name().toLowerCase())
                    .executes(ctx -> {
                        RoomDefiner.addPOI(value);
                        return SINGLE_SUCCESS;
                    }));
        }
        e.getDispatcher().register(secretCmd);
    }

    private static Optional<UUID> optionalUUIDOf(String player) {
        Optional<AbstractClientPlayer> p = Minecraft.getInstance().level.players().stream().filter((x) -> x.getDisplayName().getString().equals(player)).findFirst();
        return p.map(Entity::getUUID);
    }

    private static void sayPosition() {
        var pos = Minecraft.getInstance().player.position();
        var loc = LocatorUtil.whereAmI().name();
        Minecraft.getInstance().player.connection.sendChat(String.format("x:%.0f y:%.0f z:%.0f @ %s", pos.x, pos.y, pos.z, loc));
    }
}
