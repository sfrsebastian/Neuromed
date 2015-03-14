package api;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Excepciones.EpisodioException;
import Excepciones.UsuarioException;

import com.fasterxml.jackson.databind.JsonNode;

import models.Doctor;
import models.Episodio;
import models.Paciente;
import models.Usuario;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.*;

public class PacienteApi extends Controller {

	@Transactional
	public static Result dar(Long idPaciente){
        Paciente paciente = JPA.em().find(Paciente.class, idPaciente);
        if(paciente != null) {
            return ok(paciente.toJson());
        }
        else {
            return ok("El paciente con identificación " + idPaciente + " no existe");
        }
	}

    @Transactional
    public static Result darPorCedula(String cedula){
        List<Paciente> pacientes = JPA.em().createQuery("SELECT u FROM Paciente u WHERE u.identificacion = ?1", Paciente.class).setParameter(1, cedula).getResultList();
        if(pacientes.size() > 0) {
            return ok(pacientes.get(0).toJson());
        }
        else {
            return ok("El paciente con cedula " + cedula + " no existe");
        }
    }

	@Transactional
	public static Result darTodos(){
		List<Paciente> pacientes = JPA.em().createQuery("SELECT u FROM Paciente u", Paciente.class).getResultList();
		return ok(Usuario.listToJson(pacientes,false));
	}

