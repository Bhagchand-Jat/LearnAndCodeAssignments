package CountryFind;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CountryNameBasedOnCountryCode {

  public static Map<String, String> countryMap = new HashMap<>();
  public final static String ContryFilePath = "Country.json";

  public static void main(String[] args) {

    addCountriesToMap();
    try (Scanner scanner = new Scanner(System.in)) {

      System.out.print("Enter Country Code or Q to Exit: ");
      String countryCode = scanner.nextLine();
      while (!countryCode.toLowerCase().equals("q")) {

        System.out.println(findCountryName(countryCode));
        System.out.print("Enter Country Code or Q to Exit: ");
        countryCode = scanner.nextLine();
      }

    }

  }

  public static void addCountriesToMap() {
    String jsonContent = readCountrysDataFromJsonFile();
    convertJsonToMapAndSave(jsonContent);
  }

  public static String readCountrysDataFromJsonFile() {

    StringBuilder jsonContent = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(ContryFilePath))) {

      String line;
      while ((line = reader.readLine()) != null) {

        jsonContent.append(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return jsonContent.toString();

  }

  public static void convertJsonToMapAndSave(String jsonContent) {

    String[] countries = jsonContent.replace("[", "").replace("]", "").split("},");
    for (String country : countries) {
      String[] countryParts = country.trim().replace("{", "").replace("}", "").split(", ");
      String[] countryCodePart=countryParts[0].split(":");
      String[] countryNamePart=countryParts[1].split(":");
      String countryCode = countryCodePart[1].trim().replace("\"", "");
      String countryName = countryNamePart[1].trim().replace("\"", "");
      countryMap.put(countryCode, countryName);

    }

  }

  public static String findCountryName(String countryCode) {
    if (countryMap.containsKey(countryCode)) {
      return countryMap.get(countryCode);
    } else {
      return "Country With " + countryCode + " Code not Found.";
    }
  }

}
