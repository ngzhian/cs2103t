package com.ngzhian.textbuddy;

public interface UI {
  public String getUserInput();

  public void giveFeedback(String output);

  public Command parseToCommand();
}
