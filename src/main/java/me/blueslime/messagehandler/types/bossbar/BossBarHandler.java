package me.blueslime.messagehandler.types.bossbar;

import me.blueslime.messagehandler.MessageType;
import me.blueslime.messagehandler.types.bossbar.latest.DefaultBossBar;
import me.blueslime.messagehandler.types.bossbar.legacy.LegacyBossBar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public abstract class BossBarHandler implements MessageType {
    public static final ConcurrentHashMap<UUID, Object> WITHER_MAP = new ConcurrentHashMap<>();

    public static final String MINECRAFT_VERSION = extractNMSVersion();

    private static final BossBarHandler INSTANCE = initializeInstance();

    public void send(Player player, String message) {
        send(player, message, 100);
    }

    public void execute(Player player, String text) {
        send(player, text);
    }

    public abstract void send(Player player, String message, float percentage);

    public abstract void remove(Player player);


    public static void sendBossBar(Player player, String message) {
        INSTANCE.send(player, message);
    }

    public static void sendBossBar(Player player, String message, float percentage) {
        INSTANCE.send(player, message, percentage);
    }

    public static void removeBossBar(Player player) {
        INSTANCE.remove(player);
    }

    public Object fromPlayer(Player player, Object defObject) {
        return WITHER_MAP.computeIfAbsent(player.getUniqueId(), k -> defObject);
    }

    public Object fromPlayer(Player player) {
        return WITHER_MAP.get(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        WITHER_MAP.remove(player.getUniqueId());
    }

    protected Location obtainWitherLocation(Location location) {
        return location.add(
                location.getDirection().multiply(60)
        );
    }

    public static BossBarHandler getInstance() {
        return INSTANCE;
    }

    private static BossBarHandler initializeInstance() {
        String version = MINECRAFT_VERSION;

        if (version == null) {
            return new DefaultBossBar();
        }

        if (isLegacy(version)) {
            return new LegacyBossBar();
        }

        return new DefaultBossBar();
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
