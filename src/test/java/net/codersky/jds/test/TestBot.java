package net.codersky.jds.test;

import net.codersky.jds.BotStartResult;
import net.codersky.jds.JDSBot;
import net.codersky.jds.token.FileTokenProvider;
import net.codersky.jsky.cli.CLICommandManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class TestBot extends JDSBot {

	private final String token;

	public TestBot(@NotNull File dataFolder, @Nullable String altToken) {
		super(new CLICommandManager());
		if (altToken != null)
			this.token = altToken;
		else
			this.token = new FileTokenProvider(new File(dataFolder, "/token")).getToken();
	}

	@NotNull
	public BotStartResult start() {
		return super.start(token);
	}

	@NotNull
	@Override
	public CLICommandManager getCLICommandManager() {
		return super.getCLICommandManager();
	}
}
