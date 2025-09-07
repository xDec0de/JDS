package net.codersky.jds.message.embed;

import net.codersky.jds.message.embed.pattern.AuthorEmbedPattern;
import net.codersky.jds.message.embed.pattern.ColorEmbedPattern;
import net.codersky.jds.message.embed.pattern.DescriptionEmbedPattern;
import net.codersky.jds.message.embed.pattern.EmbedPattern;
import net.codersky.jds.message.embed.pattern.FieldEmbedPattern;
import net.codersky.jds.message.embed.pattern.FooterEmbedPattern;
import net.codersky.jds.message.embed.pattern.ImageEmbedPattern;
import net.codersky.jds.message.embed.pattern.InlineFieldEmbedPattern;
import net.codersky.jds.message.embed.pattern.ThumbnailEmbedPattern;
import net.codersky.jds.message.embed.pattern.TitleEmbedPattern;
import net.codersky.jds.message.embed.pattern.UrlEmbedPattern;
import net.codersky.jsky.collections.JCollections;
import net.codersky.jsky.strings.JStrings;
import net.codersky.jsky.strings.tag.JTag;
import net.codersky.jsky.strings.tag.JTagParseAllResult;
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
		addPattern(new FieldEmbedPattern());
		addPattern(new FooterEmbedPattern());
		addPattern(new ImageEmbedPattern());
		addPattern(new InlineFieldEmbedPattern());
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

	/**
	 * Checks if the provided {@code tag} is a valid embed tag. Embed
	 * tags are {@link JTag JTags} which {@link JTag#getName() name}
	 * is either "e" or "embed". Embed tags are considered the parent
	 * tags that will then contain {@link EmbedPattern embed patterns}
	 * inside of them (As {@link JTag} {@link JTag#getChildren() children}).
	 * Here are examples of valid embed tags (In raw string format):
	 * <ul>
	 *     <li>{@code <embed: <title:Embed title> >}</li>
	 *     <li>{@code <embed: Actual content is not verified >}</li>
	 *     <li>{@code <e: <desc:Short format> >}</li>
	 * </ul>
	 *
	 * @param tag The {@link JTag} to check.
	 *
	 * @return {@code true} if {@code tag} is an embed tag, {@code false} otherwise.
	 *
	 * @since JDS 1.0.0
	 *
	 * @see #build(String)
	 * @see #applyPatterns(String)
	 * @see #applyPatterns(EmbedBuilder, JTag)
	 */
	public static boolean isEmbedTag(@NotNull JTag tag) {
		return tag.getName().equals("e") || tag.getName().equals("embed");
	}

	/*
	 - Apply patterns
	 */

	/**
	 * Searches for {@link EmbedPattern embed patterns} directly on the provided
	 * {@code patterns} array and {@link EmbedPattern#apply(EmbedBuilder, String, JTag[])
	 * applies} them to the {@code base} {@link EmbedBuilder}.
	 *
	 * @param base The base {@link EmbedBuilder} to modify.
	 * @param patterns The {@link JTag} array used to search for
	 * {@link EmbedPattern embed patterns} to apply.
	 *
	 * @return {@code base} with any required modifications. {@code base}
	 * will not be modified if no {@link EmbedPattern} is found inside the
	 * {@code patterns} array.
	 *
	 * @since JDS 1.0.0
	 */
	@NotNull
	public static EmbedBuilder applyPatterns(@NotNull EmbedBuilder base, @NotNull JTag[] patterns) {
		for (final JTag modifier : patterns) {
			final EmbedPattern pattern = getPattern(modifier.getName());
			if (pattern != null)
				pattern.apply(base, modifier.getContent(), modifier.getChildren());
		}
		return base;
	}

	/**
	 * Applies {@link EmbedPattern embed patterns} found inside the
	 * provided {@code embedTag}. First, {@code embedTag} is verified,
	 * {@link #isEmbedTag(JTag) checking if it's actually an embed tag}.
	 * If so, then {@link #applyPatterns(EmbedBuilder, JTag[])} is used
	 * with {@code embedTag}'s {@link JTag#getChildren() children tags}
	 * to apply the {@link EmbedPattern embed patterns} to {@code base}.
	 *
	 * @param base The base {@link EmbedBuilder} to modify.
	 * @param embedTag The embed {@link JTag} to use. {@link EmbedPattern
	 * embed patterns} will be searched on its {@link JTag#getChildren()
	 * children tags}, and then applied to {@code base}.
	 *
	 * @return {@code base} with any required modifications. {@code base}
	 * will not be modified if {@code embedTag} isn't an embed tag (See
	 * {@link #isEmbedTag(JTag)}) or if no {@link EmbedPattern embed patterns}
	 * are found inside {@code embedTag}.
	 *
	 * @since JDS 1.0.0
	 *
	 * @see #isEmbedTag(JTag)
	 * @see #applyPatterns(EmbedBuilder, JTag[])
	 */
	@NotNull
	public static EmbedBuilder applyPatterns(@NotNull EmbedBuilder base, @NotNull JTag embedTag) {
		return isEmbedTag(embedTag) ? applyPatterns(base, embedTag.getChildren()) : base;
	}

	/**
	 * Attempts to obtain {@link EmbedPattern embed patterns} from the provided
	 * {@code raw} String that will then be used to modify the {@code base}
	 * {@link EmbedBuilder}.
	 * <p>
	 * The {@code raw} String is assumed to contain {@link EmbedPattern patterns}
	 * directly, not an {@link #isEmbedTag(JTag) embed tag}. So this is considered
	 * valid input:
	 * <ul>
	 *     <li>{@code <title:Modified embed title>}</li>
	 *     <li>{@code <desc:Modified embed description>}</li>
	 * </ul>
	 * While this isn't:
	 * <ul>
	 *     <li>{@code <e: <title:Modified embed title> >}</li>
	 *     <li>{@code <embed: <desc:Modified embed description> >}</li>
	 * </ul>
	 *
	 * This is because this method is meant to modify an existing {@code base}
	 * {@link EmbedBuilder}, not to create a new one entirely. For that,
	 * you can use either {@link #applyPatterns(String)} or {@link #build(String)}.
	 *
	 * @param base The base {@link EmbedBuilder} to modify.
	 * @param raw The raw String containing {@link EmbedPattern embed patterns}.
	 *
	 * @return {@code base} with any required modifications. {@code base}
	 * will not be modified if no {@link EmbedPattern embed patterns}
	 * are found inside the {@code raw} String.
	 */
	@NotNull
	public static EmbedBuilder applyPatterns(@NotNull EmbedBuilder base, @NotNull String raw) {
		EmbedBuilder builder = base;
		final JTagParseAllResult res = JTagParser.parseAll(raw);
		for (final JTag tag : res.getTags())
			builder = applyPatterns(builder, tag);
		return builder;
	}

	/**
	 * Creates an {@link EmbedBuilder} based of the provided {@code raw}
	 * String, given that it contains a valid {@link #isEmbedTag(JTag) embed tag}
	 * with valid {@link EmbedPattern embed patterns} inside of it.
	 *
	 * @param raw The raw embed String to use.
	 *
	 * @return A new {@link EmbedBuilder} based of the provided
	 * {@code raw} String if it has valid syntax, {@code null} otherwise.
	 */
	@Nullable
	public static EmbedBuilder applyPatterns(@NotNull String raw) {
		final JTag tag = JTagParser.parse(raw).getTag();
		if (tag == null || !isEmbedTag(tag))
			return null;
		return applyPatterns(new EmbedBuilder(), tag.getChildren());
	}

	/**
	 * Builds a new {@link MessageEmbed} based of the provided {@code raw}
	 * String, given that it contains a valid {@link #isEmbedTag(JTag) embed tag}
	 * with valid {@link EmbedPattern embed patterns} inside of it.
	 * <p>
	 * {@link #applyPatterns(String)} is used by this method. This method
	 * only uses it, checks if the return value is {@code null}, and if
	 * not, {@link EmbedBuilder#build() builds it}. If {@code null},
	 * it just returns {@code null}.
	 *
	 * @param raw The raw embed String to use.
	 *
	 * @return A new {@link MessageEmbed} based of the provided
	 * {@code raw} String if it has valid syntax, {@code null} otherwise.
	 *
	 * @since JDS 1.0.0
	 */
	@Nullable
	public static MessageEmbed build(@NotNull String raw) {
		final EmbedBuilder builder = applyPatterns(raw);
		return builder == null ? null : builder.build();
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
