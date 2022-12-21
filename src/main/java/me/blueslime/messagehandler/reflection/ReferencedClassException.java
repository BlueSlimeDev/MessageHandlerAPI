package me.blueslime.messagehandler.reflection;

public class ReferencedClassException extends Exception {
    public ReferencedClassException(MinecraftEnum minecraft, String version) {
        super("You are using an incompatible version because the class for '" + minecraft.toString() + "' was not found in this version '" + version + "'");
    }

    public ReferencedClassException(BukkitEnum bukkit, String version) {
        super("You are using an incompatible version because the class for '" + bukkit.toString() + "' was not found in this version '" + version + "'");
    }
}