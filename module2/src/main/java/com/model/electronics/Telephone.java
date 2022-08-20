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

    private Telephone(String series, ScreenType screenType, BigDecimal price) {
        super(series, screenType, price);
    }

    public static class Builder {
        private String series;
        private ScreenType screenType;
        private BigDecimal price;
        private Manufacture model;

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

        public Builder setModel(Manufacture model) {
            this.model = model;
            return this;
        }

        public Telephone build() {
            if (series != null && screenType != null && price != null && model != null) {
                final Telephone telephone = new Telephone(series, screenType, price);
                telephone.setModel(model);
                return telephone;
            } else {
                throw new NullPointerException("An object cannot be created without filling in all the fields");
            }
        }
    }
}