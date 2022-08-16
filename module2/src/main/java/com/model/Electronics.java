package com.model;

import com.model.constants.ScreenType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Electronics that = (Electronics) o;
        return Objects.equals(id, that.id) && Objects.equals(series, that.series)
                && screenType == that.screenType && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, series, screenType, price);
    }
}
