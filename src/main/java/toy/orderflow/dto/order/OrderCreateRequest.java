package toy.orderflow.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record OrderCreateRequest(
        @NotBlank String ordererName,
        @NotEmpty @Valid List<OrderItemRequest> items
) {
    public record OrderItemRequest(
            @NotNull Long productId,
            @Positive int quantity
    ) {
    }
}
