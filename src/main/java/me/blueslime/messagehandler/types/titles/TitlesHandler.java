package me.blueslime.messagehandler.types.titles;

import me.blueslime.messagehandler.MessageHandlerAPI;
import me.blueslime.messagehandler.MessageType;
import me.blueslime.messagehandler.reflection.ReflectionHandlerCache;
import me.blueslime.messagehandler.types.titles.latest.DefaultTitles;
import me.blueslime.messagehandler.types.titles.legacy.LegacyTitles;
import org.bukkit.entity.Player;

public abstract class TitlesHandler implements MessageType {
    public static final String MINECRAFT_VERSION = ReflectionHandlerCache.getVersion();

    private final static TitlesHandler INSTANCE = initializeInstance();

    public void send(Player player, String message) {
        String[] split = message.split("<subtitle>");

        String sub;

        int fadeIn = 20;
        int showTime = 20;
        int fadeOut = 20;

        if (split.length == 2) {
            sub = split[1];

            if (sub.contains("<title-timers>")) {
                String[] subSplit = sub.split("<title-timers>", 2);

                sub = subSplit[0];

                if (subSplit.length == 2) {
                    String[] times = subSplit[1].replace(" ", "").split(",");
                    if (times.length >= 1) {
                        if (isNumber(times[0])) {
                            fadeIn = Integer.parseInt(times[0]);
                        }
                    }
                    if (times.length >= 2) {
                        if (isNumber(times[1])) {
                            showTime = Integer.parseInt(times[1]);
                        }
                    }
                    if (times.length >= 3) {
                        if (isNumber(times[2])) {
                            fadeOut = Integer.parseInt(times[2]);
                        }
                    }
                }
            }
        } else {
            sub = "";
        }

        send(player, fadeIn, showTime, fadeOut, colorize(split[0]), colorize(sub));
    }

    public abstract void send(Player player, int fadeInTime, int showTime, int fadeOutTime, String title, String subtitle);

    public void execute(Player player, String text) {
        send(player, text);
    }

    public static void sendTitle(Player player, String message) {
        INSTANCE.send(player, message);
    }

    private static TitlesHandler initializeInstance() {

        if (MINECRAFT_VERSION == null) {
            return new DefaultTitles();
        }

        if (MessageHandlerAPI.Executor.IS_BUKKIT) {
            return new LegacyTitles();
        }

        return new DefaultTitles();
    }

    public static TitlesHandler getInstance() {
        return INSTANCE;
    }

    protected static boolean isLegacy(String version) {
        return version.equalsIgnoreCase("v1_8_R1") ||
                version.equalsIgnoreCase("v1_8_R2") ||
                version.equalsIgnoreCase("v1_8_R3") ||
                version.equalsIgnoreCase("v1_8_R4") ||
                version.equalsIgnoreCase("v1_8_R0");
    }

    public boolean isNumber(String argument) {
        try {
            Integer.parseInt(argument);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }
}
