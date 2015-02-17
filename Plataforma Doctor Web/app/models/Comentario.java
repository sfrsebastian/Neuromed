package models;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import play.libs.F.Option;
import play.mvc.QueryStringBindable;

@Entity
public class Comentario implements QueryStringBindable<Comentario>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Date fecha;
	
	private String contenido;
	
	
	@PrePersist
	private void prePersist() {
		this.fecha = Calendar.getInstance().getTime();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	@Override
	public Option<Comentario> bind(String key, Map<String, String[]> values) {
		this.contenido = values.containsKey("contenido")?values.get("contenido")[0]:"";
		return Option.Some(this);
	}
	
	@Override
	public String unbind(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String javascriptUnbind() {
		// TODO Auto-generated method stub
		return null;
	}

}
