package net.codersky.mcsb;

public enum StartCode {

	/** Bot started successfully. */
	OK,
	/** Critical error: config.yml failed to set up. */
	CONFIG_SETUP_FAIL,
	/** Critical error: No bot token on config.yml. */
	NO_BOT_TOKEN,
	/** Critical error: JDA failed to start. */
	JDA_SETUP_FAIL;

	public boolean isOk() {
		return this == OK;
	}
}
