package com.kat.cloudstream.streamprocessor.service;

import com.kat.cloudstream.streamprocessor.model.DeliveryAddress;
import com.kat.cloudstream.streamprocessor.model.HadoopRecord;
import com.kat.cloudstream.streamprocessor.model.Notification;
import com.kat.cloudstream.streamprocessor.model.PosInvoice;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecordBuilderService {

    public Notification getNotification(PosInvoice posInvoice) {
        return Notification.builder()
                .invoiceNumber(posInvoice.getInvoiceNo())
                .customerCardNo(posInvoice.getCustomerCardNo())
                .totalAmount(posInvoice.getTotalAmount())
                .earnedLoyaltyPoints(posInvoice.getTotalAmount() * 0.02)
                .build();
    }

    public PosInvoice getMaskedInvoice(PosInvoice posInvoice) {
        PosInvoice newPosInvoice = SerializationUtils.clone(posInvoice);
        posInvoice.setCustomerCardNo(null);
        if ("delivery".equals(newPosInvoice.getDeliveryType())) {
            newPosInvoice.getDeliveryAddress().setAddressLine(null);
            newPosInvoice.getDeliveryAddress().setCity(null);
            newPosInvoice.getDeliveryAddress().setContactNumber(null);
            newPosInvoice.getDeliveryAddress().setCountry(null);
            newPosInvoice.getDeliveryAddress().setPostCode(null);
        }

        return newPosInvoice;
    }

    public List<HadoopRecord> getHadoopRecords(PosInvoice posInvoice) {
        List<HadoopRecord> hadoopRecords = new ArrayList<>();
        posInvoice.getInvoiceItems().stream().forEach(invoiceItem -> {
            String deliveryType = posInvoice.getDeliveryType();
            HadoopRecord hadoopRecord = HadoopRecord.builder()
                    .invoiceNumber(posInvoice.getInvoiceNo())
                    .createdTime(posInvoice.getCreatedAt())
                    .customerType(posInvoice.getCustomerType())
                    .paymentMethod(posInvoice.getPaymentMethod())
                    .posId(posInvoice.getPosId())
                    .storeId(posInvoice.getStoreId())
                    .deliveryType(deliveryType)
                    .itemCode(invoiceItem.getItemCode())
                    .itemPrice(invoiceItem.getItemPrice())
                    .itemDescription(invoiceItem.getItemDescription())
                    .itemQuantity(invoiceItem.getItemQuantity())
                    .totalValue(invoiceItem.getTotalValue())
                    .build();
            if ("delivery".equals(deliveryType)) {
                DeliveryAddress deliveryAddress = posInvoice.getDeliveryAddress();
                hadoopRecord.setCity(deliveryAddress.getCity());
                hadoopRecord.setState(deliveryAddress.getCountry());
                hadoopRecord.setPostCode(deliveryAddress.getPostCode());
            }
            hadoopRecords.add(hadoopRecord);
        });
        return hadoopRecords;
    }
}
