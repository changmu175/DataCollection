package org.apache.poi.ss.usermodel;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.util.Removal;

public abstract interface Workbook
  extends Closeable, Iterable<Sheet>
{
  public static final int PICTURE_TYPE_EMF = 2;
  public static final int PICTURE_TYPE_WMF = 3;
  public static final int PICTURE_TYPE_PICT = 4;
  public static final int PICTURE_TYPE_JPEG = 5;
  public static final int PICTURE_TYPE_PNG = 6;
  public static final int PICTURE_TYPE_DIB = 7;
  @Deprecated
  @Removal(version="3.18")
  public static final int SHEET_STATE_VISIBLE = 0;
  @Deprecated
  @Removal(version="3.18")
  public static final int SHEET_STATE_HIDDEN = 1;
  @Deprecated
  @Removal(version="3.18")
  public static final int SHEET_STATE_VERY_HIDDEN = 2;
  
  public abstract int getActiveSheetIndex();
  
  public abstract void setActiveSheet(int paramInt);
  
  public abstract int getFirstVisibleTab();
  
  public abstract void setFirstVisibleTab(int paramInt);
  
  public abstract void setSheetOrder(String paramString, int paramInt);
  
  public abstract void setSelectedTab(int paramInt);
  
  public abstract void setSheetName(int paramInt, String paramString);
  
  public abstract String getSheetName(int paramInt);
  
  public abstract int getSheetIndex(String paramString);
  
  public abstract int getSheetIndex(Sheet paramSheet);
  
  public abstract Sheet createSheet();
  
  public abstract Sheet createSheet(String paramString);
  
  public abstract Sheet cloneSheet(int paramInt);
  
  public abstract Iterator<Sheet> sheetIterator();
  
  public abstract int getNumberOfSheets();
  
  public abstract Sheet getSheetAt(int paramInt);
  
  public abstract Sheet getSheet(String paramString);
  
  public abstract void removeSheetAt(int paramInt);
  
  public abstract Font createFont();
  
  public abstract Font findFont(boolean paramBoolean1, short paramShort1, short paramShort2, String paramString, boolean paramBoolean2, boolean paramBoolean3, short paramShort3, byte paramByte);
  
  public abstract short getNumberOfFonts();
  
  public abstract Font getFontAt(short paramShort);
  
  public abstract CellStyle createCellStyle();
  
  public abstract int getNumCellStyles();
  
  public abstract CellStyle getCellStyleAt(int paramInt);
  
  public abstract void write(OutputStream paramOutputStream)
    throws IOException;
  
  public abstract void close()
    throws IOException;
  
  public abstract int getNumberOfNames();
  
  public abstract Name getName(String paramString);
  
  public abstract List<? extends Name> getNames(String paramString);
  
  public abstract List<? extends Name> getAllNames();
  
  public abstract Name getNameAt(int paramInt);
  
  public abstract Name createName();
  
  public abstract int getNameIndex(String paramString);
  
  public abstract void removeName(int paramInt);
  
  public abstract void removeName(String paramString);
  
  public abstract void removeName(Name paramName);
  
  public abstract int linkExternalWorkbook(String paramString, Workbook paramWorkbook);
  
  public abstract void setPrintArea(int paramInt, String paramString);
  
  public abstract void setPrintArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  public abstract String getPrintArea(int paramInt);
  
  public abstract void removePrintArea(int paramInt);
  
  public abstract Row.MissingCellPolicy getMissingCellPolicy();
  
  public abstract void setMissingCellPolicy(Row.MissingCellPolicy paramMissingCellPolicy);
  
  public abstract DataFormat createDataFormat();
  
  public abstract int addPicture(byte[] paramArrayOfByte, int paramInt);
  
  public abstract List<? extends PictureData> getAllPictures();
  
  public abstract CreationHelper getCreationHelper();
  
  public abstract boolean isHidden();
  
  public abstract void setHidden(boolean paramBoolean);
  
  public abstract boolean isSheetHidden(int paramInt);
  
  public abstract boolean isSheetVeryHidden(int paramInt);
  
  public abstract void setSheetHidden(int paramInt, boolean paramBoolean);
  
  /**
   * @deprecated
   */
  @Removal(version="3.18")
  public abstract void setSheetHidden(int paramInt1, int paramInt2);
  
  public abstract SheetVisibility getSheetVisibility(int paramInt);
  
  public abstract void setSheetVisibility(int paramInt, SheetVisibility paramSheetVisibility);
  
  public abstract void addToolPack(UDFFinder paramUDFFinder);
  
  public abstract void setForceFormulaRecalculation(boolean paramBoolean);
  
  public abstract boolean getForceFormulaRecalculation();
  
  public abstract SpreadsheetVersion getSpreadsheetVersion();
  
  public abstract int addOlePackage(byte[] paramArrayOfByte, String paramString1, String paramString2, String paramString3)
    throws IOException;
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.Workbook
 * JD-Core Version:    0.7.0.1
 */