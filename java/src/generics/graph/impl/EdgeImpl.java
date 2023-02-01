package generics.graph.impl;

import generics.graph.Edge;

public class EdgeImpl<IdentifierT> implements Edge<IdentifierT> {

    protected IdentifierT from;
    protected IdentifierT to;

    public EdgeImpl(IdentifierT from, IdentifierT to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public IdentifierT getFrom() {
        return from;
    }

    @Override
    public void setFrom(IdentifierT from) {
        this.from = from;
    }

    @Override
    public IdentifierT getTo() {
        return to;
    }

    @Override
    public void setTo(IdentifierT to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Edge{" + "from=" + from + ", to=" + to + "}";
    }
}
