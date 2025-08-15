package org.ChenChenChen99.itemservice.controller;

import lombok.RequiredArgsConstructor;
import org.ChenChenChen99.itemservice.entity.Item;
import org.ChenChenChen99.itemservice.service.ItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{itemId}")
    public Item getItem(@PathVariable String itemId) {
        return itemService.getItemById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    @PostMapping
    public Item createItem(@RequestBody ItemRequest request) {
        return itemService.createItem(request);
    }
}
