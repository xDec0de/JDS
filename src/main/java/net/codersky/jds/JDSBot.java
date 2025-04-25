package net.codersky.jds;

import net.codersky.jds.cmd.JDSICommand;
import net.codersky.jds.message.JDSMessagesFile;
import net.codersky.jsky.cli.CLICommandManager;
import net.codersky.jsky.yaml.YamlFile;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class JDSBot {

	private JDA jda;

	/*
	 - Bot start
	 */

	protected void onStart() {}

	public final BotStartResult start() {
		BotStartResult res;
		setupCLI();
		if (!(res = setupConfig()).isOk())
			return res;
		if (!(res = setupJDA()).isOk())
			return res;
		onStart();
		return res;
	}

	private void setupCLI() {
		final CLICommandManager manager = getCLICommandManager();
		if (manager == null)
			return;
		manager.registerConsumer("stop", args -> {
			manager.stop();
			stop(args);
			afterStop();
		});
		manager.start();
	}

	private BotStartResult setupConfig() {
		final YamlFile cfg = getConfig();
		if (cfg == null)
			return BotStartResult.OK;
		return cfg.setup(err -> {
			System.err.println("Failed to setup config file - " + err.getMessage() + ":");
			err.printStackTrace(System.err);
		}) ? BotStartResult.OK : BotStartResult.CONFIG_SETUP_FAIL;
	}

	private BotStartResult setupJDA() {
		final String token = getToken();
		if (token.isEmpty())
			return BotStartResult.NO_BOT_TOKEN;
		jda = getJDABuilder(token).build();
		try {
			jda.awaitReady();
		} catch (InterruptedException e) {
			System.err.println("Failed JDA#awaitReady, reason: " + e.getMessage());
			return BotStartResult.JDA_SETUP_FAIL;
		}
		return BotStartResult.OK;
	}

	/**
	 * Gets the {@link JDABuilder} used to build a {@link JDA}
	 * instance when this bot is {@link #start() started}.
	 * By default, {@link JDABuilder#createDefault(String)} is
	 * used, but you can override this method to use your own.
	 *
	 * @param token The token of the bot, obtained via
	 * {@link #getToken()}. You don't need to verify the token,
	 * JDSky will take care of that for you.
	 *
	 * @return A {@link JDABuilder} made with the provided bot
	 * {@code token}.
	 *
	 * @since JDS 1.0.0
	 */
	@NotNull
	protected JDABuilder getJDABuilder(@NotNull String token) {
		return JDABuilder.createDefault(token);
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
	 * is needed before the bot actually shuts down. JSky will do the
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
		final CLICommandManager cli = getCLICommandManager();
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
	 * This can be {@code null}, in which case, JDSky won't register
	 * the built-in stop CLI command. We highly recommend to use
	 * a {@link CLICommandManager}, as it provides control over your
	 * bot from the command line, but do as you please.
	 *
	 * @return The {@link CLICommandManager} of this {@link JDSBot},
	 * can be {@code null} if the bot doesn't support CLI commands.
	 *
	 * @since JDS 1.0.0
	 */
	@Nullable
	public CLICommandManager getCLICommandManager() {
		return null;
	}

	/**
	 * Gets the {@link YamlFile} that provides configuration options
	 * for this {@link JDSBot}. This is optional, and may be {@code null}.
	 * But if you opt to not have a config file on your bot, then you must
	 * override the {@link #getToken()} method with a way to get your bot
	 * token. If you do provide a {@link YamlFile} instance, then JDSky sets
	 * it up for you automatically. By default, the token is expected to
	 * be located at the "token" path of your config.
	 *
	 * @return The {@link YamlFile config} file of this {@link JDSBot}.
	 * May be {@code null} if the bot doesn't have a config file.
	 *
	 * @since JDS 1.0.0
	 */
	@Nullable
	public YamlFile getConfig() {
		return null;
	}

	/**
	 * Gets the token used for this bot. By default, this token is obtained
	 * from {@link #getConfig()}, at the "token" path of it. If
	 * {@link #getConfig()} is {@code null}. Then an empty string is returned.
	 * You can of course override this method if you want to implement your
	 * own logic to get your bot token.
	 *
	 * @return The token of this {@link JDSBot}.
	 */
	@NotNull
	protected String getToken() {
		final YamlFile cfg = getConfig();
		return cfg == null ? "" : cfg.getString("token", "");
	}

	@Nullable
	public JDSMessagesFile getMessages() {
		return null;
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
