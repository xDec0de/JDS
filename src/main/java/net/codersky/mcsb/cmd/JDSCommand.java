package net.codersky.mcsb.cmd;

import net.codersky.mcsb.JDSkyBot;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class JDSCommand<B extends JDSkyBot> extends ListenerAdapter implements JDSICommand {

	private final B bot;
	private final SlashCommandData data;

	/**
	 * Creates a new {@link JDSCommand} owned by the provided {@link B bot}.
	 *
	 * @param bot An instance of the {@link B bot} that owns this command.
	 * @param name The name of the command, 1 to 32 lowercase alphanumeric characters long.
	 * @param desc The description of the command, 1 to 100 characters long.
	 *
	 * @throws IllegalArgumentException if:
	 * <ul>
	 *     <li><b>name</b> does not consist of 1 to 32 lowercase alphanumeric
	 *     characters. Words can separated only with dashes ('-'), not spaces.</li>
	 *     <li><b>desc</b>'s length is not between 1 to 100 characters.</li>
	 * </ul>
	 *
	 * @throws NullPointerException If any parameter is {@code null}.
	 */
	public JDSCommand(@NotNull B bot, @NotNull String name, @NotNull String desc) {
		this.bot = Objects.requireNonNull(bot);
		this.data = Commands.slash(name, desc);
	}

	@Override
	public final void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		onSlashCommand(new JDSCommandInteraction<>(this.bot, event));
	}

	/**
	 * Called whenever a {@link User} executes this {@link JDSCommand}.
	 *
	 * @param interaction the {@link JDSCommandInteraction} that executed this command.
	 *
	 * @return Doesn't matter, returning either {@code true} or {@code false} does nothing.
	 * You can use the return value to {@link JDSCommandInteraction#reply(String) reply}
	 * to the {@code interaction} and {@code return} in one line.
	 *
	 * @since JDSkyBot 1.0.0
	 *
	 * @see JDSCommandInteraction#reply(String)
	 * @see JDSCommandInteraction#replyCustom(String)
	 */
	public abstract boolean onSlashCommand(JDSCommandInteraction<B> interaction);

	@NotNull
	@Override
	public SlashCommandData getSlashCommandData() {
		return this.data;
	}

	@NotNull
	public JDSCommand<B> addOption(OptionType type, String name, String desc) {
		this.data.addOption(type, name, desc);
		return this;
	}

	@NotNull
	public JDSCommand<B> addOption(OptionType type, String name, String desc, boolean required) {
		this.data.addOption(type, name, desc, required);
		return this;
	}

	@NotNull
	public JDSCommand<B> addOption(OptionType type, String name, String desc, boolean required, boolean hasAutoComplete) {
		this.data.addOption(type, name, desc, required, hasAutoComplete);
		return this;
	}
}
