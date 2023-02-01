package generics.graph.impl;

import generics.graph.PropertyHoldingEdge;

public final class PropertyHoldingEdgeImpl<IdentifierT, PropertyT> extends EdgeImpl<IdentifierT> implements PropertyHoldingEdge<IdentifierT, PropertyT> {

    private PropertyT property;

    public PropertyHoldingEdgeImpl(IdentifierT from, IdentifierT to, PropertyT property) {
        super(from, to);
        this.property = property;
    }

    @Override
    public PropertyT getProperty() {
        return property;
    }

    @Override
    public void setProperty(PropertyT property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return "PropertyHoldingEdge{" + "from=" + super.from + ", to=" + super.to + ", property=" + property + '}';
    }
}
