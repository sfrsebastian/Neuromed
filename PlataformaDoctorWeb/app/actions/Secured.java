package actions;

import api.SecurityController;
import excepciones.StatusMessages;
import models.Usuario;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public abstract class Secured extends Security.Authenticator {

    public Usuario getUsuario(Context ctx){
        Usuario usuario;
        String[] authTokenHeaderValues = ctx.request().headers().get(SecurityController.AUTH_TOKEN_HEADER);
        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {
            usuario = Usuario.findByAuthToken(authTokenHeaderValues[0]);
            if(usuario != null){
                return usuario;
            }
        }
        return null;
    }

    public Result onUnauthorized(Context ctx) {
        return status(StatusMessages.C_UNAUTHORIZED, StatusMessages.M_UNAUTHORIZED);
    }
}