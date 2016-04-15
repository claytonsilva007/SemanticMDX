package br.com.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.xml.crypto.Data;

public class AssistenteDeData {
	public static String retornarDiaSemana(int ano, int mes, int dia)  
	{  

		Calendar calendario = new GregorianCalendar(ano, mes - 1, dia);  
		int diaSemana = calendario.get(Calendar.DAY_OF_WEEK);  

		return pesquisarDiaSemana(diaSemana);  
	}  

	//faz a pesquisa, dado um inteiro de 1 a 7  
	public static String pesquisarDiaSemana(int _diaSemana)  
	{  
		String diaSemana = null;  

		switch (_diaSemana){  

			case 1:  
				diaSemana = "domingo";  
				break;  
	
			case 2:  
				diaSemana = "segunda";  
				break;  
	
			case 3:  
				diaSemana = "terca";  
				break;  
	
			case 4:  
				diaSemana = "quarta";  
				break;  
	
			case 5:  
				diaSemana = "quinta";  
				break;  
	
			case 6:  
				diaSemana = "sexta";  
				break;  
	
			case 7:  
				diaSemana = "sabado";  
				break;  
			}  

		return diaSemana;  
	} 
	
	/* Converte uma String para um objeto Date. Caso a String seja vazia ou nula, 
	 * retorna null - para facilitar em casos onde formulários podem ter campos
	 * de datas vazios.
	 * @param data String no formato dd/MM/yyyy a ser formatada
	 * @return Date Objeto Date ou null caso receba uma String vazia ou nula
	 * @throws Exception Caso a String esteja no formato errado
	 */
	public static Date getData(String dataParam) { 
		Calendar c = Calendar.getInstance();
		Date data = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "br"));

		try {
			data = sdf.parse(dataParam);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public static String getDataFormatada(Date data){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "br"));
		return sdf.format(data);
	}
}