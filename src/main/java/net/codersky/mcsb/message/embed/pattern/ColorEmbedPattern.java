package net.codersky.mcsb.message.embed.pattern;

import net.codersky.jsky.annotations.KeyPattern;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;

public class ColorEmbedPattern implements EmbedPattern {

	private final String[] aliases = {"col"};

	@Override
	public void apply(@NotNull EmbedBuilder embed, @NotNull String context) {
		embed.setColor(Color.decode(context));
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
