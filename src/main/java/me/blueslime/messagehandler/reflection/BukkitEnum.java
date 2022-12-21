package me.blueslime.messagehandler.reflection;

public enum BukkitEnum {
    CRAFT_PLAYER("[path].[version].entity.CraftPlayer", "[path].entity.CraftPlayer", "[path].CraftPlayer"),
    CRAFT_WORLD("[path].[version].CraftWorld");

    BukkitEnum(String... path) {
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
