package dev.efl.earthmc.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import dev.efl.earthmc.config.EflConfigStore;
import dev.efl.earthmc.timer.PlayTimer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.network.chat.Component;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public final class EflClientCommands {
    private EflClientCommands() {
    }

    public static void register(EflConfigStore configStore, PlayTimer playTimer) {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
                literal("efl")
                        .then(literal("timer")
                                .then(literal("test")
                                        .executes(context -> {
                                            playTimer.start();
                                            context.getSource().sendFeedback(Component.literal("EFL timer started."));
                                            return 1;
                                        })))
                        .then(literal("trigger")
                                .then(literal("list")
                                        .executes(context -> {
                                            context.getSource().sendFeedback(Component.literal("EFL triggers: " + String.join(", ", configStore.config().triggerWords())));
                                            return configStore.config().triggerWords().size();
                                        }))
                                .then(literal("add")
                                        .then(argument("trigger", StringArgumentType.greedyString())
                                                .executes(context -> {
                                                    String trigger = StringArgumentType.getString(context, "trigger");
                                                    boolean added = configStore.addTrigger(trigger);
                                                    context.getSource().sendFeedback(Component.literal(added
                                                            ? "Added EFL trigger: " + trigger.trim()
                                                            : "EFL trigger already exists: " + trigger.trim()));
                                                    return added ? 1 : 0;
                                                })))
                                .then(literal("remove")
                                        .then(argument("trigger", StringArgumentType.greedyString())
                                                .executes(context -> {
                                                    String trigger = StringArgumentType.getString(context, "trigger");
                                                    boolean removed = configStore.removeTrigger(trigger);
                                                    context.getSource().sendFeedback(Component.literal(removed
                                                            ? "Removed EFL trigger: " + trigger.trim()
                                                            : "EFL trigger was not configured: " + trigger.trim()));
                                                    return removed ? 1 : 0;
                                                })))
                                .then(literal("reset")
                                        .executes(context -> {
                                            configStore.resetTriggers();
                                            context.getSource().sendFeedback(Component.literal("EFL triggers reset to defaults."));
                                            return 1;
                                        })))));
    }
}
