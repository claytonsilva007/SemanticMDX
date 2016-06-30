package br.com.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.json.JSONException;

import br.com.controlador.Controlador;

@Path("/query")
public class WS_SemanticQuery {
	@Path("{queryMDX}")
	@GET
	@Produces("application/json")
	public Response submeterConsultaMDX(@QueryParam("queryMDX") String queryMDX) throws JSONException {
		Controlador mediador = new Controlador();
		String novaConsulta = mediador.submeterConsulta(queryMDX);
		return Response.status(200).entity(novaConsulta).build();
	}
}
