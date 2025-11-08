# AuthBB - Enhanced Boss Bar Integration for AuthMe [Proxy Teleport - Multi Lobby Support]

AuthBB is a handy Spigot plugin that makes logging in and registering on your Minecraft server easier. It works smoothly with AuthMe and adds a cool boss bar interface along with extra features to boost server management and player interaction.

![AuthBB Login GIF](https://altkat.github.io/AuthBB/login.gif)

<div align="center"><b>Important!</b></div><br>
<div align="center"><b>This plugin requires AuthMe installed to work!</b><br><br>This plugin requires Java 17 or newer.<br>
<br><br>
</div>

## Features:
- **Tested on Minecraft versions** 1.16.5 - 1.21+.
- **Login and Register Boss Bar:** Automatically creates a customizable boss bar with a countdown timer on the login and register screen, making the authentication process more engaging for players.
- **Fully Customizable:** Offers extensive customization options for titles, boss bars, messages, and timers.
- **Intelligent Proxy Teleport Support:** Automatically detects BungeeCord and Velocity environments. It also warns about critical misconfigurations (e.g., both enabled at once) and includes a `force-proxy` option for unsupported setups.
- **Multiple Lobby Support:** Configure multiple lobby servers, and AuthBB will randomly send players to one of them after login.
- **Automatic Updaters:** Features a Config Updater to seamlessly add new options on plugin updates and an Update Checker to notify admins of new versions.
- **Auto Kick Feature:** A configurable kick mechanism that removes players if they fail to authenticate in time.
- **Session Support:** Works seamlessly with AuthMe's session feature.

## Some Extra Features:
- **Teleport on Join**
- **Make Players Invisible**
- **Prevent Movement**
- **Disable Chat**
- **Remove Join and Leave Messages**

## Commands and Permissions:
- **Main Command:** `/authbb` (Alias: `/abb`)
- **Subcommands:**
  - `/authbb help`: Displays the help menu.

  - `/authbb server <servername>`: Lets players connect to a configured lobby server.

    - Permission: `AuthBB.server` (default: true for all players)

  - `/authbb send <player> <servername>`: Sends a player to a configured lobby server.

    - Permission: `AuthBB.send` (default: op)

  - /authbb reload: Reloads the plugin's configuration file.

    - Permission: `AuthBB.reload` (default: op)

- **Admin Permission:**
  - `AuthBB.admin`: A parent permission that grants access to all admin commands (send, reload, and the admin help menu).

---

# Some Visuals from AuthBB
### **Register Screen**  
![Register](https://altkat.github.io/AuthBB/register.gif)

### **Kick System**  
![Kick System](https://altkat.github.io/AuthBB/kick.gif)

### **Server Command**  
![Server Command Full](https://altkat.github.io/AuthBB/server%20command.gif)
![Server Command](https://altkat.github.io/AuthBB/update/server.png)

### **Send Command**  
![Send Command Full](https://altkat.github.io/AuthBB/send%20command.gif)
![Server Command](https://altkat.github.io/AuthBB/update/send.png)

---

## Contact Me:
If you need any help or have suggestion, please feel free to contact me.
**Discord ID:** [streetmelodeez](https://discordapp.com/users/247441109888925697)

---

## Config File

<details>
  <summary>Click to see default config</summary>

```yaml
# ######################################################################################################
# ##                                                                                                  ##
# ##   AuthBB v1.4 by Altkat(StreetMelodeez)                                                          ##
# ##   Discord: streetmelodeez                                                                        ##
# ##   Please use /authbb reload to apply changes.                                                    ##
# ##                                                                                                  ##
# ######################################################################################################

# ---------------------------------------------------------------------------------------------------- #
# Proxy Settings
# Used for proxy systems like BungeeCord and Velocity.
# ---------------------------------------------------------------------------------------------------- #
Proxy:
  # Set to 'true' to enable proxy features.
  # Ensure your server is correctly configured in proxy mode (in spigot.yml or paper-global.yml).
  enabled: false
  # A list of lobby servers to send players to after they log in.
  # If you add multiple servers, a random one will be chosen.
  servers:
    - lobby
    - lobby2
  # The delay in seconds before sending a player to a server after they have logged in.
  delay: 3
  force-proxy: false #Set this to true ONLY IF you are having AN ISSUE with proxy teleport.(It bypasses the bungee/velocity check and force enables the proxy mode)

# ---------------------------------------------------------------------------------------------------- #
# Boss Bar Settings
# Configures the countdown boss bars shown to players on join.
# IMPORTANT: Valid options for 'color': RED, YELLOW, BLUE, GREEN, PINK, PURPLE, WHITE.
# IMPORTANT: Valid options for 'style': SOLID, SEGMENTED_6, SEGMENTED_10, SEGMENTED_12, SEGMENTED_20.
# ---------------------------------------------------------------------------------------------------- #
LoginBossBar:
  title: "&6Please login in &a%time% &6seconds."
  color: "GREEN"
  style: "SEGMENTED_10"
  time: 30

RegisterBossBar:
  title: "&bPlease register in &a%time% &6seconds."
  color: "BLUE"
  style: "SEGMENTED_10"
  time: 30

# ---------------------------------------------------------------------------------------------------- #
# Title Settings
# Configures the large text titles shown to players on join or while connecting.
# fadein, stay, and fadeout durations are in seconds.
# ---------------------------------------------------------------------------------------------------- #
LoginTitle:
  title: "&6Please Login"
  subtitle: "&f/login password"
  fadein: 2
  stay: 5
  fadeout: 2

RegisterTitle:
  title: "&bPlease register"
  subtitle: "&f/register password password"
  fadein: 2
  stay: 5
  fadeout: 2

# This title only works if 'Proxy.enabled' is set to true.
ConnectingTitle:
  title: "&fConnecting"
  subtitle: "&cYou are being connected to the lobby"
  fadein: 2
  stay: 5
  fadeout: 2

# ---------------------------------------------------------------------------------------------------- #
# Kick System
# ---------------------------------------------------------------------------------------------------- #
Kick:
  # If 'true', players will be kicked when the BossBar timer runs out.
  # If you are using AuthMe's own kick feature, make sure the timings match to avoid conflicts.
  enabled: false
  login-message: "You were kicked because you didn't log in in time"
  register-message: "You were kicked because you did not register in time"

# ---------------------------------------------------------------------------------------------------- #
# Extra Features
# ---------------------------------------------------------------------------------------------------- #
Extras:
  # Instantly teleports the player to the coordinates below upon joining the server.
  teleport-on-join: false
  teleport-coordinates:
    - 0.0 # x coordinate
    - 0.0 # y coordinate
    - 0.0 # z coordinate
    - 0.0 # yaw value
    - 0.0 # pitch value
  # Gives players an invisibility effect until they log in.
  makeInvisible: false

  # If your spawn point is in the void and players are falling upon join, do not enable this option.
  preventMovement: false # Prevents players from moving. AuthMe already handles this for unauthenticated players; this can be used to also prevent movement after they have logged in.

  disableChat: false # Prevents players from sending messages. AuthMe already handles this for unauthenticated players; this can be used to also prevent messages after they have logged in.

  # Removes the default join message when a player connects.
  removeJoinMessage: false
  # Removes the default quit message when a player disconnects.
  removeLeaveMessage: false

# ---------------------------------------------------------------------------------------------------- #
# Language & Messages
# All user-facing messages for commands and system feedback.
# ---------------------------------------------------------------------------------------------------- #
Messages:
  only-players: "&4Only players can use this command!"
  no-permission: "&4You don't have the permission to use this command!"
  wrong-usage-server: "&cWrong usage! Please type &a/server servername."
  wrong-usage-send: "&cWrong usage! Please type &a/send (player) (server)"
  disabled: "&cThis feature is disabled in the config file."
  send-success-sender: "&aYou are sending player %player% to the server %server%."
  send-success-sent: "&aYou are being connected to the lobby by an admin."
  player-not-found: "&4Player not found!"
  server-not-found: "&4Server not found!"
  not-authenticated: "&4Player is not authenticated!"
  player-already-connecting: "&4Player is already being connected!"

```
</details>


