package org.ChenChenChen99.itemservice.controller;

import lombok.RequiredArgsConstructor;
import org.ChenChenChen99.itemservice.dto.InventoryRequest;
import org.ChenChenChen99.itemservice.service.InventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/{itemId}")
    public int getAvailableStock(@PathVariable String itemId) {
        return inventoryService.getAvailableStock(itemId);
    }

    @PutMapping("/reserve")
    public void reserveInventory(@RequestBody InventoryRequest req) {
        System.out.println("reserveInventory - InventoryRequest object: " + req);
        System.out.println("reserveInventory - itemId from request body: " + req.getItemId());
        System.out.println("reserveInventory - quantity from request body: " + req.getQuantity());

        inventoryService.reserveInventory(req.getItemId(), req.getQuantity());
    }

    @PutMapping("/release")
    public void releaseInventory(@RequestBody InventoryRequest req) {
        inventoryService.releaseInventory(req.getItemId(), req.getQuantity());
    }

    @PutMapping("/confirm")
    public void confirmInventory(@RequestBody InventoryRequest req) {
        inventoryService.confirmSale(req.getItemId(), req.getQuantity());
    }
}
