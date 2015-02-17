package controllers;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Doctor;
import play.data.Form;
import play.mvc.*;
import views.html.*;

public class PacienteController extends Controller {
	
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
  
}
