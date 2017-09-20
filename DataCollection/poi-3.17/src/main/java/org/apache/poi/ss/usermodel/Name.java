package org.apache.poi.ss.usermodel;

public abstract interface Name
{
  public abstract String getSheetName();
  
  public abstract String getNameName();
  
  public abstract void setNameName(String paramString);
  
  public abstract String getRefersToFormula();
  
  public abstract void setRefersToFormula(String paramString);
  
  public abstract boolean isFunctionName();
  
  public abstract boolean isDeleted();
  
  public abstract void setSheetIndex(int paramInt);
  
  public abstract int getSheetIndex();
  
  public abstract String getComment();
  
  public abstract void setComment(String paramString);
  
  public abstract void setFunction(boolean paramBoolean);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.Name
 * JD-Core Version:    0.7.0.1
 */