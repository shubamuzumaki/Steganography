import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.awt.image.*;
import javax.imageio.ImageIO;


class Steganographer {

    private static File dataDir = new File("data");

    static{
        if(!dataDir.exists())
            dataDir.mkdir();
    }

    public static long getEmbeddigCapacity(String vesselImagePath) throws Exception {
        BufferedImage srcImage = ImageIO.read(new File(vesselImagePath));
        long embeddingCapacity = srcImage.getWidth()*srcImage.getHeight() - 2*HeaderManager.HEADER_LENGTH;
        return embeddingCapacity;
    } 

    private static void embedFile(BufferedImage srcImage, File secretFile, String password, int startPosX, int startPosY) throws Exception{
        
        DataManager dMngr = new DataManager();
        SecurityManager sMngr = new SecurityManager(password);
        WritableRaster wRstr = srcImage.getRaster();
        FileInputStream secretFIS = new FileInputStream(secretFile);
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();

        boolean isEmbeddingComplete = false;
        int x=startPosX;
        for(int y = startPosY; y<height && !isEmbeddingComplete; ++y){
            for(; x<width; ++x){
                
                //extract band from pixel of image at (x,y)
                int []oldBand = new int[3];
                for(int i=0; i<3; ++i)
                    oldBand[i] = wRstr.getSample(x, y, i);
                
                int data = secretFIS.read();
                if(data == -1){
                    isEmbeddingComplete =true;
                    break;
                }
                data = sMngr.encryptData(data);

                //set modified bands back
                int []newBand = dMngr.mergeNext(data,oldBand);
                for(int i=0; i<3; ++i)
                    wRstr.setSample(x, y, i, newBand[i]);
                
            }//inner
            x=0;
        }//outer

        secretFIS.close();
    }

    private static File extractFile(BufferedImage srcImage, HeaderManager header, String password) throws Exception{
        File outputFile = new File(dataDir, "EXTRACTED_" + header.getName());
        DataManager dMngr = new DataManager();
        SecurityManager sMngr = new SecurityManager(password);
        WritableRaster wRstr = srcImage.getRaster();
        FileOutputStream outputFOS = new FileOutputStream(outputFile);
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();

        boolean isExtractionComplete = false;
        long extractInd = 0;

        int x=header.getPosX();
        for(int y = header.getPosY(); y<height && !isExtractionComplete; ++y){
            for(; x<width; ++x){

                //extract band from pixel of image at (x,y)
                int []band = new int[3];
                for(int i=0; i<3; ++i)
                    band[i] = wRstr.getSample(x, y, i);
                
                int data = dMngr.splitNext(band);
                data = sMngr.decryptData(data);

                outputFOS.write(data);

                if(++extractInd == header.getLength()){
                    isExtractionComplete = true;
                    break;
                }
                
            }//inner
            x=0;
        }//outer

        outputFOS.close();
        return outputFile;
    }

    private static void embedHeader(BufferedImage srcImage, String header, int startPos, String password){
        
        DataManager dMngr = new DataManager();
        SecurityManager sMngr = new SecurityManager(password);
        WritableRaster wRstr = srcImage.getRaster();
        int width = srcImage.getWidth();

        for(int i=startPos; i < HeaderManager.HEADER_LENGTH+startPos; ++i){
            int x = i%width;
            int y = i/width;

            //Extract Band
            int []oldBand = new int[3];
            for(int j=0; j<3; ++j)
                oldBand[j] = wRstr.getSample(x, y, j);
            
            //encrypt data
            int data = sMngr.encryptData(header.charAt(i-startPos));

            //set modified bands back
            int []newBand = dMngr.mergeNext(data,oldBand);
            for(int j=0; j<3; ++j)
                wRstr.setSample(x, y, j, newBand[j]);

        }
    }

