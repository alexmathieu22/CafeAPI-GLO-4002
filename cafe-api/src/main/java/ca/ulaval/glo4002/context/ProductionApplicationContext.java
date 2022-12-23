package ca.ulaval.glo4002.context;

import java.util.List;
import java.util.Optional;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import ca.ulaval.glo4002.cafe.api.configuration.ConfigurationResource;
import ca.ulaval.glo4002.cafe.api.customer.CustomerResource;
import ca.ulaval.glo4002.cafe.api.exception.mapper.CafeExceptionMapper;
import ca.ulaval.glo4002.cafe.api.exception.mapper.CatchallExceptionMapper;
import ca.ulaval.glo4002.cafe.api.exception.mapper.ConstraintViolationExceptionMapper;
import ca.ulaval.glo4002.cafe.api.inventory.InventoryResource;
import ca.ulaval.glo4002.cafe.api.layout.LayoutResource;
import ca.ulaval.glo4002.cafe.api.menu.MenuResource;
import ca.ulaval.glo4002.cafe.api.operation.OperationResource;
import ca.ulaval.glo4002.cafe.api.reservation.ReservationResource;
import ca.ulaval.glo4002.cafe.application.configuration.ConfigurationService;
import ca.ulaval.glo4002.cafe.application.customer.CustomerService;
import ca.ulaval.glo4002.cafe.application.inventory.InventoryService;
import ca.ulaval.glo4002.cafe.application.layout.LayoutService;
import ca.ulaval.glo4002.cafe.application.menu.MenuService;
import ca.ulaval.glo4002.cafe.application.operation.OperationService;
import ca.ulaval.glo4002.cafe.application.reservation.ReservationService;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeFactory;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.domain.billing.TipRate;
import ca.ulaval.glo4002.cafe.domain.layout.cube.CubeName;
import ca.ulaval.glo4002.cafe.domain.layout.cube.CubeSize;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerFactory;
import ca.ulaval.glo4002.cafe.domain.ordering.order.CoffeeFactory;
import ca.ulaval.glo4002.cafe.domain.reservation.ReservationFactory;
import ca.ulaval.glo4002.cafe.domain.reservation.ReservationType;
import ca.ulaval.glo4002.cafe.domain.taxing.CountryTax;
import ca.ulaval.glo4002.cafe.domain.taxing.LocationTax;
import ca.ulaval.glo4002.cafe.domain.valueobjects.CafeName;
import ca.ulaval.glo4002.cafe.infrastructure.InMemoryCafeRepository;

public class ProductionApplicationContext implements ApplicationContext {
    private static final CafeName CAFE_NAME = new CafeName("Les 4-Fées");
    private static final CubeSize CUBE_SIZE = new CubeSize(4);
    private static final List<CubeName> CUBE_NAMES =
        List.of(new CubeName("Wanda"), new CubeName("Tinker Bell"), new CubeName("Bloom"), new CubeName("Merryweather"));
    private static final ReservationType RESERVATION_STRATEGY_TYPE = ReservationType.Default;
    private static final TipRate GROUP_TIP_RATE = new TipRate(0);
    private static final LocationTax LOCATION_TAX = new LocationTax(CountryTax.None, Optional.empty(), Optional.empty());
    private static final int PORT = 8181;

    public ResourceConfig initializeResourceConfig() {
        CafeFactory cafeFactory = new CafeFactory();
        CafeRepository cafeRepository = new InMemoryCafeRepository();

        ReservationService groupService = new ReservationService(cafeRepository, new ReservationFactory());
        ConfigurationService configurationService = new ConfigurationService(cafeRepository);
        CustomerService customersService = new CustomerService(cafeRepository, new CustomerFactory());
        InventoryService inventoryService = new InventoryService(cafeRepository);
        LayoutService layoutService = new LayoutService(cafeRepository);
        OperationService operationService = new OperationService(cafeRepository);
        MenuService menuService = new MenuService(cafeRepository, new CoffeeFactory());

        initializeCafe(cafeFactory, cafeRepository);

        return new ResourceConfig().packages("ca.ulaval.glo4002.cafe").property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true)
            .register(new ConfigurationResource(configurationService))
            .register(new CustomerResource(customersService, menuService))
            .register(new MenuResource(menuService))
            .register(new InventoryResource(inventoryService))
            .register(new LayoutResource(layoutService))
            .register(new OperationResource(operationService, customersService))
            .register(new ReservationResource(groupService))
            .register(new CafeExceptionMapper())
            .register(new CatchallExceptionMapper())
            .register(new ConstraintViolationExceptionMapper());
    }

    public int getPort() {
        return PORT;
    }

    private void initializeCafe(CafeFactory cafeFactory, CafeRepository cafeRepository) {
        Cafe cafe = cafeFactory.createCafe(CUBE_SIZE, CAFE_NAME, RESERVATION_STRATEGY_TYPE, LOCATION_TAX, GROUP_TIP_RATE, CUBE_NAMES);
        cafeRepository.saveOrUpdate(cafe);
    }
}
