package dev.fealtous.minorconvenience.convenience;

import dev.fealtous.minorconvenience.mining.DivanSolver;
import dev.fealtous.minorconvenience.mining.MiningHandler;
import dev.fealtous.minorconvenience.utils.Location;
import dev.fealtous.minorconvenience.utils.LocatorUtil;
import net.minecraft.network.chat.*;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.regex.Matcher;

import static dev.fealtous.minorconvenience.utils.RegexUtils.*;

public class ChatHandler {
    private static boolean betweener = false;

    @SubscribeEvent
    public static void chatReceived(ClientChatReceivedEvent.System e) {
        if (e.isOverlay()){ // Above hotbar
            if (LocatorUtil.isIn(Location.DIVAN)) {
                Matcher matcher = metalDetectorPattern.matcher(e.getMessage().getString());
                if (matcher.find()) {
                    String match = matcher.group(0);
                    match = match.substring(0, match.length()-1);
                    DivanSolver.push(Float.parseFloat(match));
                }
            }
        } else if (e.getMessage().getString().matches(".*You found.*Metal.*")) {
            DivanSolver.refresh();
        } else { // Actually in chat
            String msg = e.getMessage().getString();
            String clean = msg.replaceAll("§.", "");
            if (LocatorUtil.isIn(Location.HOLLOWS_GENERIC)) {
                e.setCanceled(betweener);
                if (msg.contains("▬")) {
                    betweener = !betweener;
                    MiningHandler.push(null);
                    e.setCanceled(true);
                } else if (betweener) {
                    MiningHandler.push(e.getMessage());
                }
            } else if (LocatorUtil.isIn(Location.NETHER)) {
                if (hellionFilter.matcher(clean).find() || daggerAttunementFilter.matcher(clean).find()) {
                    e.setCanceled(true);
                    return;
                }
            }
            Matcher autopet = autoPet.matcher(clean);
            if (autopet.find()) {
                e.setCanceled(true);
                String result = autopet.group();
                result = result.substring(0, result.length()-1);
                BasicInfoOverlay.petInfo = result;
                return;
            }
            MutableComponent cmp = Component.literal("<");
            cmp.setStyle(Style.EMPTY.withClickEvent(
                    new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, e.getMessage().getString().trim()))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click me to copy"))));
            cmp.append(e.getMessage());
            e.setMessage(cmp);
        }
    }
}

