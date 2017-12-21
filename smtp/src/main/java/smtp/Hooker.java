package smtp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * a {@link Hooker} hooker provided some facilities to hook OkMail smtp
 * working flow. allow you inject some specific object to client, for simply
 * reason, just extends {@link smtp.internal.hook.HookerAdapter} to override
 * the method you interest with
 */
public interface Hooker {

  <T> T hookInstantiate(T target);

  Socket socketOpened(Socket socket);

  InputStream inputStreamOpened(InputStream in);

  OutputStream outputStreamOpened(OutputStream out);

}