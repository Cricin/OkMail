package smtp;

import java.io.IOException;

/**
 * callback interface for async mailing purpose,
 * implementing this when you want be notified when
 * mail mailing finished or failed.
 */
public interface Callback {

  void onSent(Session session, Response result);

  void onFailure(Session session, IOException e);

}