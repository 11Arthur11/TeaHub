package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.shop.TeaSpeakProduct;

import java.util.List;
import java.util.Optional;

public interface TeaSpeakProductRepository {

    Optional<TeaSpeakProduct> findById(Long id);
    List<TeaSpeakProduct> findAll();
    void save(TeaSpeakProduct teaSpeakProduct);
    void update(TeaSpeakProduct teaSpeakProduct);
    void delete(TeaSpeakProduct teaSpeakProduct);

}
