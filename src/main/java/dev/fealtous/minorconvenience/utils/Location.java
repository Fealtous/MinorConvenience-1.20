package dev.fealtous.minorconvenience.utils;

import dev.fealtous.minorconvenience.convenience.chat.ChatParser;
import dev.fealtous.minorconvenience.mining.DivanSolver;
import dev.fealtous.minorconvenience.mining.MiningHandler;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum Location {

    UNKNOWN("UNKNOWN", null),
    GLOBAL("GLOBAL", null),

    JERRY_ISLAND("Jerry's Island", GLOBAL),

    CATACOMBS("The Catacombs", GLOBAL, String::contains),

    DUNGEON_HUB("Dungeon Hub", GLOBAL),

    GOLD_MINE("Gold Mine", GLOBAL),

    ISLAND("Your Island", GLOBAL),
    GARDEN("The Garden", ISLAND, String::contains),
    PLOT("Plot", ISLAND, String::contains),

    HUB("The Hub", GLOBAL),
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
    FISHERMAN("Fisherman's Hut", HUB),
    CANVAS("Canvas Room", HUB),
    BANK("Bank", HUB),
    AUCTION_HOUSE("Auction House", HUB),
    BAZAAR_ALLEY("Bazaar Alley", HUB),
    SHENS_AUCTION("Shen's Auction", HUB),
    SHENS_REGALIA("Shen's Regalia", HUB),
    FARMHOUSE("Farmhouse", HUB),
    COAL_MINE("Coal Mine", HUB),
    HUB_WIZARD("Wizard Tower", HUB),

    END("The End", GLOBAL),
    DRAGON("Dragon's Nest", END),
    BRUISERS("Bruiser Hideout", END),
    VOID_SLATE("Void Slate", END),
    VOID_SEP("Void Sepulture", END),


    THE_PARK("The Park", GLOBAL),
    BIRCH("Birch Park", THE_PARK),
    SPRUCE("Spruce Woods", THE_PARK),
    ACACIA("Savanna Woodland", THE_PARK),
    DARK_OAK("Dark Thicket", THE_PARK),
    _JUNGLE("Jungle Island", THE_PARK),
    HOWLING_CAVE("Howling Cave", THE_PARK),
    LONELY_ISLAND("Lonely Island", THE_PARK),
    MELDOYS_PLATEAU("Melody's Plateau", THE_PARK),

    DEEP_CAVERN("Deep Caverns", GLOBAL),
    GUNPOWDER_MINES("Gunpowder Mines", DEEP_CAVERN),
    LAPIS_QUARRY("Lapis Quarry", DEEP_CAVERN),
    DIAMOND_RESERVE("Diamond Reserve", DEEP_CAVERN),
    PIGMENS_DEN("Pigmen's Den", DEEP_CAVERN),
    SLIMEHILL("Slimehill", DEEP_CAVERN),
    OBSIDIAN_SANCTUARY("Obsidian Sanctuary", DEEP_CAVERN),

    NETHER("Crimson Isle", GLOBAL),
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
    MATRIARCH("Matriarch's Lair", NETHER),
    MAGMA_BOSS("Magma Chamber", NETHER),
    VORE("Belly of the Beast", NETHER),
    SQUID("Plhlegblast Pool", NETHER),
    ASHFANG("Ruins of Ashfang", NETHER),
    THE_BASTION("The Bastion", NETHER),
    DRAGONTAIL_BANK("Dragontail Bank", NETHER),
    DRAGONTAIL_SQR("Dragontail Townsquare", NETHER),
    DRAGONTAIL_AH("Dragontail Auction", NETHER, String::startsWith),
    SCARLETON_PLZ("Scarleton Plaza", NETHER),
    SCARLETON_BANK("Scarleton Bank", NETHER),
    ODGER("Odger's Hut", NETHER),
    BARB_OUTPOST("Barbarian Outpost", NETHER),
    MAGE_OUTPOST("Mage Outpost", NETHER),

    HOLLOWS_GENERIC("Crystal Hollows", GLOBAL, MiningHandler.parser),
    PRECURSOR_REMNANTS("Precursor Remnants", HOLLOWS_GENERIC),
    GOBLIN_HOLDOUT("Goblin Holdout", HOLLOWS_GENERIC),
    MITHRIL_DEPOSITS("Mithril Deposits", HOLLOWS_GENERIC),
    NUCLEUS("Crystal Nucleus", HOLLOWS_GENERIC),
    DIVAN("Mines of Divan", HOLLOWS_GENERIC, DivanSolver.parser),
    LOST_CITY("Lost Precursor City", HOLLOWS_GENERIC),
    JUNGLE("Jungle", HOLLOWS_GENERIC),
    TEMPLE("Jungle Temple", HOLLOWS_GENERIC),
    LIGMA_QUEEN("Goblin Queen's Den", HOLLOWS_GENERIC),
    MAGMA_FIELDS("Magma Fields", HOLLOWS_GENERIC),
    BAL("Khazad-dûm", HOLLOWS_GENERIC, String::contains),
    GROTTO("Fairy Grotto", HOLLOWS_GENERIC),

    FARMING_ISLANDS("Farming Islands", GLOBAL),
    THE_BARN("The Barn", FARMING_ISLANDS),
    WINDMILL("Windmill", FARMING_ISLANDS),
    MUSHROOM_DESERT("Mushroom Desert", FARMING_ISLANDS),
    DESERT_SETTLEMENT("Desert Settlement", FARMING_ISLANDS),
    OASIS("Oasis", FARMING_ISLANDS),
    DESERT_MOUNTAIN("Desert Mountain", FARMING_ISLANDS),
    TRAPPERS_DEN("Trapper's Den", FARMING_ISLANDS),
    MUSHROOM_GORGE("Mushroom Gorge", FARMING_ISLANDS),
    GLOWING_MUSHROOM_CAVE("Glowing Mushroom Cave", FARMING_ISLANDS),
    OVERGROWN_MUSHROOM_CAVE("Overgrown Mushroom Cave", FARMING_ISLANDS),
    JAKES_HOUSE("Jake's House", FARMING_ISLANDS),

    DWARVEN_GENERIC("Dwarven Mines", GLOBAL),
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
    RAMPARTS_QUARRY("Rampart's Quarry", DWARVEN_GENERIC),
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

    SPIDERS_DEN("Spider's Den", GLOBAL),
    ARACHNE_BURROW("Arachne's Burrow", SPIDERS_DEN),
    ARACHNE_CHAMBER("Arachne's Sanctuary", SPIDERS_DEN),
    GRANDMA_HOUSE("Grandma's House", SPIDERS_DEN),
    SPIDER_MOUND("Spider Mound", SPIDERS_DEN),
    ARCHEO_CAMP("Archelogist's Camp", SPIDERS_DEN),
    GRAVEL_MINES("Gravel Mines", SPIDERS_DEN),

    RIFT("The Rift", GLOBAL),
    RIFT_WIZARD("Wizard Tower", RIFT),
    WYLD_WOODS("Wild Woods", RIFT),
    SHIFTED_TAVERN("Shifted Tavern", RIFT),
    BROKEN_CAGE("Broken Cage", RIFT),
    LAGOON("Black Lagoon", RIFT),
    LEECH("Leeches Lair", RIFT),
    LAGOON_HUT("Lagoon Hut", RIFT),
    WEST_VILLAGE("West Village", RIFT),
    RIFT_COLOSSEUM("Colosseum", RIFT),
    DOLPHIN("Dolphin Trainer", RIFT),
    DREADFARM("Dreadfarm", RIFT),
    MIRRORVERSE("Mirrorverse", RIFT),
    PLAZA("Villae Plaza", RIFT),
    BARRY("Barry Center", RIFT),
    BARRY_HQ("Barry HQ", RIFT),
    RIFT_ISLAND("\"Your\" Island", RIFT),
    EMPTY_BANK("Empty Bank", RIFT),
    BARRIER_STREET("Barrier Street", RIFT),
    HALF_CAVE("Half-Eaten Cave", RIFT),
    LONELY_TERRACE("Lonely Terrace", RIFT),
    MURDER_HOUSE("Murder House", RIFT),
    PHOTON_PATH("Photon Pathway", RIFT),
    OTHERSIDE("Otherside", RIFT),
    LIVING_CAVE("Living Cave", RIFT),
    LIVING_STILLNESS("Living Stillness", RIFT),
    DEJA_VU("Déjà Vu Alley", RIFT),
    CHATAEU("Stillgore ", RIFT, String::startsWith),
    FAIRYLOSS("Fairylosophy Tower", RIFT),
    OUBLIETTE("Oubliette", RIFT),
    MOUTAINTOP("The Mountaintop", RIFT),
    TRIAL_GROUNDS("Trial Grounds", RIFT),
    RIFT_GALLERY_ENT("Rift Gallery Entrance", RIFT),
    RIFT_GALLERY("Rift Gallery", RIFT),
    TIME_CHAMBER("Time Chamber", RIFT),
    WIZARDMAN("Wizardman Bureau", RIFT),
    AMOGUS("The Vents", RIFT),
    CEREBELLUM("Cerebral Citadel", RIFT),
    FAME("Walk of Fame", RIFT),
    RIFT_BASTION("The Bastion", RIFT),

    ;

    private final String name;
    private final Location parentZone;
    private final BiFunction<String, String, Boolean> pred;
    private final ChatParser handler;

    Location(String name, Location parent, BiFunction<String, String, Boolean> predicate, ChatParser parser) {
        this.name = name;
        this.parentZone = parent;
        this.pred = predicate;
        this.handler = parser;
    }
    Location(String name, Location parent, ChatParser parser) {
        this.name = name;
        parentZone = parent;
        this.pred = String::equals;
        this.handler = parser;
    }
    Location(String name, Location parent, BiFunction<String, String, Boolean> predicate) {
        this.name = name;
        parentZone = parent;
        this.pred = predicate;
        handler = null;
    }
    Location(String name, Location parent) {
        this.name = name;
        this.parentZone = parent;
        this.pred = String::equals;
        handler = null;
    }


    public Location getParentZone() {
        return parentZone;
    }

    public String getName() {
        return name;
    }
    public ChatParser getHandler() {return handler;}
    public boolean applyFilter(Component msg) {
        if (handler != null) return handler.handleMessage(msg);
        return false;
    }
    public boolean applyParent(Component msg) {
        if (parentZone != null && parentZone.handler != null) return parentZone.handler.handleMessage(msg);
        return false;
    }
    public boolean hasParent() {
        return parentZone != null;
    }
    public boolean compare(String other) {
        return pred != null && pred.apply(other.toLowerCase(), name.toLowerCase());
    }

}

class Fuck {
    private static final Map<Loc, List<Loc>> blah = new HashMap<>();
    static {

    }
}

record Loc(String name, Loc parent, BiFunction<String, String, Boolean> predicate, ChatParser parser) {
    Loc(String name, Loc parent, ChatParser parser) {this(name, parent, String::equals, parser);}
    Loc(String name, Loc parent) {this(name, parent, String::equals, new ChatParser() {});}
    public boolean is(String loc) {
        return predicate != null && predicate.apply(loc, name);
    }
    public boolean filter(Component msg) {
        return parent.filter(msg) || parser.handleMessage(msg);
    }

}
