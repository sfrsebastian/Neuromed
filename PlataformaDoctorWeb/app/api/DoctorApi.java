package api;

import java.util.List;
import actions.CorsComposition;
import actions.ForceHttps;
import actions.SecuredDoctor;
import excepciones.StatusMessages;
import models.*;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.*;
import com.fasterxml.jackson.databind.JsonNode;
import excepciones.UsuarioException;

@CorsComposition.Cors
@With(ForceHttps.class)
public class DoctorApi extends Controller {

    @Security.Authenticated(SecuredDoctor.class)
	public static Result dar(Long idDoctor){
        if(SecurityController.validateOnlyMe(idDoctor)) {
            response().setHeader("Response-Syle", "Json-Object");
            Doctor doctor = (Doctor)SecurityController.getUser();
            return ok(doctor.toJson());
        }
        else{
            return status(StatusMessages.C_UNAUTHORIZED, StatusMessages.M_UNAUTHORIZED);
        }
	}

    @Security.Authenticated(SecuredDoctor.class)
    @Transactional
	public static Result darTodos(){
        response().setHeader("Response-Syle", "Json-Array");
		List<Doctor> doctores = JPA.em().createQuery("SELECT u FROM Doctor u WHERE u != ?1", Doctor.class).setParameter(1, SecurityController.getUser()).getResultList();
		return ok(Usuario.listToJson(doctores,false));
	}

    @Transactional
	public static Result agregar(){
		try{
            Doctor nuevo = new Doctor(request().body().asJson());
			List<Usuario> usuarios = JPA.em().createQuery("SELECT u FROM Usuario u WHERE u.identificacion = ?1 OR u.email = ?2", Usuario.class).setParameter(1, nuevo.getIdentificacion()).setParameter(2, nuevo.getEmail()).getResultList();
			if(usuarios.size()==0){
                response().setHeader("Response-Syle","Json-Object");
                JPA.em().persist(nuevo);
                return status(StatusMessages.C_CREATED, nuevo.toJson());
			}
            return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
		}
		catch(UsuarioException p) {
            return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
		}
	}

    @Security.Authenticated(SecuredDoctor.class)
	@Transactional
	public static Result actualizar(Long idDoctor){
        if (SecurityController.validateOnlyMe(idDoctor)) {
            response().setHeader("Response-Syle", "Json-Object");
            JsonNode json = request().body().asJson();
            String password = json.findPath("password").textValue();
            String email = json.findPath("email").textValue();
            Doctor actual = (Doctor) SecurityController.getUser();
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

    @Security.Authenticated(SecuredDoctor.class)
	@Transactional
	public static Result agregarPaciente(Long idDoctor){
        if (SecurityController.validateOnlyMe(idDoctor)) {
            JsonNode json = request().body().asJson();
            Long idPaciente = json.findPath("idPaciente").asLong();
            Doctor doctor = (Doctor)SecurityController.getUser();
            Paciente paciente = JPA.em().find(Paciente.class, idPaciente);
            if(paciente == null || paciente.getDoctor() != null){
                return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
            }
            else{
                paciente.setDoctor(doctor);
                JPA.em().merge(paciente);
                return ok(StatusMessages.M_SUCCESS);
            }
        }
        else {
            return status(StatusMessages.C_UNAUTHORIZED, StatusMessages.M_UNAUTHORIZED);
        }
	}

    @Security.Authenticated(SecuredDoctor.class)
	@Transactional
	public static Result darPacientes(Long idDoctor){
        if (SecurityController.validateOnlyMe(idDoctor)) {
            response().setHeader("Response-Syle","Json-Array");
            Doctor doctor = (Doctor)SecurityController.getUser();
            List<Paciente> pacientes = JPA.em().createQuery("SELECT u FROM Paciente u WHERE u.doctor = ?1", Paciente.class).setParameter(1, doctor).getResultList();
            return ok(Usuario.listToJson(pacientes,false));
        }
        else {
            return status(StatusMessages.C_UNAUTHORIZED, StatusMessages.M_UNAUTHORIZED);
        }
	}

    @Security.Authenticated(SecuredDoctor.class)
	@Transactional
	public static Result crearComentario(Long idDoctor){
        if (SecurityController.validateOnlyMe(idDoctor)) {
            response().setHeader("Response-Syle","Json-Object");
            JsonNode json = request().body().asJson();
            Long idEpisodio = json.findPath("idEpisodio").asLong();
            Doctor doctor = (Doctor)SecurityController.getUser();
            Episodio episodio = JPA.em().find(Episodio.class, idEpisodio);
            if(episodio == null){
                return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
            }
            else if(!episodio.contieneDoctor(doctor)){
                return status(StatusMessages.C_UNAUTHORIZED, StatusMessages.M_UNAUTHORIZED);
            }
            else{
                Comentario comentario = new Comentario(json);
                comentario.setDoctor(doctor);
                comentario.setEpisodio(episodio);
                JPA.em().persist(comentario);
                episodio.addComentario(comentario);
                JPA.em().merge(episodio);
                return ok(episodio.toJson());
            }
        }
        else {
            return status(StatusMessages.C_UNAUTHORIZED, StatusMessages.M_UNAUTHORIZED);
        }
	}

    @Security.Authenticated(SecuredDoctor.class)
	@Transactional
	public static Result darComentarios(Long idDoctor){
        if (SecurityController.validateOnlyMe(idDoctor)) {
            response().setHeader("Response-Syle","Json-Array");
            Doctor doctor = (Doctor)SecurityController.getUser();
            List<Comentario> comentarios = JPA.em().createQuery("SELECT u FROM Comentario u WHERE u.doctor=?1",Comentario.class).setParameter(1, doctor).getResultList();
            return ok(Comentario.listToJson(comentarios));
        }
        else {
            return status(StatusMessages.C_UNAUTHORIZED, StatusMessages.M_UNAUTHORIZED);
        }
	}

    @Security.Authenticated(SecuredDoctor.class)
	@Transactional
	public static Result darEpisodiosSegundaOpinion(Long idDoctor){
        if (SecurityController.validateOnlyMe(idDoctor)) {
            response().setHeader("Response-Syle", "Json-Array");
            Doctor doctor = (Doctor)SecurityController.getUser();
            List<Episodio> episodios = JPA.em().createQuery("SELECT u FROM Episodio u WHERE (?1 MEMBER OF u.doctores)", Episodio.class).setParameter(1, doctor).getResultList();
            return ok(Episodio.listToJson(episodios));
        }
        else {
            return status(StatusMessages.C_UNAUTHORIZED, StatusMessages.M_UNAUTHORIZED);
        }
	}

    @Security.Authenticated(SecuredDoctor.class)
    @Transactional
    public static Result agregarFotoADoctor(Long idDoctor){
        if (SecurityController.validateOnlyMe(idDoctor)) {
            Http.MultipartFormData body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart uploadFilePart = body.getFile("imagen");
            Doctor doctor = (Doctor)SecurityController.getUser();
            if (uploadFilePart == null) {
                return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
            }
            else{
                S3File s3File = new S3File();
                s3File.setName(uploadFilePart.getFilename());
                s3File.setFile(uploadFilePart.getFile());
                s3File.setOwner(doctor);
                S3File.save(s3File);
                doctor.setProfilePicture(s3File);
                JPA.em().merge(doctor);
                return ok(StatusMessages.M_SUCCESS);
            }
        }
        else {
            return status(StatusMessages.C_UNAUTHORIZED, StatusMessages.M_UNAUTHORIZED);
        }
    }
}