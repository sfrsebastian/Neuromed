package models;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import play.db.jpa.JPA;
import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import exceptions.TimeException;

@Entity
@Table(name="Intervalos")
public class Intervalo implements Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

	@Column(unique = true)
    private IntervaloId id_intervalo;
	
	public Intervalo(){
		
	}

    public Intervalo(JsonNode node) throws TimeException{
        this.id_intervalo = new IntervaloId();
        this.setInicio(stringToDate(node.findPath("inicio").asText()));
        this.setFin(stringToDate(node.findPath("fin").asText()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Date getInicio() {
        return id_intervalo.getInicio();
    }
    public void setInicio(Date inicio) {
        this.id_intervalo.setInicio(inicio);
    }
    public Date getFin() {
        return id_intervalo.getFin();
    }
    public void setFin(Date fin) {
        this.id_intervalo.setFin(fin);
    }

	public JsonNode toJson() {
		ObjectNode node = Json.newObject();
        node.put("titulo",dateToString(this.id_intervalo.getInicio()) +  " - " + dateToString(this.id_intervalo.getFin()));
        node.put("id", getId());
        node.put("inicio", dateToString(this.id_intervalo.getInicio()));
        node.put("fin", dateToString(this.id_intervalo.getFin()));
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
        DateFormat formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(date);
    }

    private static Date stringToDate(String date) throws TimeException {
        try {
            DateFormat formatter = new SimpleDateFormat("h:mm a");
            return formatter.parse(date);
        }
        catch (ParseException e) {
            throw new TimeException("Error interpretando la fecha");
        }
    }

    public static List<Intervalo> jsonToList(JsonNode intervalos) {
        List<Intervalo> ans = new ArrayList<Intervalo>();
        Iterator<JsonNode> it = intervalos.elements();
        while(it.hasNext()){
            JsonNode node = it.next();
            Intervalo intervalo = JPA.em("default").find(Intervalo.class, node.asLong());
            if(intervalo != null)
                ans.add(intervalo);
        }
        return ans;
    }
}

@Embeddable
class IntervaloId implements Serializable {

    @Temporal(TemporalType.TIMESTAMP)
    Date inicio;

    @Temporal(TemporalType.TIMESTAMP)
    Date fin;

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
}
