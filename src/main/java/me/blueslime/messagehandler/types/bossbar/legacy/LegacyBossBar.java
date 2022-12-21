package me.blueslime.messagehandler.types.bossbar.legacy;

import me.blueslime.messagehandler.reflection.BukkitEnum;
import me.blueslime.messagehandler.reflection.MinecraftEnum;
import me.blueslime.messagehandler.reflection.ReflectionHandlerCache;
import me.blueslime.messagehandler.types.bossbar.BossBarHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public class LegacyBossBar extends BossBarHandler {
    private Method worldHandler;

    private Method SET_INVISIBILITY;
    private Method SET_LOCATION;
    private Method SET_HEALTH;
    private Method GET_HEALTH;
    private Method SET_NAME;

    public LegacyBossBar() {
        try {
            worldHandler = BukkitEnum.CRAFT_WORLD.getProvided().getDeclaredMethod("getHandle");

            SET_INVISIBILITY = MinecraftEnum.WITHER.getProvided()
                    .getMethod(
                            "setInvisible",
                            Boolean.class
                    );

            SET_LOCATION = MinecraftEnum.WITHER.getProvided()
                    .getMethod(
                            "setLocation",
                            Double.class,
                            Double.class,
                            Double.class,
                            Float.class,
                            Float.class
                    );

            SET_HEALTH = MinecraftEnum.WITHER.getProvided().getMethod(
                    "setHealth",
                    Float.class
            );

            GET_HEALTH = MinecraftEnum.WITHER.getProvided().getMethod("getMaxHealth");

            SET_NAME = MinecraftEnum.WITHER.getProvided()
                    .getMethod(
                            "setCustomName",
                            String.class
                    );

        } catch (Exception exception) {
            Bukkit.getServer().getLogger().info("[MessageHandlerAPI] Can't create boss bar for this version");
            Bukkit.getServer().getLogger().info("[MessageHandlerAPI] Are you using a super legacy version?");

            exception.printStackTrace();
        }


    }


    @Override
    public void send(Player player, String message, float percentage) {
        try {
            if (percentage <= 0) {
                percentage = (float) 0.001;
            }

            Object wither = fromPlayer(
                    player,
                    MinecraftEnum.WITHER.getProvided().getConstructor(
                            MinecraftEnum.WORLD_SERVER.getProvided()
                    ).newInstance(
                            worldHandler.invoke(
                                    ReflectionHandlerCache.getCraftWorld(player.getWorld())
                            )
                    )
            );

            Location location = obtainWitherLocation(player.getLocation());

            float health = (float) GET_HEALTH.invoke(
                    wither
            );


            SET_HEALTH.invoke(
                    wither,
                    (percentage * health)
            );

            SET_NAME.invoke(
                    wither,
                    message
            );

            SET_INVISIBILITY.invoke(
                    wither,
                    true
            );

            SET_LOCATION.invoke(
                    wither,
                    location.getX(),
                    location.getY(),
                    location.getZ(),
                    0,
                    0
            );

            Object packet = MinecraftEnum.ENTITY_SPAWN.getProvided().getConstructor(
                    MinecraftEnum.WITHER.getProvided()
            ).newInstance(
                    wither
            );

            ReflectionHandlerCache.sendPacket(player, packet);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void remove(Player player) {
        Object wither = fromPlayer(player);

        removePlayer(player);

        if (wither == null) {
            return;
        }

        try {
            int id = (int) MinecraftEnum.WITHER.getProvided().getMethod("getId").invoke(
                    wither
            );

            Object packet = MinecraftEnum.ENTITY_DESTROY.getProvided().getConstructor(Integer.class).newInstance(id);

            ReflectionHandlerCache.sendPacket(player, packet);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
