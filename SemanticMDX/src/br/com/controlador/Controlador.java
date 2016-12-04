package br.com.controlador;

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
		String strDimensao = "[Feriado.Default].";
		String novaConsultaMDX = queryMDX;
		this.mapDLOntologia = new HashMap<>();
		this.listaDLOntologia = new ArrayList<>();
		String[] vetorDlonto;
		Set<OWLNamedIndividual> listaDeIndividuos;
		String diasParteIntervalo = "";
		String diaInicioFimIntervalo = "";
		String diaSemanaOcorrFeriado = "";
		HashMap<String, String> mapDataFeriado = new HashMap<>();
		this.colecaoDatasParteIntervaloPorInvididuo = new ArrayList<>();

		StringBuffer strClausulaWhere = new StringBuffer(""); 

		ArrayList<String> ontologyClassExpression = this.extrairClassExpressionEOntologiaRelacionada(queryMDX); //identificando @dl:ontologia@
		for (String classExpressionString : ontologyClassExpression) {
			vetorDlonto = classExpressionString.split(":");
			String dl = vetorDlonto[0]; // a posição 0 representa a expressão ontológica
			String ontologiaRelacionada = vetorDlonto[1]; // a posição 1 representa a ontologia em que será realizada a consulta

			listaDeIndividuos = this.semanticHelper.consultarInstanciasDLExpressionClass(dl, ontologiaRelacionada);
			int posicaoAtualVetorIndividuos = 0;

			//para cada indivíduo subclasse de uma dada classExpression ...
			for (OWLNamedIndividual owlIndividual : listaDeIndividuos) { 
				String diasParteIntervaloFormatoOntologia = this.semanticHelper.consultarDiasDaSemanaParteDeIntervalo(owlIndividual);
				diasParteIntervalo = this.extrairInformacaoFormatoOntologia(diasParteIntervaloFormatoOntologia);
				diaInicioFimIntervalo = this.extrairInformacaoFormatoOntologia(this.semanticHelper.consultarInicioFimIntervalo(owlIndividual));
				diaSemanaOcorrFeriado = this.consultarDiaSemanaOcorrFeriado(owlIndividual);
				mapDataFeriado = this.consultarDataFeriado(owlIndividual);

				String datasParteIntervaloPorIndividuo = this.montarDatasParteFeriado(diasParteIntervalo, diaInicioFimIntervalo, diaSemanaOcorrFeriado, mapDataFeriado);

				if(!datasParteIntervaloPorIndividuo.equals("0/0/0")){
					if(datasParteIntervaloPorIndividuo.indexOf(",") != -1){  // se != -1 é porque tem mais de uma data
						String[] vetorDeDatasPorIndividuo = datasParteIntervaloPorIndividuo.split(","); //separando as datas por indivíduo
						int posicaoAtualVetorDatasPorIndividuo = 0;
						for (String data : vetorDeDatasPorIndividuo) {
							if(posicaoAtualVetorIndividuos < listaDeIndividuos.size() - 1){ // verificando se não é o último indivíduo recuperado
								if(posicaoAtualVetorDatasPorIndividuo == 0){
									strClausulaWhere.append("{");
									strClausulaWhere.append(strDimensao + "[Ano].&" + "[" + this.extrair("Ano", mapDataFeriado) + "]" + ",");
									strClausulaWhere.append(strDimensao + "[Mes].&" + "[" + this.extrair("Mes", mapDataFeriado) + "]" +  ",");
									strClausulaWhere.append(strDimensao + "[Dia].&" + "[" + this.extrair("Dia", mapDataFeriado) + "]");
									strClausulaWhere.append("}, ");
								} else if(posicaoAtualVetorDatasPorIndividuo > 0 && posicaoAtualVetorDatasPorIndividuo < vetorDeDatasPorIndividuo.length - 1) {
									strClausulaWhere.append("{");
									strClausulaWhere.append(strDimensao + "[Ano].&" + "[" + this.extrair("Ano", mapDataFeriado) + "]" + ",");
									strClausulaWhere.append(strDimensao + "[Mes].&" + "[" + this.extrair("Mes", mapDataFeriado) + "]" +  ",");
									strClausulaWhere.append(strDimensao + "[Dia].&" + "[" + this.extrair("Dia", mapDataFeriado) + "]");
									strClausulaWhere.append("}, ");
								} else {
									strClausulaWhere.append("{");
									strClausulaWhere.append(strDimensao + "[Ano].&" + "[" + this.extrair("Ano", mapDataFeriado) + "]" + ",");
									strClausulaWhere.append(strDimensao + "[Mes].&" + "[" + this.extrair("Mes", mapDataFeriado) + "]" +  ",");
									strClausulaWhere.append(strDimensao + "[Dia].&" + "[" + this.extrair("Dia", mapDataFeriado) + "]");
									strClausulaWhere.append("}");
								}

								posicaoAtualVetorDatasPorIndividuo++;
								
							} else { // é o último indivíduo
								if(posicaoAtualVetorDatasPorIndividuo == 0){ 
									strClausulaWhere.append("{");
									strClausulaWhere.append(strDimensao + "[Ano].&" + this.extrair("Ano", mapDataFeriado) + ",");
									strClausulaWhere.append(strDimensao + "[Mes].&" + this.extrair("Mes", mapDataFeriado)+ ",");
									strClausulaWhere.append(strDimensao + "[Dia].&" + this.extrair("Dia", mapDataFeriado));
									strClausulaWhere.append("}");
								} else if(posicaoAtualVetorDatasPorIndividuo > 0 && posicaoAtualVetorDatasPorIndividuo < vetorDeDatasPorIndividuo.length - 1) {
									strClausulaWhere.append("{");
									strClausulaWhere.append(strDimensao + "[Ano].&" + this.extrair("Ano", mapDataFeriado) + ",");
									strClausulaWhere.append(strDimensao + "[Mes].&" + this.extrair("Mes", mapDataFeriado)+ ",");
									strClausulaWhere.append(strDimensao + "[Dia].&" + this.extrair("Dia", mapDataFeriado));
									strClausulaWhere.append("}");
								} else {
									strClausulaWhere.append("{");
									strClausulaWhere.append(strDimensao + "[Ano].&" + this.extrair("Ano", mapDataFeriado) + ",");
									strClausulaWhere.append(strDimensao + "[Mes].&" + this.extrair("Mes", mapDataFeriado)+ ",");
									strClausulaWhere.append(strDimensao + "[Dia].&" + this.extrair("Dia", mapDataFeriado));
									strClausulaWhere.append("}");
								}

								posicaoAtualVetorDatasPorIndividuo++;

							}
						}
					} else {
						strClausulaWhere.append("{");
						strClausulaWhere.append(strDimensao + "[Ano].&" + "[" + this.extrair("Ano", mapDataFeriado) + "]" + ",");
						strClausulaWhere.append(strDimensao + "[Mes].&" + "[" + this.extrair("Mes", mapDataFeriado) + "]" +  ",");
						strClausulaWhere.append(strDimensao + "[Dia].&" + "[" + this.extrair("Dia", mapDataFeriado) + "]");
						strClausulaWhere.append("}");
					}
				}

				posicaoAtualVetorIndividuos++;
			}

			novaConsultaMDX = novaConsultaMDX.replace(Configuracoes.DELIMITADOR_SEMANTICO + classExpressionString + Configuracoes.DELIMITADOR_SEMANTICO, strClausulaWhere.toString());
		}

		return novaConsultaMDX;
	}

	private String adicionarFiltroWhere(HashMap<String, String> mapDataFeriado) {
		StringBuffer sb = new StringBuffer();
		sb.append(".&" + "[" + this.extrair("Ano", mapDataFeriado) + "], " );
		sb.append(".&" + "[" + this.extrair("Mes", mapDataFeriado) + "], " );
		sb.append(".&" + "[" + this.extrair("Dia", mapDataFeriado) + "] " );
		return sb.toString();
	}

	/**
	 * Recebe como parâmetro uma consulta MDX e extrai dela os termos semanticos
	 * @param consultaMDX
	 * @return arrayList de termos semânticos
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
		int dia = 0;
		int mes = 0;
		int ano = 0;

		try{
			dia = Integer.parseInt((mapDiaMesAnoFeriado.get("Dia")).substring(1, 3));
		} catch(Exception e){
			dia = Integer.parseInt((mapDiaMesAnoFeriado.get("Dia")).substring(1, 2));
		}

		try{
			mes = Integer.parseInt(mapDiaMesAnoFeriado.get("Mes").substring(1, 3));
		} catch(Exception e){
			mes = Integer.parseInt(mapDiaMesAnoFeriado.get("Mes").substring(1, 2));
		}
		try{
			ano = Integer.parseInt(mapDiaMesAnoFeriado.get("Ano").substring(1, 5));
		} catch(Exception e){
			if(dia == 0 && mes == 0){
				ano = 0;	
			}
		}


		return AssistenteDeData.retornarDiaSemana(ano, mes, dia);
	}

	/**
	 * Método que retorna a data de ocorrência do feriado
	 * @param superClasse
	 * @return data de ocorrência do feriado
	 */
	public HashMap<String, String> consultarDataFeriado(OWLIndividual owlIndividual){
		String dataFeriado = "";
		HashMap<String, String> mapDiaMesAnoFeriado = new HashMap<>();
		mapDiaMesAnoFeriado = semanticHelper.consultarDiaMesAnoFeriado(owlIndividual);
		return mapDiaMesAnoFeriado;
	}

	/**
	 * este método irá retornar as datas que correspondentes a um intervalo de dias.
	 * Exemplo: {[ano].[mes].[dia]}, ... 
	 * @return
	 */
	public String montarDatasParteFeriado(String diasParteDeIntervalo, String diaInicioFimIntervalo,
			String diaSemanaOcorrFeriado, HashMap<String, String> mapDataFeriado) {

		int dia = Integer.parseInt(this.extrair("Dia", mapDataFeriado));
		int mes = Integer.parseInt(this.extrair("Mes", mapDataFeriado));
		int ano = Integer.parseInt(this.extrair("Ano", mapDataFeriado));

		String data = "";
		int diaBase = 0;
		String retorno = "";

		data = dia+"/"+mes+"/"+ano;
		diaBase = dia;

		StringBuilder datasIntervalo = new StringBuilder();
		String[] vetorInicioFimIntervalo = diaInicioFimIntervalo.split(Configuracoes.SEPARADOR);
		String[] vetorDiasParteDeIntervalo = diasParteDeIntervalo.split(Configuracoes.SEPARADOR);

		/* posição 0 representa o início
		 *  posição 1 representa o fim 
		 *  Se o dia de início coincidir com o dia do feriado, e ambos ocorrerem em uma sexta, estaremos diante de um feriadão de fim de semana
		 *  Se o dia de início coincidir com o dia do feriado, e ambos ocorrerem em uma quinta, estaremos diante de um imprensado de fim de semana
		 *  Se o dia de fim coincidir com o dia do feriado, e ambos ocorrerem em uma segunda, estaremos diante de um feriadão de início de semana
		 *  Se o dia de fim coincidir com o dia do feriado, e ambos ocorrerem em uma terça, estaremos diante de um imprensado de início de semana
		 */ 

		/**
		 * Se esse vetor possuir mais de uma posição quer dizer que é um feriadão.
		 * Caso contrário, será um feriado simples.
		 */
		if(vetorInicioFimIntervalo.length > 1){
			//verificando se o intervalo possui mais de um dia. Caso não possua, não é feriadão
			if(vetorDiasParteDeIntervalo.length > 1){
				//feriadaoFimDeSemana - sexta, sábado, domingo
				if(diaSemanaOcorrFeriado.equals(vetorInicioFimIntervalo[0]) && diaSemanaOcorrFeriado.equals("sexta")){
					datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
					datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
					datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]");

					//impresadoFimDeSemana	- quinta, sexta, sábado, domingo
				} else if(diaSemanaOcorrFeriado.equals(vetorInicioFimIntervalo[0]) && diaSemanaOcorrFeriado.equals("quinta")){
					datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
					datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
					datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]");

					//feriadaoInicioSemana - sábado, domingo, segunda
				} else if(diaSemanaOcorrFeriado.equals(vetorInicioFimIntervalo[1]) && diaSemanaOcorrFeriado.equals("segunda")){
					datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
					datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
					datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]");

					//imprensadoInicioSemana - sábado, domingo, segunda, terça
				} else if(diaSemanaOcorrFeriado.equals(vetorInicioFimIntervalo[1]) && diaSemanaOcorrFeriado.equals("terca")){
					datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
					datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
					datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]");
				}
			} else{
				datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]");
			}

			retorno = datasIntervalo.toString();

		} else {
			retorno = "["+ano+"]." + "[" + mes + "]." + "[" + dia + "]";
		}
		return retorno;
	}

	private String extrair(String str, HashMap<String, String> mapDataFeriado) {
		String retorno = "";
		switch (str) {
		case "Dia":
			try{
				retorno = mapDataFeriado.get("Dia").substring(1, 3);
			} catch(Exception e){
				retorno = mapDataFeriado.get("Dia").substring(1, 2);
			}
		break;

		case "Mes":
			try{
				retorno = mapDataFeriado.get("Mes").substring(1, 3);
			} catch(Exception e){
				retorno = mapDataFeriado.get("Mes").substring(1, 2);
			}
		break;
		case "Ano":
			try{
				retorno = mapDataFeriado.get("Ano").substring(1, 5);
			} catch(Exception e){
				retorno = "0";
			}
		break;
		default:
			retorno = "0";
			break;
			
		}
		return retorno;
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