package dev.fealtous.minorconvenience.commands;

import com.mojang.brigadier.Command;
import dev.fealtous.minorconvenience.dungeons.DungeonsHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientCommands {
    @SubscribeEvent
    public static void register(RegisterClientCommandsEvent e) {
        e.getDispatcher().register(Commands.literal("resetblaze").executes((x)-> {
            DungeonsHandler.resetBlazes();
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Successfully reset blazes."));
            return Command.SINGLE_SUCCESS;
        }));
    }
}
