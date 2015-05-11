package exceptions;

/**
 * Created by sfrsebastian on 4/18/15.
 */
public class StatusMessages {
    //Status Codes
    public static final int C_UNAUTHORIZED = 401;
    public static final int C_CREATED = 201;
    public static final int C_BAD_REQUEST = 400;
    public static final int C_INTERNAL_SERVER_ERROR = 500;
    public static final int C_SSL_REQUIRED = 403;
    public static final int C_INTEGRITY_VIOLATION = 403;


    //Status Messages
    public static final String M_UNAUTHORIZED = "Acceso no autorizado a recurso";
    public static final String M_SUCCESS = "Solicitud exitosa";
    public static final String M_INCORRECT_PARAMS = "Parametros incorrectos, deberias volver a realizar solicitud con valores distintos";
    public static final String M_SERVER_EXCEPTION = "Error procesando la solicitud";
    public static final String M_SSL_REQUIRED = "La conexion debe ser SSL";
    public static final String M_INTEGRITY_VIOLATION = "Error de seguridad con conexion. Se violó la integridad de los datos";
    public static final String M_METODO_NO_AUTORIZADO = "Método no autorizado";
    public static final String M_LOGIN_FAILED = "Usuario y/o contraseña incorrecto";
    public static final String M_UNSUPPORTED_DEVICE = "Dispositivo no válido" ;
}