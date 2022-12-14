package me.blueslime.messagehandler.types.bossbar.legacy;

import me.blueslime.messagehandler.types.bossbar.BossBarHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class LegacyBossBar extends BossBarHandler {

    private Class<?> craftPlayerClass;
    private Class<?> entityWither;

    private Class<?> entitySpawn;

    private Class<?> entityDestroy;

    private Class<?> craftWorld;

    private Class<?> worldServer;

    private Method worldHandler;

    private Method playerHandler;

    private Class<?> packet;

    private Method SET_INVISIBILITY;
    private Method SET_LOCATION;
    private Method SET_HEALTH;
    private Method GET_HEALTH;
    private Method SET_NAME;

    public LegacyBossBar() {
        String MINECRAFT_PATH = "net.minecraft.server." + MINECRAFT_VERSION + ".";
        String BUKKIT_MAIN_PATH = "org.bukkit.craftbukkit.";
        String BUKKIT_PATH = BUKKIT_MAIN_PATH + MINECRAFT_VERSION + ".";


        boolean notFoundCraft = false;

        try {
            craftPlayerClass = Class.forName(BUKKIT_PATH + "entity.CraftPlayer");
        } catch (Exception ignored) {
            notFoundCraft = true;
        }

        try {
            if (notFoundCraft) {
                craftPlayerClass = Class.forName(BUKKIT_MAIN_PATH + "CraftPlayer");
            }

            entityWither = Class.forName(MINECRAFT_PATH + "EntityWither");

            entitySpawn = Class.forName(MINECRAFT_PATH + "PacketPlayOutSpawnEntityLiving");

            entityDestroy = Class.forName(MINECRAFT_PATH + "PacketPlayOutEntityDestroy");

            craftWorld = Class.forName(BUKKIT_PATH + "CraftWorld");

            packet = Class.forName(MINECRAFT_PATH + "Packet");

            worldServer = Class.forName(MINECRAFT_PATH + "WorldServer");

            playerHandler = craftPlayerClass.getDeclaredMethod("getHandle");
            worldHandler = craftWorld.getDeclaredMethod("getHandle");

            SET_INVISIBILITY = entityWither.getMethod("setInvisible", Boolean.class);
            SET_LOCATION = entityWither.getMethod("setLocation", Double.class, Double.class, Double.class, Float.class, Float.class);
            SET_HEALTH = entityWither.getMethod("setHealth", Float.class);
            GET_HEALTH = entityWither.getMethod("getMaxHealth");
            SET_NAME = entityWither.getMethod("setCustomName", String.class);
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
                    entityWither.getConstructor(
                            worldServer
                    ).newInstance(
                            worldHandler.invoke(
                                    getCraftWorld(player.getWorld())
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

            Object packet = entitySpawn.getConstructor(
                    entityWither
            ).newInstance(
                    wither
            );

            sendPacket(player, packet);
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
            int id = (int) entityWither.getMethod("getId").invoke(
                    wither
            );

            Object packet = entityDestroy.getConstructor(Integer.class).newInstance(id);

            sendPacket(player, packet);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void sendPacket(Player player, Object packet) {
        try {
            Object connection = playerHandler.invoke(getCraftPlayer(player));

            Field playerConnectionField = connection.getClass().getDeclaredField("playerConnection");

            Object obtainConnection = playerConnectionField.get(connection);

            Method sendPacket = obtainConnection.getClass().getDeclaredMethod("sendPacket", this.packet);

            sendPacket.invoke(
                    obtainConnection,
                    packet
            );
        } catch (Exception ignored) {}
    }

    private Object getCraftWorld(World world) {
        return craftWorld.cast(world);
    }

    private Object getCraftPlayer(Player player) {
        return craftPlayerClass.cast(player);
    }
}
