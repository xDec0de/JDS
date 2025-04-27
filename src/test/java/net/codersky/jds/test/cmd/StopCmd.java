package net.codersky.jds.test.cmd;

import net.codersky.jds.JDSBot;
import net.codersky.jds.cmd.JDSCommand;
import net.codersky.jds.cmd.JDSCommandInteraction;
import org.jetbrains.annotations.NotNull;

public class StopCmd extends JDSCommand {

	private final JDSBot bot;

	public StopCmd(@NotNull JDSBot bot) {
		super("stop", "Stops the bot!");
		this.bot = bot;
	}

	@Override
	public boolean onSlashCommand(JDSCommandInteraction interaction) {
		interaction.deferReply(true);
		interaction.replyRaw("Stopping!");
		System.out.println("Stopping bot now.");
		bot.stop();
		return true;
	}
}
