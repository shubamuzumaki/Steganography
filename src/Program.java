import java.util.Scanner;
import java.io.File;
import java.text.NumberFormat;

class Program
{
    static Scanner scan = new Scanner(System.in);
    static final String basicMethodHelp = "placeholder help about how basic mode works";
    static final String proMethodHelp = "placeholder help about how pro mode works";

    //Testing purpose only
    static final String vesselImagePathTest = "data/vessel.jpg";
    static final String secretFile1PathTest = "data/secret.jpg";
    static final String secretFile2PathTest = "data/top_secret.jpg";
    static final String doctoredImagePathTest = "data/doctored_image.png";
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

            System.out.println("\n**********[ BASIC Mode ]**********");
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

            
            System.out.print("\nPath to secret file : ");
            secretFilePath = scan.next();

            System.out.print("Password            : ");
            password = scan.next();

            //TODO: Testing
            // /*
            secretFilePath = secretFile1PathTest;
            password = password1Test;
            // */ 

            System.out.println("Hiding your secrets...");

            File outputFile = Steganographer.basicEmbed(vesselImagePath, secretFilePath, password);
            System.out.println("Secrets hidden successfully.");
            System.out.println("Output Image Path: " + outputFile.getAbsolutePath());

        }catch(Exception e){
            System.out.println("Failed to hide your secrets.");
            e.printStackTrace();
        }
    }

    private static void proMode(){
        try{
            System.out.println("\n**********[ PRO Mode ]**********");
            System.out.println(proMethodHelp);
            System.out.println();

            String vesselImagePath;
            String secretFile1Path;
            String secretFile2Path;

            String password1;
            String password2;

            System.out.print("Path to vessel image          : ");
            vesselImagePath = scan.next();

            //TODO: Testing
            vesselImagePath = vesselImagePathTest;
            
            long embeddingCapacity = Steganographer.getEmbeddigCapacity(vesselImagePath);
            System.out.println("You can hide around "+ byteToKB(embeddingCapacity) + " KB in this image");

            
            System.out.print("\nPath to less sensitive file   : ");
            secretFile1Path = scan.next();

            System.out.print("Password                      : ");
            password1 = scan.next();

            System.out.print("\nPath to not super secret file : ");
            secretFile1Path = scan.next();

            do{
                System.out.print("Password                      : ");
                password2 = scan.next();

                if(password1.equals(password2)){
                    System.out.println("You need to pick different password for both files.");
                    System.out.println("NOTE: During Extraction file will be extracted based on the password supplied.");
                    System.out.println();
                }

            }while(password1.equals(password2));


            //TODO: Testing
            // /*
            secretFile1Path = secretFile1PathTest;
            password1 = password1Test;
            secretFile2Path = secretFile2PathTest;
            password2 = password2Test;
            // */

            System.out.println("Hiding your secrets...");

            File outputFile = Steganographer.proEmbed(vesselImagePath, secretFile1Path, password1, secretFile2Path, password2);
            System.out.println("Secrets hidden successfully.");
            System.out.println("Output Image Path: " + outputFile.getAbsolutePath());
        }catch(Exception e){
            System.out.println("Failed to hide your secrets.");
            e.printStackTrace();
        }

    }

    private static void extract(){
        try{
            System.out.println("\n**********[ EXTRACT ]**********");

            String doctoredImagePath;
            String password;

            System.out.print("Path to doctored image: ");
            doctoredImagePath = scan.next();

            System.out.print("Password              : ");
            password = scan.next();

            //TODO: Testing
            doctoredImagePath = doctoredImagePathTest;
            
            // password = password1Test;
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