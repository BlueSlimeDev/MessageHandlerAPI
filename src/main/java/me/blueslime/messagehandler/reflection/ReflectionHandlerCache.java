package me.blueslime.messagehandler.reflection;

import org.bukkit.Bukkit;

import java.util.EnumMap;

public class ReflectionHandlerCache {
    public static final ReflectionHandlerCache REFLECTION_HANDLER_CACHE = new ReflectionHandlerCache();

    private final EnumMap<MinecraftEnum, Class<?>> minecraftMap = new EnumMap<>(MinecraftEnum.class);

    private final EnumMap<BukkitEnum, Class<?>> bukkitMap = new EnumMap<>(BukkitEnum.class);

    public ReflectionHandlerCache() {
        String name = Bukkit.getServer().getClass().getPackage().getName();

        String version = name.substring(
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
    }

    public void initialize(String path, String version, MinecraftEnum minecraft) {
        for (String location : minecraft.getPath()) {
            try {
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

    private static String getVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();

        return name.substring(
                name.lastIndexOf(".") + 1
        );
    }
}
