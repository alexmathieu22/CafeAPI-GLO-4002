package ca.ulaval.glo4002.cafe.medium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.application.configuration.ConfigurationService;
import ca.ulaval.glo4002.cafe.application.parameter.ConfigurationParams;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeFactory;
import ca.ulaval.glo4002.cafe.domain.CafeName;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.infrastructure.InMemoryCafeRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigurationServiceTest {
    private static final CafeName NEW_CAFE_NAME = new CafeName("Les 4-Ogres");
    private static final ConfigurationParams CONFIGURATION_PARAMS = new ConfigurationParams(5, NEW_CAFE_NAME.value(), "Default", "CA",
        "QC", "", 5);

    private ConfigurationService configurationService;
    private Cafe cafe;
    private CafeRepository cafeRepository;

    private void initializeCafe(CafeFactory cafeFactory, CafeRepository cafeRepository) {
        Cafe cafe = cafeFactory.createCafe();
        cafeRepository.saveOrUpdate(cafe);
    }

    @BeforeEach
    public void instanciateAttributes() {
        CafeFactory cafeFactory = new CafeFactory();
        cafeRepository = new InMemoryCafeRepository();
        configurationService = new ConfigurationService(cafeRepository);
        initializeCafe(cafeFactory, cafeRepository);
    }

    @Test
    public void whenUpdatingConfiguration_shouldUpdateConfiguration() {
        configurationService.updateConfiguration(CONFIGURATION_PARAMS);

        cafe = cafeRepository.get();
        assertEquals(NEW_CAFE_NAME, cafe.getName());
    }
}