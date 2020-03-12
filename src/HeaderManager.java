import java.io.File;

class HeaderManager
{
    public static final int HEADER_LENGTH = 25;
    static final int NAME_LENGTH = 12;
    static final char SEPARATOR = '~';
    static final int SIZE_LENGTH;
    static final char PADDING_CHAR = '#';

    static
    {
        SIZE_LENGTH = HEADER_LENGTH - NAME_LENGTH - 1;
    }

    public static String getHeader(File file)
    {
        String name = file.getName();
        String sizeOfFile = String.valueOf(file.length());

        if(name.length() < NAME_LENGTH)//padding
        {
            while (name.length() != NAME_LENGTH) 
                name += PADDING_CHAR;
        }
        else if(name.length() > NAME_LENGTH)//trimming
        {
            String extension = name.substring(name.lastIndexOf("."));
            name = name.substring(0, NAME_LENGTH-extension.length());
            name += extension;
        }

        while (sizeOfFile.length() < SIZE_LENGTH )
            sizeOfFile += PADDING_CHAR;

        return name + SEPARATOR + sizeOfFile;
    }

    public static String getFileName(String header)
    {
        return header.split("~")[0].replaceAll("#", "");
    }

    public static long getFileLength(String header)
    {
        return Long.parseLong(header.split("~")[1].replaceAll("#", ""));
    }
}