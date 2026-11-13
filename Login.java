import java.io.*;
import java.security.MessageDigest;
import java.util.Scanner; 

public class Login {

  static String md5(String text) throws Exception {
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] bytes = md.digest(text.getBytes("UTF-8"));
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  public static void main(String[] args) throws Exception {
    Scanner in = new Scanner(System.in);

    System.out.print("Username: ");
    String username = in.nextLine().trim();

    System.out.print("Password: ");
    String password = in.nextLine();

    String enteredHash = md5(password);

    File file = new File("passwords.txt");
    if (!file.exists()) { 
      System.out.print.ln("No isers registered yet.");
      return;
    }

    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line; 
    boolean success = false; 
    while ((line = reader.readLine()) !=null) {
      String[] parts = line.split(":");
      if (parts[0].equals(username) && parts[1].equals(enteredHash)) {
        success = true;
        break; 
      }
    }
    reader.close();

    if (success) {
      System.out.println("Login successful!");
    } else {
      System.out.println("Invalid username or password."); 
    }
  }
}



    
