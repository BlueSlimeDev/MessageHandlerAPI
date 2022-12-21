package me.blueslime.messagehandler.types.titles.legacy;

import me.blueslime.messagehandler.reflection.MinecraftEnum;
import me.blueslime.messagehandler.reflection.ReflectionHandlerCache;
import me.blueslime.messagehandler.types.titles.TitlesHandler;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class LegacyTitles extends TitlesHandler {

    private Constructor<?> titleConstructor;

    public LegacyTitles() {
        try {
            titleConstructor = MinecraftEnum.PLAY_OUT_TITLE.getProvided().getConstructor(
                    MinecraftEnum.PLAY_OUT_TITLE.getProvided().getDeclaredClasses()[0],
                    MinecraftEnum.CHAT_BASE_COMPONENT.getProvided(),
                    int.class,
                    int.class,
                    int.class
            );
        } catch (Exception ignored) {

        }
    }

    @Override
    public void send(Player player, int fadeInTime, int showTime, int fadeOutTime, String title, String subtitle) {
        try {
            Object chatTitle = MinecraftEnum.CHAT_BASE_COMPONENT.getProvided().getDeclaredClasses()[0].getMethod("a", String.class)
                    .invoke(null, "{\"text\": \"" + title + "\"}");

            Object packet = titleConstructor.newInstance(
                    MinecraftEnum.PLAY_OUT_TITLE.getProvided().getDeclaredClasses()[0].getField("TITLE").get(null),
                    chatTitle,
                    fadeInTime,
                    showTime,
                    fadeOutTime
            );

            Object chatsTitle = MinecraftEnum.CHAT_BASE_COMPONENT.getProvided().getDeclaredClasses()[0].getMethod("a", String.class)
                    .invoke(null, "{\"text\": \"" + subtitle + "\"}");

            Object subtitlePackage = titleConstructor.newInstance(
                    MinecraftEnum.PLAY_OUT_TITLE.getProvided().getDeclaredClasses()[0].getField("SUBTITLE").get(null),
                    chatsTitle,
                    fadeInTime,
                    showTime,
                    fadeOutTime
            );

            ReflectionHandlerCache.sendPacket(player, packet);
            ReflectionHandlerCache.sendPacket(player, subtitlePackage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
