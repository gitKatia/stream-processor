package com.kat.cloudstream.streamprocessor.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PosInvoice implements Serializable {

    @JsonProperty("invoice_no")
    private String invoiceNo;
    @JsonProperty("created_at")
    private Long createdAt;
    @JsonProperty("store_id")
    private String storeId;
    @JsonProperty("pos_id")
    private String posId;
    @JsonProperty("customer_type")
    private String customerType;
    @JsonProperty("customer_card_no")
    private String customerCardNo;
    @JsonProperty("total_amount")
    private Double totalAmount;
    @JsonProperty("item_quantity")
    private Integer itemQuantity;
    @JsonProperty("payment_method")
    private String paymentMethod;
    @JsonProperty("taxable_amount")
    private Double taxableAmount;
    @JsonProperty("delivery_type")
    private String deliveryType;
    @JsonProperty("delivery_address")
    private DeliveryAddress deliveryAddress;
    @JsonProperty("invoice_items")
    private List<Item> invoiceItems;
}
