package models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import play.libs.Json;
import Excepciones.PacienteException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Entity
@Table(name="Pacientes")
public class Paciente{
	private static final String MASCULINO="Masculino";
	private static final String FEMENINO="Femenino";
	
	@Id
	@Column(name="id_paciente")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nombre;

	private String apellido;
	
	private String genero;
	
	private String email;

	private String identificacion;

	private String password;

	private Date fechaVinculacion;

	private Date fechaNacimiento;
	
	@OneToOne
	private Doctor doctor;
	
	@OneToMany
	private List<Episodio> episodios;
	
	@OneToMany
	private List<Medicamento> medicamentos;

	@PrePersist
	private void prePersist() {
		this.fechaVinculacion = Calendar.getInstance().getTime();
		episodios=new ArrayList<Episodio>();
	}
	
	public Paciente(){
		
	}
	
	public Paciente(JsonNode node) throws PacienteException{
		this.setNombre(node.findPath("nombre").asText());
		this.setApellido(node.findPath("apellido").asText());
		this.setPassword(node.findPath("password").asText());
		this.setGenero(node.findPath("genero").asInt());
		this.setEmail(node.findPath("email").asText());
		this.setIdentificacion(node.findPath("identificacion").asText());
		this.setFechaNacimiento(stringToDate(node.findPath("fechaNacimiento").asText()));
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

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getFechaVinculacion() {
		return fechaVinculacion;
	}

	public void setFechaVinculacion(Date fechaVinculacion) {
		this.fechaVinculacion = fechaVinculacion;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(int genero) throws PacienteException {
		if(genero==1){
			this.genero=MASCULINO;
		}
		else if(genero==0){
			this.genero=FEMENINO;
		}
		else{
			throw new PacienteException("Error con el género del paciente");
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Episodio> getEpisodios() {
		return episodios;
	}

	public void setEpisodios(List<Episodio> episodios) {
		this.episodios = episodios;
	}
	
	public void addEpisodio(Episodio episodio){
		if(this.episodios==null){
			this.episodios=new ArrayList<Episodio>();
		}
		this.episodios.add(episodio);
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

	public boolean eliminarEpisodio(Episodio episodio) {
		return episodios.remove(episodio);
	}
	
	public boolean tieneEpisodio(Episodio episodio){
		return episodios.contains(episodio);
	}

	public JsonNode toJson() {
		ObjectNode node = Json.newObject();
		node.put("id", getId());
		node.put("nombre", getNombre());
		node.put("apellido", getApellido());
		node.put("genero", getGenero());
		node.put("identificacion", getIdentificacion());
		node.put("email", getEmail());
		node.put("fechaNacimiento", dateToString(getFechaNacimiento()));
		node.put("fechaVinculacion", dateToString(getFechaVinculacion()));
		node.put("idDoctor", doctor!=null?doctor.getId():null);
		node.put("episodios", episodiosToJson());
		return node;
	}
	
	private ArrayNode episodiosToJson(){
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
	
	private static Date stringToDate(String date) throws PacienteException{
		try {
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); 
			return formatter.parse(date);
		} catch (ParseException e) {
			throw new PacienteException("Error interpretando la fecha");
		}
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public List<Episodio> getEpisodios(Date inicio, Date fin) {
		List<Episodio> eps = new ArrayList<Episodio>();
		for (Episodio episodio : episodios) {
			if(episodio.getFecha().compareTo(inicio)>=0 && episodio.getFecha().compareTo(fin)<=0){
				eps.add(episodio);
			}
		}
		return eps;
	}
}
