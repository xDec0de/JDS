package net.codersky.mcsb.message;

import net.codersky.jsky.strings.JStrings;
import net.codersky.mcsb.message.embed.JDSEmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

public class JDSMessage {

	private final String raw;
	private MessageEmbed embed = null;
	private final boolean ephemeral;

	public JDSMessage(@NotNull String raw) {
		this.ephemeral = raw.startsWith("eph:");
		this.raw = this.ephemeral ? raw.substring(4) : raw;
		JStrings.match(this.raw, "<e", "/>", tokens -> {
			this.embed = JDSEmbedBuilder.build(tokens);
		});
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
