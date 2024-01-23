package io.github._4drian3d.v1211;

import com.google.inject.Inject;
import com.mojang.brigadier.Command;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

@Plugin(
		id = "velocity1211test",
		name = "Velocity1211Test",
		description = "A Velocity Plugin Template",
		version = Constants.VERSION,
		authors = {"4drian3d"}
)
public final class VelocityPlugin {
	@Inject
	private ComponentLogger logger;
    @Inject
    private CommandManager commandManager;
    @Inject
    private EventManager eventManager;

	@Subscribe
	void onProxyInitialization(final ProxyInitializeEvent event) {
        logger.info("Starting tests for Velocity#1211");
		final var node = BrigadierCommand.literalArgumentBuilder("test1211")
			.then(BrigadierCommand.literalArgumentBuilder("unregister")
				.executes(ctx -> {
					eventManager.unregisterListeners(this);
					ctx.getSource().sendMessage(miniMessage().deserialize("<rainbow:!>Unregistered listeners"));
					return Command.SINGLE_SUCCESS;
				})
            )
			.then(BrigadierCommand.literalArgumentBuilder("register")
				.executes(ctx -> {
					eventManager.register(this, this);
					ctx.getSource().sendMessage(miniMessage().deserialize("<rainbow>Registered main class listener"));
					return Command.SINGLE_SUCCESS;
				})
            )
			.then(BrigadierCommand.literalArgumentBuilder("fire")
				.executes(ctx -> {
                    eventManager.fireAndForget(eventManager);
                    ctx.getSource().sendMessage(miniMessage().deserialize("<green>Event executed"));
                    return Command.SINGLE_SUCCESS;
                })
            );
        commandManager.register(new BrigadierCommand(node));
	}

    public record TestEvent() {}

	@Subscribe
	public void onTestEvent(TestEvent event) {
		logger.info(miniMessage().deserialize("<red>Event executed " + System.currentTimeMillis()));
	}
}