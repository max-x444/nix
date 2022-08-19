package com.model.electronics;

import com.model.constants.Country;
import com.model.constants.ScreenType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString(callSuper = true)
public class Television extends Electronics {
    private int diagonal;
    private Country country;

    public Television(String series, ScreenType screenType, BigDecimal price, int diagonal, Country country) {
        super(series, screenType, price);
        this.diagonal = diagonal;
        this.country = country;
    }
}