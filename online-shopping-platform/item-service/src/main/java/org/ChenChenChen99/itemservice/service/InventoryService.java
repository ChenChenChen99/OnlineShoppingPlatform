package org.ChenChenChen99.itemservice.service;

import lombok.RequiredArgsConstructor;
import org.ChenChenChen99.itemservice.entity.Item;
import org.ChenChenChen99.itemservice.repository.ItemRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final ItemRepository itemRepository;

    public int getAvailableStock(String itemId) {
        System.out.println("getAvailableStock - itemId type: " + itemId.getClass().getName());
        System.out.println("getAvailableStock - itemId content: " + itemId);

        return itemRepository.findById(itemId)
                .map(item -> item.getInventory().getAvailable())
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public void reserveInventory(String itemId, int quantity) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        if (item.getInventory().getAvailable() < quantity) {
            throw new RuntimeException("Not enough stock");
        }
        item.getInventory().setAvailable(item.getInventory().getAvailable() - quantity);
        item.getInventory().setReserved(item.getInventory().getReserved() + quantity);
        itemRepository.save(item);
    }

    public void releaseInventory(String itemId, int quantity) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        if (item.getInventory().getReserved() < quantity) {
            throw new RuntimeException("Not enough reserved stock");
        }
        item.getInventory().setReserved(item.getInventory().getReserved() - quantity);
        item.getInventory().setAvailable(item.getInventory().getAvailable() + quantity);
        itemRepository.save(item);
    }

    public void confirmSale(String itemId, int quantity) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        if (item.getInventory().getReserved() < quantity) {
            throw new RuntimeException("Not enough reserved stock");
        }
        item.getInventory().setReserved(item.getInventory().getReserved() - quantity);
        itemRepository.save(item);
    }
}
