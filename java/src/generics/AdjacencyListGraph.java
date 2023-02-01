package generics;

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.function.BiConsumer;

public class AdjacencyListGraph<IdentifierT, GraphProperties, EdgeProperties> {

    public final class Edge {
        public IdentifierT from;
        public IdentifierT to;
        public EdgeProperties properties;

        public Edge(IdentifierT from, IdentifierT to, EdgeProperties properties) {
            this.from = from;
            this.to = to;
            this.properties = properties;
        }

        @Override
        public String toString() {
            return "Edge{" + "from=" + from + ", to=" + to + ", properties=" + properties + '}';
        }
    }

    private long edgeCount = 0;
    private GraphProperties properties;
    private final LinkedHashMap<IdentifierT, LinkedHashMap<IdentifierT, Edge>> vertices = new LinkedHashMap<>();
    private final BiConsumer<GraphProperties, IdentifierT> onVertexAdd;
    private final BiConsumer<GraphProperties, IdentifierT> onVertexRemove;
    private final BiConsumer<GraphProperties, Edge> onEdgeAdd;
    private final BiConsumer<GraphProperties, Edge> onEdgeRemove;

    public AdjacencyListGraph() {
        this(null, (i1, i2) -> {}, (i1, i2) -> {}, (i1, i2) -> {}, (i1, i2) -> {});
    }

    public AdjacencyListGraph(GraphProperties properties, BiConsumer<GraphProperties, IdentifierT> onVertexAdd,
                              BiConsumer<GraphProperties, IdentifierT> onVertexRemove, BiConsumer<GraphProperties,
            Edge> onEdgeAdd, BiConsumer<GraphProperties, Edge> onEdgeRemove) {
        this.properties = properties;
        this.onVertexAdd = onVertexAdd;
        this.onVertexRemove = onVertexRemove;
        this.onEdgeAdd = onEdgeAdd;
        this.onEdgeRemove = onEdgeRemove;
    }

    public void setProperties(GraphProperties properties) {
        this.properties = properties;
    }

    public void removeVertex(IdentifierT v) {
        assertVertices(v);
        onVertexRemove.accept(getProperties(), v);
        vertices.forEach((from, value) -> {
            if (value.containsKey(v))
                removeEdge(from, v);
        });
        edgeCount -= vertices.get(v).size();
        vertices.remove(v);
    }

    public void addVertex(IdentifierT v) {
        if (containsVertex(v))
            return;
        onVertexAdd.accept(getProperties(), v);
        vertices.put(v, new LinkedHashMap<>());
    }

    public GraphProperties getProperties() {
        return properties;
    }

    public void addEdge(IdentifierT from, IdentifierT to, EdgeProperties edgeProperties) {
        if (vertices.containsKey(from) && vertices.get(from).containsKey(to))
            throw new RuntimeException("Edge already exists");
        addVertex(from);
        addVertex(to);
        Edge edge = new Edge(from, to, edgeProperties);
        onEdgeAdd.accept(getProperties(), edge);
        vertices.get(from).put(to, edge);
        ++edgeCount;
    }

    public void addReverseEdge(IdentifierT from, IdentifierT to, EdgeProperties edgeProperties) {
        addEdge(to, from, edgeProperties);
    }

    public Edge getEdge(IdentifierT from, IdentifierT to) {
        assertEdge(from, to);
        return vertices.get(from).get(to);
    }

    public Edge getReverseEdge(IdentifierT from, IdentifierT to) {
        return getEdge(to, from);
    }

    public boolean containsEdge(IdentifierT from, IdentifierT to) {
        return vertices.containsKey(from) && vertices.get(from).containsKey(to);
    }

    public boolean containsReverseEdge(IdentifierT from, IdentifierT to) {
        return containsEdge(to, from);
    }

    public void removeEdge(IdentifierT from, IdentifierT to) {
        assertEdge(from, to);
        onEdgeRemove.accept(getProperties(), getEdge(from, to));
        vertices.get(from).remove(to);
        --edgeCount;
    }

    public void removeReverseEdge(IdentifierT from, IdentifierT to) {
        removeEdge(to, from);
    }

    public long getEdgeCount() {
        return edgeCount;
    }

    public int getVertexAmount() {
        return vertices.size();
    }

    public Set<IdentifierT> getNeighbors(IdentifierT from) {
        assertVertices(from);
        return vertices.get(from).keySet();
    }

    public boolean containsVertex(IdentifierT t) {
        return vertices.containsKey(t);
    }

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
