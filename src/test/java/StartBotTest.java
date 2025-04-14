import net.codersky.mcsb.BotStartResult;
import net.dv8tion.jda.api.entities.Guild;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StartBotTest {

	private final String token;
	private final long guild;
	final File dataFolder = Path.of("./src/test/run/").toFile();
	final TestBot bot = new TestBot(dataFolder);

	public StartBotTest() throws IOException {
		final File tokenFile = new File(dataFolder, "/token");
		final File guildFile = new File(dataFolder, "/guild");
		if (!tokenFile.exists() || !guildFile.exists()) {
			dataFolder.mkdirs();
			tokenFile.createNewFile();
			guildFile.createNewFile();
		}
		token = Files.readString(tokenFile.toPath());
		guild = Long.valueOf(Files.readString(guildFile.toPath()));
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
		bot.getConfig().setString("token", token);
		bot.getConfig().save();
		Assertions.assertEquals(BotStartResult.OK, bot.start());
		Assertions.assertNotNull(bot.getJDA());
		final Guild guild = bot.getJDA().getGuildById(this.guild);
		if (guild == null)
			return;
		bot.addCommands(guild, new EmbedTestCmd(bot));
		while (bot.getCLICommandManager().isRunning())
			continue;
		Assertions.assertTrue(true);
	}
}
