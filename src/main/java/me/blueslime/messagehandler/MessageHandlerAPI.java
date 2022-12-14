package me.blueslime.messagehandler;

import me.blueslime.messagehandler.hooks.PlaceholderParser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class MessageHandlerAPI {

    private static final boolean HAS_PLACEHOLDER_API = Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");

    public static boolean sendMessage(Player player, String message) {
        if (HAS_PLACEHOLDER_API) {
            return Executor.send(
                    player,
                    PlaceholderParser.parse(
                            player,
                            message
                    )
            );
        }
        return Executor.send(
                player,
                message
        );
    }

    public static class Executor {
        public static final boolean IS_BUKKIT = isBukkit();

        public static boolean isBukkit() {
            try {
                Class.forName("org.bukkit.entity.Player$Spigot");
                return true;
            } catch (Throwable ignored) {
                return false;
            }
        }

        public static boolean send(Player player, String message) {
            Type executableType = Type.fromString(message);

            MessageType type = MessageType.fromType(
                    executableType
            );


            message = message.replace("%player%", player.getName())
                    .replace("%max%", String.valueOf(Bukkit.getMaxPlayers()))
                    .replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()));

            if (executableType.getContainer() != null) {
                message = message.replace(
                        executableType.getContainer(),
                        ""
                );
            }

            boolean isBossBar = false;

            if (message.contains("<bossbar>")) {
                isBossBar = true;
            }

            if (HAS_PLACEHOLDER_API) {
                type.execute(
                        player,
                        PlaceholderParser.parse(
                                player,
                                message
                        )
                );
                return isBossBar;
            }
            type.execute(
                    player,
                    message
            );
            return isBossBar;
        }
    }

    public enum Type {
        ACTION_BAR("<actionbar>"),
        TITLES("<title>"),
        BOSS_BAR("<bossbar>"),
        MESSAGES;

        private final String container;

        Type(String container) {
            this.container = container;
        }

        Type() {
            this.container = null;
        }

        public String getContainer() {
            return container;
        }

        public static Type fromString(String message) {
            for (Type type : values()) {
                if (type.getContainer() != null) {
                    if (message.contains(type.getContainer())) {
                        return type;
                    }
                }
            }
            return Type.MESSAGES;
        }
    }
}
