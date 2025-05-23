package net.codersky.jds.message.embed.pattern;

import net.codersky.jds.message.embed.JDSEmbedBuilder;
import net.codersky.jsky.annotations.KeyPattern;
import net.codersky.jsky.strings.tag.JTag;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

public class ImageEmbedPattern implements EmbedPattern {

	private final String[] aliases = {"img"};

	@Override
	public void apply(@NotNull EmbedBuilder embed, @NotNull String context, @NotNull JTag[] extra) {
		if (JDSEmbedBuilder.isUrlOrAttachment(context))
			embed.setImage(context);
	}

	@Override
	public @NotNull @KeyPattern String getKey() {
		return "image";
	}

	@Override
	public @NotNull String @NotNull [] getAliases() {
		return aliases;
	}
}
