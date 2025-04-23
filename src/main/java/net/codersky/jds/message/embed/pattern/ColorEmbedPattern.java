package net.codersky.jds.message.embed.pattern;

import net.codersky.jsky.JColor;
import net.codersky.jsky.annotations.KeyPattern;
import net.codersky.jsky.strings.tag.JTag;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

public class ColorEmbedPattern implements EmbedPattern {

	private final String[] aliases = {"col"};

	@Override
	public void apply(@NotNull EmbedBuilder embed, @NotNull String context, @NotNull JTag[] extra) {
		embed.setColor(JColor.of(context));
	}

	@Override
	public @NotNull @KeyPattern String getKey() {
		return "color";
	}

	@Override
	public @NotNull String @NotNull [] getAliases() {
		return aliases;
	}
}
