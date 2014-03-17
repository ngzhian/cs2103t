package com.ngzhian.textbuddy;

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
public class TextBuddyMain {
  private static final String MSG_BAD_ARGUMENTS = "BAD %s";
  private TextBuddy textBuddy;
  private UI ui;

  public static void main(String[] args) {
    checkArguments(args);
    System.out.println("Welcome to TextBuddy!");
    int exitCode = new TextBuddyMain().launchCli();
    System.exit(exitCode);
  }

  private static void checkArguments(String[] args) {
    if (args.length == 0) {
      System.out.println(String.format(MSG_BAD_ARGUMENTS, "no arguments"));
      System.exit(0);
    }
  }

  private int launchCli() {
    textBuddy = new TextBuddy();
    ui = new CliUserInterface(textBuddy);
    runCommandsUntilExit(ui);
    return 0;
  }

  @SuppressWarnings("unused")
  private int launchGui() {
    return 0;
  }

  private int runCommandsUntilExit(UI ui) {
    String input = ui.getUserInput();
    while (!shouldExit(input)) {
      Command c = ui.parseToCommand();
      Result r = textBuddy.execute(c);
      String result = textBuddy.runthrucli(input);
      ui.giveFeedback(result);
      input = ui.getUserInput();
    }
    return 0;
  }

  private boolean shouldExit(String input) {
    return input.equalsIgnoreCase("exit");
  }
}
