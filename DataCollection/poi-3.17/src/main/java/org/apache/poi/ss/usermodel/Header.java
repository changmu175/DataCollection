package org.apache.poi.ss.usermodel;

public abstract interface Header
  extends HeaderFooter
{
  public abstract String getLeft();
  
  public abstract void setLeft(String paramString);
  
  public abstract String getCenter();
  
  public abstract void setCenter(String paramString);
  
  public abstract String getRight();
  
  public abstract void setRight(String paramString);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.Header
 * JD-Core Version:    0.7.0.1
 */