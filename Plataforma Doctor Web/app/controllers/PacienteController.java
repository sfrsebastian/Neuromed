package controllers;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Doctor;
import models.Episodio;
import models.Paciente;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.*;
import views.html.*;

public class PacienteController extends Controller {

	public static Result index() {
		return ok();
	}

	@Transactional
	public static Result getPaciente(String idPaciente){
		Paciente buscado = JPA.em().find(Paciente.class, Long.parseLong(idPaciente));
		if(buscado != null){
			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode node = mapper.convertValue(buscado, JsonNode.class);
			return ok(node);
		}else{
			return ok("El paciente con identificacion " + idPaciente + " no existe");
		}
	}

	@Transactional
	public static Result getEpisodiosPeriodo(String idPaciente, String fecha1,String fecha2){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

		Date f1 = null;
		Date f2 = null;

		try {
			f1 = sdf.parse(fecha1);
			f2 = sdf.parse(fecha2);
		} catch (ParseException e) {
			System.out.println("Error en parser fecha, revisar formato");

			System.out.println(fecha1);
			System.out.println(fecha2);
			return ok("No se ha podido parsear las fechas dadas");
		}

		Paciente actual = JPA.em().find(Paciente.class, Long.parseLong(idPaciente));

		if(actual != null){
			List<Episodio> episodios = actual.getEpisodios();
			List<Episodio> ep = new ArrayList<Episodio>();

			for (Episodio episodio : episodios) {
				Date fechaActual = episodio.getFecha();
				if (fechaActual.after(f1) && fechaActual.before(f2))
					ep.add(episodio);
			}

			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode node = mapper.convertValue(ep, JsonNode.class);
			return ok(node);
		}else{
			return ok("No se ha podido encontrar el paciente dado");
		}
	}

	@Transactional
	public static Result agregarEpisodioPaciente(String idPaciente){
		Paciente actual = JPA.em().find(Paciente.class, Long.parseLong(idPaciente));
		if(actual != null){
			Episodio datos = Form.form(Episodio.class).bindFromRequest().get();
			JPA.em().persist(datos);
			actual.addEpisodio(datos);
			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode node = mapper.convertValue(actual, JsonNode.class);
			JPA.em().merge(actual);
			return ok(node);

		}else{
			return ok("No se ha podido encontrar el paciente dado");
		}
	}

	@Transactional
	public static Result actualizarPaciente(String identificacion){
		Paciente nuevo = Form.form(Paciente.class).bindFromRequest().get();
		Paciente actual = JPA.em().find(Paciente.class, Long.parseLong(identificacion));
		if(actual!=null){
			actual.setPassword(nuevo.getPassword());
			actual.setNombre(nuevo.getNombre());
			JPA.em().merge(actual);
			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode node = mapper.convertValue(actual, JsonNode.class);
			return ok(node);
		}	
		else{
			return ok("El paciente con identificacion: " + identificacion+ " no existe en el sistema.");
		}
	}

	@Transactional
	public static Result obtenerEpisodio(String idPaciente,String idEpisodio){

		Paciente actual = JPA.em().find(Paciente.class, Long.parseLong(idPaciente));

		if(actual != null){
			List<Episodio> episodios = actual.getEpisodios();
			Episodio ep=null;
			for(int i=0;i<episodios.size();i++){
				if(episodios.get(i).getId()==Long.parseLong(idEpisodio)){
					ep=episodios.get(i);
					break;
				}
			}

			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode node = mapper.convertValue(ep, JsonNode.class);
			return ok(node);
		}else{
			return ok("No se ha podido encontrar el paciente dado");
		}
	}

	@Transactional
	public static Result eliminarEpisodio(Long id){
		JsonNode json = request().body().asJson();
		if(json == null) {
			return badRequest("Se esperaban parámetros JSON");
		} 
		else {
			Long idEpisodio = json.findPath("idEpisodio").asLong();
			Paciente paciente = JPA.em().find(Paciente.class, id);	
			Episodio eliminar = paciente.eliminarEpisodio(idEpisodio);
			if(eliminar!=null){
				JPA.em().remove(eliminar);
				return ok("El episodio con id: " + id + " fue eliminado correctamente");
			}	
			else{
				return ok("El episodio con id: " + id + " no existe");
			}
		}
		
	}
	
	//Mario
	@Transactional
	public static Result crearPaciente(){
		Paciente nuevo = Form.form(Paciente.class).bindFromRequest().get();
		Paciente actual = null;
		try{
			actual = JPA.em().createQuery("SELECT u FROM Paciente u WHERE u.identificacion=?1",Paciente.class).setParameter(1, nuevo.getIdentificacion()).getSingleResult();
		}
		catch(Exception e){
			System.out.println("Error ejecutando query");
		}
		if(actual==null){
			JPA.em().persist(nuevo);			
			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode node = mapper.convertValue(nuevo, JsonNode.class);
			return ok(node);
		}	
		else{
			return ok("El paciente ya existe");
		}
	}

	//Mario
	@Transactional
	public static Result darTodosLosEpisodios(String idPaciente){
		Paciente actual = JPA.em().find(Paciente.class, Long.parseLong(idPaciente));
		if(actual != null){
			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode node = mapper.convertValue(actual.getEpisodios(), JsonNode.class);
			return ok(node);

		}
		else{
			return ok("El paciente no existe");
		}
	}

	@Transactional
	public static Result getPacientes(){
		List<Paciente> pacientes=JPA.em().createQuery("SELECT u FROM Paciente u",Paciente.class).getResultList();
		ObjectMapper mapper = new ObjectMapper(); 
		JsonNode node = mapper.convertValue(pacientes, JsonNode.class);
		return ok(node);
	}



}
