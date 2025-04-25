package net.codersky.jds.test.cmd;

import net.codersky.jds.JDSBot;
import net.codersky.jds.cmd.JDSCommand;
import net.codersky.jds.cmd.JDSCommandInteraction;
import org.jetbrains.annotations.NotNull;

public class StopCmd extends JDSCommand<JDSBot> {

	public StopCmd(@NotNull JDSBot bot) {
		super(bot, "stop", "Stops the bot!");
	}

	@Override
	public boolean onSlashCommand(JDSCommandInteraction<JDSBot> interaction) {
		interaction.deferReply(true);
		interaction.replyCustom("Stopping!");
		System.out.println("Stopping bot now.");
		getBot().stop();
		return true;
	}
}
