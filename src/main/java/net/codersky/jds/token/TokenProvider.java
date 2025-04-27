package net.codersky.jds.token;

import org.jetbrains.annotations.Nullable;

/**
 * {@link FunctionalInterface Functional interface} with a
 * {@link #getToken()} method, used to provide JDA tokens
 * in different ways.
 *
 * @since JDS 1.0.0
 *
 * @author xDec0de_
 */
@FunctionalInterface
public interface TokenProvider {

	/**
	 * Gets the token from this {@link TokenProvider}. This token
	 * can be {@code null} if an error occurs while obtaining the
	 * token, and {@link String#isEmpty() empty} if no error occurred
	 * but no token could be obtained.
	 * <p>
	 * For example, a {@link FileTokenProvider} provider, will return
	 * an empty string if no token has been written into the file,
	 * and {@code null} if any error occurs while reading its file.
	 *
	 * @return The token provided by this {@link TokenProvider}. Can
	 * be {@code null} or {@link String#isEmpty() empty}.
	 *
	 * @since JDS 1.0.0
	 */
	@Nullable
	String getToken();
}
