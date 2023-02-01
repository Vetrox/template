package generics.graph;

import java.util.Set;

public interface Graph<IdentifierT> {
    void removeVertex(IdentifierT v);

    void addVertex(IdentifierT v);

    <T extends Edge<IdentifierT>> void addEdge(T edge);

    <T extends Edge<IdentifierT>> T getEdge(IdentifierT from, IdentifierT to);

    default <T extends Edge<IdentifierT>> T getReverseEdge(IdentifierT from, IdentifierT to) {
        return getEdge(to, from);
    }

    boolean containsEdge(IdentifierT from, IdentifierT to);

    default boolean containsReverseEdge(IdentifierT from, IdentifierT to) {
        return containsEdge(to, from);
    }

    void removeEdge(IdentifierT from, IdentifierT to);

    default void removeReverseEdge(IdentifierT from, IdentifierT to) {
        removeEdge(to, from);
    }

    long getEdgeCount();

    int getVertexAmount();

    Set<IdentifierT> getNeighbors(IdentifierT from);

    default boolean containsVertex(IdentifierT t) {
        return getVertices().contains(t);
    }

    Set<IdentifierT> getVertices();
}
