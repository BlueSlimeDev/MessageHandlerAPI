package me.blueslime.messagehandler.types.bossbar.latest;

import me.blueslime.messagehandler.types.bossbar.BossBarHandler;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class DefaultBossBar extends BossBarHandler {
    @Override
    public void send(Player player, String message, float percentage) {
        BossBar bar = (BossBar) fromPlayer(
                player,
                Bukkit.createBossBar(
                        message,
                        BarColor.BLUE,
                        BarStyle.SOLID
                )
        );

        float calculate = percentage / 100;

        bar.removePlayer(player);

        bar.addPlayer(player);

        bar.setProgress(calculate);

        bar.setTitle(message);

        bar.setVisible(true);
    }

    @Override
    public void remove(Player player) {
        BossBar bar = (BossBar) fromPlayer(player);

        if (bar != null) {
            bar.removePlayer(player);

            removePlayer(
                    player
            );
        }
    }
}
