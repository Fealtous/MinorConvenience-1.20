package dev.fealtous.minorconvenience.convenience.chat;

import com.mojang.logging.LogUtils;
import dev.fealtous.minorconvenience.convenience.BasicInfoOverlay;
import dev.fealtous.minorconvenience.utils.Location;
import dev.fealtous.minorconvenience.utils.LocatorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.*;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.SystemMessageReceivedEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

import java.util.List;
import java.util.regex.Matcher;

import static dev.fealtous.minorconvenience.utils.RegexUtils.*;

public class ChatHandler {
    private static ChatParser activeParser = null;

    public static void init() {
        SystemMessageReceivedEvent.BUS.addListener(ChatHandler::chatReceived);
    }

    @SubscribeEvent
    public static boolean chatReceived(SystemMessageReceivedEvent e) {
        Location loc = LocatorUtil.whereAmI();
        Component msg = e.getMessage();
        boolean defaultHandler = false;
        boolean parentHandler = false;
        boolean forceCancel = false;
        if (e.isOverlay()) {
            defaultHandler = loc.applyFilter(msg);
            parentHandler = loc.applyParent(msg);
            //boolean globalHandler todo
        } else {
            if (activeParser == null) {
                defaultHandler = loc.applyFilter(msg);
                parentHandler = loc.applyParent(msg);
                if (defaultHandler) {
                    activeParser = loc.getHandler();
                } else if (parentHandler) {
                    activeParser = loc.getParentZone().getHandler();
                }
                if (defaultHandler && parentHandler) {
                    LogUtils.getLogger().error("More than 1 chat parser has fired: {} and {}. Zone handler takes priority.", loc.getName(), loc.getParentZone().getName());
                }
            } else {
                if (!activeParser.handleMessage(msg)) {
                    activeParser = null;
                }
                forceCancel = true;
            }
        }

        if (defaultHandler || parentHandler || forceCancel) return true;
        String clean = msg.getString().replaceAll("§.", "");
        // todo add strict message block filter config
        // todo also add options to turn off these filters
        if (LocatorUtil.isIn(Location.NETHER)) {
            if (hellionFilter.matcher(clean).find() || daggerAttunementFilter.matcher(clean).find()) {
                return true;
            }
        }
        Matcher autopet = autoPet.matcher(clean);
        if (autopet.find()) {
            String result = autopet.group();
            result = result.substring(0, result.length() - 1);
            BasicInfoOverlay.petInfo = result;
            return true;
        }
        MutableComponent cmp = Component.literal("<");
        cmp.setStyle(Style.EMPTY.withClickEvent(
                        new ClickEvent.CopyToClipboard(e.getMessage().getString().trim()))
                .withHoverEvent(new HoverEvent.ShowText(Component.literal("Click me to copy"))));
        cmp.append(e.getMessage());
        e.setMessage(cmp);
        return false;
    }

    public static void unCancel(List<Component> componentList) {
        var mc = Minecraft.getInstance().gui.getChat();
        for (Component component : componentList) {
            mc.addMessage(component);
        }
    }
}

