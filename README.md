# AuthBB - Enhanced Boss Bar Integration for AuthMe [Proxy Teleport - Multi Lobby Support]

AuthBB is a handy Spigot plugin that makes logging in and registering on your Minecraft server easier. It works smoothly with AuthMe and adds a cool boss bar interface along with extra features to boost server management and player interaction.

![AuthBB Login GIF](https://altkat.github.io/AuthBB/login.gif)

## Important Notes:
This plugin is built for modern Minecraft versions and has been tested on 1.16.5 - 1.21+. While it may work on other versions, compatibility is not guaranteed.

## Key Features:
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

