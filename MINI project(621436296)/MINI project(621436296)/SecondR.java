import java.util.*;// import all utility classes like Map, HashMap, List, etc.

public class SecondR {

    // Vehicle variables class
    static class Vehicle {
        int priority;
        String type;
        String location;
        String time;
        String direction;

        Vehicle(int priority, String type, String location, String time, String direction) {
            this.priority = priority;
            this.type = type;
            this.location = location;
            this.time = time;
            this.direction = direction;
        }// construct
    } 

    

    // Tree node class
    static class TreeNode {//Define BST for treenode
        Vehicle vehicle;// each node store vehicle object 
        TreeNode left, right; //Has two child references: left and right

        TreeNode(Vehicle vehicle) {
            this.vehicle = vehicle;
        }// construct for tree node
    }

       static class VehicleBST {
        TreeNode root; // nested class manage BST treee node calles root

        // Insert cars 
        void insert(Vehicle v) {
            root = insertRec(root, v); //helper method for update root  
        }

        // Recursive method
        private TreeNode insertRec(TreeNode root, Vehicle v) {
            if (root == null) return new TreeNode(v); // if current node full create new
            if (v.priority < root.vehicle.priority) //vehicle's priority is less tha current node insert into left
                root.left = insertRec(root.left, v);
            else
                root.right = insertRec(root.right, v); //other vise in to right
            return root;
        }

        // display vehicles high to low priority
        void serveVehiclesHighToLow(TreeNode node) {
            if (node == null) return;
            serveVehiclesHighToLow(node.right);
            Vehicle v = node.vehicle; //
            System.out.printf("GREEN for %s at %s (%s, %s, Priority %d)%n", //after visit subtree print cuurebt node
                    v.type.toUpperCase(), v.location, v.time,
                    v.direction.toUpperCase(), v.priority); //upgrade message with upperclass
            serveVehiclesHighToLow(node.left);
        }//recursively visit the left subtree
    }

    public static void main(String[] args) {
        // Priority map for each vehicle type
        Map<String, Integer> priorityMap = Map.of(
                "ambulance", 5,
                "firetruck", 4,
                "police", 3,
                "car", 1
        );// priority listt

        // Predefined vehicles data
        String[][] vehiclesData = {
                {"ambulance", "Junction A", "08:05AM", "north"},
                {"car", "Junction F", "08:05AM", "south"},
                {"firetruck", "Junction B", "08:06AM", "east"},
                {"car", "Junction G", "08:06AM", "east"},
                {"police", "Junction C", "08:07AM", "west"},
                {"car", "Junction H", "08:08AM", "north"}
               
        };

        VehicleBST tree = new VehicleBST(); // Create instance 

        for (String[] v : vehiclesData) {//loop over vehicle
            String type = v[0], loc = v[1], t = v[2], dir = v[3]; //get type, location, time, direction.
            int prio = priorityMap.getOrDefault(type, 1);//get  priority map
            Vehicle vehicle = new Vehicle(prio, type, loc, t, dir);
            tree.insert(vehicle); // Insert into BST

            // Print detection message
            System.out.printf(" Cars Detected: %s at %s, %s, going %s (Priority %d)%n",
                    type.toUpperCase(), loc, t, dir.toUpperCase(), prio);
        }

        // Serve vehicles in priority order
        System.out.println("\nGiving green light based on priority:");
        tree.serveVehiclesHighToLow(tree.root);
    }
}