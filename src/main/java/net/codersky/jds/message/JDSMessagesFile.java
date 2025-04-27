package net.codersky.jds.message;

import net.codersky.jsky.storage.DataProvider;
import net.codersky.jsky.strings.Replacer;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

/**
 * Class capable of storing any {@link DataProvider} to fetch raw
 * message data that can be then converted to {@link JDSMessage}s.
 *
 * @since JDS 1.0.0
 *
 * @author xDec0de_
 */
public class JDSMessagesFile<P extends DataProvider> {

	private final P provider;

	public JDSMessagesFile(@NotNull P provider) {
		this.provider = Objects.requireNonNull(provider);
	}

	@NotNull
	public P getProvider() {
		return provider;
	}

	@NotNull
	public JDSMessage getDefaultMessage(@NotNull String path) {
		return new JDSMessage("Message not found: " + path);
	}

	@NotNull
	private JDSMessage getJDSMessage(@NotNull String path, @NotNull Function<String, JDSMessage> getter) {
		final String raw = provider.getString(path);
		return raw == null ? getDefaultMessage(path) : getter.apply(raw);
	}

	@NotNull
	public JDSMessage getJDSMessage(@NotNull String path) {
		return getJDSMessage(path, JDSMessage::new);
	}

	@NotNull
	public JDSMessage getJDSMessage(@NotNull String path, @NotNull Replacer replacer) {
		return getJDSMessage(path, raw -> new JDSMessage(replacer.replaceAt(raw)));
	}

	@NotNull
	public JDSMessage getJDSMessage(@NotNull String path, @NotNull Object... replacements) {
		return getJDSMessage(path, new Replacer(replacements));
	}

	/*
	 - Reply
	 */

	public boolean reply(@NotNull IReplyCallback toReply, @NotNull String path) {
		return getJDSMessage(path).reply(toReply);
	}

	public boolean reply(@NotNull IReplyCallback toReply, @NotNull String path, @NotNull Replacer replacer) {
		return getJDSMessage(path, replacer).reply(toReply);
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
