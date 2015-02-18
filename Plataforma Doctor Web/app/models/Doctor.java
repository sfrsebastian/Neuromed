package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name="Doctores")
public class Doctor{

	private String nombre;

	private String apellido;

	@Id
	@Column(name="id")
	private String identificacion;

	private boolean autorizado;

	private String password;

	private Date fechaVinculacion;

	private Date fechaNacimiento;

	@OneToMany
	private List<Paciente> pacientes;

	@OneToMany
	private List<Comentario> comentarios;

	@ManyToMany
	private List<Episodio> segundasOpiniones;

	@ManyToMany
	private List<Doctor> colegas;

	@PrePersist
	private void prePersist() {
		this.fechaVinculacion = Calendar.getInstance().getTime();
		this.autorizado=false;
		this.pacientes = new ArrayList<Paciente>();
		this.comentarios = new ArrayList<Comentario>();
		this.segundasOpiniones = new ArrayList<Episodio>();
		this.colegas = new ArrayList<Doctor>();
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

	public List<Paciente> getPacientes() {
		return pacientes;
	}

	public void setPacientes(List<Paciente> pacientes) {
		this.pacientes = pacientes;
	}

	public void addPaciente(Paciente paciente){
		if(this.pacientes==null){
			this.pacientes=new ArrayList<Paciente>();
		}
		this.pacientes.add(paciente);
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

	public List<Episodio> getSegundasOpiniones() {
		return segundasOpiniones;
	}

	public void setSegundasOpiniones(List<Episodio> segundasOpiniones) {
		this.segundasOpiniones = segundasOpiniones;
	}

	public void addSegundaOpinion(Episodio episodio){
		if(this.segundasOpiniones==null){
			this.segundasOpiniones=new ArrayList<Episodio>();
		}
		this.segundasOpiniones.add(episodio);
	}

	public List<Doctor> getColegas() {
		return colegas;
	}

	public void setColegas(List<Doctor> colegas) {
		this.colegas = colegas;
	}

	public void addColega(Doctor colega){
		if(this.colegas==null){
			this.colegas=new ArrayList<Doctor>();
		}
		this.colegas.add(colega);
	}

	public boolean poseePaciente(String id){


		Iterator<Paciente> it = pacientes.listIterator();

		while(it.hasNext()){

			Paciente p = it.next();

			if(p.getIdentificacion().equals(id)){

				return true;

			}

		}

		return false;

	}

}
