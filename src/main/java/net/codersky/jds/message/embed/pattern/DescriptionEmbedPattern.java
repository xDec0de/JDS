package net.codersky.jds.message.embed.pattern;

import net.codersky.jsky.annotations.KeyPattern;
import net.codersky.jsky.strings.tag.JTag;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

public class DescriptionEmbedPattern implements EmbedPattern {

	final String[] aliases = {"description"};

	@Override
	public void apply(@NotNull EmbedBuilder embed, @NotNull String context, @NotNull JTag[] extra) {
		if (context.length() <= MessageEmbed.DESCRIPTION_MAX_LENGTH)
			embed.setDescription(context);
	}

	@Override
	public @NotNull @KeyPattern String getKey() {
		return "desc";
	}

	@Override
	public @NotNull String @NotNull [] getAliases() {
		return aliases;
	}
}
