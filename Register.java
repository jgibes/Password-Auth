import java.io.*;
import java.security.MessageDigest;
import java.util.Scanner;

public class Register {

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

        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }

        System.out.print("Enter password: ");
        String password = in.nextLine();

        File file = new File("passwords.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length > 0 && parts[0].equals(username)) {
                        System.out.println("Username already exists.");
                        return;
                    }
                }
            }
        }

        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(username + ":" + md5(password) + "\n");
        }

        System.out.println("User registered successfully.");
    }
}
