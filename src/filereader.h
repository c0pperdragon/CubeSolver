

// ------------- HALFBYTE FILE READER SUBSYSTEM ---------

byte filehandle;
bool havehalfbyte;
byte bytebuffer;

void checkfileerror(unsigned int err)
{
     if (err!=0)
     {  ResetScreen();
        TextOut (5,30, "FILE ERROR");
        NumOut (5,20, err);
        for (;;);
     }
}

void openfile(string fname)
{
     int filesize;
     checkfileerror( OpenFileRead	(fname, filesize, filehandle) );
     havehalfbyte=false;
}

void closefile()
{
     checkfileerror (CloseFile(filehandle));
}

byte readhalfbyte()
{
     if (havehalfbyte)
     {  havehalfbyte = false;
        return bytebuffer & 0xf;
     }
     checkfileerror (Read(filehandle, bytebuffer));
     havehalfbyte = true;
     return (bytebuffer>>4) & 0x0f;
}

byte readbyte()
{
    byte a = readhalfbyte();
    byte b = readhalfbyte();
    return (a<<4) | b;
}

unsigned int readuint()
{
    unsigned int a = readbyte();
    unsigned int b = readbyte();
    return (a<<8) | b;
}

void seektohalfbyte(unsigned int halfbyteposition)
{
    FileSeekType args;
    args.FileHandle = filehandle;
    args.Origin = SEEK_SET;
    args.Length = halfbyteposition>>1;
    SysFileSeek(args);

    checkfileerror (args.Result);

    havehalfbyte=false;

    if ((halfbyteposition&1)==1)
    {   readhalfbyte();
    }
}

void findandskiphalfbyteF()
{
    // if there still is a half-byte in buffer,
    // do one normal search step
    if (havehalfbyte)
    {   byte a = readhalfbyte();
        if (a==0xf)
        {   return;
        }
    }
    // read whole bytes and check for contained F nibble faster
    for (;;)
    {   checkfileerror (Read(filehandle, bytebuffer));
        if ((bytebuffer&0xf0) == 0xf0)    // found end mark in first nibble
        {   havehalfbyte=true;
            return;
        }
        else if ((bytebuffer&0x0f) == 0x0f)   // found end mark in second nibble
        {   havehalfbyte=false;
            return;
        }
    }
}

