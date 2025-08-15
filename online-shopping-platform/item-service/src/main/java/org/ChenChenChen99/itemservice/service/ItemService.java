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
}
