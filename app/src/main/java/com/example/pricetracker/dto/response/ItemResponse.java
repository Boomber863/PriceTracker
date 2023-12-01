package com.example.pricetracker.dto.response;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ItemResponse implements Serializable {

    private String name;
    private int id;
    private String item_img_url;
    private ItemPriceResponse newest_price;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getImageUrl() {
        return item_img_url;
    }

    public ItemPriceResponse getNewestPrice() {
        return newest_price;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemResponse that = (ItemResponse) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
