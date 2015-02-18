package controllers;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Episodio;
import models.Paciente;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.*;
import views.html.*;

public class PacienteController extends Controller {
	
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result actualizarPaciente(String id){
        ArrayList<Paciente> pacientes=inicializarArreglo();
        Paciente paciente =Form.form(Paciente.class).bindFromRequest().get();
        paciente.setIdentificacion(id);
        //Busqueda por JPA
        //Persistir por JPA
        if(reemplazarPaciente(pacientes,paciente)){
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.convertValue(paciente, JsonNode.class);
            return ok(node);
        }
        else{
            return status(1, "Doctor no existente");
        }
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
    		actual.addEpisodio(datos);
    		JPA.em().persist(actual);
    		
    		ObjectMapper mapper = new ObjectMapper(); 
    		JsonNode node = mapper.convertValue(actual, JsonNode.class);
    		return ok(node);
    		
    	}else{
    		return status(1,"No se ha podido encontrar el paciente dado");
    	}
    }
  
}
