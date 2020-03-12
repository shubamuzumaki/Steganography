class DataManager
{
    int flag = 0;
    final int MAX_FLAG = 3;
    // flag 1: 11 111 111
    // flag 2: 111 11 111
    // flag 3: 111 111 11

    public int[] mergeNext(int data, int[] oldBand)
    {
        //split the data based on flag
        int []splitData = splitData(data, flag);

        int newBand[] = new int[3];
        switch (flag) 
        {
            case 0:
                newBand[0] = (oldBand[0] & ~0x3) | splitData[0];
                newBand[1] = (oldBand[1] & ~0x7) | splitData[1];
                newBand[2] = (oldBand[2] & ~0x7) | splitData[2];
                break;
            case 1:
                newBand[0] = (oldBand[0] & ~0x7) | splitData[0];
                newBand[1] = (oldBand[1] & ~0x3) | splitData[1];
                newBand[2] = (oldBand[2] & ~0x7) | splitData[2];
                break;
            case 2:
                newBand[0] = (oldBand[0] & ~0x7) | splitData[0];
                newBand[1] = (oldBand[1] & ~0x7) | splitData[1];
                newBand[2] = (oldBand[2] & ~0x3) | splitData[2];
                break;
        }
        flag = (flag+1)%MAX_FLAG;
        return newBand;
    }

    public int splitNext(int[] band)
    {
        int data = 0;
        switch (flag) 
        {
            case 0:
                data = (band[0] & 0x3)<<6 | (band[1] & 0x7)<<3 | (band[2] & 0x7);
                break;
            case 1:
                data = (band[0] & 0x7)<<5 | (band[1] & 0x3)<<3 | (band[2] & 0x7);
                break;
            case 2:
                data = (band[0] & 0x7)<<5 | (band[1] & 0x7)<<2 | (band[2] & 0x3);
                break;
        }

        flag = (flag+1)%MAX_FLAG;
        return data;
    }

    private int[] splitData(int data, int flag)
    {
        int []sp = new int[3];
        switch (flag) 
        {
            case 0:
                sp[0] = (data & 0b11000000) >> 6;
                sp[1] = (data & 0b00111000) >> 3;
                sp[2] = (data & 0b00000111);
                break;
            case 1:
                sp[0] = (data & 0b11100000) >> 5;
                sp[1] = (data & 0b00011000) >> 3;
                sp[2] = (data & 0b00000111);
                break;
            case 2:
                sp[0] = (data & 0b11100000) >> 5;
                sp[1] = (data & 0b00011100) >> 2;
                sp[2] = (data & 0b00000011);
                break;
        }
        return sp;
    }
}