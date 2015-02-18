package controllers;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Doctor;
import models.Paciente;
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
    		return status(1,"El Doctor dado ya existe");
    	}
    }
    
    public static Result actualizarDoctor(String id){
    	
    }
    
    public static Result getPacienteDoctor(String idDoctor, String idPaciente){
    	Doctor actual = JPA.em().find(Doctor.class, idDoctor);
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
    			return status(1,"No existe paciente asociado con el identificador del doctor dado");
    		}
    	}else{
    		return status(1,"No existe doctor con el identificador dado");
    	}
    }
}
