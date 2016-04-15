package br.com.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.controlador.ControladorSemantico;

@Path("/query")
public class WS_SemanticQuery {
	@Path("{queryMDX}")
	@GET
	@Produces("application/json")
	public Response submeterConsultaMDX(@PathParam("queryMDX") String queryMDX) throws JSONException {

		JSONObject jsonObject = new JSONObject();

		ControladorSemantico mediador = new ControladorSemantico();
		String novaConsulta = mediador.submeterConsulta(queryMDX);

		jsonObject.put("O retorno da ontologia é:", novaConsulta);

		String result = "@Produces(\"application/json\") Output: \n\nF to C Converter Output: \n\n" + jsonObject;
		return Response.status(200).entity(result).build();
	}
}
