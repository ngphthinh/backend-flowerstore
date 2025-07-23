package com.ngphthinh.flower.enums;

public enum DeliveryMethod {
    DELIVERY,
    PICKUP;

    public static boolean isValid(String method) {
        for (DeliveryMethod deliveryMethod : DeliveryMethod.values()) {
            if (deliveryMethod.name().equals(method)) {
                return true;
            }
        }
        return false;
    }

}
