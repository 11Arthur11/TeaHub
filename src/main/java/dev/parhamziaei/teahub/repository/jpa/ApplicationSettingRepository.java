package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.ApplicationSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationSettingRepository extends JpaRepository<ApplicationSetting, Long> {
}
