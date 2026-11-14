import java.io.*;
import java.util.*;

public class PasswordStrengthChecker {

//Loading dictionary words

static List<String>
  loadDictionary(String path) throws
  IOException {
    List<String> words = new ArrayList<>();
    BufferedReader reader = new
      BufferedReader(new FileReader(path));
    String line; 
  while ((line = reader.readLine()) != null) {
    line = line.trim();
    if (!line.isEmpty()) words.add(line);
  }
    reader.close();
    return words;
  }

// Weak, Moderate, Strong password 
static String evaluateStrength(String password, List<String> words) {
  // Weak password: password is a dictionary word 
  for (String word : words) {
    if (password.equals(word)) {
      return "Weak";
    }
  }
  // Moderate password
  for (String word : words) {
    if (password.contains(word)) {
      return "Moderate";
    }
  }
  // Strong Password 
  return "Strong";
}
  // Ask for password to evaluate 
  public static void main(String[] args) throws
  Exception {
    Scanner in = new Scanner(System.in);
    String dictionaryFile = "dictionary.txt";

  System.out.print("Enter a password to evaluate: ");
    String password = in.nextLine().trim();

  List<String> words = 
    loadDictionary)dictionaryFile);
    System.out.println("Loaded " + words.size() + "dictionary words.");

  String result = evaluateStrength(password, words);
    System.out.println("Password strength: " + result);
  }
}
