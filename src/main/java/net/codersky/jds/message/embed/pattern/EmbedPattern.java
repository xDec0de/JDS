package net.codersky.jds.message.embed.pattern;

import net.codersky.jds.message.embed.JDSEmbedBuilder;
import net.codersky.jsky.annotations.KeyPattern;
import net.codersky.jsky.collections.JCollections;
import net.codersky.jsky.strings.tag.JTag;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public interface EmbedPattern {

	String[] NO_ARGS = new String[0];

	void apply(@NotNull EmbedBuilder embed, @NotNull String context, @NotNull JTag[] extra);

	@NotNull
	@KeyPattern
	String getKey();

	@NotNull
	default String @NotNull [] getAliases() {
		return NO_ARGS;
	}

	/*
	 - Match by name or aliases
	 */

	default boolean matches(@NotNull String key) {
		if (getKey().equals(key))
			return true;
		for (final String alias : getAliases())
			if (alias.equals(key))
				return true;
		return false;
	}

	default boolean matches(@NotNull EmbedPattern other) {
		if (matches(other.getKey()))
			return true;
		for (final String alias : other.getAliases())
			if (matches(alias))
				return true;
		return false;
	}

	/*
	 - Helper methods
	 */

	@Nullable
	default String getUrl(@NotNull JTag @NotNull [] extra) {
		final JTag url = JCollections.get(extra, tag -> tag.getName().equals("url"));
		if (url == null || !JDSEmbedBuilder.isUrlOrAttachment(url.getContent()))
			return null;
		return url.getContent();
	}

	/*
	 - Static builders
	 */

	static EmbedPattern of(BiConsumer<EmbedBuilder, String> apply, @NotNull @KeyPattern String key, @NotNull String... keyAliases) {
		return new EmbedPattern() {
			@Override
			public void apply(@NotNull EmbedBuilder embed, @NotNull String context, @NotNull JTag[] extra) {
				apply.accept(embed, context);
			}

			@Override
			@NotNull
			@KeyPattern
			public String getKey() {
				return key;
			}

			@Override
			@NotNull
			public String @NotNull [] getAliases() {
				return keyAliases;
			}
		};
	}

	static EmbedPattern of(BiConsumer<EmbedBuilder, String> apply, @NotNull @KeyPattern String key) {
		return of(apply, key, new String[0]);
	}
}
