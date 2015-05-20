package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Entity
@Table(name="Comentarios")
public class Comentario{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private Date fecha;
	
	private String contenido;
	
	@ManyToOne
	private Doctor doctor;

    @ManyToOne
    private Episodio episodio;

	@PrePersist
	private void prePersist() {
		this.fecha = Calendar.getInstance().getTime();
	}

	public Comentario(){
		
	}
	
	public Comentario(JsonNode node){
		this.setContenido(node.findPath("contenido").asText());
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

    public Episodio getEpisodio() {
        return episodio;
    }

    public void setEpisodio(Episodio episodio) {
        this.episodio = episodio;
    }

    public ObjectNode toJson(){
		ObjectNode node = Json.newObject();
		node.put("id", getId());
		node.put("fecha", dateToString(getFecha()));
		node.put("contenido", getContenido());
        node.put("doctorNombre", "Dr. " + getDoctor().getNombre() + " " + getDoctor().getApellido());
        node.put("idDoctor",getDoctor().getId());
        node.put("episodio", getEpisodio().getId());
		return node;
	}
	
	private String dateToString(Date date){
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); 
		return formatter.format(date);
	}

    public static ArrayNode listToJson(List<Comentario> comentarios) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode array = new ArrayNode(factory);
        for (Comentario p : comentarios) {
            array.add(p.toJson());
        }
        return array;
    }
}
