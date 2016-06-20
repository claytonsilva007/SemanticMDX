import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class ConsumerTeste {
	
	public String consumirWsReescritaMDX(String mdx){
		Client client = Client.create();
		
		WebResource r = client.resource("http://localhost:8080/SemanticMDX/query/mdx");

	    MultivaluedMap<String, String> params = new MultivaluedMapImpl();
	    try {
			params.add("queryMDX", URLEncoder.encode(mdx, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    
	    String response = r.path("").
	        queryParams(params).
	        get(String.class);
	    
		return response;
	}
	
	public static void main(String[] args) {
		ConsumerTeste teste = new ConsumerTeste();
		teste.consumirWsReescritaMDX("SELECT {[Measures].[Unit Sales]} ON COLUMNS, {[Time.Weekly].[1997].@FeriadaoFimDeSemana@} ON ROWS FROM [Sales]");
	}
}
