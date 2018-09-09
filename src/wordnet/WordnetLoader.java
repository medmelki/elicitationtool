package wordnet;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.dictionary.Dictionary;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class WordnetLoader {

    public static Dictionary dictionary;

    static {
        try {
            JWNL.initialize(new FileInputStream("wordnet-config.xml"));
        } catch (JWNLException | FileNotFoundException e) {
            e.printStackTrace();
        }
        dictionary = Dictionary.getInstance();
    }

    public static Dictionary getDictionary() {
        return dictionary;
    }
}
