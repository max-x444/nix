package com.model;

import com.model.constants.Country;
import com.model.constants.ScreenType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Television extends Electronics {
    private int diagonal;
    private Country country;

    public Television(String series, ScreenType screenType, BigDecimal price, int diagonal, Country country) {
        super(series, screenType, price);
        this.diagonal = diagonal;
        this.country = country;
    }

    @Override
    public String toString() {
        return "Television{" +
                "diagonal=" + diagonal +
                ", country=" + country +
                ", id='" + id + '\'' +
                ", series='" + series + '\'' +
                ", screenType=" + screenType +
                ", price=" + price +
                '}';
    }
}
