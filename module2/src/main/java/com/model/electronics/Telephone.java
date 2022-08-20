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

    private Telephone() {
    }

    public static class Builder {
        private final Telephone telephone;

        public Builder() {
            telephone = new Telephone();
        }

        public Builder setSeries(String series) {
            telephone.setSeries(series);
            return this;
        }

        public Builder setScreenType(ScreenType screenType) {
            telephone.setScreenType(screenType);
            return this;
        }

        public Builder setPrice(BigDecimal price) {
            telephone.setPrice(price);
            return this;
        }

        public Builder setModel(Manufacture model) {
            telephone.setModel(model);
            return this;
        }

        public Telephone build() {
            if (telephone.series != null && telephone.screenType != null &&
                    telephone.price != null && telephone.model != null) {
                return telephone;
            } else {
                throw new NullPointerException("An object cannot be created without filling in all the fields");
            }
        }
    }
}