package org.apache.poi.poifs.crypt.dsig.services;

import java.security.cert.X509Certificate;
import java.util.List;

public abstract interface RevocationDataService
{
  public abstract RevocationData getRevocationData(List<X509Certificate> paramList);
}


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.dsig.services.RevocationDataService
 * JD-Core Version:    0.7.0.1
 */