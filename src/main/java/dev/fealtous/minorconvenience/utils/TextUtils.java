package dev.fealtous.minorconvenience.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
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
    public static String cleanColorCodes(String t) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] cleaned = t.toCharArray();
        boolean flag = false;
        for (char c : cleaned) {
            if ((int) c == 167) {
                flag = true;
                continue;
            }
            if (flag) {
                flag = false;
                continue;
            }
            if ((int) c > 20 && (int) c < 127) {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }
    public static String isolateNumbers(String t) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] iso = t.toCharArray();
        for (char c : iso) {
            if (c >= 0x30 && c <= 0x39) {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }
    public static String cleanItemName(Slot s) {
        return cleanColorCodes(s.getItem().getDisplayName().getString());
    }
}
