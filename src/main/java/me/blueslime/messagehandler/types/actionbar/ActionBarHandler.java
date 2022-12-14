package me.blueslime.messagehandler.types.actionbar;

import me.blueslime.messagehandler.MessageType;
import me.blueslime.messagehandler.types.actionbar.latest.DefaultActionBar;
import me.blueslime.messagehandler.types.actionbar.legacy.LegacyActionBar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ActionBarHandler implements MessageType {
    public static final String MINECRAFT_VERSION = extractNMSVersion();

    private final static ActionBarHandler INSTANCE = initializeInstance();

    public abstract void send(Player player, String message);

    public static void sendActionBar(Player player, String message) {
        INSTANCE.send(player, message);
    }

    public void execute(Player player, String text) {
        send(player, text);
    }

    private static ActionBarHandler initializeInstance() {
        String version = MINECRAFT_VERSION;

        if (version == null) {
            return new DefaultActionBar();
        }

        if (isLegacy(version)) {
            return new LegacyActionBar();
        }

        return new DefaultActionBar();
    }

    public static ActionBarHandler getInstance() {
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
