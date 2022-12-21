package me.blueslime.messagehandler.types.messages;

import me.blueslime.messagehandler.MessageHandlerAPI;
import me.blueslime.messagehandler.MessageType;
import me.blueslime.messagehandler.types.messages.latest.DefaultMessage;
import me.blueslime.messagehandler.types.messages.legacy.LegacyMessages;
import org.bukkit.entity.Player;

public abstract class MessageHandler implements MessageType {
    private final static MessageHandler INSTANCE = initializeInstance();

    public abstract void send(Player player, String message);

    public static void sendMessage(Player player, String message) {
        INSTANCE.send(player, message);
    }

    public void execute(Player player, String text) {
        send(player, text);
    }

    private static MessageHandler initializeInstance() {
        if (MessageHandlerAPI.Executor.IS_BUKKIT) {
            return new LegacyMessages();
        }

        return new DefaultMessage();
    }

    /**
     * Use this method to send {@link net.md_5.bungee.api.chat.BaseComponent}
     * @return DefaultMessage as MessageHandler
     */
    public DefaultMessage asComponentSender() {
        if (INSTANCE instanceof DefaultMessage) {
            return (DefaultMessage) INSTANCE;
        }
        return new DefaultMessage();
    }

    public static MessageHandler getInstance() {
        return INSTANCE;
    }
}
