package br.com.controlador;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import br.com.ontologia.SemanticHelper;
import br.com.util.AssistenteDeData;
import br.com.util.Configuracoes;

public class Controlador {
	private SemanticHelper semanticHelper;
	HashMap<String, String> mapDLOntologia;
	ArrayList<HashMap<String, String>> listaDLOntologia;
	ArrayList<String> colecaoDatasParteIntervaloPorInvididuo;

	public Controlador(){
		this.semanticHelper = new SemanticHelper();
	}
	
	public String submeterConsulta(String queryMDX){
		StringBuilder stringBuilder = new StringBuilder();
		String novaConsultaMDX = queryMDX;
		this.mapDLOntologia = new HashMap<>();
		this.listaDLOntologia = new ArrayList<>();
		String[] vetorDlonto;
		Set<OWLNamedIndividual> listaDeIndividuos;
		String diasParteIntervalo = "";
		String diaInicioFimIntervalo = "";
		String diaSemanaOcorrFeriado = "";
		HashMap<String, String> mapDiaMesAnoFeriado = new HashMap<>();
		String listaDatasParteIntervalo = "";
		HashMap<String, String> mapDataFeriado = new HashMap<>();
		this.colecaoDatasParteIntervaloPorInvididuo = new ArrayList<>();
		
		ArrayList<String> colecaoClassExpression = this.extrairClassExpressionEOntologiaRelacionada(queryMDX); //identificando @dl:ontologia@
		for (String classExpressionString : colecaoClassExpression) {
			vetorDlonto = classExpressionString.split(":");
			String dl = vetorDlonto[0];
			String ontologiaRelacionada = vetorDlonto[1];

			listaDeIndividuos = this.semanticHelper.consultarInstanciasDLExpressionClass(dl, ontologiaRelacionada);
			int posicaoAtual = 0;
			//para cada indiv�duo subclasse de uma dada classExpression ...
			for (OWLNamedIndividual owlIndividual : listaDeIndividuos) { 
				String diasParteIntervaloFormatoOntologia = this.semanticHelper.consultarDiasDaSemanaParteDeIntervalo(owlIndividual);
				diasParteIntervalo = this.extrairInformacaoFormatoOntologia(diasParteIntervaloFormatoOntologia);
				diaInicioFimIntervalo = this.extrairInformacaoFormatoOntologia(this.semanticHelper.consultarInicioFimIntervalo(owlIndividual));
				diaSemanaOcorrFeriado = this.consultarDiaSemanaOcorrFeriado(owlIndividual);
				mapDataFeriado = this.consultarDataFeriado(owlIndividual);
				String datasParteIntervaloPorIndividuo = this.montarDatasParteFeriado(diasParteIntervalo, diaInicioFimIntervalo, diaSemanaOcorrFeriado, mapDataFeriado);
				
				if(posicaoAtual < listaDeIndividuos.size() - 1){
					stringBuilder.append(datasParteIntervaloPorIndividuo + ",");
				} else {
					stringBuilder.append(datasParteIntervaloPorIndividuo);
				}
				posicaoAtual++;
			}
			
			novaConsultaMDX = novaConsultaMDX.replace(Configuracoes.DELIMITADOR_SEMANTICO + classExpressionString + Configuracoes.DELIMITADOR_SEMANTICO, stringBuilder.toString());
		}
		
		return novaConsultaMDX;
	}
	
	/**
	 * Recebe como par�metro uma consulta MDX e extrai dela os termos semanticos
	 * @param consultaMDX
	 * @return arrayList de termos sem�nticos
	 */
	public ArrayList<String> extrairClassExpressionEOntologiaRelacionada(String consultaMDX){
		char[] caracteres = consultaMDX.toCharArray();
		StringBuilder termoSemantico = new StringBuilder();
		ArrayList<String> termosSemanticos = new ArrayList<>();

		int k = 0;
		boolean achouTokenAbertura = false;
		boolean achouTokenFechamento = false;

		for (int i = 0; i < caracteres.length; i++) {
			if(caracteres[i] == Configuracoes.DELIMITADOR_SEMANTICO){
				achouTokenAbertura = true;
				for (k = i+1; k < caracteres.length; k++) {
					if(caracteres[k] == Configuracoes.DELIMITADOR_SEMANTICO){
						achouTokenFechamento = true;
						break;
					} else{
						termoSemantico.append(caracteres[k]);
					}
				}

				if(achouTokenAbertura && achouTokenFechamento){
					termosSemanticos.add(termoSemantico.toString());
					termoSemantico = new StringBuilder();
					i = k+1;
				}
			}
		}
		return termosSemanticos;
	}
	
