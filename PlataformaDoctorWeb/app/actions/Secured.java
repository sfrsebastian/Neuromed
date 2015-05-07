package actions;

import api.SecurityController;
import excepciones.StatusMessages;
import models.Usuario;
import org.apache.commons.codec.binary.Base64;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;
import sun.misc.BASE64Decoder;
import sun.security.provider.MD5;

import javax.persistence.Convert;
import java.security.MessageDigest;

public abstract class Secured extends Security.Authenticator {

    public Usuario getUsuario(Context ctx){
        Usuario usuario;
        String[] authTokenHeaderValues = ctx.request().headers().get(SecurityController.AUTH_TOKEN_HEADER);
        String[] hashHeaderValues = ctx.request().headers().get(SecurityController.HASH_HEADER);
        boolean hashbien = false;
        try{
            byte[] bytesOfMessage = ctx.request().body().asJson().toString().getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesOfMessage);
            String hash = Base64.encodeBase64String(thedigest);
            System.out.println("body " + ctx.request().body().asJson().toString());
            System.out.println("Hash " + hash);
            System.out.println("Hash Recibido " + hashHeaderValues[0]);
            hashbien = hash.equals(hashHeaderValues[0]);
            System.out.println("Hash bien " + hashbien);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }

        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null) && hashbien) {
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