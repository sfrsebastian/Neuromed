package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="Episodios")
public class Episodio{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private int nivelDolor;

	private Date fecha;

	private String localizacion;

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

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String localizacion) {
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
}
