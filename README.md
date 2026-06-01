# EFL EarthMC

Client-side Fabric mod for EFL players on EarthMC.

**WIP! Goal will be to track stats in the future and make playing within the EFL as easy as possible!**

## Current Features

- Starts a small HUD timer when a configured play-start trigger appears in `[Local]` player chat.
- Shows a red `Blitz in _` countdown for the first 4 seconds.
- Turns green as `Blitz now!` after 4 seconds so players and refs can see when the QB can be tackled.
- Includes client commands for trigger setup:
  - `/efl trigger list`
  - `/efl trigger add <word or phrase>`
  - `/efl trigger remove <word or phrase>`
  - `/efl trigger reset`
  - `/efl timer test`

## Baseline

- Minecraft: 1.21.11
- Java: 21
- Fabric Loader: 0.19.2
- Fabric API: 0.141.4+1.21.11
- Mappings: official Mojang mappings

## Build

```powershell
.\gradlew.bat build
```
