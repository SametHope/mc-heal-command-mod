# Minecraft Heal Command Mod

## Overview
This is a simple fabric mod that adds a `/heal` command to Minecraft, allowing players to heal themselves or others. This mod supports **single-player**, **integrated/LAN servers**, and **dedicated servers**.

### Features:
- Heals players with the `/heal` command.
- Works with modified maximum health values.
- Requires **server-side installation only** (ignored on clients).

## Command Usage
```sh
/heal [<targets>] [<amount>]
/heal [<amount>] [<targets>]
```

### Parameters:
- `<targets>` (optional) – The player(s) to heal. Defaults to the command executor.
- `<amount>` (optional) – The amount of health to restore. Defaults to full health.

## Permissions
- Requires **operator permissions (level 2)** or higher to use.

## Notes
- Defaults to **self-heal** if no target is specified.
- Defaults to **full heal** if no amount is specified.
- Works with modified maximum health values.
- Not required on clients for dedicated and integrated/LAN servers.

