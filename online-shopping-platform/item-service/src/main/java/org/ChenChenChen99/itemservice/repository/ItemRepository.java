package org.ChenChenChen99.itemservice.repository;


import org.ChenChenChen99.itemservice.entity.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<Item, String> {
}