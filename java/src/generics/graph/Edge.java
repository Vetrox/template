package generics.graph;

public interface Edge<IdentifierT> {
    void setFrom(IdentifierT value);

    void setTo(IdentifierT value);

    IdentifierT getFrom();

    IdentifierT getTo();
}
