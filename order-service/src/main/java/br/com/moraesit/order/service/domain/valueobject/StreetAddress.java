package br.com.moraesit.order.service.domain.valueobject;

import java.util.UUID;

public class StreetAddress {
    private final UUID id;
    private final String street;
    private final String postalCode;
    private final String city;

    public StreetAddress(UUID id, String street, String postalCode, String city) {
        this.id = id;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
    }

    private StreetAddress(Builder builder) {
        id = builder.id;
        street = builder.street;
        postalCode = builder.postalCode;
        city = builder.city;
    }

    public static Builder builder() {
        return new Builder();
    }

    public UUID getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StreetAddress that = (StreetAddress) o;

        if (!street.equals(that.street)) return false;
        if (!postalCode.equals(that.postalCode)) return false;
        return city.equals(that.city);
    }

    @Override
    public int hashCode() {
        int result = street.hashCode();
        result = 31 * result + postalCode.hashCode();
        result = 31 * result + city.hashCode();
        return result;
    }

    public static final class Builder {
        private UUID id;
        private String street;
        private String postalCode;
        private String city;

        private Builder() {
        }

        public Builder id(UUID val) {
            id = val;
            return this;
        }

        public Builder street(String val) {
            street = val;
            return this;
        }

        public Builder postalCode(String val) {
            postalCode = val;
            return this;
        }

        public Builder city(String val) {
            city = val;
            return this;
        }

        public StreetAddress build() {
            return new StreetAddress(this);
        }
    }
}
