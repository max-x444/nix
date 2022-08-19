package com.model.electronics;

import com.model.constants.ScreenType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public abstract class Electronics {
    protected final String id;
    protected String series;
    protected ScreenType screenType;
    protected BigDecimal price;

    public Electronics(String series, ScreenType screenType, BigDecimal price) {
        this.id = UUID.randomUUID().toString();
        this.series = series;
        this.screenType = screenType;
        this.price = price;
    }
}