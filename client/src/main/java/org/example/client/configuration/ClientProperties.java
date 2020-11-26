package org.example.client.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component()
@ConfigurationProperties("game")
public class ClientProperties {

    private String serverUrl;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        if (serverUrl != null && !serverUrl.isBlank()) {
            this.serverUrl = serverUrl;
        }
    }
}
