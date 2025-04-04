package tk.project.orchestrator.goodsstorage.service.product;

import tk.project.orchestrator.goodsstorage.dto.product.SetOrderStatusDto;

public interface ProductService {
    String sendRequestSetOrderStatus(SetOrderStatusDto orderStatusDto);
}
