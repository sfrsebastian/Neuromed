package models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import excepciones.EpisodioException;

@Entity
@Table(name="Episodios")
public class Episodio implements Comparable<Episodio>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToOne
    private Paciente paciente;
	
	@ManyToMany
	private List<Doctor> doctores;

	@PrePersist
	private void prePersist() {
        doctores = new ArrayList<Doctor>();
		comentarios = new ArrayList<Comentario>();
        causas = new ArrayList<Causa>();
        medicamentos = new ArrayList<Medicamento>();
        patronesSueno = new ArrayList<Intervalo>();
	}
	
	public Episodio(){
		
	}
	
	public Episodio(JsonNode node) throws EpisodioException{
		this.setNivelDolor(node.findPath("nivelDolor").asInt());
		this.setFecha(stringToDate(node.findPath("fecha").asText()));
		this.setLocalizacion(node.findPath("localizacion").asText());
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
		this.comentarios.add(comentario);
	}

	public List<Medicamento> getMedicamentos() {
		return medicamentos;
	}

	public void setMedicamentos(List<Medicamento> medicamentos) {
		this.medicamentos = medicamentos;
	}
	
	public void addMedicamento(Medicamento medicamento){
		this.medicamentos.add(medicamento);
	}

	public List<Intervalo> getPatronesSueno() {
		return patronesSueno;
	}

	public void setPatronesSueno(List<Intervalo> patronesSueno) {
		this.patronesSueno = patronesSueno;
	}

	public void addPatronDeSueno(Intervalo patron){
		this.patronesSueno.add(patron);
	}

	public List<Causa> getCausas() {
		return causas;
	}

	public void setCausas(List<Causa> causas) {
		this.causas = causas;
	}

	public void addCausa(Causa causa){
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

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public boolean contieneDoctor(Doctor doctor) {
        return doctores.contains(doctor) || this.doctor==doctor;
    }

    public ObjectNode toJson(){
		ObjectNode node = Json.newObject();
		node.put("id", getId());
		node.put("nivelDolor", getNivelDolor());
		node.put("fecha", dateToString(getFecha()));
		node.put("localizacion", getLocalizacion());
		node.put("comentarios", Comentario.listToJson(this.comentarios));
		node.put("doctor", this.doctor.getId());
        node.put("paciente", this.paciente.getId());
		node.put("doctores",Doctor.listToJson(this.doctores,true));
		node.put("causas", Causa.listToJson(this.causas));
		node.put("medicamentos", Medicamento.listToJson(this.medicamentos));
		node.put("patronesDeSueno", Intervalo.listToJson(this.patronesSueno));
		return node;
	}

    public static ArrayNode listToJson(List<Episodio> episodios){
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode array = new ArrayNode(factory);
        for (Episodio p : episodios) {
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
}
