package org.ChenChenChen99.itemservice.service;


import lombok.RequiredArgsConstructor;
import org.ChenChenChen99.itemservice.entity.Item;
import org.ChenChenChen99.itemservice.repository.ItemRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public Optional<Item> getItemById(String itemId) {
        return itemRepository.findById(itemId);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item createItem(ItemRequest request) {
        Item item = new Item();
        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setUpc(request.getUpc());
        item.setImageUrls(request.getImageUrls());
        Inventory inventory = new Inventory();
        inventory.setAvailable(request.getAvailable());
        inventory.setReserved(request.getReserved());
        item.setInventory(inventory);

        return itemRepository.save(item);
    }
}
