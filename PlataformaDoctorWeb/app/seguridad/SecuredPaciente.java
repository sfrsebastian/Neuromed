package seguridad;

import models.Usuario;
import play.mvc.Http;

public class SecuredPaciente extends Secured {

    public String getUsername(Http.Context ctx) {
        Usuario usuario = super.getUsuario(ctx);
        if(usuario != null && usuario.getRol().equals(Usuario.ROL_PACIENTE)) {
            System.out.println("Usuario validado: " + usuario.getId());
            ctx.current().args.put("user", usuario);
            return usuario.getNombre() + " " + usuario.getApellido();
        }
        return null;
    }
}
