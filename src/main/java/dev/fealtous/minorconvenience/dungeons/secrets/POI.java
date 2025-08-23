package dev.fealtous.minorconvenience.dungeons.secrets;

import net.minecraft.core.BlockPos;

import java.io.Serializable;

public class POI implements Serializable {
    private final POIType type;
    private final BlockPos offset;

    public POI(POIType type, BlockPos coordinates) {
        this.type = type;
        this.offset = coordinates;
    }

    public BlockPos getOffset() {
        return offset;
    }

    public POIType getType() {
        return type;
    }
}

