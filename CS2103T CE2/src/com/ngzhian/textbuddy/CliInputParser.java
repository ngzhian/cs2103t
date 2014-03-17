package com.ngzhian.textbuddy;

public class CliInputParser implements InputParser {
  String input;

  public CliInputParser(String cliInput) {
    this.input = cliInput.trim();
  }

  @Override
  public Command parse() {
    Command.TYPE type = parseInputForCommand(input);
    Command c = new Command(type);
    if (type == Command.TYPE.SEARCH) {
      String[] searchTerms = parseInputForSearchTerms(input);
      c.setSearchTerms(searchTerms);
    } else {
      String parameter = parseInputForParameters(input);
      c.setText(new Text(parameter));
    }
    return c;
  }

  private String[] parseInputForSearchTerms(String line) {
    String[] words = splitCommandFromParameters(line);
    // words[0] contains user entered command
    boolean hasNoParameters = words.length == 1;
    return hasNoParameters ? new String[0] : words;
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

  private Command.TYPE parseInputForCommand(String input) {
    String command = input.split(" ")[0];
    switch (command) {
      case "add" :
        return Command.TYPE.ADD;
      case "display" :
        return Command.TYPE.DISPLAY;
      case "delete" :
        return Command.TYPE.DELETE;
      case "clear" :
        return Command.TYPE.CLEAR;
      case "sort" :
        return Command.TYPE.SORT;
      case "search" :
        return Command.TYPE.SEARCH;
      default :
        return Command.TYPE.INVALID;
    }
  }

}
