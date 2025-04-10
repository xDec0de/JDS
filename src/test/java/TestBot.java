import net.codersky.mcsb.MCSkyBot;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class TestBot extends MCSkyBot {

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
