package smtp.interceptor;


import okio.Buffer;
import org.junit.Test;
import smtp.mail.Encoding;
import smtp.mail.Headers;
import smtp.mail.Mailbox;
import smtp.misc.Utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class StmpHeaderTest {
  @Test
  public void testWriteWithLength() throws IOException {

    Buffer buffer = new Buffer();
    Headers headers = new Headers.Builder()
//        .add("subject", "wcF9YfW2FxIsEQQWpEGIRJZ6yKz39Xp3D8pFPoKlRP7v9+gYeVJ86IlPXD6zz" +
//            "Hsu4G62msM5ekpal080QreuhuDIVFr7BpwcOmTqhhIYBG4wAqAXUVFwT7m1fWQzgLZbC2Ol" +
//            "aohTo2Vs/iAPeaRRgJqmRKvlf5b1VT0UXg8u3spt0x3VBL3EmAtib4QzzrsjWI+6DcC4YZG" +
//            "5YwwBLKZMv93n3zuDnA4o/baeyYmq4dx/C8OlojCNfF3tpmS6XK+PyiDu5ZrIkwr4ZYUHRh" +
//            "Ky+CNmIn7Xe7ns1jenOqCRVYDiP+FlTQHoGb8oa1Dojkm4NuFEgx4EE4sHHfKwTEbtXgIvb" +
//            "Ey+pOMc")
        .add("content", "wcF9YfW2FxIsEQQWpEGIRJZ6yKz39Xp3D8pFPoKlRP7v9+gYeVJ86IlPXD6zz" +
            "Hsu4G62msM5ekpal080QreuhuDIVFr7BpwcOmTqhhIYBG4wAqAXUVFwT7m1fWQzgLZbC2Ol" +
            "aohTo2Vs/iAPeaRRgJqmRKvlf5b1VT0UXg8u3spt0x3VBL3EmAtib4QzzrsjWI+6DcC4YZG" +
            "5YwwBLKZMv93n3zuDnA4o/baeyYmq4dx/C8OlojCNfF3tpmS6XK+PyiDu5ZrIkwr4ZYUHRh" +
            "Ky+CNmIn7Xe7ns1jenOqCRVYDiP+FlTQHoGb8oa1Dojkm4NuFEgx4EE4sHHfKwTEbtXgIvb" +
            "Ey+pOMc")
        .build();

    TransferSpec spec = new TransferSpec.Builder().lengthLimit(20).charset(Utils.UTF8).encoding
        (Encoding.BASE64).build();

    SmtpHeader.writeAllHeaders(buffer, headers, spec);

    System.out.println(buffer.readUtf8());


  }

  @Test
  public void testWriteMailbox() throws IOException{
    Buffer buffer = new Buffer();

    TransferSpec spec = new TransferSpec.Builder().lengthLimit(20).charset(Utils.UTF8).encoding
        (Encoding.BASE64).build();


    final List<Mailbox> list = Arrays.asList(Mailbox.parse("cricin<cricin@126.com>"),
        Mailbox.parse("炒面<chaomian@qq.com>"));

    SmtpHeader.writeMailbox(buffer, spec, "To", list);

    System.out.println(buffer.readUtf8());
  }



}
