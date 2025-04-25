package net.codersky.jds.cmd;

import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationMap;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for any type of command that holds {@link SlashCommandData},
 * so implementations of Discord slash commands.
 *
 * @author xDec0de_
 *
 * @since JDS 1.0.0
 */
public interface JDSICommand {

	/**
	 * Gets the {@link SlashCommandData} being used internally
	 * by this {@link JDSICommand}.
	 *
	 * @return The {@link SlashCommandData} being used internally
	 * by this {@link JDSICommand}.
	 *
	 * @since JDS 1.0.0
	 */
	@NotNull
	SlashCommandData getSlashCommandData();

	/*
	 - Name
	 */

	/**
	 * Gets the {@link LocalizationMap} for the <b>name</b> of this command. This map
	 * allows having a different command name for all the {@link DiscordLocale languages}
	 * supported by Discord.
	 *
	 * @return The <b>name</b> {@link LocalizationMap} of this {@link JDSICommand}.
	 *
	 * @since JDS 1.0.0
	 */
	@NotNull
	default LocalizationMap getNameLocalizations() {
		return getSlashCommandData().getNameLocalizations();
	}

	/**
	 * Sets the name of this {@link JDSICommand} on different {@link DiscordLocale languages}.
	 * You can set different names for different locales, or one for each locale.
	 *
	 * @param name The name to set for the provided {@code locales}.
	 * @param locales The locales that will use the provided {@code name}. You can use one
	 * or as many as you want.
	 *
	 * @return This {@link JDSICommand}.
	 *
	 * @since JDS 1.0.0
	 */
	@NotNull
	default JDSICommand setName(@NotNull String name, @NotNull DiscordLocale @NotNull ... locales) {
		final LocalizationMap map = getNameLocalizations();
		for (DiscordLocale locale : locales)
			map.setTranslation(locale, name);
		return this;
	}

	/**
	 * Sets the name of this {@link JDSICommand} for all {@link DiscordLocale languages}. This
	 * will override any other {@link #setName(String, DiscordLocale...) setting} for any other
	 * languages, so if you want to set a default, this must be done first.
	 *
	 * @param name The name of this {@link JDSICommand} for <b>all</b>
	 * {@link DiscordLocale languages}.
	 *
	 * @return This {@link JDSICommand}.
	 *
	 * @since JDS 1.0.0
	 */
	default JDSICommand setName(@NotNull String name) {
		return setName(name, DiscordLocale.values());
	}

	/*
	 - Description
	 */

	/**
	 * Gets the {@link LocalizationMap} for the <b>description</b> of this command. This map
	 * allows having a different command description for all the {@link DiscordLocale languages}
	 * supported by Discord.
	 *
	 * @return The <b>description</b> {@link LocalizationMap} of this {@link JDSICommand}.
	 *
	 * @since JDS 1.0.0
	 */
	@NotNull
	default LocalizationMap getDescLocalizations() {
		return getSlashCommandData().getDescriptionLocalizations();
	}

	/**
	 * Sets the description of this {@link JDSICommand} on different {@link DiscordLocale languages}.
	 * You can set different descriptions for different locales, or one for each locale.
	 *
	 * @param desc The description to set for the provided {@code locales}.
	 * @param locales The locales that will use the provided {@code name}. You can use one
	 * or as many as you want.
	 *
	 * @return This {@link JDSICommand}.
	 *
	 * @since JDS 1.0.0
	 */
	@NotNull
	default JDSICommand setDesc(@NotNull String desc, @NotNull DiscordLocale @NotNull ... locales) {
		final LocalizationMap map = getDescLocalizations();
		for (DiscordLocale locale : locales)
			map.setTranslation(locale, desc);
		return this;
	}

	/**
	 * Sets the description of this {@link JDSICommand} for all {@link DiscordLocale languages}. This
	 * will override any other {@link #setDesc(String, DiscordLocale...) setting} for any other
	 * languages, so if you want to set a default, this must be done first.
	 *
	 * @param desc The description of this {@link JDSICommand} for <b>all</b>
	 * {@link DiscordLocale languages}.
	 *
	 * @return This {@link JDSICommand}.
	 *
	 * @since JDS 1.0.0
	 */
	default JDSICommand setDesc(@NotNull String desc) {
		return setDesc(desc, DiscordLocale.values());
	}

	/*
	 - NSFW
	 */

	/**
	 * Whether this command should only be usable on age-restricted channels or not.
	 * NSFW commands cannot be used on regular channels.
	 *
	 * @return The NSFW status of this {@link JDSICommand}. {@code true} if enabled
	 * {@code false} otherwise.
	 *
	 * @since JDS 1.0.0
	 *
	 * @see #setNSFW(boolean)
	 * @see <a href="https://support.discord.com/hc/en-us/articles/10123937946007">
	 *     Age-Restricted Commands FAQ</a>
	 */
	default boolean isNSFW() {
		return getSlashCommandData().isNSFW();
	}

	/**
	 * Sets the NSFW status of this command. NSFW commands cannot be used on regular channels,
	 * only on age-restricted channels.
	 *
	 * @param nsfw The new NSFW status of this {@link JDSICommand}.
	 *
	 * @return This {@link JDSICommand}.
	 *
	 * @since JDS 1.0.0
	 *
	 * @see #isNSFW()
	 * @see <a href="https://support.discord.com/hc/en-us/articles/10123937946007">
	 *     Age-Restricted Commands FAQ</a>
	 */
	@NotNull
	default JDSICommand setNSFW(boolean nsfw) {
		getSlashCommandData().setNSFW(nsfw);
		return this;
	}
}
