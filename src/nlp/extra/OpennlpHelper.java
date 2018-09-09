package nlp.extra;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.dictionary.Dictionary;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import wordnet.WordnetLoader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OpennlpHelper {
    public static final String POS_NN = "NN"; // Noun, singular or mass
    public static final String POS_NNS = "NNS"; // Noun, singular or mass
    public static final String POS_NNP = "NNP"; // Proper noun, singular
    public static final String POS_NNPS = "NNPS"; // Proper noun, plural

    public static final String POS_VB = "VB"; // Verb, base form
    public static final String POS_VBD = "VBD"; // Verb, past tens
    public static final String POS_VBG = "VBG"; // Verb, gerund or present participle
    public static final String POS_VBN = "VBN"; // Verb, past participle
    public static final String POS_VBP = "VBP"; // Verb, non-3rd person singular present
    public static final String POS_VBZ = "VBZ"; // Verb, third person singular present

    public static final String POS_DT = "DT"; // Determiner
    public static final String POS_IN = "IN"; // Preposition or subordinating conjunction
    public static final String POS_TO = "TO"; // to
    public static final String POS_JJ = "JJ"; // Adjective

    private static SentenceModel sentenceModel = null;
    private static POSModel posModel = null;

    public static Set<String> getNounTags() {
        Set<String> nounTagSet = new HashSet<String>();

        nounTagSet.add(POS_NN);
        nounTagSet.add(POS_NNS);
        nounTagSet.add(POS_NNP);
        nounTagSet.add(POS_NNPS);
        return nounTagSet;
    }

    public static Set<String> getVerbTags() {
        Set<String> verbTagSet = new HashSet<String>();

        verbTagSet.add(POS_VB);
        verbTagSet.add(POS_VBD);
        verbTagSet.add(POS_VBG);
        verbTagSet.add(POS_VBN);
        verbTagSet.add(POS_VBP);
        verbTagSet.add(POS_VBZ);
        return verbTagSet;
    }

    public static String stem(String word) {
        PorterStemmer stemmer = new PorterStemmer();
        return stemmer.stem(word);

    }

    public static boolean equalOrSynonymStems(String firstWord, String secondWord) {
        String firstStem = stem(firstWord.toLowerCase());
        String secondStem = stem(secondWord.toLowerCase());
        try {
            Dictionary dictionary = WordnetLoader.getDictionary();
            IndexWord indexVerb = dictionary.getIndexWord(POS.VERB, secondWord);
            IndexWord indexNoun = dictionary.getIndexWord(POS.NOUN, secondWord);

            List<Synset> sensesList = new ArrayList<>();
            if (indexVerb != null) {
                Synset[] verbSenses = indexVerb.getSenses();
                sensesList.addAll(Arrays.asList(verbSenses));
            }
            if (indexNoun != null) {
                Synset[] nounSenses = indexNoun.getSenses();
                sensesList.addAll(Arrays.asList(nounSenses));
            }

            for (Synset set : sensesList) {
                for (Word word : set.getWords()) {
                    if (stem(word.getLemma()).equalsIgnoreCase(firstStem))
                        return true;
                }
            }
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        return firstStem.equalsIgnoreCase(secondStem);
    }

    public static String[] splitToSentences(String sentence) {
        SentenceDetectorME detector = new SentenceDetectorME(getSentenceModel());
        String sentences[] = detector.sentDetect(sentence);
        return sentences;
    }

    public static String[] splitToTokens(String sentence) {
        WhitespaceTokenizer tokenizer = WhitespaceTokenizer.INSTANCE;
        String tokens[] = tokenizer.tokenize(sentence);
        return tokens;
    }

    public static String getPosTagsPretty(String sentence) {
        return getPosTags(sentence).toString();
    }

    public static POSSample getPosTags(String sentence) {
        POSTaggerME tagger = new POSTaggerME(getPosModel());

        String[] tokens = splitToTokens(sentence);
        String[] tags = tagger.tag(tokens);
        POSSample sample = new POSSample(tokens, tags);
        return sample;
    }

    public static List<String> filterByTag(String sentence, Set<String> targetedTags) {
        ArrayList<String> filteredTags = new ArrayList<>();
        POSSample sample = getPosTags(sentence);
        for (int i = 0; i < sample.getTags().length; i++) {
            if (targetedTags.contains(sample.getTags()[i])) {
                filteredTags.add(sample.getSentence()[i]);
            }
        }

        return filteredTags;
    }

    private static SentenceModel getSentenceModel() {
        if (sentenceModel == null) {
            try {
                InputStream inputStream = new FileInputStream("models/en-sent.bin");
                sentenceModel = new SentenceModel(inputStream);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return sentenceModel;
    }

    private static POSModel getPosModel() {

        if (posModel == null) {
            InputStream inputStream;
            try {
                inputStream = new FileInputStream("models/en-pos-maxent.bin");
                posModel = new POSModel(inputStream);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return posModel;
    }

}
