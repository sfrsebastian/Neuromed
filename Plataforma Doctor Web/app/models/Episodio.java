package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import play.libs.F.Option;
import play.mvc.QueryStringBindable;

public class Episodio implements QueryStringBindable<Episodio>{

	private String nivelDolor;
	private String fecha;
	private String localizacion;
	
	private String comentarios;
	private String[] medicamentos;
	private String[] patronesSueno;
	private String[] causas;
	
	@Override
	public Option<Episodio> bind(String key, Map<String, String[]> values) {
		try{
			this.nivelDolor = values.containsKey("intensidad")?values.get("intensidad")[0]:"";
			this.localizacion = values.containsKey("descripcion")?values.get("descripcion")[0]:"";
			this.comentarios = values.containsKey("comentarios")?values.get("comentarios")[0]:"";
			
			//Falta inicializar los arreglos!
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
			this.fecha =  sdf.format(Calendar.getInstance().getTime());
			this.fecha=values.get("fecha")[0];
			return Option.Some(this);
		}
		catch(Exception e){
			return Option.None();
		}
		
	}

	public Date getFecha() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		return fecha!=null?sdf.parse(fecha):null;
	}
	
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getNivelDolor() {
		return nivelDolor;
	}

	public void setNivelDolor(String intensidad) {
		this.nivelDolor = intensidad;
	}

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String descripcion) {
		this.localizacion = descripcion;
	}
	
	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public String[] getMedicamentos() {
		return medicamentos;
	}

	public void setMedicamentos(String[] medicamentos) {
		this.medicamentos = medicamentos;
	}

	public String[] getPatronesSueno() {
		return patronesSueno;
	}

	public void setPatronesSueno(String[] patronesSueno) {
		this.patronesSueno = patronesSueno;
	}

	public String[] getCausas() {
		return causas;
	}

	public void setCausas(String[] causas) {
		this.causas = causas;
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
