package ca.ulaval.glo4002.cafe.domain.billing.bill.taxing.strategy;

import ca.ulaval.glo4002.cafe.domain.billing.bill.taxing.Tax;
import ca.ulaval.glo4002.cafe.domain.geolocalisation.Location;

public class TaxingNone implements TaxingStrategy {
    @Override
    public Tax getTaxRate(Location location) {
        return location.country().getTax();
    }
}
