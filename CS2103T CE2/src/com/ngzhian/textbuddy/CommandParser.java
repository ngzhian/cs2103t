package com.ngzhian.textbuddy;

public class CommandParser {
  String userInput;
  Command.TYPE command;
  String parameter;

  public CommandParser() {
    userInput = new String();
    command = null;
    parameter = new String();
  }

  public Command.TYPE getCommand() {
    return command;
  }

  public String getParameters() {
    return parameter;
  }

  public boolean parseInput(String input) {
    input = input.trim();
    CliInputParser cip = new CliInputParser(input);
    Command c = cip.parse();
    return false;
  }

}
