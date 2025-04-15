package net.codersky.mcsb.message.embed;

import net.codersky.jsky.strings.JStrings;
import net.codersky.mcsb.message.embed.pattern.ColorEmbedPattern;
import net.codersky.mcsb.message.embed.pattern.EmbedPattern;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class JDSEmbedBuilder {

	private final static ArrayList<EmbedPattern> patterns = new ArrayList<>();

	static {
		addPattern(EmbedPattern.of(EmbedBuilder::setTitle,"title"));
		addPattern(new ColorEmbedPattern());
		addPattern(EmbedPattern.of(EmbedBuilder::setDescription, "desc", "description"));
		addPattern(EmbedPattern.of(EmbedBuilder::setFooter, "footer"));
		addPattern(EmbedPattern.of(EmbedBuilder::setImage, "image", "img"));
		addPattern(EmbedPattern.of(EmbedBuilder::setThumbnail, "thumbnail", "thmb"));
		addPattern(EmbedPattern.of(EmbedBuilder::setAuthor, "author"));
	}

	@Nullable
	public static EmbedPattern getPattern(@NotNull String key) {
		for (final EmbedPattern pattern : patterns)
			if (pattern.matches(key))
				return pattern;
		return null;
	}

	public static boolean addPattern(@NotNull EmbedPattern pattern) {
		if (!isValidPattern(pattern))
			return false;
		for (EmbedPattern added : patterns)
			if (added.matches(pattern))
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

	public static MessageEmbed build(@NotNull String raw) {
		final EmbedBuilder builder = new EmbedBuilder();
		final List<String> tokens = tokenize(raw);
		for (String token : tokens)
			processToken(token.substring(1, token.length() - 1), builder);
		return builder.build();
	}

	private static List<String> tokenize(String input) {
		final List<String> tokens = new ArrayList<>();
		StringBuilder token = new StringBuilder();
		boolean inToken = false;
		boolean escapeNext = false;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (escapeNext) {
				token.append(c);
				escapeNext = false;
				continue;
			}
			if (c == '\\') {
				escapeNext = true;
				continue;
			}
			if (c == '<' && !inToken) {
				inToken = true;
				token = new StringBuilder();
				token.append(c);
			} else if (c == '>' && inToken) {
				token.append(c);
				tokens.add(token.toString());
				inToken = false;
			} else if (inToken)
				token.append(c);
		}
		return tokens;
	}

	private static void processToken(String token, @NotNull EmbedBuilder builder) {
		int colonIndex = token.indexOf(':');
		if (colonIndex == -1)
			return;
		final String key = token.substring(0, colonIndex).trim().toLowerCase();
		final String content = unescape(token.substring(colonIndex + 1).trim());
		if (key.isEmpty() || content.isEmpty())
			return;
		final EmbedPattern pattern = getPattern(key);
		if (pattern != null)
			pattern.apply(builder, content);
	}

	private static String unescape(String input) {
		final StringBuilder result = new StringBuilder();
		boolean escapeNext = false;
		for (int i = 0; i < input.length(); i++) {
			final char c = input.charAt(i);
			if (escapeNext) {
				result.append(c);
				escapeNext = false;
			} else if (c == '\\')
				escapeNext = true;
			else
				result.append(c);
		}
		return result.toString();
	}
}
