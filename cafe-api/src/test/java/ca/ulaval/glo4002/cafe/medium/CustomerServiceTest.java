package ca.ulaval.glo4002.cafe.medium;

import java.util.LinkedList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.application.customer.CustomerService;
import ca.ulaval.glo4002.cafe.application.customer.dto.BillDTO;
import ca.ulaval.glo4002.cafe.application.customer.dto.CustomerDTO;
import ca.ulaval.glo4002.cafe.application.customer.dto.OrderDTO;
import ca.ulaval.glo4002.cafe.application.customer.parameter.CheckInCustomerParams;
import ca.ulaval.glo4002.cafe.application.customer.parameter.CheckOutCustomerParams;
import ca.ulaval.glo4002.cafe.application.customer.parameter.CustomerOrderParams;
import ca.ulaval.glo4002.cafe.application.inventory.InventoryService;
import ca.ulaval.glo4002.cafe.application.parameter.IngredientsParams;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.domain.exception.CustomerNotFoundException;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.SeatNumber;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerFactory;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerId;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerName;
import ca.ulaval.glo4002.cafe.domain.ordering.order.Order;
import ca.ulaval.glo4002.cafe.domain.valueobjects.Amount;
import ca.ulaval.glo4002.cafe.fixture.OrderFixture;
import ca.ulaval.glo4002.cafe.infrastructure.InMemoryCafeRepository;
import ca.ulaval.glo4002.cafe.util.CafeInitializer;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerServiceTest {
    private static final CafeInitializer CAFE_INITIALIZER = new CafeInitializer();
    private static final CustomerId A_CUSTOMER_ID = new CustomerId("123");
    private static final String A_CUSTOMER_NAME = "Joe";
    private static final CheckInCustomerParams CHECK_IN_CUSTOMER_PARAMS = new CheckInCustomerParams(A_CUSTOMER_ID.value(), A_CUSTOMER_NAME, null);
    private static final CheckOutCustomerParams CHECK_OUT_CUSTOMER_PARAMS = new CheckOutCustomerParams(A_CUSTOMER_ID.value());
    private static final CustomerOrderParams CUSTOMER_ORDER_PARAMS = new CustomerOrderParams(A_CUSTOMER_ID, new OrderFixture().build());
    private static final IngredientsParams INGREDIENT_PARAMS = new IngredientsParams(100, 100, 100, 100);

    CustomerService customerService;
    CafeRepository cafeRepository;
    InventoryService inventoryService;

    @BeforeEach
    public void instanciateAttributes() {
        cafeRepository = new InMemoryCafeRepository();
        customerService = new CustomerService(cafeRepository, new CustomerFactory());
        inventoryService = new InventoryService(cafeRepository);
        CAFE_INITIALIZER.initializeCafe(cafeRepository);
    }

    @Test
    public void whenCheckingIn_shouldSaveCustomer() {
        customerService.checkIn(CHECK_IN_CUSTOMER_PARAMS);

        assertDoesNotThrow(() -> customerService.getCustomer(A_CUSTOMER_ID));
    }

    @Test
    public void givenSavedCustomer_whenCheckingOut_shouldSaveCustomerBill() {
        customerService.checkIn(CHECK_IN_CUSTOMER_PARAMS);

        customerService.checkOut(CHECK_OUT_CUSTOMER_PARAMS);

        assertNotNull(customerService.getCustomerBill(A_CUSTOMER_ID));
    }

    @Test
    public void givenSavedCustomer_whenCheckingOut_shouldRemoveCustomerFromSeat() {
        customerService.checkIn(CHECK_IN_CUSTOMER_PARAMS);

        customerService.checkOut(CHECK_OUT_CUSTOMER_PARAMS);

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomer(A_CUSTOMER_ID));
    }

    @Test
    public void givenSavedCustomer_whenPlacingOrder_shouldSaveOrderForCustomer() {
        Order expectedOrder = new OrderFixture().build();
        customerService.checkIn(CHECK_IN_CUSTOMER_PARAMS);
        inventoryService.addIngredientsToInventory(INGREDIENT_PARAMS);

        customerService.placeOrder(CUSTOMER_ORDER_PARAMS);
        OrderDTO actualOrder = customerService.getOrder(A_CUSTOMER_ID);

        assertEquals(OrderDTO.fromOrder(expectedOrder), actualOrder);
    }

    @Test
    public void givenSavedCustomer_whenGettingCustomer_shouldReturnValidCustomerDTO() {
        CustomerDTO expectedCustomerDTO = new CustomerDTO(new CustomerName(A_CUSTOMER_NAME), new SeatNumber(1), Optional.empty());
        customerService.checkIn(CHECK_IN_CUSTOMER_PARAMS);

        CustomerDTO actualCustomerDTO = customerService.getCustomer(A_CUSTOMER_ID);

        assertEquals(expectedCustomerDTO, actualCustomerDTO);
    }

    @Test
    public void givenSavedCustomerWithOrder_whenGettingOrder_shouldReturnValidOrderDTO() {
        OrderDTO expectedOrderDTO = new OrderDTO(CUSTOMER_ORDER_PARAMS.order().items());
        customerService.checkIn(CHECK_IN_CUSTOMER_PARAMS);
        inventoryService.addIngredientsToInventory(INGREDIENT_PARAMS);
        customerService.placeOrder(CUSTOMER_ORDER_PARAMS);

        OrderDTO actualOrderDTO = customerService.getOrder(CHECK_IN_CUSTOMER_PARAMS.customerId());

        assertEquals(expectedOrderDTO, actualOrderDTO);
    }

    @Test
    public void givenSavedBill_whenGettingCustomerBill_shouldReturnValidBillDTO() {
        BillDTO expectedBillDTO = new BillDTO(new LinkedList<>(CUSTOMER_ORDER_PARAMS.order().items()), new Amount(0),
            new Amount(2.95f), new Amount(0), new Amount(2.95f));
        customerService.checkIn(CHECK_IN_CUSTOMER_PARAMS);
        inventoryService.addIngredientsToInventory(INGREDIENT_PARAMS);
        customerService.placeOrder(CUSTOMER_ORDER_PARAMS);
        customerService.checkOut(CHECK_OUT_CUSTOMER_PARAMS);

        BillDTO actualBillDTO = customerService.getCustomerBill(CHECK_IN_CUSTOMER_PARAMS.customerId());

        assertEquals(expectedBillDTO, actualBillDTO);
    }
}
