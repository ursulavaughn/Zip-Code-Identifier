import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class ZipCodeIdentifier {
  public static void main(String[] args) {
    // Initialize scanner to read user input
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter a zip code: ");
    String zipCode = scanner.nextLine();

    try {
      // Create a URL object with the provided zip code and open an HTTP connection
      URL url = new URL("https://api.zippopotam.us/us/" + zipCode);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");

      // Check if the HTTP response code is not 200 (OK)
      if (conn.getResponseCode() != 200) {
        throw new RuntimeException("HTTP error code : " + conn.getResponseCode());
      }

      // Read the response from the API and parse it as a JSON object
      Scanner apiScanner = new Scanner(conn.getInputStream());
      StringBuilder apiResponse = new StringBuilder();
      while (apiScanner.hasNextLine()) {
        apiResponse.append(apiScanner.nextLine());
      }
      apiScanner.close();
      JSONObject json = new JSONObject(apiResponse.toString());

      // Extract the "places" array from the JSON object
      JSONArray places = json.getJSONArray("places");

      // Generate a random index within the "places" array and extract the corresponding place object
      int randomIndex = (int)(Math.random() * places.length());
      JSONObject place = places.getJSONObject(randomIndex);

      // Extract the city and state information from the place object
      String city = place.optString("place name", "");
      String state = place.optString("state", "");

      // Print out the identified city and state
      System.out.println(city + ", " + state);
    } catch (Exception e) {
      // If an error occurs, print out the error message
      System.out.println("Error: " + e.getMessage());
    }

    // Close the scanner
    scanner.close();
  }
}