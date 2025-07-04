import java.util.*; // import all utility classes

public class AllafterViva {

    static Scanner scanner = new Scanner(System.in); // scan user input

    static class VehicleInfo {
        String type;
        int waitTime;

        VehicleInfo(String type, int waitTime) {
            this.type = type;
            this.waitTime = waitTime;
        }
    } //store detail of each vehicle

    static final String[] DIRECTIONS = {"north", "south", "east", "west"}; //array list of valid direction

    // Main structure: Map of Junction
    static Map<String, Map<String, LinkedList<VehicleInfo>>> junctions = new HashMap<>();

    static void initJunction(String name) {
        Map<String, LinkedList<VehicleInfo>> queues = new HashMap<>();
        for (String d : DIRECTIONS) {
            queues.put(d, new LinkedList<>());
        }
        junctions.put(name, queues);
        System.out.println(" New Junction " + name + " Added");
    } //added new junction

    static void addCar() {
        System.out.print("Enter junction name  ");
        String junction = scanner.nextLine().trim();
        if (!junctions.containsKey(junction)) {
            System.out.println("Junction not found.");
            return;
        } //enter junction by when you added

        System.out.print("Enter direction (north/south/east/west): ");
        String dir = scanner.nextLine().trim().toLowerCase();
        if (!Arrays.asList(DIRECTIONS).contains(dir)) {
            System.out.println("Error direction.");
            return;
        }// enter direction

        System.out.print("Enter car type (regular/public/emergency): ");
        String type = scanner.nextLine().trim().toLowerCase();
        if (!type.equals("regular") && !type.equals("public") && !type.equals("emergency")) {
            System.out.println("Error car type.");
            return;
        } // enter car type

        System.out.print("Enter wait time in seconds: ");
        try {
            int wait = Integer.parseInt(scanner.nextLine().trim());
            junctions.get(junction).get(dir).add(new VehicleInfo(type, wait));
            System.out.printf("%s added  %s queue  %s.\n", capitalize(type), capitalize(dir), junction);
        } catch (NumberFormatException e) {
            System.out.println("Please enter an integer wait time.");
        } //enter time and print what add
    }

