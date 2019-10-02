package bearmaps.utils.graph;

import bearmaps.utils.pq.MinHeapPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {

    private AStarGraph<Vertex> input;
    private Vertex goal;

    private LinkedList<Vertex> solution;
    private SolverOutcome outcome;
    private double solutionWeight;
    private double explorationTime;
    private int numStatesExplored;

    private MinHeapPQ<Vertex> fringe;
    private HashMap<Vertex, Double> toDist;
    private HashMap<Vertex, Vertex> edgeTo;

    // Constructor which finds the solution, computing everything
    // necessary for all other methods to return their results in
    // constant time. Note that timeout passed in is in seconds.
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout){
        Stopwatch sw = new Stopwatch();
        this.input = input;
        this.goal = end;

        this.solution = new LinkedList<>();
        this.outcome = SolverOutcome.SOLVED;
        this.solutionWeight = 0;
        this.explorationTime = 0;
        this.numStatesExplored = 0;

        fringe = new MinHeapPQ<>();
        toDist = new HashMap<>();
        edgeTo = new HashMap<>();

        toDist.put(start, 0.0);
        edgeTo.put(start, null);
        fringe.insert(start, toDist.get(start) + input.estimatedDistanceToGoal(start, end));

        while (!fringe.isEmpty() && !fringe.peek().equals(end) && explorationTime < timeout) {
            Vertex currVertex = fringe.poll();
            List<WeightedEdge<Vertex>> neigh = input.neighbors(currVertex);
            for (WeightedEdge<Vertex> edge : input.neighbors(currVertex)) {
                relax(edge);
            }
            explorationTime = sw.elapsedTime();
        }

        explorationTime = sw.elapsedTime();

        if (fringe.isEmpty()) {
            this.outcome = SolverOutcome.UNSOLVABLE;

        } else if (explorationTime > timeout) {
            this.outcome = SolverOutcome.TIMEOUT;

        } else if (fringe.peek().equals(end)) {
            this.outcome = SolverOutcome.SOLVED;
            Vertex currVertex = fringe.peek();
            solution.add(currVertex);
            solutionWeight = toDist.get(currVertex);

            while (edgeTo.get(currVertex) != null) {
                currVertex = edgeTo.get(currVertex);
                solution.addFirst(currVertex);
            }
        }

    }

    private void relax(WeightedEdge<Vertex> currEdge) {
        Vertex from = currEdge.from();
        Vertex to = currEdge.to();
        Double weight = currEdge.weight();

        if (!toDist.containsKey(to) || toDist.get(from) + weight < toDist.get(to)) {
            toDist.put(to, toDist.get(from) + weight);
            edgeTo.put(to, from);
            if (fringe.contains(to)) {
                fringe.changePriority(to, toDist.get(to) + input.estimatedDistanceToGoal(to, goal));
            } else {
                fringe.insert(to, toDist.get(to) + input.estimatedDistanceToGoal(to, goal));
            }
        }

    }

    // Returns one of
    // SolverOutcome.SOLVED,
    // SolverOutcome.TIMEOUT, or
    // SolverOutcome.UNSOLVABLE.
    // Should be SOLVED if the AStarSolver
    // was able to complete all work in the time given.
    // UNSOLVABLE if the priority queue became empty before finding the solution.
    // TIMEOUT if the solver ran out of time.
    // You should check to see if you have run out of time every time you dequeue.
    public SolverOutcome outcome(){
        return outcome;
    }

    // A list of vertices corresponding to a solution.
    // Should be empty if result was TIMEOUT or UNSOLVABLE.
    public List<Vertex> solution(){
        return solution;
    }

    // The total weight of the given solution,
    // taking into account edge weights.
    // Should be 0 if result was TIMEOUT or UNSOLVABLE.
    public double solutionWeight(){
        return solutionWeight;
    }

    // The total number of priority queue poll() operations.
    // Should be the number of states explored so far
    // if result was TIMEOUT or UNSOLVABLE.
    public int numStatesExplored(){
        return numStatesExplored;
    }

    // The total time spent in seconds by the constructor.
    public double explorationTime(){
        return explorationTime;
    }
}
