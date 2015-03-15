package models;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import excepciones.TimeException;

@Entity
@Table(name="Intervalos")
public class Intervalo implements Serializable{
	
	@Id
	private Date inicio;

    @Id
	private Date fin;
	
	public Intervalo(){
		
	}

    public Intervalo(JsonNode node) throws TimeException{
        this.setInicio(stringToDate(node.findPath("inicio").asText()));
        this.setFin(stringToDate(node.findPath("fin").asText()));
    }

	public Date getInicio() {
		return inicio;
	}
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	public Date getFin() {
		return fin;
	}
	public void setFin(Date fin) {
		this.fin = fin;
	}

	public JsonNode toJson() {
		ObjectNode node = Json.newObject();
        node.put("inicio", dateToString(this.inicio));
        node.put("fin", dateToString(this.fin));
		return node;
	}


    public static ArrayNode listToJson(List<Intervalo> intervalos) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode array = new ArrayNode(factory);
        for (Intervalo p : intervalos) {
            array.add(p.toJson());
        }
        return array;
    }

    private String dateToString(Date date){
        DateFormat formatter = new SimpleDateFormat("h:m:s a");
        return formatter.format(date);
    }

    private static Date stringToDate(String date) throws TimeException {
        try {
            DateFormat formatter = new SimpleDateFormat("h:m:s a");
            return formatter.parse(date);
        }
        catch (ParseException e) {
            throw new TimeException("Error interpretando la fecha");
        }
    }
}
