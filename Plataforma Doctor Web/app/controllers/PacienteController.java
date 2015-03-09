package controllers;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Excepciones.EpisodioException;
import Excepciones.PacienteException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import models.Doctor;
import models.Episodio;
import models.Paciente;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.*;

public class PacienteController extends Controller {

	@Transactional
	public static Result dar(Long idPaciente){
		try{
			Paciente buscado = JPA.em().find(Paciente.class, idPaciente);
			return ok(buscado.toJson());
		}
		catch(Exception e){
			return ok("El paciente con identificacion " + idPaciente + " no existe");
		}
	}

	@Transactional
	public static Result darTodos(){
		List<Paciente> doctores=JPA.em().createQuery("SELECT u FROM Paciente u",Paciente.class).getResultList();
		return ok(pacientesToJson(doctores));
	}

	@Transactional
	public static Result agregar(){
		Paciente nuevo = null;
		try{
			nuevo = new Paciente(request().body().asJson());
			List<Paciente> pacientes = JPA.em().createQuery("SELECT u FROM Paciente u WHERE u.identificacion=?1",Paciente.class).setParameter(1, nuevo.getIdentificacion()).getResultList();
			if(pacientes.size()>0){
				return ok("El paciente con identificacion " + nuevo.getIdentificacion() + " ya existe");
			}
			else{
				JPA.em().persist(nuevo);
				return ok(nuevo.toJson());
			}
		}
		catch(PacienteException p){
			return ok(p.getMessage());
		}
	}

	@Transactional
	public static Result actualizar(Long idPaciente){
		JsonNode json = request().body().asJson();
		//Unicamente se puede actualizar la contraseña o correo.
		String password=json.findPath("password").textValue();
		String email = json.findPath("email").textValue();
		Paciente actual = null;
		try{
			actual=JPA.em().find(Paciente.class, idPaciente);
			actual.setPassword(password);
			actual.setEmail(email);
			JPA.em().merge(actual);
			return ok(actual.toJson());
		}
		catch(Exception e){
			return ok("El paciente con identificacion: " + idPaciente + " no existe en el sistema.");
		}
	}

	@Transactional
	public static Result eliminar(Long idPaciente){
		try{
			Paciente actual = JPA.em().find(Paciente.class, idPaciente);
			JPA.em().remove(actual);
			return ok(actual.toJson());
		}
		catch(Exception e){
			return ok("El paciente con identificacion: " + idPaciente + " no existe en el sistema.");
		}
	}

	@Transactional
	public static Result agregarEpisodio(Long idPaciente){
		JsonNode json = request().body().asJson();
		try{
			Paciente paciente = JPA.em().find(Paciente.class, idPaciente);
			if(paciente.getDoctor()!=null){
				Episodio episodio = new Episodio(json);
				episodio.setDoctor(paciente.getDoctor());
				JPA.em().persist(episodio);
				paciente.addEpisodio(episodio);
				JPA.em().merge(paciente);
				return ok(paciente.toJson());
			}
			else{
				return ok("El paciente con identificacion: " + idPaciente + " no tiene asignado a un doctor.");
			}
		}
		catch(EpisodioException a){
			return ok(a.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			return ok("El paciente con identificacion: " + idPaciente + " no existe en el sistema.");
		}
	}

	@Transactional
	public static Result eliminarEpisodio(Long idPaciente,Long idEpisodio){
		Paciente paciente=null;
		Episodio episodio=null;
		try{
			paciente = JPA.em().find(Paciente.class, idPaciente);
		}
		catch(Exception e){
			return ok("El paciente con identificacion: " + idPaciente + " no existe en el sistema.");
		}
		try{
			episodio = JPA.em().find(Episodio.class, idEpisodio);
		}
		catch(Exception e){
			return ok("El episodio con identificacion: " + idEpisodio + " no existe en el sistema.");
		}
		
		if(paciente!=null && paciente.eliminarEpisodio(episodio)){
			JPA.em().remove(episodio);
			return ok(paciente.toJson());
		}
		else{
			return ok("El episodio con id: " + idEpisodio + " no hace parte de los episodios del paciente");
		}
	}
	
	@Transactional
	public static Result darEpisodio(Long idPaciente,Long idEpisodio){
		Paciente paciente=null;
		Episodio episodio=null;
		try{
			paciente = JPA.em().find(Paciente.class, idPaciente);
		}
		catch(Exception e){
			return ok("El paciente con identificacion: " + idPaciente + " no existe en el sistema.");
		}
		try{
			episodio = JPA.em().find(Episodio.class, idEpisodio);
		}
		catch(Exception e){
			return ok("El episodio con identificacion: " + idEpisodio + " no existe en el sistema.");
		}
		if(paciente.tieneEpisodio(episodio)){
			return ok(episodio.toJson());
		}
		else{
			return ok("El episodio con id: " + idEpisodio + " no hace parte de los episodios del paciente");
		}
	}
	
	@Transactional
	public static Result darTodosLosEpisodios(Long idPaciente){
		try{
			Paciente actual = JPA.em().find(Paciente.class,idPaciente);
			List<Episodio> episodios = actual.getEpisodios();
			return ok(episodiosToJson(episodios));
		}
		catch(Exception e){
			return ok("El paciente con identificacion: " + idPaciente + " no existe en el sistema.");
		}
	}
	
	@Transactional
	public static Result darEpisodiosPorFecha(Long idPaciente,String inic, String fi){
		try{
			Paciente paciente = JPA.em().find(Paciente.class, idPaciente);
			Date inicio=stringToDate(inic);
			Date fin = stringToDate(fi);
			return ok(episodiosToJson(paciente.getEpisodios(inicio,fin)));
		}
		catch(PacienteException a){
			return ok(a.getMessage());
		}
		catch(Exception e){
			return ok("El paciente con identificacion: " + idPaciente + " no existe en el sistema.");
		}
	}
	
	//REVISAR POR SESION SI QUIEN SOLICITA ES PARTE DEL EPISODIO
	@Transactional
	public static Result agregarDoctorAEpisodio(Long idPaciente,Long idEpisodio){
		JsonNode json = request().body().asJson();
		Paciente paciente=null;
		Episodio episodio=null;
		Long idDoctor = json.path("idDoctor").asLong();
		Doctor doctor=null;
		try{
			paciente = JPA.em().find(Paciente.class, idPaciente);
		}
		catch(Exception e){
			return ok("El paciente con identificacion: " + idPaciente + " no existe en el sistema.");
		}
		try{
			episodio = JPA.em().find(Episodio.class, idEpisodio);
		}
		catch(Exception e){
			return ok("El episodio con identificacion: " + idEpisodio + " no existe en el sistema.");
		}
		try{
			doctor = JPA.em().find(Doctor.class, idDoctor);
		}
		catch(Exception e){
			return ok("El doctor con identificacion " + idDoctor + " no existe");
		}
		if(paciente.tieneEpisodio(episodio) && !episodio.contieneDoctor(doctor)){
			episodio.addDoctor(doctor);
			JPA.em().merge(episodio);
			return ok("El doctor fue agregado al episodio");
		}
		else{
			return ok("El episodio con id: " + idEpisodio + " no hace parte de los episodios del paciente");
		}
		
	}

	private static ArrayNode episodiosToJson(List<Episodio> episodios){
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ArrayNode array = new ArrayNode(factory);
		for (Episodio p : episodios) {
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
	
	private static Date stringToDate(String date) throws PacienteException{
		try {
			DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy"); 
			return formatter.parse(date);
		} 
		catch (ParseException e) {
			throw new PacienteException("Error interpretando la fecha");
		}
	}
}
