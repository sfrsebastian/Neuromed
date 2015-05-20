package security;

import exceptions.StatusMessages;
import org.apache.commons.codec.binary.Hex;
import play.libs.F;
import play.mvc.*;
import security.SecurityController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.security.MessageDigest;

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
            String device = context.request().getHeader(SecurityController.DEVICE_HEADER);
            if(!device.equals("IOS") && (context.request().method().equals("POST") || context.request().method().equals("PUT")|| context.request().method().equals("DELETE"))) {
                String hash = context.request().getHeader(SecurityController.HASH_HEADER);
                Http.RequestBody body = context.request().body();
                System.out.println(body.asText());
                boolean hashBien = true;
                if(body.asJson() != null) {
                    byte[] bytesOfMessage = body.asJson().toString().getBytes("UTF-8");
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] theDigest = md.digest(bytesOfMessage);
                    String hashed = Hex.encodeHexString(theDigest);
                    hashBien = hashed.equals(hash);
                    System.out.println("body " + context.request().body().asJson().toString());
                    System.out.println("Hash " + hashed);
                    System.out.println("Hash Recibido " + hash);
                }
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