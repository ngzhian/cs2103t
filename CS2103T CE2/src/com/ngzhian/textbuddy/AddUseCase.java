package com.ngzhian.textbuddy;

public class AddUseCase extends Command {
  private TextBuddy textBuddy;
  private Text text;
  private static final String MSG_ADD = "added to %s: \"%s\"";
  private static final String MSG_ADD_TEXT_ERROR = "Error: add text fail. Please try again";

  public AddUseCase(TextBuddy textBuddy) {
    this.textBuddy = textBuddy;
  }

  public Text getText() {
    return text;
  }

  public void setText(String string) {
    setText(new Text(string));
  }

  public void setText(Text text) {
    this.text = text;
  }

  @Override
  public Result execute() {
    Result r = new Result();
    if (text == null) {
      r.isSuccess = false;
      r.errorMessage = MSG_ADD_TEXT_ERROR;
    } else {
      r.isSuccess = store.addText(parameters);
      return r.isSuccess ? String.format(MSG_ADD, file.getName(), parameters)
          : formatMsgToString(MSG_ADD_TEXT_ERROR);
    }
    return null;
  }
}
