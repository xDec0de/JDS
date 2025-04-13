import net.codersky.mcsb.JDSkyBot;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class TestBot extends JDSkyBot {

	public TestBot(File dataFolder) {
		super(dataFolder);
	}

	@Override
	protected void onStart() {

	}

	@Override
	protected void onStop(@NotNull String @NotNull [] args) {

	}
}
