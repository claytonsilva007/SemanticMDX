package br.com.ontologia;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLPropertyAssertionAxiom;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

import br.com.util.Configuracoes;

public class SemanticHelper implements SemanticHelperInterface{

	private static final File file = new File("C:\\Users\\clayton\\Desktop\\testandoTopObjectProperty.owl");
	private static OWLOntology OWL_ONTOLOGY = null;

	public SemanticHelper() {
		OWL_ONTOLOGY = this.carregarOntologia();
	}

	public OWLOntology carregarOntologia(){
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = null;
		try {
			ontology = manager.loadOntologyFromOntologyDocument(IRI.create(file));
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		return ontology;
	}

	public Set<OWLNamedIndividual> consultarInstanciasDeSuperClasse(String superClasse) {

		OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
		OWLReasoner reasoner = reasonerFactory.createReasoner(OWL_ONTOLOGY, new SimpleConfiguration());

		OWLClass owlClass = 
				OWL_ONTOLOGY
				.getOWLOntologyManager()
				.getOWLDataFactory()
				.getOWLClass(
						IRI.create(Configuracoes.getIriBase() + Configuracoes.SEPARADOR_ONTOLOGICO + superClasse));

		reasoner.precomputeInferences();
		Set<OWLNamedIndividual> individuos = reasoner.getInstances(owlClass, false).getFlattened();

		return individuos;
	}

	@Override
	public HashMap<String, String> consultarDiaMesAnoFeriado(Set<OWLNamedIndividual> setOWLNamedIndividuals) {
		HashMap<String, String> hashMap = new HashMap<>();
		OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
		OWLReasoner reasoner = reasonerFactory.createReasoner(OWL_ONTOLOGY, new SimpleConfiguration());

		reasoner.precomputeInferences();

		for (OWLNamedIndividual owlNamedIndividual : setOWLNamedIndividuals) {
			hashMap.put("Dia", this.consultarDiaOcorrenciaFeriado(owlNamedIndividual));
			hashMap.put("Mes", this.consultarMesOcorrenciaFeriado(owlNamedIndividual));
			hashMap.put("Ano", this.consultarAnoOcorrenciaFeriado(owlNamedIndividual));
		}
		return hashMap;
	}

	public String consultarDiaOcorrenciaFeriado(OWLNamedIndividual owlNamedIndividual){
		String retorno = "";
		OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
		OWLReasoner reasoner = reasonerFactory.createReasoner(OWL_ONTOLOGY, new SimpleConfiguration());

		OWLDataProperty OwlDataPropertyTemDia = 
				OWL_ONTOLOGY
				.getOWLOntologyManager()
				.getOWLDataFactory()
				.getOWLDataProperty(IRI.create(Configuracoes.getIriBase() + Configuracoes.SEPARADOR_ONTOLOGICO + "temDia"));

		reasoner.precomputeInferences();

		Set<OWLLiteral> valueProperty	= reasoner.getDataPropertyValues(owlNamedIndividual, OwlDataPropertyTemDia);
		for (OWLLiteral owlLiteral : valueProperty) {
			retorno = owlLiteral.toString();
		}
		return retorno;
	}

	public String consultarMesOcorrenciaFeriado(OWLNamedIndividual owlNamedIndividual){
		String retorno = "";
		OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
		OWLReasoner reasoner = reasonerFactory.createReasoner(OWL_ONTOLOGY, new SimpleConfiguration());

		OWLDataProperty OwlDataPropertyTemMes = 
				OWL_ONTOLOGY
				.getOWLOntologyManager()
				.getOWLDataFactory()
				.getOWLDataProperty(IRI.create(Configuracoes.getIriBase() + Configuracoes.SEPARADOR_ONTOLOGICO + "temMes"));

		reasoner.precomputeInferences();

		Set<OWLLiteral> valueProperty	= reasoner.getDataPropertyValues(owlNamedIndividual, OwlDataPropertyTemMes);
		for (OWLLiteral owlLiteral : valueProperty) {
			retorno = owlLiteral.toString();
		}
		return retorno;
	}

	public String consultarAnoOcorrenciaFeriado(OWLNamedIndividual owlNamedIndividual){
		String retorno = "";
		OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
		OWLReasoner reasoner = reasonerFactory.createReasoner(OWL_ONTOLOGY, new SimpleConfiguration());

		OWLDataProperty OwlDataPropertyTemAno = 
				OWL_ONTOLOGY
				.getOWLOntologyManager()
				.getOWLDataFactory()
				.getOWLDataProperty(IRI.create(Configuracoes.getIriBase() + Configuracoes.SEPARADOR_ONTOLOGICO + "temAno"));
		
		reasoner.precomputeInferences();

		Set<OWLLiteral> valueProperty = reasoner.getDataPropertyValues(owlNamedIndividual, OwlDataPropertyTemAno);
		for (OWLLiteral owlLiteral : valueProperty) {
			retorno = owlLiteral.toString();
		}
		return retorno;
	}

	@Override
	public String consultarDiasDaSemanaParteDeIntervalo(OWLNamedIndividual owlNamedIndividual) {
		String diasParteDeIntervalo = "";

		OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
		OWLReasoner reasoner = reasonerFactory.createReasoner(OWL_ONTOLOGY, new SimpleConfiguration());
		reasoner.precomputeInferences();

		OWLDataFactory df = this.OWL_ONTOLOGY.getOWLOntologyManager().getOWLDataFactory();

		OWLObjectProperty temDiaFeriado = df.getOWLObjectProperty(IRI.create(Configuracoes.getIriBase() + "#temDiaFeriado"));

		NodeSet<OWLNamedIndividual> valuesNodeSet = reasoner.getObjectPropertyValues(owlNamedIndividual, temDiaFeriado);
		Set<OWLNamedIndividual> values = valuesNodeSet.getFlattened();

		for(OWLNamedIndividual ind : values) {
			if("".equals(diasParteDeIntervalo)){
				diasParteDeIntervalo =  ind.toString();
			} else{
				diasParteDeIntervalo = diasParteDeIntervalo + Configuracoes.SEPARADOR + ind.toString();
			}
		}
		return diasParteDeIntervalo;
	}
	
	public String consultarInicioFimIntervalo(String superClasse){
		String inicioFimFeriado = "";
		
		Set<OWLNamedIndividual> individuos = this.consultarInstanciasDeSuperClasse(superClasse);
		
		for (OWLNamedIndividual owlNamedIndividual : individuos) {
			inicioFimFeriado = this.consultarDiaInicioIntervalo(owlNamedIndividual) + Configuracoes.SEPARADOR + this.consultarDiaFimIntervalo(owlNamedIndividual);
		}
		
		return inicioFimFeriado;
		
	}

	public String consultarDiaInicioIntervalo(OWLNamedIndividual owlNamedIndividual){
		String diaInicioFeriado = "";
		OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
		OWLReasoner reasoner = reasonerFactory.createReasoner(OWL_ONTOLOGY, new SimpleConfiguration());

		reasoner.precomputeInferences();

		OWLDataFactory df = OWL_ONTOLOGY.getOWLOntologyManager().getOWLDataFactory();
		OWLObjectProperty temDiaInicio = df.getOWLObjectProperty(IRI.create(Configuracoes.getIriBase() + Configuracoes.SEPARADOR_ONTOLOGICO + "temDiaInicio"));

		NodeSet<OWLNamedIndividual> valuesNodeSet = reasoner.getObjectPropertyValues(owlNamedIndividual, temDiaInicio);
		Set<OWLNamedIndividual> values = valuesNodeSet.getFlattened();
		
		for(OWLNamedIndividual ind : values) {
			if("".equals(diaInicioFeriado)){
				diaInicioFeriado =  ind.toString();
			} 
		}
		return diaInicioFeriado;
	}
	
	public String consultarDiaFimIntervalo(OWLNamedIndividual owlNamedIndividual){
		String diaFimFeriado = "";
		OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
		OWLReasoner reasoner = reasonerFactory.createReasoner(OWL_ONTOLOGY, new SimpleConfiguration());

		reasoner.precomputeInferences();

		OWLDataFactory df = OWL_ONTOLOGY.getOWLOntologyManager().getOWLDataFactory();
		OWLObjectProperty temDiaFim = df.getOWLObjectProperty(IRI.create(Configuracoes.getIriBase() + Configuracoes.SEPARADOR_ONTOLOGICO + "temDiaFim"));

		NodeSet<OWLNamedIndividual> valuesNodeSet = reasoner.getObjectPropertyValues(owlNamedIndividual, temDiaFim);
		Set<OWLNamedIndividual> values = valuesNodeSet.getFlattened();
		
		for(OWLNamedIndividual ind : values) {
			if("".equals(diaFimFeriado)){
				diaFimFeriado =  ind.toString();
			} 
		}
		return diaFimFeriado;
	}
	
	/*public static void main(String args[]){
		SemanticHelper semanticHelper = new SemanticHelper();
		//semanticHelper.consultarDiaMesAnoFeriado(semanticHelper.consultarInstanciasDeSuperClasse("FeriadaoFimDeSemana"));
		semanticHelper.consultarInicioFimIntervalo("FeriadaoFimDeSemana");
	}*/
}