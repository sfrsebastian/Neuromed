package actions;

import excepciones.StatusMessages;
import play.libs.F;
import play.mvc.*;
import seguridad.SecurityController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.security.MessageDigest;
import java.util.Base64;

public class IntegrityCheck  {

    @With(IntegrityAction.class)
    @Target({ ElementType.TYPE, ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Integrity {
        String value() default "*";
    }

    public static class IntegrityAction extends Action<Integrity> {

        @Override
        public F.Promise<Result> call(Http.Context context) throws Throwable {
            if(context.request().method().equals("POST") || context.request().method().equals("PUT")|| context.request().method().equals("DELETE")) {
                String hash = context.request().getHeader(SecurityController.HASH_HEADER);
                byte[] bytesOfMessage = context.request().body().asJson().toString().getBytes("UTF-8");
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] thedigest = md.digest(bytesOfMessage);
                String hashed = Base64.getEncoder().encodeToString(thedigest);
                boolean hashBien = hashed.equals(hash);
                System.out.println("body " + context.request().body().asJson().toString());
                System.out.println("Hash " + hashed);
                System.out.println("Hash Recibido " + hash);
                System.out.println("Hash bien " + hashBien);
                if (hashBien) {
                    return delegate.call(context);
                }
                else{
                    return F.Promise.promise(() -> status(StatusMessages.C_INTEGRITY_VIOLATION, StatusMessages.M_INTEGRITY_VIOLATION));
                }
            }
            return delegate.call(context);
        }
    }
}