	/**
	 * extrai os dias da semana da iri da ontologia
	 * @param elementoFormatoIRIOntologia
	 * @return
	 */
	public String extrairInformacaoFormatoOntologia(String elementoFormatoIRIOntologia){
		String diasDaSemanaParteIntervalo = "";
		String[] vetorDiasDasemana = elementoFormatoIRIOntologia.split(Configuracoes.SEPARADOR);
		for(int i=0; i< vetorDiasDasemana.length; i++){
			if(diasDaSemanaParteIntervalo.equals("")){
				diasDaSemanaParteIntervalo = vetorDiasDasemana[i].substring(vetorDiasDasemana[i].indexOf(Configuracoes.SEPARADOR_ONTOLOGICO) + 1, vetorDiasDasemana[i].indexOf(">"));
			} else {
				diasDaSemanaParteIntervalo = diasDaSemanaParteIntervalo + Configuracoes.SEPARADOR + vetorDiasDasemana[i]
						.substring(vetorDiasDasemana[i].indexOf(Configuracoes.SEPARADOR_ONTOLOGICO) + 1, vetorDiasDasemana[i].indexOf(">"));
			}
		}

		return diasDaSemanaParteIntervalo;
	}
	
	public String consultarDiaSemanaOcorrFeriado(OWLIndividual owlIndividual){
		HashMap<String, String> mapDiaMesAnoFeriado = new HashMap<>();
		mapDiaMesAnoFeriado = this.semanticHelper.consultarDiaMesAnoFeriado(owlIndividual);

		int dia = Integer.parseInt(mapDiaMesAnoFeriado.get("Dia").substring(1, 3));
		int mes = Integer.parseInt(mapDiaMesAnoFeriado.get("Mes").substring(1, 3));
		int ano = Integer.parseInt(mapDiaMesAnoFeriado.get("Ano").substring(1, 5));

		return AssistenteDeData.retornarDiaSemana(ano, mes, dia);
	}
	
	/**
	 * M�todo que retorna a data de ocorr�ncia do feriado
	 * @param superClasse
	 * @return data de ocorr�ncia do feriado
	 */
	public HashMap<String, String> consultarDataFeriado(OWLIndividual owlIndividual){
		String dataFeriado = "";
		HashMap<String, String> mapDiaMesAnoFeriado = new HashMap<>();
		mapDiaMesAnoFeriado = semanticHelper.consultarDiaMesAnoFeriado(owlIndividual);
		return mapDiaMesAnoFeriado;
	}
	
	/**
	 * este m�todo ir� retornar as datas que correspondentes a um intervalo de dias.
	 * Exemplo: {[ano].[mes].[dia]}, ... 
	 * @return
	 */
	public String montarDatasParteFeriado(String diasParteDeIntervalo, String diaInicioFimIntervalo,
			String diaSemanaOcorrFeriado, HashMap<String, String> mapDataFeriado) {

		String dia = mapDataFeriado.get("Dia").substring(1, 3);
		String mes = mapDataFeriado.get("Mes").substring(1, 3);
		String ano = mapDataFeriado.get("Ano").substring(1, 5);

		String data = dia+"/"+mes+"/"+ano;

		int diaBase = Integer.parseInt(dia);

		StringBuilder datasIntervalo = new StringBuilder();
		String[] vetorInicioFimIntervalo = diaInicioFimIntervalo.split(Configuracoes.SEPARADOR);
		String[] vetorDiasParteDeIntervalo = diasParteDeIntervalo.split(Configuracoes.SEPARADOR);
		
		 /* posi��o 0 representa o in�cio
		 *  posi��o 1 representa o fim 
		 *  Se o dia de in�cio coincidir com o dia do feriado, e ambos ocorrerem em uma sexta, estaremos diante de um feriad�o de fim de semana
		 *  Se o dia de in�cio coincidir com o dia do feriado, e ambos ocorrerem em uma quinta, estaremos diante de um imprensado de fim de semana
		 *  Se o dia de fim coincidir com o dia do feriado, e ambos ocorrerem em uma segunda, estaremos diante de um feriad�o de in�cio de semana
		 *  Se o dia de fim coincidir com o dia do feriado, e ambos ocorrerem em uma ter�a, estaremos diante de um imprensado de in�cio de semana
		*/ 
		//feriadaoFimDeSemana - sexta, s�bado, domingo
		if(diaSemanaOcorrFeriado.equals(vetorInicioFimIntervalo[0]) && diaSemanaOcorrFeriado.equals("sexta")){
			datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
			datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
			datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]");

			//impresadoFimDeSemana	- quinta, sexta, s�bado, domingo
		} else if(diaSemanaOcorrFeriado.equals(vetorInicioFimIntervalo[0]) && diaSemanaOcorrFeriado.equals("quinta")){
			datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
			datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
			datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]");

