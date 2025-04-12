package net.codersky.mcsb.cmd;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class MSBCommand extends ListenerAdapter {

	private SlashCommandData data;

	/**
	 * Called whenever a {@link User} executes this {@link MSBCommand}.
	 *
	 * @param interaction the {@link MSBCommandInteraction} that executed this command.
	 *
	 * @return Doesn't matter, returning either {@code true} or {@code false} does nothing.
	 * You can use the return value to {@link MSBCommandInteraction#reply(String) reply}
	 * to the {@code interaction} and {@code return} in one line.
	 *
	 * @since MCSkyBot 1.0.0
	 *
	 * @see MSBCommandInteraction#reply(String)
	 * @see MSBCommandInteraction#replyCustom(String)
	 */
	public abstract boolean onSlashCommand(MSBCommandInteraction interaction);
}
