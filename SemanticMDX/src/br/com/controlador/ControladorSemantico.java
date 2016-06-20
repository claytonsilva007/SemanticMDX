package br.com.controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

import br.com.ontologia.SemanticHelper;
import br.com.ontologia.SemanticHelperInterface;
import br.com.util.AssistenteDeData;
import br.com.util.Configuracoes;

public class ControladorSemantico implements ControladorSemanticoInterface{

	SemanticHelperInterface semanticHelper;

	public ControladorSemantico() {
		this.semanticHelper = new SemanticHelper();
	}

	@Override
	public String submeterConsulta(String queryMDX) {
		String novaConsultaMDX = queryMDX;
		ArrayList<String> listaTermosSemanticos = new ArrayList<>();
		ArrayList<HashMap<String, String>> listMapOnto = new ArrayList<>();
		HashMap<String, String> mapTermoValorOnto = new HashMap<>();
		String diasParteDeIntervalo = "";
		String diaInicioFimIntervalo = "";
		String diaSemanaOcorrFeriado = "";;
		HashMap<String, String> mapDataFeriado = new HashMap<>();;
		String listaDatasParteIntervalo = "";
		
		listaTermosSemanticos = this.extrairTermosSemanticos(queryMDX);
		
		for (String termoSemanticoTemp : listaTermosSemanticos) {
			diasParteDeIntervalo = this.consultarDiasParteIntervalo(termoSemanticoTemp);
			diaInicioFimIntervalo = this.consultarInicioFimIntervalo(termoSemanticoTemp);
			mapDataFeriado = this.consultarDataFeriado(termoSemanticoTemp);
			diaSemanaOcorrFeriado = this.consultarDiaSemanaOcorrFeriado(termoSemanticoTemp);
			listaDatasParteIntervalo = this.montarDatasParteFeriado(diasParteDeIntervalo, diaInicioFimIntervalo, diaSemanaOcorrFeriado, mapDataFeriado);
			
			novaConsultaMDX = novaConsultaMDX.replace(Configuracoes.DELIMITADOR_SEMANTICO + termoSemanticoTemp + Configuracoes.DELIMITADOR_SEMANTICO, listaDatasParteIntervalo);
		}

		return novaConsultaMDX;
	}

	public String consultarDiasParteIntervalo(String superClasse){
		String diasParteIntervalo = "";
		String diasParteIntervaloFormatoOntologia = "";		
		Set<OWLNamedIndividual> individuos = this.semanticHelper.consultarInstanciasDeSuperClasse(superClasse);
		for (OWLNamedIndividual owlNamedIndividual : individuos) {
			diasParteIntervaloFormatoOntologia = this.semanticHelper.consultarDiasDaSemanaParteDeIntervalo(owlNamedIndividual);
		}

		diasParteIntervalo = this.extrairDiasDaSemana(diasParteIntervaloFormatoOntologia);

		return diasParteIntervalo;
	}

	public String consultarInicioFimIntervalo(String superClasse){
		SemanticHelper semanticHelper = new SemanticHelper();
		String[] diaInicioFim = semanticHelper.consultarInicioFimIntervalo(superClasse).split(Configuracoes.SEPARADOR);
		return this.extrairDiasDaSemana(diaInicioFim[0]) + Configuracoes.SEPARADOR + this.extrairDiasDaSemana(diaInicioFim[1]);
	}

	/**
	 * Método que retorna a data de ocorrência do feriado
	 * @param superClasse
	 * @return data de ocorrência do feriado
	 */
	public HashMap<String, String> consultarDataFeriado(String superClasse){
		String dataFeriado = "";
		HashMap<String, String> mapDiaMesAnoFeriado = new HashMap<>();
		mapDiaMesAnoFeriado = semanticHelper.consultarDiaMesAnoFeriado(semanticHelper.consultarInstanciasDeSuperClasse(superClasse));
		return mapDiaMesAnoFeriado;
	}

	/**
	 * retorna uma string no formado dia/mes/ano extraida a partir do retorno da ontologia(hashMap)
	 * @param mapDiaMesFeriado
	 * @return 
	 */
	public String extrairDiaMesAno(HashMap<String, String> mapDiaMesFeriado){
		String dia = mapDiaMesFeriado.get("Dia").substring(2, 4);
		String mes = mapDiaMesFeriado.get("Mes").substring(2, 4);
		String ano = mapDiaMesFeriado.get("Ano").substring(2, 6);
		return dia + Configuracoes.SEPARADOR_ONTOLOGICO + mes + Configuracoes.SEPARADOR_ONTOLOGICO + ano;
	}

