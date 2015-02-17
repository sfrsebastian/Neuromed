package controllers;

import java.util.ArrayList;

import models.Medicamento;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class MedicamentoController extends Controller {

	 public static Result index() {
	        return ok(index.render("Your new application is ready."));
	    }
	    
	    public static Result actualizarMedicamento(String id){
	    	ArrayList<Medicamento> medicamentos=inicializarArreglo();
	    	 Medicamento medicamento =Form.form(Medicamento.class).bindFromRequest().get();
	    	medicamento.setIdentificacion(id);
	    	//Busqueda por JPA
	    	//Persistir por JPA
	    	if(reemplazarMedicamento(medicamentos,medicamento)){
	    		ObjectMapper mapper = new ObjectMapper(); 
	    		JsonNode node = mapper.convertValue(medicamento, JsonNode.class);
	    		return ok(node);
	    	}
	    	else{
	    		return status(1, "Medicamento no existente");
	    	}
	    }	
	    
	    private static ArrayList<Medicamento> inicializarArreglo(){
	    	ArrayList<Medicamento> medicamentos = new ArrayList<Medicamento>();
	    	for (int i = 0; i < 10; i++) {
				Medicamento nuevo = new Medicamento();
				nuevo.setIdentificacion(i+"");
				medicamentos.add(nuevo);
			}
	    	return medicamentos;
	    }
	    
	    private static boolean reemplazarMedicamento(ArrayList<Medicamento> medicamentos,Medicamento medicamento){
	    	boolean termino=false;
	    	for (int i = 0; i < medicamentos.size() && !termino; i++) {
				if(medicamentos.get(i).getIdentificacion().equals(medicamento.getIdentificacion())){
					medicamentos.add(i, medicamento);
					termino=true;
				}
			}
	    	return termino;
	    }
	
}
