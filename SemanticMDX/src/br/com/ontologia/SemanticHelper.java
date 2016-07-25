package br.com.ontologia;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;
import org.semanticweb.owlapi.vocab.OWLFacet;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

import br.com.ontologia.dlquery.DLQueryEngine;
import br.com.ontologia.dlquery.DLQueryParser;
import br.com.pojo.ClasseFeriado;
import br.com.util.Configuracoes;

public class SemanticHelper implements SemanticHelperInterface{
	private static final File file = new File("C:\\Users\\clayton\\Desktop\\testandoTopObjectProperty.owl");
	private static OWLOntology OWL_ONTOLOGY = null;

	private OWLReasoner reasoner;
	private DLQueryEngine dlQueryEngine;
	private ShortFormProvider shortFormProvider;

	public SemanticHelper() {
		OWL_ONTOLOGY = this.carregarOntologia();
		OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
		this.reasoner = reasonerFactory.createReasoner(OWL_ONTOLOGY, new SimpleConfiguration());
		this.shortFormProvider = new SimpleShortFormProvider();
		this.dlQueryEngine = new DLQueryEngine(this.reasoner, shortFormProvider); 
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
		OWLClass owlClass = 
				OWL_ONTOLOGY
				.getOWLOntologyManager()
				.getOWLDataFactory()
				.getOWLClass(IRI.create(Configuracoes.getIriBase() + Configuracoes.SEPARADOR_ONTOLOGICO + superClasse));

		this.reasoner.precomputeInferences();
		Set<OWLNamedIndividual> individuos = this.reasoner.getInstances(owlClass, false).getFlattened();

		return individuos;
	}

	@Override
	public HashMap<String, String> consultarDiaMesAnoFeriado(Set<OWLNamedIndividual> setOWLNamedIndividuals) {
		HashMap<String, String> hashMap = new HashMap<>();

		this.reasoner.precomputeInferences();

		for (OWLNamedIndividual owlNamedIndividual : setOWLNamedIndividuals) {
			hashMap.put("Dia", this.consultarDiaOcorrenciaFeriado(owlNamedIndividual));
			hashMap.put("Mes", this.consultarMesOcorrenciaFeriado(owlNamedIndividual));
			hashMap.put("Ano", this.consultarAnoOcorrenciaFeriado(owlNamedIndividual));
		}
		return hashMap;
	}

	public HashMap<String, String> consultarDiaMesAnoFeriado(OWLIndividual oWLIndividual) {
		HashMap<String, String> hashMap = new HashMap<>();

		this.reasoner.precomputeInferences();

		hashMap.put("Dia", this.consultarDiaOcorrenciaFeriado((OWLNamedIndividual) oWLIndividual));
		hashMap.put("Mes", this.consultarMesOcorrenciaFeriado((OWLNamedIndividual) oWLIndividual));
		hashMap.put("Ano", this.consultarAnoOcorrenciaFeriado((OWLNamedIndividual) oWLIndividual));

		return hashMap;
	}

	public String consultarDiaOcorrenciaFeriado(OWLNamedIndividual owlNamedIndividual){
		String retorno = "";

		OWLDataProperty OwlDataPropertyTemDia = 
				OWL_ONTOLOGY
				.getOWLOntologyManager()
				.getOWLDataFactory()
				.getOWLDataProperty(IRI.create(Configuracoes.getIriBase() + Configuracoes.SEPARADOR_ONTOLOGICO + "temDia"));

		this.reasoner.precomputeInferences();

		Set<OWLLiteral> valueProperty	= this.reasoner.getDataPropertyValues(owlNamedIndividual, OwlDataPropertyTemDia);
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

		OWLDataProperty OwlDataPropertyTemAno = 
				OWL_ONTOLOGY
				.getOWLOntologyManager()
				.getOWLDataFactory()
				.getOWLDataProperty(IRI.create(Configuracoes.getIriBase() + Configuracoes.SEPARADOR_ONTOLOGICO + "temAno"));

		this.reasoner.precomputeInferences();

		Set<OWLLiteral> valueProperty = this.reasoner.getDataPropertyValues(owlNamedIndividual, OwlDataPropertyTemAno);
		for (OWLLiteral owlLiteral : valueProperty) {
			retorno = owlLiteral.toString();
		}
		return retorno;
	}

