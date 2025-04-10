import net.codersky.mcsb.BotStartResult;
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
		Assertions.assertEquals(bot.start(), BotStartResult.NO_BOT_TOKEN);
		bot.getConfig().setString("token", token);
		bot.getConfig().save();
		Assertions.assertEquals(bot.start(), BotStartResult.OK);
		bot.getConfig().setString("token", "");
		bot.getConfig().save();
	}

	@Test
	public void testBotCLI() {
		bot.start();
		Assertions.assertTrue(bot.getCLICommandManager().isRunning());
	}

	@Test
	public void testBotManually() {
		bot.start();
		while (bot.getCLICommandManager().isRunning())
			continue;
		Assertions.assertTrue(true);
	}
}
