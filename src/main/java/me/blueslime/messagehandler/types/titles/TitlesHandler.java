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

        if (split.length == 2) {
            sub = split[1];
        } else {
            sub = "";
        }

        send(player, 20, 20, 20, colorize(split[0]), colorize(sub));
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
                version.equalsIgnoreCase("v1_8_R3");
    }
}
