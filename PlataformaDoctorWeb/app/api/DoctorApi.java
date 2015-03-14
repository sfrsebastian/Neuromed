package api;
import java.util.List;

import Excepciones.UsuarioException;
import models.*;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import com.fasterxml.jackson.databind.JsonNode;

public class DoctorApi extends Controller {

	@Transactional
	public static Result dar(Long idDoctor){
        Doctor doctor = JPA.em().find(Doctor.class, idDoctor);
        if(doctor != null) {
            return ok(doctor.toJson());
        }
        else {
            return ok("El doctor con identificación " + idDoctor + " no existe");
        }
	}
	
	@Transactional
	public static Result darTodos(){
		List<Doctor> doctores = JPA.em().createQuery("SELECT u FROM Doctor u", Doctor.class).getResultList();
		return ok(Usuario.listToJson(doctores,false));
	}
	
	@Transactional
	public static Result agregar(){
		try{
            Doctor nuevo = new Doctor(request().body().asJson());
			List<Usuario> usuarios = JPA.em().createQuery("SELECT u FROM Usuario u WHERE u.identificacion = ?1", Usuario.class).setParameter(1, nuevo.getIdentificacion()).getResultList();
			if(usuarios.size()>0){
				return ok("El usuario con identificacion " + nuevo.getIdentificacion() + " ya existe");
			}
			else{
				JPA.em().persist(nuevo);
				return ok(nuevo.toJson());
			}
		}
		catch(UsuarioException p){
			return ok(p.getMessage());
		}
	}

	@Transactional
	public static Result actualizar(Long idDoctor){
		JsonNode json = request().body().asJson();
		String password = json.findPath("password").textValue();
		String email = json.findPath("email").textValue();
		Doctor actual = JPA.em().find(Doctor.class, idDoctor);
        if(actual != null) {
            actual.setPassword(password);
            actual.setEmail(email);
            JPA.em().merge(actual);
            return ok(actual.toJson());
        }
        else {
            return ok("El doctor con identificacion " + idDoctor + " no existe en el sistema.");
        }
	}

	@Transactional
	public static Result eliminar(Long idDoctor){
        Doctor doctor = JPA.em().find(Doctor.class, idDoctor);
        if(doctor != null){
            JPA.em().remove(doctor);
            return ok("El doctor con identificacion " + idDoctor + " fue eliminado del sistema.");
        }
        else{
            return ok("El doctor con identificacion " + idDoctor + " no existe en el sistema.");
        }
	}

	@Transactional
	public static Result agregarPaciente(Long idDoctor){
		JsonNode json = request().body().asJson();
		Long idPaciente = json.findPath("idPaciente").asLong();
		Doctor doctor = JPA.em().find(Doctor.class, idDoctor);
        Paciente paciente = JPA.em().find(Paciente.class, idPaciente);
        if(doctor == null){
            return ok("El doctor con identificacion " + idDoctor + " no existe en el sistema.");
        }
        else if(paciente == null){
            return ok("El paciente con identificacion " + idPaciente + " no existe en el sistema.");
        }
        else if(paciente.getDoctor() != null){
            return ok("El paciente ya tiene un doctor asociado");
        }
        else{
            paciente.setDoctor(doctor);
            JPA.em().merge(paciente);
            return ok("Al paciente " + idPaciente +" se le asoció el doctor " + idDoctor + " exitosamente");
        }
	}
	
	@Transactional
	public static Result darPacientes(Long idDoctor){
        Doctor doctor = JPA.em().find(Doctor.class, idDoctor);
        if(doctor != null){
            List<Paciente> pacientes = JPA.em().createQuery("SELECT u FROM Paciente u WHERE u.doctor = ?1", Paciente.class).setParameter(1, doctor).getResultList();
            return ok(Usuario.listToJson(pacientes,false));
        }
        else{
            return ok("No existe doctor con el identificador dado");
        }
	}
	
	@Transactional
	public static Result crearComentario(Long idDoctor){
		JsonNode json = request().body().asJson();
		Long idEpisodio = json.findPath("idEpisodio").asLong();
		Doctor doctor = JPA.em().find(Doctor.class, idDoctor);
        Episodio episodio = JPA.em().find(Episodio.class, idEpisodio);
        if(doctor == null){
            return ok("El doctor con identificacion: " + idDoctor + " no existe en el sistema.");
        }
        else if(episodio == null){
            return ok("El episodio con identificacion: " + idEpisodio + " no existe en el sistema.");
        }
        else{
            Comentario comentario = new Comentario(json);
            comentario.setDoctor(doctor);
            comentario.setEpisodio(episodio);
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
	}
	
	@Transactional
	public static Result darComentarios(Long idDoctor){
		Doctor doctor = JPA.em().find(Doctor.class, idDoctor);
        if(doctor != null){
            List<Comentario> comentarios = JPA.em().createQuery("SELECT u FROM Comentario u WHERE u.doctor=?1",Comentario.class).setParameter(1, doctor).getResultList();
            return ok(Comentario.listToJson(comentarios));
        }
		else{
			return ok("El doctor con identificacion: " + idDoctor + " no existe en el sistema.");
		}
	}
	
	@Transactional
	public static Result darEpisodiosSegundaOpinion(Long idDoctor){
		Doctor doctor = JPA.em().find(Doctor.class, idDoctor);
        if(doctor != null){
			List<Episodio> episodios = JPA.em().createQuery("SELECT u FROM Episodio u WHERE (?1 MEMBER OF u.doctores)",Episodio.class).setParameter(1, doctor).getResultList();
			return ok(Episodio.listToJson(episodios));
		}
		else{
			return ok("El doctor con identificacion: " + idDoctor + " no existe en el sistema.");
		}
	}
}