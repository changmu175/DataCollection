package org.apache.poi.poifs.crypt.dsig.services;

import org.apache.poi.poifs.crypt.dsig.SignatureConfig.SignatureConfigurable;

public abstract interface TimeStampService
  extends SignatureConfig.SignatureConfigurable
{
  public abstract byte[] timeStamp(byte[] paramArrayOfByte, RevocationData paramRevocationData)
    throws Exception;
}


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.dsig.services.TimeStampService
 * JD-Core Version:    0.7.0.1
 */