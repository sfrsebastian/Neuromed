package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import play.libs.F.Option;
import play.mvc.QueryStringBindable;

public class Doctor implements QueryStringBindable<Doctor>{
	private String nombre;
	private String apellido;
	private String identificacion;
	private boolean autorizado;
	private String password;
	private String fechaVinculacion;
	private String fechaNacimiento;
	private String[] pacientes;
	
	//Anotaciones JPA
//	private Doctor colegas;
//	private Episodio segundasOpiniones;
//	private Comentario comentarios;
//	private Paciente pacientes;

	
//	public Doctor getColegas() {
//		return colegas;
//	}
//
//	public void setColegas(Doctor colegas) {
//		this.colegas = colegas;
//	}
//
//	public Episodio getSegundasOpiniones() {
//		return segundasOpiniones;
//	}
//
//	public void setSegundasOpiniones(Episodio segundasOpiniones) {
//		this.segundasOpiniones = segundasOpiniones;
//	}
//
//	public Comentario getComentarios() {
//		return comentarios;
//	}
//
//	public void setComentarios(Comentario comentarios) {
//		this.comentarios = comentarios;
//	}
//
//	public Paciente getPacientes() {
//		return pacientes;
//	}
//
//	public void setPacientes(Paciente pacientes) {
//		this.pacientes = pacientes;
//	}

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
	
	public Date getFechaVinculacion() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		return fechaVinculacion!=null?sdf.parse(fechaVinculacion):null;
	}
	
	public void setFechaVinculacion(String fechaVinculacion) {
		this.fechaVinculacion = fechaVinculacion;
	}
	
	public Date getFechaNacimiento() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		return fechaNacimiento!=null?sdf.parse(fechaNacimiento):null;
	}
	
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
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

	public String[] getPacientes(){return pacientes;}

	public void setPaciente(String[] pacientes){this.pacientes=pacientes;}
	
	@Override
	public Option<Doctor> bind(String key, Map<String, String[]> values) {
		try{
			this.nombre = values.containsKey("nombre")?values.get("nombre")[0]:"";
			this.apellido = values.containsKey("apellido")?values.get("apellido")[0]:"";
			this.identificacion = values.containsKey("identificacion")?values.get("identificacion")[0]:"";
			this.autorizado=false;
			this.password = values.containsKey("password")?values.get("password")[0]:"";
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
			this.fechaVinculacion =  sdf.format(Calendar.getInstance().getTime());
			this.fechaNacimiento=values.get("fechaNacimiento")[0];
			this.pacientes=values.containsKey("pacientes")?values.get("pacientes"):null;
			return Option.Some(this);
		}
		catch(Exception e){
			return Option.None();
		}
		
	}

	@Override
	public String javascriptUnbind() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String unbind(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
