package dev.fealtous.minorconvenience.utils;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LocatorUtil {
    public enum LOCATIONS {CATACOMBS, DWARVEN, HUB, ISLAND, PARK, UNKNOWN};
    public static Object whereAmI() {
        Scoreboard sideBar = Minecraft.getInstance().level.getScoreboard();
        Objective objective = sideBar.getDisplayObjective(1);
        if (objective == null) return LOCATIONS.UNKNOWN;
        Collection<Score> scores = sideBar.getPlayerScores(objective);
        List<Score> list = scores.stream().filter(i -> i != null && i.getOwner() != null && !i.getOwner().startsWith("#")).collect(Collectors.toList());
        if (list.size() > 15) {
            scores = Lists.newArrayList(Iterables.skip(list, scores.size() -15));
        }
        else {
            scores = list;
        }
        for (Score s: scores) {
            PlayerTeam team = sideBar.getPlayersTeam(s.getOwner());
            Component pnamecomponent = Component.literal(s.getOwner());
            if (team == null) continue;
            String sbtext = TextUtils.cleanColorCodes(PlayerTeam.formatNameForTeam(team, pnamecomponent).getString());
            if (sbtext.contains("Catacombs")) return LOCATIONS.CATACOMBS;
            if (sbtext.contains("Your Island")) return LOCATIONS.ISLAND;
            if (sbtext.contains("Divan's")) return LOCATIONS.DWARVEN;
            if (sbtext.contains("Rampart's")) return LOCATIONS.DWARVEN;
            if (sbtext.contains("Upper")) return LOCATIONS.DWARVEN;
            if (sbtext.contains("Royal")) return LOCATIONS.DWARVEN;
            if (sbtext.contains("Village") || sbtext.contains("Graveyard") || sbtext.contains("Coal Mine")) return LOCATIONS.HUB;
            if (sbtext.contains("Forest") || sbtext.contains("Ruins") || sbtext.contains("High Level") || sbtext.contains("Wilderness")) return LOCATIONS.HUB;
            if (sbtext.contains("Farm")) return LOCATIONS.HUB;
        }
        return LOCATIONS.UNKNOWN;
    }
}
