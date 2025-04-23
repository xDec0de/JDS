package net.codersky.jds.message.embed.pattern;

import net.codersky.jsky.annotations.KeyPattern;
import net.codersky.jsky.strings.tag.JTag;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

public class FooterEmbedPattern implements EmbedPattern {

	@Override
	public void apply(@NotNull EmbedBuilder embed, @NotNull String context, @NotNull JTag[] extra) {
		if (context.length() <= MessageEmbed.TEXT_MAX_LENGTH)
			embed.setFooter(context, getUrl(extra));
	}

	@Override
	public @NotNull @KeyPattern String getKey() {
		return "footer";
	}
}
