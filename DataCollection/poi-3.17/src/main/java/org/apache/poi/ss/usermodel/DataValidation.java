package org.apache.poi.ss.usermodel;

import org.apache.poi.ss.util.CellRangeAddressList;

public abstract interface DataValidation
{
  public abstract DataValidationConstraint getValidationConstraint();
  
  public abstract void setErrorStyle(int paramInt);
  
  public abstract int getErrorStyle();
  
  public abstract void setEmptyCellAllowed(boolean paramBoolean);
  
  public abstract boolean getEmptyCellAllowed();
  
  public abstract void setSuppressDropDownArrow(boolean paramBoolean);
  
  public abstract boolean getSuppressDropDownArrow();
  
  public abstract void setShowPromptBox(boolean paramBoolean);
  
  public abstract boolean getShowPromptBox();
  
  public abstract void setShowErrorBox(boolean paramBoolean);
  
  public abstract boolean getShowErrorBox();
  
  public abstract void createPromptBox(String paramString1, String paramString2);
  
  public abstract String getPromptBoxTitle();
  
  public abstract String getPromptBoxText();
  
  public abstract void createErrorBox(String paramString1, String paramString2);
  
  public abstract String getErrorBoxTitle();
  
  public abstract String getErrorBoxText();
  
  public abstract CellRangeAddressList getRegions();
  
  public static final class ErrorStyle
  {
    public static final int STOP = 0;
    public static final int WARNING = 1;
    public static final int INFO = 2;
  }
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.DataValidation
 * JD-Core Version:    0.7.0.1
 */