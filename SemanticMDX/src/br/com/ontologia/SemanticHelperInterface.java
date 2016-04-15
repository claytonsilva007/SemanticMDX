package br.com.ontologia;

import java.util.HashMap;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

public interface SemanticHelperInterface {
	Set<OWLNamedIndividual> consultarInstanciasDeSuperClasse(String superClasse);
	String consultarDiasDaSemanaParteDeIntervalo(OWLNamedIndividual owlNamedIndividual);
	HashMap<String, String> consultarDiaMesAnoFeriado(Set<OWLNamedIndividual> instanciasDeSuperClasse);
}
