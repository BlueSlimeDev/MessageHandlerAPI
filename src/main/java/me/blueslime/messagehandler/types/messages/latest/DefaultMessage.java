package me.blueslime.messagehandler.types.messages.latest;

import me.blueslime.messagehandler.types.messages.MessageHandler;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class DefaultMessage extends MessageHandler {
    @Override
    public void send(Player player, String message) {
        player.spigot().sendMessage(
                new TextComponent(
                        colorize(message)
                )
        );
    }

    public void send(Player player, BaseComponent... component) {
        player.spigot().sendMessage(
                component
        );
    }
}
