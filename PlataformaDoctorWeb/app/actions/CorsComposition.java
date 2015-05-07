package actions;

import excepciones.StatusMessages;
import play.libs.F;
import play.mvc.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class CorsComposition  {

    @With(CorsAction.class)
    @Target({ ElementType.TYPE, ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Cors {
        String value() default "*";
    }

    public static class CorsAction extends Action<Cors> {

        @Override
        public F.Promise<Result> call(Http.Context context) throws Throwable {
            Http.Response response = context.response();
            System.out.println("entro cors");
            response.setHeader("Access-Control-Allow-Origin", "*");
            if(context.request().method().equals("OPTIONS")) {
                System.out.println(context.request().toString());
                response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
                response.setHeader("Access-Control-Max-Age", "3600");
                response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization, X-Auth-Token");
                response.setHeader("Access-Control-Allow-Credentials", "true");
                System.out.println("reponder cors");
                return F.Promise.promise(() -> ok());
            }
            else if(context.request().method().equals("POST") || context.request().method().equals("PUT")|| context.request().method().equals("GET") || context.request().method().equals("DELETE")){
                return delegate.call(context);
            }
            else{
                return F.Promise.promise(() -> ok());
            }
        }
    }
}