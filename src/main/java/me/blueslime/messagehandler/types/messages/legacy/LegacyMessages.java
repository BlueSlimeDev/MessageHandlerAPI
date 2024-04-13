package me.blueslime.messagehandler.types.messages.legacy;

import me.blueslime.messagehandler.types.messages.MessageHandler;
import org.bukkit.entity.Player;

public class LegacyMessages extends MessageHandler {
    @Override
    public void send(Player player, String message) {
        player.sendMessage(
            colorize(message)
        );
    }
}
