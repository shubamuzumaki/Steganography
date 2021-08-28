import java.io.File;


class HeaderManager
{
    public static final int HEADER_LENGTH = 50;
    static final char SEPARATOR = '~';
    static final char PADDING_CHAR = '#';
    static final int NAME_LENGTH = 20;
    static final int SIZE_LENGTH;
    static final int POS_LENGTH = 5;
    static final String VALIDATOR = "VALID";


    static{
        SIZE_LENGTH = HEADER_LENGTH - NAME_LENGTH - 2*POS_LENGTH - VALIDATOR.length() - 4;
    }

    private static String pad(String str, int length){
        StringBuilder s = new StringBuilder(str);
        while (s.length() != length) 
            s.append(PADDING_CHAR);
        return s.toString();
    }

    public static String generateHeader(File file, int posX, int posY){
        
        String name = file.getName().replaceAll(String.valueOf(SEPARATOR), "");
        if(name.length() < NAME_LENGTH)//padding
            name = pad(name, NAME_LENGTH);
        else if(name.length() > NAME_LENGTH)//trimming
        {
            String extension = name.substring(name.lastIndexOf("."));
            name = name.substring(0, NAME_LENGTH-extension.length());
            name += extension;
        }
        
        String fileLength = pad(String.valueOf(file.length()), SIZE_LENGTH);
        String x = pad(String.valueOf(posX), POS_LENGTH);
        String y = pad(String.valueOf(posY), POS_LENGTH);


        StringBuilder header = new StringBuilder()
        .append(VALIDATOR)
        .append(SEPARATOR)
        .append(name)
        .append(SEPARATOR)
        .append(fileLength)
        .append(SEPARATOR)
        .append(x)
        .append(SEPARATOR)
        .append(y);

        return header.toString();
    }

    //------------------------------

    private boolean isValid = false;
    private String name = "";
    private long length = -1L;
    private int posX = -1;
    private int posY = -1;

    //VALID~violet.png##########~16033496###~2####~4####
    public HeaderManager(String header) {
        String[] arr = header.split("~");
        isValid = VALIDATOR.equals(arr[0]);

        if(isValid){
            name = arr[1].replaceAll(String.valueOf(PADDING_CHAR), "");
            length = Long.parseLong(arr[2].replaceAll(String.valueOf(PADDING_CHAR), ""));
            posX = Integer.parseInt(arr[3].replaceAll(String.valueOf(PADDING_CHAR), ""));
            posY = Integer.parseInt(arr[4].replaceAll(String.valueOf(PADDING_CHAR), ""));
        }
    }

    public boolean isValid(){
        return isValid;
    }

    public String getName(){
        return name;
    }
    public long getLength(){
        return length;
    }
    public int getPosX(){
        return posX;
    }
    public int getPosY(){
        return posY;
    }
}