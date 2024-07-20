package dev.fealtous.minorconvenience.dungeons;

import com.mojang.logging.LogUtils;
import dev.fealtous.minorconvenience.MinorConvenience;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.world.phys.BlockHitResult;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoomDefiner implements Serializable {
    public static RoomDefiner instance;
    public static HashMap<Integer, RoomDefiner> knownRooms = new HashMap<>();
    public static final RoomDefiner EMPTY = new RoomDefiner("none", 0);
    private final String name;
    private final int hash;
    public List<POI> pois = new ArrayList<>();
    private RoomDefiner solvedBy = null;
    public RoomDefiner(String t, int h) {
        name = t;
        hash = h;
    }

    public static void create(@NonNull String roomTitle, int hash) {
        MutableComponent mut = MutableComponent.create(new PlainTextContents.LiteralContents("Starting room definition for " + roomTitle));
        mut.setStyle(Style.EMPTY.withClickEvent(
                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/secret solved " + hash)));
        Minecraft.getInstance().gui.getChat().addMessage(mut);
        instance = new RoomDefiner(roomTitle, hash);
    }

    public static void build() {
        knownRooms.put(instance.hash, instance);
        Minecraft.getInstance().gui.getChat().addMessage(
                Component.literal("Added new room: " + instance.hash));
        instance = null;
    }
    public static RoomDefiner tryGet(int hash) {
        var room = knownRooms.get(hash);
        while (room != null && room.solvedBy != null) {
            room = knownRooms.get(room.solvedBy.hash);
        }
        return room;
    }

    public static void solveVia(int hash) {
        instance.solvedBy = knownRooms.get(hash);
        build();
    }

    public static void addPOI(POIType type) {
        switch (type) {
            case LEVER, ITEM, CHEST, BAT ->
                    // do the same thing
                    instance.pois.add(new POI(type, Minecraft.getInstance().player.blockPosition()));
            case WALL, RKEY ->
                    // do look vector first block
                    instance.pois.add(new POI(type, ((BlockHitResult)
                            Minecraft.getInstance().player.pick(5, 0, false))
                            .getBlockPos()));
        }
    }

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
            knownRooms = ((HashMap<Integer, RoomDefiner>) x);
        } catch (FileNotFoundException e) {
            // Need to write
        } catch (Exception e) {
            LogUtils.getLogger().error("Unable to load known rooms.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomDefiner that = (RoomDefiner) o;
        return hash == that.hash && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return hash;
    }


}
