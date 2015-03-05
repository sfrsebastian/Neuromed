package controllers;

import java.util.List;

import models.Comentario;
import models.Doctor;
import models.Episodio;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EpisodioController extends Controller {
	
	@Transactional
	public static Result getEpisodio(String idEpisodio){
		Episodio buscado = JPA.em().find(Episodio.class, Long.parseLong(idEpisodio));
		if(buscado != null){
			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode node = mapper.convertValue(buscado, JsonNode.class);
			return ok(node);
		}else{
			return ok("El episodio con identificacion " + idEpisodio + " no existe");
		}
	}

	@Transactional
	public static Result crearComentario(Long id){
		JsonNode json = request().body().asJson();
		Episodio actual = JPA.em().find(Episodio.class, id);
		Long idDoctor = json.path("idDoctor").asLong();
		Doctor doctor = JPA.em().find(Doctor.class, idDoctor);
		if(actual!=null){
			Comentario nuevo = Form.form(Comentario.class).bindFromRequest().get();
			JPA.em().persist(nuevo);
			actual.addComentario(nuevo);
			doctor.addComentario(nuevo);
			List<Comentario> comentarios = actual.getComentarios();
			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode node = mapper.convertValue(comentarios, JsonNode.class);
			JPA.em().merge(actual);
			JPA.em().merge(doctor);
			return ok(node);
		}	
		else{
			return ok("El episodio con identificacion: " + id+ " no existe en el sistema, o el doctor dado no existe.");
		}
	}
	
	
}
