package br.com.moraesit.commons.rest.exception.handler;

public class ErrorDTO {
    private final String code;
    private final String message;

    private ErrorDTO(Builder builder) {
        code = builder.code;
        message = builder.message;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private String code;
        private String message;

        private Builder() {
        }

        public Builder code(String val) {
            code = val;
            return this;
        }

        public Builder message(String val) {
            message = val;
            return this;
        }

        public ErrorDTO build() {
            return new ErrorDTO(this);
        }
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
