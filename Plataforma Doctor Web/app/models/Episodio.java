package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.libs.F.Option;
import play.mvc.QueryStringBindable;

@Entity
public class Episodio implements QueryStringBindable<Episodio>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private int nivelDolor;

	private Date fecha;

	private Causa localizacion;

	@OneToMany
	private List<Comentario> comentarios;

	@ManyToMany
	private List<Medicamento> medicamentos;

	@ManyToMany
	private List<Intervalo> patronesSueno;

	@ManyToMany
	private List<Causa> causas;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNivelDolor() {
		return nivelDolor;
	}

	public void setNivelDolor(int nivelDolor) {
		this.nivelDolor = nivelDolor;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Causa getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(Causa localizacion) {
		this.localizacion = localizacion;
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

	public List<Medicamento> getMedicamentos() {
		return medicamentos;
	}

	public void setMedicamentos(List<Medicamento> medicamentos) {
		this.medicamentos = medicamentos;
	}

	public List<Intervalo> getPatronesSueno() {
		return patronesSueno;
	}

	public void setPatronesSueno(List<Intervalo> patronesSueno) {
		this.patronesSueno = patronesSueno;
	}

	public void addPatronDeSueno(Intervalo patron){
		if(this.patronesSueno==null){
			this.patronesSueno=new ArrayList<Intervalo>();
		}
		this.patronesSueno.add(patron);
	}

	public List<Causa> getCausas() {
		return causas;
	}

	public void setCausas(List<Causa> causas) {
		this.causas = causas;
	}

	public void addCausa(Causa causa){
		if(this.causas==null){
			this.causas=new ArrayList<Causa>();
		}
		this.causas.add(causa);
	}

	@Override
	public Option<Episodio> bind(String key, Map<String, String[]> values) {
//		try{
//			this.nivelDolor = values.containsKey("intensidad")?values.get("intensidad")[0]:"";
//			this.localizacion = values.containsKey("descripcion")?values.get("descripcion")[0]:"";
//			this.comentarios = values.containsKey("comentarios")?values.get("comentarios")[0]:"";
//
//			//Falta inicializar los arreglos!
//
//			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
//			this.fecha =  sdf.format(Calendar.getInstance().getTime());
//			this.fecha=values.get("fecha")[0];
//			return Option.Some(this);
//		}
//		catch(Exception e){
//			return Option.None();
//		}
		return Option.None();
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
