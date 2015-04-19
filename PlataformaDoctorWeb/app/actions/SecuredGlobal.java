package actions;

import models.Usuario;
import play.mvc.Http;

public class SecuredGlobal extends Secured {

    public String getUsername(Http.Context ctx) {
        Usuario usuario = super.getUsuario(ctx);
        if(usuario != null) {
            System.out.println("Usuario validado: " + usuario.getId());
            ctx.current().args.put("user", usuario);
            return usuario.getNombre() + " " + usuario.getApellido();
        }
        return null;
    }
}
