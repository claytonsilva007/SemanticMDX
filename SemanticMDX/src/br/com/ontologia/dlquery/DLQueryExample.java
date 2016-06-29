package br.com.ontologia.dlquery;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Set;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

public class DLQueryExample {
	@SuppressWarnings("javadoc")
	public static void main(String args[]) {
		try {
			File file = new File("C:\\Users\\clayton\\Desktop\\testandoTopObjectProperty.owl");
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLOntology ontology = manager.loadOntologyFromOntologyDocument(IRI.create(file));
			System.out.println("Loaded ontology: " + ontology.getOntologyID());

			OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
			OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, new SimpleConfiguration());

			ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
			
			DLQueryEngine dlQueryEngine = new DLQueryEngine(reasoner, shortFormProvider);
			
			Set<OWLNamedIndividual> instances = dlQueryEngine.getInstances("temDiaFeriado some Domingo", true);
			
			for (OWLNamedIndividual owlNamedIndividual : instances) {
				System.out.println(owlNamedIndividual.toString());
			}

			//DLQueryPrinter dlQueryPrinter = new DLQueryPrinter(new DLQueryEngine(reasoner, shortFormProvider), shortFormProvider);

			//doQueryLoop(dlQueryPrinter);
		} catch (OWLOntologyCreationException e) {
			System.out.println("Could not load ontology: " + e.getMessage());
		} /*catch (IOException ioEx) {
			System.out.println(ioEx.getMessage());
		}*/
	}

	private static void doQueryLoop(DLQueryPrinter dlQueryPrinter) throws IOException {
		while (true) {
			System.out.println("Please type a class expression in Manchester Syntax and press Enter (or press x to exit):");
			System.out.println("");
			String classExpression = readInput();
			if (classExpression.equalsIgnoreCase("x")) {
				break;
			}
			dlQueryPrinter.askQuery(classExpression.trim());
			System.out.println();
		}
	}

	private static String readInput() throws IOException {
		InputStream is = System.in;
		InputStreamReader reader;
		reader = new InputStreamReader(is, "UTF-8");
		BufferedReader br = new BufferedReader(reader);
		return br.readLine();
	}

	private static OWLReasoner createReasoner(final OWLOntology rootOntology) {
		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		return reasonerFactory.createReasoner(rootOntology);
	}
}