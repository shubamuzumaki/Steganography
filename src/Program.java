import java.util.Scanner;
import java.io.File;
import java.text.NumberFormat;

class Program
{
    static Scanner scan = new Scanner(System.in);
    static final String basicMethodHelp = "placeholder help about how basic mode works";
    static final String proMethodHelp = "placeholder help about how pro mode works";

    //Testing purpose only
    static final String vesselImagePathTest = "/home/s41n7/Documents/ACAD/Steganography/data/violet.png";
    static final String secretFile1PathTest = "/home/s41n7/Documents/ACAD/Steganography/data/secret.txt";
    static final String secretFile2PathTest = "/home/s41n7/Documents/ACAD/Steganography/data/super_secret.txt";
    static final String doctoredImagePathTest = "/home/s41n7/Documents/ACAD/Steganography/data/Doctored_Image.png";
    static final String password1Test = "123";
    static final String password2Test = "1234";

    public static void main(String[] args) 
    {
        System.out.println("**********[ STEGANOGRAPHY ]**********");
        System.out.println("1.Basic Mode");
        System.out.println("2.Pro Mode");
        System.out.println("3.Extract");

        int ch = scan.nextInt();
        if(ch == 1)
            basicMode();
        else if(ch == 2)
            proMode();
        else if(ch == 3)
            extract();
        else
            System.out.println("Wrong Choice.");

    }

    private static void basicMode(){
        
        try{

            System.out.println("\n**********[ BASIC Hide ]**********");
            System.out.println(basicMethodHelp);
            System.out.println();

            String vesselImagePath;
            String secretFilePath;
            String password;

            System.out.print("Path to vessel image: ");
            vesselImagePath = scan.next();

            //TODO: Testing
            vesselImagePath = vesselImagePathTest;
            
            long embeddingCapacity = Steganographer.getEmbeddigCapacity(vesselImagePath);
            System.out.println("You can hide around "+ byteToKB(embeddingCapacity) + " KB in this image");

            
            System.out.print("Path to secret file : ");
            secretFilePath = scan.next();

            System.out.print("Password            : ");
            password = scan.next();

            //TODO: Testing
            secretFilePath = secretFile1PathTest;
            password = password1Test;


            System.out.println("Hiding your secrets...");

            File outputFile = Steganographer.embed(vesselImagePath, secretFilePath, password);
            System.out.println("Secrets hidden successfully.");
            System.out.println("Output Image Path: " + outputFile.getAbsolutePath());

        }catch(Exception e){
            System.out.println("Failed to hide your secrets.");
            e.printStackTrace();
        }



    }

    private static void proMode(){

    }

    private static void extract(){
        try{
            System.out.println("\n**********[ EXTRACT ]**********");

            String doctoredImagePath;
            String password;

            System.out.print("Path to vessel image: ");
            doctoredImagePath = scan.next();

            System.out.print("Password            : ");
            password = scan.next();

            //TODO: Testing
            doctoredImagePath = doctoredImagePathTest;
            password = password1Test;

            System.out.println("Extracting your secrets...");

            File secretFile = Steganographer.extract(doctoredImagePath, password);

            System.out.println("Secrets Extracted successfully.");
            System.out.println("Extracted Secret File Path: " + secretFile.getAbsolutePath());

        }catch(Exception e){
            System.out.println("Failed to extract data");
            e.printStackTrace();
        }

    }

    private static String byteToKB(long size){
        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);
        return myFormat.format(size/1024);
    }
   
}