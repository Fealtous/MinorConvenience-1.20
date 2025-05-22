package dev.fealtous.minorconvenience.utils;

import dev.fealtous.minorconvenience.MinorConvenience;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;

import java.util.LinkedList;
import java.util.List;

public class TextUtils {
    public static List<String> getPlayerNameList(){
        PlayerTabOverlay playerList = Minecraft.getInstance().gui.getTabList();
        List<String> list = new LinkedList<>();
        for (PlayerInfo pinfo : Minecraft.getInstance().player.connection.getListedOnlinePlayers()) {
            String s = playerList.getNameForDisplay(pinfo).getString();
            list.add(s);
        }
        return list;
    }
    public static void sendIngameMessage(String text) {
        Minecraft.getInstance().gui.getChat().addMessage(Component.literal(text));
    }
    public static String clean(Component msg) {
        return msg.getString().replaceAll("§.", "");
    }

    public static ResourceLocation rl(String name) {
        return ResourceLocation.fromNamespaceAndPath(MinorConvenience.MODID, name);
    }
}
