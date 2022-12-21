package me.blueslime.messagehandler.reflection;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.EnumMap;

public class ReflectionHandlerCache {
    public static final ReflectionHandlerCache REFLECTION_HANDLER_CACHE = new ReflectionHandlerCache();

    private final EnumMap<MinecraftEnum, Class<?>> minecraftMap = new EnumMap<>(MinecraftEnum.class);

    private final EnumMap<BukkitEnum, Class<?>> bukkitMap = new EnumMap<>(BukkitEnum.class);

    private Method playerHandler;

    private final String version;

    public ReflectionHandlerCache() {
        String name = Bukkit.getServer().getClass().getPackage().getName();

        this.version = name.substring(
                name.lastIndexOf(".") + 1
        );

        for (MinecraftEnum minecraft : MinecraftEnum.values()) {
            initialize(
                    "net.minecraft.server",
                    version,
                    minecraft
            );
        }

        for (BukkitEnum minecraft : BukkitEnum.values()) {
            initialize(
                    "org.bukkit.craftbukkit",
                    version,
                    minecraft
            );
        }

        try {
            playerHandler = bukkitMap.get(BukkitEnum.CRAFT_PLAYER).getDeclaredMethod("getHandle");
        } catch (Exception ignored) {

        }
    }

    public void initialize(String path, String version, MinecraftEnum minecraft) {
        for (String location : minecraft.getPath()) {
            try {
                if (minecraft == MinecraftEnum.CHAT_SERIALIZER) {
                    location = location.replace(
                            "[serializer]",
                            version.equalsIgnoreCase("v1_8_R1") ?
                                    "ChatSerializer" :
                                    "ChatComponentText"
                    );
                }
                minecraftMap.put(
                        minecraft,
                        Class.forName(
                                location.replace("[version]", version)
                                        .replace("[path]", path)
                        )
                );
            } catch (Exception ignored) {}
        }
    }

    public void initialize(String path, String version, BukkitEnum bukkit) {
        for (String location : bukkit.getPath()) {
            try {
                bukkitMap.put(
                        bukkit,
                        Class.forName(
                                location.replace("[version]", version)
                                        .replace("[path]", path)
                        )
                );
            } catch (Exception ignored) {}
        }
    }

    public static Class<?> getReference(MinecraftEnum minecraft) {
        Class<?> reference = REFLECTION_HANDLER_CACHE.minecraftMap.get(minecraft);

        if (reference != null) {
            return reference;
        }

        new ReferencedClassException(minecraft, getVersion()).printStackTrace();
        return null;
    }

    public static Class<?> getReference(BukkitEnum bukkit) {
        Class<?> reference = REFLECTION_HANDLER_CACHE.bukkitMap.get(bukkit);

        if (reference != null) {
            return reference;
        }

        new ReferencedClassException(bukkit, getVersion()).printStackTrace();
        return null;
    }

    public static String getVersion() {
        return REFLECTION_HANDLER_CACHE.version;
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object connection = REFLECTION_HANDLER_CACHE.playerHandler.invoke(getCraftPlayer(player));

            Field playerConnectionField = connection.getClass().getDeclaredField("playerConnection");

            Object obtainConnection = playerConnectionField.get(connection);

            Method sendPacket = obtainConnection.getClass().getDeclaredMethod("sendPacket", MinecraftEnum.PACKET.getProvided());

            sendPacket.invoke(
                    obtainConnection,
                    packet
            );
        } catch (Exception ignored) {}
    }

    public static Object getCraftWorld(World world) {
        return BukkitEnum.CRAFT_WORLD.getProvided().cast(world);
    }

    public static Object getCraftPlayer(Player player) {
        return BukkitEnum.CRAFT_PLAYER.getProvided().cast(player);
    }
}
