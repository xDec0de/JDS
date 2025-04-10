package net.codersky.mcsb.message;

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
import java.util.concurrent.TimeUnit;

public class MSBMessage {

	private final String raw;
	private final MessageEmbed embed;
	private final boolean ephemeral;

	public MSBMessage(JDA jda, @NotNull String raw) {
		this.ephemeral = raw.startsWith("eph:");
		this.raw = this.ephemeral ? raw.substring(4) : raw;
		if (this.raw.startsWith("msg:"))
			this.embed = setupEmbed(jda, Color.WHITE, this.raw);
		else
			this.embed = this.raw.startsWith("err:") ? setupEmbed(jda, Color.RED, this.raw) : null;
	}

	private MessageEmbed setupEmbed(JDA jda, Color color, String raw) {
		try {
			String[] data = raw.substring(4).split(">>");
			if (data.length == 0)
				return null;
			final EmbedBuilder builder = new EmbedBuilder()
					.setColor(color)
					.setFooter("SkyBot", jda.getSelfUser().getAvatarUrl());
			builder.setTitle(data[0]);
			if (data.length > 1 && !data[1].isBlank())
				builder.setDescription(data[1]);
			for (int i = 2; i < data.length; i++) {
				String[] field = data[i].split("=");
				boolean inline = field[0].charAt(0) == '$';
				builder.addField(inline ? field[0].substring(1) : field[0], field[1], inline);
			}
			return builder.build();
		} catch (Exception ex) {
			return null;
		}
	}

	@NotNull
	public String toRawString() {
		return raw;
	}

	@Nullable
	public MessageEmbed toEmbed() {
		return embed;
	}

	@NotNull
	public MessageCreateAction getSendAction(TextChannel channel) {
		return (embed == null ? channel.sendMessage(raw) : channel.sendMessageEmbeds(embed));
	}

	public boolean send(@NotNull TextChannel channel) {
		getSendAction(channel).queue();
		return true;
	}

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
