package ca.ulaval.glo4002.cafe.medium;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.application.menu.MenuService;
import ca.ulaval.glo4002.cafe.application.menu.parameter.NewCoffeeParams;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.domain.inventory.IngredientType;
import ca.ulaval.glo4002.cafe.domain.inventory.Quantity;
import ca.ulaval.glo4002.cafe.domain.ordering.order.Coffee;
import ca.ulaval.glo4002.cafe.domain.ordering.order.CoffeeFactory;
import ca.ulaval.glo4002.cafe.domain.ordering.order.CoffeeType;
import ca.ulaval.glo4002.cafe.domain.ordering.order.Recipe;
import ca.ulaval.glo4002.cafe.domain.valueobjects.Amount;
import ca.ulaval.glo4002.cafe.infrastructure.InMemoryCafeRepository;
import ca.ulaval.glo4002.cafe.util.CafeInitializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MenuServiceTest {
    private static final CafeInitializer CAFE_INITIALIZER = new CafeInitializer();
    private static final Coffee A_COFFEE = new Coffee(new CoffeeType("NewLatte"), new Amount(2.95f),
        new Recipe(Map.of(IngredientType.Espresso, new Quantity(50), IngredientType.Milk, new Quantity(50))));
    private static final NewCoffeeParams A_NEW_COFFEE_PARAMS = NewCoffeeParams.from("NewCoffee", 2.95f, 0, 50, 0, 50);

    private MenuService menuService;
    private CafeRepository cafeRepository;
    private CoffeeFactory coffeeFactory;

    @BeforeEach
    public void instanciateAttributes() {
        coffeeFactory = mock(CoffeeFactory.class);
        cafeRepository = new InMemoryCafeRepository();
        menuService = new MenuService(cafeRepository, coffeeFactory);
        CAFE_INITIALIZER.initializeCafe(cafeRepository);
    }

    @Test
    public void whenAddingCoffeeToMenu_shouldAddCoffeeToMenu() {
        when(coffeeFactory.createCoffee(any(), any(), any())).thenReturn(A_COFFEE);

        menuService.addCoffeeToMenu(A_NEW_COFFEE_PARAMS);

        Cafe cafe = cafeRepository.get();
        assertEquals(A_COFFEE, cafe.getCoffeeByCoffeeType(A_COFFEE.coffeeType()));
    }
}
