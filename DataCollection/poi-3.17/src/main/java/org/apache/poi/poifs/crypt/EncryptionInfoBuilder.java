package org.apache.poi.poifs.crypt;

import java.io.IOException;
import org.apache.poi.util.LittleEndianInput;

public abstract interface EncryptionInfoBuilder
{
  public abstract void initialize(EncryptionInfo paramEncryptionInfo, LittleEndianInput paramLittleEndianInput)
    throws IOException;
  
  public abstract void initialize(EncryptionInfo paramEncryptionInfo, CipherAlgorithm paramCipherAlgorithm, HashAlgorithm paramHashAlgorithm, int paramInt1, int paramInt2, ChainingMode paramChainingMode);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.EncryptionInfoBuilder
 * JD-Core Version:    0.7.0.1
 */