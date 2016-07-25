package br.com.ontologia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import br.com.pojo.ClasseFeriado;

public interface SemanticHelperInterface {
	Set<OWLNamedIndividual> consultarInstanciasDeSuperClasse(String superClasse);
	String consultarDiasDaSemanaParteDeIntervalo(OWLNamedIndividual owlNamedIndividual);
	HashMap<String, String> consultarDiaMesAnoFeriado(Set<OWLNamedIndividual> instanciasDeSuperClasse);
	void criarInstanciasFeriados(ArrayList<ClasseFeriado> datasFeriados) throws OWLOntologyCreationException, OWLOntologyStorageException;
}
