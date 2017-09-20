package org.apache.poi.poifs.crypt.dsig.services;

import java.security.cert.X509Certificate;
import java.util.List;

public abstract interface TimeStampServiceValidator
{
  public abstract void validate(List<X509Certificate> paramList, RevocationData paramRevocationData)
    throws Exception;
}


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.dsig.services.TimeStampServiceValidator
 * JD-Core Version:    0.7.0.1
 */