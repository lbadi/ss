package util;

import java.io.File;

/**
 * Created by leonelbadi on 11/3/16.
 */
public interface PlainWritable {

    PlainWritable readObject(String plainObject);
    String writeObject();
}
