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
    private boolean tryFirst = false;
    private boolean trySecond = false;
    private Method worldHandler;

    private Method SET_INVISIBILITY;
    private Method SET_LOCATION;
    private Method SET_HEALTH;
    private Method GET_HEALTH;
    private Method SET_NAME;
    private int failure = 0;

    public LegacyBossBar() {
        load(0);
    }

    private void load(int id) {
        if (id == 0) {
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

            } catch (Exception ignored) {
                load(1);
            }
        } else if (id == 1) {
            try {
                worldHandler = BukkitEnum.CRAFT_WORLD.getProvided().getDeclaredMethod("getHandle");

                SET_INVISIBILITY = MinecraftEnum.ENTITY.getProvided()
                        .getDeclaredMethod(
                                "setInvisible",
                                Boolean.class
                        );

                SET_LOCATION = MinecraftEnum.ENTITY.getProvided()
                        .getMethod(
                                "setLocation",
                                Double.class,
                                Double.class,
                                Double.class,
                                Float.class,
                                Float.class
                        );

                SET_HEALTH = MinecraftEnum.ENTITY_LIVING.getProvided().getMethod(
                        "setHealth",
                        Float.class
                );

                GET_HEALTH = MinecraftEnum.ENTITY_LIVING.getProvided().getMethod("getMaxHealth");

                SET_NAME = MinecraftEnum.ENTITY.getProvided()
                        .getMethod(
                                "setCustomName",
                                String.class
                        );

            } catch (Exception ignored) {
                load(2);
            }
        } else if (id == 2) {
            try {
                worldHandler = BukkitEnum.CRAFT_WORLD.getProvided().getDeclaredMethod("getHandle");

                SET_INVISIBILITY = MinecraftEnum.ENTITY_LIVING.getProvided()
                        .getMethod(
                                "setInvisible",
                                Boolean.class
                        );

                SET_LOCATION = MinecraftEnum.ENTITY.getProvided()
                        .getMethod(
                                "setLocation",
                                Double.class,
                                Double.class,
                                Double.class,
                                Float.class,
                                Float.class
                        );

                SET_HEALTH = MinecraftEnum.ENTITY_LIVING.getProvided().getMethod(
                        "setHealth",
                        Float.class
                );

                GET_HEALTH = MinecraftEnum.ENTITY_LIVING.getProvided().getMethod("getMaxHealth");

                SET_NAME = MinecraftEnum.ENTITY.getProvided()
                        .getMethod(
                                "setCustomName",
                                String.class
                        );

            } catch (Exception ignored) {
                load(3);
            }
        } else if (id == 3) {
            try {
                worldHandler = BukkitEnum.CRAFT_WORLD.getProvided().getDeclaredMethod("getHandle");

                failure = 1;

                SET_INVISIBILITY = MinecraftEnum.ENTITY.getProvided()
                        .getDeclaredMethod(
                                "b",
                                int.class,
                                boolean.class
                        );

                SET_LOCATION = MinecraftEnum.ENTITY.getProvided()
                        .getMethod(
                                "setLocation",
                                Double.class,
                                Double.class,
                                Double.class,
                                Float.class,
                                Float.class
                        );

                SET_HEALTH = MinecraftEnum.ENTITY_LIVING.getProvided().getMethod(
                        "setHealth",
                        Float.class
                );

                GET_HEALTH = MinecraftEnum.ENTITY_LIVING.getProvided().getMethod("getMaxHealth");

                SET_NAME = MinecraftEnum.ENTITY.getProvided()
                        .getMethod(
                                "setCustomName",
                                String.class
                        );

            } catch (Exception exception) {
                Bukkit.getServer().getLogger().info("[MessageHandlerAPI] Can't create boss bar for this version");
                Bukkit.getServer().getLogger().info("[MessageHandlerAPI] Are you using a super legacy version?");

                exception.printStackTrace();
                failure = 2;
            }
        }
    }


    @Override
    public void send(Player player, String message, float percentage) {
        if (failure == 2) {
            return;
        }
        try {
            if (percentage <= 0) {
                percentage = (float) 0.001;
            }

            Object wither;

            if (!tryFirst)  {
                try {
                    wither = fromPlayer(
                            player,
                            MinecraftEnum.WITHER.getProvided().getConstructor(
                                    MinecraftEnum.WORLD_SERVER.getProvided()
                            ).newInstance(
                                    worldHandler.invoke(
                                            ReflectionHandlerCache.getCraftWorld(player.getWorld())
                                    )
                            )
                    );
                } catch (Exception ignored) {
                    tryFirst = true;
                    send(player, message, percentage);
                    return;
                }
            } else {
                if (!trySecond) {
                    try {
                        wither = fromPlayer(
                                player,
                                MinecraftEnum.WITHER.getProvided().getConstructor(
                                        MinecraftEnum.WORLD.getProvided()
                                ).newInstance(
                                        worldHandler.invoke(
                                                ReflectionHandlerCache.getCraftWorld(player.getWorld())
                                        )
                                )
                        );
                    } catch (Exception ignored) {
                        trySecond = true;
                        send(player, message, percentage);
                        return;
                    }
                } else {
                    try {
                        Object casted = MinecraftEnum.WORLD.getProvided().cast(
                                worldHandler.invoke(
                                        ReflectionHandlerCache.getCraftWorld(player.getWorld())
                                )
                        );

                        wither = fromPlayer(
                                player,
                                MinecraftEnum.WITHER.getProvided().getConstructor(
                                        MinecraftEnum.WORLD.getProvided()
                                ).newInstance(
                                        casted
                                )
                        );
                    } catch (Exception ignored) {
                        Bukkit.getServer().getLogger().info("Can't initialize bossBar in this version (" + ReflectionHandlerCache.getVersion() + "), wither method not found");
                        return;
                    }
                }
            }

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

            if (failure != 1) {
                SET_INVISIBILITY.invoke(
                        wither,
                        true
                );
            } else {
                SET_INVISIBILITY.invoke(
                        wither,
                        5,
                        true
                );
            }

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
