package dev.fealtous.minorconvenience.convenience;

import dev.fealtous.minorconvenience.mining.MiningHandler;
import dev.fealtous.minorconvenience.utils.Location;
import dev.fealtous.minorconvenience.utils.LocatorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;

import static dev.fealtous.minorconvenience.utils.RegexUtils.metalDetectorPattern;

public class ChatHandler {


    @SubscribeEvent
    public static void chatReceived(ClientChatReceivedEvent.System e) {

        if (e.isOverlay()){ // Above hotbar
            if (LocatorUtil.isIn(Location.DIVAN)) {
                Matcher matcher = metalDetectorPattern.matcher(e.getMessage().getString());
                if (matcher.find()) {
                    String match = matcher.group(0);
                    match = match.substring(0, match.length()-1);
                    MiningHandler.push(Float.parseFloat(match));
                }
            }
        } else if (e.getMessage().getString().matches(".*You found.*Metal.*")) {
            MiningHandler.refresh();
        } else { // Actually in chat
            MutableComponent cmp = Component.literal("|");
            cmp.setStyle(Style.EMPTY.withClickEvent(
                    new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, e.getMessage().getString().trim()))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click me to copy"))));
            cmp.append(e.getMessage());
            e.setMessage(cmp);
        }
    }
}

