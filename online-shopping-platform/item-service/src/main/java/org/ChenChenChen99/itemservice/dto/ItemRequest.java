package org.ChenChenChen99.itemservice.dto;

import lombok.Data;
import java.util.List;

@Data
public class ItemRequest {
    private String name;
    private double price;
    private String upc;
    private List<String> imageUrls;
    private int available;
    private int reserved;
}
