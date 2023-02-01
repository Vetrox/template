package generics.graph;

import java.util.Set;

public interface PropertyHoldingGraph<IdentifierT, PropertyT> {
    void removeVertex(IdentifierT v);

    void addVertex(IdentifierT v);

    void addEdge(PropertyHoldingEdge<IdentifierT, PropertyT> edge);

    PropertyHoldingEdge<IdentifierT, PropertyT> getEdge(IdentifierT from, IdentifierT to);

    default PropertyHoldingEdge<IdentifierT, PropertyT> getReverseEdge(IdentifierT from, IdentifierT to) {
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
