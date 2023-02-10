package app.company.reader.model;

import java.util.Objects;

public class Group {
    private long number;

    public Group(long number) {
        this.number = number;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        Group group = (Group) o;
        return number == group.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
