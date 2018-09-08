package owl.extra;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

public class OntologyHelper {
	public static JenaOWLModel owlModel = null;

	// public static String owlDirectory = "resources/bucket.owl";
	// Initialize the helper
	public static void init(String ontologyURI) {
		File file = new File(ontologyURI);
		try {
			owlModel = ProtegeOWL.createJenaOWLModelFromURI(file.toURI().toString());
		} catch (OntologyLoadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Search for a Class by name
	public static OWLNamedClass findByClassName(String name) {
		OWLNamedClass destinationClass = null;
		destinationClass = owlModel.getOWLNamedClass(name);

		return destinationClass;

	}

	public static List<String> findAllClasses() {
//		if(Concep)
		Collection classes = owlModel.getUserDefinedOWLNamedClasses();
		List<String> result = new ArrayList<String>();
		for (Iterator it = classes.iterator(); it.hasNext();) {
			OWLNamedClass cls = (OWLNamedClass) it.next();
			result.add(cls.getBrowserText());
		}
		return result;
	}

	public static OWLNamedClass findByCustomEqualFunction(String name, IEqual customEqual) {
		Collection classes = owlModel.getUserDefinedOWLNamedClasses();
		for (Iterator it = classes.iterator(); it.hasNext();) {
			OWLNamedClass cls = (OWLNamedClass) it.next();
			if (customEqual.customEquals(name, cls.getBrowserText())) {
				return cls;
			}

			// summary += "Class " + cls.getBrowserText() + " (" + instances.size() + ")
			// \n";
		}
		return null;

	}

	// whether a concept is exist in the ontology or not
	public static boolean isConceptExist(String name) {
		OWLNamedClass targetConcept = findByClassName(name);
		return targetConcept != null;
	}

	// Print the summary of the ontology
	public static String getSummary() {
		String summary = "";
		Collection classes = owlModel.getUserDefinedOWLNamedClasses();
		for (Iterator it = classes.iterator(); it.hasNext();) {
			OWLNamedClass cls = (OWLNamedClass) it.next();
			Collection instances = cls.getInstances(false);
			summary += "Class " + cls.getBrowserText() + " (" + instances.size() + ") \n";
			for (Iterator jt = instances.iterator(); jt.hasNext();) {
				OWLIndividual individual = (OWLIndividual) jt.next();
				summary += " - " + individual.getBrowserText() + " \n";
			}
		}
		System.out.println(summary);
		return summary;

	}

}
