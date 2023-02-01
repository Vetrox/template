package fulkerson;

import generics.graph.AdjacencyListFlowNetwork;
import generics.graph.impl.AdjacencyListFlowNetworkImpl;

import java.util.*;

public class FordFulkerson {

    private static class Path<IdentifierT> implements Iterable<Path<IdentifierT>.Edge>,
            Iterator<Path<IdentifierT>.Edge> {
        @Override
        public Iterator<Edge> iterator() {
            return this;
        }

        public final class Edge {
            public final IdentifierT from;
            public final IdentifierT to;

            public Edge(IdentifierT from, IdentifierT to) {
                this.from = from;
                this.to = to;
            }
        }

        private final ListIterator<IdentifierT> pathIter;
        private IdentifierT previous;

        private Path(List<IdentifierT> path) {
            this.pathIter = path.listIterator();

            if (hasNext())
                this.previous = pathIter.next(); // skip 1st
        }

        @Override
        public boolean hasNext() {
            return pathIter.hasNext();
        }

        @Override
        public Edge next() {
            IdentifierT cachePrev = previous;
            previous = pathIter.next();
            return new Edge(cachePrev, previous);
        }
    }

    private record PathAndCapacity<IdentifierT>(Path<IdentifierT> path, double choke) {}

    public static <IdentifierT> double computeMaxFlow(AdjacencyListFlowNetworkImpl<IdentifierT> network) {
        // flow is initialized to 0 automatically
        double maxFlow = 0;
        AdjacencyListFlowNetwork<IdentifierT> restNetwork = network.constructRestGraph();
        PathAndCapacity<IdentifierT> augPath = getPath(restNetwork);
        while (augPath.choke > 0) {
            augment(network, augPath);
            restNetwork = network.constructRestGraph();
            maxFlow += augPath.choke;
            augPath = getPath(restNetwork);
        }
        return maxFlow;
    }

    private static <IdentifierT> void augment(AdjacencyListFlowNetworkImpl<IdentifierT> network,
                                              PathAndCapacity<IdentifierT> augPath) {
        for (Path<IdentifierT>.Edge edge : augPath.path) {
            if (network.containsEdge(edge.from, edge.to))
                network.setFlow(edge.from, edge.to, network.getFlow(edge.from, edge.to) + augPath.choke);
            if (network.containsReverseEdge(edge.from, edge.to))
                network.setReverseFlow(edge.from, edge.to, network.getReverseFlow(edge.from, edge.to) - augPath.choke);
        }
    }

    private static <IdentifierT> PathAndCapacity<IdentifierT> getPath(AdjacencyListFlowNetwork<IdentifierT> network) {
        LinkedList<IdentifierT> path = new LinkedList<>();
        double choke = getPathInternal(network.getSource(), network, new HashSet<>(), path);
        return new PathAndCapacity<>(new Path<>(path), choke);
    }

    private static <IdentifierT> double getPathInternal(IdentifierT from,
                                                        AdjacencyListFlowNetwork<IdentifierT> network,
                                                        Set<IdentifierT> visited, LinkedList<IdentifierT> path) {
        if (from.equals(network.getDrain())) {
            path.addFirst(from);
            return Double.MAX_VALUE;
        }
        visited.add(from);
        for (IdentifierT neighbor : network.getNeighbors(from)) {
            if (visited.contains(neighbor))
                continue;
            validateCapacity(network.getCapacity(from, neighbor));
            double choke = getPathInternal(neighbor, network, visited, path);
            if (choke > 0) {
                path.addFirst(from);
                return Math.min(choke, network.getCapacity(from, neighbor));
            }
        }
        return 0;
    }

    private static void validateCapacity(double capacity) {
        if (capacity <= 0)
            throw new RuntimeException("Somewhere else should be guaranteed that no edge with capacity=0 is returned "
                    + "from getNeighbors()");
    }
}
