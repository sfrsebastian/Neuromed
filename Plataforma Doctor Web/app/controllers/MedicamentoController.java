package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Medicamento;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class MedicamentoController extends Controller {

	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}
	
    //Mario
    @Transactional
	public static Result crearMedicamento(){
    	Medicamento nuevo = Form.form(Medicamento.class).bindFromRequest().get();
		Medicamento actual = JPA.em().find(Medicamento.class, nuevo.getId());
		if(actual==null){
			
			JPA.em().persist(nuevo);			
			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode node = mapper.convertValue(nuevo, JsonNode.class);
			return ok(node);
		}	
		else{
			return status(1,"El medicamento ya existe");
		}
	}

}
