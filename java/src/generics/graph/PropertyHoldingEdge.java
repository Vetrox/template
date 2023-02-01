package generics.graph;

import generics.Property;

public interface PropertyHoldingEdge<IdentifierT, EdgeProperties> extends Edge<IdentifierT>, Property<EdgeProperties> {}
