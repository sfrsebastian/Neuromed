package models;

import javax.persistence.*;

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
@Table(name="Medicamentos")
public class Medicamento implements Serializable{

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private String nombre;

	private String marca;
	
	private String tipo;
	
	private String advertencias;

	public Medicamento(){
		
	}

    public Medicamento(JsonNode node){
        this.setNombre(node.findPath("nombre").asText());
        this.setMarca(node.findPath("marca").asText());
        this.setTipo(node.findPath("tipo").asText());
        this.setAdvertencias(node.findPath("advertencias").asText());
    }
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipoMedicamento) {
		this.tipo = tipoMedicamento;
	}

	public String getAdvertencias() {
		return advertencias;
	}

	public void setAdvertencias(String advertencias) {
		this.advertencias = advertencias;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public JsonNode toJson() {
		ObjectNode node = Json.newObject();
        node.put("id", getId());
        node.put("titulo", getNombre());
        node.put("marca", getMarca());
        node.put("advertencias", getAdvertencias());
		return node;
	}

    public static ArrayNode listToJson(List<Medicamento> medicamentos) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode array = new ArrayNode(factory);
        for (Medicamento p : medicamentos) {
            array.add(p.toJson());
        }
        return array;
    }

    public static List<Medicamento> jsonToList(JsonNode medicamentos) {
        List<Medicamento> ans = new ArrayList<Medicamento>();
        Iterator<JsonNode> it = medicamentos.elements();
        while(it.hasNext()){
            JsonNode node = it.next();
            Medicamento med = JPA.em("default").find(Medicamento.class, node.asLong());
            if(med != null)
                ans.add(med);
        }
        return ans;
    }
}
