package com.ngzhian.textbuddy;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CommandParserTest {
  CommandParser commandParser;
  private static final String ADD_STRING = "add little brown fox";
  private static final String ADD_STRING_WITH_LEADING_WHITESPACE = " add little brown fox";
  private static final String EXPECTED_ARGUMENT_FOR_ADD = "little brown fox";
  private static final String ADD_STRING_WITH_NO_ARGUMENT = " add";
  private static final String DISPLAY_STRING = "display";
  private static final String DISPLAY_STRING_WITH_ARGUMENT = "display this";
  private static final String EXPECTED_ARGUMENT_FOR_DISPLAY = "this";
  private static final String DELETE_STRING = "delete 1";
  private static final String DELETE_PARAMETER = "1";
  private static final String DELETE_STRING_WITH_NO_ARGUMENT = "delete";
  private static final String CLEAR_STRING = "clear";
  private static final String EXIT_STRING = "exit";
  private static final String SORT_STRING = "sort";
  private static final String SEARCH_STRING = "search fox";
  private static final String SEARCH_PARAMETER = "fox";
  private static final String SEARCH_STRING_WITH_NO_ARGUMENT = "search";
  private static final String INVALID_STRING = "invalid";

  @Before
  public void setup() {
    commandParser = new CommandParser();
  }

  @Test
  public void testAddCommand() {
    commandParser.parseInput(ADD_STRING);
    assertEquals(TextBuddy.COMMAND.ADD, commandParser.getCommand());
    assertEquals(EXPECTED_ARGUMENT_FOR_ADD, commandParser.getParameters());

    commandParser.parseInput(ADD_STRING_WITH_LEADING_WHITESPACE);
    assertEquals(TextBuddy.COMMAND.ADD, commandParser.getCommand());
    assertEquals(EXPECTED_ARGUMENT_FOR_ADD, commandParser.getParameters());

    commandParser.parseInput(ADD_STRING_WITH_NO_ARGUMENT);
    assertEquals(TextBuddy.COMMAND.ADD, commandParser.getCommand());
    assertEquals("", commandParser.getParameters());
  }

  @Test
  public void testDisplayCommand() {
    commandParser.parseInput(DISPLAY_STRING);
    assertEquals(TextBuddy.COMMAND.DISPLAY, commandParser.getCommand());
    assertEquals("", commandParser.getParameters());

    commandParser.parseInput(DISPLAY_STRING_WITH_ARGUMENT);
    assertEquals(TextBuddy.COMMAND.DISPLAY, commandParser.getCommand());
    assertEquals(EXPECTED_ARGUMENT_FOR_DISPLAY, commandParser.getParameters());
  }

  @Test
  public void testDeleteCommand() {
    commandParser.parseInput(DELETE_STRING);
    assertEquals(TextBuddy.COMMAND.DELETE, commandParser.getCommand());
    assertEquals(DELETE_PARAMETER, commandParser.getParameters());

    commandParser.parseInput(DELETE_STRING_WITH_NO_ARGUMENT);
    assertEquals(TextBuddy.COMMAND.DELETE, commandParser.getCommand());
    assertEquals("", commandParser.getParameters());
  }

  @Test
  public void testClearCommand() {
    commandParser.parseInput(CLEAR_STRING);
    assertEquals(TextBuddy.COMMAND.CLEAR, commandParser.getCommand());
    assertEquals("", commandParser.getParameters());
  }

  @Test
  public void testExitCommand() {
    commandParser.parseInput(EXIT_STRING);
    assertEquals(TextBuddy.COMMAND.EXIT, commandParser.getCommand());
    assertEquals("", commandParser.getParameters());
  }

  @Test
  public void testSortCommand() {
    commandParser.parseInput(SORT_STRING);
    assertEquals(TextBuddy.COMMAND.SORT, commandParser.getCommand());
    assertEquals("", commandParser.getParameters());
  }

  @Test
  public void testSearchCommand() {
    commandParser.parseInput(SEARCH_STRING);
    assertEquals(TextBuddy.COMMAND.SEARCH, commandParser.getCommand());
    assertEquals(SEARCH_PARAMETER, commandParser.getParameters());
    commandParser.parseInput(SEARCH_STRING_WITH_NO_ARGUMENT);
    assertEquals(TextBuddy.COMMAND.SEARCH, commandParser.getCommand());
    assertEquals("", commandParser.getParameters());
  }

  @Test
  public void testInvalidCommand() {
    commandParser.parseInput(INVALID_STRING);
    assertEquals(TextBuddy.COMMAND.INVALID, commandParser.getCommand());
    assertEquals("", commandParser.getParameters());
  }
}
