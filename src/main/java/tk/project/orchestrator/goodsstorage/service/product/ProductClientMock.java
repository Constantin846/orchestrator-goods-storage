package tk.project.orchestrator.goodsstorage.service.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import tk.project.orchestrator.goodsstorage.dto.product.SetOrderStatusRequest;
import tk.project.orchestrator.goodsstorage.dto.product.SetOrderStatusResponse;
import tk.project.orchestrator.goodsstorage.enums.OrderStatus;

@Slf4j
@Service
@ConditionalOnExpression("'${app.product-client.type}'.equals('mock')")
public class ProductClientMock implements ProductClient {

    @Override
    public SetOrderStatusResponse sendRequestSetOrderStatus(final SetOrderStatusRequest orderStatusDto) {
        log.info("ProductServiceMock send request set order status");

        return SetOrderStatusResponse.builder()
                .status(OrderStatus.CONFIRMED.toString())
                .build();
    }
}
