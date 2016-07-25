package br.com.ws;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;

import br.com.controlador.Controlador;
import br.com.pojo.RetornoOntologia;

public class WS_ObjectFactory {
	@Path("/factory")
	public class WS_RetornoOntologia {
		
		@Path("{objects}")
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		public RetornoOntologia submeterConsultaMDX(String query) throws JSONException {
			Controlador mediador = new Controlador();

			RetornoOntologia retornoOntologia = new RetornoOntologia();
			retornoOntologia.setInstances(mediador.consultarInstancias(query));
			retornoOntologia.setClasses(mediador.consultarSuperClasses(query));
			retornoOntologia.setEquivalentClasses(mediador.consultarEquivalentClasses(query));
			retornoOntologia.setSuperClasses(mediador.consultarSuperClasses(query));
			
			return retornoOntologia;
		}
	}
}
