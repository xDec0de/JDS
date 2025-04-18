package net.codersky.jds.cmd;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

/**
 * {@link JDSICommand} implementation designed for base commands that
 * won't be executed. This implementation only allows other
 * {@link JDSCommand subcommands} to be added to it, acting as a main
 * base command.
 *
 * @author xDec0de_
 *
 * @since JDSky 1.0.0
 */
public class JDSBaseCommand implements JDSICommand {

	private final SlashCommandData data;

	public JDSBaseCommand(@NotNull String name, @NotNull String desc) {
		this.data = Commands.slash(name, desc);
	}

	@NotNull
	@Override
	public SlashCommandData getSlashCommandData() {
		return data;
	}

	/**
	 * Adds up to {@link CommandData#MAX_OPTIONS 25} {@link JDSCommand commands}
	 * as subcommands of this {@link JDSBaseCommand}.
	 *
	 * @param subcommands The {@link JDSCommand subcommands} to add to this {@link JDSCommand}.
	 *
	 * @return This {@link JDSCommand}, the base command.
	 */
	public JDSBaseCommand addSubCommand(@NotNull JDSCommand<?>... subcommands) {
		final SubcommandData[] data = new SubcommandData[subcommands.length];
		for (int i = 0; i < subcommands.length; i++)
			data[i] = SubcommandData.fromData(subcommands[i].getSlashCommandData().toData());
		this.data.addSubcommands(data);
		return this;
	}
}
