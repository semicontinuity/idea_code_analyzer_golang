package semicontinuity.idea.code.analyzer.golang;

public class QualifiedName {
    private final String qualifier;
    private final String name;

    public QualifiedName(String qualifier, String name) {
        this.qualifier = qualifier;
        this.name = name;
    }

    public String getQualifier() {
        return qualifier;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return qualifier.isEmpty() ? name : qualifier + '.' + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QualifiedName that = (QualifiedName) o;

        if (!qualifier.equals(that.qualifier)) {
            return false;
        }
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = qualifier.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
