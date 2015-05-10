package seguridad;

import excepciones.StatusMessages;
import models.Usuario;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public abstract class Secured extends Security.Authenticator {

    public Usuario getUsuario(Context ctx) {
        String authToken = ctx.request().getHeader(SecurityController.AUTH_TOKEN_HEADER);
        String device = ctx.request().getHeader(SecurityController.DEVICE_HEADER);

        if (authToken != null && device != null) {
            Usuario usuario = SecurityController.validateToken(authToken, device);
            if (usuario != null) {
                return usuario;
            }
        }
        return null;
    }

    public Result onUnauthorized(Context ctx) {
        return status(StatusMessages.C_UNAUTHORIZED, StatusMessages.M_UNAUTHORIZED);
    }
}