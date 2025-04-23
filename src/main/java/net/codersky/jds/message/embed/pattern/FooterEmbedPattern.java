package net.codersky.jds.message.embed.pattern;

import net.codersky.jds.message.embed.JDSEmbedBuilder;
import net.codersky.jsky.annotations.KeyPattern;
import net.codersky.jsky.collections.JCollections;
import net.codersky.jsky.strings.tag.JTag;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

public class FooterEmbedPattern implements EmbedPattern {

	@Override
	public void apply(@NotNull EmbedBuilder embed, @NotNull String context, @NotNull JTag[] extra) {
		if (context.length() > MessageEmbed.TEXT_MAX_LENGTH)
			return;
		final JTag url = JCollections.get(extra, tag -> tag.getName().equals("url"));
		if (url != null && JDSEmbedBuilder.isUrlOrAttachment(url.getContent()))
			embed.setFooter(context, url.getContent());
		else
			embed.setFooter(context);
	}

	@Override
	public @NotNull @KeyPattern String getKey() {
		return "footer";
	}
}
