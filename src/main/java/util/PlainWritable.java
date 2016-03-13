package util;

public interface PlainWritable {

    PlainWritable readObject(String plainObject);

    String writeObject();

}
