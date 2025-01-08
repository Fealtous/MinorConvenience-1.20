package dev.fealtous.minorconvenience.dungeons.secrets;

public enum POIType {
    WALL(0x00ff0000),
    LEVER(0x00ff0000),
    BAT(0x00ff0000),
    ITEM(0x00ff0000),
    CHEST(0x00ff0000),
    RKEY(0x00ff0000),
    ESSENCE(0x00ff0000);
    int color;
    POIType(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
