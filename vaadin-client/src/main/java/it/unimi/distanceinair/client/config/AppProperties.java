package it.unimi.distanceinair.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@Data
@ConfigurationProperties(prefix = "soap")
public class AppProperties {
    private boolean soapActive = false;
    private String clientPwd = "";
    private String serverPwd = "";
    private Resource serverKeystore;
    private Resource clientKeystore;
    private String clientAlias = "";
    private String serverAlias = "";
    private String apiUrl = "";
}
