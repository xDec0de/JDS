package net.codersky.mcsb.message;

import net.codersky.jsky.strings.JStrings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class JDSMessage {

	private final String raw;
	private MessageEmbed embed = null;
	private final boolean ephemeral;

	public JDSMessage(JDA jda, @NotNull String raw) {
		this.ephemeral = raw.startsWith("eph:");
		this.raw = this.ephemeral ? raw.substring(4) : raw;
		JStrings.match(this.raw, "<e", "/>", tokens -> {
			this.embed = processEmbed(tokens);
		});
	}

	/*
	 - Embed processing
	 */

	private MessageEmbed processEmbed(String raw) {
		final EmbedBuilder builder = new EmbedBuilder();
		final List<String> tokens = tokenize(raw);
		for (String token : tokens)
			processToken(token.substring(1, token.length() - 1), builder);
		return builder.build();
	}

	private List<String> tokenize(String input) {
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

	private void processToken(String token, @NotNull EmbedBuilder builder) {
		int colonIndex = token.indexOf(':');
		if (colonIndex == -1)
			return;
		final String type = token.substring(0, colonIndex).trim().toLowerCase();
		final String content = unescape(token.substring(colonIndex + 1).trim());
		if (type.isEmpty() || content.isEmpty())
			return;
		switch (type) {
			case "title" -> builder.setTitle(content);
			case "color" -> builder.setColor(Color.decode(content));
			case "desc", "description" -> builder.setDescription(content);
			case "footer" -> builder.setFooter(content);
			case "img", "image" -> builder.setImage(content);
			case "thmb", "thumbnail" -> builder.setThumbnail(content);
			case "author" -> builder.setAuthor(content);
		}
	}

	private String unescape(String input) {
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

	/*
	 - Conversion methods
	 */

	@NotNull
	public String toRawString() {
		return raw;
	}

	@Nullable
	public MessageEmbed toEmbed() {
		return embed;
	}

	/*
	 - Send message to text channel
	 */

	@NotNull
	public MessageCreateAction getSendAction(TextChannel channel) {
		return (embed == null ? channel.sendMessage(raw) : channel.sendMessageEmbeds(embed));
	}

	public boolean send(@NotNull TextChannel channel) {
		getSendAction(channel).queue();
		return true;
	}

	/*
	 - Reply to action
	 */

	public ReplyCallbackAction getReplyAction(IReplyCallback toReply) {
		return (embed == null ? toReply.reply(raw) : toReply.replyEmbeds(embed)).setEphemeral(ephemeral);
	}

	public boolean reply(IReplyCallback toReply) {
		getReplyAction(toReply).queue();
		return true;
	}

	public boolean replyAfter(IReplyCallback toReply, int delay, TimeUnit unit) {
		getReplyAction(toReply).queueAfter(delay, unit);
		return true;
	}

	public MessageCreateAction getMsgReplyAction(Message toReply) {
		return (embed == null ? toReply.reply(raw) : toReply.replyEmbeds(embed));
	}

	public boolean reply(Message toReply) {
		getMsgReplyAction(toReply).queue();
		return true;
	}
}
