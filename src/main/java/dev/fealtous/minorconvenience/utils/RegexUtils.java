package dev.fealtous.minorconvenience.utils;

import java.util.regex.Pattern;

public class RegexUtils {
    public static final Pattern metalDetectorPattern = Pattern.compile("([0-9/,.]+m)");
    public static final Pattern blazeHealthPattern = Pattern.compile("([,\\d]+)/");
    public static final Pattern keeperPattern = Pattern.compile("(diamond)|(gold)|(emerald)|(lapis)");
    public static final Pattern enteringCataPattern = Pattern.compile(".*entered The Catacombs.*");
    public static final Pattern cataFloorPattern = Pattern.compile("(Floor) ([VI]+)");
    public static final Pattern cataPlayerPattern = Pattern.compile("([a-zA-Z0-9_]+)\sis now ready!.*");
    public static final Pattern mortFoundADeadBody = Pattern.compile(".*Mort.*found.*map.*");
    public static final Pattern goodLuckLoser = Pattern.compile(".*Mort.*Good luck.*");
    public static final Pattern autoPet = Pattern.compile("(\\[Lvl \\d{1,3}] [\\w\\s]+)!");
    public static final Pattern hellionFilter = Pattern.compile(".*Hellion Shield");
    public static final Pattern daggerAttunementFilter = Pattern.compile("Strike using the.*attunement");
}
