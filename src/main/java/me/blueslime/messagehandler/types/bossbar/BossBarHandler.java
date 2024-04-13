package me.blueslime.messagehandler.types.bossbar;

import me.blueslime.messagehandler.MessageType;
import me.blueslime.messagehandler.reflection.ReflectionHandlerCache;
import me.blueslime.messagehandler.types.bossbar.latest.DefaultBossBar;
import me.blueslime.messagehandler.types.bossbar.legacy.LegacyBossBar;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public abstract class BossBarHandler implements MessageType {
    public static final ConcurrentHashMap<UUID, Object> WITHER_MAP = new ConcurrentHashMap<>();

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
        String version = ReflectionHandlerCache.getVersion();

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
                version.equalsIgnoreCase("v1_7_R4") ||
                version.equalsIgnoreCase("v1_8_R0") ||
                version.equalsIgnoreCase("v1_8_R1") ||
                version.equalsIgnoreCase("v1_8_R2") ||
                version.equalsIgnoreCase("v1_8_R3") ||
                version.equalsIgnoreCase("v1_8_R4");
    }
}
