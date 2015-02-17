package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.swing.text.html.Option;

public class Episodio implements QueryStringBindable<Episodio>{

	private String intensidad;
	private String fecha;
	private String descripcion;
	
	public Date getFecha() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		return fecha!=null?sdf.parse(fecha):null;
	}
	
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getIntensidad() {
		return intensidad;
	}

	public void setIntensidad(String intensidad) {
		this.intensidad = intensidad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	public Option<Episodio> bind(String key, Map<String, String[]> values) {
		try{
			this.intensidad = values.containsKey("intensidad")?values.get("intensidad")[0]:"";
			this.descripcion = values.containsKey("descripcion")?values.get("descripcion")[0]:"";
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
			this.fecha =  sdf.format(Calendar.getInstance().getTime());
			this.fecha=values.get("fecha")[0];
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
