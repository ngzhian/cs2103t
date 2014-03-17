package com.ngzhian.textbuddy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * TextBuddy is used to store and retrieve Text that the user wishes to save. A
 * Text is a single line of characters. The user's texts are then stored onto a
 * file on disk. The command format is given by the interaction below:
 * 
 * <pre>
 * Welcome to TextBuddy. mytextfile.txt is ready for use
 * command: add little brown fox
 * added to mytextfile.txt: "little brown fox"
 * command:  display
 * 1. little brown fox
 * command: add jumped over the moon
 * added to mytextfile.txt: "jumped over the moon"
 * command: display
 * 1. little brown fox
 * 2. jumped over the moon
 * command: delete 2
 * deleted from mytextfile.txt: "jumped over the moon"
 * command: display
 * 1. little brown fox
 * command: clear
 * all content deleted from mytextfile.txt
 * command: display
 * mytextfile.txt is empty
 * command: exit
 * </pre>
 * 
 * @author Ng Zhi An
 * 
 */
public class TextBuddy {
  private static final String MSG_BAD_ARGUMENTS = "Bad arguments supplied: %s.\nExiting...";
  private static final String MSG_WELCOME = "Welcome to TextBuddy. %s is ready for use";
  private static final String MSG_ADD = "added to %s: \"%s\"";
  private static final String MSG_DELETE = "deleted from %s: \"%s\"";
  private static final String MSG_CLEAR = "all content deleted from %s";
  private static final String MSG_EMPTY_FILE = "%s is empty";
  private static final String MSG_GENERIC_ERROR = "Error: %s. Please try again";
  private static final String DISPLAY_LINE = "%d. %s";
  private static final String PROMPT = "command: ";
  private static final String MSG_WRONG_NUMBER_FORMAT = "\"%s\" is not a number";
  private static final String MSG_BAD_INDEX = "%d is not a valid index. Valid range is %d to %d, inclusive";
  private static final String MSG_DELETE_EMPTY_TEXTS = "you have no text to delete!";

  // stores all the texts of a user
  private static ArrayList<String> texts;

  // file on disc where texts are stored
  private static File file;
  private static Scanner sc;
  private static final int INVALID_INDEX = -1;

  // indicates when user wants to exit the program
  private static boolean shouldExit = false;

  // different command types used in TextBuddy
  enum COMMAND {
    ADD, DISPLAY, DELETE, CLEAR, EXIT, INVALID
  };

  public static void main(String[] args) {
    checkArguments(args);
    setup(args[0]);
    printWelcomeMessage();
    acceptCommandsUntilUserExits();
  }

  private static void checkArguments(String[] args) {
    if (args.length == 0) {
      showToUser(MSG_BAD_ARGUMENTS, "no arguments");
      System.exit(0);
    }
  }

  private static void setup(String filename) {
    initialize(filename);
    loadTextsFromFile(file);
  }

  private static void printWelcomeMessage() {
    showToUser(MSG_WELCOME, file.getName());
  }

  private static void acceptCommandsUntilUserExits() {
    String input;
    while (!shouldExit) {
      promptUser();
      input = getUserInput();
      COMMAND command = parseInputForCommand(input);
      execute(command, getParameters(input));
    }
  }

  private static void initialize(String filename) {
    texts = new ArrayList<String>();
    file = new File(filename);
    sc = new Scanner(System.in);
  }

  private static void loadTextsFromFile(File file) {
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(
          new FileInputStream(file)));
      String line;
      while ((line = br.readLine()) != null) {
        texts.add(line);
      }
      br.close();
    } catch (FileNotFoundException e) {
    } catch (IOException e) {
      showToUser(MSG_GENERIC_ERROR, e.getMessage());
    }
  }

  private static void promptUser() {
    showToUser(PROMPT);
  }

  private static String getUserInput() {
    // strip starting and trailing blank spaces as they may cause trouble later
    return sc.nextLine().trim();
  }

  private static COMMAND parseInputForCommand(String input) {
    String[] words = input.split(" ");
    String command = words[0];
    switch (command) {
      case "add" :
        return COMMAND.ADD;
      case "display" :
        return COMMAND.DISPLAY;
      case "delete" :
        return COMMAND.DELETE;
      case "clear" :
        return COMMAND.CLEAR;
      case "exit" :
        return COMMAND.EXIT;
      default :
        return COMMAND.INVALID;
    }
  }

  private static void execute(COMMAND command, String parameters) {
    switch (command) {
      case ADD :
        addText(parameters);
        break;
      case DISPLAY :
        displayTexts();
        return; // display doesn't involve writing changes to file, so we return
      case DELETE :
        deleteTexts(parameters);
        break;
      case CLEAR :
        clearTexts();
        break;
      case EXIT :
        exit();
        break;
      case INVALID :
        return;
      default :
        throw new Error(); // this should never happen
    }
    // this is called if ADD, DELETE, or CLEAR
    flushToFile();
  }

  private static String getParameters(String line) {
    String[] words = splitCommandFromParameters(line);
    // words[0] contains user entered command
    if (words.length == 1) {
      return null;
    } else {
      return words[1];
    }
  }

  private static String[] splitCommandFromParameters(String line) {
    return line.split(" ", 2); // split command from the rest
  }

  private static void addText(String parameters) {
    if (parameters == null) {
      return;
    }
    texts.add(parameters);
    showToUser(MSG_ADD, file.getName(), parameters);
  }

  private static void displayTexts() {
    if (texts.size() == 0) {
      showToUser(MSG_EMPTY_FILE, file.getName());
    }
    for (int i = 0; i < texts.size(); i++) {
      showToUser(DISPLAY_LINE, i + 1, texts.get(i));
    }
  }

  private static void deleteTexts(String parameters) {
    if (texts.size() == 0) {
      showToUser(MSG_DELETE_EMPTY_TEXTS);
      return;
    }
    int index = getIndex(parameters);
    removeTextAtIndex(index);
  }

  private static void clearTexts() {
    texts.clear();
    showToUser(MSG_CLEAR, file.getName());
  }

  private static void exit() {
    sc.close();
    shouldExit = true;
  }

  private static int getIndex(String parameters) {
    try {
      return Integer.parseInt(parameters) - 1;
    } catch (NumberFormatException e) {
      showToUser(MSG_WRONG_NUMBER_FORMAT, parameters);
    }
    return INVALID_INDEX;
  }

  private static void removeTextAtIndex(int index) {
    if (index == INVALID_INDEX) {
      return;
    }
    try {
      String content = texts.get(index);
      texts.remove(index);
      showToUser(MSG_DELETE, file.getName(), content);
    } catch (IndexOutOfBoundsException e) {
      showToUser(MSG_BAD_INDEX, index, 1, texts.size());
    }

  }

  private static void flushToFile() {
    try {
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
          new FileOutputStream(file)));
      for (String s : texts) {
        bw.write(s);
        bw.newLine();
      }
      bw.close();
    } catch (FileNotFoundException e) {
    } catch (IOException e) {
    }
  }

  private static void showToUser(String message, Object... args) {
    System.out.println(String.format(message, args));
  }
}
