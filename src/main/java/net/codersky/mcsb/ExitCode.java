package net.codersky.mcsb;

/**
 * {@code enum} that contains all expected error codes that
 * may cause a {@link MCSkyBot} to exit (Critical errors).
 *
 * @see #exit()
 *
 * @author xDec0de_
 */
public enum ExitCode {

	/** Program executed successfully. */
	OK,
	/** Critical error: config.yml failed to set up. */
	CONFIG_SETUP_FAIL;

	/**
	 * Exits the JVM with this {@link ExitCode} by calling
	 * {@link System#exit(int)} with an exit code that corresponds
	 * to the {@link #ordinal()} of this {@link ExitCode}. If this
	 * {@link ExitCode} is not {@link ExitCode#OK}, then
	 * an error message is printed on {@link System#err}
	 */
	public void exit() {
		if (this != OK)
			System.err.println("Stopping bot with exit code: " + name());
		System.exit(ordinal());
	}
}
