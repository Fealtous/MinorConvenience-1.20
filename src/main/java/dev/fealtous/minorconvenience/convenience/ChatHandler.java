package dev.fealtous.minorconvenience.convenience;

import dev.fealtous.minorconvenience.mining.MiningHandler;
import dev.fealtous.minorconvenience.utils.Location;
import dev.fealtous.minorconvenience.utils.LocatorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
            //Minecraft.getInstance().gui.getChat().addMessage(Component.literal(LocatorUtil.whereAmI().name()));
            if (LocatorUtil.whereAmI().equals(Location.DIVAN)) {
                Matcher matcher = metalDetectorPattern.matcher(e.getMessage().getString());
                if (matcher.find()) {
                    String match = matcher.group(0);
                    match = match.substring(0, match.length()-1);
                    MiningHandler.push(Float.parseFloat(match));
                }
            }

        } else { // Actually in chat

            var msg = ((MutableComponent) e.getMessage());
            var sibs = msg.getSiblings();
            MutableComponent last;
            var newmsg = Component.literal(((PlainTextContents) msg.getContents()).text());
            newmsg.setStyle(msg.getStyle());

            if (sibs.size() > 0) {
                last = ((MutableComponent) sibs.get(sibs.size() - 1));
            } else {
                last = msg;
            }

            if (last.getStyle().getClickEvent() == null) {
                last.setStyle(last.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, msg.getString())));
            } else {
                var evt = last.getStyle().getClickEvent();
                last.setStyle(last.getStyle().withClickEvent((new BiClickEvent(evt, ClickEvent.Action.COPY_TO_CLIPBOARD, msg.getString()))));
            }

            if (sibs.size() > 0) {
                for (int i = 0; i < sibs.size() - 1; i++) {
                    newmsg.append(sibs.get(i));
                }
            }
            if (!last.getString().equals(((PlainTextContents) msg.getContents()).text())) newmsg.append(last);

            e.setMessage(newmsg);
        }
    }

    static class BiClickEvent extends ClickEvent {
        private final ClickEvent.Action alt;
        private final String altValue;
        public BiClickEvent(ClickEvent origin, ClickEvent.Action alt, String altValue) {
            super(origin.getAction(), origin.getValue());
            this.alt = alt;
            this.altValue = altValue;
        }
        @Override
        public BiClickEvent.@NotNull Action getAction() {
            if (KeyBindingHandlers.COPY_TOGGLE.isDown()) {
                return alt;
            } else {
                return super.getAction();
            }
        }
        @Override
        public @NotNull String getValue() {
            if (KeyBindingHandlers.COPY_TOGGLE.isDown()) {
                return altValue;
            } else {
                return super.getValue();
            }
        }
    }
}

