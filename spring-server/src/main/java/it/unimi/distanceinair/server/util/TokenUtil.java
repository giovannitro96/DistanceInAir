package it.unimi.distanceinair.server.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.gson.io.GsonDeserializer;
import it.unimi.distanceinair.server.config.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Optional;

@Component
@Slf4j
public class TokenUtil {
    @Autowired
    AppProperties appProperties;
    public Optional<RSAPublicKey> getParsedPublicKey() {
        String PUB_KEY = appProperties.getPublicKey();
        String PUBLIC_KEY = PUB_KEY.replace(" ", "");

        try {
            byte[] decode = Base64.decodeBase64(PUBLIC_KEY);
            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(decode);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKey pubKey = (RSAPublicKey) keyFactory.generatePublic(keySpecX509);
            return Optional.of(pubKey);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            System.out.println("Exception block | Public key parsing error ");
            return Optional.empty();
        }
    }
    public Claims getAllClaimsFromToken(String token) {
        Claims claims;
        Gson gson = new Gson();
        try {
            claims = Jwts.parser().deserializeJsonWith(new GsonDeserializer<>(gson))
                    .setSigningKey(getParsedPublicKey().get())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Could not get all claims Token from passed token");
            claims = null;
        }
        return claims;
    }
}
