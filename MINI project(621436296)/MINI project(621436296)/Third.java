import java.util.*;//import all utility classes like map, hashmap, list, 

public class Third { // starts main class 

    static Map<String, List<Edge>> graph = new HashMap<>(); // define static graph

    static class Edge {
        String neighbor; //destination node
        int weight; // time cost

        Edge(String neighbor, int weight) {
            this.neighbor = neighbor;
            this.weight = weight; // constructors for variables 
        }
    }

    static class Node implements Comparable<Node> {
        String name; // A B C D 
        int cost; 
        List<String> path; // helper class for dijksta 

        Node(String name, int cost, List<String> path) {
            this.name = name; 
            this.cost = cost;
            this.path = new ArrayList<>(path);
            this.path.add(name);
        } // Constructor for node

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.cost, other.cost); // for min-heap
        } //compare cost for priority queue
    }

    public static int dijkstra(String start, String end) { //use Dijlksta metod
        PriorityQueue<Node> queue = new PriorityQueue<>(); 
        Set<String> visited = new HashSet<>();//  pick node lowest total and avoid same value

        queue.add(new Node(start, 0, new ArrayList<>())); // cost =0

        while (!queue.isEmpty()) {
            Node current = queue.poll(); //queue not empty take node with lowest total 

            if (visited.contains(current.name))
                continue; //already visited skip

            visited.add(current.name); //mark the node

            if (current.name.equals(end)) {
                System.out.println("Shortest path: " + String.join(" ➝   ", current.path));
                System.out.println("Answer  Total travel time is :  " + current.cost + "  minutes");
                return current.cost;
            } // print shortest path and return cost

            List<Edge> neighbors = graph.getOrDefault(current.name, new ArrayList<>()); // list all nighbors of current node graph
            for (Edge edge : neighbors) {
                if (!visited.contains(edge.neighbor)) {
                    queue.add(new Node(edge.neighbor, current.cost + edge.weight, current.path));
                }
            }
        } // new cost = cuurent cost+ weight path= current path+ neighbour

        System.out.println("No path found.");
        return Integer.MAX_VALUE;
    } // if loop fail destination not found

    public static void main(String[] args) { //main method 
        graph.put("A", List.of(new Edge("B", 4), new Edge("C", 2)));
        graph.put("B", List.of(new Edge("A", 4), new Edge("D", 5)));
        graph.put("C", List.of(new Edge("A", 2), new Edge("D", 1)));
        graph.put("D", List.of(new Edge("B", 5), new Edge("C", 1))); //Manually builds the graph with how weight

        Scanner scanner = new Scanner(System.in); //get user input 

        System.out.print("Enter starting point A B C D: "); // get first value convert uppercase
        String start = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter destination point A B C D: ");
        String end = scanner.nextLine().trim().toUpperCase();// last destination

        dijkstra(start, end); // call method
    }
}
