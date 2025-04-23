package net.codersky.jds.message.embed.pattern;

import net.codersky.jsky.annotations.KeyPattern;
import net.codersky.jsky.collections.JCollections;
import net.codersky.jsky.strings.tag.JTag;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

public class FieldEmbedPattern implements EmbedPattern {

	@Override
	public void apply(@NotNull EmbedBuilder embed, @NotNull String context, @NotNull JTag[] extra) {
		final JTag value = JCollections.get(extra, tag -> tag.getName().equals("value") || tag.getName().equals("v"));
		final String valueStr = value == null ? "" : value.getContent();
		embed.addField(context, valueStr, false);
	}

	@Override
	public @NotNull @KeyPattern String getKey() {
		return "field";
	}
}
