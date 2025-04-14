import net.codersky.jsky.cli.CLICommandManager;
import net.codersky.jsky.yaml.YamlFile;
import net.codersky.mcsb.JDSkyBot;
import net.codersky.mcsb.message.JDSMessagesFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TestBot extends JDSkyBot {

	private final CLICommandManager cli = new CLICommandManager();
	private final YamlFile cfg;
	private final JDSMessagesFile msg;
	private final String token;

	public TestBot(@NotNull File dataFolder, @Nullable String altToken) throws IOException {
		this.cfg = new YamlFile(dataFolder, "config.yml");
		this.msg = new JDSMessagesFile(dataFolder, "messages.yml");
		if (altToken != null) {
			this.token = altToken;
			return;
		}
		final File tokenFile = new File(dataFolder, "/token");
		if (!dataFolder.exists())
			dataFolder.mkdirs();
		if (!tokenFile.exists())
			tokenFile.createNewFile();
		token = Files.readString(tokenFile.toPath());
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

	@NotNull
	@Override
	protected String getToken() {
		return token;
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
