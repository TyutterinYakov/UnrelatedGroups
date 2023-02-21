package app.company.reader.model;

import java.util.Objects;

public class Line {

    private final String value;
    private final Group group;

    public Line(String value, Group group) {
        this.value = value;
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Line)) return false;
        Line line = (Line) o;
        return Objects.equals(value, line.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
