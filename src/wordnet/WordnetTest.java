package wordnet;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.dictionary.Dictionary;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class WordnetTest {

    public static void main(String[] args) {
        try {
            JWNL.initialize(new FileInputStream("wordnet-config.xml"));
        } catch (JWNLException | FileNotFoundException e) {
            e.printStackTrace();
        }
        final Dictionary dictionary = Dictionary.getInstance();

        try {
            IndexWord indexWord = dictionary.getIndexWord(POS.ADJECTIVE, "amazing");
            Synset[] senses = indexWord.getSenses();
            for (Synset set : senses) {
                for (Word word : set.getWords()) {
                    System.out.println(word);
                }
            }
        } catch (JWNLException e) {
            e.printStackTrace();
        }
    }
}
