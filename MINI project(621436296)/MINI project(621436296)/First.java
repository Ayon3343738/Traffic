import java.util.*; // import all utility classes like map, hashmap, list, 

public class First { // declare first class 

    static Map<String, Queue<String>> queues = new HashMap<>(); //A map to store vehicle queues for each direction (north, south, east, west).
    static Map<String, List<Integer>> waitTimes = new HashMap<>(); //map store list of wait times
    static Scanner scanner = new Scanner(System.in); // scan the user input

    public static void main(String[] args) { // main method
        // Initialize directions
        String[] directions = {"north", "south", "east", "west"}; // array for correcr direction
        for (String dir : directions) {
            queues.put(dir, new LinkedList<>());
            waitTimes.put(dir, new ArrayList<>());
        } // input queue and time 

        // Main loop
        while (true) { // keep run option 3 choose
            System.out.println("\n------- Traffic Signal Control ===");
            System.out.println("1. Add car to road");
            System.out.println("2. Serve green light request");
            System.out.println("3. Exit due to Emergency vehicle");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();  // get user input

            switch (choice) { // Handle user choice
                case "1":
                    addCar();
                    break; // id user add car function
                case "2":
                    String direction = getGreenLightDirection();
                    if (direction != null) {
                        serveVehicles(direction);
                    } else {
                        System.out.println("No cars in any direction.");
                    }
                    break; // call the function getGreenLightDirection();  to If a direction has vehicle it calls serveVehicles() to let cars pass
                case "3":
                    System.out.println("Exiting traffic controller due to emergency vehicle. Bye!");
                    return; // exit program 
                default:
                    System.out.println("wroong option. Please choose 1, 2, or 3.");
            } // invalid input 
        }
    }

    static void addCar() {
        System.out.print("Enter direction (north/south/east/west): ");
        String direction = scanner.nextLine().trim().toLowerCase(); // convert format output and het direction prompt
        if (!queues.containsKey(direction)) {
            System.out.println("wrong  direction. Try again."); // valid direction input 
            return;
        }

        System.out.print("Enter car type (regular/public/emergency): ");
        String carType = scanner.nextLine().trim().toLowerCase(); // get car type and convert lower
        if (!Arrays.asList("regular", "public", "emergency").contains(carType)) {
            System.out.println("Invalid car type.");
            return; // find valid car type
        }

        System.out.print("Enter wait time in seconds (10-100): ");//  ask time
        try {
            int waitTime = Integer.parseInt(scanner.nextLine());
            queues.get(direction).add(carType);
            waitTimes.get(direction).add(waitTime); // wait time adds with car type
            System.out.println(carType.substring(0, 1).toUpperCase() + carType.substring(1) +
                    " added to " + direction.substring(0, 1).toUpperCase() + direction.substring(1) + " queue."); // confirm message
        } catch (NumberFormatException e) { 
            System.out.println("Invalid wait time. Please enter a number.");
        } // find wrong input time
    }

    static String getGreenLightDirection() {
        double maxScore = -1;
        String selectedDirection = null; // get green light function

        for (String direction : queues.keySet()) {
            int count = queues.get(direction).size();
            double avgWait = 0; // get number of cars
            if (count > 0) {
                int totalWait = 0;
                for (int t : waitTimes.get(direction)) {
                    totalWait += t;
                }
                avgWait = (double) totalWait / count;
            } // calculate averarge wait time
            double score = count + avgWait; //add score count time 
            System.out.printf("%s - Vehicles: %d, Avg Wait: %.2f, Score: %.2f%n",
                    capitalize(direction), count, avgWait, score); // shoe data with each direction

            if (score > maxScore) {
                maxScore = score;
                selectedDirection = direction;
            }
        }
        return selectedDirection; // choose direction with high speed
    }

    static void serveVehicles(String direction) {
        System.out.println("\nGreen light given to " + direction.toUpperCase() + " direction:");
        int count = 0;// message for release vehicle 
        Queue<String> carQueue = queues.get(direction);
        List<Integer> waitList = waitTimes.get(direction); //get queue list and time

        while (!carQueue.isEmpty() && count < 3) {
            String car = carQueue.poll();
            waitList.remove(0);
            System.out.println("  Car (" + car + ") passed.");
            count++; 
        }
    } // let 3 car release remove queue and list

    static String capitalize(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
} // captal first letter 
