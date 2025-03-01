# Minecraft Heal Command Mod

This is a simple Fabric mod that adds a `/heal` command to Minecraft, allowing players to heal themselves or others. This mod supports **single-player**, **integrated/LAN servers**, and **dedicated servers**.

## Command Usage
```
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
- Similar to the `/damage` command, the `<amount>` parameter must be greater than 0.
- Unlike the `/damage` command, multiple players can be affected at once.
- Players can be healed regardless of their gamemode, given that they are alive.
