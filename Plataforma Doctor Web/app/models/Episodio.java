package models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import play.libs.Json;
import Excepciones.EpisodioException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Entity
@Table(name="Episodios")
public class Episodio implements Comparable<Episodio>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private int nivelDolor;

	private Date fecha;

	private String localizacion;

	@OneToMany
	private List<Comentario> comentarios;

	@ManyToMany
	private List<Medicamento> medicamentos;

	@ManyToMany
	private List<Intervalo> patronesSueno;

	@ManyToMany
	private List<Causa> causas;
	
	@ManyToOne
	private Doctor doctor;
	
	@ManyToMany
	private List<Doctor> doctores;

	@PrePersist
	private void prePersist() {
		comentarios=new ArrayList<Comentario>();
	}
	
	public Episodio(){
		
	}
	
	public Episodio(JsonNode node) throws EpisodioException{
		this.setNivelDolor(node.findPath("nivelDolor").asInt());
		this.setFecha(stringToDate(node.findPath("fecha").asText()));
		this.setLocalizacion(node.findPath("localizacion").asText());	
		doctores=new ArrayList<Doctor>();
	}
	
	private static Date stringToDate(String date) throws EpisodioException{
		try {
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); 
			return formatter.parse(date);
		} 
		catch (ParseException e) {
			throw new EpisodioException("Error interpretando la fecha");
		}
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNivelDolor() {
		return nivelDolor;
	}

	public void setNivelDolor(int nivelDolor) {
		this.nivelDolor = nivelDolor;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}

	public List<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}

	public void addComentario(Comentario comentario){
		if(this.comentarios==null){
			this.comentarios=new ArrayList<Comentario>();
		}
		this.comentarios.add(comentario);
	}

	public List<Medicamento> getMedicamentos() {
		return medicamentos;
	}

	public void setMedicamentos(List<Medicamento> medicamentos) {
		this.medicamentos = medicamentos;
	}
	
	public void addMedicamento(Medicamento medicamento){
		if(this.medicamentos==null){
			this.medicamentos=new ArrayList<Medicamento>();
		}
		this.medicamentos.add(medicamento);
	}

	public List<Intervalo> getPatronesSueno() {
		return patronesSueno;
	}

	public void setPatronesSueno(List<Intervalo> patronesSueno) {
		this.patronesSueno = patronesSueno;
	}

	public void addPatronDeSueno(Intervalo patron){
		if(this.patronesSueno==null){
			this.patronesSueno=new ArrayList<Intervalo>();
		}
		this.patronesSueno.add(patron);
	}

	public List<Causa> getCausas() {
		return causas;
	}

	public void setCausas(List<Causa> causas) {
		this.causas = causas;
	}

	public void addCausa(Causa causa){
		if(this.causas==null){
			this.causas=new ArrayList<Causa>();
		}
		this.causas.add(causa);
	}
	
	public List<Doctor> getDoctores() {
		return doctores;
	}

	public void addDoctor(Doctor doctor) {
		this.doctores.add(doctor);
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public ObjectNode toJson(){
		ObjectNode node = Json.newObject();
		node.put("id", getId());
		node.put("nivelDolor", getNivelDolor());
		node.put("fecha", dateToString(getFecha()));
		node.put("localizacion", getLocalizacion());
		node.put("comentarios", comentariosToJson());
		node.put("doctor",doctor.toJson());
		node.put("doctores",doctoresToJson());
		node.put("causas", causasToJson());
		node.put("medicamentos", medicamentosToJson());
		node.put("patronesDeSueno", patronesToJson());
		return node;
	}
	
	private JsonNode patronesToJson() {
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ArrayNode array = new ArrayNode(factory);
		for (Intervalo p : patronesSueno) {
			array.add(p.toJson());
		}
		return array;
	}

	private JsonNode medicamentosToJson() {
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ArrayNode array = new ArrayNode(factory);
		for (Medicamento p : medicamentos) {
			array.add(p.toJson());
		}
		return array;
	}

	private JsonNode causasToJson() {
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ArrayNode array = new ArrayNode(factory);
		for (Causa p : causas) {
			array.add(p.toJson());
		}
		return array;
	}

	private ArrayNode comentariosToJson(){
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ArrayNode array = new ArrayNode(factory);
		for (Comentario p : comentarios) {
			array.add(p.toJson());
		}
		return array;
	}
	
	private ArrayNode doctoresToJson(){
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ArrayNode array = new ArrayNode(factory);
		for (Doctor p : doctores) {
			array.add(p.toJson());
		}
		return array;
	}
	
	private String dateToString(Date date){
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); 
		return formatter.format(date);
	}

	@Override
	public int compareTo(Episodio o) {
		if(this.id == o.getId()){
			return 0;
		}
		else if(this.id > o.getId()){
			return 1;
		}
		else{
			return -1;
		}
	}

	public boolean contieneDoctor(Doctor doctor) {
		return doctores.contains(doctor) || this.doctor==doctor;
	}
}
