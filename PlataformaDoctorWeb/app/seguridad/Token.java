package seguridad;

import models.Usuario;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity
public class Token implements Serializable {
    public static final String DEVICES[] = {"WEB", "IOS"};

    @Id
    @ManyToOne
    private Usuario usuario;

    @Id
    private String device;
    private String token;
    private Date expiracion;

    public Token(Usuario usuario, String device){
        this.usuario = usuario;
        expiracion = new Date(Calendar.getInstance().getTimeInMillis() + (long)(30 * 24 * 60 * 60 * 1000));
        token = SecurityController.getSha512(usuario.getId() + ":" + device + ":" + expiracion);
        this.device = device;
    }

    public Token(){

    }

    public String getToken(){
        return this.token;
    }

    public Usuario getUsuario(){
        return this.usuario;
    }

    public static boolean validDevice(String device){
        for (int i = 0; i<DEVICES.length; i++){
            if(DEVICES[i].equals(device)){
                return true;
            }
        }
        return false;
    }
}
