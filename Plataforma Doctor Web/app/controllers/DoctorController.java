package controllers;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Comentario;
import models.Doctor;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.*;

public class DoctorController extends Controller {
	
    public static Result index() {
        return ok();
    }
    
    @Transactional
    public static Result agregarDoctor(){
    	Doctor nuevo =Form.form(Doctor.class).bindFromRequest().get();
    	Doctor actual = JPA.em().find(Doctor.class, nuevo.getIdentificacion());
    	if(actual==null){
    		JPA.em().persist(nuevo);
    		ObjectMapper mapper = new ObjectMapper(); 
    		JsonNode node = mapper.convertValue(nuevo, JsonNode.class);
    		return ok(node);
    	}	
    	else{
    		return status(1,"El doctor con identificacion " + nuevo.getIdentificacion() + " ya existe");
    	}
    }
    
    @Transactional
    public static Result actualizarDoctor(String identificacion){
    	Doctor nuevo = Form.form(Doctor.class).bindFromRequest().get();
    	Doctor actual = JPA.em().find(Doctor.class, identificacion);
    	if(actual!=null){
    		actual.setPassword(nuevo.getPassword());
    		JPA.em().merge(actual);
    		ObjectMapper mapper = new ObjectMapper(); 
    		JsonNode node = mapper.convertValue(actual, JsonNode.class);
    		return ok(node);
    	}	
    	else{
    		return status(1,"El doctor con identificacion: " + identificacion+ " no existe en el sistema.");
    	}
    }
    
    @Transactional
    public static Result darComentarios(String identificacion){
    	Doctor actual = JPA.em().find(Doctor.class, identificacion);
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
    		return status(1,"El doctor con identificacion: " + identificacion+ " no existe en el sistema.");
    	}
    }
    
    
}
