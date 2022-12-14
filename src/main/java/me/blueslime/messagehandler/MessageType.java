package me.blueslime.messagehandler;

import me.blueslime.messagehandler.types.actionbar.ActionBarHandler;
import me.blueslime.messagehandler.types.bossbar.BossBarHandler;
import me.blueslime.messagehandler.types.messages.MessageHandler;
import me.blueslime.messagehandler.types.titles.TitlesHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public interface MessageType {

    static MessageType fromType(MessageHandlerAPI.Type type) {
        switch (type) {
            case TITLES:
                return TitlesHandler.getInstance();
            case BOSS_BAR:
                return BossBarHandler.getInstance();
            case ACTION_BAR:
                return ActionBarHandler.getInstance();
            default:
            case MESSAGES:
                return MessageHandler.getInstance();
        }
    }

    void execute(Player player, String text);

    default String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
