package net.codersky.jds.message.embed.pattern;

import net.codersky.jsky.annotations.KeyPattern;
import net.codersky.jsky.strings.tag.JTag;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

public class AuthorEmbedPattern implements EmbedPattern {

	@Override
	public void apply(@NotNull EmbedBuilder embed, @NotNull String context, @NotNull JTag[] extra) {
		if (context.length() <= MessageEmbed.AUTHOR_MAX_LENGTH)
			embed.setAuthor(context, getUrl("url", extra), getUrl("icon", extra));
	}

	@Override
	public @NotNull @KeyPattern String getKey() {
		return "author";
	}
}
