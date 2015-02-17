package models;

import java.util.Map;

import play.libs.F.Option;
import play.mvc.QueryStringBindable;

public class Comentario implements QueryStringBindable<Comentario>{
	
	private Doctor doctor;
	
	private Episodio episodio;
	
	private String identificacion;
	



	@Override
	public Option<Comentario> bind(String key, Map<String, String[]> values) {
		// TODO Auto-generated method stub
		String idDoc = values.containsKey("idDoc")?values.get("idDoc")[0]:"";
		//aqui debo buscar el doctor y asignarlo
		String idEp = values.containsKey("idEpisodio")?values.get("idEpisodio")[0]:"";
		
		return Option.Some(this);
	}
	
	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public Episodio getEpisodio() {
		return episodio;
	}

	public void setEpisodio(Episodio episodio) {
		this.episodio = episodio;
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
