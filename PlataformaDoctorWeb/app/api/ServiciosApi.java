package api;

import java.util.List;

import actions.CorsComposition;
import actions.ForceHttps;
import exceptions.StatusMessages;
import models.*;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.*;
import security.*;

@CorsComposition.Cors
@ForceHttps.Https
@IntegrityCheck.Integrity
@Security.Authenticated(SecuredGlobal.class)
@Transactional
public class ServiciosApi extends Controller {

    public static Result darMedicamentos(){
        response().setHeader("Response-Syle", "Json-Array");
        List<Medicamento> medicamentos = JPA.em().createQuery("SELECT u FROM Medicamento u", Medicamento.class).getResultList();
        return ok(Medicamento.listToJson(medicamentos));
    }

    public static Result crearMedicamento(){
        try{
            Medicamento nuevo = new Medicamento(request().body().asJson());
            response().setHeader("Response-Syle","Json-Object");
            JPA.em().persist(nuevo);
            return status(StatusMessages.C_CREATED, nuevo.toJson());
        }
        catch(Exception p) {
            p.printStackTrace();
            return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
        }
    }

    public static Result darCausas(){
        response().setHeader("Response-Syle", "Json-Array");
        List<Causa> causas = JPA.em().createQuery("SELECT u FROM Causa u", Causa.class).getResultList();
        return ok(Causa.listToJson(causas));
    }

    public static Result crearCausa(){
        try{
            Causa nuevo = new Causa(request().body().asJson());
            response().setHeader("Response-Syle","Json-Object");
            JPA.em().persist(nuevo);
            return status(StatusMessages.C_CREATED, nuevo.toJson());
        }
        catch(Exception p) {
            p.printStackTrace();
            return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
        }
    }

    public static Result darIntervalos(){
        response().setHeader("Response-Syle", "Json-Array");
        List<Intervalo> intervalos = JPA.em().createQuery("SELECT u FROM Intervalo u", Intervalo.class).getResultList();
        return ok(Intervalo.listToJson(intervalos));
    }

    public static Result crearIntervalo(){
        try{
            Intervalo nuevo = new Intervalo(request().body().asJson());
            response().setHeader("Response-Syle","Json-Object");
            JPA.em().persist(nuevo);
            return status(StatusMessages.C_CREATED, nuevo.toJson());
        }
        catch(Exception p) {
            p.printStackTrace();
            return status(StatusMessages.C_BAD_REQUEST, StatusMessages.M_INCORRECT_PARAMS);
        }
    }

}
