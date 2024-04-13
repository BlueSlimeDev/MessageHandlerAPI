# MessageHandlerAPI | THE LIGHTWEIGHT MESSAGE HANDLER

[![](https://jitpack.io/v/BlueSlimeDev/MessageHandlerAPI.svg)](https://jitpack.io/#BlueSlimeDev/MessageHandlerAPI)

___

* Support from 1.7.x to 1.21.x
* ActionBar
* BossBar
* Titles
* PlaceholderAPI Support
* Messages with components

___

Maven Usage (With jitpack):
```XML
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
```

# REQUIRES UTILITIES API FROM BLUE SLIME DEV

Dependency:
```XML
    <dependencies>
        <dependency>
            <groupId>com.github.BlueSlimeDev</groupId>
            <artifactId>UtilitiesAPI</artifactId>
            <version>1.0.1</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>com.github.BlueSlimeDev</groupId>
            <artifactId>MessageHandlerAPI</artifactId>
            <version>TAG</version>
            <!-- TAG = latest version, for example:
            <version>0.7</version>
            -->
        </dependency>
    </dependencies>
```


## Example Usages:
* Auto Detect `<titles>`, `<subtitle>`, `<actionbar>`, `<bossbar>`:
```Java
public class ExampleGeneral {
    public void sendMessage(Player player, String message) {
        MessageHandlerAPI.sendMessage(player, message);
    }
}
```
* Manual BossBar:
```Java
public class ExampleBoss {
    public void sendMessage(Player player, String message) {
        // Without percentage:
        BossBarHandler.sendBossBar(player, message);
        // With percentage:
        BossBarHandler.sendBossBar(player, message, 60);
    }
}
```
* Manual Titles:
```Java
public class ExampleTitles {
    public void sendMessage(Player player, String message) {
        // Without percentage:
        TitlesHandler.sendTitle(player, message);
        // With percentage:
        TitlesHandler.sendTitle(player, message, 60);
    }
}
```

* Manual ActionBar:
```Java
public class ExampleAction {
    public void sendMessage(Player player, String message) {
        ActionBarHander.sendActionBar(player, message);
    }
}
```