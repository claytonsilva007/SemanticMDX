package br.com.olap;

/*import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.olap4j.Axis;
import org.olap4j.CellSet;
import org.olap4j.OlapConnection;
import org.olap4j.OlapStatement;
import org.olap4j.OlapWrapper;
import org.olap4j.layout.RectangularCellSetFormatter;
import org.olap4j.mdx.IdentifierNode;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.Member;
import org.olap4j.query.Query;
import org.olap4j.query.QueryDimension;
import org.olap4j.query.Selection;*/

public class AssistenteConsultaOlap {
	
	/*public void submeterConsulta(String consultaMDX){
		try {
			Class.forName("mondrian.olap4j.MondrianOlap4jDriver");
			Connection connection  = DriverManager.getConnection(
					"jdbc:mondrian:"+
					"Jdbc=jdbc:mysql://localhost:3306/foodmart?user=root&password=root;" +
					"JdbcDrivers=com.mysql.jdbc.Driver;" + 
					"Catalog= C:/opt/Acessórios/FoodMart.xml;");

			OlapWrapper wrapper = (OlapWrapper) connection;

			OlapConnection olapConnection = (OlapConnection) wrapper.unwrap(OlapConnection.class);
			OlapStatement statement = olapConnection.createStatement();
			
			CellSet cellSet =
				    statement.executeOlapQuery(
				        "SELECT {[Measures].[Unit Sales]} ON COLUMNS,\n"
				        + "  {[Product].Members} ON ROWS\n"
				        + "FROM [Sales]");
			
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		public void metodoTeste(){
			
			try {
				Class.forName("mondrian.olap4j.MondrianOlap4jDriver");
				Connection connection  = DriverManager.getConnection(
						"jdbc:mondrian:"+
						"Jdbc=jdbc:mysql://localhost:3306/foodmart?user=root&password=root;" +
						"JdbcDrivers=com.mysql.jdbc.Driver;" + 
						"Catalog= C:/opt/Acessórios/FoodMart.xml;");

				OlapWrapper wrapper = (OlapWrapper) connection;

				OlapConnection olapConnection = (OlapConnection) wrapper.unwrap(OlapConnection.class);
				OlapStatement statement = olapConnection.createStatement();

			
			// Get a cube object.
			Cube salesCube = 
					olapConnection
					.getOlapSchema()
					.getCubes()
					.get("Sales");

			// Build a query object.
			Query myQuery =
					new Query(
							"myQuery", 
							salesCube);

			// Lookup some dimensions
			QueryDimension productDim = myQuery.getDimension("Product");
			QueryDimension storeDim = myQuery.getDimension("Store");
			QueryDimension timeDim = myQuery.getDimension("Time");


			// Place dimensions on some axis
			myQuery.getAxis(Axis.COLUMNS).addDimension(productDim);
			myQuery.getAxis(Axis.ROWS).addDimension(storeDim);
			myQuery.getAxis(Axis.FILTER).addDimension(timeDim);


			// Including a member by metadata
			Member year1997 =
					salesCube.lookupMember(
							IdentifierNode.ofNames("Time", "1997")
							.getSegmentList());
			timeDim.include(year1997);


			// Including a member by name parts
			productDim.include(
					Selection.Operator.CHILDREN,
					IdentifierNode.ofNames(
							"Product", 
							"Drink",
							"Beverages")
							.getSegmentList());


			// We can also exclude members
			productDim.exclude(
					IdentifierNode.ofNames(
							"Product", 
							"Drink", 
							"Beverages",
							"Carbonated Beverages")
							.getSegmentList());

			// Validate this query
			myQuery.validate();

			// Print!
			//System.out.println("/********************* QUERY ***********************"); 

			System.out.println(myQuery.getSelect().toString());

			System.out.println("/********************* QUERY ***********************");
			System.out.println(" ");
			System.out.println(" ");
			System.out.println(" ");
			System.out.println(" ");

			System.out.println("/********************* RESULTS ***********************");

			RectangularCellSetFormatter formatter =
					new RectangularCellSetFormatter(false);

			PrintWriter writer = new PrintWriter(System.out);

			formatter.format(
					myQuery.execute(), 
					writer);

			writer.flush();
			System.out.println("/********************* RESULTS ***********************");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		AssistenteConsultaOlap fc = new AssistenteConsultaOlap();
		fc.submeterConsulta("");
	}*/
}