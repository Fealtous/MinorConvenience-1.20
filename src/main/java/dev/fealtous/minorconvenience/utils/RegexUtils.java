package dev.fealtous.minorconvenience.utils;

import java.util.regex.Pattern;

public class RegexUtils {
    public static final Pattern metalDetectorPattern = Pattern.compile("([0-9/,.]+m)");
    public static final Pattern playerDungeonPattern = Pattern.compile("\\[\\d+]\\s+([^\\s]*)\\s+.*");
    public static final Pattern blazeHealthPattern = Pattern.compile("([,\\d]+)/");
    public static final Pattern keeperPattern = Pattern.compile("(diamond)|(gold)|(emerald)|(lapis)");
}
