package dev.fealtous.minorconvenience.utils;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Stream;

import static dev.fealtous.minorconvenience.utils.Location.*;

public class LocatorUtil {
    private static Location current = UNKNOWN;
    private static final HashSet<Integer> ignoreList = new HashSet<>();
    private static final HashMap<Integer, Pair<Location, String>> knownList = new HashMap<>();

    public static void update() {
        Scoreboard sideBar = Minecraft.getInstance().level.getScoreboard();
        for (PlayerTeam playerTeam : sideBar.getPlayerTeams()) {
            String str = (playerTeam.getPlayerPrefix().getString() + playerTeam.getPlayerSuffix().getString()).trim();
            if (str.contains("⏣")) {
                str = str.replaceAll("[^a-zA-Z\\s]", "");
                int hash = str.hashCode();
                if (ignoreList.contains(hash)) return;
                if (knownList.get(hash) != null) {
                    current = knownList.get(hash).key();
                    return;
                }
                // Oh boy I sure do love else if chains :)))))))
                // which is why i'm saying fuck it we hashing, skip the entire fucking thing if possible.
                if (str.contains("Plot") || str.contains("Garden")) {
                    set(hash, GARDEN, str);
                } else if (str.contains("Jungle Temple")) {
                    set(hash, TEMPLE, str);
                } else if (Stream.of("Precursor Remnants", "Goblin Holdout", "Mithril Deposits").anyMatch(str::contains) || str.equals("Jungle")) {
                    set(hash, HOLLOWS_GENERIC, str);
                } else if (str.contains("Lost Precursor City")) {
                    set(hash, LOST_CITY, str);
                } else if (str.contains("Mines of Divan")) {
                    set(hash, DIVAN, str);
                } else if (str.contains("Magma Fields")) {
                    set(hash, MAGMA_FIELDS, str);
                } else if (str.contains("Khazad")) {
                    set(hash, BAL, str);
                } else if (str.contains("Goblin Queen")) {
                    set(hash, LIGMA_QUEEN, str);
                } else if (str.contains("Crystal Nucleus")) {
                    set(hash, NUCLEUS, str);
                } else if (str.contains("Your Island")) {
                    set(hash, ISLAND, str);
                } else if (str.contains("Base Camp")) {
                    set(hash, TUNNELS, str);
                } else if (str.contains("Mineshaft")) {
                    set(hash, GLACITE_MINESHAFT, str);
                } else if (Stream.of("The Barn", "Windmill", "Mushroom Desert",
                        "Desert Settlement", "Oasis", "Desert Mountain",
                        "Trappers Den", "Mushroom Gorge", "Glowing Mushroom Cave",
                        "Overgrown Mushroom Cave", "Jakes House").anyMatch(str::contains)) {
                    set(hash, FARMING_ISLANDS, str);
                } else if (Stream.of("Dwarven", "The Lift", "Lava Springs", "Palace Bridge",
                        "Royal Palace", "Grand Library", "Royal Quarters",
                        "Barracks of Heroes", "Hanging Court", "Great Ice Wall",
                        "Divans Gateway", "Cliffside Veins", "Ramparts Quarry",
                        "Far Reserve", "Goblin Burrows", "The Mist",
                        "Upper Mines", "Forge Basin", "The Forge",
                        "Royal Mines", "Aristocrat Passage").anyMatch(str::contains)) {
                    set(hash, DWARVEN_GENERIC, str);
                } else if (Stream.of("Birch Park", "Spruce Woods", "Savanna Woodland",
                        "Dark Thicket", "Jungle Island", "Howling Cave",
                        "Lonely Island", "Melodys Plateau").anyMatch(str::contains)) {
                    set(hash, PARK, str);
                } else if (Stream.of("Forest", "Ruins", "Unincorporated", "Museum", "Mountain",
                        "Village", "Coal Mines", "Flower House", "Colosseum", "Farm",
                        "Pet Care", "Community Center", "Graveyard", "Wilderness",
                        "Fishermans Hut", "Canvas Room", "Bank", "Auction House",
                        "Bazaar Alley", "Shen's Auction", "Shen's Regalia",
                        "Farmhouse").anyMatch(str::contains)) {
                    set(hash, HUB, str);
                } else if (str.contains("Dungeon Hub")) {
                    set(hash, DUNGEON_HUB, str);
                } else if (str.contains("The Catacombs")) {
                    set(hash, CATACOMBS, str);
                } else if (str.contains("Gold Mine")) {
                    set(hash, GOLD_MINE, str);
                } else if (Stream.of("Deep Caverns", "Gunpowder Mines", "Lapis Quarry", "Pigmens Den", "Slimehill",
                        "Diamond Reserve", "Obsidian Sanctuary").anyMatch(str::contains)) {
                    set(hash, DEEP_CAVERN, str);
                } else if (Stream.of("The End", "Dragons Nest", "Bruiser Hideout").anyMatch(str::contains)) {
                    set(hash, END, str);
                } else if (Stream.of("Spiders Den", "Arachne", "Grandma", "Spider Mound").anyMatch(str::contains)) {
                    set(hash, SPIDER, str);
                } else if (Stream.of("Stronghold", "Blazing Volcano", "The Wasteland", "Forgotten Skull",
                        "Scarleton", "Crimson Isle", "Dragontail", "Burning Desert", "Mystic Marsh",
                        "Smoldering Tomb").anyMatch(str::contains)) {
                    set(hash, NETHER, str);
                } else if (str.contains("Jerry")) {
                    set(hash, JERRY_ISLAND, str);
                }
                ignoreList.add(hash); // Not a location I care about or not a location at all
                break;
            }
        }
    }


    public static void _update(Component prefix, Component suffix) {

    }
    private static void set(int hash, Location loc, String str) {
        current = loc;
        knownList.put(hash, Pair.of(loc, str));
    }
    public static Location whereAmI() {
        return current;
    }

}
