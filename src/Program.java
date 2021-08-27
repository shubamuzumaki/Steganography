import java.util.Scanner;
import java.io.File;

class Program
{
    static Scanner scan;
    public static void main(String[] args) 
    {
        scan = new Scanner(System.in);
        System.out.println("1.Embedd");
        System.out.println("2.Extract");
        System.out.println("3.Exit");
        
        // int ch = 3;
        int ch = scan.nextInt();
        switch (ch) 
        {
            case 1:
                embed();
                break;
            case 2:
                extract();
                break;
            case 3:
                break;
            default:
                System.out.println("Invaid Entry");
                break;
        }
    }

    static void embed()
    {
        try 
        {
            String vesselFile = "violet.png";
            String secretFile = "secret_ciri.jpg";
            String password = "password";
            String outputFile = "output.png";

            System.out.print("Vessel File: ");
            // vesselFile = scan.next();
            System.out.print("Secret File: ");
            // secretFile = scan.next();
            System.out.print("Password:    ");
            // password = scan.next();
            System.out.print("Output File: ");
            // outputFile = scan.next();

            Steganographer.embed(new File(vesselFile), new File(secretFile), password, new File(outputFile));
            System.out.println("\nFile Embedded Successfully");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    static void extract()
    {
        try 
        {
            String vesselFile = "output.png";
            String password = "password";

            System.out.print("Vessel File: ");
            // vesselFile = scan.next();
            System.out.print("Password:    ");
            // password = scan.next();

            Steganographer.extract(new File(vesselFile), password);

            System.out.println("\nFile Extracted Successfully");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}