	/**
	 * extrai os dias da semana da iri da ontologia
	 * @param diasParteIntervaloFormatoOntologia
	 * @return
	 */
	public String extrairDiasDaSemana(String diasParteIntervaloFormatoOntologia){
		String diasDaSemanaParteIntervalo = "";
		String[] vetorDiasDasemana = diasParteIntervaloFormatoOntologia.split(Configuracoes.SEPARADOR);
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

	public String consultarDiaSemanaOcorrFeriado(String superClasse){
		HashMap<String, String> mapDiaMesAnoFeriado = new HashMap<>();
		mapDiaMesAnoFeriado = semanticHelper.consultarDiaMesAnoFeriado(semanticHelper.consultarInstanciasDeSuperClasse(superClasse));

		int dia = Integer.parseInt(mapDiaMesAnoFeriado.get("Dia").substring(1, 3));
		int mes = Integer.parseInt(mapDiaMesAnoFeriado.get("Mes").substring(1, 3));
		int ano = Integer.parseInt(mapDiaMesAnoFeriado.get("Ano").substring(1, 5));

		return AssistenteDeData.retornarDiaSemana(ano, mes, dia);
	}

	/**
	 * este método irá retornar as datas que correspondentes a um intervalo de dias.
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
		
		 /* posição 0 representa o início
		 *  posição 1 representa o fim 
		 *  Se o dia de início coincidir com o dia do feriado, e ambos ocorrerem em uma sexta, estaremos diante de um feriadão de fim de semana
		 *  Se o dia de início coincidir com o dia do feriado, e ambos ocorrerem em uma quinta, estaremos diante de um imprensado de fim de semana
		 *  Se o dia de fim coincidir com o dia do feriado, e ambos ocorrerem em uma segunda, estaremos diante de um feriadão de início de semana
		 *  Se o dia de fim coincidir com o dia do feriado, e ambos ocorrerem em uma terça, estaremos diante de um imprensado de início de semana
		*/ 
		//feriadaoFimDeSemana - sexta, sábado, domingo
		if(diaSemanaOcorrFeriado.equals(vetorInicioFimIntervalo[0]) && diaSemanaOcorrFeriado.equals("sexta")){
			datasIntervalo.append("{" + "[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
			datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
			datasIntervalo.append("[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + "}");

			//impresadoFimDeSemana	- quinta, sexta, sábado, domingo
		} else if(diaSemanaOcorrFeriado.equals(vetorInicioFimIntervalo[0]) && diaSemanaOcorrFeriado.equals("quinta")){
			datasIntervalo.append("{" + "[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
			datasIntervalo.append("{" + "[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
			datasIntervalo.append("{" + "[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]");

			//feriadaoInicioSemana - sábado, domingo, segunda
		} else if(diaSemanaOcorrFeriado.equals(vetorInicioFimIntervalo[1]) && diaSemanaOcorrFeriado.equals("segunda")){
			datasIntervalo.append("{" + "[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
			datasIntervalo.append("{" + "[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
			datasIntervalo.append("{" + "[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]");
			
			//imprensadoInicioSemana - sábado, domingo, segunda, terça
		} else if(diaSemanaOcorrFeriado.equals(vetorInicioFimIntervalo[1]) && diaSemanaOcorrFeriado.equals("terca")){
			datasIntervalo.append("{" + "[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
			datasIntervalo.append("{" + "[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]" + ", ");
			datasIntervalo.append("{" + "[" + ano + "]" + "." + "[" + mes + "]" + "." + "[" + ++diaBase + "]");
		}
		return datasIntervalo.toString();
	}

	/**
	 * Recebe como parâmetro uma consulta MDX e extrai dela os termos semanticos
	 * @param consultaMDX
	 * @return arrayList de termos semânticos
	 */
	public ArrayList<String> extrairTermosSemanticos(String consultaMDX){
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
	
	/*public static void main(String arg[]){
		ControladorSemantico cs = new ControladorSemantico();
		System.out.println(cs.consultarDiasParteIntervalo("FeriadaoFimDeSemana"));
	}*/
}