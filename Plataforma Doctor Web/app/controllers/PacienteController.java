package controllers;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Doctor;
import models.Episodio;
import models.Paciente;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.*;
import views.html.*;

public class PacienteController extends Controller {
	
    public static Result index() {
        return ok();
    }

  

    private static ArrayList<Paciente> inicializarArreglo(){
        ArrayList<Paciente> pacientes = new ArrayList<Paciente>();
        for (int i = 0; i < 10; i++) {
            Paciente nuevo = new Paciente();
            nuevo.setIdentificacion(i+"");
            pacientes.add(nuevo);
        }
        return pacientes;
    }

    private static boolean reemplazarPaciente(ArrayList<Paciente> pacientes,Paciente paciente){
        boolean termino=false;
        for (int i = 0; i < pacientes.size() && !termino; i++) {
            if(pacientes.get(i).getIdentificacion().equals(paciente.getIdentificacion())){
                pacientes.add(i, paciente);
                termino=true;
            }
        }
        return termino;
    }
    
    public static Result getEpisodiosPeriodo(String idPaciente, String fecha1,String fecha2){
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    	
    	Date f1 = null;
    	Date f2 = null;
    	
    	try {
			f1 = sdf.parse(fecha1);
			f2 = sdf.parse(fecha2);
		} catch (ParseException e) {
			System.out.println("Error en parser fecha, revisar formato");
			return status(1,"No se ha podido parsear las fechas dadas");
		}
    	
    	Paciente actual = JPA.em().find(Paciente.class, idPaciente);
    	
    	if(actual != null){
    		List<Episodio> episodios = actual.getEpisodios();
    		List<Episodio> ep = new ArrayList<Episodio>();
    		
    		for (Episodio episodio : episodios) {
				Date fechaActual = episodio.getFecha();
				if (fechaActual.after(f1) && fechaActual.before(f2))
					ep.add(episodio);
			}
    		
    		ObjectMapper mapper = new ObjectMapper(); 
    		JsonNode node = mapper.convertValue(ep, JsonNode.class);
    		return ok(node);
    	}else{
    		return status(1,"No se ha podido encontrar el paciente dado");
    	}
    }
    
    @Transactional
    public static Result agregarEpisodioPaciente(String idPaciente){
    	Paciente actual = JPA.em().find(Paciente.class, idPaciente);
    	if(actual != null){
    		Episodio datos = Form.form(Episodio.class).bindFromRequest().get();
    		JPA.em().persist(datos);
    		actual.addEpisodio(datos);
    		JPA.em().merge(actual);
    		
    		ObjectMapper mapper = new ObjectMapper(); 
    		JsonNode node = mapper.convertValue(actual, JsonNode.class);
    		return ok(node);
    		
    	}else{
    		return status(1,"No se ha podido encontrar el paciente dado");
    	}
    }
    
    @Transactional
    public static Result actualizarPaciente(String identificacion){
    	Paciente nuevo = Form.form(Paciente.class).bindFromRequest().get();
		Paciente actual = JPA.em().find(Paciente.class, identificacion);
		if(actual!=null){
			actual.setPassword(nuevo.getPassword());
			actual.setNombre(nuevo.getNombre());
			JPA.em().merge(actual);
			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode node = mapper.convertValue(actual, JsonNode.class);
			return ok(node);
		}	
		else{
			return status(1,"El paciente con identificacion: " + identificacion+ " no existe en el sistema.");
		}
    }
    
    @Transactional
    public static Result obtenerEpisodio(String idPaciente,String idEpisodio){
    	
    	Paciente actual = JPA.em().find(Paciente.class, idPaciente);
    	
    	if(actual != null){
    		List<Episodio> episodios = actual.getEpisodios();
    		Episodio ep=null;
    		for(int i=0;i<episodios.size();i++){
    			if(episodios.get(i).getId()==Long.parseLong(idEpisodio)){
    				ep=episodios.get(i);
    				break;
    			}
    		}
    		
    		ObjectMapper mapper = new ObjectMapper(); 
    		JsonNode node = mapper.convertValue(ep, JsonNode.class);
    		return ok(node);
    	}else{
    		return status(1,"No se ha podido encontrar el paciente dado");
    	}
    }
  
}
