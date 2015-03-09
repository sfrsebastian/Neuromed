package models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import play.libs.Json;
import Excepciones.DoctorException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Entity
@Table(name="Doctores")
public class Doctor implements Comparable<Doctor>{
	private static final String MASCULINO="Masculino";
	private static final String FEMENINO="Femenino";

	@Id
	@Column(name="id_doctor")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nombre;

	private String apellido;

	private String identificacion;

	private boolean autorizado;

	private String password;

	private Date fechaVinculacion;

	private Date fechaNacimiento;

	private String email;

	private String genero;

	@PrePersist
	private void prePersist() {
		this.fechaVinculacion = Calendar.getInstance().getTime();
		this.autorizado=false;
	}

	public Doctor(){
		
	}
	
	public Doctor(JsonNode node) throws DoctorException{
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

	public boolean isAutorizado() {
		return autorizado;
	}

	public void setAutorizado(boolean autorizado) {
		this.autorizado = autorizado;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(int genero) throws DoctorException{
		if(genero==1){
			this.genero=MASCULINO;
		}
		else if(genero==0){
			this.genero=FEMENINO;
		}
		else{
			throw new DoctorException("Error con el género del doctor");
		}
	}

	public ObjectNode toJson(){
		ObjectNode node = Json.newObject();
		node.put("id", getId());
		node.put("nombre", getNombre());
		node.put("apellido", getApellido());
		node.put("genero", getGenero());
		node.put("identificacion", getIdentificacion());
		node.put("email", getEmail());
		node.put("fechaNacimiento", dateToString(getFechaNacimiento()));
		node.put("fechaVinculacion", dateToString(getFechaVinculacion()));
		return node;
	}
	
	private String dateToString(Date date){
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); 
		return formatter.format(date);
	}

	private static Date stringToDate(String date) throws DoctorException{
		try {
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); 
			return formatter.parse(date);
		} catch (ParseException e) {
			throw new DoctorException("Error interpretando la fecha");
		}
	}

	@Override
	public int compareTo(Doctor o) {
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
