package dev.fealtous.minorconvenience.utils;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

import static dev.fealtous.minorconvenience.utils.Location.*;
import static dev.fealtous.minorconvenience.utils.Location.DUNGEON_HUB;

public class LocatorUtil {
    private static Location current = UNKNOWN;
    private static final Location[] easyRef = Location.values();

    public static void alert() {
        Scoreboard sideBar = Minecraft.getInstance().level.getScoreboard();
        for (PlayerTeam playerTeam : sideBar.getPlayerTeams()) {
            String str = (playerTeam.getPlayerPrefix().getString() + playerTeam.getPlayerSuffix().getString()).trim();
            if (str.contains("⏣")) {
                str = str.replaceAll("[^a-zA-Z\\s]", "").trim().toLowerCase();
                if (quickSearch(str)) {
                    break;
                }
                for (Location location : easyRef) {
                    //LogUtils.getLogger().debug(location.getName());
                    if (location.compare(str)) {
                        current = location;
                        return; // Function is finished, gtfo
                    }
                }
                current = UNKNOWN;
                LogUtils.getLogger().debug(str);
                break; // Didn't find, set to unknown, log so you can drop in later.
            }
        }
    }
    public static Location whereAmI() {return current;}
    public static boolean isIn(Location loc) {
        return current == loc || current.getParentZone() == loc;
    }

    private static boolean quickSearch(String str) {
        int iterator = -1;
        try {
            if (!current.hasParent()) return false;
            iterator = easyRef[current.ordinal()].getParentZone().ordinal();
            do {
                var ref = easyRef[iterator];
                if (ref.compare(str)) {
                    current = ref;
                    return true;
                }
                iterator++;
            } while (!easyRef[iterator].getParentZone().equals(NONE));
        } catch (Exception e) {
            LogUtils.getLogger().error(String.format("Failed to process: %s", str));
            LogUtils.getLogger().error(String.format("Iterator was at: %d", iterator));
        }
        return false;
    }

    public static boolean isDungeons() {
        return LocatorUtil.isIn(CATACOMBS);
    }
    public static boolean isDHub() {
        return LocatorUtil.isIn(DUNGEON_HUB);
    }
}