	@Override
	public String consultarDiasDaSemanaParteDeIntervalo(OWLNamedIndividual owlNamedIndividual) {
		String diasParteDeIntervalo = "";

		this.reasoner.precomputeInferences();

		OWLDataFactory df = this.OWL_ONTOLOGY.getOWLOntologyManager().getOWLDataFactory();

		OWLObjectProperty temDiaFeriado = df.getOWLObjectProperty(IRI.create(Configuracoes.getIriBase() + "#temDiaFeriado"));

		NodeSet<OWLNamedIndividual> valuesNodeSet = this.reasoner.getObjectPropertyValues(owlNamedIndividual, temDiaFeriado);
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

	public String consultarInicioFimIntervalo(OWLIndividual owlIndividual){
		String inicioFimFeriado = "";
		inicioFimFeriado = this.consultarDiaInicioIntervalo((OWLNamedIndividual) owlIndividual) + Configuracoes.SEPARADOR + this.consultarDiaFimIntervalo((OWLNamedIndividual) owlIndividual);
		return inicioFimFeriado;
	}

	public String consultarDiaInicioIntervalo(OWLNamedIndividual owlNamedIndividual){
		String diaInicioFeriado = "";

		this.reasoner.precomputeInferences();

		OWLDataFactory df = OWL_ONTOLOGY.getOWLOntologyManager().getOWLDataFactory();
		OWLObjectProperty temDiaInicio = df.getOWLObjectProperty(IRI.create(Configuracoes.getIriBase() + Configuracoes.SEPARADOR_ONTOLOGICO + "temDiaInicio"));

		NodeSet<OWLNamedIndividual> valuesNodeSet = this.reasoner.getObjectPropertyValues(owlNamedIndividual, temDiaInicio);
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

		this.reasoner.precomputeInferences();

		OWLDataFactory df = OWL_ONTOLOGY.getOWLOntologyManager().getOWLDataFactory();
		OWLObjectProperty temDiaFim = df.getOWLObjectProperty(IRI.create(Configuracoes.getIriBase() + Configuracoes.SEPARADOR_ONTOLOGICO + "temDiaFim"));

		NodeSet<OWLNamedIndividual> valuesNodeSet = this.reasoner.getObjectPropertyValues(owlNamedIndividual, temDiaFim);
		Set<OWLNamedIndividual> values = valuesNodeSet.getFlattened();

		for(OWLNamedIndividual ind : values) {
			if("".equals(diaFimFeriado)){
				diaFimFeriado =  ind.toString();
			} 
		}
		return diaFimFeriado;
	}

	public Set<OWLNamedIndividual> consultarInstanciasDLExpressionClass(String classExpressionString, String ontologiaRelacionada){
		return this.dlQueryEngine.getInstances(classExpressionString, true);
	}

	public Set<OWLClass> consultarClassesDLExpressionClass(String classExpressionString, String ontologiaRelacionada){
		return this.dlQueryEngine.getSubClasses(classExpressionString, true);
	}

	public Set<OWLClass> consultarEquivalentClassesDLExpressionClass(String classExpressionString, String ontologiaRelacionada){
		return this.dlQueryEngine.getEquivalentClasses(classExpressionString);
	}

	public Set<OWLClass> consultarSuperClassesDLExpressionClass(String classExpressionString, String ontologiaRelacionada){
		return this.dlQueryEngine.getSuperClasses(classExpressionString, true);
	}

	public Set<OWLClass> consultarTodasAsClassesOntologia(){
		return OWL_ONTOLOGY.getClassesInSignature();
	}

	public Set<OWLObjectProperty> consultarTodasPropriedadesOntologia(){
		return OWL_ONTOLOGY.getObjectPropertiesInSignature();
	}

	@Override
	public void criarInstanciasFeriados(ArrayList<ClasseFeriado> feriados) throws OWLOntologyCreationException, OWLOntologyStorageException {

		OWLOntologyManager manager = OWL_ONTOLOGY.getOWLOntologyManager(); 
		OWLDataFactory factory = OWL_ONTOLOGY.getOWLOntologyManager().getOWLDataFactory();
		
		for (ClasseFeriado feriado : feriados) {
			OWLIndividual descricaoFeriado = factory.getOWLNamedIndividual(IRI.create(Configuracoes.IRI_BASE + "#" + feriado.getNome())); //novo indivíduo
			OWLIndividual diaSemanaOcorrencia = factory.getOWLNamedIndividual(IRI.create(Configuracoes.IRI_BASE + "#" + feriado.getDiaDaSemanaOcorrencia()));
			OWLObjectProperty property = factory.getOWLObjectProperty(IRI.create(Configuracoes.IRI_BASE + "#temDiaFeriado"));
			OWLObjectPropertyAssertionAxiom axiom1 = factory.getOWLObjectPropertyAssertionAxiom(property, descricaoFeriado, diaSemanaOcorrencia);

			AddAxiom addAxiom1 = new AddAxiom(OWL_ONTOLOGY, axiom1);
			manager.applyChange(addAxiom1);

			OWLDataProperty temDia = factory.getOWLDataProperty(IRI.create(Configuracoes.IRI_BASE + "#temDia"));
			OWLDataProperty temMes= factory.getOWLDataProperty(IRI.create(Configuracoes.IRI_BASE + "#temMes"));
			OWLDataProperty temAno = factory.getOWLDataProperty(IRI.create(Configuracoes.IRI_BASE + "#temAno"));

			OWLLiteral dia = factory.getOWLLiteral(Integer.parseInt(feriado.getDia()));
			OWLLiteral mes = factory.getOWLLiteral(Integer.parseInt(feriado.getMes()));
			OWLLiteral ano = factory.getOWLLiteral(Integer.parseInt(feriado.getAno()));

			OWLAxiom axiomDia = factory.getOWLDataPropertyAssertionAxiom(temDia, descricaoFeriado, dia);
			OWLAxiom axiomMes = factory.getOWLDataPropertyAssertionAxiom(temMes, descricaoFeriado, mes);
			OWLAxiom axiomAno = factory.getOWLDataPropertyAssertionAxiom(temAno, descricaoFeriado, ano);

			AddAxiom addAxiomDia = new AddAxiom(this.OWL_ONTOLOGY, axiomDia);
			manager.applyChange(addAxiomDia);

			AddAxiom addAxiomMes = new AddAxiom(this.OWL_ONTOLOGY, axiomMes);
			manager.applyChange(addAxiomMes);

			AddAxiom addAxiomAno = new AddAxiom(this.OWL_ONTOLOGY, axiomAno);
			manager.applyChange(addAxiomAno);
			
			manager.saveOntology(OWL_ONTOLOGY);

		}
	}

	public static void main(String args[]){
		ArrayList<ClasseFeriado> feriados = new ArrayList<>();
		SemanticHelper sh = new SemanticHelper();
		ClasseFeriado feriado = new ClasseFeriado();
		feriado.setDia("15");
		feriado.setMes("12");
		feriado.setAno("2016");
		feriado.setDiaDaSemanaOcorrencia("sexta");
		feriado.setNome("feriadoTesteABCD");
		feriados.add(feriado);	

		try {
			sh.criarInstanciasFeriados(feriados);
		} catch (OWLOntologyCreationException | OWLOntologyStorageException e) {
			e.printStackTrace();
		}

	}
}