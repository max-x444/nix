package com.example.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductBundle extends NotifiableProduct {
    protected int amount;

    private ProductBundle(){

    }

    @Override
    public String getBasicInfo() {
        return "ProductBundle{" +
                "channel='" + channel + '\'' +
                ", id=" + id +
                ", available=" + available +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", amountInBundle=" + amount +
                '}';
    }

    public static class Builder {
        private final ProductBundle newProductBundle;

        public Builder() {
            newProductBundle = new ProductBundle();
        }

        public Builder setId(Long id) {
            newProductBundle.id = id;
            return this;
        }

        public Builder setAvailable(boolean available) {
            newProductBundle.available = available;
            return this;
        }

        public Builder setTitle(String title) {
            if (title.length() <= 20) {
                newProductBundle.title = title;
            }
            return this;
        }

        public Builder setPrice(double price) {
            newProductBundle.price = price;
            return this;
        }

        public Builder setChannel(String channel) {
            newProductBundle.channel = channel;
            return this;
        }

        public Builder setAmount(int amount) {
            if (amount > 0) {
                newProductBundle.amount = amount;
            }
            return this;
        }

        public ProductBundle build() {
            if (newProductBundle.price != 0 && newProductBundle.id != 0) {
                return newProductBundle;
            } else {
                throw new IllegalArgumentException("Not valid price or id");
            }
        }
    }
}