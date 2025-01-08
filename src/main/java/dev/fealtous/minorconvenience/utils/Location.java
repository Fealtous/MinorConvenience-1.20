package dev.fealtous.minorconvenience.utils;

import java.util.function.BiFunction;
import java.util.function.Function;

public enum Location {

    UNKNOWN("UNKNOWN", null, null),
    NONE("NONE", null, null),

    JERRY_ISLAND("Jerrys Island", NONE),

    CATACOMBS("The Catacombs", NONE, String::contains),

    DUNGEON_HUB("Dungeon Hub", NONE),

    GOLD_MINE("Gold Mine", NONE),

    ISLAND("Your Island", NONE),
    GARDEN("The Garden", ISLAND, String::contains),
    PLOT("Plot", ISLAND, String::contains),

    HUB("The Hub", NONE),
    FOREST("Forest", HUB),
    RUINS("Ruins", HUB),
    HIGH_LEVEL("Unincorporated", HUB),
    FARM("Farm", HUB),
    VILLAGE("Village", HUB),
    COLOSSEUM("Colosseum", HUB),
    FLOWER_HOUSE("Flower House", HUB),
    MUSEUM("Museum", HUB),
    MOUNTAIN("Mountain", HUB),
    KAT("Pet Care", HUB),
    COMMUNITY("Community Center", HUB),
    GRAVEYARD("Graveyard", HUB),
    WILDERNESS("Wilderness", HUB),
    FISHERMAN("Fishermans Hut", HUB),
    CANVAS("Canvas Room", HUB),
    BANK("Bank", HUB),
    AUCTION_HOUSE("Auction House", HUB),
    BAZAAR_ALLEY("Bazaar Alley", HUB),
    SHENS_AUCTION("Shens Auction", HUB),
    SHENS_REGALIA("Shens Regalia", HUB),
    FARMHOUSE("Farmhouse", HUB),
    COAL_MINE("Coal Mine", HUB),

    END("The End", NONE),
    DRAGON("Dragons Nest", END),
    BRUISERS("Bruiser Hideout", END),
    VOID_SLATE("Void Slate", END),

    THE_PARK("The Park", NONE),
    BIRCH("Birch Park", THE_PARK),
    SPRUCE("Spruce Woods", THE_PARK),
    ACACIA("Savanna Woodland", THE_PARK),
    DARK_OAK("Dark Thicket", THE_PARK),
    _JUNGLE("Jungle Island", THE_PARK),
    HOWLING_CAVE("Howling Cave", THE_PARK),
    LONELY_ISLAND("Lonely Island", THE_PARK),
    MELDOYS_PLATEAU("Melodys Plateau", THE_PARK),

    DEEP_CAVERN("Deep Caverns", NONE),
    GUNPOWDER_MINES("Gunpowder Mines", DEEP_CAVERN),
    LAPIS_QUARRY("Lapis Quarry", DEEP_CAVERN),
    DIAMOND_RESERVE("Diamond Reserve", DEEP_CAVERN),
    PIGMENS_DEN("Pigmens Den", DEEP_CAVERN),
    SLIMEHILL("Slimehill", DEEP_CAVERN),
    OBSIDIAN_SANCTUARY("Obsidian Sanctuary", DEEP_CAVERN),

    NETHER("Crimson Isle", NONE),
    MYSTIC_MARSH("Mystic Marsh", NETHER),
    BURNING_DESERT("Burning Desert", NETHER),
    SMOLDERING_TOMB("Smoldering Tomb", NETHER),
    CRIMSON_ISLE("Crimson Isle", NETHER),
    SCARLETON("Scarleton", NETHER),
    DRAGONTAIL("Dragontail", NETHER),
    BLAZING_VOLCANO("Blazing Volcano", NETHER),
    THE_WASTELAND("The Wasteland", NETHER),
    STRONGHOLD("Stronghold", NETHER),
    FORGOTTEN_SKULL("Forgotten Skull", NETHER),
    DOJO("Dojo", NETHER),
    CATHEDRAL("Cathedral", NETHER),
    COURTYARD("Courtyard", NETHER),
    CRIMSON_FIELDS("Crimson Fields", NETHER),
    MATRIARCH("Matriarchs Lair", NETHER),
    MAGMA_BOSS("Magma Chamber", NETHER),
    VORE("Belly of the Beast", NETHER),
    SQUID("Plhlegblast Pool", NETHER),

