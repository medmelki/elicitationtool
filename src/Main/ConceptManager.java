package Main;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import nlp.extra.OpennlpHelper;
import owl.extra.OntologyHelper;

public class ConceptManager {

	public static Boolean isInitialized() {
		return OntologyHelper.owlModel != null;
	}

	public static void init(String owlDirectory) {
		OntologyHelper.init(owlDirectory);

	}

	public static List<String> getAllConcepts() {
		return OntologyHelper.findAllClasses();
	}

	public static Set<OWLNamedClass> extractConcepts(String sentence) {

		// Sentence Tokenizing
		String sentences[] = OpennlpHelper.splitToSentences(sentence);

		// Part of Speech Extraction
		String posSummary = OpennlpHelper.getPosTagsPretty(sentence);
		System.out.println(posSummary);

		Set<String> targetedTags = OpennlpHelper.getNounTags();
		targetedTags.addAll(OpennlpHelper.getVerbTags());
		List<String> allVerbsNouns = OpennlpHelper.filterByTag(sentence, targetedTags);

		System.out.println("Extracted Noun and Verbs:  " + allVerbsNouns.toString());

		Set<OWLNamedClass> matchedConecpets = new HashSet<OWLNamedClass>();
		allVerbsNouns.forEach(item -> {
			OWLNamedClass concept = OntologyHelper.findByCustomEqualFunction(item, OpennlpHelper::equaIsgnoreForm);
			if (concept != null)
				matchedConecpets.add(concept);
		});

		// = OntologyHelper.findByCustomEqualFunction("Buckets",
		// OpennlpHelper::equaIsgnoreForm);
		// System.out.println(conceptToFind + " is matched with : "
		// + OntologyHelper.findByCustomEqualFunction("Buckets",
		// OpennlpHelper::equaIsgnoreForm).getBrowserText());
		return matchedConecpets;

	}

}
