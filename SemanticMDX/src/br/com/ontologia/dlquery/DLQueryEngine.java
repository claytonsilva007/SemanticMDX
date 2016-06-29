package br.com.ontologia.dlquery;

import java.util.Collections;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.ShortFormProvider;

public class DLQueryEngine {
	private final OWLReasoner reasoner;
	private final DLQueryParser parser;

	public DLQueryEngine(OWLReasoner reasoner, ShortFormProvider shortFormProvider) {
		this.reasoner = reasoner;
		OWLOntology rootOntology = reasoner.getRootOntology();
		parser = new DLQueryParser(rootOntology, shortFormProvider);
	}

	public Set<OWLClass> getSuperClasses(String classExpressionString, boolean direct) {
		if (classExpressionString.trim().length() == 0) {
			return Collections.emptySet();
		}
		OWLClassExpression classExpression = parser.parseClassExpression(classExpressionString);
		NodeSet<OWLClass> superClasses = reasoner.getSuperClasses(classExpression, direct);
		return superClasses.getFlattened();
	}

	public Set<OWLClass> getEquivalentClasses(String classExpressionString) {
		if (classExpressionString.trim().length() == 0) {
			return Collections.emptySet();
		}
		OWLClassExpression classExpression = parser.parseClassExpression(classExpressionString);
		Node<OWLClass> equivalentClasses = reasoner.getEquivalentClasses(classExpression);
		Set<OWLClass> result;
		if (classExpression.isAnonymous()) {
			result = equivalentClasses.getEntities();
		} else {
			result = equivalentClasses.getEntitiesMinus(classExpression.asOWLClass());
		}
		return result;
	}

	public Set<OWLClass> getSubClasses(String classExpressionString, boolean direct) {
		if (classExpressionString.trim().length() == 0) {
			return Collections.emptySet();
		}
		OWLClassExpression classExpression = parser.parseClassExpression(classExpressionString);
		NodeSet<OWLClass> subClasses = reasoner.getSubClasses(classExpression, direct);
		return subClasses.getFlattened();
	}

	public Set<OWLNamedIndividual> getInstances(String classExpressionString, boolean direct) {
		if (classExpressionString.trim().length() == 0) {
			return Collections.emptySet();
		}
		OWLClassExpression classExpression = ((DLQueryParser) parser).parseClassExpression(classExpressionString);
		
		reasoner.precomputeInferences();
		return reasoner.getInstances(classExpression, false).getFlattened();
	}
}
