package net.codersky.jds.test.cmd;

import net.codersky.jds.cmd.JDSCommand;
import net.codersky.jds.cmd.JDSCommandInteraction;
import net.codersky.jds.test.TestBot;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

public class EmbedTestCmd extends JDSCommand<TestBot> {

	public EmbedTestCmd(@NotNull TestBot bot) {
		super(bot, "embedraw", "Test embed format");
		addOption(OptionType.STRING, "embed", "The embed to send", true);
	}

	@Override
	public boolean onSlashCommand(JDSCommandInteraction<TestBot> interaction) {
		final String raw = interaction.asString("embed", "JDSky!");
		interaction.deferReply();
		return interaction.replyCustom(raw.replace("\\n", "\n"));
	}
}
