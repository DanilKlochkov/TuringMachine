package org.turing;

public class Rule {
    Key key;
    Value value;
    public Rule (Key key, Value value) {
        this.key = key;
        this.value = value;
    }

    public Key getKey() {
        return key;
    }

    public Value getValue() {
        return value;
    }

    public static class Key {
        String state;
        String symbol;

        public Key(String state, String symbol) {
            this.state = state;
            this.symbol = symbol;
        }

        public String getState() {
            return state;
        }

        public String getSymbol() {
            return symbol;
        }
    }
    public static class Value {
        String newState;
        String newValue;
        String direction;

        public Value(String newState, String newValue, String direction) {
            this.newState = newState;
            this.newValue = newValue;
            this.direction = direction;
        }

        public String getDirection() {
            return direction;
        }

        public String getNewState() {
            return newState;
        }

        public String getNewValue() {
            return newValue;
        }
    }
}
