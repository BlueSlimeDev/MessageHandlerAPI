package me.blueslime.messagehandler;

import me.blueslime.messagehandler.types.actionbar.ActionBarHandler;
import me.blueslime.messagehandler.types.bossbar.BossBarHandler;
import me.blueslime.messagehandler.types.messages.MessageHandler;
import me.blueslime.messagehandler.types.titles.TitlesHandler;
import me.blueslime.utilitiesapi.text.TextUtilities;
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
        return TextUtilities.colorize(text);
    }

}
