# BetterForgeChat
A Forge based serverside chat mod to allow prefixes, suffixes and integration with LuckPerms and FTB-Essentials nicknames
***This mod is still a WIP, however, if you are building a modded server on MC1.18.2 there unfortunately is no alternative available due to SpongeForge, Spigot, etc. not supporting MC1.18.2 - this mod only supports MC1.18.2 forge servers for this reason***

## Features
### LuckPerms integration
![screenshots/LuckPermsIntegration.png](https://github.com/abc123me/BetterForgeChat/raw/main/screenshots/LuckPermsIntegration.png)
### Tab list integration
![screenshots/TabListIntegration.png](https://github.com/abc123me/BetterForgeChat/raw/main/screenshots/TabListIntegration.png)
### Nickname integration
![screenshots/NicknameAndGroupIntegreation.png](https://github.com/abc123me/BetterForgeChat/raw/main/screenshots/NicknameAndGroupIntegreation.png)
### Colors command
![screenshots/ColorsCommand.png](https://github.com/abc123me/BetterForgeChat/raw/main/screenshots/ColorsCommand.png)

## Installation
- Add JAR file to serverside `mods/` folder
  - Latest version: [Beta 0.1](https://github.com/abc123me/BetterForgeChat/releases/tag/MC1.18.2)
- *LuckPerms must also be installed on server*
- *FTB Essentials must also be installed on server*
- Profit!

## Important disclaimer
I have literally built this mod in one afternoon, and it still needs polish however the core code is quite secure and works extremely well, there are some key things however I have not yet implemented:
- Chat color permissions, anyone can use chat colors in chat regardless of status 
- Chat styling permissions, anyone can use chat styles in chat regardless of status 
- Basic mod configuration file
  - No way to enable / disable tab list formatting or nicknames
  - A server might want to disable tab list nicknames to ensure clarity
  - No way to configure chat timestamp

## Development
I used eclipse to make this project, all files included, please feel free to do whatever you want with it
