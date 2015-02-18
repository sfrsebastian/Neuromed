package controllers;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Doctor;
import models.Episodio;
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
    
    @Transactional
    public static Result publicarSegundaOpinionEpisodio(String idDoctor,String idColega){
    	Episodio episodio = Form.form(Episodio.class).bindFromRequest().get();
    	Doctor actual = JPA.em().find(Doctor.class, idDoctor);
    	if(actual != null){
    		List<Doctor> colegas = actual.getColegas();
    		
    		Doctor temp = null;
    		for (Doctor doctor : colegas) {
				if(doctor.getIdentificacion().equals(idColega))
					temp = doctor;
			}
    		
    		if(temp != null){
    			temp.addSegundaOpinion(episodio);
    			JPA.em().persist(temp);
    			ObjectMapper mapper = new ObjectMapper(); 
        		JsonNode node = mapper.convertValue(temp, JsonNode.class);
        		return ok(node);
    		}else{
    			return status(1,"No existe colega asociado con el identificador del doctor dado");
    		}
    	}else{
    		return status(1,"No existe doctor con el identificador dado");
    	}
    }
}
