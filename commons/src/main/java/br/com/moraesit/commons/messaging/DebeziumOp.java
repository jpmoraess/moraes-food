package br.com.moraesit.commons.messaging;

public enum DebeziumOp {

    CREATE("c"),
    UPDATE("u"),
    DELETE("d");

    private String value;

    DebeziumOp(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
