package tk.project.orchestrator.goodsstorage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import jakarta.ws.rs.core.MediaType;
import lombok.SneakyThrows;
import org.awaitility.Awaitility;
import org.camunda.bpm.engine.RuntimeService;
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
import tk.project.orchestrator.goodsstorage.dto.ComplianceStatus;
import tk.project.orchestrator.goodsstorage.dto.OrchestratorConfirmOrderDto;
import tk.project.orchestrator.goodsstorage.dto.delivery.DeliveryDateDto;
import tk.project.orchestrator.goodsstorage.dto.payment.PaymentResultDto;
import tk.project.orchestrator.goodsstorage.dto.product.OrderStatus;
import tk.project.orchestrator.goodsstorage.dto.product.SetOrderStatusRequest;
import tk.project.orchestrator.goodsstorage.dto.product.SetOrderStatusResponse;
import tk.project.orchestrator.goodsstorage.service.agreement.AgreementServiceImpl;
import tk.project.orchestrator.goodsstorage.service.product.ProductServiceImpl;

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
class OrchestratorGoodsStorageTest {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private AgreementServiceImpl agreementService;
    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @RegisterExtension
    private static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
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
        OrchestratorConfirmOrderDto orderDto = createOrchestratorConfirmOrderDto();
        AgreementDataDto sendAgreementData = createAgreementDataDto(orderDto);
        SetOrderStatusRequest orderStatusRequest = createSetOrderStatusRequest(orderDto);
        addWireMockExtensionStubs();

        String result = mockMvc.perform(post("/confirm-order")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, String> mapBusinessKey = objectMapper.readValue(result, Map.class);
        UUID businessKey = UUID.fromString(mapBusinessKey.get("businessKey"));

        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(2, TimeUnit.SECONDS)
                .untilAsserted(() -> wireMockExtension.verify(postRequestedFor(
                        urlEqualTo("/agreement"))
                        .withRequestBody(equalToJson(objectMapper.writeValueAsString(sendAgreementData)))));


        ComplianceResultDto complianceResultDto = createComplianceResultDto(businessKey);
        String resultContinue = mockMvc.perform(post("/confirm-order/continue")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(complianceResultDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

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

        DeliveryDateDto deliveryDateDto = new DeliveryDateDto();
        deliveryDateDto.setDate(deliveryDate);
        addWireMockExtensionStubPost("/delivery", deliveryDateDto);

        addWireMockExtensionStubPost("/send-message", "ok");

        PaymentResultDto paymentResultDto = new PaymentResultDto();
        paymentResultDto.setIsSuccessful(true);
        addWireMockExtensionStubPost("/pay-for-order", paymentResultDto);

        SetOrderStatusResponse orderStatusResponse = new SetOrderStatusResponse();
        orderStatusResponse.setStatus(OrderStatus.CONFIRMED.toString());
        wireMockExtension.stubFor(
                WireMock.patch(urlMatching("/gs/order/set-status"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON)
                                .withBody(objectMapper.writeValueAsString(orderStatusResponse)))
        );

    }

    @SneakyThrows
    private void addWireMockExtensionStubPost(String url, Object responseBody) {
        wireMockExtension.stubFor(
                WireMock.post(url)
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON)
                                .withBody(objectMapper.writeValueAsString(responseBody)))
        );
    }

    private OrchestratorConfirmOrderDto createOrchestratorConfirmOrderDto() {
        OrchestratorConfirmOrderDto orderDto = new OrchestratorConfirmOrderDto();
        orderDto.setId(UUID.randomUUID());
        orderDto.setDeliveryAddress("address");
        orderDto.setCustomerInn("123456");
        orderDto.setCustomerAccountNumber("12345678");
        orderDto.setPrice(BigDecimal.valueOf(111.11));
        orderDto.setCustomerLogin("login");
        return orderDto;
    }

    private AgreementDataDto createAgreementDataDto(OrchestratorConfirmOrderDto orderDto) {
        AgreementDataDto agreementDataDto = new AgreementDataDto();
        agreementDataDto.setInn(orderDto.getCustomerInn());
        agreementDataDto.setAccountNumber(orderDto.getCustomerAccountNumber());
        return agreementDataDto;
    }

    private SetOrderStatusRequest createSetOrderStatusRequest(OrchestratorConfirmOrderDto orderDto) {
        SetOrderStatusRequest orderStatusRequest = new SetOrderStatusRequest();
        orderStatusRequest.setOrderId(orderDto.getId());
        orderStatusRequest.setStatus(OrderStatus.CONFIRMED);
        orderStatusRequest.setDeliveryDate(deliveryDate);
        return orderStatusRequest;
    }

    private ComplianceResultDto createComplianceResultDto(UUID businessKey) {
        ComplianceResultDto complianceResultDto = new ComplianceResultDto();
        complianceResultDto.setBusinessKey(businessKey);
        complianceResultDto.setComplianceStatus(ComplianceStatus.SUCCESS);
        return complianceResultDto;
    }
}