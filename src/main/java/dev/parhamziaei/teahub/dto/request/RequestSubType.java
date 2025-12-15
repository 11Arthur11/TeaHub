package dev.parhamziaei.teahub.dto.request;

import dev.parhamziaei.teahub.enums.shop.ResourceType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequestSubType {
    ResourceType value();
}
