package controllers;
import java.util.List;

import models.Comentario;
import models.Doctor;
import models.Episodio;
import models.Paciente;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import Excepciones.DoctorException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class DoctorController extends Controller {

	@Transactional
	public static Result dar(Long idDoctor){
		try{
			Doctor buscado = JPA.em().find(Doctor.class, idDoctor);
			return ok(buscado.toJson());
		}
		catch(Exception e){
			return ok("El doctor con identificacion " + idDoctor + " no existe");
		}
	}
	
	@Transactional
	public static Result darTodos(){
		List<Doctor> doctores=JPA.em().createQuery("SELECT u FROM Doctor u",Doctor.class).getResultList();
		return ok(doctoresToJson(doctores));
	}
	
	@Transactional
	public static Result agregar(){
		Doctor nuevo = null;
		try{
			nuevo = new Doctor(request().body().asJson());
			List<Doctor>doctores = JPA.em().createQuery("SELECT u FROM Doctor u WHERE u.identificacion=?1",Doctor.class).setParameter(1, nuevo.getIdentificacion()).getResultList();
			if(doctores.size()>0){
				return ok("El doctor con identificacion " + nuevo.getIdentificacion() + " ya existe");
			}
			else{
				JPA.em().persist(nuevo);
				return ok(nuevo.toJson());	
			}
		}
		catch(DoctorException p){
			return ok(p.getMessage());
		}
	}

	@Transactional
	public static Result actualizar(Long idDoctor){
		JsonNode json = request().body().asJson();
		String password=json.findPath("password").textValue();
		String email = json.findPath("email").textValue();
		Doctor actual = null;
		try{
			actual=JPA.em().find(Doctor.class, idDoctor);
			actual.setPassword(password);
			actual.setEmail(email);
			JPA.em().merge(actual);
			return ok(actual.toJson());
		}
		catch(Exception e){
			return ok("El doctor con identificacion: " + idDoctor + " no existe en el sistema.");
		}
	}

	@Transactional
	public static Result eliminar(Long identificacion){
		try{
			Doctor actual = JPA.em().find(Doctor.class, identificacion);
			JPA.em().remove(actual);
			return ok(actual.toJson());
		}
		catch(Exception e){
			return ok("El doctor con identificacion: " + identificacion + " no existe en el sistema.");
		}
	}

	@Transactional
	public static Result agregarPaciente(Long idDoctor){
		JsonNode json = request().body().asJson();
		Long idPaciente = json.findPath("idPaciente").asLong();
		Doctor doctor=null;
		try{
			doctor = JPA.em().find(Doctor.class, idDoctor);
		}
		catch(Exception e){
			return ok("El doctor con identificacion: " + idDoctor + " no existe en el sistema.");
		}
		try{
			Paciente paciente = JPA.em().find(Paciente.class,idPaciente);
			if(paciente.getDoctor()==null){
				paciente.setDoctor(doctor);
				JPA.em().merge(paciente);
				return ok("Al paciente " + idPaciente +" se le asoció el doctor " + idDoctor + " exitosamente");
			}
			else{
				return ok("El paciente ya tiene un doctor asociado");
			}
			
		}
		catch(Exception e){
			return ok("El paciente con identificacion: " + idPaciente + " no existe en el sistema.");
		}
	}
	
	@Transactional
	public static Result darPacientes(Long idDoctor){
		try{
			Doctor doctor = JPA.em().find(Doctor.class, idDoctor);
			List<Paciente> pacientes = JPA.em().createQuery("SELECT u FROM Paciente u WHERE u.doctor=?1",Paciente.class).setParameter(1, doctor).getResultList();
			return ok(pacientesToJson(pacientes));
		}
		catch(Exception e){
			return ok("No existe doctor con el identificador dado");
		}
	}
	
	@Transactional
	public static Result crearComentario(Long idDoctor){
		JsonNode json = request().body().asJson();
		Long idEpisodio = json.findPath("idEpisodio").asLong();
		Doctor doctor = null;
		try{
			doctor = JPA.em().find(Doctor.class, idDoctor);
		}
		catch(Exception e){
			return ok("El doctor con identificacion: " + idDoctor + " no existe en el sistema.");
		}
		try{
			Comentario comentario = new Comentario(json);
			comentario.setDoctor(doctor);
			Episodio episodio = JPA.em().find(Episodio.class, idEpisodio);
			if(episodio.contieneDoctor(doctor)){
				JPA.em().persist(comentario);
				episodio.addComentario(comentario);
				JPA.em().merge(episodio);
				return ok(episodio.toJson());
			}
			else{
				return ok("El medico con identificacion: " + idDoctor + " no esta autorizado para comentar el episodio " + idEpisodio);
			}
		}
		catch(Exception e){
			return ok("El episodio con identificacion: " + idEpisodio + " no existe en el sistema.");
		}
	}
	
	@Transactional
	public static Result darComentarios(Long idDoctor){
		Doctor doctor = null;
		try{
			doctor = JPA.em().find(Doctor.class, idDoctor);
			List<Comentario> comentarios = JPA.em().createQuery("SELECT u FROM Comentario u WHERE u.doctor=?1",Comentario.class).setParameter(1, doctor).getResultList();
			return ok(comentariosToJson(comentarios));
		}
		catch(Exception e){
			return ok("El doctor con identificacion: " + idDoctor + " no existe en el sistema.");
		}
	}
	
	@Transactional
	public static Result darEpisodiosSegundaOpinion(Long idDoctor){
		Doctor doctor = null;
		try{
			doctor = JPA.em().find(Doctor.class, idDoctor);
			List<Episodio> episodios = JPA.em().createQuery("SELECT u FROM Episodio u WHERE (?1 MEMBER OF u.doctores)",Episodio.class).setParameter(1, doctor).getResultList();
			return ok(episodiosToJson(episodios));
		}
		catch(Exception e){
			return ok("El doctor con identificacion: " + idDoctor + " no existe en el sistema.");
		}
	}

	private static ArrayNode doctoresToJson(List<Doctor> doctores){
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ArrayNode array = new ArrayNode(factory);
		for (Doctor p : doctores) {
			array.add(p.toJson());
		}
		return array;
	}
	
	private static ArrayNode pacientesToJson(List<Paciente> pacientes){
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ArrayNode array = new ArrayNode(factory);
		for (Paciente p : pacientes) {
			array.add(p.toJson());
		}
		return array;
	}
	
	private static ArrayNode comentariosToJson(List<Comentario> comentarios){
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ArrayNode array = new ArrayNode(factory);
		for (Comentario p : comentarios) {
			array.add(p.toJson());
		}
		return array;
	}
	
	private static ArrayNode episodiosToJson(List<Episodio> episodios){
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ArrayNode array = new ArrayNode(factory);
		for (Episodio p : episodios) {
			array.add(p.toJson());
		}
		return array;
	}
}