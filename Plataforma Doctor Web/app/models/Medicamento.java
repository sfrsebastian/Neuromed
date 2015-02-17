package models;

import java.util.Map;

import play.libs.F.Option;
import play.mvc.QueryStringBindable;

public class Medicamento implements QueryStringBindable<Medicamento> {

	private String nombre;
	
	private String identificacion;
	
	// no se si hay que tener paciente y episodio como atributos
	
	@Override
	public Option<Medicamento> bind(String key, Map<String, String[]> values) {
		// TODO Auto-generated method stub
		nombre = values.containsKey("nombre")?values.get("nombre")[0]:"";
		
		return null;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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
