package com.model;

import com.model.constants.Manufacture;
import com.model.constants.ScreenType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Telephone extends Electronics {
    private Manufacture model;

    public Telephone(String series, ScreenType screenType, BigDecimal price, Manufacture model) {
        super(series, screenType, price);
        this.model = model;
    }

    @Override
    public String toString() {
        return "Telephone{" +
                "model=" + model +
                ", id='" + id + '\'' +
                ", series='" + series + '\'' +
                ", screenType=" + screenType +
                ", price=" + price +
                '}';
    }
}
