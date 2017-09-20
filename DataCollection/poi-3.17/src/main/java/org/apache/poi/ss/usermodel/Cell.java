package org.apache.poi.ss.usermodel;

import java.util.Calendar;
import java.util.Date;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.Removal;

public abstract interface Cell
{
  @Deprecated
  @Removal(version="4.0")
  public static final int CELL_TYPE_NUMERIC = 0;
  @Deprecated
  @Removal(version="4.0")
  public static final int CELL_TYPE_STRING = 1;
  @Deprecated
  @Removal(version="4.0")
  public static final int CELL_TYPE_FORMULA = 2;
  @Deprecated
  @Removal(version="4.0")
  public static final int CELL_TYPE_BLANK = 3;
  @Deprecated
  @Removal(version="4.0")
  public static final int CELL_TYPE_BOOLEAN = 4;
  @Deprecated
  @Removal(version="4.0")
  public static final int CELL_TYPE_ERROR = 5;
  
  public abstract int getColumnIndex();
  
  public abstract int getRowIndex();
  
  public abstract Sheet getSheet();
  
  public abstract Row getRow();
  
  @Deprecated
  @Removal(version="4.0")
  public abstract void setCellType(int paramInt);
  
  public abstract void setCellType(CellType paramCellType);
  
  @Deprecated
  public abstract int getCellType();
  
  @Removal(version="4.2")
  public abstract CellType getCellTypeEnum();
  
  @Deprecated
  public abstract int getCachedFormulaResultType();
  
  public abstract CellType getCachedFormulaResultTypeEnum();
  
  public abstract void setCellValue(double paramDouble);
  
  public abstract void setCellValue(Date paramDate);
  
  public abstract void setCellValue(Calendar paramCalendar);
  
  public abstract void setCellValue(RichTextString paramRichTextString);
  
  public abstract void setCellValue(String paramString);
  
  public abstract void setCellFormula(String paramString)
    throws FormulaParseException;
  
  public abstract String getCellFormula();
  
  public abstract double getNumericCellValue();
  
  public abstract Date getDateCellValue();
  
  public abstract RichTextString getRichStringCellValue();
  
  public abstract String getStringCellValue();
  
  public abstract void setCellValue(boolean paramBoolean);
  
  public abstract void setCellErrorValue(byte paramByte);
  
  public abstract boolean getBooleanCellValue();
  
  public abstract byte getErrorCellValue();
  
  public abstract void setCellStyle(CellStyle paramCellStyle);
  
  public abstract CellStyle getCellStyle();
  
  public abstract void setAsActiveCell();
  
  public abstract CellAddress getAddress();
  
  public abstract void setCellComment(Comment paramComment);
  
  public abstract Comment getCellComment();
  
  public abstract void removeCellComment();
  
  public abstract Hyperlink getHyperlink();
  
  public abstract void setHyperlink(Hyperlink paramHyperlink);
  
  public abstract void removeHyperlink();
  
  public abstract CellRangeAddress getArrayFormulaRange();
  
  public abstract boolean isPartOfArrayFormulaGroup();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.Cell
 * JD-Core Version:    0.7.0.1
 */