package io.github.wreed12345.input;

import io.github.wreed12345.shared.Game;
import io.github.wreed12345.shared.Player;

import java.util.List;
import java.util.Scanner;

public class CommandListener extends Thread {

	private Scanner input;
	private CommandProcessor cmdProcessor;
	
	public CommandListener(List<Game> games, List<Player> onlinePlayers, List<String> usernames) {
		input = new Scanner(System.in);
		cmdProcessor = new CommandProcessor(games, onlinePlayers, usernames);
	}

	public void run() {
		//wait for user to enter something.
		while(input.hasNext()){
			String command = input.nextLine();
			//send to cmd processor.
			cmdProcessor.processCommand(command);
		}
	}
}
