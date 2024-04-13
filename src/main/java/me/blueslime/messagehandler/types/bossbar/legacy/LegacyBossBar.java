package me.blueslime.messagehandler.types.bossbar.legacy;

import me.blueslime.messagehandler.types.bossbar.BossBarHandler;
import me.blueslime.messagehandler.utils.GameVersion;
import me.blueslime.messagehandler.utils.list.ReturnableArrayList;
import me.blueslime.utilitiesapi.reflection.SpecifiedClass;
import me.blueslime.utilitiesapi.reflection.method.MethodContainer;
import me.blueslime.utilitiesapi.reflection.method.MethodData;
import me.blueslime.utilitiesapi.reflection.utils.presets.Presets;
import me.blueslime.utilitiesapi.reflection.utils.storage.PluginStorage;
import me.blueslime.utilitiesapi.utils.consumer.PluginConsumer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

public class LegacyBossBar extends BossBarHandler {
    public static final PluginStorage<UUID, Object> WITHER_MAP = PluginStorage.initAsHash();

    private static boolean bossBarMethodNotFound = false;

    private final Constructor<?> destroyBuilder;
    private final Constructor<?> witherBuilder;
    private final Constructor<?> spawnBuilder;
    private final MethodContainer worldHandler;
    private final Method playerHandler;
    private final Method customName;
    private final Method invisible;
    private final Method maxHealth;
    private final Method location;
    private final Method health;
    private final Method id;

    public LegacyBossBar() {
        String version = GameVersion.getCurrent().toString();

        SpecifiedClass spawnEntity = SpecifiedClass.build(
                true,
                "[minecraft]." + version + ".PacketPlayOutSpawnEntityLiving",
                "net.minecraft." + version + ".PacketPlayOutSpawnEntityLiving",
                "net.minecraft.[version].PacketPlayOutSpawnEntityLiving",
                "[minecraft].[version].PacketPlayOutSpawnEntityLiving",
                "net.minecraft.server." + version + ".PacketPlayOutSpawnEntityLiving"
        );

        SpecifiedClass entityWither = SpecifiedClass.build(
                true,
                "[minecraft]." + version + ".EntityWither",
                "net.minecraft." + version + ".EntityWither",
                "net.minecraft.[version].EntityWither",
                "[minecraft].[version].EntityWither",
                "net.minecraft.server." + version + ".EntityWither"
        );

        if (spawnEntity.exists() && entityWither.exists()) {
            spawnBuilder = PluginConsumer.ofUnchecked(
                    "Can't find 'generate boss bar' constructor",
                    () -> spawnEntity.exists() ?
                            spawnEntity.getResult().getConstructor(entityWither.getResult()) :
                            null
            );
        } else {
            sendMessage("[MessageHandlerAPI] BossBar creator builder issue: " + spawnEntity.exists() + ", " + entityWither.exists());
            spawnBuilder = null;
        }

        if (entityWither.exists()) {
            Class<?> wither = entityWither.getResult();

            witherBuilder = PluginConsumer.ofUnchecked(
                    "Can't find 'wither builder' constructor",
                    () -> wither.getConstructor(Presets.WORLD.getResult())
            );

            customName = PluginConsumer.ofUnchecked(() -> wither.getMethod("setCustomName", String.class));

            invisible = PluginConsumer.ofUnchecked(() -> wither.getMethod("setInvisible", boolean.class));

            maxHealth = PluginConsumer.ofUnchecked(() -> wither.getMethod("getMaxHealth"));

            location = PluginConsumer.ofUnchecked(() -> wither.getMethod("setLocation", double.class, double.class, double.class, float.class, float.class));

            health = PluginConsumer.ofUnchecked(() -> wither.getMethod("setHealth", float.class));

            id = PluginConsumer.ofUnchecked(() -> wither.getMethod("getId"));

        } else {
            id = null;
            health = null;
            location = null;
            maxHealth = null;
            invisible = null;
            customName = null;
            witherBuilder = null;
        }
        SpecifiedClass destroyEntity = SpecifiedClass.build(
                true,
                "[minecraft]." + version + ".PacketPlayOutEntityDestroy",
                "net.minecraft." + version + ".PacketPlayOutEntityDestroy",
                "[minecraft].[version].PacketPlayOutEntityDestroy",
                "net.minecraft.server." + version + ".PacketPlayOutEntityDestroy"
        );

        if (destroyEntity.exists()) {
            destroyBuilder = PluginConsumer.ofUnchecked(
                    "Can't find 'destroy boss bar' constructor",
                    () -> destroyEntity.exists() ?
                            destroyEntity.getResult().getConstructor(int.class) :
                            null
            );
        } else {
            sendMessage("Destroy entity builder is not found.");
            destroyBuilder = null;
        }

        worldHandler = Presets.CRAFT_WORLD.findMethods(
            MethodContainer.builder(
                new ReturnableArrayList<MethodData>()
                    .addValue(
                        MethodData.build(
                            "custom-class",
                            MethodData.SearchMethod.DECLARED,
                            0,
                            "getHandle"
                        )
                    )
            )
        );

        playerHandler = PluginConsumer.ofUnchecked(() -> Presets.CRAFT_PLAYER.getResult().getDeclaredMethod("getHandle"));
    }

