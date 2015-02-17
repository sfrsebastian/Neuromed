package controllers;

import java.util.ArrayList;

import models.Comentario;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class ComentarioController extends Controller{

	 public static Result index() {
	        return ok(index.render("Your new application is ready."));
	    }
	    
	    public static Result actualizarComentario(String id){
	    	ArrayList<Comentario> comentarios=inicializarArreglo();
	    	 Comentario comentario =Form.form(Comentario.class).bindFromRequest().get();
	    	comentario.setIdentificacion(id);
	    	//Busqueda por JPA
	    	//Persistir por JPA
	    	if(reemplazarComentario(comentarios,comentario)){
	    		ObjectMapper mapper = new ObjectMapper(); 
	    		JsonNode node = mapper.convertValue(comentario, JsonNode.class);
	    		return ok(node);
	    	}
	    	else{
	    		return status(1, "Comentario no existente");
	    	}
	    }	
	    
	    private static ArrayList<Comentario> inicializarArreglo(){
	    	ArrayList<Comentario> comentarios = new ArrayList<Comentario>();
	    	for (int i = 0; i < 10; i++) {
				Comentario nuevo = new Comentario();
				nuevo.setIdentificacion(i+"");
				comentarios.add(nuevo);
			}
	    	return comentarios;
	    }
	    
	    private static boolean reemplazarComentario(ArrayList<Comentario> comentarios,Comentario comentario){
	    	boolean termino=false;
	    	for (int i = 0; i < comentarios.size() && !termino; i++) {
				if(comentarios.get(i).getIdentificacion().equals(comentario.getIdentificacion())){
					comentarios.add(i, comentario);
					termino=true;
				}
			}
	    	return termino;
	    }
	
}
