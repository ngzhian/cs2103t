package com.ngzhian.textbuddy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("serial")
public class TextList extends ArrayList<Text> implements List<Text> {
  public void sort() {
    Collections.sort(this);
  }

  public TextList search(String term) {
    TextList result = new TextList();
    for (Text text : this) {
      if (text.contains(term)) {
        result.add(text);
      }
    }
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof TextList) {
      TextList otherTextList = (TextList) o;
      this.sort();
      otherTextList.sort();
      return super.equals(otherTextList);
    }
    return false;
  }
}
