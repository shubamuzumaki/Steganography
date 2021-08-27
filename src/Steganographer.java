import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.awt.image.*;
import javax.imageio.ImageIO;


class Steganographer
{
    public static void embed(File vesselFile, File secretFile, String password, File outputFile) throws Exception
    {
        BufferedImage srcImage = ImageIO.read(vesselFile);
        
        //calculate embedding capacity
        long embeddingCapacity = srcImage.getWidth()*srcImage.getHeight() - HeaderManager.HEADER_LENGTH;
        if(secretFile.length() > embeddingCapacity)
            throw new Exception("File too Long");

        //initialize vars
        FileInputStream toBeEmbed = new FileInputStream(secretFile);
        SecurityManager sMngr = new SecurityManager(password);
        DataManager dMngr = new DataManager();
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        WritableRaster wRstr = srcImage.getRaster();
        String header = HeaderManager.getHeader(secretFile);
        int embedInd = 0;
        boolean isEmbeddingComplete = false;

        //loop image data
        for(int y = 0; y < height && !isEmbeddingComplete; ++y)
        {
            for(int x=0; x < width; ++x)
            {
                //extract band from pixel of image at (x,y)
                int []oldBand = new int[3];
                for(int i=0; i<3; ++i)
                    oldBand[i] = wRstr.getSample(x, y, i);
                
                //extract data from header or file
                int data;
                if(embedInd < HeaderManager.HEADER_LENGTH)
                    data = header.charAt(embedInd);
                else
                {
                    data =  toBeEmbed.read();
                    if(data == -1)
                    {
                        isEmbeddingComplete = true;
                        break;
                    }
                }

                //encrypt data
                data = sMngr.encryptData(data);

                //set modified bands back
                int []newBand = dMngr.mergeNext(data,oldBand);
                for(int i=0; i<3; ++i)
                    wRstr.setSample(x, y, i, newBand[i]);
                
                embedInd++;
            
            }//inner for
        }//outer for

        System.out.println("@completed");
        //!CAREFUL HERE ".png" doesn't work and give no error
        ImageIO.write(srcImage, "png", outputFile);
        toBeEmbed.close();
    }//embed

    public static void extract(File vesselFile, String password) throws Exception
    {
        BufferedImage srcImage = ImageIO.read(vesselFile);

        //initialize vars
        FileOutputStream fout = null;
        SecurityManager sMngr = new SecurityManager(password);
        DataManager dMngr = new DataManager();
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        WritableRaster wRstr = srcImage.getRaster();
        int embedInd = 0;
        boolean isExtractionComplete = false;
        StringBuilder header = new StringBuilder();
        long hiddenDataLength = Long.MAX_VALUE;
        //loop image data
        for(int y = 0; y < height && !isExtractionComplete; ++y)
        {
            for(int x=0; x < width; ++x)
            {
                //extract band from pixel of image at (x,y)
                int []band = new int[3];
                for(int i=0; i<3; ++i)
                    band[i] = wRstr.getSample(x, y, i);
                
                //extract data from header or file
                int data = dMngr.splitNext(band);
                data = sMngr.decryptData(data);

                if(embedInd < HeaderManager.HEADER_LENGTH)
                {
                    //!remenber to cast it to char first
                    header.append((char)data);
                    if(embedInd == HeaderManager.HEADER_LENGTH-1)//header extraction completes
                    {
                        System.out.println(header);
                        fout = new FileOutputStream("Extracted_" + HeaderManager.getFileName(header.toString()));
                        hiddenDataLength = HeaderManager.getFileLength(header.toString()) + HeaderManager.HEADER_LENGTH;
                    }            
                }   
                else
                {
                    if(embedInd == hiddenDataLength)
                    {
                        isExtractionComplete = true;
                        break;
                    }
                    fout.write(data);
                }

                embedInd++;
            }//inner for
        }//outer for

        System.out.println("@completed");
        fout.close();
    }
}