package tk.project.orchestrator.goodsstorage.webclient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfiguration {
    private static final int TIMEOUT = 1000;

    @Bean
    public WebClient agreementWebClient(@Value("${agreement-service.host}") final String baseUrl,
                                        @Value("${agreement-service.timeout}") Integer timeout) {
        timeout = Objects.isNull(timeout) ? TIMEOUT : timeout;
        return webClientWithTimeout(baseUrl, timeout);
    }

    @Bean
    public WebClient deliveryWebClient(@Value("${delivery-service.host}") final String baseUrl,
                                       @Value("${delivery-service.timeout}") Integer timeout) {
        timeout = Objects.isNull(timeout) ? TIMEOUT : timeout;
        return webClientWithTimeout(baseUrl, timeout);
    }

    @Bean
    public WebClient notificationWebClient(@Value("${notification-service.host}") final String baseUrl,
                                           @Value("${notification-service.timeout}") Integer timeout) {
        timeout = Objects.isNull(timeout) ? TIMEOUT : timeout;
        return webClientWithTimeout(baseUrl, timeout);
    }

    @Bean
    public WebClient paymentWebClient(@Value("${payment-service.host}") final String baseUrl,
                                      @Value("${payment-service.timeout}") Integer timeout) {
        timeout = Objects.isNull(timeout) ? TIMEOUT : timeout;
        return webClientWithTimeout(baseUrl, timeout);
    }

    @Bean
    public WebClient productWebClient(@Value("${product-service.host}") final String baseUrl,
                                      @Value("${product-service.timeout}") Integer timeout) {
        timeout = Objects.isNull(timeout) ? TIMEOUT : timeout;
        return webClientWithTimeout(baseUrl, timeout);
    }

    private WebClient webClientWithTimeout(final String baseUrl, final Integer timeout) {
        final TcpClient tcpClient = TcpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(timeout, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(timeout, TimeUnit.MILLISECONDS));
                });

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .build();
    }
}
