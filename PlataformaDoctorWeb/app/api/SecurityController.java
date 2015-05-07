package api;

import actions.CorsComposition;
import actions.ForceHttps;
import actions.SecuredGlobal;
import com.amazonaws.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Usuario;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.*;

import javax.xml.ws.spi.http.HttpContext;
import java.util.List;

@CorsComposition.Cors
@With(ForceHttps.class)
public class SecurityController extends Controller {

    public final static String AUTH_TOKEN_HEADER = "X-Auth-Token";
    public final static String HASH_HEADER = "X-Hash";
    public static final String AUTH_TOKEN = "token";


    public static Usuario getUser() {
        return (Usuario)Http.Context.current().args.get("user");
    }

    @Transactional
    public static Result login() {
        JsonNode json = request().body().asJson();
        String password = json.findPath("password").textValue();
        String email = json.findPath("email").textValue();
        byte[] shaPassword = Usuario.getSha512(password);
        List<Usuario> usuarios = JPA.em().createQuery("SELECT u FROM Usuario u WHERE u.email = ?1 AND u.shaPassword = ?2", Usuario.class).setParameter(1,email).setParameter(2,shaPassword).getResultList();
        if(usuarios.size() == 1) {
            Usuario usuario = usuarios.get(0);
            String authToken = usuario.createToken();
            ObjectNode authTokenJson = Json.newObject();
            authTokenJson.put(AUTH_TOKEN, authToken);
            authTokenJson.put("id", usuario.getId());
            authTokenJson.put("rol", usuario.getRol());
            response().setCookie(AUTH_TOKEN, authToken);
            return ok(authTokenJson);
        }
        else {
            return ok("Usuario o contraseña invalidos");
        }
    }

    @Security.Authenticated(SecuredGlobal.class)
    public static Result logout() {
        response().discardCookie(AUTH_TOKEN);
        getUser().deleteAuthToken();
        return ok("Logged Out");
    }

    public static Result cors(String s1){
        return ok();
    }

    public static Result cors2(){
        return ok();
    }

    public static Result cors3(String s1, String s2){
        return ok();
    }

    public static Result cors4(String s1, String s2, String s3){
        return ok();
    }


    public static boolean validateOnlyMe(Long id){
        return getUser().getId()==id;
    }

    public static boolean validateOnlyMeWithDoctor(Usuario usuario){
        return getUser().getId()==usuario.getId() || !usuario.getRol().equals(Usuario.ROL_PACIENTE) ;
    }
}
