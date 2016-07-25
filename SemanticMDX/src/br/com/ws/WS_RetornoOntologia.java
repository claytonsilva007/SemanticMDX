package br.com.ws;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;

import br.com.controlador.Controlador;
import br.com.pojo.ClassesOntologia;
import br.com.pojo.RetornoOntologia;

@Path("/dl")
public class WS_RetornoOntologia {
	
	@Path("{query}")
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
	
	@Path("{classes}")
	@GET
	@Produces("application/json")
	public ClassesOntologia consultarClasses() throws JSONException {
		Controlador mediador = new Controlador();
		ClassesOntologia classesOntologia = new ClassesOntologia();
		ArrayList<String> listaDeclassesOntologia = mediador.consultarTodasAsClassesOntologia();
		classesOntologia.setClasses(listaDeclassesOntologia);
		return classesOntologia;
	}
	
	/*@Path("{propriedades}")
	@GET
	@Produces("application/json")
	public PropriedadesOntologia consultarPropriedades() throws JSONException {
		Controlador mediador = new Controlador();
		PropriedadesOntologia propriedadesOntologia = new PropriedadesOntologia();
		ArrayList<String> ListaDePropriedadesOntologia = mediador.consultarPropriedadesOntologia();
		propriedadesOntologia.setPropriedades(ListaDePropriedadesOntologia);
		return propriedadesOntologia;
	}*/
}