package dev.fealtous.minorconvenience.utils;

import java.util.function.BiFunction;
import java.util.function.Function;

public enum Location {

    UNKNOWN("UNKNOWN", null, null),
    NONE("NONE", null, null),

    JERRY_ISLAND("Jerrys Island", NONE, String::equals),

    CATACOMBS("The Catacombs", NONE, String::contains),

    DUNGEON_HUB("Dungeon Hub", NONE, String::equals),

    GOLD_MINE("Gold Mine", NONE, String::equals),

    ISLAND("Your Island", NONE, String::equals),
    GARDEN("The Garden", ISLAND, String::contains),
    PLOT("Plot", ISLAND, String::contains),

    HUB("The Hub", NONE, String::equals),
    FOREST("Forest", HUB, String::equals),
    RUINS("Ruins", HUB, String::equals),
    HIGH_LEVEL("Unincorporated", HUB, String::equals),
    FARM("Farm", HUB, String::equals),
    VILLAGE("Village", HUB, String::equals),
    COLOSSEUM("Colosseum", HUB, String::equals),
    FLOWER_HOUSE("Flower House", HUB, String::equals),
    MUSEUM("Museum", HUB, String::equals),
    MOUNTAIN("Mountain", HUB, String::equals),
    KAT("Pet Care", HUB, String::equals),
    COMMUNITY("Community Center", HUB, String::equals),
    GRAVEYARD("Graveyard", HUB, String::equals),
    WILDERNESS("Wilderness", HUB, String::equals),
    FISHERMAN("Fishermans Hut", HUB, String::equals),
    CANVAS("Canvas Room", HUB, String::equals),
    BANK("Bank", HUB, String::equals),
    AUCTION_HOUSE("Auction House", HUB, String::equals),
    BAZAAR_ALLEY("Bazaar Alley", HUB, String::equals),
    SHENS_AUCTION("Shens Auction", HUB, String::equals),
    SHENS_REGALIA("Shens Regalia", HUB, String::equals),
    FARMHOUSE("Farmhouse", HUB, String::equals),
    COAL_MINE("Coal Mine", HUB, String::equals),

    END("The End", NONE, String::equals),
    DRAGON("Dragons Nest", END, String::equals),
    BRUISERS("Bruiser Hideout", END, String::equals),
    VOID_SLATE("Void Slate", END, String::equals),

    THE_PARK("The Park", NONE, String::equals),
    BIRCH("Birch Park", THE_PARK, String::equals),
    SPRUCE("Spruce Woods", THE_PARK, String::equals),
    ACACIA("Savanna Woodland", THE_PARK, String::equals),
    DARK_OAK("Dark Thicket", THE_PARK, String::equals),
    _JUNGLE("Jungle Island", THE_PARK, String::equals),
    HOWLING_CAVE("Howling Cave", THE_PARK, String::equals),
    LONELY_ISLAND("Lonely Island", THE_PARK, String::equals),
    MELDOYS_PLATEAU("Melodys Plateau", THE_PARK, String::equals),

    DEEP_CAVERN("Deep Caverns", NONE, String::equals),
    GUNPOWDER_MINES("Gunpowder Mines", DEEP_CAVERN, String::equals),
    LAPIS_QUARRY("Lapis Quarry", DEEP_CAVERN, String::equals),
    DIAMOND_RESERVE("Diamond Reserve", DEEP_CAVERN, String::equals),
    PIGMENS_DEN("Pigmens Den", DEEP_CAVERN, String::equals),
    SLIMEHILL("Slimehill", DEEP_CAVERN, String::equals),
    OBSIDIAN_SANCTUARY("Obsidian Sanctuary", DEEP_CAVERN, String::equals),

    NETHER("Crimson Isle", NONE, String::equals),
    MYSTIC_MARSH("Mystic Marsh", NETHER, String::equals),
    BURNING_DESERT("Burning Desert", NETHER, String::equals),
    SMOLDERING_TOMB("Smoldering Tomb", NETHER, String::equals),
    CRIMSON_ISLE("Crimson Isle", NETHER, String::equals),
    SCARLETON("Scarleton", NETHER, String::equals),
    DRAGONTAIL("Dragontail", NETHER, String::equals),
    BLAZING_VOLCANO("Blazing Volcano", NETHER, String::equals),
    THE_WASTELAND("The Wasteland", NETHER, String::equals),
    STRONGHOLD("Stronghold", NETHER, String::equals),
    FORGOTTEN_SKULL("Forgotten Skull", NETHER, String::equals),
    DOJO("Dojo", NETHER, String::equals),
    CATHEDRAL("Cathedral", NETHER, String::equals),
    COURTYARD("Courtyard", NETHER, String::equals),
    CRIMSON_FIELDS("Crimson Fields", NETHER, String::equals),
    MATRIARCH("Matriarchs Lair", NETHER, String::equals),
    MAGMA_BOSS("Magma Chamber", NETHER, String::equals),
    VORE("Belly of the Beast", NETHER, String::equals),
    SQUID("Plhlegblast Pool", NETHER, String::equals),

