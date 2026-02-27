package dev.parhamziaei.teahub.test_util;

import dev.parhamziaei.teahub.dto.request.shop.admin.CategoryAdminRequest;
import dev.parhamziaei.teahub.entity.jpa.shop.Category;
import dev.parhamziaei.teahub.exception.custom.global.ConflictEntityException;
import dev.parhamziaei.teahub.repository.jpa.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CategoryTestUtil {

    @Autowired
    private CategoryRepository categoryRepo;

    public Category persistedDummyCategory(boolean active) {
        Category category = Category.builder()
                .name("test dummy category" + String.format("%04d", new Random().nextInt(10000)))
                .description("this is a dummy category")
                .slug("test-" + String.format("%04d", new Random().nextInt(10000)))
                .active(active)
                .build();
        categoryRepo.save(category);
        return category;
    }

}
