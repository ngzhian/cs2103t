package com.ngzhian.textbuddy;

import java.util.Scanner;

public class CliUserInterface implements UI {
  Scanner sc;
  private TextBuddy textBuddy;
  private String input;
  private String filename;
  private CliInputParser cliInputParser;
  private static final String PROMPT = "command: ";
  static final String MSG_WELCOME = "Welcome to TextBuddy. %s is ready for use";

  public CliUserInterface(TextBuddy textBuddy) {
    sc = new Scanner(System.in);
    this.textBuddy = textBuddy;
    filename = textBuddy.getFile().getName();
  }

  void printWelcomeMessage() {
    showToUser(MSG_WELCOME, filename);
  }

  private void promptUser() {
    showToUser(PROMPT);
  }

  @Override
  public String getUserInput() {
    promptUser();
    input = sc.nextLine();
    return input;
  }

  @Override
  public void giveFeedback(String output) {
    System.out.println(output);
  }

  void showToUser(String message, Object... args) {
    System.out.println(String.format(message, args));
  }

  @Override
  public Command parseToCommand() {
    cliInputParser = new CliInputParser(input);
    return cliInputParser.parse();
  }

  private class CliInputParser implements InputParser {
    String input;

    public CliInputParser(String cliInput) {
      this.input = cliInput.trim();
    }

    @Override
    public Command parse() {
      String command = input.split(" ")[0];
      String params = parseInputForParameters(input);
      switch (command) {
        case "add" :
          AddUseCase add = new AddUseCase(textBuddy);
          add.setText(params);
          return add;
          // case "display" :
          // return Command.TYPE.DISPLAY;
          // case "delete" :
          // return Command.TYPE.DELETE;
          // case "clear" :
          // return Command.TYPE.CLEAR;
          // case "sort" :
          // return Command.TYPE.SORT;
          // case "search" :
          // return Command.TYPE.SEARCH;
          // default :
          // return Command.TYPE.INVALID;
        default :
          return null;
      }
      // if (type == Command.TYPE.SEARCH) {

      // String[] searchTerms = parseInputForSearchTerms(input);
      // c.setSearchTerms(searchTerms);
      // } else {
      // String parameter = parseInputForParameters(input);
      // c.setText(new Text(parameter));
      // }
      // return c;
    }

    private String[] splitStringUsingFirstSpace(String line) {
      return line.split(" ", 2); // split command from the rest
    }

    private String[] splitCommandFromParameters(String line) {
      return splitStringUsingFirstSpace(line);
    }

    private String parseInputForParameters(String line) {
      String[] words = splitCommandFromParameters(line);
      // words[0] contains user entered command
      boolean hasNoParameters = words.length == 1;
      return hasNoParameters ? "" : words[1];
    }

  }

}
