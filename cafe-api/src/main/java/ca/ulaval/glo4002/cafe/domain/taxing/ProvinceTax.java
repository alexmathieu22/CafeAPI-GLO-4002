package ca.ulaval.glo4002.cafe.domain.taxing;

import ca.ulaval.glo4002.cafe.domain.exception.InvalidConfigurationCountryException;

public enum ProvinceTax {
    AB(new Tax(0)),
    BC(new Tax(0.07f)),
    MB(new Tax(0.07f)),
    NB(new Tax(0.10f)),
    NL(new Tax(0.10f)),
    NT(new Tax(0)),
    NS(new Tax(0.10f)),
    NU(new Tax(0)),
    ON(new Tax(0.08f)),
    PE(new Tax(0.10f)),
    QC(new Tax(0.09975f)),
    SK(new Tax(0.06f)),
    YT(new Tax(0));

    private final Tax tax;

    ProvinceTax(Tax tax) {
        this.tax = tax;
    }

    public static ProvinceTax fromString(String province) {
        if (ProvinceTax.contains(province)) {
            return ProvinceTax.valueOf(province);
        }
        throw new InvalidConfigurationCountryException();
    }

    private static boolean contains(String other) {
        for (ProvinceTax provinceTax : ProvinceTax.values()) {
            if (provinceTax.name().equals(other)) {
                return true;
            }
        }
        return false;
    }

    public Tax getTax() {
        return tax;
    }
}
