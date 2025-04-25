package net.codersky.jds.message;

import net.codersky.jsky.strings.Replacer;
import net.codersky.jsky.yaml.YamlFile;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.function.Function;

/**
 * {@link YamlFile} extension to provide customizable {@link JDSMessage messages}.
 *
 * @since JDSBot 1.0.0
 *
 * @author xDec0de_
 */
public class JDSMessagesFile extends YamlFile {

	public JDSMessagesFile(@Nullable File diskPath, @NotNull String resourcePath) {
		super(diskPath, resourcePath);
	}

	@NotNull
	public JDSMessage getDefaultMessage(@NotNull String path) {
		return new JDSMessage("Message not found: " + path);
	}

	@NotNull
	private JDSMessage getMSBMessage(@NotNull String path, @NotNull Function<String, JDSMessage> getter) {
		final JDSMessage msg = getter.apply(path);
		return msg == null ? getDefaultMessage(path) : msg;
	}

	@NotNull
	public JDSMessage getMSBMessage(@NotNull String path) {
		return getMSBMessage(path, JDSMessage::new);
	}

	@NotNull
	public JDSMessage getMSBMessage(@NotNull String path, @NotNull Replacer replacer) {
		return getMSBMessage(path, raw -> new JDSMessage(replacer.replaceAt(raw)));
	}

	@NotNull
	public JDSMessage getMSBMessage(@NotNull String path, @NotNull Object... replacements) {
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
