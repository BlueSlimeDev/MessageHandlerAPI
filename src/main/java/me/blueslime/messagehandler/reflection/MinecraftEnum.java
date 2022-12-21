package me.blueslime.messagehandler.reflection;

public enum MinecraftEnum {
    ENTITY_DESTROY("[path].[version].PacketPlayOutEntityDestroy"),
    ENTITY_SPAWN("[path].[version].PacketPlayOutSpawnEntityLiving"),
    CHAT_BASE_COMPONENT("[path].[version].IChatBaseComponent"),
    PLAY_OUT_TITLE("[path].[version].PacketPlayOutTitle"),
    PLAY_OUT_CHAT("[path].[version].PacketPlayOutChat"),
    CHAT_SERIALIZER("[path].[version].[serializer]"),
    WORLD_SERVER("[path].[version].WorldServer"),
    WITHER("[path].[version].EntityWither"),
    PACKET("[path].[version].Packet");

    MinecraftEnum(String... path) {
        this.path = path;
    }

    private final String[] path;

    public String[] getPath() {
        return path;
    }

    /**
     * obtain the class provided from this reflection
     * @return {@link java.lang.Class}
     */
    public Class<?> getProvided() {
        return ReflectionHandlerCache.getReference(this);
    }
}
