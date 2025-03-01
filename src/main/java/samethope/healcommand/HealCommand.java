package samethope.healcommand;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HealCommand implements ModInitializer {
    public static final String MOD_ID = "heal-command";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(HealCommand::registerCommandListener);
    }

    public static void registerCommandListener(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("heal")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> executeHealCommand(context, Collections.singleton(context.getSource().getPlayerOrThrow()), -1))
                .then(CommandManager.argument("amount", FloatArgumentType.floatArg(0))
                        .executes(context -> executeHealCommand(context, Collections.singleton(context.getSource().getPlayerOrThrow()), FloatArgumentType.getFloat(context, "amount")))
                        .then(CommandManager.argument("targets", EntityArgumentType.players())
                                .executes(context -> executeHealCommand(context, EntityArgumentType.getPlayers(context, "targets"), FloatArgumentType.getFloat(context, "amount")))
                        )
                )
                .then(CommandManager.argument("targets", EntityArgumentType.players())
                        .executes(context -> executeHealCommand(context, EntityArgumentType.getPlayers(context, "targets"), -1))
                        .then(CommandManager.argument("amount", FloatArgumentType.floatArg(0))
                                .executes(context -> executeHealCommand(context, EntityArgumentType.getPlayers(context, "targets"), FloatArgumentType.getFloat(context, "amount")))
                        )
                )
        );
    }

    private static int executeHealCommand(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> targets, float amount) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        boolean isFullHeal = amount == -1;
        List<ServerPlayerEntity> healedPlayers = new ArrayList<>(targets.stream().toList());

        for (ServerPlayerEntity target : targets) {
            if (amount != -1 && amount <= 0) {
                healedPlayers.remove(target);
                continue;
            }
            if (!target.isAlive()) {
                if (targets.size() <= 1) {
                    source.sendError(Text.literal(String.format("%s can't be healed because they are dead", target.getName().getString())));
                }
                healedPlayers.remove(target);
                continue;
            }
            if (target.getHealth() == target.getMaxHealth()) {
                if (targets.size() <= 1) {
                    source.sendError(Text.literal(String.format("%s is already at full health", target.getName().getString())));
                }
                healedPlayers.remove(target);
                continue;
            }

            if (isFullHeal) target.setHealth(target.getMaxHealth());
            else target.setHealth(Math.min(target.getHealth() + amount, target.getMaxHealth()));
        }

        final int finalHealedPlayersSize = healedPlayers.size();
        if (finalHealedPlayersSize == 0) {
            if (targets.size() > 1 || (amount != -1 && amount <= 0)) {
                source.sendError(Text.literal("No players were healed"));
            }
            return 0;
        }

        String message;
        if (finalHealedPlayersSize == 1) {
            String playerName = healedPlayers.getFirst().getName().getString();
            message = isFullHeal
                    ? String.format("Fully healed %s", playerName)
                    : String.format("Applied %s heal to %s", amount, playerName);
        } else {
            message = isFullHeal
                    ? String.format("Fully healed %d players", finalHealedPlayersSize)
                    : String.format("Applied %s heal to %d players", amount, finalHealedPlayersSize);
        }

        source.sendFeedback(() -> Text.literal(message), true);

        return Command.SINGLE_SUCCESS;
    }
}