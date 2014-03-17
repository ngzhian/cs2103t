package com.ngzhian.textbuddy;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TextStoreTest {
  TextStore store;
  TextStore persistentStore;
  private static final String TEXT_ONE = "String 1";
  private static final int TEXT_ONE_INDEX = 0;
  private static final String TEXT_TWO = "String 2";
  private static final int TEXT_TWO_INDEX = 1;
  private static final String WORD_TO_SEARCH_FOUND = "String";
  private static final String WORD_TO_SEARCH_NOT_FOUND = "Thread";
  private static String filename;

  @Before
  public void setup() {
    store = new TextStore();
    filename = generateNewFilename();
    try {
      persistentStore = new TextStore(new File(filename));
    } catch (IOException e) {

    }
  }

  @After
  public void teardown() {
    persistentStore.getFile().delete();
  }

  private String generateNewFilename() {
    return "" + String.valueOf(System.currentTimeMillis()) + ".txt";
  }

  @Test
  public void loadTextFromFile_fileThatDoesNotExist_createsNewFile() {
    try {
      int length = persistentStore.loadTextsFromFile();
      assertEquals(0, length);
    } catch (IOException e) {
    }
  }

  @Test
  public void loadTextFromFile_fileWith3Lines_returns3() {
    try {
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
          new FileOutputStream(persistentStore.getFile())));
      for (int i = 0; i < 3; i++) {
        bw.write("This is text " + i);
        bw.newLine();
      }
      bw.close();
      assertEquals(3, persistentStore.loadTextsFromFile());
    } catch (FileNotFoundException e) {
    } catch (IOException e) {
    }
  }

  @Test
  public void addTwoTexts_inSequence() throws IOException {
    store.clearTexts();
    assertEquals(true, store.addText(TEXT_ONE));
    assertEquals(true, store.addText(TEXT_TWO));
    assertEquals(TEXT_ONE, store.getText(TEXT_ONE_INDEX));
    assertEquals(TEXT_TWO, store.getText(TEXT_TWO_INDEX));
  }

  @Test
  public void addText_null_returnsFalse() throws IOException {
    store.clearTexts();
    assertEquals(false, store.addText(null));
  }

  @Test
  public void getAllTexts_fromEmptyStore_returnsArrayListOfSize0()
      throws IOException {
    store.clearTexts();
    assertEquals(0, store.getAllTexts().size());
  }

  @Test
  public void getAllTexts_fromStoreWith2Texts_returnsArrayListOfSize2()
      throws IOException {
    store.clearTexts();
    store.addText(TEXT_ONE);
    store.addText(TEXT_TWO);
    assertEquals(2, store.getAllTexts().size());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getText_withInvalidIndex_throwsIndexOutOfBoundsException() {
    store.getText(-1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void deleteText_withInvalidIndex_throwsIndexOutOfBoundsException()
      throws IOException {
    store.clearTexts();
    store.deleteText(0);
  }

  @Test
  public void deleteText_returnsDeletedText() throws IOException {
    store.clearTexts();
    store.addText(TEXT_ONE);
    store.addText(TEXT_TWO);
    assertEquals(TEXT_ONE, store.deleteText(0));
    assertEquals(TEXT_TWO, store.deleteText(0));
  }

  @Test
  public void clearText_onStoreWithItems_emptiesStore() throws IOException {
    store.addText(TEXT_ONE);
    store.addText(TEXT_TWO);
    store.clearTexts();
    assertEquals(0, store.getStore().size());
  }

  @Test
  public void sortText_sortsTextInAlphabeticaOrder() throws IOException {
    store.addText(TEXT_TWO);
    store.addText(TEXT_ONE);
    store.sortTexts();
    assertEquals(TEXT_ONE, store.getText(0));
    assertEquals(TEXT_TWO, store.getText(1));
  }

  @Test
  public void searchForWord_inEmptyStore_returnsListOfSize0()
      throws IOException {
    store.clearTexts();
    ArrayList<String> results = store.searchForWord(WORD_TO_SEARCH_FOUND);
    assertEquals(0, results.size());
  }

  @Test(expected = NullPointerException.class)
  public void searchForWord_thatIsNull_throwsNullPointerException()
      throws IOException {
    store.addText(TEXT_ONE);
    store.searchForWord(null);
  }

  @Test
  public void searchForWord_wordInStore_returnsListOfTextsWithWord()
      throws IOException {
    store.clearTexts();
    store.addText(TEXT_ONE);
    store.addText(TEXT_TWO);
    ArrayList<String> results = store.searchForWord(WORD_TO_SEARCH_FOUND);
    assertEquals(2, results.size());
    for (String result : results) {
      assertEquals(true, result.contains(WORD_TO_SEARCH_FOUND));
    }
  }

  @Test
  public void searchForWord_wordNotInStore_returnsEmptyList()
      throws IOException {
    store.clearTexts();
    store.addText(TEXT_ONE);
    store.addText(TEXT_TWO);
    ArrayList<String> results = store.searchForWord(WORD_TO_SEARCH_NOT_FOUND);
    assertEquals(0, results.size());
  }

}
