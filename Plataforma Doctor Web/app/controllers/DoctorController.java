package controllers;
import java.util.Calendar;
import java.util.List;

import models.Comentario;
import models.Doctor;
import models.Episodio;
import models.Medicamento;
import models.Paciente;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DoctorController extends Controller {

	public static Result index() {
		return ok();
	}

	@Transactional
	public static Result getDoctor(String idDoctor){
		Doctor buscado = JPA.em().find(Doctor.class, Long.parseLong(idDoctor));
		if(buscado != null){
			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode node = mapper.convertValue(buscado, JsonNode.class);
			return ok(node);
		}else{
			return ok("El doctor con identificacion " + idDoctor + " no existe");
		}
	}

	@Transactional
	public static Result agregarDoctor(){
		Doctor nuevo =Form.form(Doctor.class).bindFromRequest().get();
		Doctor actual = null;
		try{
			actual = JPA.em().createQuery("SELECT u FROM Doctor u WHERE u.identificacion=?1",Doctor.class).setParameter(1, nuevo.getIdentificacion()).getSingleResult();
		}
		catch(Exception e){

		}
		if(actual==null){
			JPA.em().persist(nuevo);
			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode node = mapper.convertValue(nuevo, JsonNode.class);
			return ok(node);
		}	
		else{
			return ok("El doctor con identificacion " + nuevo.getIdentificacion() + " ya existe");
		}
	}

	@Transactional
	public static Result actualizarDoctor(String identificacion){
		Doctor nuevo = Form.form(Doctor.class).bindFromRequest().get();
		Doctor actual = JPA.em().find(Doctor.class, Long.parseLong(identificacion));
		if(actual!=null){
			actual.setPassword(nuevo.getPassword());
			JPA.em().merge(actual);
			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode node = mapper.convertValue(actual, JsonNode.class);
			return ok(node);
		}	
		else{
			return ok("El doctor con identificacion: " + identificacion+ " no existe en el sistema.");
		}
	}


	@Transactional
	public static Result darComentarios(String identificacion){
		Doctor actual = JPA.em().find(Doctor.class, Long.parseLong(identificacion));
		if(actual!=null){
			Comentario nuevo = new Comentario();
			nuevo.setContenido("El paciente presenta una migraña");
			JPA.em().persist(nuevo);
			actual.addComentario(nuevo);
			List<Comentario> comentarios = actual.getComentarios();
			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode node = mapper.convertValue(comentarios, JsonNode.class);
			JPA.em().merge(actual);
			return ok(node);
		}	
		else{
			return ok("El doctor con identificacion: " + identificacion+ " no existe en el sistema.");
		}
	}

	@Transactional
	public static Result getPacienteDoctor(String idDoctor, String idPaciente){
		Doctor actual = JPA.em().find(Doctor.class, Long.parseLong(idDoctor));
		if (actual != null){

			List<Paciente> pacientes = actual.getPacientes();

			Paciente abuscar = null;

			for (Paciente paciente : pacientes) {
				if (paciente.getIdentificacion().equals(idPaciente))
					abuscar = paciente;
			}

			if(abuscar != null){
				ObjectMapper mapper = new ObjectMapper(); 
				JsonNode node = mapper.convertValue(abuscar, JsonNode.class);
				return ok(node);
			}else{
				return ok("No existe paciente asociado con el identificador del doctor dado");
			}
		}
		else{
			return ok("No existe doctor con el identificador dado");
		}
	}

	@Transactional
	public static Result darSegundasOpiniones(String identificacion){
		Doctor actual = JPA.em().find(Doctor.class, Long.parseLong(identificacion));
		if(actual!=null){
			Episodio nuevo = new Episodio();
			nuevo.setNivelDolor(10);
			nuevo.setLocalizacion("Lobulo Occipital");
			nuevo.setFecha(Calendar.getInstance().getTime());
			Medicamento med = new Medicamento();
			med.setNombre("Acetaminofen");
			med.setMarca("Dolex");
			JPA.em().persist(med);
			nuevo.addMedicamento(med);
			JPA.em().persist(nuevo);
			actual.addSegundaOpinion(nuevo);
			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode node = mapper.convertValue(actual.getSegundasOpiniones(), JsonNode.class);
			JPA.em().merge(actual);
			return ok(node);
		}	
		else{
			return ok("El doctor con identificacion: " + identificacion+ " no existe en el sistema.");
		}
	}

	@Transactional
	public static Result publicarSegundaOpinionEpisodio(String idDoctor,String idColega){
		Episodio episodio = Form.form(Episodio.class).bindFromRequest().get();
		Episodio encontrado = JPA.em().find(Episodio.class, episodio.getId());

		if (encontrado != null){

			Doctor actual = JPA.em().find(Doctor.class, Long.parseLong(idDoctor));
			if(actual != null){
				List<Doctor> colegas = actual.getColegas();

				Doctor temp = null;
				for (Doctor doctor : colegas) {
					if(doctor.getIdentificacion().equals(idColega))
						temp = doctor;
				}

				if(temp != null){
					temp.addSegundaOpinion(encontrado);
					JPA.em().merge(temp);
					ObjectMapper mapper = new ObjectMapper(); 
					JsonNode node = mapper.convertValue(temp, JsonNode.class);
					return ok(node);
				}
				else{
					return ok("No existe colega asociado con el identificador del doctor dado");
				}
			}
			else{
				return ok("No existe doctor con el identificador dado");
			}

		}else{
			return ok("No existe episodio con el identificador: " + episodio.getId());
		}

	}

	@Transactional
	public static Result darPacientes(String identificacion){
		Doctor actual = JPA.em().find(Doctor.class, Long.parseLong(identificacion));
		if(actual!=null){
			List<Paciente> pacientes=actual.getPacientes();
			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode node = mapper.convertValue(pacientes, JsonNode.class);
			return ok(node);
		}	
		else{
			return ok("El doctor con identificacion: " + identificacion+ " no existe en el sistema.");
		}
	}

	@Transactional
	public static Result agregarColega(String idDoc,String idColega){
		Doctor actual = JPA.em().find(Doctor.class, Long.parseLong(idDoc));
		if(actual!=null){
			Doctor colega = JPA.em().find(Doctor.class, Long.parseLong(idColega));
			if(colega!=null){
				actual.addColega(colega);;
				JPA.em().merge(actual);
				ObjectMapper mapper = new ObjectMapper(); 
				JsonNode node = mapper.convertValue(actual, JsonNode.class);
				return ok(node);
			}else{
				return ok("El doctor con identificacion: " + idColega+ " no existe en el sistema.");
			}
		}	
		else{
			return ok("El doctor con identificacion: " + idDoc+ " no existe en el sistema.");
		}
	}

	@Transactional
	public static Result autorizarDoctor(String identificacion){
		Doctor actual = JPA.em().find(Doctor.class, Long.parseLong(identificacion));
		if(actual!=null){
			actual.setAutorizado(true);
			JPA.em().merge(actual);
			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode node = mapper.convertValue(actual, JsonNode.class);
			return ok(node);
		}	
		else{
			return ok("El doctor con identificacion: " + identificacion+ " no existe en el sistema.");
		}
	}



	// Mario
	@Transactional
	public static Result agregarPaciente(String idDoctor){
		Paciente nuevo = Form.form(Paciente.class).bindFromRequest().get();
		Paciente pac = JPA.em().find(Paciente.class, Long.parseLong(nuevo.getIdentificacion()));
		Doctor actual = JPA.em().find(Doctor.class, Long.parseLong(idDoctor));
		if(actual!=null && pac != null && !actual.poseePaciente(pac.getIdentificacion())){
			actual.getPacientes().add(pac);
			JPA.em().merge(actual);
			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode node = mapper.convertValue(actual, JsonNode.class);
			return ok(node);
		}	
		else{
			return ok("El doctor con identificacion: " + idDoctor+ " no existe en el sistema o ya posee al paciente.");
		}
	}

	//Mario 
	@Transactional    
	public static Result eliminarComentario(String idDoctor){

		Comentario nuevo = Form.form(Comentario.class).bindFromRequest().get();
		Long idComentario = nuevo.getId();
		Doctor doc = JPA.em().find(Doctor.class, Long.parseLong(idDoctor));


		if(doc != null){
			if(doc != null && doc.eliminarComentario(idComentario)){
				JPA.em().remove(JPA.em().find(Comentario.class, idComentario));
				ObjectMapper mapper = new ObjectMapper();  
				JsonNode node = mapper.convertValue(doc, JsonNode.class);
				return ok(node);
			}
			else{
				return ok("El doctor con identificacion: " + idDoctor+ " no existe en el sistema o no posee el comentario.");
			}
		}else{
			return ok("El doctor con identificacion: " + idDoctor+ " no existe en el sistema o no posee el comentario.");
		}

	}

	@Transactional
	public static Result getDoctores(){
		List<Doctor> doctores=JPA.em().createQuery("SELECT u FROM Doctor u",Doctor.class).getResultList();
		ObjectMapper mapper = new ObjectMapper(); 
		JsonNode node = mapper.convertValue(doctores, JsonNode.class);
		return ok(node);
	}

}
