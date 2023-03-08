package it.unimi.distanceinair.server.config;

import com.google.gson.JsonParser;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Configuration
@Data
@ConfigurationProperties(prefix = "application")
public class AppProperties {

    private String accessToken = "";
    private String clientPwd = "";
    private String serverPwd = "";
    private Resource serverKeystore;
    private Resource clientKeystore;
    private String keycloakUrl = "";
    private String clientAlias = "";
    private String serverAlias = "";
    private String publicKey = "";

    @PostConstruct
    public void init() {
        setPublicKey(new JsonParser().parse(Objects.requireNonNull(new RestTemplate().getForObject(getKeycloakUrl(), String.class))).getAsJsonObject().get("public_key").getAsString());
    }
}
