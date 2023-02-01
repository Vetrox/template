package generics.graph;


public interface AdjacencyListFlowNetwork<IdentifierT> extends PropertyHoldingGraph<IdentifierT,
        AdjacencyListFlowNetwork.FlowCapacityProperty> {
    IdentifierT getSource();

    void setSource(IdentifierT source);

    IdentifierT getDrain();

    void setDrain(IdentifierT drain);

    default double getReverseFlow(IdentifierT from, IdentifierT to) {
        return getFlow(to, from);
    }

    AdjacencyListFlowNetwork<IdentifierT> constructRestGraph();

    void setFlow(IdentifierT from, IdentifierT to, double flow);

    default void setReverseFlow(IdentifierT from, IdentifierT to, double flow) {
        setFlow(to, from, flow);
    }

    void setCapacity(IdentifierT from, IdentifierT to, double capacity);

    double getCapacity(IdentifierT from, IdentifierT to);

    double getFlow(IdentifierT from, IdentifierT to);

    final class FlowCapacityProperty {
        private double capacity;
        private double flow = 0;

        public FlowCapacityProperty(double capacity) {
            this.capacity = capacity;
            validate();
        }

        public double getCapacity() {
            return capacity;
        }

        public void setCapacity(double capacity) {
            this.capacity = capacity;
            validate();
        }

        public double getFlow() {
            return flow;
        }

        public void setFlow(double flow) {
            this.flow = flow;
            validate();
        }

        public void validate() {
            assert 0 <= flow : "Invalid flow value of " + flow;
            assert 0 < capacity && flow <= capacity : "Invalid capacity value of " + capacity;
        }
    }
}
