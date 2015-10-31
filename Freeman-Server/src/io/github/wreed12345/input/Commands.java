package io.github.wreed12345.input;

/**
 * Represents Commands used for the server
 * 
 * @author William Reed
 * @since 0.0.11 - 1/16/14
 */
public enum Commands {
	/** Usage: get password (username) */
	GET_PASSWORD("get password", 1),
	/** Usage: game id (game name) */
	GAME_ID("game id", 1),
	/** Usage: game stop (game id) */
	GAME_STOP("game stop", 1),
	/** Usage: server info */
	SERVER_INFO("server info", 0),
	/** Usage: stop (message to tell users) */
	STOP("stop", 1),
	/** Usage: delete account (username of account to be deleted) */
	DELETE_ACCOUNT("delete account", 1),
	/** Usage: ban username (username) (reason) */
	BAN_USERNAME("ban username", 2),
	/** Usage: ban ip (ip address) (reason) */
	BAN_IP("ban ip", 2),
	/** Usage: save */
	SAVE("save", 0),
	/** Usage: help */
	HELP("help", 0),
	/**Usage: create accont (username) (password)*/
	CREATE_ACCOUNT("create account", 2);

	private String command;
	private int args;

	Commands(String command, int args) {
		this.command = command;
		this.args = args;
	}

	/**
	 * @return Gets the actual command string involved with the enum
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @return The amount of arguements necesary for the command to function.
	 */
	public int getAmountOfArguements() {
		return args;
	}
}
