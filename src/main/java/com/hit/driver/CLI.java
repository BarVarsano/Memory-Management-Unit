package com.hit.driver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;

import com.hit.exception.HardDiskException.ActionError;
import com.hit.model.MMUModel;
import com.hit.view.View;

/*
 * Bar Varsano 307929497
 * Amit Levy 305650517
 */
//TODO
public class CLI extends Observable implements Runnable, View {

	/* Members */
	public static final String START = "start";
	public static final String STOP = "stop";
	public static final String LRU = "LRU";
	public static final String MRU = "MRU";
	public static final String RANDOM = "RANDOM";
	private static String[] algoConfiguration;
	private Scanner localScanner;
	private OutputStreamWriter writer;

	/* Constructor */
	public CLI(InputStream in, OutputStream out) {
		localScanner = new Scanner(in);
		writer = new OutputStreamWriter(out);
	}
	
	/* Public Methods */
	@Override
	public void run() {
		GetCommand();
	}
	
	@Override
	public void start() {
		GetCommand();
	}

	public void write(String string) {
		try {
			writer.write(string + System.lineSeparator());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* Private Methods */
	private void GetCommand() {
		boolean valid = false;
		String command = localScanner.nextLine();
		while (!valid) {
			if (command == null) {
				throw new NullPointerException("The value can not be empty.");
			}
			command = command.toLowerCase();
			switch (command) {
			case "start":
				while (!valid) {
					write("Please enter required algorithm and RAM capacity");
					command = localScanner.nextLine();
					if (command.contains(" ")) {
						algoConfiguration = command.split(" ");
						algoConfiguration[0] = algoConfiguration[0].toUpperCase();
						if (!CheckInput()) {
							write("Wrong Command, system will now exit");
						} else {
							valid = true;
						}
					}
				}
				break;
			case "stop":
				valid = true;
				write("Thank You");
				break;
			default:
				write("Value does not exist.");
				command = localScanner.nextLine();
			}
		}
		setChanged();
		notifyObservers(command);
	}

	private static boolean IsNumeric(String checkMe) {
		for (char c : checkMe.toCharArray()) {
			if (!Character.isDigit(c)) {
				algoConfiguration = null;
				return false;
			}
		}
		return true;
	}

	private static boolean CheckInput() {
		boolean result = false;
		if (algoConfiguration[0].equalsIgnoreCase("RANDOM") || algoConfiguration[0].equalsIgnoreCase("LRU")
				|| algoConfiguration[0].equalsIgnoreCase("MRU")) {
			if (IsNumeric(algoConfiguration[1]) == true)
				result = true;
		}
		return result;
	}
}
