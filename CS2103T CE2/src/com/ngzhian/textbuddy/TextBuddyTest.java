package com.ngzhian.textbuddy;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TextBuddyTest {
  private static final String MSG_ADD = "added to %s: \"%s\"";
  private static final String MSG_CLEAR = "all content deleted from %s";
  private static final String MSG_EMPTY_FILE = "%s is empty";
  private static final String MSG_ADD_TEXT_ERROR = "Error: add text fail. Please try again";
  private static final String MSG_WRONG_NUMBER_FORMAT = "\"%s\" is not a number";
  private static final String MSG_BAD_INDEX = "%d is not a valid index. Valid range is %d to %d, inclusive";
  private static final String MSG_DELETE_EMPTY_TEXTS = "you have no text to delete!";
  private static final String MSG_NO_SEARCH_TERM_GIVEN = "no search term was given!";
  private static final String MSG_NO_SEARCH_RESULTS_FOUND = "no results found for: %s";
  private static final String MSG_EXIT = "Exiting...";
  private static final String DISPLAY_LINE = "%d. %s";
  private static final String DUMMY_TEXT = "hello world!";
  private static final String FILE_NAME = "file_under_test.txt";
  private static final String FIRST_TEXT = "text 1";
  private static final String SECOND_TEXT = "text 2";
  private static final String SEARCH_TERM_WITH_ONE_RESULT = "1";
  private static final String SEARCH_TERM_WITH_MULTIPLE_RESULTS = "text";
  private static final String SEARCH_TERM_WITH_NO_RESULT = "NOOOO";
  private static final String NEWLINE = System.lineSeparator();

  TextBuddy tb;

  @Before
  public void setup() {
    tb = new TextBuddy(FILE_NAME);
  }

  @After
  public void teardown() {
    tb.getFile().delete();
  }

  @Test
  public void execute_nullCommand_returnsEmptyString() {
    assertEquals("", tb.execute(null, null));
    assertEquals("", tb.execute(null, ""));
    assertEquals("", tb.execute(null, "null"));
  }

  @Test
  public void execute_addCommandWithNullParameters_returnsMSG_ADD_TEXT_ERROR() {
    assertEquals(String.format(MSG_ADD_TEXT_ERROR),
        tb.execute(TextBuddy.COMMAND.ADD, null));
  }

  @Test
  public void execute_addCommandWithString_returnsMessage() {
    String expected = String
        .format(MSG_ADD, tb.getFile().getName(), DUMMY_TEXT);
    String actual = tb.execute(TextBuddy.COMMAND.ADD, DUMMY_TEXT);
    assertEquals(expected, actual);
  }

  @Test
  public void execute_displayCommandWhenFileIsEmpty_returnsMSG_EMPTY_FILE() {
    assertEquals(String.format(MSG_EMPTY_FILE, tb.getFile().getName()),
        tb.execute(TextBuddy.COMMAND.DISPLAY, null));
  }

  @Test
  public void execute_displayCommandWhenFileHasEntries_returnsEntries() {
    StringBuilder sb = new StringBuilder();
    tb.execute(TextBuddy.COMMAND.ADD, DUMMY_TEXT);
    tb.execute(TextBuddy.COMMAND.ADD, DUMMY_TEXT);
    sb.append(String.format(DISPLAY_LINE, 1, DUMMY_TEXT));
    sb.append(NEWLINE);
    sb.append(String.format(DISPLAY_LINE, 2, DUMMY_TEXT));
    sb.append(NEWLINE);
    String expected = sb.toString();
    String actual = tb.execute(TextBuddy.COMMAND.DISPLAY, null);
    assertEquals(expected, actual);
  }

  @Test
  public void execute_deleteCommandEmptyStore_returnsMSG_DELETE_EMPTY_TEXTS() {
    String expected = String.format(MSG_DELETE_EMPTY_TEXTS);
    String result = tb.execute(TextBuddy.COMMAND.DELETE, "1");
    assertEquals(expected, result);
  }

  @Test
  public void execute_deleteCommandIndexOutOfBounds_returnsMSG_BAD_INDEX() {
    tb.execute(TextBuddy.COMMAND.ADD, DUMMY_TEXT);
    String BAD_INDEX = "-1";
    String expected = String.format(MSG_BAD_INDEX, -1 - 1, 1, tb.getSize());
    String actual = tb.execute(TextBuddy.COMMAND.DELETE, BAD_INDEX);
    assertEquals(expected, actual);
    BAD_INDEX = "2";
    expected = String.format(MSG_BAD_INDEX, 2 - 1, 1, tb.getSize());
    actual = tb.execute(TextBuddy.COMMAND.DELETE, BAD_INDEX);
    assertEquals(expected, actual);
    BAD_INDEX = "100";
    expected = String.format(MSG_BAD_INDEX, 100 - 1, 1, tb.getSize());
    actual = tb.execute(TextBuddy.COMMAND.DELETE, BAD_INDEX);
    assertEquals(expected, actual);
  }

  @Test
  public void execute_deleteCommandBadNumberFormat_returnsMSG_WRONG_NUMBER_FORMAT() {
    tb.execute(TextBuddy.COMMAND.ADD, DUMMY_TEXT);
    String BAD_NUMBER, expected, actual;
    BAD_NUMBER = "";
    expected = String.format(MSG_WRONG_NUMBER_FORMAT, BAD_NUMBER);
    actual = tb.execute(TextBuddy.COMMAND.DELETE, BAD_NUMBER);
    assertEquals(expected, actual);
    BAD_NUMBER = "ABC";
    expected = String.format(MSG_WRONG_NUMBER_FORMAT, BAD_NUMBER);
    actual = tb.execute(TextBuddy.COMMAND.DELETE, BAD_NUMBER);
    assertEquals(expected, actual);
  }

  @Test
  public void execute_clearCommand() throws Exception {
    String expected = String.format(MSG_CLEAR, tb.getFile().getName());
    String actual = tb.execute(TextBuddy.COMMAND.CLEAR, null);
    assertEquals(expected, actual);
  }

  @Test
  public void execute_sortCommandWithEmptyStore_returnsMSG_EMPTY_FILE()
      throws Exception {
    String expected;
    String actual;
    expected = String.format(MSG_EMPTY_FILE, tb.getFile().getName());
    actual = tb.execute(TextBuddy.COMMAND.SORT, null);
    assertEquals(expected, actual);

    execute_sortCommandWithSortedTexts_returnsSortedTexts();

  }

  @Test
  public void execute_sortCommandWithSortedTexts_returnsSortedTexts() {
    String expected;
    String actual;
    tb.execute(TextBuddy.COMMAND.ADD, FIRST_TEXT);
    tb.execute(TextBuddy.COMMAND.ADD, SECOND_TEXT);
    StringBuilder sb = new StringBuilder();
    sb.append(String.format(DISPLAY_LINE, 1, FIRST_TEXT));
    sb.append(NEWLINE);
    sb.append(String.format(DISPLAY_LINE, 2, SECOND_TEXT));
    sb.append(NEWLINE);
    expected = sb.toString();
    actual = tb.execute(TextBuddy.COMMAND.SORT, null);
    assertEquals(expected, actual);
  }

  @Test
  public void execute_sortCommandWithUnsortedTexts_returnsSortedTexts() {
    String expected;
    String actual;
    tb.execute(TextBuddy.COMMAND.ADD, SECOND_TEXT);
    tb.execute(TextBuddy.COMMAND.ADD, FIRST_TEXT);
    StringBuilder sb = new StringBuilder();
    sb.append(String.format(DISPLAY_LINE, 1, FIRST_TEXT));
    sb.append(NEWLINE);
    sb.append(String.format(DISPLAY_LINE, 2, SECOND_TEXT));
    sb.append(NEWLINE);
    expected = sb.toString();
    actual = tb.execute(TextBuddy.COMMAND.SORT, null);
    assertEquals(expected, actual);
  }

  @Test
  public void execute_searchCommandWithNoSearchTerm_returnsMSG_NO_SEARCH_TERM_GIVEN()
      throws Exception {
    String expected = String.format(MSG_NO_SEARCH_TERM_GIVEN);
    String actual = tb.execute(TextBuddy.COMMAND.SEARCH, "");
    assertEquals(expected, actual);
  }

  @Test
  public void execute_searchCommandWithSearchTerm_returnsOneResult()
      throws Exception {
    tb.execute(TextBuddy.COMMAND.ADD, SECOND_TEXT);
    tb.execute(TextBuddy.COMMAND.ADD, FIRST_TEXT);
    StringBuilder sb = new StringBuilder();
    sb.append(String.format(DISPLAY_LINE, 1, FIRST_TEXT));
    sb.append(NEWLINE);
    String expected = sb.toString();
    String actual = tb.execute(TextBuddy.COMMAND.SEARCH,
        SEARCH_TERM_WITH_ONE_RESULT);
    assertEquals(expected, actual);
  }

  @Test
  public void execute_searchCommandWithSearchTerm_returnsManyResult() {
    {
      tb.execute(TextBuddy.COMMAND.ADD, SECOND_TEXT);
      tb.execute(TextBuddy.COMMAND.ADD, FIRST_TEXT);
      StringBuilder sb = new StringBuilder();
      sb.append(String.format(DISPLAY_LINE, 1, SECOND_TEXT));
      sb.append(NEWLINE);
      sb.append(String.format(DISPLAY_LINE, 2, FIRST_TEXT));
      sb.append(NEWLINE);
      String expected = sb.toString();
      String actual = tb.execute(TextBuddy.COMMAND.SEARCH,
          SEARCH_TERM_WITH_MULTIPLE_RESULTS);
      assertEquals(expected, actual);
    }
  }

  @Test
  public void execute_searchCommandWithSearchTerm_returnsNoResult()
      throws Exception {
    tb.execute(TextBuddy.COMMAND.ADD, SECOND_TEXT);
    tb.execute(TextBuddy.COMMAND.ADD, FIRST_TEXT);
    String expected = String.format(MSG_NO_SEARCH_RESULTS_FOUND,
        SEARCH_TERM_WITH_NO_RESULT);
    String actual = tb.execute(TextBuddy.COMMAND.SEARCH,
        SEARCH_TERM_WITH_NO_RESULT);
    assertEquals(expected, actual);
  }

  @Test
  public void execute_exitCommand_returnsMSG_EXIT() throws Exception {
    String expected = MSG_EXIT;
    String actual = tb.execute(TextBuddy.COMMAND.EXIT, null);
    assertEquals(expected, actual);
  }
}
