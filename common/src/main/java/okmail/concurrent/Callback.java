package okmail.concurrent;

public interface Callback<Tag, Result, Wrong extends Throwable> {

  void onSuccess(Tag tag, Result result);

  void onFailure(Tag tag, Wrong wrong);

}