package net.codersky.mcsb.cmd;

import net.codersky.jsky.strings.Replacer;
import net.codersky.mcsb.JDSkyBot;
import net.codersky.mcsb.message.JDSMessage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class JDSCommandInteraction<B extends JDSkyBot> {

	private final B bot;
	private final SlashCommandInteractionEvent original;

	public JDSCommandInteraction(@NotNull B bot, @NotNull SlashCommandInteractionEvent event) {
		this.bot = Objects.requireNonNull(bot);
		this.original = Objects.requireNonNull(event);
	}

	/*
	 - Utility getters
	 */

	@NotNull
	public B getBot() {
		return bot;
	}

	@NotNull
	public JDA getJDA() {
		return original.getJDA();
	}

	@NotNull
	public User getUser() {
		return original.getUser();
	}

	/*
	 - Reply status
	 */

	public boolean deferReply() {
		original.deferReply();
		return true;
	}

	public boolean deferReply(boolean ephemeral) {
		original.deferReply(ephemeral);
		return true;
	}

	public boolean isReplied() {
		return original.isAcknowledged();
	}

	/*
	 - Reply (Custom)
	 */

	public boolean replyCustom(String message) {
		return new JDSMessage(message).reply(original);
	}

	public boolean replyCustom(JDSMessage message) {
		return message.reply(original);
	}

	/*
	 - Reply (From messages file)
	 */

	public boolean reply(@NotNull String path) {
		return bot.getMessages().reply(getUser(), original, path);
	}

	public boolean reply(@NotNull String path, @NotNull Replacer replacer) {
		return bot.getMessages().reply(getUser(), original, path, replacer);
	}

	public boolean reply(@NotNull String path, @NotNull Object... replacements) {
		return bot.getMessages().reply(getUser(), original, path, replacements);
	}

	/*
	 - Primitives
	 */

	public boolean asBoolean(@NotNull String id, boolean def) {
		final OptionMapping mapping = original.getOption(id);
		return mapping == null ? def : mapping.getAsBoolean();
	}

	public boolean asBoolean(@NotNull String id) {
		return asBoolean(id, false);
	}

	public int asInt(@NotNull String id, int def) {
		final OptionMapping mapping = original.getOption(id);
		return mapping == null ? def : mapping.getAsInt();
	}

	public int asInt(@NotNull String id) {
		return asInt(id, -1);
	}

	public long asLong(@NotNull String id, long def) {
		final OptionMapping mapping = original.getOption(id);
		return mapping == null ? def : mapping.getAsLong();
	}

	public long asLong(@NotNull String id) {
		return asLong(id, -1L);
	}

	public double asDouble(@NotNull String id, double def) {
		final OptionMapping mapping = original.getOption(id);
		return mapping == null ? def : mapping.getAsDouble();
	}

	public double asDouble(@NotNull String id) {
		return asDouble(id, -1.0d);
	}

	@Nullable
	public String asString(@NotNull String id, String def) {
		final OptionMapping mapping = original.getOption(id);
		return mapping == null ? def : mapping.getAsString();
	}

	@Nullable
	public String asString(@NotNull String id) {
		return asString(id, null);
	}

	/*
	 - JDA
	 */

	@Nullable
	public Message.Attachment asAttachment(@NotNull String id, Message.Attachment def) {
		final OptionMapping mapping = original.getOption(id);
		return mapping == null ? def : mapping.getAsAttachment();
	}

	@Nullable
	public Message.Attachment asAttachment(@NotNull String id) {
		return asAttachment(id, null);
	}

	@Nullable
	public GuildChannelUnion asChannel(@NotNull String id, GuildChannelUnion def) {
		final OptionMapping mapping = original.getOption(id);
		return mapping == null ? def : mapping.getAsChannel();
	}

	@Nullable
	public GuildChannelUnion asChannel(@NotNull String id) {
		return asChannel(id, null);
	}

	@Nullable
	public Member asMember(@NotNull String id, Member def) {
		final OptionMapping mapping = original.getOption(id);
		return mapping == null ? def : mapping.getAsMember();
	}

	@Nullable
	public Member asMember(@NotNull String id) {
		return asMember(id, null);
	}

	@Nullable
	public IMentionable asMention(@NotNull String id, IMentionable def) {
		final OptionMapping mapping = original.getOption(id);
		return mapping == null ? def : mapping.getAsMentionable();
	}

	@Nullable
	public IMentionable asMention(@NotNull String id) {
		return asMention(id, null);
	}

	@Nullable
	public Role asRole(@NotNull String id, Role def) {
		final OptionMapping mapping = original.getOption(id);
		return mapping == null ? def : mapping.getAsRole();
	}

	@Nullable
	public Role asRole(@NotNull String id) {
		return asRole(id, null);
	}

	@Nullable
	public User asUser(@NotNull String id, User def) {
		final OptionMapping mapping = original.getOption(id);
		return mapping == null ? def : mapping.getAsUser();
	}

	@Nullable
	public User asUser(@NotNull String id) {
		return asUser(id, null);
	}
}
