import net.codersky.jsky.cli.CLICommandManager;
import net.codersky.jsky.yaml.YamlFile;
import net.codersky.mcsb.JDSkyBot;
import net.codersky.mcsb.message.JDSMessagesFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class TestBot extends JDSkyBot {

	private final CLICommandManager cli = new CLICommandManager();
	private final YamlFile cfg;
	private final JDSMessagesFile msg;

	public TestBot(@NotNull File dataFolder) {
		this.cfg = new YamlFile(dataFolder, "config.yml");
		this.msg = new JDSMessagesFile(dataFolder, "messages.yml");
	}

	@Override
	protected void onStart() {

	}

	@Override
	protected void onStop(@NotNull String @NotNull [] args) {

	}

	@Override
	public @Nullable CLICommandManager getCLICommandManager() {
		return cli;
	}

	@Override
	public @Nullable YamlFile getConfig() {
		return cfg;
	}

	@Override
	public @Nullable JDSMessagesFile getMessages() {
		return msg;
	}
}
