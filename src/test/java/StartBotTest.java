import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StartBotTest {

	private final String token;
	final File dataFolder = Path.of("./src/test/run/").toFile();
	final TestBot bot = new TestBot(dataFolder);

	public StartBotTest() throws IOException {
		final File tokenFile = new File(dataFolder, "/token");
		if (!tokenFile.exists()) {
			dataFolder.mkdirs();
			tokenFile.createNewFile();
		}
		token = Files.readString(tokenFile.toPath());
	}

	@Test
	public void testBotStart() {
		bot.getConfig().setString("token", token);
		bot.getConfig().save();
		Assertions.assertDoesNotThrow(bot::start);
		bot.getConfig().setString("token", "");
		bot.getConfig().save();
	}

	@Test
	public void testBotManually() {
		while (bot.getCLICommandManager().isRunning())
			continue;
		Assertions.assertTrue(true);
	}
}
