package net.codersky.jds.message.button;

import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import org.jetbrains.annotations.NotNull;

public class JDSButtonBuilder {

	public Button of(@NotNull String raw) {
		Button.of(ButtonStyle.PRIMARY, "button-id", "Hi");
		return null;
	}
}
