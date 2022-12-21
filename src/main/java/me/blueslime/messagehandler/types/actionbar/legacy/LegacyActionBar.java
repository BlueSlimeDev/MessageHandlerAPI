package me.blueslime.messagehandler.types.actionbar.legacy;

import me.blueslime.messagehandler.reflection.MinecraftEnum;
import me.blueslime.messagehandler.reflection.ReflectionHandlerCache;
import me.blueslime.messagehandler.types.actionbar.ActionBarHandler;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public class LegacyActionBar extends ActionBarHandler {
    private Method CHAT_BUILDER;

    public LegacyActionBar() {
        try {
            if (ReflectionHandlerCache.getVersion().equalsIgnoreCase("v1_8_R1")) {
                CHAT_BUILDER = MinecraftEnum.CHAT_SERIALIZER.getProvided().getDeclaredMethod("a", String.class);
            }
        } catch (Exception ignored) {

        }
    }
    @Override
    public void send(Player player, String message) {
        try {
            Object object = ReflectionHandlerCache.getVersion().equalsIgnoreCase("v1_8_R1") ?
                    MinecraftEnum.CHAT_BASE_COMPONENT.getProvided().cast(
                            CHAT_BUILDER.invoke(
                                    MinecraftEnum.CHAT_SERIALIZER.getProvided(),
                                    "{'text': '" + message + "'}"
                            )
                    ) :
                    MinecraftEnum.CHAT_SERIALIZER.getProvided().getConstructor(new Class[]{String.class}).newInstance(message);

            Object packetPlayOutChat = MinecraftEnum.PLAY_OUT_CHAT.getProvided().getConstructor(
                    new Class[]{MinecraftEnum.CHAT_BASE_COMPONENT.getProvided(), Byte.TYPE}
            ).newInstance(object, (byte) 2);

            ReflectionHandlerCache.sendPacket(player, packetPlayOutChat);
        } catch (Exception ignored) {

        }
    }
}
