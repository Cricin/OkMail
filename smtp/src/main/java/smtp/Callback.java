package smtp;

import java.io.IOException;

public interface Callback {

  void onSent(Session session, Response result);

  void onFailure(Session session, IOException e);

}