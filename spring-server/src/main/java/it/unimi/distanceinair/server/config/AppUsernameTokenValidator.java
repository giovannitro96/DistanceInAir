package it.unimi.distanceinair.server.config;

import lombok.RequiredArgsConstructor;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.dom.handler.RequestData;
import org.apache.wss4j.dom.message.token.UsernameToken;
import org.apache.wss4j.dom.validate.Credential;
import org.apache.wss4j.dom.validate.Validator;
import org.apache.xml.security.utils.XMLUtils;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.apache.wss4j.common.util.UsernameTokenUtil.doPasswordDigest;

@Configuration
@RequiredArgsConstructor
public class AppUsernameTokenValidator implements Validator {

    final AppProperties appProperties;

    @Override
    public Credential validate(Credential credential, RequestData requestData) throws WSSecurityException {
        if (requestData.getCallbackHandler() == null) {
            throw new WSSecurityException(WSSecurityException.ErrorCode.FAILURE, "noCallback");
        }
        UsernameToken usernameToken = credential.getUsernametoken();
        String user = usernameToken.getName();
        String password = usernameToken.getPassword();
        String nonce = usernameToken.getNonce();
        String createdTime = usernameToken.getCreated();
        String pwType = usernameToken.getPasswordType();
        boolean passwordsAreEncoded = usernameToken.getPasswordsAreEncoded();

        WSPasswordCallback pwCb =
                new WSPasswordCallback(user, null, pwType, WSPasswordCallback.USERNAME_TOKEN);
        try {
            SamplePasswordCallback passwordCallback = new SamplePasswordCallback();
            passwordCallback.handle(new Callback[]{pwCb});
        } catch (IOException | UnsupportedCallbackException e) {
            throw new WSSecurityException(
                    WSSecurityException.ErrorCode.FAILED_AUTHENTICATION, e
            );
        }
        String origPassword = pwCb.getPassword();
        if (origPassword == null) {

            throw new WSSecurityException(WSSecurityException.ErrorCode.FAILED_AUTHENTICATION);
        }
        if (usernameToken.isHashed()) {
            String passDigest;
            if (passwordsAreEncoded) {
                passDigest = doPasswordDigest(XMLUtils.decode(nonce), createdTime,
                        Base64.getMimeDecoder().decode(origPassword));
            } else {
                passDigest = doPasswordDigest(XMLUtils.decode(nonce), createdTime, origPassword.getBytes(StandardCharsets.UTF_8));
            }
            if (!passDigest.equals(password)) {
                throw new WSSecurityException(WSSecurityException.ErrorCode.FAILED_AUTHENTICATION);
            }
        } else {
            if (!origPassword.equals(password)) {
                throw new WSSecurityException(WSSecurityException.ErrorCode.FAILED_AUTHENTICATION);
            }
        }
        return credential;
    }

    public class SamplePasswordCallback implements CallbackHandler {

        private static final Map<String, String> userPasswords = new HashMap<String, String>();
        private final Map<String, String> keyPasswords = new HashMap<String, String>();

        public SamplePasswordCallback() {
            // some example user passwords
            userPasswords.put(appProperties.getClientAlias(), appProperties.getClientPwd());
        }

        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
            for (Callback callback : callbacks) {
                WSPasswordCallback pcb = (WSPasswordCallback) callback;
                String id = pcb.getIdentifier();
                String pass = userPasswords.get(id);
                pcb.setPassword(pass);

            }
        }
    }
}
