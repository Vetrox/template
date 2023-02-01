package generics.graph.impl;

import generics.graph.PropertyHoldingEdge;
import generics.graph.PropertyHoldingGraph;

import java.util.LinkedHashMap;
import java.util.Set;

public class AdjacencyListGraph<IdentifierT, EdgePropertyT> implements PropertyHoldingGraph<IdentifierT,
        EdgePropertyT> {

    private long edgeCount = 0;
    private final LinkedHashMap<IdentifierT, LinkedHashMap<IdentifierT, PropertyHoldingEdge<IdentifierT,
            EdgePropertyT>>> vertices = new LinkedHashMap<>();

    @Override
    public void removeVertex(IdentifierT v) {
        assertVertices(v);

        vertices.forEach((from, value) -> {
            if (value.containsKey(v))
                removeEdge(from, v);
        });
        edgeCount -= vertices.get(v).size();
        vertices.remove(v);
    }

    @Override
    public void addVertex(IdentifierT v) {
        if (containsVertex(v))
            return;
        vertices.put(v, new LinkedHashMap<>());
    }

    @Override
    public void addEdge(PropertyHoldingEdge<IdentifierT, EdgePropertyT> edge) {
        if (containsEdge(edge.getFrom(), edge.getTo()))
            throw new RuntimeException("Edge already exists");
        addVertex(edge.getFrom());
        addVertex(edge.getTo());
        vertices.get(edge.getFrom()).put(edge.getTo(), edge);
        ++edgeCount;
    }

    @Override
    public PropertyHoldingEdge<IdentifierT, EdgePropertyT> getEdge(IdentifierT from, IdentifierT to) {
        assertEdge(from, to);
        return vertices.get(from).get(to);
    }

    @Override
    public boolean containsEdge(IdentifierT from, IdentifierT to) {
        return vertices.containsKey(from) && vertices.get(from).containsKey(to);
    }

    @Override
    public void removeEdge(IdentifierT from, IdentifierT to) {
        assertEdge(from, to);
        vertices.get(from).remove(to);
        --edgeCount;
    }

    @Override
    public long getEdgeCount() {
        return edgeCount;
    }

    @Override
    public int getVertexAmount() {
        return vertices.size();
    }

    @Override
    public Set<IdentifierT> getNeighbors(IdentifierT from) {
        assertVertices(from);
        return vertices.get(from).keySet();
    }

    @Override
    public Set<IdentifierT> getVertices() {
        return vertices.keySet();
    }

    private void assertEdge(IdentifierT from, IdentifierT to) {
        assertVertices(from);
        assertVertices(to);
        if (!containsEdge(from, to))
            throw new RuntimeException("Edge ('" + from.toString() + "' -> '" + to.toString() + "') didn't exist.");
    }

    private void assertVertices(IdentifierT v) {
        if (!containsVertex(v))
            throw new RuntimeException("Vertex '" + v.toString() + "' not found.");
    }

}
