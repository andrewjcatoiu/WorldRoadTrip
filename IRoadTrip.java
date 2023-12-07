import java.io.*;
import java.util.*;

public class IRoadTrip {

    // Method to convert state names to country codes
    public Map<String, String> convertedCountries(String stateNamePath) {
        Map<String, String> CODES = new HashMap<>();

        try {
            File stateNameFile = new File(stateNamePath);
            FileReader fileReader = new FileReader(stateNameFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            bufferedReader.readLine();
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] parts = line.split("\t");

                if (parts.length >= 5) {
                    String stateID = parts[1];
                    String countryName = parts[2];
                    String endDate = parts[4];

                    if (endDate.equals("2020-12-31")) {
                        CODES.put(countryName, stateID);
                    }
                }

                line = bufferedReader.readLine();
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        CODES.put("Bahamas, The", "BHM");
        CODES.put("Belarus", "BLR");
        CODES.put("Bosnia and Herzegovina", "BOS");
        CODES.put("Burkina Faso", "BFO");
        CODES.put("Burma","MYA");
        CODES.put("Cabo Verde","CAP");
        CODES.put("Cambodia","CAM");
        CODES.put("Congo, Democratic Republic of the","DRC");
        CODES.put("Congo, Republic of the","CON");
        CODES.put("Cote d'Ivoire","CDI");
        CODES.put("Czechia","CZR");
        CODES.put("Democratic Republic of the Congo","DRC");
        CODES.put("Gambia, The","GAM");
        CODES.put("Germany","GFR");
        CODES.put("Iran","IRN");
        CODES.put("Italy","ITA");
        CODES.put("Korea, North","PRK");
        CODES.put("Korea, South","ROK");
        CODES.put("Kyrgyzstan","KYR");
        CODES.put("Lithuania (Kaliningrad Oblast)", "LIT");
        CODES.put("Macedonia","MAC");
        CODES.put("North Korea","PRK");
        CODES.put("North Macedonia","MAC");
        CODES.put("Republic of the Congo","CON");
        CODES.put("Russia","RUS");
        CODES.put("Russia (Kaliningrad)","RUS");
        CODES.put("South Korea","ROK");
        CODES.put("Sri Lanka","SRI");
        CODES.put("Tanzania","TAZ");
        CODES.put("The Gambia","GAM");
        CODES.put("Timor-Leste","ETM");
        CODES.put("Turkey","TUR");
        CODES.put("Poland (Kaliningrad Oblast)", "POL");
        CODES.put("Turkey (Turkiye)","TUR");
        CODES.put("UAE","UAE");
        CODES.put("UK","UKG");
        CODES.put("United States","USA");
        CODES.put("US","USA");
        CODES.put("Vietnam","DRV");
        CODES.put("Yemen","YEM");
        CODES.put("Zimbabwe","ZIM");
        CODES.put("Spain (Ceuta)", "SPN");
        CODES.put("Morocco (Ceuta)","MOR");
        CODES.put("Eswatini", "SWA");
        CODES.put("Czechia", "CZR");
        
        return CODES;
    }


    // Method to fix difficult country names
    private Map<String, String> fixDifficultCountryNames(){
        fixedCountries.put("United Arab Emirates","UAE");
        fixedCountries.put("UK","United Kingdom");
        fixedCountries.put("US","United States");
        fixedCountries.put("Cote d'Ivoire","Cote D'Ivoire");
        fixedCountries.put("Czech Republic","Czechia");
        fixedCountries.put("Congo, Democratic Republic of (Zaire)","Democratic Republic of the Congo");
        fixedCountries.put("Congo","Republic of the Congo");
        fixedCountries.put("Democratic Republic of the Congo","Congo, Democratic Republic of the");
        fixedCountries.put("Republic of the Congo","Congo, Republic of the");
        fixedCountries.put("Gambia, The","The Gambia");
        fixedCountries.put("German Federal Republic","Germany");
        fixedCountries.put("Morocco (Ceuta)", "Morocco");
        fixedCountries.put("Spain (Ceuta)", "Spain");
        fixedCountries.put("Iran (Persia)","Iran");
        fixedCountries.put("Italy/Sardinia","Italy");
        fixedCountries.put("Korea, Republic of","Korea, South");
        fixedCountries.put("Kyrgyz Republic","Kyrgyzstan");
        fixedCountries.put("Macedonia (Former Yugoslav Republic of)","Macedonia");
        fixedCountries.put("North Macedonia","Macedonia");
        fixedCountries.put("Macedonia", "North Macedonia");
        fixedCountries.put("Poland (Kaliningrad Oblast)", "Poland");
        fixedCountries.put("Russia (Kaliningrad Oblast)", "Poland");
        fixedCountries.put("Korea, People's Republic of","Korea, North");
        fixedCountries.put("North Korea","Korea, North");
        fixedCountries.put("South Korea","Korea, South");
        fixedCountries.put("Russia (Kaliningrad)", "Russia");
        fixedCountries.put("Lithuania (Kaliningrad Oblast)", "Lithuania");
        fixedCountries.put("Sri Lanka (Ceylon)","Sri Lanka");
        fixedCountries.put("Surinam","Suriname");
        fixedCountries.put("Tanzania/Tanganyika","Tanzania");
        fixedCountries.put("The Gambia","Gambia, The");
        fixedCountries.put("East Timor","Timor-Leste");
        fixedCountries.put("Turkey","Turkey (Turkiye)");

        return fixedCountries;
    }


    private Map<String,Map<String,Integer>> borders;
    private Map<String,Map<String,Integer>> capitalDistances;
    private Map<String, String> stateNames;
    private Map<String,String> fixedCountries = new HashMap<>();
    private Map<String,Map<String,Integer>> updatedCountries = new HashMap<>();
    

    // Class representing a node in the graph
    private class Node implements Comparable<Node> {
        private final String country;
        private final int distance;
    
        public Node(String country, int distance) {
            this.country = country;
            this.distance = distance;
        }
    
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }


    // Constructor
    public IRoadTrip(String[] args) {
        try {
            initializeFiles(args);
            readAndProcessData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String bordersFile;
    private String capdistFile;
    private String stateNameFile;


    // Initialization method for file paths
    private void initializeFiles(String[] args) {
        bordersFile = args[0];
        capdistFile = args[1];
        stateNameFile = args[2];
        fixDifficultCountryNames();
    }


    // Method to read and process data from files
    private void readAndProcessData() {
        borders = neighboringCountries(bordersFile);
        capitalDistances = distanceMap(capdistFile);
        stateNames = convertedCountries(stateNameFile);

        correctAllCountryDistances();
    }

    
    // Method to correct distances for all countries
    private void correctAllCountryDistances() {
        for (String key : borders.keySet()) {
            String newKey = fixedCountries.getOrDefault(key, key);
            updatedCountries.put(newKey, new HashMap<>());

            processCountryDistances(key, newKey);
        }
    }


    // Method to process distances for a specific country
    private void processCountryDistances(String key, String newKey) {
        if (stateNames.containsKey(key)) {
            String tempCountryId1 = stateNames.get(key);
            Map<String, Integer> temp = borders.get(key);

            for (String key2 : temp.keySet()) {
                String newKey2 = fixedCountries.getOrDefault(key2, key2);

                processCountryDistanceDetails(tempCountryId1, key2, newKey, newKey2);
            }
        }
    }


    // Method to process details of country distances
    private void processCountryDistanceDetails(String countryCode, String secondKey, String newKey, String newKey2) {
        if (stateNames.containsKey(secondKey)) {
            String tempCountryId2 = stateNames.get(secondKey);

            if (capitalDistances.containsKey(countryCode) && capitalDistances.get(countryCode).containsKey(tempCountryId2)) {
                int distance = capitalDistances.get(countryCode).get(tempCountryId2);
                updatedCountries.get(newKey).put(newKey2, distance);
            }
        }
    }


    // Method to get the distance between two countries
    public int getDistance(String country1, String country2) {
        String fixedCountry1 = fixedCountries.getOrDefault(country1, country1);
        String fixedCountry2 = fixedCountries.getOrDefault(country2, country2);
    
        return updatedCountries.getOrDefault(fixedCountry1, Collections.emptyMap()).getOrDefault(fixedCountry2, -1);
    }
    

    // Method to find the shortest path between two countries
    public List<String> findPath(String country1, String country2) {
        fixDifficultCountryNames();
        boolean isValidPath = true;
        List<String> pathResult = new ArrayList<>();
        Map<String, String> path = Dijkstra(country1, country2);
    
        country1 = fixedCountries.getOrDefault(country1, country1);
        country2 = fixedCountries.getOrDefault(country2, country2);
    
        String currentCountry = country2;
        List<String> reversedPath = new ArrayList<>();
    
        while (!currentCountry.equals(country1)) {
            if (path.get(currentCountry) == null) {
                isValidPath = false;
                break;
            }
    
            int distance = getDistance(path.get(currentCountry), currentCountry);
            String pathSegment = "* " + path.get(currentCountry) + " --> " + currentCountry + " (" + distance + " km.)";
            reversedPath.add(pathSegment);
            currentCountry = path.get(currentCountry);
        }
    
        String formattedPath = formatPath(reversedPath);
        Collections.addAll(pathResult, formattedPath.split("\n"));
    
        List<String> result;
        if (isValidPath) {
            result = pathResult;
        } else {
            result = Collections.emptyList();
        }

        return result;
    }
    

    // Method to format the path
    private String formatPath(List<String> reversedPath) {
        StringBuilder formattedPath = new StringBuilder();
        for (int i = reversedPath.size() - 1; i >= 0; i--) {
            formattedPath.append(reversedPath.get(i)).append("\n");
        }
        return formattedPath.toString();
    }

    
    // Dijkstra's algorithm for finding the shortest path
    private Map<String, String> Dijkstra(String startCountry, String endCountry) {
        String[] startEnd = {startCountry, endCountry};
        fixCountryNames(startEnd);
        PriorityQueue<Node> minHeap = new PriorityQueue<>();
        minHeap.add(new Node(startCountry, 0));
        Map<String, String> neighbors = new HashMap<>();
        neighbors.put(startCountry, "");
        Set<String> countries = new HashSet<>();
        Map<String, Integer> shortestDistance = new HashMap<>();
        shortestDistance.put(startCountry, 0);
    
        while (!minHeap.isEmpty() && !endCountry.equals(neighbors.keySet().iterator().next())) {
            Node currentTemp = removeProcessedNode(minHeap, countries);
    
            if (currentTemp == null) {
                break;
            }
    
            updateCurrentTempIfAlternateName(currentTemp);
    
            int currentTempDistance = currentTemp.distance;
            String currentTempName = currentTemp.country;
    
            countries.add(currentTemp.country);
    
            processNeighbors(neighbors, shortestDistance, minHeap, currentTempName, currentTempDistance);
        }
    
        return neighbors;
    }
    

    // Method to fix country names in an array
    private void fixCountryNames(String[] countryNames) {
        for (int i = 0; i < countryNames.length; i++) {
            if (fixedCountries.containsKey(countryNames[i])) {
                countryNames[i] = fixedCountries.get(countryNames[i]);
            }
        }
    }

    
    // Method to remove processed node from the priority queue
    private Node removeProcessedNode(PriorityQueue<Node> heap, Set<String> C) {
        Node currentTemp = heap.remove();
        while (!heap.isEmpty() && C.contains(currentTemp.country)) {
            currentTemp = heap.remove();
        }
        return currentTemp;
    }


    // Method to update current node if an alternate name exists
    private void updateCurrentTempIfAlternateName(Node currentTemp) {
        if (updatedCountries.get(currentTemp.country) == null && fixedCountries.containsKey(currentTemp.country)) {
            currentTemp = new Node(fixedCountries.get(currentTemp.country), currentTemp.distance);
        }
    }


    // Method to process neighbors in the graph
    private void processNeighbors(Map<String, String> neighbors, Map<String, Integer> shortestDistance, PriorityQueue<Node> heap, String currentTempName, int currentTempDistance) {
        Map<String, Integer> neighborsInfo = updatedCountries.get(currentTempName);

        if (neighborsInfo != null) {
            for (String neighbor : neighborsInfo.keySet()) {
                int nearbyDistance = neighborsInfo.get(neighbor);

                String fixedNeighbor = fixedCountries.getOrDefault(neighbor, neighbor);
                if (!shortestDistance.containsKey(fixedNeighbor) ||
                    shortestDistance.get(fixedNeighbor) > (nearbyDistance + currentTempDistance)) {
                    neighbors.put(fixedNeighbor, currentTempName);
                    shortestDistance.put(fixedNeighbor, nearbyDistance + currentTempDistance);
                    heap.add(new Node(fixedNeighbor, nearbyDistance + currentTempDistance));
                }
            }
        }
    }


    // Method to read neighboring countries from a file
    public Map<String, Map<String, Integer>> neighboringCountries(String bordersPath) {
        Map<String, Map<String, Integer>> processedBordersMap = new HashMap<>();
        try {
            File bordersFile = new File(bordersPath);
            FileReader fileReader = new FileReader(bordersFile);
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line, processedBordersMap);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return processedBordersMap;
    }
    

    // Method to process a line from the borders file
    private void processLine(String line, Map<String, Map<String, Integer>> processedBordersMap) {
        Map<String, Integer> innerMap = new HashMap<>();
        String[] value = line.split(" = ");
        
        if (value.length > 1) {
            String changedStr = value[1].substring(0, value[1].length() - 3);
            String[] namesDistances = changedStr.split(" km; ");
    
            for (String namesDistance : namesDistances) {
                String[] tempND = namesDistance.split(" ");
                String countryName = buildCountryName(tempND);
    
                innerMap.put(countryName, Integer.MAX_VALUE);
            }
        }
    
        processedBordersMap.put(value[0], innerMap);
    }
    

    // Method to build a country name from temporary strings
    private String buildCountryName(String[] tempStrings) {
        StringBuilder countryNameBuilder = new StringBuilder();
    
        if (tempStrings.length == 2) {
            countryNameBuilder.append(tempStrings[0]);
        } else {
            for (int i = 0; i < tempStrings.length - 2; i++) {
                countryNameBuilder.append(tempStrings[i]).append(" ");
            }
            countryNameBuilder.append(tempStrings[tempStrings.length - 2]);
        }
    
        return countryNameBuilder.toString();
    }
    
    
    // Method to read capital distances from a file
    public Map<String, Map<String, Integer>> distanceMap(String capdistPath) {
        Map<String, Map<String, Integer>> processedDistancesMap = new HashMap<>();

        try {
            File capdistFile = new File(capdistPath);
            FileReader fileReader = new FileReader(capdistFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            // Skip first line
            bufferedReader.readLine();
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] parts = line.split(",");

                if (parts.length >= 5) {
                    String country1 = parts[1];
                    String country2 = parts[3];
                    int distance = Integer.parseInt(parts[4]);

                    processedDistancesMap.computeIfAbsent(country1, k -> new HashMap<>()).put(country2, distance);
                }

                line = bufferedReader.readLine();
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return processedDistancesMap;
    }


    // Method to accept user input for country names
    public void acceptUserInput() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String country1 = promptForCountry("Enter the name of the first country (type EXIT to quit): ", scanner);
            if (country1.equals("EXIT")) {
                break;
            }

            String country2 = promptForCountry("Enter the name of the second country (type EXIT to quit): ", scanner);
            if (country2.equals("EXIT")) {
                break;
            }

            System.out.println("Route from " + country1 + " to " + country2 + ":");
            List<String> path = findPath(country1, country2);
            for (String step : path) {
                System.out.println(step);
            }
        }

        scanner.close();
    }


    // Method to prompt for a country name
    private String promptForCountry(String message, Scanner scanner) {
        while (true) {
            System.out.print(message);
            String userInput = scanner.nextLine();

            if (userInput.equals("EXIT")) {
                return "EXIT";
            }

            String fixedCountry = fixedCountries.getOrDefault(userInput, userInput);

            if (updatedCountries.containsKey(fixedCountry)) {
                return fixedCountry;
            } else {
                System.out.println("Invalid country name. Please enter a valid country name.");
            }
        }
    }

    
    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);
        a3.acceptUserInput();
    }
}