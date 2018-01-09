package okmail.web.security;

import java.io.File;
import java.io.InputStream;

public interface FileSystem {

  File file(String path);

  InputStream inputStream();


}