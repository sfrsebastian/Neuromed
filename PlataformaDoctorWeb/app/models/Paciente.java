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

    private static final String APOS = "A+";
    private static final String ANEG = "A-";
    private static final String BPOS = "B+";
    private static final String BNEG = "B-";
    private static final String OPOS = "O+";
    private static final String ONEG = "O-";
    private static final String ABPOS = "AB+";
    private static final String ABNEG = "AB-";

	private double peso;

    private double altura;

    private String tipoSangre;

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
        super();
		this.rol=ROL_PACIENTE;
	}
	
	public Paciente(JsonNode node) throws UsuarioException{
		super(node);
        this.rol=ROL_PACIENTE;
        tipoSangre = node.path("tipoSangre").asText();
        peso = node.path("peso").asDouble();
        altura = node.path("altura").asDouble();
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

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public String getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public ObjectNode toJson() {
		ObjectNode node = super.toJson();
		node.put("idDoctor", doctor!=null?doctor.getId():null);
		node.put("episodios", Episodio.listToJson(this.episodios));
        node.put("tipoSangre", tipoSangre);
        node.put("peso", peso);
        node.put("altura", altura);
		return node;
	}
}
