package org.apache.poi.poifs.crypt.standard;

import org.apache.poi.util.LittleEndianByteArrayOutputStream;

public abstract interface EncryptionRecord
{
  public abstract void write(LittleEndianByteArrayOutputStream paramLittleEndianByteArrayOutputStream);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.standard.EncryptionRecord
 * JD-Core Version:    0.7.0.1
 */