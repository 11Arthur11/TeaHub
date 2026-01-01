package dev.parhamziaei.teahub.entity.jpa.shop;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@DiscriminatorValue("AUDIO_BOT_PRODUCT")
@NoArgsConstructor
@SuperBuilder
public class AudioBotProduct extends BillableProduct {



}
