package dev.parhamziaei.teahub.entity.jpa.payment.invoice;

import dev.parhamziaei.teahub.enums.payment.PostPaymentType;
import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "post_payment_action")
@DiscriminatorColumn(name = "action_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
public class PostPaymentAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "postPaymentAction")
    private Invoice forInvoice;

    @Column(name = "action_type", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private PostPaymentType postPaymentType;

}
