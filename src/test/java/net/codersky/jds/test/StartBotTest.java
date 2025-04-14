package net.codersky.jds.test;

import net.codersky.jds.test.cmd.EmbedTestCmd;
import net.codersky.jds.test.cmd.StopCmd;
import net.codersky.mcsb.BotStartResult;
import net.dv8tion.jda.api.entities.Guild;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StartBotTest {

	private final long guild;
	final File dataFolder = Path.of("./src/test/run/").toFile();
	final TestBot bot = new TestBot(dataFolder, null);

	public StartBotTest() throws IOException {
		final File guildFile = new File(dataFolder, "/guild");
		if (!dataFolder.exists())
			dataFolder.mkdirs();
		if (!guildFile.exists())
			guildFile.createNewFile();
		guild = Long.valueOf(Files.readString(guildFile.toPath()));
	}

	@Test
	public void testBotStart() throws IOException {
		Assertions.assertEquals(new TestBot(dataFolder, "").start(), BotStartResult.NO_BOT_TOKEN);
		Assertions.assertEquals(bot.start(), BotStartResult.OK);
	}

	@Test
	public void testBotCLI() {
		bot.start();
		Assertions.assertTrue(bot.getCLICommandManager().isRunning());
	}

	@Test
	public void testBotManually() {
		Assertions.assertEquals(BotStartResult.OK, bot.start());
		Assertions.assertNotNull(bot.getJDA());
		final Guild guild = bot.getJDA().getGuildById(this.guild);
		if (guild == null)
			return;
		while (bot.getCLICommandManager().isRunning())
			continue;
		Assertions.assertTrue(true);
	}
}
