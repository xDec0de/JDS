package net.codersky.mcsb;

import net.codersky.jsky.cli.CLICommandManager;
import net.codersky.jsky.yaml.YamlFile;
import net.codersky.mcsb.message.JDSMessagesFile;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public abstract class JDSkyBot {

	private final File dataFolder;
	private final YamlFile cfg;
	private final CLICommandManager cliCommandManager = new CLICommandManager();
	private JDA jda;

	public JDSkyBot(@NotNull File dataFolder) {
		this.dataFolder = dataFolder;
		this.cfg = new YamlFile(dataFolder, "config.yml");
	}

	/*
	 - Bot start
	 */

	protected abstract void onStart();

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
		cliCommandManager.registerConsumer("stop", args -> {
			cliCommandManager.stop();
			stop(args);
			afterStop();
		});
		cliCommandManager.start();
	}

	private BotStartResult setupConfig() {
		return cfg.setup(err -> {
			System.err.println("Failed to setup config file - " + err.getMessage() + ":");
			err.printStackTrace(System.err);
		}) ? BotStartResult.OK : BotStartResult.CONFIG_SETUP_FAIL;
	}

	private BotStartResult setupJDA() {
		final String token = getConfig().getString("token", "");
		if (token.isEmpty())
			return BotStartResult.NO_BOT_TOKEN;
		jda = JDABuilder.createDefault(token).build();
		try {
			jda.awaitReady();
		} catch (InterruptedException e) {
			System.err.println("Failed JDA#awaitReady, reason: " + e.getMessage());
			return BotStartResult.JDA_SETUP_FAIL;
		}
		return BotStartResult.OK;
	}

	/*
	 - Bot stop
	 */

	/**
	 * Stops this {@link JDSkyBot}. This is usually done by the built-in
	 * stop CLI command, but you can also stop the bot manually with this
	 * method.
	 *
	 * @param args The arguments used when stopping the bot. This array
	 * must not be {@code null} nor contain {@code null} elements. The
	 * array can be empty. This just emulates arguments being passed to
	 * the built-in stop CLI command.
	 *
	 * @since JDSkyBot 1.0.0
	 */
	public void stop(final @NotNull String @NotNull [] args) {
		Objects.requireNonNull(args, "Stop args array cannot be null.");
		for (final String arg : args)
			Objects.requireNonNull(arg, "Stop args array cannot contain null elements.");
		onStop(args);
		afterStop();
	}

	protected abstract void onStop(final @NotNull String @NotNull [] args);

	private void afterStop() {
		jda.shutdownNow();
	}

	/*
	 - Utility getters
	 */

	@NotNull
	public CLICommandManager getCLICommandManager() {
		return cliCommandManager;
	}

	@NotNull
	public File getDataFolder() {
		return dataFolder;
	}

	@NotNull
	public YamlFile getConfig() {
		return cfg;
	}

	@NotNull
	public abstract JDSMessagesFile getMessages();

	@NotNull
	public JDA getJDA() {
		return jda;
	}
}
