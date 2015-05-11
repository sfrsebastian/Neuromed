package api;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import actions.*;
import com.fasterxml.jackson.databind.JsonNode;
import exceptions.EpisodioException;
import exceptions.StatusMessages;
import exceptions.UsuarioException;
import models.*;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.*;
import security.SecuredDoctor;
import security.SecuredGlobal;
import security.SecuredPaciente;
import security.SecurityController;

@CorsComposition.Cors
@ForceHttps.Https
@IntegrityCheck.Integrity
public class PacienteApi extends Controller {

    @Security.Authenticated(SecuredGlobal.class)
    @Transactional
	public static Result dar(Long idPaciente){
        if(SecurityController.validateOnlyMeWithDoctor(SecurityController.getUser())) {
            response().setHeader("Response-Syle", "Json-Object");
            Paciente paciente = JPA.em().find(Paciente.class, idPaciente);
            if (paciente != null) {
                return ok(paciente.toJson());
            }
        }
        return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
	}

    @Security.Authenticated(SecuredDoctor.class)
    @Transactional
    public static Result darPorCedula(String cedula){
        response().setHeader("Response-Syle","Json-Object");
        List<Paciente> pacientes = JPA.em().createQuery("SELECT u FROM Paciente u WHERE u.identificacion = ?1", Paciente.class).setParameter(1, cedula).getResultList();
        if(pacientes.size() == 1) {
            return ok(pacientes.get(0).toJson());
        }
        else {
            return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
        }
    }

    @Security.Authenticated(SecuredDoctor.class)
	@Transactional
	public static Result darTodos(){
        response().setHeader("Response-Syle", "Json-Array");
		List<Paciente> pacientes = JPA.em().createQuery("SELECT u FROM Paciente u WHERE u.doctor != ?1 OR u.doctor IS NULL", Paciente.class).setParameter(1, SecurityController.getUser()).getResultList();
		return ok(Usuario.listToJson(pacientes, false));
	}

	@Transactional
	public static Result agregar(){
        try{
            Paciente nuevo = new Paciente(request().body().asJson());
            List<Usuario> usuarios = JPA.em().createQuery("SELECT u FROM Usuario u WHERE u.identificacion = ?1 OR u.email = ?2", Usuario.class).setParameter(1, nuevo.getIdentificacion()).setParameter(2, nuevo.getEmail()).getResultList();
            if(usuarios.size()==0){
                response().setHeader("Response-Syle","Json-Object");
                JPA.em().persist(nuevo);
                return status(StatusMessages.C_CREATED, nuevo.toJson());
            }
            return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
        }
        catch(UsuarioException p){
            return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
        }
	}

    @Security.Authenticated(SecuredPaciente.class)
	@Transactional
	public static Result actualizar(Long idPaciente){
        if (SecurityController.validateOnlyMe(idPaciente)) {
            response().setHeader("Response-Syle", "Json-Object");
            JsonNode json = request().body().asJson();
            String password = json.findPath("password").textValue();
            String email = json.findPath("email").textValue();
            Paciente actual = (Paciente) SecurityController.getUser();
            List<Usuario> usuarios = JPA.em().createQuery("SELECT u FROM Usuario u WHERE u.email = ?1", Usuario.class).setParameter(1, email).getResultList();
            if (email.equals("") || password.equals("") || usuarios.size() > 0) {
                return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
            }
            else{
                actual.setPassword(password);
                actual.setEmail(email);
                JPA.em().merge(actual);
                return ok(StatusMessages.M_SUCCESS);
            }
        }
        else {
            return status(StatusMessages.C_UNAUTHORIZED, StatusMessages.M_UNAUTHORIZED);
        }
	}

    //Administrador
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

    @Security.Authenticated(SecuredPaciente.class)
	@Transactional
	public static Result agregarEpisodio(Long idPaciente){
        if (SecurityController.validateOnlyMe(idPaciente)) {
            Paciente paciente = (Paciente)SecurityController.getUser();
            if(paciente.getDoctor() != null){
                try{
                    response().setHeader("Response-Syle","Json-Object");
                    JsonNode json = request().body().asJson();
                    Episodio episodio = new Episodio(json);
                    episodio.setDoctor(paciente.getDoctor());
                    episodio.setPaciente(paciente);
                    JPA.em().persist(episodio);
                    paciente.addEpisodio(episodio);
                    JPA.em().merge(paciente);
                    return ok(episodio.toJson());
                }
                catch(EpisodioException a){
                    return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
                }
            }
            else{
                return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
            }
        }
        else {
            return status(StatusMessages.C_UNAUTHORIZED, StatusMessages.M_UNAUTHORIZED);
        }
	}

    @Security.Authenticated(SecuredPaciente.class)
    @Transactional
    public static Result agregarGrabacionAEpisodio(Long idPaciente, Long idEpisodio){
        if (SecurityController.validateOnlyMe(idPaciente)) {
            Http.MultipartFormData body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart uploadFilePart = body.getFile("grabacion");
            Paciente paciente = (Paciente)SecurityController.getUser();
            Episodio episodio = JPA.em().find(Episodio.class, idEpisodio);
            if(episodio == null || uploadFilePart == null){
                return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
            }
            else{
                S3File s3File = new S3File();
                s3File.setName("episodios/" + episodio.getId() + "/" + uploadFilePart.getFilename());
                s3File.setFile(uploadFilePart.getFile());
                s3File.setOwner(paciente);
                S3File.save(s3File);
                episodio.setGrabacion(s3File);
                JPA.em().merge(episodio);
                return ok(StatusMessages.M_SUCCESS);
            }
        }
        else {
            return status(StatusMessages.C_UNAUTHORIZED, StatusMessages.M_UNAUTHORIZED);
        }
    }

