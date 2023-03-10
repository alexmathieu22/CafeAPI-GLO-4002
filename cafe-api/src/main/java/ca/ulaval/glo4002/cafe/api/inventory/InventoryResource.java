package ca.ulaval.glo4002.cafe.api.inventory;

import ca.ulaval.glo4002.cafe.api.inventory.assembler.InventoryResponseAssembler;
import ca.ulaval.glo4002.cafe.api.inventory.response.InventoryResponse;
import ca.ulaval.glo4002.cafe.api.request.IngredientsRequest;
import ca.ulaval.glo4002.cafe.application.inventory.InventoryService;
import ca.ulaval.glo4002.cafe.application.parameter.IngredientsParams;

import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class InventoryResource {
    private final InventoryService inventoryService;
    private final InventoryResponseAssembler inventoryResponseAssembler = new InventoryResponseAssembler();

    public InventoryResource(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GET
    @Path("/inventory")
    public Response getInventory() {
        InventoryResponse response = inventoryResponseAssembler.toInventoryResponse(inventoryService.getInventory());
        return Response.ok(response).build();
    }

    @PUT
    @Path("/inventory")
    public Response putInventory(@Valid IngredientsRequest ingredientsRequest) {
        IngredientsParams ingredientsParams =
            IngredientsParams.from(ingredientsRequest.Chocolate, ingredientsRequest.Milk, ingredientsRequest.Water, ingredientsRequest.Espresso);
        inventoryService.addIngredientsToInventory(ingredientsParams);
        return Response.status(200).build();
    }
}
