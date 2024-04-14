// kamel run --config configmap:camelk-config-timer-http-base64-kafka --resource secret:camelk-truststore -e KEYSTORE_PATH="file:///etc/camel/resources/camelk-truststore/truststore.jks" -e KEYSTORE_PASSWORD=password -e KEYSTORE_TYPE=PKCS12 Routes.java --dev
// camel-k: language=java dependency=mvn:org.apache.camel.quarkus:camel-quarkus-kafka dependency=mvn:org.apache.camel.quarkus:camel-quarkus-kafka dependency=mvn:org.apache.camel.quarkus:camel-quarkus-dataformat dependency=mvn:org.apache.camel.quarkus:camel-quarkus-base64 dependency=mvn:org.apache.camel.quarkus:camel-quarkus-http

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;

public class Routes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:hello?period={{timer.period}}").routeId("hello")
                .to("https://{{rest.url}}")
                .filter(simple("${body} contains 'foo'"))
                .to("log:foo")
                .end()
                .marshal().base64(-1, "", false)
                .to("kafka:test-topic");

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
        ksp.setResource(System.getenv("KEYSTORE_PATH"));
        ksp.setPassword(System.getenv("KEYSTORE_PASSWORD"));
        ksp.setType(System.getenv("KEYSTORE_TYPE"));
        return ksp;
    }

}
