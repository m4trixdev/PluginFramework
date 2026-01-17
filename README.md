# PluginFramework - Advanced Minecraft Plugin Development Framework

A powerful and comprehensive framework for Minecraft plugin development, providing a structured architecture with utilities, services, and management systems to accelerate plugin creation.

![Minecraft](https://img.shields.io/badge/Minecraft-1.20.x+-brightgreen)
![Platform](https://img.shields.io/badge/Platform-Paper%20%7C%20Spigot-blue)
![Language](https://img.shields.io/badge/Language-Java-orange)
![Java Version](https://img.shields.io/badge/Java-17-red)

## 📋 Features

- 🏗️ **Base Plugin Architecture** - Extend `BasePlugin` for automatic framework initialization
- ⚙️ **Configuration Management** - Multi-file YAML configuration system with caching and reloading
- 🎮 **Command System** - Advanced command registry with sub-command support and tab completion
- 📡 **Event Registry** - Centralized event listener management with automatic cleanup
- 🔧 **Service Manager** - Lifecycle-managed services for modular plugin architecture
- 🔒 **Permission Manager** - Dynamic permission registration and validation system
- ⚡ **Scheduler Utilities** - Simplified synchronous and asynchronous task scheduling
- 💾 **Cache Service** - In-memory caching system with TTL support
- ⏱️ **Cooldown Manager** - Per-player cooldown system for commands and actions
- 🎨 **Text Utilities** - Legacy color codes and MiniMessage support
- 📝 **Message Builder** - Fluent API for building complex messages
- ✅ **Validation Utilities** - Input validation and error handling
- ⏰ **Time Utilities** - Duration formatting and tick conversion

## 🚀 Installation

### Requirements

- Java 17+
- Paper/Spigot 1.20.4
- Maven 3.6+

### Maven Dependency

Add the framework to your project's `pom.xml`:

```xml
<dependency>
    <groupId>dev.m4trix</groupId>
    <artifactId>plugin-framework</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>
```

### Repository

```xml
<repository>
    <id>m4trix-repo</id>
    <url>https://github.com/m4trixdev/PluginFramework</url>
</repository>
```

## ⚙️ Configuration

### config.yml

```yaml
settings:
  debug: false
  locale: en_US

cache:
  enabled: true
  default-ttl: 3600000

example:
  enabled: true
  value: "default"
```

### Multiple Configuration Files

The framework supports multiple configuration files:

```java
ConfigurationManager configManager = getConfigManager();
FileConfiguration config = configManager.load("config.yml");
FileConfiguration messages = configManager.load("messages.yml");
FileConfiguration database = configManager.load("database.yml");

// Reload specific config
configManager.reload("config.yml");

// Reload all configs
configManager.reloadAll();
```

## 🎮 Usage

### Basic Plugin Setup

Extend `BasePlugin` and implement the required methods:

```java
public class MyPlugin extends BasePlugin {

    @Override
    protected void initialize() {
        // Plugin initialization logic
        getLogger().info("Plugin initialized!");
    }

    @Override
    protected void registerCommands(CommandRegistry registry) {
        registry.register("mycommand", new MyCommand());
    }

    @Override
    protected void registerEvents(EventRegistry registry) {
        registry.register(new MyListener());
    }

    @Override
    protected void registerServices(ServiceManager manager) {
        manager.register(new MyService());
    }

    @Override
    protected void shutdown() {
        // Cleanup logic
        getLogger().info("Plugin disabled!");
    }
}
```

### Command System

#### Simple Command

```java
public class MyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("Hello from framework!");
        return true;
    }
}
```

#### Sub-Command System

```java
public class FrameworkCommand extends BaseCommand {

    public FrameworkCommand() {
        registerSubCommand("reload", new CommandHandler() {
            @Override
            public boolean execute(CommandSender sender, String[] args) {
                // Reload logic
                return true;
            }

            @Override
            public String getPermission() {
                return "plugin.reload";
            }
        });
    }

    @Override
    protected boolean executeDefault(CommandSender sender, String[] args) {
        sender.sendMessage("Usage: /command <subcommand>");
        return true;
    }
}
```

### Event Handling

```java
public class MyListener implements Listener {
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerUtil.sendMessage(player, "&aWelcome to the server!");
    }
}

// Register in BasePlugin
@Override
protected void registerEvents(EventRegistry registry) {
    registry.register(new MyListener());
}
```

### Service System

Create custom services with lifecycle management:

```java
public class DatabaseService implements Service {
    
    @Override
    public void start() {
        // Initialize database connection
        getLogger().info("Database service started");
    }

    @Override
    public void stop() {
        // Close database connection
        getLogger().info("Database service stopped");
    }

    @Override
    public boolean isRunning() {
        return connection != null && connection.isValid();
    }
}

// Register in BasePlugin
@Override
protected void registerServices(ServiceManager manager) {
    manager.register(new DatabaseService());
}

// Access service
DatabaseService dbService = getServiceManager().get(DatabaseService.class);
```

### Cache Service

```java
CacheService cache = getServiceManager().get(CacheService.class);

// Store with TTL (Time To Live)
cache.put("player:uuid", playerData, 3600000); // 1 hour

// Store permanently
cache.put("config:value", configValue);

// Retrieve
Optional<PlayerData> data = cache.get("player:uuid");

// Get or compute
PlayerData data = cache.getOrCompute("player:uuid", () -> {
    // Fetch from database
    return database.getPlayerData(uuid);
});

// Check existence
if (cache.contains("player:uuid")) {
    // Cache hit
}

// Invalidate
cache.invalidate("player:uuid");

// Clear all
cache.clear();
```

### Cooldown Manager

```java
CooldownManager cooldown = getServiceManager().get(CooldownManager.class);

// Set cooldown for player
cooldown.set("command:teleport", player.getUniqueId(), Duration.ofSeconds(30));

// Check if player is on cooldown
if (cooldown.has("command:teleport", player.getUniqueId())) {
    Duration remaining = cooldown.remaining("command:teleport", player.getUniqueId());
    player.sendMessage("Please wait " + TimeUtil.format(remaining));
    return;
}

// Remove cooldown
cooldown.remove("command:teleport", player.getUniqueId());

// Clear all cooldowns for a specific key
cooldown.clear("command:teleport");
```

### Scheduler Utilities

```java
// Synchronous task
SchedulerUtil.sync(plugin, () -> {
    // Runs on main thread
});

// Asynchronous task
SchedulerUtil.async(plugin, () -> {
    // Runs on async thread
});

// Delayed task
SchedulerUtil.syncLater(plugin, () -> {
    // Runs after 20 ticks (1 second)
}, 20);

// Repeating task
BukkitTask task = SchedulerUtil.syncRepeating(plugin, () -> {
    // Runs every 20 ticks
}, 0, 20);

// Cancel task
SchedulerUtil.cancelTask(task);

// Supply async, consume sync
SchedulerUtil.supplyThenAccept(plugin, 
    () -> fetchDataFromDatabase(),  // Async
    (data) -> processData(data)      // Sync
);

// CompletableFuture support
CompletableFuture<String> future = SchedulerUtil.supply(plugin, () -> {
    return fetchStringAsync();
});
future.thenAccept(result -> {
    // Process result
});
```

### Text Utilities

```java
// Legacy color codes
Component message = TextUtil.color("&aHello &cWorld");

// MiniMessage support
Component mmMessage = TextUtil.miniMessage("<green>Hello <red>World</red></green>");

// Strip colors
String plain = TextUtil.strip("&aHello");

// Convert to legacy
String legacy = TextUtil.toLegacy(component);

// Message Builder (Fluent API)
Component message = MessageBuilder.create()
    .text("Hello", NamedTextColor.GREEN)
    .space()
    .bold("World")
    .newline()
    .colored("&7This is a description")
    .build();
```

### Player Utilities

```java
// Get player by name
Optional<Player> player = PlayerUtil.getPlayer("PlayerName");

// Get player by UUID
Optional<Player> player = PlayerUtil.getPlayer(uuid);

// Send message
PlayerUtil.sendMessage(player, "&aHello!");
PlayerUtil.sendMessage(player, component);

// Broadcast
PlayerUtil.broadcast("&aServer restart in 10 minutes!");
PlayerUtil.broadcast(component);

// Check permission
if (PlayerUtil.hasPermission(player, "plugin.use")) {
    // Has permission
}
```

### Time Utilities

```java
// Convert Duration to ticks
long ticks = TimeUtil.toTicks(Duration.ofSeconds(5)); // 100 ticks

// Convert TimeUnit to ticks
long ticks = TimeUtil.toTicks(5, TimeUnit.SECONDS);

// Convert ticks to Duration
Duration duration = TimeUtil.fromTicks(100);

// Format duration
String formatted = TimeUtil.format(Duration.ofSeconds(3661)); // "1h 1m 1s"
String compact = TimeUtil.formatCompact(Duration.ofSeconds(3661)); // "01:01:01"

// Parse duration string
Duration duration = TimeUtil.parse("1h30m15s");
```

### Permission Manager

```java
PermissionManager permManager = new PermissionManager(plugin);

// Register permission
permManager.register("plugin.use", PermissionDefault.OP);
permManager.register("plugin.admin", PermissionDefault.OP, "Admin permission");

// Check permission
if (permManager.has(sender, "plugin.use")) {
    // Has permission
}

// Check any permission
if (permManager.hasAny(sender, "plugin.use", "plugin.admin")) {
    // Has at least one
}

// Check all permissions
if (permManager.hasAll(sender, "plugin.use", "plugin.reload")) {
    // Has all
}

// Unregister
permManager.unregister("plugin.use");
permManager.unregisterAll();
```

### Validation Utilities

```java
// Null checks
String value = ValidationUtil.requireNonNull(obj, "object");
String text = ValidationUtil.requireNonEmpty(str, "string");

// Collection checks
List<String> list = ValidationUtil.requireNonEmpty(collection, "list");

// Boolean checks
ValidationUtil.requireTrue(condition, "Condition must be true");
ValidationUtil.requireFalse(condition, "Condition must be false");

// Range checks
ValidationUtil.requireInRange(value, 0, 100, "percentage");
ValidationUtil.requirePositive(value, "count");
ValidationUtil.requireNonNegative(value, "amount");
```

### Logger Utilities

```java
// Info log
LoggerUtil.info(plugin, "Plugin loaded successfully");

// Warning log
LoggerUtil.warning(plugin, "Configuration missing, using defaults");

// Severe log
LoggerUtil.severe(plugin, "Critical error occurred");

// Error with exception
LoggerUtil.error(plugin, "Failed to connect to database", exception);

// Debug log (with debug mode check)
LoggerUtil.debug(plugin, "Player joined: " + player.getName(), debugMode);

// Get logger
Logger logger = LoggerUtil.getLogger(plugin);
```

## 🔐 Permissions

The framework provides built-in permission management. Permissions are registered dynamically:

```java
PermissionManager permManager = new PermissionManager(plugin);
permManager.register("plugin.use", PermissionDefault.OP);
permManager.register("plugin.admin", PermissionDefault.OP);
```

## 🛠️ Building

### Requirements

- JDK 17
- Maven 3.6+

### Build Commands

```bash
# Clone repository
git clone https://github.com/m4trixdev/PluginFramework.git
cd PluginFramework

# Compile
mvn clean compile

# Package
mvn clean package

# Install to local repository
mvn clean install
```

The compiled `.jar` file will be in `target/PluginFramework-1.0.0.jar`

## 📊 Architecture

### Core Components

```
BasePlugin
├── ConfigurationManager
│   └── Multi-file YAML support
├── CommandRegistry
│   └── Command registration & sub-commands
├── EventRegistry
│   └── Listener management
└── ServiceManager
    └── Lifecycle-managed services
```

### Services

- **CacheService** - In-memory cache with TTL
- **CooldownManager** - Player cooldown system

### Utilities

- **SchedulerUtil** - Task scheduling
- **TaskUtil** - Alternative task utilities
- **LoggerUtil** - Logging helpers
- **PlayerUtil** - Player operations
- **TextUtil** - Text formatting
- **MessageBuilder** - Fluent message building
- **TimeUtil** - Time & duration utilities
- **ValidationUtil** - Input validation

## 📝 API Overview

### BasePlugin Lifecycle

1. `initialize()` - Plugin initialization
2. `registerCommands()` - Register commands
3. `registerEvents()` - Register event listeners
4. `registerServices()` - Register services
5. `postInitialize()` - Post-initialization hook (optional)
6. `preShutdown()` - Pre-shutdown hook (optional)
7. `shutdown()` - Plugin shutdown

### Service Interface

```java
public interface Service {
    void start();
    void stop();
    String getName();
    boolean isRunning();
}
```

### CommandHandler Interface

```java
public interface CommandHandler extends TabCompleter {
    boolean execute(CommandSender sender, String[] args);
    String getPermission();
}
```

## 🐛 Troubleshooting

### Plugin doesn't initialize

- Ensure your plugin extends `BasePlugin`
- Check that all abstract methods are implemented
- Verify `plugin.yml` has correct main class
- Check server logs for initialization errors

### Configuration not loading

- Verify file exists in `src/main/resources/`
- Check YAML syntax is valid
- Ensure `ConfigurationManager.load()` is called
- Verify file permissions

### Commands not working

- Confirm command is declared in `plugin.yml`
- Check `CommandRegistry.register()` is called
- Verify command executor is set correctly
- Check for permission issues

### Services not starting

- Ensure service implements `Service` interface
- Verify `start()` method is implemented correctly
- Check service registration in `registerServices()`
- Review logs for service errors

## 📝 Changelog

### v1.0.0
- Initial release
- Base plugin architecture
- Configuration management system
- Command and event registries
- Service manager
- Permission manager
- Cache and cooldown services
- Comprehensive utility classes
- Text formatting utilities
- Message builder API
- Time and validation utilities

## 📄 License

This project is licensed under the MIT License.

## 👨‍💻 Author

**M4trixDev**

- GitHub: [@m4trixdev](https://github.com/m4trixdev)

## 🤝 Contributing

Contributions are welcome! Feel free to:
- Report bugs
- Suggest new features
- Submit pull requests
- Improve documentation

## 📞 Support

- Issues: [GitHub Issues](https://github.com/m4trixdev/PluginFramework/issues)
- Discussions: [GitHub Discussions](https://github.com/m4trixdev/PluginFramework/discussions)

---

Made with ❤️ for the Minecraft plugin development community
