package tk.project.orchestrator.goodsstorage.service.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import tk.project.orchestrator.goodsstorage.dto.product.SetOrderStatusDto;

@Slf4j
@Service
@ConditionalOnExpression("'${app.product-service.type}'.equals('mock')")
public class ProductServiceMock implements ProductService {

    @Override
    public String sendRequestSetOrderStatus(SetOrderStatusDto orderStatusDto) {
        return "ok";
    }
}