    HOLLOWS_GENERIC("Crystal Hollows", NONE, String::equals),
    PRECURSOR_REMNANTS("Precursor Remnants", HOLLOWS_GENERIC, String::equals),
    GOBLIN_HOLDOUT("Goblin Holdout", HOLLOWS_GENERIC, String::equals),
    MITHRIL_DEPOSITS("Mithril Deposits", HOLLOWS_GENERIC, String::equals),
    NUCLEUS("Crystal Nucleus", HOLLOWS_GENERIC, String::equals),
    DIVAN("Mines of Divan", HOLLOWS_GENERIC, String::equals),
    LOST_CITY("Lost Precursor City", HOLLOWS_GENERIC, String::equals),
    JUNGLE("Jungle", HOLLOWS_GENERIC, String::equals),
    TEMPLE("Jungle Temple", HOLLOWS_GENERIC, String::equals),
    LIGMA_QUEEN("Goblin Queens Den", HOLLOWS_GENERIC, String::equals),
    MAGMA_FIELDS("Magma Fields", HOLLOWS_GENERIC, String::equals),
    BAL("Khazad", HOLLOWS_GENERIC, String::contains),
    GROTTO("Fairy Grotto", HOLLOWS_GENERIC, String::equals),

    FARMING_ISLANDS("Farming Islands", NONE, String::equals),
    THE_BARN("The Barn", FARMING_ISLANDS, String::equals),
    WINDMILL("Windmill", FARMING_ISLANDS, String::equals),
    MUSHROOM_DESERT("Mushroom Desert", FARMING_ISLANDS, String::equals),
    DESERT_SETTLEMENT("Desert Settlement", FARMING_ISLANDS, String::equals),
    OASIS("Oasis", FARMING_ISLANDS, String::equals),
    DESERT_MOUNTAIN("Desert Mountain", FARMING_ISLANDS, String::equals),
    TRAPPERS_DEN("Trappers Den", FARMING_ISLANDS, String::equals),
    MUSHROOM_GORGE("Mushroom Gorge", FARMING_ISLANDS, String::equals),
    GLOWING_MUSHROOM_CAVE("Glowing Mushroom Cave", FARMING_ISLANDS, String::equals),
    OVERGROWN_MUSHROOM_CAVE("Overgrown Mushroom Cave", FARMING_ISLANDS, String::equals),
    JAKES_HOUSE("Jakes House", FARMING_ISLANDS, String::equals),

    DWARVEN_GENERIC("Dwarven Mines", NONE, String::equals),
    DWARVEN_VILLAGE("Dwarven Village", DWARVEN_GENERIC, String::equals),
    DWARVEN_MINES("Dwarven Mines", DWARVEN_GENERIC, String::equals),
    THE_LIFT("The Lift", DWARVEN_GENERIC, String::equals),
    LAVA_SPRINGS("Lava Springs", DWARVEN_GENERIC, String::equals),
    PALACE_BRIDGE("Palace Bridge", DWARVEN_GENERIC, String::equals),
    ROYAL_PALACE("Royal Palace", DWARVEN_GENERIC, String::equals),
    GRAND_LIBRARY("Grand Library", DWARVEN_GENERIC, String::equals),
    ROYAL_QUARTERS("Royal Quarters", DWARVEN_GENERIC, String::equals),
    BARRACKS_OF_HEROES("Barracks of Heroes", DWARVEN_GENERIC, String::equals),
    HANGING_COURT("Hanging Court", DWARVEN_GENERIC, String::equals),
    GREAT_ICE_WALL("Great Ice Wall", DWARVEN_GENERIC, String::equals),
    DIVANS_GATEWAY("Divans Gateway", DWARVEN_GENERIC, String::equals),
    CLIFFSIDE_VEINS("Cliffside Veins", DWARVEN_GENERIC, String::equals),
    RAMPARTS_QUARRY("Ramparts Quarry", DWARVEN_GENERIC, String::equals),
    FAR_RESERVE("Far Reserve", DWARVEN_GENERIC, String::equals),
    GOBLIN_BURROWS("Goblin Burrows", DWARVEN_GENERIC, String::equals),
    THE_MIST("The Mist", DWARVEN_GENERIC, String::equals),
    UPPER_MINES("Upper Mines", DWARVEN_GENERIC, String::equals),
    FORGE_BASIN("Forge Basin", DWARVEN_GENERIC, String::equals),
    THE_FORGE("The Forge", DWARVEN_GENERIC, String::equals),
    ROYAL_MINES("Royal Mines", DWARVEN_GENERIC, String::equals),
    ARISTOCRAT_PASSAGE("Aristocrat Passage", DWARVEN_GENERIC, String::equals),
    GLACITE_MINESHAFT("Glacite Mineshafts", DWARVEN_GENERIC, String::equals),
    TUNNELS("Glacite Tunnels", DWARVEN_GENERIC, String::equals),
    CAMP("Dwarven Base Camp", DWARVEN_GENERIC, String::equals),
    GLACITE_LAKE("Glacite Lake", DWARVEN_GENERIC, String::equals),

    SPIDERS_DEN("Spiders Den", NONE, String::equals),
    ARACHNE_BURROW("Arachnes Burrow", SPIDERS_DEN, String::equals),
    ARACHNE_CHAMBER("Arachnes Chamber", SPIDERS_DEN, String::equals),
    GRANDMA_HOUSE("Grandmas House", SPIDERS_DEN, String::equals),
    SPIDER_MOUND("Spider Mound", SPIDERS_DEN, String::equals),

    ;

    private final String name;
    private final Location parentZone;
    private final BiFunction<String, String, Boolean> pred;
    Location(String name, Location parent, BiFunction<String, String, Boolean> predicate) {
        this.name = name;
        parentZone = parent;
        this.pred = predicate;
    }

    public Location getParentZone() {
        return parentZone;
    }

    public String getName() {
        return name;
    }
    public boolean hasParent() {
        return parentZone != null;
    }
    public boolean compare(String other) {
        return pred != null && pred.apply(other.toLowerCase(), name.toLowerCase());
    }

}
