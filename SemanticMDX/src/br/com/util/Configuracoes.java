package br.com.util;

public class Configuracoes {
	public static final String IRI_BASE = "http://www.semanticweb.org/clayton/ontologies/2014/11/untitled-ontology-167";
	public static final String SEPARADOR = ";";
	public static final char DELIMITADOR_SEMANTICO = '@';
	public static final String SEPARADOR_ONTOLOGICO = "#";
	
	public static String getIriBase() {
		return IRI_BASE;
	}
	
	public static String getSeparador(){
		return SEPARADOR;
	}
	
	public static char getTokenSemantico(){
		return DELIMITADOR_SEMANTICO;
	}
}
