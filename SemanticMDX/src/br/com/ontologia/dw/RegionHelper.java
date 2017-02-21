package br.com.ontologia.dw;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import br.com.pojo.Region;

public class RegionHelper {
	
ArrayList<Region> listaDeCidades = null;
	
	public ArrayList<Region> consultarRegions(){
		//consultando todas as datas do DW
		String consulta = "select region_id, sales_city, sales_state_province from region where sales_state_province in ('CA', 'OR', 'WA')";
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			
			this.listaDeCidades= new ArrayList<>();
			
			Connection con = ConnectionDW.getInstance().getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(consulta);

			while(rs.next()){
				int id  = rs.getInt("region_id");
				String city = rs.getString("sales_city");
				String province = rs.getString("sales_state_province");
				
				listaDeCidades.add(new Region(id, city.replace(" ", "_"), province.replace(" ", "_")));

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
		return listaDeCidades;
	}
}
