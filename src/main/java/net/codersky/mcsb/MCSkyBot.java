package net.codersky.mcsb;

import net.codersky.jsky.cli.CLICommandManager;
import net.codersky.jsky.yaml.YamlFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public abstract class MCSkyBot {

	private final File dataFolder;
	private final YamlFile cfg;
	private final CLICommandManager cliCommandManager = new CLICommandManager();

	public MCSkyBot(@NotNull File dataFolder) {
		this.dataFolder = dataFolder;
		this.cfg = new YamlFile(dataFolder, "config.yml");
	}

	/*
	 - Bot start
	 */

	protected abstract void onStart();

	public final void start() {
		setupCLI();
		setupConfig();
		onStart();
	}

	private void setupCLI() {
		cliCommandManager.start();
		cliCommandManager.registerConsumer("stop", args -> {
			cliCommandManager.stop();
			onStop(args);
			afterStop();
		});
	}

	private void setupConfig() {
		cfg.setup(err -> {
			System.err.println("Failed to setup config file - " + err.getMessage() + ":");
			err.printStackTrace(System.err);
			ExitCode.CONFIG_SETUP_FAIL.exit();
		});
	}

	protected abstract void onStop(@NotNull String @NotNull [] args);

	private void afterStop() {
		ExitCode.OK.exit();
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
}
