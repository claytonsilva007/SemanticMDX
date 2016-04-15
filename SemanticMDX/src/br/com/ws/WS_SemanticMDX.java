package br.com.ws;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.controlador.ControladorSemantico;
import br.com.controlador.ControladorSemanticoInterface;

@Path("/")
public class WS_SemanticMDX {
	@Path("{f}")
	@GET
	@Produces("application/json")
	public Response convertFtoCfromInput(@PathParam("f") String f) throws JSONException {

		JSONObject jsonObject = new JSONObject();

		ControladorSemantico mediador = new ControladorSemantico();
		String retorno = mediador.submeterConsulta(f);
		
		jsonObject.put("O retorno da ontologia é: ", "retorno");
		
		String result = "@Produces(\"application/json\") Output: \n\nF to C Converter Output: \n\n" + jsonObject;
		return Response.status(200).entity(result).build();
	}
}