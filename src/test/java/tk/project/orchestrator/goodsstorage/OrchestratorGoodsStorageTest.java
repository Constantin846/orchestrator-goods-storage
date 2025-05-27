package tk.project.orchestrator.goodsstorage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import jakarta.ws.rs.core.MediaType;
import lombok.SneakyThrows;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import tk.project.orchestrator.goodsstorage.dto.AgreementDataDto;
import tk.project.orchestrator.goodsstorage.dto.ComplianceResultDto;
import tk.project.orchestrator.goodsstorage.dto.OrchestratorConfirmOrderDto;
import tk.project.orchestrator.goodsstorage.dto.delivery.DeliveryDateDto;
import tk.project.orchestrator.goodsstorage.dto.payment.PaymentResultDto;
import tk.project.orchestrator.goodsstorage.dto.product.SetOrderStatusRequest;
import tk.project.orchestrator.goodsstorage.dto.product.SetOrderStatusResponse;
import tk.project.orchestrator.goodsstorage.enums.ComplianceStatus;
import tk.project.orchestrator.goodsstorage.enums.OrderStatus;
import tk.project.orchestrator.goodsstorage.kafka.KafkaProducerImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.patchRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {OrchestratorGoodsStorage.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WireMockTest
class OrchestratorGoodsStorageTest implements KafkaTestContainer {
    @Autowired
    private KafkaProducerImpl kafkaProducer;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @RegisterExtension
    private static final WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().dynamicPort().dynamicPort())
            .build();

    @DynamicPropertySource
    private static void setWireMockExtension(DynamicPropertyRegistry registry) {
        registry.add("agreement-service.host", wireMockExtension::baseUrl);
        registry.add("delivery-service.host", wireMockExtension::baseUrl);
        registry.add("notification-service.host", wireMockExtension::baseUrl);
        registry.add("payment-service.host", wireMockExtension::baseUrl);
        registry.add("product-service.host", wireMockExtension::baseUrl);
    }

    @SneakyThrows
    @Test
    void confirmOrder() {
        // GIVEN
        final OrchestratorConfirmOrderDto orderDto = createOrchestratorConfirmOrderDto();
        final AgreementDataDto sendAgreementData = createAgreementDataDto(orderDto);
        final SetOrderStatusRequest orderStatusRequest = createSetOrderStatusRequest(orderDto);
        addWireMockExtensionStubs();

        // WHEN
        final String result = mockMvc.perform(post("/confirm-order")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final Map<String, String> mapBusinessKey = objectMapper.readValue(result, Map.class);
        final UUID businessKey = UUID.fromString(mapBusinessKey.get("businessKey"));

        // THEN
        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(2, TimeUnit.SECONDS)
                .untilAsserted(() -> wireMockExtension.verify(postRequestedFor(
                        urlEqualTo("/agreement"))
                        .withRequestBody(equalToJson(objectMapper.writeValueAsString(sendAgreementData)))));


        // WHEN
        final ComplianceResultDto complianceResultDto = createComplianceResultDto(businessKey);
        kafkaProducer.sendMessage(kafkaProducer.getComplianceInfoTopic(), businessKey.toString(), complianceResultDto);

        // THEN
        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(2, TimeUnit.SECONDS)
                .untilAsserted(() -> wireMockExtension.verify(patchRequestedFor(
                        urlEqualTo("/gs/order/set-status"))
                        .withRequestBody(equalToJson(objectMapper.writeValueAsString(orderStatusRequest)))));
    }

    private final LocalDate deliveryDate = LocalDate.now();

    @SneakyThrows
    private void addWireMockExtensionStubs() {

        addWireMockExtensionStubPost("/agreement", "agreementId");

        final DeliveryDateDto deliveryDateDto = DeliveryDateDto.builder()
                .date(deliveryDate)
                .build();
        addWireMockExtensionStubPost("/delivery", deliveryDateDto);

        addWireMockExtensionStubPost("/send-message", "ok");

        final PaymentResultDto paymentResultDto = PaymentResultDto.builder()
                .isSuccessful(true)
                .build();
        addWireMockExtensionStubPost("/pay-for-order", paymentResultDto);

        final SetOrderStatusResponse orderStatusResponse = SetOrderStatusResponse.builder()
                .status(OrderStatus.CONFIRMED.toString())
                .build();
        wireMockExtension.stubFor(
                WireMock.patch(urlMatching("/gs/order/set-status"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON)
                                .withBody(objectMapper.writeValueAsString(orderStatusResponse)))
        );
    }

    @SneakyThrows
    private void addWireMockExtensionStubPost(final String url, final Object responseBody) {
        wireMockExtension.stubFor(
                WireMock.post(url)
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON)
                                .withBody(objectMapper.writeValueAsString(responseBody)))
        );
    }

    private OrchestratorConfirmOrderDto createOrchestratorConfirmOrderDto() {
        return OrchestratorConfirmOrderDto.builder()
                .id(UUID.randomUUID())
                .deliveryAddress("address")
                .customerInn("123456")
                .customerAccountNumber("12345678")
                .price(BigDecimal.valueOf(111.11))
                .customerLogin("login")
                .build();
    }

    private AgreementDataDto createAgreementDataDto(final OrchestratorConfirmOrderDto orderDto) {
        return AgreementDataDto.builder()
                .inn(orderDto.getCustomerInn())
                .accountNumber(orderDto.getCustomerAccountNumber())
                .build();
    }

    private SetOrderStatusRequest createSetOrderStatusRequest(final OrchestratorConfirmOrderDto orderDto) {
        return SetOrderStatusRequest.builder()
                .orderId(orderDto.getId())
                .status(OrderStatus.CONFIRMED)
                .deliveryDate(deliveryDate)
                .build();
    }

    private ComplianceResultDto createComplianceResultDto(UUID businessKey) {
        return ComplianceResultDto.builder()
                .businessKey(businessKey)
                .complianceStatus(ComplianceStatus.SUCCESS)
                .build();
    }
}