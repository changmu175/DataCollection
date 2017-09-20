package org.apache.poi.ss.format;

import java.util.Locale;

public enum CellFormatType
{
  GENERAL,  NUMBER,  DATE,  ELAPSED,  TEXT;
  
  private CellFormatType() {}
  
  abstract boolean isSpecial(char paramChar);
  
  abstract CellFormatter formatter(String paramString);
  
  abstract CellFormatter formatter(Locale paramLocale, String paramString);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.format.CellFormatType
 * JD-Core Version:    0.7.0.1
 */