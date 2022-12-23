package ca.ulaval.glo4002.cafe.domain.taxing.strategy;

import ca.ulaval.glo4002.cafe.domain.taxing.Location;
import ca.ulaval.glo4002.cafe.domain.taxing.Tax;

public class TaxingNone implements TaxingStrategy {
    @Override
    public Tax getTaxRate(Location location) {
        return location.countryTax().getTax();
    }
}