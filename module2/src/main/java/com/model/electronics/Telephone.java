package com.model.electronics;

import com.model.constants.Manufacture;
import com.model.constants.ScreenType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString(callSuper = true)
public class Telephone extends Electronics {
    private Manufacture model;

    public Telephone(String series, ScreenType screenType, BigDecimal price, Manufacture model) {
        super(series, screenType, price);
        this.model = model;
    }
}