			//feriadaoInicioSemana - s�bado, domingo, segunda
		} else if(diaSemanaOcorrFeriado.equals(vetorInicioFimIntervalo[1]) && diaSemanaOcorrFeriado.equals("segunda")){
			datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
			datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
			datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]");
			
			//imprensadoInicioSemana - s�bado, domingo, segunda, ter�a
		} else if(diaSemanaOcorrFeriado.equals(vetorInicioFimIntervalo[1]) && diaSemanaOcorrFeriado.equals("terca")){
			datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
			datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
			datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]");
		}
		return datasIntervalo.toString();
	}

	public ArrayList<String> consultarSuperClasses(String query) {
		ArrayList<String> listaDeSuperClasses = new ArrayList<>();
		Set<OWLClass> superClasses = this.semanticHelper.consultarSuperClassesDLExpressionClass(query, "ajustar depois");
		for (OWLClass owlClass : superClasses) {
			listaDeSuperClasses.add(this.extrairInformacaoFormatoOntologia(owlClass.toString()));
		}
		return listaDeSuperClasses;
	}
	
	public ArrayList<String> consultarClasses(String query){
		ArrayList<String> listaDeClasses = new ArrayList<>();
		Set<OWLClass> classes = this.semanticHelper.consultarClassesDLExpressionClass(query, "ajustar depois");
		for (OWLClass owlClass : classes) {
			listaDeClasses.add(this.extrairInformacaoFormatoOntologia(owlClass.toString()));
		}
		return listaDeClasses;
	} 
	
	public ArrayList<String> consultarInstancias(String query){
		ArrayList<String> listaDeInstancias = new ArrayList<>();
		Set<OWLNamedIndividual> instancias = this.semanticHelper.consultarInstanciasDLExpressionClass(query, "ajustar depois");
		for (OWLNamedIndividual owlNamedIndividual : instancias) {
			listaDeInstancias.add(this.extrairInformacaoFormatoOntologia(owlNamedIndividual.toString()));
		}
		return listaDeInstancias;
	}
	
	public ArrayList<String> consultarEquivalentClasses(String query){
		ArrayList<String> listaDeEquivalentClasses = new ArrayList<>();
		Set<OWLClass> equivalentClasses = this.semanticHelper.consultarEquivalentClassesDLExpressionClass(query, "ajustar depois");
		for (OWLClass owlClass : equivalentClasses) {
			listaDeEquivalentClasses.add(this.extrairInformacaoFormatoOntologia(owlClass.toString()));
		}
		return listaDeEquivalentClasses;
	}
	
	public ArrayList<String> consultarTodasAsClassesOntologia(){
		ArrayList<String> listaClasses = new ArrayList<>();
		Set<OWLClass> listaTodasAsclasses = this.semanticHelper.consultarTodasAsClassesOntologia();
		for (OWLClass owlClass : listaTodasAsclasses) {
			listaClasses.add(this.extrairInformacaoFormatoOntologia(owlClass.toString()));
		}
		return listaClasses;
	}
	
	public ArrayList<String> consultarPropriedadesOntologia(){
		ArrayList<String> listaPropriedades = new ArrayList<>();
		Set<OWLObjectProperty> listaTemp = this.semanticHelper.consultarTodasPropriedadesOntologia();
		for (OWLObjectProperty owlObjectProperty : listaTemp) {
			listaPropriedades.add(this.extrairInformacaoFormatoOntologia(listaTemp.toString()));
		}
		return listaPropriedades;
	}
}