    @Security.Authenticated(SecuredPaciente.class)
    @Transactional
    public static Result agregarFotoAPaciente(Long idPaciente){
        if (SecurityController.validateOnlyMe(idPaciente)) {
            Http.MultipartFormData body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart uploadFilePart = body.getFile("imagen");
            Paciente paciente = (Paciente)SecurityController.getUser();
            if (uploadFilePart == null) {
                return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
            }
            else{
                S3File s3File = new S3File();
                s3File.setName(uploadFilePart.getFilename());
                s3File.setFile(uploadFilePart.getFile());
                s3File.setOwner(paciente);
                S3File.save(s3File);
                paciente.setProfilePicture(s3File);
                JPA.em().merge(paciente);
                return ok(StatusMessages.M_SUCCESS);
            }
        }
        else {
            return status(StatusMessages.C_UNAUTHORIZED, StatusMessages.M_UNAUTHORIZED);
        }
    }

    @Security.Authenticated(SecuredPaciente.class)
	@Transactional
	public static Result eliminarEpisodio(Long idPaciente, Long idEpisodio){
        if (SecurityController.validateOnlyMe(idPaciente)) {
            Paciente paciente = (Paciente)SecurityController.getUser();
            Episodio episodio = JPA.em().find(Episodio.class, idEpisodio);
            if(paciente.eliminarEpisodio(episodio)){
                JPA.em().merge(paciente);
                JPA.em().remove(episodio);
                return ok(StatusMessages.M_SUCCESS);
            }
            else{
                return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
            }
        }
        else {
            return status(StatusMessages.C_UNAUTHORIZED, StatusMessages.M_UNAUTHORIZED);
        }
	}

    @Security.Authenticated(SecuredPaciente.class)
	@Transactional
	public static Result darEpisodio(Long idPaciente,Long idEpisodio){
        if (SecurityController.validateOnlyMe(idPaciente)) {
            response().setHeader("Response-Syle","Json-Object");
            Paciente paciente = (Paciente) SecurityController.getUser();
            Episodio episodio = JPA.em().find(Episodio.class, idEpisodio);
            if(episodio == null || !paciente.contieneEpisodio(episodio)) {
                return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
            }
            else{
                return ok(episodio.toJson());
            }
        }
        else {
            return status(StatusMessages.C_UNAUTHORIZED, StatusMessages.M_UNAUTHORIZED);
        }
	}

    @Security.Authenticated(SecuredPaciente.class)
	@Transactional
	public static Result darTodosLosEpisodios(Long idPaciente){
        if (SecurityController.validateOnlyMe(idPaciente)) {
            response().setHeader("Response-Syle","Json-Array");
            Paciente actual = (Paciente)SecurityController.getUser();
            List<Episodio> episodios = actual.getEpisodios();
            return ok(Episodio.listToJson(episodios));
        }
        else {
            return status(StatusMessages.C_UNAUTHORIZED, StatusMessages.M_UNAUTHORIZED);
        }
	}

    @Security.Authenticated(SecuredPaciente.class)
	@Transactional
	public static Result darEpisodiosPorFecha(Long idPaciente, String inic, String fi){
        if (SecurityController.validateOnlyMe(idPaciente)) {
            response().setHeader("Response-Syle","Json-Array");
            Paciente paciente = (Paciente)SecurityController.getUser();
            try {
                Date inicio = stringToDate(inic);
                Date fin = stringToDate(fi);
                List<Episodio> episodios = JPA.em().createQuery("SELECT e FROM Episodio e WHERE e.paciente = ?1 AND e.fecha BETWEEN ?2 AND ?3", Episodio.class).setParameter(1, paciente).setParameter(2, inicio).setParameter(3, fin).getResultList();
                return ok(Episodio.listToJson(episodios));
            }
            catch(Exception e){
                return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
            }
        }
        else {
            return status(StatusMessages.C_UNAUTHORIZED, StatusMessages.M_UNAUTHORIZED);
        }
	}

    @Security.Authenticated(SecuredDoctor.class)
	@Transactional
	public static Result agregarDoctorAEpisodio(Long idPaciente,Long idEpisodio){
		JsonNode json = request().body().asJson();
		Long idDoctor = json.path("idDoctor").asLong();
        Paciente paciente = JPA.em().find(Paciente.class, idPaciente);
        Episodio episodio = JPA.em().find(Episodio.class, idEpisodio);
        Doctor doctor = (Doctor)SecurityController.getUser();
        Doctor doctorAgregar = JPA.em().find(Doctor.class, idDoctor);
        if(episodio != null && paciente != null && doctorAgregar!= null){
            if(episodio.contieneDoctor(doctor)){
                if(paciente.contieneEpisodio(episodio) && !episodio.contieneDoctor(doctorAgregar)){
                    episodio.addDoctor(doctorAgregar);
                    JPA.em().merge(episodio);
                    return ok(StatusMessages.M_SUCCESS);
                }
                else{
                    return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
                }
            }
            else{
                return status(StatusMessages.C_UNAUTHORIZED, StatusMessages.M_UNAUTHORIZED);
            }
        }
        else{
            return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
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
