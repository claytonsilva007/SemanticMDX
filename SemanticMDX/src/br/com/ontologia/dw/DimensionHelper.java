package br.com.ontologia.dw;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import br.com.pojo.ClasseFeriado;

public class DimensionHelper {
	
	ArrayList<ClasseFeriado> listaDeFeriados = null;
	
	public ArrayList<ClasseFeriado> consultarFeriados(){
		//consultando todas as datas do DW
		String consulta = "select id_feriado, dia, mes, ano, dia_semana, descricao, abrangencia from feriado_by_day";
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			
			listaDeFeriados = new ArrayList<>();
			
			Connection con = ConnectionDW.getInstance().getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(consulta);

			while(rs.next()){
				int id  = rs.getInt("id_feriado");
				String numDia = String.valueOf(rs.getInt("dia"));
				String numMes = String.valueOf(rs.getInt("mes"));
				String numAno = String.valueOf(rs.getInt("ano"));
				String diaSemana = rs.getString("dia_semana");
				String descricao = rs.getString("descricao");
				String abrangencia = rs.getString("abrangencia");
				
				listaDeFeriados.add(new ClasseFeriado(descricao, numDia, numMes, numAno, diaSemana, abrangencia));

			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listaDeFeriados;
	}
}
