package api;
import actions.CorsComposition;
import com.fasterxml.jackson.databind.JsonNode;
import models.Usuario;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
@CorsComposition.Cors
public class UsuarioApi extends Controller {

    @Transactional
    public static Result autenticar(){
        response().setHeader("Response-Syle","Json-Object");
        JsonNode json = request().body().asJson();
        String password = json.findPath("password").textValue();
        String email = json.findPath("email").textValue();
        List<Usuario> usuarios = JPA.em().createQuery("SELECT u FROM Usuario u WHERE u.email = ?1 AND u.password = ?2", Usuario.class).setParameter(1,email).setParameter(2,password).getResultList();
        if(usuarios.size() == 1) {
            return ok(usuarios.get(0).toJson());
        }
        else {
            return ok("Usuario o contraseña invalidos");
        }
    }

    public static Result preflight(String path) {
        return ok("");
    }
}
