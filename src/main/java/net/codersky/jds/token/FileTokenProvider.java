package net.codersky.jds.token;

import net.codersky.jsky.JFiles;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.util.Objects;

public class FileTokenProvider implements TokenProvider {

	private final File file;

	public FileTokenProvider(@NotNull File file) {
		this.file = Objects.requireNonNull(file);
	}

	@NotNull
	public File getFile() {
		return file;
	}

	public boolean setup() {
		return file.exists() || JFiles.create(file);
	}

	@Override
	public @Nullable String getToken() {
		if (!file.exists()) {
			setup();
			return "";
		}
		try {
			if (!file.isFile() || !file.canRead())
				return null;
			return Files.readString(file.toPath());
		} catch (InvalidPathException | IOException | OutOfMemoryError | SecurityException e) {
			return null;
		}
	}
}
