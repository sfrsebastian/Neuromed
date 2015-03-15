package models;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.JsonNode;

import excepciones.UsuarioException;

@Entity
@Table(name="Doctores")
public class Doctor extends Usuario{

    private boolean autorizado;

	public Doctor(){
		super();
	}

    @PrePersist
    public void prePersist() {
        super.prePersist();
        setAutorizado(false);
    }

    public Doctor (JsonNode node) throws UsuarioException {
        super(node);
    }

    public boolean isAutorizado() {
        return autorizado;
    }

    public void setAutorizado(boolean autorizado) {
        this.autorizado = autorizado;
    }
}
