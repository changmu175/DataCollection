package org.apache.poi.poifs.crypt.dsig.services;

public abstract interface SignaturePolicyService
{
  public abstract String getSignaturePolicyIdentifier();
  
  public abstract String getSignaturePolicyDescription();
  
  public abstract String getSignaturePolicyDownloadUrl();
  
  public abstract byte[] getSignaturePolicyDocument();
}


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.dsig.services.SignaturePolicyService
 * JD-Core Version:    0.7.0.1
 */