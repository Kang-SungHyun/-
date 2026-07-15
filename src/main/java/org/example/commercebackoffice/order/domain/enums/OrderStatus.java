package org.example.commercebackoffice.order.domain.enums;

public enum OrderStatus {
    READY, SHIPPING, DELIVERED, CANCELLED;

    public boolean canTransitionTo(OrderStatus next) {
        return switch (this) {
            case READY -> next == SHIPPING;
            case SHIPPING -> next == DELIVERED;
            default -> false;
        };
    }
}
