package security;

import actions.CorsComposition;
import actions.ForceHttps;
import actions.IntegrityCheck;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.StatusMessages;
import models.Usuario;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;

@CorsComposition.Cors
@ForceHttps.Https
@IntegrityCheck.Integrity
public class SecurityController extends Controller {

    public final static String AUTH_TOKEN_HEADER = "X-Auth-Token";
    public final static String HASH_HEADER = "X-Hash";
    public static final String AUTH_TOKEN = "token";
    public static final String DEVICE_HEADER = "X-Device";


    public static Usuario getUser() {
        return (Usuario)Http.Context.current().args.get("user");
    }

    @Transactional
    public static Result login() {
        String device = request().getHeader(DEVICE_HEADER);
        if(device != null && Token.validDevice(device)){
            JsonNode json = request().body().asJson();
            String password = json.findPath("password").textValue();
            String email = json.findPath("email").textValue().toLowerCase();
            String shaPassword = getSha512(password);
            List<Usuario> usuarios = JPA.em().createQuery("SELECT u FROM Usuario u WHERE u.email = ?1 AND u.password = ?2", Usuario.class).setParameter(1,email).setParameter(2,shaPassword).getResultList();
            if(usuarios.size() == 1) {
                Usuario usuario = usuarios.get(0);
                Token token = getTokenByUser(usuario, device);
                if(token==null){
                    token = generateToken(usuario , device);
                }
                ObjectNode authTokenJson = Json.newObject();
                authTokenJson.put(AUTH_TOKEN, token.getToken());
                authTokenJson.put("id", usuario.getId());
                authTokenJson.put("rol", usuario.getRol());
                return ok(authTokenJson);
            }
            else {
                return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_LOGIN_FAILED);
            }
        }
        else{
            return status(StatusMessages.C_UNAUTHORIZED, StatusMessages.M_UNSUPPORTED_DEVICE);
        }
    }

    @Security.Authenticated(SecuredGlobal.class)
    public static Result logout() {
        String device = request().getHeader(DEVICE_HEADER);
        deleteToken(getUser(), device);
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

    public static String getSha512(String value){
        try {
            SecretKeySpec secret_key;
            Mac sha256_HMAC = Mac.getInstance("HmacSHA512");
            System.out.println(System.getenv("SHA_KEY"));
            secret_key = new SecretKeySpec(System.getenv("SHA_KEY").getBytes(), "HmacSHA512");
            sha256_HMAC.init(secret_key);
            return Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(value.getBytes()));
        }
        catch(Exception e){
            return "";
        }
    }

    @Transactional
    private static Token generateToken(Usuario user, String device){
        try{
            Token tokenNuevo = new Token(user, device);
            JPA.withTransaction("default", false, new play.libs.F.Function0<Token>() {
                public Token apply() throws Throwable {
                    JPA.em().persist(tokenNuevo);
                    return tokenNuevo;
                }
            });
            return tokenNuevo;
        }
        catch(Throwable e){
            return null;
        }

    }

    @Transactional
    public static Usuario validateToken(String token, String device) {
        try {
            Usuario user = JPA.withTransaction("default", false, new play.libs.F.Function0<Usuario>() {
                public Usuario apply() throws Throwable {
                    List<Token> tokens = JPA.em().createQuery("SELECT u FROM Token u WHERE u.token = ?1 AND u.device = ?2", Token.class).setParameter(1, token).setParameter(2, device).getResultList();
                    if (tokens.size() == 1) {
                        return tokens.get(0).getUsuario();
                    }
                    return null;
                }
            });
            return user;
        }
        catch(Throwable e){
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    private static Token getTokenByUser(Usuario usuario, String device){
        try {
            Token token =  JPA.withTransaction("default", false, new play.libs.F.Function0<Token>() {
                public Token apply() throws Throwable{
                    List<Token> tokens = JPA.em().createQuery("SELECT u FROM Token u WHERE u.usuario = ?1 AND u.device = ?2", Token.class).setParameter(1, usuario).setParameter(2, device).getResultList();
                    if(tokens.size() == 1){
                        return tokens.get(0);
                    }
                    return null;
                }
            });
            return token;
        }
        catch(Throwable e){
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    private static void deleteToken(Usuario usuario, String device) {
        try{
            JPA.withTransaction("default", false, new play.libs.F.Function0() {
                public Boolean apply() throws Throwable {
                    JPA.em().createQuery("DELETE FROM Token u WHERE u.usuario = ?1 AND u.device = ?2").setParameter(1, usuario).setParameter(2,device).executeUpdate();
                    return true;
                }
            });
        }
        catch (Throwable e) {

        }
    }
}
