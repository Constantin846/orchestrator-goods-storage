package tk.project.orchestrator.goodsstorage.service.product;

import tk.project.orchestrator.goodsstorage.dto.product.SetOrderStatusRequest;
import tk.project.orchestrator.goodsstorage.dto.product.SetOrderStatusResponse;

public interface ProductClient {
    SetOrderStatusResponse sendRequestSetOrderStatus(final SetOrderStatusRequest orderStatusDto);
}
