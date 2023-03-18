package it.unimi.distanceinair.client.config;

import it.unimi.distanceinair.client.service.ServerApis;
import org.apache.wss4j.common.WSS4JConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.soap.security.support.KeyManagersFactoryBean;
import org.springframework.ws.soap.security.support.KeyStoreFactoryBean;
import org.springframework.ws.soap.security.support.TrustManagersFactoryBean;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.callback.KeyStoreCallbackHandler;
import org.springframework.ws.soap.security.wss4j2.support.CryptoFactoryBean;
import org.springframework.ws.transport.http.HttpsUrlConnectionMessageSender;

import java.io.IOException;

@Configuration
class SoapConfiguration extends WsConfigurerAdapter {

    @Autowired
    AppProperties appProperties;

    @Bean
    Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("it.unimi.distanceinair.client.domain.xml");
        return marshaller;
    }

    @Bean
    KeyStoreFactoryBean keyStore() {
        KeyStoreFactoryBean factoryBean = new KeyStoreFactoryBean();
        factoryBean.setLocation(appProperties.getServerKeystore());
        factoryBean.setPassword(appProperties.getServerPwd());
        return factoryBean;
    }

    @Bean
    TrustManagersFactoryBean trustManagers(KeyStoreFactoryBean keyStore) {
        TrustManagersFactoryBean factoryBean = new TrustManagersFactoryBean();
        factoryBean.setKeyStore(keyStore.getObject());
        return factoryBean;
    }

    @Bean
    HttpsUrlConnectionMessageSender messageSender(
            KeyStoreFactoryBean keyStore,
            TrustManagersFactoryBean trustManagers
    ) throws Exception {
        HttpsUrlConnectionMessageSender sender = new HttpsUrlConnectionMessageSender();

        KeyManagersFactoryBean keyManagersFactoryBean = new KeyManagersFactoryBean();
        keyManagersFactoryBean.setKeyStore(keyStore.getObject());
        keyManagersFactoryBean.setPassword(appProperties.getServerPwd());
        keyManagersFactoryBean.afterPropertiesSet();

        sender.setKeyManagers(keyManagersFactoryBean.getObject());

        sender.setTrustManagers(trustManagers.getObject());
        return sender;
    }

    @Bean
    CryptoFactoryBean getServerCryptoFactoryBean() throws IOException {
        CryptoFactoryBean cryptoFactoryBean = new CryptoFactoryBean();
        cryptoFactoryBean.setKeyStoreLocation(appProperties.getServerKeystore());
        cryptoFactoryBean.setKeyStorePassword(appProperties.getServerPwd());
        return cryptoFactoryBean;
    }

    @Bean
    CryptoFactoryBean getClientCryptoFactoryBean() throws IOException {
        CryptoFactoryBean cryptoFactoryBean = new CryptoFactoryBean();
        cryptoFactoryBean.setKeyStoreLocation(appProperties.getClientKeystore());
        cryptoFactoryBean.setKeyStorePassword(appProperties.getClientPwd());
        return cryptoFactoryBean;
    }

   @Bean
    Wss4jSecurityInterceptor securityInterceptor() throws Exception {
        Wss4jSecurityInterceptor securityInterceptor = new Wss4jSecurityInterceptor();

        securityInterceptor.setSecurementActions("Signature Encrypt UsernameToken");

        securityInterceptor.setSecurementUsername(appProperties.getClientAlias());
        securityInterceptor.setSecurementPassword(appProperties.getClientPwd());
        securityInterceptor.setSecurementEncryptionUser(appProperties.getServerAlias());
        securityInterceptor.setSecurementSignatureKeyIdentifier("IssuerSerial");
        securityInterceptor.setSecurementSignatureAlgorithm(WSS4JConstants.RSA_SHA1);
        securityInterceptor.setSecurementSignatureDigestAlgorithm(WSS4JConstants.SHA1);
        securityInterceptor.setSecurementEncryptionCrypto(getServerCryptoFactoryBean().getObject());
        securityInterceptor.setSecurementSignatureCrypto(getClientCryptoFactoryBean().getObject());

        securityInterceptor.setValidationActions("Signature Encrypt");
        securityInterceptor.setValidationSignatureCrypto(getServerCryptoFactoryBean().getObject());
        securityInterceptor.setValidationCallbackHandler(securityCallbackHandler());
        securityInterceptor.setValidationDecryptionCrypto(getClientCryptoFactoryBean().getObject());

        return securityInterceptor;
    }

    @Bean
    public KeyStoreCallbackHandler securityCallbackHandler() {
        KeyStoreCallbackHandler callbackHandler = new KeyStoreCallbackHandler();
        callbackHandler.setPrivateKeyPassword(appProperties.getServerPwd());
        return callbackHandler;
    }

    @Bean
    ServerApis countryClient(
            Jaxb2Marshaller marshaller,
            HttpsUrlConnectionMessageSender messageSender,
            Wss4jSecurityInterceptor securityInterceptor
    ) {
        ServerApis serverApis = new ServerApis();

        serverApis.setInterceptors(new ClientInterceptor[]{securityInterceptor});
        serverApis.setMessageSender(messageSender);

        serverApis.setMarshaller(marshaller);
        serverApis.setUnmarshaller(marshaller);

        return serverApis;
    }

}
