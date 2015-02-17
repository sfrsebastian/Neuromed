package controllers;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Paciente;
import play.data.Form;
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
  
}
