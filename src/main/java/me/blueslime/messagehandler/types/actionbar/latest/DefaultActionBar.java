package me.blueslime.messagehandler.types.actionbar.latest;

import me.blueslime.messagehandler.types.actionbar.ActionBarHandler;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class DefaultActionBar extends ActionBarHandler {
    @Override
    public void send(Player player, String message) {
        TextComponent component = new TextComponent(
                message
        );

        player.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                component
        );
    }
}
