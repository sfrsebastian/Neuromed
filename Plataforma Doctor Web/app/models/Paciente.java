package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import play.libs.F.Option;
import play.mvc.QueryStringBindable;

public class Paciente implements QueryStringBindable<Paciente>{
	private String nombre;
	private String apellido;
	private String password;
	private String identificacion;
	private Episodio[] episodios;
	private Medicamento[] medicamentosConsumidos;
	private String fechaNacimiento;
	


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
	
	@Override
	public Option<Paciente> bind(String key, Map<String, String[]> values) {
		try{
			this.nombre = values.containsKey("nombre")?values.get("nombre")[0]:"";
			this.apellido = values.containsKey("apellido")?values.get("apellido")[0]:"";
			this.identificacion = values.containsKey("identificacion")?values.get("identificacion")[0]:"";
			this.password = values.containsKey("password")?values.get("password")[0]:"";
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
			this.fechaNacimiento=values.get("fechaNacimiento")[0];
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
