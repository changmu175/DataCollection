package org.apache.poi.common.usermodel;

public abstract interface Hyperlink
{
  public abstract String getAddress();
  
  public abstract void setAddress(String paramString);
  
  public abstract String getLabel();
  
  public abstract void setLabel(String paramString);
  
  /**
   * @deprecated
   */
  public abstract int getType();
  
  public abstract HyperlinkType getTypeEnum();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.common.usermodel.Hyperlink
 * JD-Core Version:    0.7.0.1
 */