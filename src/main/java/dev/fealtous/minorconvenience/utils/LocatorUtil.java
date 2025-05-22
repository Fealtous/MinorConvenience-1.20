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
            String str = (TextUtils.clean(playerTeam.getPlayerPrefix()) + TextUtils.clean(playerTeam.getPlayerSuffix())).trim();
            if (str.startsWith("⏣")) {
                str = str.substring(1).trim();
                if (quickSearch(str)) {
                    break;
                }
                for (Location location : easyRef) {
                    if (location.compare(str)) {
                        current = location;
                        return; // Function is finished, gtfo
                    }
                }
                current = UNKNOWN;
                LogUtils.getLogger().debug(str);
                break; // Didn't find, set to unknown, log so you can drop in later.
            } else if (str.startsWith("ф")) {
                current = RIFT;
                str = str.substring(1).trim();
                for (int i = RIFT.ordinal(); i < easyRef.length; i++) {
                    if (easyRef[i].compare(str)) {
                        current = easyRef[i];
                        return;
                    }
                }
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
            } while (!easyRef[iterator].getParentZone().equals(GLOBAL));
        } catch (Exception e) {
            LogUtils.getLogger().error(String.format("Failed to process: %s", str));
            LogUtils.getLogger().error(String.format("Iterator was at: %d", iterator));
        }
        return false;
    }

    public static boolean isDungeons() {
        return LocatorUtil.isIn(CATACOMBS);
    }
}
