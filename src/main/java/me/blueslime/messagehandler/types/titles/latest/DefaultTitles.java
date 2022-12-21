package me.blueslime.messagehandler.types.titles.latest;

import me.blueslime.messagehandler.reflection.ReflectionHandlerCache;
import me.blueslime.messagehandler.types.titles.TitlesHandler;
import org.bukkit.entity.Player;

public class DefaultTitles extends TitlesHandler {

    @SuppressWarnings("deprecation")
    @Override
    public void send(Player player, int fadeInTime, int showTime, int fadeOutTime, String title, String subtitle) {
        if (!isLegacy(ReflectionHandlerCache.getVersion())) {
            player.sendTitle(
                    title,
                    subtitle,
                    fadeInTime,
                    showTime,
                    fadeOutTime
            );
            return;
        }
        player.sendTitle(
                title,
                subtitle
        );
    }
}
