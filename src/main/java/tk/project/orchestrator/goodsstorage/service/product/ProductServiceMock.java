package tk.project.orchestrator.goodsstorage.service.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import tk.project.orchestrator.goodsstorage.dto.product.OrderStatus;
import tk.project.orchestrator.goodsstorage.dto.product.SetOrderStatusRequest;
import tk.project.orchestrator.goodsstorage.dto.product.SetOrderStatusResponse;

@Slf4j
@Service
@ConditionalOnExpression("'${app.product-service.type}'.equals('mock')")
public class ProductServiceMock implements ProductService {

    @Override
    public SetOrderStatusResponse sendRequestSetOrderStatus(SetOrderStatusRequest orderStatusDto) {
        log.info("ProductServiceMock send request set order status");
        SetOrderStatusResponse response = new SetOrderStatusResponse();
        response.setStatus(OrderStatus.CONFIRMED.toString());
        return response;
    }
}
