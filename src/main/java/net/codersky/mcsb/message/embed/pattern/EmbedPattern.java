package net.codersky.mcsb.message.embed.pattern;

import net.codersky.jsky.annotations.KeyPattern;
import net.codersky.jsky.strings.JStrings;
import net.dv8tion.jda.api.EmbedBuilder;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public interface EmbedPattern {

	void apply(@NotNull EmbedBuilder embed, @NotNull String context);

	@NotNull
	@KeyPattern
	String getKey();

	@NotNull
	default String @NotNull [] getAliases() {
		return new String[0];
	}

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

	static EmbedPattern of(BiConsumer<EmbedBuilder, String> apply, @NotNull @KeyPattern String key, @NotNull String... keyAliases) {
		return new EmbedPattern() {
			@Override
			public void apply(@NotNull EmbedBuilder embed, @NotNull String context) {
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