    private void sendMessage(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(message);
    }

    @Override
    public void send(Player player, String message) {
        sendBossBar(player, message, 100);
    }

    @Override
    public void send(Player player, String message, float percentage) {
        try {
            if (!bossBarMethodNotFound) {
                return;
            }
        } catch (Exception ignored) {
            bossBarMethodNotFound = true;
        }

        if (canNotInit()) {
            return;
        }

        try {
            if (percentage <= 0) {
                percentage = (float) 0.001;
            }

            Object world = worldHandler.execute(player.getLocation().getWorld());

            Object wither = WITHER_MAP.get(
                    player.getUniqueId(),
                    K -> PluginConsumer.ofUnchecked("Can't generate wither", () -> witherBuilder.newInstance(world))
            );

            if (wither == null) {
                return;
            }

            float health = (float) maxHealth.invoke(wither);

            float life = (percentage * health);

            Location location = obtainWitherLocation(player.getLocation());

            this.customName.invoke(wither, message);
            this.health.invoke(wither, life);
            this.invisible.invoke(wither, true);
            this.location.invoke(wither, location.getX(), location.getY(), location.getZ(), 0, 0);

            Object packet = Presets.PACKET.getResult().getConstructor(
                    Presets.ENTITY.getResult()
            ).newInstance(
                    wither
            );

            sendPacket(player, packet);
        } catch (Throwable ignored) { }
    }

    public void sendPacket(Player player, Object packet) {
        PluginConsumer.ofUnchecked(
            () -> {
                Object connection = playerHandler.invoke(getCraftPlayer(player));

                Field playerConnectionField = connection.getClass().getDeclaredField("playerConnection");

                Object obtainConnection = playerConnectionField.get(connection);

                Method sendPacket = obtainConnection.getClass().getDeclaredMethod(
                    "sendPacket", Presets.PACKET.getResult()
                );

                sendPacket.invoke(
                    obtainConnection,
                    packet
                );

                return true;
            }
        );
    }

    private Object getCraftPlayer(Player player) {
        return Presets.CRAFT_PLAYER.convert(player);
    }

    @Override
    public void remove(Player player) {
        try {
            if (!bossBarMethodNotFound) {
                return;
            }
        } catch (Throwable ignored) {
            bossBarMethodNotFound = true;
        }

        if (canNotInit()) {
            return;
        }

        if (WITHER_MAP.toMap().containsKey(player.getUniqueId())) {
            Object wither = WITHER_MAP.get(
                    player.getUniqueId()
            );

            PluginConsumer.ofUnchecked(
                () -> {
                    int id = (int)this.id.invoke(wither);

                    Object packet = destroyBuilder.newInstance(id);

                    sendPacket(
                        player,
                        packet
                    );
                    return true;
                }
            );
        }
    }

    private boolean canNotInit() {
        return id == null ||
            playerHandler == null ||
            health == null ||
            location == null ||
            maxHealth == null ||
            invisible == null ||
            customName == null ||
            witherBuilder == null ||
            !worldHandler.exists() ||
            spawnBuilder == null ||
            destroyBuilder == null;
    }
}