    static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    } //capitalize first letter and other simple

    static double avgWaitTime(LinkedList<VehicleInfo> queue) {
        if (queue.isEmpty()) return 0;
        int sum = 0;
        for (VehicleInfo v : queue) sum += v.waitTime;
        return (double) sum / queue.size();
    } // calculate avg time all times/ no of cars

    static double scoreQueue(LinkedList<VehicleInfo> queue) {
        return queue.size() + avgWaitTime(queue);
    } // score= avgtime+ queue.size

    static String getGreenLightDirection(Map<String, LinkedList<VehicleInfo>> queues) {
        double maxScore = -1;
        String bestDir = null;
        for (String d : DIRECTIONS) {
            double score = scoreQueue(queues.get(d));
            if (score > maxScore) {
                maxScore = score;
                bestDir = d;
            }
        } // find highest score and release vehicles
        return bestDir;
    }

    static void serveVehicles() {
        System.out.print("Enter junction name: ");
        String junction = scanner.nextLine().trim();
        if (!junctions.containsKey(junction)) {
            System.out.println("Junction not found.");
            return;
        } // enter junction to relase

        Map<String, LinkedList<VehicleInfo>> queues = junctions.get(junction);
        int served = 0; //retrieves the map of direction queues for the given junction name

        for (String d : DIRECTIONS) {
            Iterator<VehicleInfo> it = queues.get(d).iterator();
            while (it.hasNext() && served < 3) {
                VehicleInfo v = it.next();
                if (v.type.equals("emergency")) {
                    System.out.printf("GREEN light %s: EMERGENCY vehicle passed  %s.\n", d.toUpperCase(), junction);
                    it.remove();
                    served++;
                } // if user add emergency vehicle then it will be imediately remove
            }
        }

        if (served > 0) return;//Because emergency vehicles have the highest priority no need to serve others

        String dir = getGreenLightDirection(queues);//if no emergency this method call
        if (dir == null || queues.get(dir).isEmpty()) {// if no direction OR best direction empty
            System.out.println("No vehicles waiting.");
            return;
        }

        System.out.printf("GREEN light %s === %s:\n", dir.toUpperCase(), junction);//print message green light given
        LinkedList<VehicleInfo> queue = queues.get(dir);
        served = 0; //reset served counter before non emergency
        List<String> priorities = List.of("public", "regular"); //define priority order non emrgency

        for (String priority : priorities) {
            Iterator<VehicleInfo> it = queue.iterator();
            while (it.hasNext() && served < 3) {
                VehicleInfo v = it.next();
                if (v.type.equals(priority)) {
                    System.out.printf("  %s vehicle passed.\n", v.type.toUpperCase());
                    it.remove();
                    served++;
                } 
            }
            if (served >= 3) break;
        }
    }// this serves non-emergency vehicles

    static void watchVehicles() {
        if (junctions.isEmpty()) {
            System.out.println("No junctions available.");
            return;
        } //choose 5

        System.out.println("=== VEHICLE STATUS AT ALL JUNCTIONS -------");
        for (String junction : junctions.keySet()) { //loop every junction 
            Map<String, LinkedList<VehicleInfo>> queues = junctions.get(junction);
            boolean hasVehicles = false; //check if there vehicles

            for (String dir : DIRECTIONS) {
                LinkedList<VehicleInfo> queue = queues.get(dir);
                if (!queue.isEmpty()) {
                    hasVehicles = true;
                    for (VehicleInfo v : queue) {
                        System.out.printf("Available cars are Junction=== %-10s \n Direction: %-6s  \n Type: %-10s  \n Wait Time: %ds\n",
                                junction, capitalize(dir), capitalize(v.type), v.waitTime);
                    }
                }
            } //display cars in every junction

            if (!hasVehicles) {
                System.out.printf("Junction: %-10s has no cars.\n", junction);
            }
        }
    }

    static class Edge {
        String neighbor;//destination node
        int weight;// time cost

        Edge(String neighbor, int weight) {
            this.neighbor = neighbor;
            this.weight = weight;
        }// store connection between nodes
    }

    static class Node implements Comparable<Node> {
        String name;// A B C D 
        int cost;
        List<String> path;//names that form the path from the starting node to the current Node. T

        Node(String name, int cost, List<String> path) {
            this.name = name;
            this.cost = cost;
            this.path = new ArrayList<>(path);//it creates a new ArrayList element from path list
            this.path.add(name);
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.cost, other.cost);
        }//compare cost for priority queue
    }

    static Map<String, List<Edge>> graph = new HashMap<>();//main graph

    static void buildGraph() {
        graph.put("A", List.of(new Edge("B", 4), new Edge("C", 2)));
        graph.put("B", List.of(new Edge("A", 4), new Edge("D", 5)));
        graph.put("C", List.of(new Edge("A", 2), new Edge("D", 1)));
        graph.put("D", List.of(new Edge("B", 5), new Edge("C", 1)));//add roads between points and weights
    }// ask user entry

    static void findShortestPath() {
        System.out.print("Enter starting point (A/B/C/D): ");
        String start = scanner.nextLine().trim().toUpperCase();
        System.out.print("Enter destination point (A/B/C/D): ");
        String end = scanner.nextLine().trim().toUpperCase();

        if (!graph.containsKey(start) || !graph.containsKey(end)) {
            System.out.println("Invalid nodes.");
            return;
        }// check input

        PriorityQueue<Node> pq = new PriorityQueue<>();//Creates a priority queue smallest total cost first.
        Set<String> visited = new HashSet<>();//Keeps track of visited nodes 
        pq.add(new Node(start, 0, new ArrayList<>())); //starting node to the queue with cost 0

        while (!pq.isEmpty()) {//keep checking untill reach end
            Node current = pq.poll();// get lowest cost
            if (visited.contains(current.name)) continue;
            visited.add(current.name);// Mark this node as visited.

            if (current.name.equals(end)) {
                System.out.println("Shortest path: " + String.join(" -> ", current.path));
                System.out.println("Total travel time: " + current.cost + " minutes");
                return;// end found show to user
            }

            for (Edge e : graph.getOrDefault(current.name, Collections.emptyList())) {
                if (!visited.contains(e.neighbor)) {
                    pq.add(new Node(e.neighbor, current.cost + e.weight, current.path));
                }//If neighbor not visited, create a new node with updated new cost
            
            }
        }

        System.out.println("No path found.");
    }

    public static void main(String[] args) {
        buildGraph();

        while (true) {
            System.out.println("\n---------======---==== TRAFFIC CONTROL SYSTEM ");
            System.out.println("0. Initialize new junction");
            System.out.println("1. Add car to queue");
            System.out.println("2. Serve vehicles (emergency first)");
            System.out.println("3. Find shortest route (Dijkstra)");
            System.out.println("4. Exit");
            System.out.println("5. Watch all vehicles at all junctions");
            System.out.print("Choose an option: ");// ask questions

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "0":
                    System.out.print("Enter junction name to initialize: ");
                    String name = scanner.nextLine().trim();
                    initJunction(name); // add junction
                    break;
                case "1":
                    addCar();// go addcar function
                    break;
                case "2":
                    serveVehicles();//go serveVehicles function
                     break;
                case "3":
                    findShortestPath();// go findShortestPath function
                    break;
                case "4":
                    System.out.println("Exiting. Stay safe!");
                    return;
                case "5":
                    watchVehicles(); //watch all cars by direction
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}

