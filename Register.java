import java.io.*;
import java.security.MessageDigest;
import java.util.Scanner; 

public class Register {

  //Turns a password into an MD5 hash wrritten in hex 
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

    System.out.print("Enter new username: "); 
    String username = in.nextLine().trim();

    System.out.print("Enter password: ");
    String password = in.nextLine();

    // Check if the username already exists 
    File file = new File("passwords.txt");
    if (file.exists()) {
      BufferedReader reader = new BufferedReader (new FileReader(file)); 
      String line; 
      while ((line = reader.readLine()) != null) {
        if (line.split(":")[0].equals(username)) {
          System.out.println("Username already exists.");
          reader.close();
          return;
        }
      }
      reader.close();
    }

    // Append username: hash to the file 
    FileWriter writer = new FileWriter(file, true); // true = append 
    writer.write(username + ":" + md5(password) + "\n");
    writer.close();

    System.out.println("USer registered successfully."); 
  }
}
