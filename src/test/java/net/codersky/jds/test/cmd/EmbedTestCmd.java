package net.codersky.jds.test.cmd;

import net.codersky.jds.cmd.JDSCommand;
import net.codersky.jds.cmd.JDSCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class EmbedTestCmd extends JDSCommand {

	public EmbedTestCmd() {
		super("embedraw", "Test embed format");
		addOption(OptionType.STRING, "embed", "The embed to send", true);
	}

	@Override
	public boolean onSlashCommand(JDSCommandInteraction interaction) {
		final String raw = interaction.asString("embed", "JDSky!");
		interaction.deferReply();
		return interaction.reply(raw.replace("\\n", "\n"));
	}
}
