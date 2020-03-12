/**
 * SecurityManager
 */
public class SecurityManager 
{
    String key;

    int ind = 0;
    int keyLength;
    public SecurityManager(String key)
    {
        this.key = key;
        this.keyLength = key.length();
    }
    
    public int encryptData(int data)
    {
        int encData = data ^ (int)key.charAt(ind);
        ind = (ind+1)%keyLength;
        return encData;
    }

    public int decryptData(int encryptedData)
    {
        int data = encryptedData ^ (int)key.charAt(ind);
        ind = (ind+1)%keyLength;
        return data;
    }
}