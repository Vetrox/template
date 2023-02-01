package generics.graph.impl;

import generics.graph.AdjacencyListFlowNetwork;
import generics.graph.PropertyHoldingEdge;

public class AdjacencyListFlowNetworkImpl<IdentifierT> extends AdjacencyListGraph<IdentifierT,
        AdjacencyListFlowNetwork.FlowCapacityProperty> implements AdjacencyListFlowNetwork<IdentifierT> {
    // TODO: update restgraph when deleting or adding new vertices / edges

    private IdentifierT source;
    private IdentifierT drain;
    private AdjacencyListFlowNetworkImpl<IdentifierT> restGraph;

    @Override
    public void removeVertex(IdentifierT v) {
        if (source.equals(v) || drain.equals(v))
            throw new RuntimeException("Vertex '" + v + "' was the source or drain.");
        super.removeVertex(v);
    }

    @Override
    public void addEdge(PropertyHoldingEdge<IdentifierT, FlowCapacityProperty> edge) {
        super.addEdge(edge);
    }

    @Override
    public IdentifierT getSource() {
        return this.source;
    }

    @Override
    public void setSource(IdentifierT source) {
        this.source = source;
    }

    @Override
    public IdentifierT getDrain() {
        return this.drain;
    }

    @Override
    public void setDrain(IdentifierT drain) {
        this.drain = drain;
    }

    @Override
    public AdjacencyListFlowNetwork<IdentifierT> constructRestGraph() {
        if (restGraph != null)
            return restGraph; // once the restgraph exists, it gets automatically updated
        restGraph = new AdjacencyListFlowNetworkImpl<>();
        for (IdentifierT from : getVertices())
            for (IdentifierT to : getNeighbors(from))
                updateRestGraph(from, to);
        restGraph.setSource(this.getSource());
        restGraph.setDrain(this.getDrain());
        return restGraph;
    }

    @Override
    public void setFlow(IdentifierT from, IdentifierT to, double flow) {
        getEdge(from, to).getProperty().setFlow(flow);
        updateRestGraph(from, to);
    }

    @Override
    public void setCapacity(IdentifierT from, IdentifierT to, double capacity) {
        getEdge(from, to).getProperty().setCapacity(capacity);
        updateRestGraph(from, to);
    }

    @Override
    public double getCapacity(IdentifierT from, IdentifierT to) {
        return getEdge(from, to).getProperty().getCapacity();
    }

    @Override
    public double getFlow(IdentifierT from, IdentifierT to) {
        return getEdge(from, to).getProperty().getFlow();
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

    private void forceRemoveEdge(IdentifierT from, IdentifierT to) {
        if (containsEdge(from, to))
            removeEdge(from, to);
    }

    private void forceEdgeState(IdentifierT from, IdentifierT to, double capacity) {
        if (containsEdge(from, to))
            setCapacity(from, to, capacity);
        else
            addEdge(new PropertyHoldingEdgeImpl<>(from, to, new FlowCapacityProperty(capacity)));
    }

}
