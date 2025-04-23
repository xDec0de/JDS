package net.codersky.jds.message.embed.pattern;

import net.codersky.jds.message.embed.JDSEmbedBuilder;
import net.codersky.jsky.annotations.KeyPattern;
import net.codersky.jsky.strings.tag.JTag;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

public class ThumbnailEmbedPattern  implements EmbedPattern {

	final String[] aliases = {"thumbnail", "thumb"};

	@Override
	public void apply(@NotNull EmbedBuilder embed, @NotNull String context, @NotNull JTag[] extra) {
		if (JDSEmbedBuilder.isUrlOrAttachment(context))
			embed.setThumbnail(context);
	}

	@Override
	public @NotNull @KeyPattern String getKey() {
		return "tn";
	}

	@Override
	public @NotNull String @NotNull [] getAliases() {
		return aliases;
	}
}