package toy.orderflow.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.orderflow.domain.product.Product;
import toy.orderflow.dto.product.ProductCreateRequest;
import toy.orderflow.dto.product.ProductResponse;
import toy.orderflow.repository.ProductRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        Product product = Product.create(request.name(), request.price(), request.stockQuantity());
        return ProductResponse.from(productRepository.save(product));
    }

    public List<ProductResponse> getProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .toList();
    }
}
