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

    private Television() {
    }

    public static class Builder {
        private final Television television;

        public Builder() {
            television = new Television();
        }

        public Builder setSeries(String series) {
            television.setSeries(series);
            return this;
        }

        public Builder setScreenType(ScreenType screenType) {
            television.setScreenType(screenType);
            return this;
        }

        public Builder setPrice(BigDecimal price) {
            television.setPrice(price);
            return this;
        }

        public Builder setDiagonal(int diagonal) {
            television.setDiagonal(diagonal);
            return this;
        }

        public Builder setCountry(Country country) {
            television.setCountry(country);
            return this;
        }

        public Television build() {
            if (television.series != null && television.screenType != null &&
                    television.price != null && television.country != null) {
                return television;
            } else {
                throw new NullPointerException("An object cannot be created without filling in all the fields");
            }
        }
    }
}