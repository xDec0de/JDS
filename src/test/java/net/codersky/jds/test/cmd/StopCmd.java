package net.codersky.jds.test.cmd;

import net.codersky.mcsb.JDSkyBot;
import net.codersky.mcsb.cmd.JDSCommand;
import net.codersky.mcsb.cmd.JDSCommandInteraction;
import org.jetbrains.annotations.NotNull;

public class StopCmd extends JDSCommand<JDSkyBot> {

	public StopCmd(@NotNull JDSkyBot bot) {
		super(bot, "stop", "Stops the bot!");
	}

	@Override
	public boolean onSlashCommand(JDSCommandInteraction<JDSkyBot> interaction) {
		interaction.deferReply(true);
		interaction.replyCustom("Stopping!");
		System.out.println("Stopping bot now.");
		getBot().stop();
		return true;
	}
}
