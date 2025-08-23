package dev.fealtous.minorconvenience.mining;


import com.mojang.logging.LogUtils;
import dev.fealtous.minorconvenience.Config;
import dev.fealtous.minorconvenience.convenience.chat.ChatHandler;
import dev.fealtous.minorconvenience.convenience.chat.ChatParser;
import dev.fealtous.minorconvenience.utils.Location;
import dev.fealtous.minorconvenience.utils.LocatorUtil;
import dev.fealtous.minorconvenience.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static dev.fealtous.minorconvenience.Config.getXOff;
import static dev.fealtous.minorconvenience.Config.getYOff;

public class MiningHandler {
    private static Minecraft mc = Minecraft.getInstance();
    private static final Pattern IS_GEMSTONE = Pattern.compile("(✎)|(❤)|(⸕)|(☘)|(✧)");
    private static final Pattern HAS_QUANTITY = Pattern.compile("x(\\d+)");
    private static Pattern SHOULD_TRACK = null;
    private static HashMap<String, Integer> renderables = new HashMap<>();
    private static List<String> RENDER_ORDER = List.of();
    public static void updateRenderList(List<String> strings) {
        RENDER_ORDER = strings;
        String whatever = "";
        for (String string : strings) {
            whatever += "(" + string + ")" + "|";
        }
        SHOULD_TRACK = Pattern.compile(whatever.substring(0, whatever.length()-1));
    }
    private static List<Component> components = null;
    private static int chestCount = 0;
    public static final ChatParser parser = new ChatParser() {
        boolean chestContents = false;

        @Override
        public boolean handleMessage(Component msg) {
            String clean = msg.getString().replaceAll("§.", "");
            if (clean.contains("▬")) {
                chestContents = !chestContents;
                MiningHandler.push(null);
            } else if (chestContents) {
                push(msg);
            }
            return chestContents;
        }
    };

    public static void push(Component comp) {
        if (comp == null) {
            if (components == null) {
                components = new ArrayList<>();
            } else {
                if (!components.get(0).getString().matches(".*CHEST.*")) {
                    ChatHandler.unCancel(components);
                    components = null;
                    return;
                }
                for (Component component : components) {
                    var msg = component.getString();
                    Matcher finder = SHOULD_TRACK.matcher(msg);
                    if (!finder.find()) continue;
                    var q = HAS_QUANTITY.matcher(msg);
                    int i = 1;
                    if (q.find()) {
                        try {
                            i = Integer.parseInt(q.group().substring(1));
                        } catch (Exception e){
                            LogUtils.getLogger().info(q.group());
                        }
                    }
                    renderables.merge(finder.group(), i, Integer::sum);
                }
                chestCount++;
                components = null;
            }
        } else {
            components.add(comp);
        }
    }

//    public static final LayeredDraw.Layer miningOverlay = (gui, delta) -> {
//        if (LocatorUtil.whereAmI().getParentZone() != Location.HOLLOWS_GENERIC) return;
//        int yoffset = getYOff(5, Config.powderTop);
//        int xoffset = getXOff(5, Config.powderLeft);
//        RenderUtils.renderText(gui, "Chests: " + chestCount, xoffset, yoffset);
//        for (String s : RENDER_ORDER) {
//            var val = renderables.get(s);
//            if (val != null) {
//                yoffset += 10;
//                RenderUtils.renderText(gui, s + ": " + val, xoffset, yoffset);
//            }
//        }
//    };
}