	@Transactional
	public static Result agregar(){
        try{
            Paciente nuevo = new Paciente(request().body().asJson());
            List<Usuario> usuarios = JPA.em().createQuery("SELECT u FROM Usuario u WHERE u.identificacion = ?1 ", Usuario.class).setParameter(1, nuevo.getIdentificacion()).getResultList();
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
	public static Result actualizar(Long idPaciente){
        JsonNode json = request().body().asJson();
        String password = json.findPath("password").textValue();
        String email = json.findPath("email").textValue();
        Paciente actual = JPA.em().find(Paciente.class, idPaciente);
        if(actual != null) {
            actual.setPassword(password);
            actual.setEmail(email);
            JPA.em().merge(actual);
            return ok(actual.toJson());
        }
        else {
            return ok("El paciente con identificacion " + idPaciente + " no existe en el sistema.");
        }
	}

	@Transactional
	public static Result eliminar(Long idPaciente){
        Paciente paciente = JPA.em().find(Paciente.class, idPaciente);
        if(paciente != null){
            JPA.em().remove(paciente);
            return ok("El paciente con identificacion " + idPaciente + " fue eliminado del sistema.");
        }
        else{
            return ok("El paciente con identificacion " + idPaciente + " no existe en el sistema.");
        }
	}

	@Transactional
	public static Result agregarEpisodio(Long idPaciente){
		JsonNode json = request().body().asJson();
        Paciente paciente = JPA.em().find(Paciente.class, idPaciente);
        if(paciente != null){
            if(paciente.getDoctor() != null){
                try{
                    Episodio episodio = new Episodio(json);
                    episodio.setDoctor(paciente.getDoctor());
                    episodio.setPaciente(paciente);
                    JPA.em().persist(episodio);
                    paciente.addEpisodio(episodio);
                    JPA.em().merge(paciente);
                    return ok(paciente.toJson());
                }
                catch(EpisodioException a){
                    return ok(a.getMessage());
                }
            }
            else {
                return ok("El paciente con identificacion: " + idPaciente + " no tiene asignado a un doctor.");
            }
        }
        else{
            return ok("El paciente con identificacion " + idPaciente + " no existe en el sistema.");
        }

	}

	@Transactional
	public static Result eliminarEpisodio(Long idPaciente, Long idEpisodio){
		Paciente paciente = JPA.em().find(Paciente.class, idPaciente);
		Episodio episodio = JPA.em().find(Episodio.class, idEpisodio);
        if(paciente == null){
            return ok("El paciente con identificacion: " + idPaciente + " no existe en el sistema.");
        }
        else if(episodio == null){
            return ok("El episodio con identificacion: " + idEpisodio + " no existe en el sistema.");
        }
		else if(paciente.eliminarEpisodio(episodio)){
			JPA.em().merge(paciente);
            JPA.em().remove(episodio);
			return ok("El episodio fue eliminado");
		}
		else{
			return ok("El episodio con id: " + idEpisodio + " no hace parte de los episodios del paciente");
		}
	}
	
	@Transactional
	public static Result darEpisodio(Long idPaciente,Long idEpisodio){
        Paciente paciente = JPA.em().find(Paciente.class, idPaciente);
        Episodio episodio = JPA.em().find(Episodio.class, idEpisodio);
        if(paciente == null){
            return ok("El paciente con identificacion: " + idPaciente + " no existe en el sistema.");
        }
        else if(episodio == null){
            return ok("El episodio con identificacion: " + idEpisodio + " no existe en el sistema.");
        }
		else if(paciente.contieneEpisodio(episodio)){
			return ok(episodio.toJson());
		}
		else{
			return ok("El episodio con id: " + idEpisodio + " no hace parte de los episodios del paciente");
		}
	}
	
	@Transactional
	public static Result darTodosLosEpisodios(Long idPaciente){
        Paciente actual = JPA.em().find(Paciente.class,idPaciente);
        if(actual != null){
            List<Episodio> episodios = actual.getEpisodios();
            return ok(Episodio.listToJson(episodios));
        }
        else{
            return ok("El paciente con identificacion: " + idPaciente + " no existe en el sistema.");
        }
	}
	
	@Transactional
	public static Result darEpisodiosPorFecha(Long idPaciente, String inic, String fi){
        Paciente paciente = JPA.em().find(Paciente.class, idPaciente);
        if(paciente != null){
            try {
                Date inicio = stringToDate(inic);
                Date fin = stringToDate(fi);
                List<Episodio> episodios = JPA.em().createQuery("SELECT e FROM Episodio e WHERE e.paciente = ?1 AND e.fecha BETWEEN ?2 AND ?3", Episodio.class).setParameter(1, paciente).setParameter(2, inicio).setParameter(3, fin).getResultList();
                return ok(Episodio.listToJson(episodios));
            }
            catch(Exception e){
                return ok(e.getMessage());
            }
        }
        else{
            return ok("El paciente con identificacion: " + idPaciente + " no existe en el sistema.");
        }
	}
	
	//REVISAR POR SESION SI QUIEN SOLICITA ES PARTE DEL EPISODIO
	@Transactional
	public static Result agregarDoctorAEpisodio(Long idPaciente,Long idEpisodio){
		JsonNode json = request().body().asJson();
		Long idDoctor = json.path("idDoctor").asLong();
        Paciente paciente = JPA.em().find(Paciente.class, idPaciente);
        Episodio episodio = JPA.em().find(Episodio.class, idEpisodio);
        Doctor doctor = JPA.em().find(Doctor.class, idDoctor);
        if(paciente == null){
            return ok("El paciente con identificacion: " + idPaciente + " no existe en el sistema.");
        }
        else if(episodio == null){
            return ok("El episodio con identificacion: " + idEpisodio + " no existe en el sistema.");
        }
        else if(doctor == null){
            return ok("El doctor con identificacion: " + idDoctor + " no existe en el sistema.");
        }
        else if(paciente.contieneEpisodio(episodio) && !episodio.contieneDoctor(doctor)){
			episodio.addDoctor(doctor);
			JPA.em().merge(episodio);
			return ok("El doctor fue agregado al episodio");
		}
		else{
			return ok("El episodio con id: " + idEpisodio + " no hace parte de los episodios del paciente");
		}
	}

    private static Date stringToDate(String date) throws Exception {
        try {
            DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            return formatter.parse(date);
        }
        catch (ParseException e) {
            throw new Exception("Error interpretando la fecha");
        }
    }
}
