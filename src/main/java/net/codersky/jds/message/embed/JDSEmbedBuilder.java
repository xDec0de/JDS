package net.codersky.jds.message.embed;

import net.codersky.jds.message.embed.pattern.AuthorEmbedPattern;
import net.codersky.jds.message.embed.pattern.ColorEmbedPattern;
import net.codersky.jds.message.embed.pattern.DescriptionEmbedPattern;
import net.codersky.jds.message.embed.pattern.EmbedPattern;
import net.codersky.jds.message.embed.pattern.FooterEmbedPattern;
import net.codersky.jds.message.embed.pattern.ImageEmbedPattern;
import net.codersky.jds.message.embed.pattern.ThumbnailEmbedPattern;
import net.codersky.jds.message.embed.pattern.TitleEmbedPattern;
import net.codersky.jds.message.embed.pattern.UrlEmbedPattern;
import net.codersky.jsky.collections.JCollections;
import net.codersky.jsky.strings.JStrings;
import net.codersky.jsky.strings.tag.JTag;
import net.codersky.jsky.strings.tag.JTagParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class JDSEmbedBuilder {

	private final static ArrayList<EmbedPattern> patterns = new ArrayList<>();

	static { // NOTE: Keep alphabetic order for organization, please.
		addPattern(new AuthorEmbedPattern());
		addPattern(new ColorEmbedPattern());
		addPattern(new DescriptionEmbedPattern());
		addPattern(new FooterEmbedPattern());
		addPattern(new ImageEmbedPattern());
		addPattern(new ThumbnailEmbedPattern());
		addPattern(new TitleEmbedPattern());
		addPattern(new UrlEmbedPattern());
	}

	/*
	 - Pattern management
	 */

	@Nullable
	public static EmbedPattern getPattern(@NotNull String key) {
		if (!JStrings.hasKeyPattern(key))
			return null;
		for (final EmbedPattern pattern : patterns) {
			if (pattern.getKey().equals(key))
				return pattern;
			if (JCollections.contains(pattern.getAliases(), alias -> alias.equals(key)))
				return pattern;
		}
		return null;
	}

	public static boolean addPattern(@NotNull EmbedPattern pattern) {
		if (!isValidPattern(pattern))
			return false;
		if (getPattern(pattern.getKey()) != null)
			return false;
		for (final String alias : pattern.getAliases())
			if (getPattern(alias) != null)
				return false;
		return patterns.add(pattern);
	}

	public static boolean isValidPattern(@NotNull EmbedPattern pattern) {
		if (!JStrings.hasKeyPattern(pattern.getKey()))
			return false;
		for (final String alias : pattern.getAliases())
			if (!JStrings.hasKeyPattern(alias))
				return false;
		return true;
	}

	/*
	 - Embed building
	 */

	@Nullable
	public static MessageEmbed build(@NotNull String raw) {
		final JTag embed = JTagParser.parseOne(raw);
		return embed == null ? null : build(embed);
	}

	@Nullable
	public static MessageEmbed build(@NotNull JTag tag) {
		if (!tag.getName().equals("e") && tag.getName().equals("embed"))
			return null;
		final EmbedBuilder builder = new EmbedBuilder();
		for (final JTag modifier : tag.getChildren()) {
			final EmbedPattern pattern = getPattern(modifier.getName());
			if (pattern != null)
				pattern.apply(builder, modifier.getContent(), modifier.getChildren());
		}
		return builder.build();
	}

	/*
	 - Utilities
	 */

	public static boolean isUrlOrAttachment(@NotNull String url) {
		final int urlLen = url.length();
		if (urlLen > MessageEmbed.URL_MAX_LENGTH)
			return false;
		final String http = "http://";
		if (urlLen <= http.length())
			return false;
		final String https = "https://";
		if (urlLen > https.length() && JStrings.startsWith(true, url, https))
			return true;
		if (JStrings.startsWith(true, url, http))
			return true;
		final String attachment = "attachment://";
		return urlLen > attachment.length() && JStrings.startsWith(true, url, attachment);
	}
}
