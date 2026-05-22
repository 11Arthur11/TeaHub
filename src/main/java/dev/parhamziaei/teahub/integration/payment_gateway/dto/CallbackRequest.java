package dev.parhamziaei.teahub.integration.payment_gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class CallbackRequest {
    private String status;

    private String transid;

    private String invoice_id;

    public String getInvoiceId() {
        return invoice_id;
    }

    public void setInvoiceId(String invoice_id) {
        this.invoice_id = invoice_id;
    }
}
