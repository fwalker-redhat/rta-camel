package com.example;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class Routes extends RouteBuilder {

    @ConfigProperty(name = "camel.keystore.path")
    String keystorePath;

    @ConfigProperty(name = "camel.keystore.password")
    String keystorePassword;

    @ConfigProperty(name = "camel.keystore.type")
    String keystoreType;

    @Override
    public void configure() throws Exception {
        from("timer:hello?period={{timer.period}}").routeId("hello")
                .to("https://{{rest.url}}")
                .filter(simple("${body} contains 'foo'"))
                .to("log:foo")
                .end()
                .marshal().base64(-1, "", false)
                .to("kafka:test-topic?brokers={{kafka.brokers}}");

        configTrustManager();
    }

    private void configTrustManager() {
        KeyStoreParameters ksp = getKeyStoreParameters();
        TrustManagersParameters tmp = getTrustManagersParameters(ksp);
        SSLContextParameters scp = getSslContextParameters(tmp);
        HttpComponent httpComponent = getContext().getComponent("https", HttpComponent.class);
        httpComponent.setSslContextParameters(scp);
    }

    private static SSLContextParameters getSslContextParameters(TrustManagersParameters tmp) {
        SSLContextParameters scp = new SSLContextParameters();
        scp.setTrustManagers(tmp);
        return scp;
    }

    private static TrustManagersParameters getTrustManagersParameters(KeyStoreParameters ksp) {
        TrustManagersParameters tmp = new TrustManagersParameters();
        tmp.setKeyStore(ksp);
        return tmp;
    }

    private KeyStoreParameters getKeyStoreParameters() {
        KeyStoreParameters ksp = new KeyStoreParameters();
        ksp.setResource(keystorePath);
        ksp.setPassword(keystorePassword);
        ksp.setType(keystoreType);
        return ksp;
    }

}
