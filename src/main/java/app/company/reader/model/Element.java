package app.company.reader.model;

import java.util.Objects;

public class Element {

    private final String value;
    private final int column;

    public Element(String value, int column) {
        this.value = value;
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Element)) return false;
        Element element = (Element) o;
        return column == element.column && Objects.equals(value, element.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, column);
    }
}
