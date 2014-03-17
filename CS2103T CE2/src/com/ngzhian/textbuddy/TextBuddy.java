package com.ngzhian.textbuddy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TextBuddy {
  private static final String MSG_BAD_ARGUMENTS = "Bad arguments supplied: %s.\nExiting...";
  private static final String MSG_UNABLE_TO_SAVE_CHANGES = "Error opening %s, TextBuddy will not be able to save changes";
  private static final String MSG_ERROR_SAVING_CHANGES = "Error saving your last action \"%s\". TextBuddy might not be up to date";
  private static final String MSG_DELETE = "deleted from %s: \"%s\"";
  private static final String MSG_CLEAR = "all content deleted from %s";
  private static final String MSG_EMPTY_FILE = "%s is empty";
  private static final String MSG_WRONG_NUMBER_FORMAT = "\"%s\" is not a number";
  private static final String MSG_BAD_INDEX = "%d is not a valid index. Valid range is 1 to %d, inclusive";
  private static final String MSG_DELETE_EMPTY_TEXTS = "you have no text to delete!";
  private static final String MSG_NO_SEARCH_TERM_GIVEN = "no search term was given!";
  private static final String MSG_NO_SEARCH_RESULTS_FOUND = "no results found for: %s";
  private static final String MSG_INVALID_ARGUMENT = "Invalid command supplied";
  private static final String MSG_EXIT = "Exiting...";
  private static final String DISPLAY_LINE = "%d. %s";
  private static final String DEFAULT_FILE_NAME = "TextBuddyData.txt";
  private static final String NEWLINE = System.lineSeparator();

  // stores all the texts of a user
  private static TextStore store;

  // file on disc where texts are stored
  private static File file;
  private static CommandParser parser;

  // different command types used in TextBuddy
  enum COMMAND {
    ADD, DISPLAY, DELETE, CLEAR, EXIT, SORT, SEARCH, INVALID
  };

  public TextBuddy() {
    this(DEFAULT_FILE_NAME);
  }

  public TextBuddy(String filename) {
    file = new File(filename);
    parser = new CommandParser();
    new Scanner(System.in);
    try {
      store = new TextStore(file);
    } catch (IOException e) {
      // CliUserInterface.showToUser(MSG_UNABLE_TO_SAVE_CHANGES, filename);
    }
  }

  public String runthrucli(String userInput) {
    try {
      parser.parseInput(userInput);
      return null;
    } catch (Exception e) {
      return String.format("error parsing: %s", userInput);
    }
  }

  public String execute(Command command, String parameters) {
    String result = "";
    try {
      result = (command == null ? "" : executeCommand(command, parameters));
    } catch (IOException e) {
      result = formatMsgToString(MSG_ERROR_SAVING_CHANGES, command.toString()
          + parameters);
    }
    return result;
  }

  private String executeCommand(Command command, String parameters)
      throws IOException {
    String result = "";
    // switch (command) {
    // case ADD :
    // result = addText(parameters);
    // break;
    // case DISPLAY :
    // result = displayTexts();
    // return result; // display doesn't involve writing changes to file, so we
    // // return
    // case DELETE :
    // result = deleteTexts(parameters);
    // break;
    // case CLEAR :
    // result = clearTexts();
    // break;
    // case SORT :
    // result = sortTexts();
    // break;
    // case SEARCH :
    // result = searchTexts(parameters);
    // break;
    // case INVALID :
    // result = formatMsgToString(MSG_INVALID_ARGUMENT);
    // break;
    // default :
    // throw new Error(); // this should never happen
    // }
    return result;
  }

  private String displayTexts() {
    if (store.isEmpty()) {
      return formatMsgToString(MSG_EMPTY_FILE, file.getName());
    }
    return formatTextsIntoString(store.getAllTexts());
  }

  private String deleteTexts(String parameters) throws IOException {
    if (store.getSize() == 0) {
      return formatMsgToString(MSG_DELETE_EMPTY_TEXTS);
    }
    try {
      int index = getIndex(parameters);
      return deleteTextAtIndex(index);
    } catch (NumberFormatException e) {
      return formatMsgToString(MSG_WRONG_NUMBER_FORMAT, parameters);
    }
  }

  private String clearTexts() throws IOException {
    store.clearTexts();
    return formatMsgToString(MSG_CLEAR, file.getName());
  }

  private String searchTexts(String searchTerm) {
    if (searchTerm == null || searchTerm.isEmpty()) {
      return formatMsgToString(MSG_NO_SEARCH_TERM_GIVEN);
    }
    ArrayList<String> results = searchForWord(searchTerm);
    boolean noResults = results.isEmpty();
    return noResults ? formatMsgToString(MSG_NO_SEARCH_RESULTS_FOUND,
        searchTerm) : formatTextsIntoString(results);
  }

  private String sortTexts() throws IOException {
    store.sortTexts();
    return displayTexts();
  }

  private String formatTextsIntoString(ArrayList<String> texts) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < texts.size(); i++) {
      sb.append(String.format(DISPLAY_LINE + NEWLINE, i + 1, texts.get(i)));
    }
    return sb.toString();
  }

  private int getIndex(String parameters) throws NumberFormatException {
    int result = Integer.parseInt(parameters) - 1;
    return result;
  }

  private String deleteTextAtIndex(int index) throws IOException {
    try {
      String deleted = store.deleteText(index);
      return formatMsgToString(MSG_DELETE, file.getName(), deleted);
    } catch (IndexOutOfBoundsException e) {
      return formatMsgToString(MSG_BAD_INDEX, index, store.getSize());
    }
  }

  private ArrayList<String> searchForWord(String parameters) {
    if (parameters == null) {
      return new ArrayList<String>();
    }
    return store.searchForWord(parameters);
  }

  private String formatMsgToString(final String message, Object... args) {
    return String.format(message, args);
  }

  public File getFile() {
    return file;
  }

  public void setFile(File f) {
    file = f;
  }

  public int getSize() {
    return store.getSize();
  }

  public Result execute(Command c) {
    Result r = c.execute();
    return r;
  }
}
