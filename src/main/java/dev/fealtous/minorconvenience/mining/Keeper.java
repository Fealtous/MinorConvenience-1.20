package dev.fealtous.minorconvenience.mining;

enum Keeper {
    GOLD(3, -33),
    LAPIS(-33, -3),
    EMERALD(-3, 33),
    DIAMOND(33, 3),
    NONE(0,0);
    private final int x, z;
    Keeper(int x, int z) {
        this.x = x;
        this.z = z;
    }
    public int x() {return x;}
    public int z() {return z;}
}
