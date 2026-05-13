package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.ApplicationSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApplicationSettingRepository extends JpaRepository<ApplicationSettings, Long> {

    @Query("SELECT s from ApplicationSettings s WHERE s.id = 1")
    ApplicationSettings find();

}
