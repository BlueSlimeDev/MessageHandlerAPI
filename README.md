# MessageHandlerAPI | THE LIGHTWEIGHT MESSAGE HANDLER

___

* Support from 1.7.x to 1.19.x
* ActionBar
* BossBar
* Titles
* PlaceholderAPI Support
* Messages with components

___


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