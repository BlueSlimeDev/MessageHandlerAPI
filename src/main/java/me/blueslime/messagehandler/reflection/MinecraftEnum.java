package me.blueslime.messagehandler.reflection;

public enum MinecraftEnum {
    ENTITY_DESTROY("[path].[version].PacketPlayOutEntityDestroy"),
    ENTITY_SPAWN("[path].[version].PacketPlayOutSpawnEntityLiving"),
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
