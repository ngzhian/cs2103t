package com.ngzhian.textbuddy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;

public class TextStore {
  private File file;
  private ArrayList<String> store;

  public TextStore() {
    this.file = null;
    store = new ArrayList<String>();
  }

  public TextStore(File file) throws IOException {
    this.file = file;
    store = new ArrayList<String>();
    loadTextsFromFile();
  }

  public File getFile() {
    return file;
  }

  public ArrayList<String> getStore() {
    return store;
  }

  public void clearTexts() throws IOException {
    store.clear();
    flushTextsToFile();
  }

  public boolean addText(String text) throws IOException {
    if (text == null) {
      return false;
    }
    boolean result = store.add(text);
    flushTextsToFile();
    return result;
  }

  public String getText(int i) {
    return store.get(i);
  }

  public ArrayList<String> getAllTexts() {
    return store;
  }

  public String deleteText(int index) throws IndexOutOfBoundsException,
      IOException {
    String result = store.remove(index);
    flushTextsToFile();
    return result;
  }

  public void sortTexts() throws IOException {
    Collections.sort(store);
    flushTextsToFile();
  }

  public ArrayList<String> searchForWord(String word) {
    ArrayList<String> results = new ArrayList<String>();
    for (String text : store) {
      if (text.contains(word)) {
        results.add(text);
      }
    }
    return results;
  }

  public int getSize() {
    return store.size();
  }

  public boolean isEmpty() {
    return store.isEmpty();
  }

  public int loadTextsFromFile() throws IOException {
    if (!file.exists()) {
      file.createNewFile();
    } else {
      BufferedReader br = new BufferedReader(new InputStreamReader(
          new FileInputStream(this.file)));
      String line;
      while ((line = br.readLine()) != null) {
        this.addText(line);
      }
      br.close();
    }
    return this.getSize();
  }

  public int flushTextsToFile() throws IOException {
    if (file == null) {
      return this.getSize();
    }
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(file)));
    for (String s : this.getAllTexts()) {
      bw.write(s);
      bw.newLine();
    }
    bw.close();
    return this.getSize();
  }
}
