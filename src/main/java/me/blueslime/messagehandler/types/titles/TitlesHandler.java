package me.blueslime.messagehandler.types.titles;

import me.blueslime.messagehandler.MessageType;
import me.blueslime.messagehandler.types.titles.latest.DefaultTitles;
import me.blueslime.messagehandler.types.titles.legacy.LegacyTitles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class TitlesHandler implements MessageType {
    public static final String MINECRAFT_VERSION = extractNMSVersion();

    private final static TitlesHandler INSTANCE = initializeInstance();

    public abstract void send(Player player, String message);

    public void execute(Player player, String text) {
        send(player, text);
    }

    public static void sendTitle(Player player, String message) {
        INSTANCE.send(player, message);
    }

    private static TitlesHandler initializeInstance() {
        String version = MINECRAFT_VERSION;

        if (version == null) {
            return new DefaultTitles();
        }

        if (isLegacy(version)) {
            return new LegacyTitles();
        }

        return new DefaultTitles();
    }

    public static TitlesHandler getInstance() {
        return INSTANCE;
    }

    private static boolean isLegacy(String version) {
        return version.equalsIgnoreCase("v1_7_R0") ||
                version.equalsIgnoreCase("v1_7_R1") ||
                version.equalsIgnoreCase("v1_7_R2") ||
                version.equalsIgnoreCase("v1_7_R3") ||
                version.equalsIgnoreCase("v1_8_R1") ||
                version.equalsIgnoreCase("v1_8_R2") ||
                version.equalsIgnoreCase("v1_8_R3");
    }

    private static String extractNMSVersion() {
        Matcher matcher = Pattern.compile("v\\d+_\\d+_R\\d+").matcher(Bukkit.getServer().getClass().getPackage().getName());

        if (matcher.find()) {
            return matcher.group();
        }

        return "v1_19_R1";
    }

}
