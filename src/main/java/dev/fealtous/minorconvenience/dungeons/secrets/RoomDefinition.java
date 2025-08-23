package dev.fealtous.minorconvenience.dungeons.secrets;

import com.mojang.logging.LogUtils;
import dev.fealtous.minorconvenience.MinorConvenience;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import org.checkerframework.checker.nullness.qual.NonNull;
import oshi.util.tuples.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoomDefinition implements Serializable {
    private static RoomDefinition instance;
    public static final HashMap<Long, RoomDefinition> knownRooms = new HashMap<>();
    public static final RoomDefinition EMPTY = new RoomDefinition( 0, 0);
    private static Vec2 instanceLocation = null;
    private String name;
    private final long identifier;
    private final long alignmentIdentifier;
    public List<POI> pois = new ArrayList<>();
    private long parent = -1;
    private List<Pair<Long, Vec2>> children = new ArrayList<>();
    public RoomDefinition(long identifier, long alignmentIdentifier) {
        this.identifier = identifier;
        this.alignmentIdentifier = alignmentIdentifier;
    }
    public RoomDefinition(Pair<Long, Long> identifiers) {
        this.identifier = identifiers.getA();
        this.alignmentIdentifier = identifiers.getB();
    }

    public static void create(long hash, long alignmentIdentifier, Vec2 pair) {
        instance = new RoomDefinition(hash, alignmentIdentifier);
        instanceLocation = pair;
        MutableComponent mut = MutableComponent.create(new PlainTextContents.LiteralContents("Starting room definition"));
//        mut.setStyle(Style.EMPTY.withClickEvent(
//                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/secret solved " + hash)));
        Minecraft.getInstance().gui.getChat().addMessage(mut);
    }

    /**
     * Finalize the current instance.
     */
    public static void build() {
        knownRooms.put(instance.identifier, instance);
        instanceLocation = null;
        instance = null;
    }
    public static void name(@NonNull String name) {
        instance.name = name;
    }
    public static void build(long identifier, long alignmentIdentifier) {
        RoomDefinition dependent = new RoomDefinition(identifier, alignmentIdentifier);
        dependent.parent = instance.identifier;
    }
    public boolean matchesAlignment(long alignmentHash) {
        return alignmentHash == alignmentIdentifier;
    }

    public static RoomDefinition getIfKnown(long hash) {
        return knownRooms.get(hash);
    }

    public static boolean addChild(long childHash) {
        if (instance == null || childHash == instance.identifier) return false; // No parent or room == parent.
        // Add child main hash and relative index location to parent
        instance.children.add(new Pair<>(childHash, RoomScanner.getIndices().negated().add(instanceLocation)));
        // Also add child as solved room, since it does not require any secrets.
        RoomDefinition child = new RoomDefinition(RoomScanner.getIdentifiers());
        knownRooms.put(child.identifier, child);
        return true;
    }
    public List<Vec2> getRelatives() {
        if (parent == -1) return null;
        return null;
    }

    public static void addPOI(POIType type) {
        switch (type) {
            case  ITEM ->
                    // just grab player position
                    instance.pois.add(new POI(type, Minecraft.getInstance().player.blockPosition()));
            case LEVER, WALL, RKEY, BAT, CHEST->
                    // do look vector first block
                    instance.pois.add(new POI(type, ((BlockHitResult)
                            Minecraft.getInstance().player.pick(15, 0, false))
                            .getBlockPos()));
        }
    }
    // todo Implement custom save system that doesn't use Serializable
    public static void save() {
        try {
            FileOutputStream fw = new FileOutputStream(MinorConvenience.DUNGEONS + "rooms.dat");
            ObjectOutputStream out = new ObjectOutputStream(fw);
            out.writeObject(knownRooms);
            out.close();
            fw.close();
        } catch (Exception e) {
            LogUtils.getLogger().error("Unable to save known rooms. dunno how to fix if this happens");
        }
    }
    public static void load() {
        try {
            FileInputStream fi = new FileInputStream(MinorConvenience.DUNGEONS + "rooms.dat");
            ObjectInputStream in = new ObjectInputStream(fi);
            Object x = in.readObject();
            //knownRooms.putAll((HashMap<Long, Pair<RoomDefinition, DungeonSecretRenderer.Alignment>>) x);
        } catch (FileNotFoundException e) {
            // Need to write
        } catch (Exception e) {
            LogUtils.getLogger().error("Unable to load known rooms.");
        }
    }
}
