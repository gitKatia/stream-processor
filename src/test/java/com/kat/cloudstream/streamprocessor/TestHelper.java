package com.kat.cloudstream.streamprocessor;

import com.kat.cloudstream.streamprocessor.model.DeliveryAddress;
import com.kat.cloudstream.streamprocessor.model.Item;
import com.kat.cloudstream.streamprocessor.model.PosInvoice;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

public interface TestHelper {

    private static Item getRandomItem() {
        Random random = new Random();
        Double itemPrice = random.nextDouble() * 50;
        Integer itemQuantity = random.nextInt(5);
        Double totalValue = itemPrice * itemQuantity;
        return Item.builder()
                .itemCode(UUID.randomUUID().toString())
                .itemDescription(UUID.randomUUID().toString())
                .itemPrice(itemPrice)
                .itemQuantity(itemQuantity)
                .totalValue(totalValue)
                .build();
    }

    private static DeliveryAddress getRandomDeliveryAddress(String country) {
        return DeliveryAddress.builder()
                .addressLine(UUID.randomUUID().toString())
                .city(UUID.randomUUID().toString())
                .country(country)
                .postCode(UUID.randomUUID().toString())
                .contactNumber(UUID.randomUUID().toString())
                .build();
    }

    static PosInvoice generatePosInvoice(String deliveryType, String customerType, String country) {
        Random random = new Random();
        DeliveryAddress deliveryAddress = "delivery".equals(deliveryType) ?
                getRandomDeliveryAddress(country) : null;
        List<Item> invoiceItems = new ArrayList<>();
        Integer totalNumberOfItems = random.nextInt(10);
        IntStream.range(0, totalNumberOfItems)
                .forEach(i -> {
                    invoiceItems.add(getRandomItem());
                });
        Double totalAmount = invoiceItems.stream()
                .map(it -> it.getTotalValue())
                .reduce(Double::sum)
                .orElse(0.00);
        return PosInvoice.builder()
                .createdAt(System.currentTimeMillis())
                .deliveryAddress(deliveryAddress)
                .customerCardNo(UUID.randomUUID().toString())
                .posId(UUID.randomUUID().toString())
                .storeId(UUID.randomUUID().toString())
                .customerType(customerType)
                .deliveryType(deliveryType)
                .paymentMethod("card")
                .taxableAmount(totalAmount)
                .invoiceNo(UUID.randomUUID().toString())
                .itemQuantity(totalNumberOfItems)
                .invoiceItems(invoiceItems)
                .totalAmount(totalAmount)
                .build();
    }
}