    HOLLOWS_GENERIC("Crystal Hollows", NONE),
    PRECURSOR_REMNANTS("Precursor Remnants", HOLLOWS_GENERIC),
    GOBLIN_HOLDOUT("Goblin Holdout", HOLLOWS_GENERIC),
    MITHRIL_DEPOSITS("Mithril Deposits", HOLLOWS_GENERIC),
    NUCLEUS("Crystal Nucleus", HOLLOWS_GENERIC),
    DIVAN("Mines of Divan", HOLLOWS_GENERIC),
    LOST_CITY("Lost Precursor City", HOLLOWS_GENERIC),
    JUNGLE("Jungle", HOLLOWS_GENERIC),
    TEMPLE("Jungle Temple", HOLLOWS_GENERIC),
    LIGMA_QUEEN("Goblin Queens Den", HOLLOWS_GENERIC),
    MAGMA_FIELDS("Magma Fields", HOLLOWS_GENERIC),
    BAL("Khazad", HOLLOWS_GENERIC, String::contains),
    GROTTO("Fairy Grotto", HOLLOWS_GENERIC),

    FARMING_ISLANDS("Farming Islands", NONE),
    THE_BARN("The Barn", FARMING_ISLANDS),
    WINDMILL("Windmill", FARMING_ISLANDS),
    MUSHROOM_DESERT("Mushroom Desert", FARMING_ISLANDS),
    DESERT_SETTLEMENT("Desert Settlement", FARMING_ISLANDS),
    OASIS("Oasis", FARMING_ISLANDS),
    DESERT_MOUNTAIN("Desert Mountain", FARMING_ISLANDS),
    TRAPPERS_DEN("Trappers Den", FARMING_ISLANDS),
    MUSHROOM_GORGE("Mushroom Gorge", FARMING_ISLANDS),
    GLOWING_MUSHROOM_CAVE("Glowing Mushroom Cave", FARMING_ISLANDS),
    OVERGROWN_MUSHROOM_CAVE("Overgrown Mushroom Cave", FARMING_ISLANDS),
    JAKES_HOUSE("Jakes House", FARMING_ISLANDS),

    DWARVEN_GENERIC("Dwarven Mines", NONE),
    DWARVEN_VILLAGE("Dwarven Village", DWARVEN_GENERIC),
    DWARVEN_MINES("Dwarven Mines", DWARVEN_GENERIC),
    THE_LIFT("The Lift", DWARVEN_GENERIC),
    LAVA_SPRINGS("Lava Springs", DWARVEN_GENERIC),
    PALACE_BRIDGE("Palace Bridge", DWARVEN_GENERIC),
    ROYAL_PALACE("Royal Palace", DWARVEN_GENERIC),
    GRAND_LIBRARY("Grand Library", DWARVEN_GENERIC),
    ROYAL_QUARTERS("Royal Quarters", DWARVEN_GENERIC),
    BARRACKS_OF_HEROES("Barracks of Heroes", DWARVEN_GENERIC),
    HANGING_COURT("Hanging Court", DWARVEN_GENERIC),
    GREAT_ICE_WALL("Great Ice Wall", DWARVEN_GENERIC),
    DIVANS_GATEWAY("Divans Gateway", DWARVEN_GENERIC),
    CLIFFSIDE_VEINS("Cliffside Veins", DWARVEN_GENERIC),
    RAMPARTS_QUARRY("Ramparts Quarry", DWARVEN_GENERIC),
    FAR_RESERVE("Far Reserve", DWARVEN_GENERIC),
    GOBLIN_BURROWS("Goblin Burrows", DWARVEN_GENERIC),
    THE_MIST("The Mist", DWARVEN_GENERIC),
    UPPER_MINES("Upper Mines", DWARVEN_GENERIC),
    FORGE_BASIN("Forge Basin", DWARVEN_GENERIC),
    THE_FORGE("The Forge", DWARVEN_GENERIC),
    ROYAL_MINES("Royal Mines", DWARVEN_GENERIC),
    ARISTOCRAT_PASSAGE("Aristocrat Passage", DWARVEN_GENERIC),
    GLACITE_MINESHAFT("Glacite Mineshafts", DWARVEN_GENERIC),
    TUNNELS("Glacite Tunnels", DWARVEN_GENERIC),
    CAMP("Dwarven Base Camp", DWARVEN_GENERIC),
    GLACITE_LAKE("Glacite Lake", DWARVEN_GENERIC),

    SPIDERS_DEN("Spiders Den", NONE),
    ARACHNE_BURROW("Arachnes Burrow", SPIDERS_DEN),
    ARACHNE_CHAMBER("Arachnes Chamber", SPIDERS_DEN),
    GRANDMA_HOUSE("Grandmas House", SPIDERS_DEN),
    SPIDER_MOUND("Spider Mound", SPIDERS_DEN),

    ;

    private final String name;
    private final Location parentZone;
    private final BiFunction<String, String, Boolean> pred;
    Location(String name, Location parent, BiFunction<String, String, Boolean> predicate) {
        this.name = name;
        parentZone = parent;
        this.pred = predicate;
    }
    Location(String name, Location parent) {
        this.name = name;
        this.parentZone = parent;
        this.pred = String::equals;
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
