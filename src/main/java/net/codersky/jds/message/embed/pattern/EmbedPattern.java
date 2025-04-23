package net.codersky.jds.message.embed.pattern;

import net.codersky.jds.message.embed.JDSEmbedBuilder;
import net.codersky.jsky.annotations.KeyPattern;
import net.codersky.jsky.collections.JCollections;
import net.codersky.jsky.strings.tag.JTag;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EmbedPattern {

	String[] NO_ARGS = new String[0];

	void apply(@NotNull EmbedBuilder embed, @NotNull String context, @NotNull JTag[] extra);

	/*
	 - Pattern info
	 */

	@NotNull
	@KeyPattern
	String getKey();

	@NotNull
	default String @NotNull [] getAliases() {
		return NO_ARGS;
	}

	/*
	 - Helper methods
	 */

	@Nullable
	default String getUrl(@NotNull String name, @NotNull JTag @NotNull [] extra) {
		final JTag url = JCollections.get(extra, tag -> tag.getName().equals(name));
		if (url == null || !JDSEmbedBuilder.isUrlOrAttachment(url.getContent()))
			return null;
		return url.getContent();
	}
}
