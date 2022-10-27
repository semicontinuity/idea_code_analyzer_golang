package semicontinuity.idea.code.analyzer.golang.actions;

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
}
