package it.unimi.distanceinair.server.config;

import org.apache.wss4j.common.WSS4JConstants;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.engine.WSSConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.callback.KeyStoreCallbackHandler;
import org.springframework.ws.soap.security.wss4j2.support.CryptoFactoryBean;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.io.IOException;
import java.util.List;

@EnableWs
@Configuration
public class SoapEndpointConfig extends WsConfigurerAdapter {
    // bean definitions

    private static final String NAMESPACE_URI = "http://www.unimi.it/distanceinair/";


    @Autowired
    AppProperties appProperties;

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "DistanceInAir")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema distanceInAirSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("GetDistanceInAir");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setSchema(distanceInAirSchema);
        wsdl11Definition.setTargetNamespace("http://www.unimi.it/distanceinair/");
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema distanceInAirSchema() {
        return new SimpleXsdSchema(new ClassPathResource("DistanceInAir.xsd"));
    }

    @Bean
    public CryptoFactoryBean getServerCryptoFactoryBean() throws IOException {
        CryptoFactoryBean cryptoFactoryBean = new CryptoFactoryBean();
        cryptoFactoryBean.setKeyStorePassword(appProperties.getServerPwd());
        cryptoFactoryBean.setKeyStoreLocation(appProperties.getServerKeystore());
        return cryptoFactoryBean;
    }

    @Bean
    public CryptoFactoryBean getClientCryptoFactoryBean() throws IOException {
        CryptoFactoryBean cryptoFactoryBean = new CryptoFactoryBean();
        cryptoFactoryBean.setKeyStorePassword(appProperties.getClientPwd());
        cryptoFactoryBean.setKeyStoreLocation(appProperties.getClientKeystore());
        return cryptoFactoryBean;
    }

    @Bean
    Wss4jSecurityInterceptor securityInterceptor() throws Exception {

        Wss4jSecurityInterceptor securityInterceptor = new Wss4jSecurityInterceptor();
        securityInterceptor.setValidationActions("Signature Encrypt UsernameToken");
        securityInterceptor.setSecurementEncryptionUser("studente-giovanni");
        securityInterceptor.setValidationSignatureCrypto(getClientCryptoFactoryBean().getObject());
        securityInterceptor.setValidationCallbackHandler(securityCallbackHandler());
        securityInterceptor.setValidationDecryptionCrypto(getServerCryptoFactoryBean().getObject());
        WSSConfig wssConfig = WSSConfig.getNewInstance();
        wssConfig.setValidator(WSConstants.USERNAME_TOKEN, AppUsernameTokenValidator.class);
        securityInterceptor.setWssConfig(wssConfig);


        securityInterceptor.setSecurementActions("Signature Encrypt");
        securityInterceptor.setSecurementUsername(appProperties.getServerPwd());
        securityInterceptor.setSecurementUsername("progetto-unimi");
        securityInterceptor.setSecurementPassword(appProperties.getServerPwd());
        securityInterceptor.setSecurementSignatureCrypto(getServerCryptoFactoryBean().getObject());
        securityInterceptor.setSecurementSignatureKeyIdentifier("IssuerSerial");
        securityInterceptor.setSecurementSignatureAlgorithm(WSS4JConstants.RSA_SHA1);
        securityInterceptor.setSecurementSignatureDigestAlgorithm(WSS4JConstants.SHA1);
        securityInterceptor.setSecurementEncryptionCrypto(getClientCryptoFactoryBean().getObject());

        return securityInterceptor;
    }

    @Bean
    public KeyStoreCallbackHandler securityCallbackHandler() {
        KeyStoreCallbackHandler callbackHandler = new KeyStoreCallbackHandler();
        callbackHandler.setPrivateKeyPassword(appProperties.getServerPwd());
        return callbackHandler;
    }

    @Bean
    PayloadLoggingInterceptor payloadLoggingInterceptor() {
        return new PayloadLoggingInterceptor();
    }


    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {

        try {
            interceptors.add(securityInterceptor());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        interceptors.add(payloadLoggingInterceptor());
    }

}


