package net.codersky.jds;

import net.codersky.jds.cmd.JDSICommand;
import net.codersky.jsky.cli.CLICommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;

public class JDSBot {

	private JDA jda;
	private final CLICommandManager cli;

	public JDSBot(@Nullable CLICommandManager cli) {
		this.cli = cli;
	}

	public JDSBot() {
		this(null);
	}

	/*
	 - Bot start
	 */

	protected void onStart() {}

	@NotNull
	public final BotStartResult start(@NotNull JDABuilder builder) {
		setupCLI();
		this.jda = builder.build();
		try {
			this.jda.awaitReady();
		} catch (InterruptedException e) {
			System.err.println("Failed JDA#awaitReady, reason: " + e.getMessage());
			return BotStartResult.JDA_SETUP_FAIL;
		}
		onStart();
		return BotStartResult.OK;
	}

	@NotNull
	public final BotStartResult start(@NotNull String token) {
		if (token.isBlank())
			return BotStartResult.NO_BOT_TOKEN;
		return start(JDABuilder.createDefault(token));
	}

	private void setupCLI() {
		if (cli == null)
			return;
		cli.registerConsumer("stop", args -> {
			stop(args);
			afterStop();
			cli.stop();
		});
		cli.start();
	}

	/*
	 - Bot stop
	 */

	/**
	 * Stops this {@link JDSBot}. This is usually done by the built-in
	 * stop CLI command, but you can also stop the bot manually with this
	 * method.
	 *
	 * @param args The arguments used when stopping the bot. This array
	 * must not be {@code null} nor contain {@code null} elements. The
	 * array can be empty. This just emulates arguments being passed to
	 * the built-in stop CLI command.
	 *
	 * @since JDS 1.0.0
	 *
	 * @see #stop()
	 * @see #onStop(String[])
	 */
	public void stop(final @NotNull String @NotNull [] args) {
		Objects.requireNonNull(args, "Stop args array cannot be null.");
		for (final String arg : args)
			Objects.requireNonNull(arg, "Stop args array cannot contain null elements.");
		onStop(args);
		afterStop();
	}

	/**
	 * Stops this {@link JDSBot} without command arguments. Stopping
	 * the bot is usually done by the built-in stop CLI command, but
	 * you can also stop the bot manually with this method.
	 *
	 * @since JDS 1.0.0
	 *
	 * @see #stop(String[])
	 * @see #onStop(String[])
	 */
	public void stop() {
		stop(new String[0]);
	}

	/**
	 * This method is automatically called whenever the bot is signaled
	 * to {@link #stop(String[]) stop}. Here, you can add any task that
	 * is needed before the bot actually shuts down. JDS will do the
	 * following for you <b>after</b> this method is called:
	 * <ul>
	 *     <li>Stop the {@link #getCLICommandManager() CLICommandManager} (If any)</li>
	 *     <li>{@link JDA#shutdown() Shutdown JDA}</li>
	 * </ul>
	 *
	 * @param args The arguments provided on the stop command, may be empty.
	 *
	 * @since JDS 1.0.0
	 */
	protected void onStop(final @NotNull String @NotNull [] args) {}

	private void afterStop() {
		if (cli != null)
			cli.stop();
		jda.shutdown();
	}

	/*
	 - Utility getters
	 */

	@NotNull
	public JDA getJDA() {
		return jda;
	}

	/**
	 * Gets the {@link CLICommandManager} of this {@link JDSBot}.
	 * This can be {@code null}, in which case, JDS won't register
	 * the built-in stop CLI command. We highly recommend to use
	 * a {@link CLICommandManager}, as it provides control over your
	 * bot from the command line, but do as you please.
	 *
	 * @return The {@link CLICommandManager} of this {@link JDSBot},
	 * can be {@code null} if the bot doesn't support CLI commands.
	 *
	 * @since JDS 1.0.0
	 */
	@UnknownNullability
	public CLICommandManager getCLICommandManager() {
		return cli;
	}

	/*
	 - Command registration
	 */

	@NotNull
	public JDSBot addCommands(@NotNull Guild guild, @NotNull JDSICommand @NotNull ... commands) {
		final SlashCommandData[] data = new SlashCommandData[commands.length];
		for (int i = 0; i < commands.length; i++) {
			data[i] = commands[i].getSlashCommandData();
			jda.addEventListener(commands[i]);
		}
		guild.updateCommands().addCommands(data).queue();
		return this;
	}
}
