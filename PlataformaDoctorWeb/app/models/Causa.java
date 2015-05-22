package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import play.db.jpa.JPA;
import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name="Causas")
public class Causa implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String descripcion;
	
	private String titulo;

	private String tipo;
	
	public Causa(){
		
	}

    public Causa(JsonNode node){
        this.setTitulo(node.findPath("titulo").asText());
        this.setDescripcion(node.findPath("descripcion").asText());
        this.setTipo(node.findPath("tipo").asText());
    }
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public JsonNode toJson() {
		ObjectNode node = Json.newObject();
        node.put("id", getId());
        node.put("titulo", getTitulo());
        node.put("descripcion", getDescripcion());
        node.put("tipo", getTipo());
		return node;
	}

    public static ArrayNode listToJson(List<Causa> causas) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode array = new ArrayNode(factory);
        for (Causa p : causas) {
            array.add(p.toJson());
        }
        return array;
    }

    public static List<Causa> jsonToList(JsonNode causas) {
        List<Causa> ans = new ArrayList<Causa>();
        Iterator<JsonNode> it = causas.elements();
        while(it.hasNext()){
            JsonNode node = it.next();
            Causa causa = JPA.em("default").find(Causa.class, node.asLong());
            if(causa != null)
                ans.add(causa);
        }
        return ans;
    }
}
