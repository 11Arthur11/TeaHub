package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.ApplicationSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApplicationSettingRepository extends JpaRepository<ApplicationSetting, Long> {

    @Query("SELECT s from ApplicationSetting s WHERE s.id = 1")
    ApplicationSetting find();

}
