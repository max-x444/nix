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
    private Country country;
    private int diagonal;

    private Television(String series, ScreenType screenType, BigDecimal price) {
        super(series, screenType, price);
    }

    public static class Builder {
        private String series;
        private ScreenType screenType;
        private BigDecimal price;
        private Country country;
        private int diagonal;

        public Builder setSeries(String series) {
            this.series = series;
            return this;
        }

        public Builder setScreenType(ScreenType screenType) {
            this.screenType = screenType;
            return this;
        }

        public Builder setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder setCountry(Country country) {
            this.country = country;
            return this;
        }

        public Builder setDiagonal(int diagonal) {
            this.diagonal = diagonal;
            return this;
        }

        public Television build() {
            if (series != null && screenType != null && price != null && country != null) {
                final Television television = new Television(series, screenType, price);
                television.setCountry(country);
                television.setDiagonal(diagonal);
                return television;
            } else {
                throw new NullPointerException("An object cannot be created without filling in all the fields");
            }
        }
    }
}