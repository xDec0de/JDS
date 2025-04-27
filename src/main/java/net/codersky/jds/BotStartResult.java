package net.codersky.jds;

public enum BotStartResult {

	/** Bot started successfully. */
	OK,
	/** Critical error: No bot token on config.yml. */
	NO_BOT_TOKEN,
	/** Critical error: JDA failed to start. */
	JDA_SETUP_FAIL;

	public boolean isOk() {
		return this == OK;
	}
}
