package com.wordline.onlinesales.component;
import com.wordline.onlinesales.enums.ProductType;
import com.wordline.onlinesales.model.Client;
import com.wordline.onlinesales.model.IndividualClient;
import com.wordline.onlinesales.model.ProfessionalClient;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

@Component
public class PriceCalculator {

    private final Map<ProductType, BigDecimal> individualPrices;
    private final Map<ProductType, BigDecimal> professionalHighRevenuePrices;
    private final Map<ProductType, BigDecimal> professionalLowRevenuePrices;

    public PriceCalculator() {
        this.individualPrices = initializeIndividualPrices();
        this.professionalHighRevenuePrices = initializeProfessionalHighRevenuePrices();
        this.professionalLowRevenuePrices = initializeProfessionalLowRevenuePrices();
    }

    private Map<ProductType, BigDecimal> initializeIndividualPrices() {
        Map<ProductType, BigDecimal> prices = new EnumMap<>(ProductType.class);
        prices.put(ProductType.HIGH_END_PHONE, BigDecimal.valueOf(1500.00));
        prices.put(ProductType.MID_RANGE_PHONE, BigDecimal.valueOf(800.00));
        prices.put(ProductType.LAPTOP, BigDecimal.valueOf(1200.00));
        return prices;
    }

    private Map<ProductType, BigDecimal> initializeProfessionalHighRevenuePrices() {
        Map<ProductType, BigDecimal> prices = new EnumMap<>(ProductType.class);
        prices.put(ProductType.HIGH_END_PHONE, BigDecimal.valueOf(1000.00));
        prices.put(ProductType.MID_RANGE_PHONE, BigDecimal.valueOf(550.00));
        prices.put(ProductType.LAPTOP, BigDecimal.valueOf(900.00));
        return prices;
    }

    private Map<ProductType, BigDecimal> initializeProfessionalLowRevenuePrices() {
        Map<ProductType, BigDecimal> prices = new EnumMap<>(ProductType.class);
        prices.put(ProductType.HIGH_END_PHONE, BigDecimal.valueOf(1150.00));
        prices.put(ProductType.MID_RANGE_PHONE, BigDecimal.valueOf(600.00));
        prices.put(ProductType.LAPTOP, BigDecimal.valueOf(1000.00));
        return prices;
    }
}
