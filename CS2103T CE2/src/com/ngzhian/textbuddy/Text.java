package com.ngzhian.textbuddy;

public class Text implements Comparable<Text> {
  String contents;

  public Text(String string) {
    contents = string;
  }

  @Override
  public int compareTo(Text o) {
    return this.toString().compareToIgnoreCase(o.toString());
  }

  @Override
  public String toString() {
    return contents;
  }

  public boolean contains(String string) {
    return contents.toLowerCase().contains(string.toLowerCase());
  }
}
