package generics;

public class AdjacencyListFlowNetwork<IdentifierT> extends AdjacencyListGraph<IdentifierT,
        AdjacencyListFlowNetwork<IdentifierT>.GraphProperties, AdjacencyListFlowNetwork.EdgeProperties> {

    protected final class GraphProperties {
        private IdentifierT source;
        private IdentifierT drain;
    }

    public static final class EdgeProperties {
        private double capacity;
        private double flow = 0;

        public EdgeProperties(double capacity) {
            this.capacity = capacity;
        }
    }

    private AdjacencyListFlowNetwork<IdentifierT> restGraph;

    public AdjacencyListFlowNetwork() {
        super(null, (i1, i2) -> {}, (properties, v) -> {
            if (properties.source.equals(v) || properties.drain.equals(v))
                throw new RuntimeException("Vertex '" + v + "' was the source or drain.");
        }, (properties, edge) -> {
            validateFlow(edge.properties.flow);
            if (edge.properties.capacity < edge.properties.flow)
                throw new RuntimeException("Flow was not less than or equal to capacity");
        }, (i1, i2) -> {});
        setProperties(new GraphProperties());
    }

    public void setSource(IdentifierT source) {
        getProperties().source = source;
    }

    public IdentifierT getSource() {
        return getProperties().source;
    }

    public IdentifierT getDrain() {
        return getProperties().drain;
    }

    public void setDrain(IdentifierT drain) {
        getProperties().drain = drain;
    }

    public double getReverseFlow(IdentifierT from, IdentifierT to) {
        return getFlow(to, from);
    }

    public AdjacencyListFlowNetwork<IdentifierT> constructRestGraph() {
        if (restGraph != null)
            return restGraph; // once the restgraph exists, it gets automatically updated
        restGraph = new AdjacencyListFlowNetwork<>();
        for (IdentifierT from : getVertices())
            for (IdentifierT to : getNeighbors(from))
                updateRestGraph(from, to);
        restGraph.setSource(this.getSource());
        restGraph.setDrain(this.getDrain());
        return restGraph;
    }

    public void setFlow(IdentifierT from, IdentifierT to, double flow) {
        validateFlow(flow);
        getEdge(from, to).properties.flow = flow;
        updateRestGraph(from, to);
    }

    public void setReverseFlow(IdentifierT from, IdentifierT to, double flow) {
        setFlow(to, from, flow);
    }

    public void setCapacity(IdentifierT from, IdentifierT to, double capacity) {
        validateCapacity(from, to, capacity);
        getEdge(from, to).properties.capacity = capacity;
        updateRestGraph(from, to);
    }

    private void updateRestGraph(IdentifierT from, IdentifierT to) {
        if (restGraph == null)
            return;
        double edgeFlow = getFlow(from, to);
        double edgeCapacity = getCapacity(from, to);
        if (edgeFlow < edgeCapacity)
            restGraph.forceEdgeState(from, to, edgeCapacity - edgeFlow);
        else
            restGraph.forceRemoveEdge(from, to);
        if (edgeFlow > 0)
            restGraph.forceEdgeState(to, from, edgeFlow);
        else
            restGraph.forceRemoveEdge(to, from);
    }

    public double getCapacity(IdentifierT from, IdentifierT to) {
        return getEdge(from, to).properties.capacity;
    }

    public double getFlow(IdentifierT from, IdentifierT to) {
        return getEdge(from, to).properties.flow;
    }

    private void forceRemoveEdge(IdentifierT from, IdentifierT to) {
        if (containsEdge(from, to))
            removeEdge(from, to);
    }

    private void forceEdgeState(IdentifierT from, IdentifierT to, double capacity) {
        if (containsEdge(from, to))
            setCapacity(from, to, capacity);
        else
            addEdge(from, to, new EdgeProperties(capacity));
    }

    public static void validateFlow(double flow) {
        if (flow < 0)
            throw new RuntimeException("Invalid flow value of " + flow);
    }

    public void validateCapacity(IdentifierT from, IdentifierT to, double capacity) {
        if (capacity <= 0 || capacity < getFlow(from, to))
            throw new RuntimeException("Invalid capacity value of " + capacity);
    }
}
