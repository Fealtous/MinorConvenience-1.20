package dev.fealtous.minorconvenience.convenience;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChatHandler {
    @SubscribeEvent
    public static void chatReceived(ClientChatReceivedEvent e) {
        MutableComponent origin = (MutableComponent) e.getMessage();
        if (origin.getStyle().getClickEvent() == null) {
            origin.setStyle(origin.getStyle().withClickEvent(
                    new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, origin.getString()))
            );
        } else {
            origin.setStyle(origin.getStyle().withClickEvent(
                    new BiClickEvent(origin.getStyle().getClickEvent(),
                            ClickEvent.Action.COPY_TO_CLIPBOARD,
                            origin.getString())));
        }
        e.setMessage(origin);
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
        public BiClickEvent.Action getAction() {
            if (KeyBindingHandlers.COPY_TOGGLE.isDown()) {
                return alt;
            } else {
                return super.getAction();
            }
        }
        @Override
        public String getValue() {
            if (KeyBindingHandlers.COPY_TOGGLE.isDown()) {
                return altValue;
            } else {
                return super.getValue();
            }
        }
    }
}

