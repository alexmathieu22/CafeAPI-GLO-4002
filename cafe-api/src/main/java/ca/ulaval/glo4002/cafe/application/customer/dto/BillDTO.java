package ca.ulaval.glo4002.cafe.application.customer.dto;

import java.util.List;

import ca.ulaval.glo4002.cafe.domain.billing.bill.Bill;
import ca.ulaval.glo4002.cafe.domain.ordering.order.Coffee;
import ca.ulaval.glo4002.cafe.domain.valueobjects.Amount;

public record BillDTO(List<Coffee> coffees, Amount tip, Amount subtotal, Amount taxes, Amount total) {
    public static BillDTO fromBill(Bill bill) {
        return new BillDTO(List.copyOf(bill.order().items()), bill.tip(), bill.subtotal(), bill.taxes(), bill.total());
    }
}
