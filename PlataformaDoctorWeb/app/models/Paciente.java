package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import excepciones.UsuarioException;

@Entity
@Table(name="Pacientes")
public class Paciente extends Usuario{

	
	@OneToOne
	private Doctor doctor;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Episodio> episodios;
	
	@OneToMany
	private List<Medicamento> medicamentos;

	@PrePersist
	public void prePersist() {
        super.prePersist();
		episodios = new ArrayList<Episodio>();
        medicamentos = new ArrayList<Medicamento>();
	}
	
	public Paciente(){
		
	}
	
	public Paciente(JsonNode node) throws UsuarioException{
		super(node);
	}

	public List<Episodio> getEpisodios() {
        return episodios;
	}

	public void setEpisodios(List<Episodio> episodios) {
        this.episodios = episodios;
	}
	
	public void addEpisodio(Episodio episodio){
		this.episodios.add(episodio);
	}

	public List<Medicamento> getMedicamentos() {
		return medicamentos;
	}

	public void setMedicamentos(List<Medicamento> medicamentos) {
		this.medicamentos = medicamentos;
	}
	
	public void addMedicamento(Medicamento medicamento){
		this.medicamentos.add(medicamento);
	}

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public boolean eliminarEpisodio(Episodio episodio) {
        return episodios.remove(episodio);
    }

    public boolean contieneEpisodio(Episodio episodio) {
        return episodios.contains(episodio);
    }

	public ObjectNode toJson() {
		ObjectNode node = super.toJson();
		node.put("idDoctor", doctor!=null?doctor.getId():null);
		node.put("episodios", Episodio.listToJson(this.episodios));
		return node;
	}
}
