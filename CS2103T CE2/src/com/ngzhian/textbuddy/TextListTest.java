package com.ngzhian.textbuddy;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TextListTest {
  private static final String FOUND_SEARCH_TERM = "1";
  private static final String FOUND_SEARCH_TERM_TEST = "TEST";
  private static final String NOT_FOUND_SEARCH_TERM = "NOT FOUND";
  TextList textList;
  Text text1;
  Text text2;
  Text text3;

  @Before
  public void setup() {
    textList = new TextList();
    text1 = new Text("Test text 1");
    text2 = new Text("TEST text 2");
    text3 = new Text("teST text 3");
  }

  @Test
  public void sort_sortedTextList_returnsSameTextList() throws Exception {
    textList.add(text1);
    textList.add(text2);
    TextList original = (TextList) textList.clone();
    textList.sort();
    assertEquals(original, textList);
  }

  @Test
  public void sort_unsortedTextList_returnsSortedTextList() throws Exception {
    textList.add(text1);
    textList.add(text2);
    textList.add(text3);
    TextList sorted = (TextList) textList.clone();
    textList.clear();
    textList.add(text2);
    textList.add(text3);
    textList.add(text1);
    textList.sort();
    assertEquals(sorted, textList);
  }

  @Test
  public void search_emptySearchTerm_returnsEntireTextList() {
    TextList result = textList.search("");
    assertEquals(textList, result);
  }

  @Test
  public void search_foundTerm_returnsTextListWithSearchTerm() {
    TextList expected = new TextList();
    TextList result;
    expected.add(text1);

    textList.add(text1);
    textList.add(text2);
    textList.add(text3);
    result = textList.search(FOUND_SEARCH_TERM);
    assertEquals(expected, result);

    expected.add(text3);
    expected.add(text2);
    result = textList.search(FOUND_SEARCH_TERM_TEST);
    assertEquals(expected, result);
  }

  public void search_notFoundTerm_returnsEmptyTextList() {
    TextList expected = new TextList();

    textList.add(text1);
    textList.add(text2);
    textList.add(text3);
    TextList result = textList.search(NOT_FOUND_SEARCH_TERM);
    assertEquals(expected, result);
  }
}
