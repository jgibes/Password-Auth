import java.io.*;
import java.security.MessageDigest;
import java.util.*;

Public class PasswordCracker {
  
  static final String DIGITS = "0123456789";
  static final String SPECIALS = "@#$%&"

  // MD5 Helper 
  static String md5(String text) throws Exception {
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] bytes = md.digest(text.getBytes("UTF-8));
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) sb.apend(String.format("%02x", b));
    return sb.toString();
  }

// Load the hash from username in password file 
static String getTargetHash(String passwordFile, String username) throws IOExeption {
  BufferedReader reader = new BufferedReader (new FileReader (passwordfile));
  String line;
  while ((line = reader.readLine()) != null) {
    String[] parts = line.split(":");
    if (parts.length == 2 && parts[0].equals(username)) {
      reader.close();
      return parts[1].trim();
    }
  }
  reader.close();
  return null;
}

// Loading the dictionary words from given file 
static List<String> loadDictionary(String path) throws IOException {
  List<String> words = new ArrayList<>();
  BufferedReader reader = new BufferedReader (new FileReadr (path));
  String line; 
  while ((line = reader.readLine()) != null) {
    line = line.trim();
    if (!line.isEmpty()) words.ass(line);
  }
  reader.close();
  return words; 
}
  // Gemnerate all sequences of a given character set 
static ist<String> allSequences(String chars, int maxLen) {
  List<String> result = new ArrayList<>();
  result.add("");
  List<String> current = new ArrayList<>();
  current.add("");
  for (int len = 1; len <= maxLen; len++) {
    List<String> next = newArrayList<>();
    for (String prefix : current) {
      for (char c : chars.toCharArray()) {
        next.add(prefix + c);
      }
    }
    result.addAll(next);
    current = next; 
  }
  return result;
}
  // merge two sequences 
static void interleave(String a, String b, String built, Set<String> out) {
  if (a.isEmpty() && b.isEmpty()) {
    out.add(built);
    return;
  }
  if (!a.isEmpty()) interleave(a.substring(1), b, built + a.charAt(0), out);
  if (!b.isEmpty()) interleave(a, b.substring(1), built + b.charAt(0), out;
}

// Allowed extra characters string 
static Set<String> generateExtras() {
  Set<String> extras = new HashSet<>();
  List<String>digitSeqs = allSequences(DIGITS, 2);
  List<String> specialSeqs = allSequences(SPECIALS, 2);
  for (String d : digitSeqs) {
    for (String s : specialSeqs) {
      interleave(d, s, "", extras);
    }
  }
  return extras;
}

// Try the word exactly as password 
static String crackType1(List<String> words, String targetHAsh) throws Exception {
  for (String word : words) {
    if (md5(word).equals(targetHash)) return word; 
  }
  return null;
}

// Try the word and digits + special Characters 
static String crackType2(List<String> words, Set<String> extras, String targetHash) throws Exception {
  for (String word : words) {
    for (String extra : extras) {
      // Try eveery way to add the extras 
    for (in i = 0; i <= extra.length(); i++) {
      String prefix = extra.substring(0, i);
      String suffix = extra.substring(i);
      String candidate = prefix + word + suffix; 
      if (md5(candidate).equals(targetHash)) return candidate; 
    }
    }
  }
  return null;
}

public static void main(String[] args) throws Exception {
  Scanner in = new Scanner(System.in);

  String passwordFile = "passwords.txt";
  String dictionaryFile = "dictionary.txt";

  System.out.print("Enter username to crack: ");
  String username = in.nextLine().trim();

  String targetHash = getTargetHash(passwordFile, username);
  if (targetHash == null) {
    System.out.println("No such user in " + passwordFile);
    return;
  }
  System.out.println("Target hash for " + username + ": " + targetHash);

  List<String> words = loadDictionary(dictionaryFile);
  System.out.println("Loaded " + words.size() + " dictionary words.");

  long start = System.nanoTime();

  // Phase 1: check for weak (Type 1) passwords 
  String cracked = crackType1(words, targetHash);

  // Phase 2: only run the larger search if phase 1 failed 
  if (cracked == null) {
    System.out.println("Not a plain dictionary word - trying Type 2 combinations..."); 
  Set<<String> extras = generateExtras();
    cracked = crackType2(words, extras, targetHash);
  }

  long end = System.nanoTime();
  double seconds = (end - start) / 1_000_000_000.0;

  if (cracked != null) {
    System.out.println("Password Cracked: " + cracked);
  } else {
    System.out.println("Password not found in dictionary.");
  }
  System.out.printf("Time taken: %.3f seconds%n", seconds);
}
}