    private static String extractHeader(BufferedImage srcImage, int startPos, String password){
        DataManager dMngr = new DataManager();
        SecurityManager sMngr = new SecurityManager(password);
        WritableRaster wRstr = srcImage.getRaster();
        int width = srcImage.getWidth();
        StringBuilder header = new StringBuilder();
        
        for(int i=startPos; i < HeaderManager.HEADER_LENGTH+startPos; ++i){
            int x = i%width;
            int y = i/width;

            //Extract Band
            int []band = new int[3];
            for(int j=0; j<3; ++j)
                band[j] = wRstr.getSample(x, y, j);

            //encrypt data
            int data = dMngr.splitNext(band);
            data = sMngr.decryptData(data);
            header.append((char)data);
            
        }

        return header.toString();
    }

    public static File basicEmbed(String vesselImagePath, String secretFilePath, String password) throws Exception {
        

        File vesselFile = new File(vesselImagePath);
        File secretFile = new File(secretFilePath);        
        File outputFile = new File(dataDir, "doctored_image.png");
        
        BufferedImage srcImage = ImageIO.read(vesselFile);

        //calculate embedding capacity
        long embeddingCapacity = getEmbeddigCapacity(vesselImagePath);
        if(secretFile.length() > embeddingCapacity)
            throw new Exception("File too Long");
        
        
        //Header
        int startPosX = (2*HeaderManager.HEADER_LENGTH) % srcImage.getWidth();
        int startPosY = (2*HeaderManager.HEADER_LENGTH) / srcImage.getWidth();
        String header = HeaderManager.generateHeader(secretFile, startPosX, startPosY);
        embedHeader(srcImage, header, 0, password);
        embedFile(srcImage, secretFile, password, startPosX, startPosY);
        
        ImageIO.write(srcImage, "png", outputFile);

        return outputFile;
    }

    public static File proEmbed(String vesselImagePath, String secretFile1Path, String password1, String secretFile2Path, String password2) throws Exception{
    
        File vesselFile = new File(vesselImagePath);
        File secretFile1 = new File(secretFile1Path);        
        File secretFile2 = new File(secretFile2Path);        
        File outputFile = new File(dataDir, "doctored_image.png");
        
        BufferedImage srcImage = ImageIO.read(vesselFile);

        //calculate embedding capacity
        long embeddingCapacity = getEmbeddigCapacity(vesselImagePath);
        if(secretFile1.length()+secretFile2.length() > embeddingCapacity)
            throw new Exception("Files too Long");
        
        
        //Header
        int startPosX1 = (2*HeaderManager.HEADER_LENGTH) % srcImage.getWidth();
        int startPosY1 = (2*HeaderManager.HEADER_LENGTH) / srcImage.getWidth();
        String header1 = HeaderManager.generateHeader(secretFile1, startPosX1, startPosY1);

        int startPosX2 = (2*HeaderManager.HEADER_LENGTH + (int)secretFile1.length()) % srcImage.getWidth();
        int startPosY2 = (2*HeaderManager.HEADER_LENGTH + (int)secretFile1.length()) / srcImage.getWidth();
        String header2 = HeaderManager.generateHeader(secretFile2, startPosX2, startPosY2);
        

        embedHeader(srcImage, header1, 0, password1);
        embedFile(srcImage, secretFile1, password1, startPosX1, startPosY1);

        embedHeader(srcImage, header2, HeaderManager.HEADER_LENGTH, password2);
        embedFile(srcImage, secretFile2, password2, startPosX2, startPosY2);
        
        ImageIO.write(srcImage, "png", outputFile);

        return outputFile; 
    }

    public static File extract(String doctoredImagePath, String password) throws Exception{
        File vesselFile = new File(doctoredImagePath);

        BufferedImage srcImage = ImageIO.read(vesselFile);   

        HeaderManager header = new HeaderManager(extractHeader(srcImage, 0, password));
        if(!header.isValid()){
            header = new HeaderManager(extractHeader(srcImage, HeaderManager.HEADER_LENGTH, password));
        }

        if(!header.isValid())
            throw new Exception("This Image have no secrets to hide");
        
        return extractFile(srcImage, header, password);
    }
}