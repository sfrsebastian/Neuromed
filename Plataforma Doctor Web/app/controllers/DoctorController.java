package controllers;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Doctor;
import play.mvc.*;
import views.html.*;

public class DoctorController extends Controller {
	
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    public static Result actualizarDoctor(Doctor doctor){
    	ArrayList<Doctor> doctores=inicializarArreglo();
    	if(reemplazarDoctor(doctores,doctor)){
    		ObjectMapper mapper = new ObjectMapper(); 
    		JsonNode node = mapper.convertValue(doctor, JsonNode.class);
    		return ok(node);
    	}
    	else{
    		return status(1, "Doctor no existente");
    	}
    }	
    
    private static ArrayList<Doctor> inicializarArreglo(){
    	ArrayList<Doctor> doctores = new ArrayList<Doctor>();
    	for (int i = 0; i < 10; i++) {
			Doctor nuevo = new Doctor();
			nuevo.setIdentificacion(i+"");
			doctores.add(nuevo);
		}
    	return doctores;
    }
    
    private static boolean reemplazarDoctor(ArrayList<Doctor> doctores,Doctor doctor){
    	boolean termino=false;
    	for (int i = 0; i < doctores.size() && !termino; i++) {
			if(doctores.get(i).getIdentificacion().equals(doctor.getIdentificacion())){
				doctores.add(i, doctor);
				termino=true;
			}
		}
    	return termino;
    }
}
