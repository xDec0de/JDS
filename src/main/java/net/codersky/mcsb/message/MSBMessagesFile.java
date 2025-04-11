package net.codersky.mcsb.message;

import net.codersky.jsky.strings.Replacer;
import net.codersky.jsky.yaml.YamlFile;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Objects;
import java.util.function.Function;

/**
 * {@link YamlFile} extension to provide customizable {@link MSBMessage messages}.
 *
 * @since MCSkyBot 1.0.0
 *
 * @author xDec0de_
 */
public class MSBMessagesFile extends YamlFile {

	private final JDA jda;

	public MSBMessagesFile(@NotNull JDA jda, @Nullable File diskPath, @NotNull String resourcePath) {
		super(diskPath, resourcePath);
		this.jda = Objects.requireNonNull(jda);
	}

	@NotNull
	public MSBMessage getDefaultMessage(@NotNull String path) {
		return new MSBMessage(jda, "Message not found: " + path);
	}

	@NotNull
	private MSBMessage getMSBMessage(@NotNull String path, @NotNull Function<String, MSBMessage> getter) {
		final MSBMessage msg = getter.apply(path);
		return msg == null ? getDefaultMessage(path) : msg;
	}

	@NotNull
	public MSBMessage getMSBMessage(@NotNull String path) {
		return getMSBMessage(path, raw -> new MSBMessage(jda, raw));
	}

	@NotNull
	public MSBMessage getMSBMessage(@NotNull String path, @NotNull Replacer replacer) {
		return getMSBMessage(path, raw -> new MSBMessage(jda, replacer.replaceAt(raw)));
	}

	@NotNull
	public MSBMessage getMSBMessage(@NotNull String path, @NotNull Object... replacements) {
		return getMSBMessage(path, new Replacer(replacements));
	}

	/*
	 - Reply
	 */

	public boolean reply(@NotNull IReplyCallback toReply, @NotNull String path) {
		return getMSBMessage(path).reply(toReply);
	}

	public boolean reply(@NotNull IReplyCallback toReply, @NotNull String path, @NotNull Replacer replacer) {
		return getMSBMessage(path, replacer).reply(toReply);
	}

	public boolean reply(@NotNull IReplyCallback toReply, @NotNull String path, @NotNull Object... replacements) {
		return reply(toReply, path, new Replacer(replacements));
	}

	/*
	 - Reply - With user context
	 */

	public boolean reply(@NotNull User user, @NotNull IReplyCallback toReply, @NotNull String path) {
		return reply(toReply, path);
	}

	public boolean reply(@NotNull User user, @NotNull IReplyCallback toReply, @NotNull String path, @NotNull Replacer replacer) {
		return reply(toReply, path, replacer);
	}

	public boolean reply(@NotNull User user, @NotNull IReplyCallback toReply, @NotNull String path, @NotNull Object... replacements) {
		return reply(toReply, path, replacements);
	}
}
