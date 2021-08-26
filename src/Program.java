import java.util.Scanner;
import java.io.File;
import java.text.NumberFormat;

class Program
{
    static Scanner scan = new Scanner(System.in);
    static final String basicMethodHelp = "placeholder help about how basic method works";
    static final String proMethodHelp = "placeholder help about how pro method works";

    public static void main(String[] args) 
    {
        System.out.println("**********[ STEGANOGRAPHY ]**********");
        System.out.println("1.Basic Hide");
        System.out.println("2.Pro Hide");
        System.out.println("3.Extract");

        int ch = scan.nextInt();
        if(ch == 1)
            basicHide();
        else if(ch == 2)
            proHide();
        else if(ch == 3)
            extract();
        else
            System.out.println("Wrong Choice.");

    }

    private static void basicHide(){
        
        try{

            System.out.println("\n**********[ BASIC Hide ]**********");
            System.out.println(basicMethodHelp);
            System.out.println();

            String vesselImagePath;
            String secretFilePath;
            String password;

            System.out.print("Path to vessel image: ");
            vesselImagePath = scan.next();

            vesselImagePath = "/home/sa1n7/Documents/Acad/Steganography/data/violet.png";
            
            long embeddingCapacity = Steganographer.getEmbeddigCapacity(vesselImagePath);
            System.out.println("You can hide around "+ byteToKB(embeddingCapacity) + " KB in this image");

            
            System.out.print("Path to secret file : ");
            secretFilePath = scan.next();

            System.out.print("Password            : ");
            password = scan.next();

            //Testing purpose only
            secretFilePath = "/home/sa1n7/Documents/Acad/Steganography/data/secret.txt";
            password = "1234";


            System.out.println("Hiding your secrets...");

            File outputFile = Steganographer.embed(vesselImagePath, secretFilePath, password);
            System.out.println("Secrets hidden successfully.");
            System.out.print("Output Image Path: " + outputFile.getAbsolutePath());

        }catch(Exception e){
            System.out.println("Failed to hide your secrets.");
            e.printStackTrace();
        }



    }

    private static void proHide(){

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

            //Testing purpose only
            doctoredImagePath = "/home/sa1n7/Documents/Acad/Steganography/data/Doctored_Image";
            password = "1234";

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