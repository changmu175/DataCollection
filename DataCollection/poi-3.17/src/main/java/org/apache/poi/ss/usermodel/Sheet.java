package org.apache.poi.ss.usermodel;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.PaneInformation;

public abstract interface Sheet
  extends Iterable<Row>
{
  public static final short LeftMargin = 0;
  public static final short RightMargin = 1;
  public static final short TopMargin = 2;
  public static final short BottomMargin = 3;
  public static final short HeaderMargin = 4;
  public static final short FooterMargin = 5;
  public static final byte PANE_LOWER_RIGHT = 0;
  public static final byte PANE_UPPER_RIGHT = 1;
  public static final byte PANE_LOWER_LEFT = 2;
  public static final byte PANE_UPPER_LEFT = 3;
  
  public abstract Row createRow(int paramInt);
  
  public abstract void removeRow(Row paramRow);
  
  public abstract Row getRow(int paramInt);
  
  public abstract int getPhysicalNumberOfRows();
  
  public abstract int getFirstRowNum();
  
  public abstract int getLastRowNum();
  
  public abstract void setColumnHidden(int paramInt, boolean paramBoolean);
  
  public abstract boolean isColumnHidden(int paramInt);
  
  public abstract void setRightToLeft(boolean paramBoolean);
  
  public abstract boolean isRightToLeft();
  
  public abstract void setColumnWidth(int paramInt1, int paramInt2);
  
  public abstract int getColumnWidth(int paramInt);
  
  public abstract float getColumnWidthInPixels(int paramInt);
  
  public abstract void setDefaultColumnWidth(int paramInt);
  
  public abstract int getDefaultColumnWidth();
  
  public abstract short getDefaultRowHeight();
  
  public abstract float getDefaultRowHeightInPoints();
  
  public abstract void setDefaultRowHeight(short paramShort);
  
  public abstract void setDefaultRowHeightInPoints(float paramFloat);
  
  public abstract CellStyle getColumnStyle(int paramInt);
  
  public abstract int addMergedRegion(CellRangeAddress paramCellRangeAddress);
  
  public abstract int addMergedRegionUnsafe(CellRangeAddress paramCellRangeAddress);
  
  public abstract void validateMergedRegions();
  
  public abstract void setVerticallyCenter(boolean paramBoolean);
  
  public abstract void setHorizontallyCenter(boolean paramBoolean);
  
  public abstract boolean getHorizontallyCenter();
  
  public abstract boolean getVerticallyCenter();
  
  public abstract void removeMergedRegion(int paramInt);
  
  public abstract void removeMergedRegions(Collection<Integer> paramCollection);
  
  public abstract int getNumMergedRegions();
  
  public abstract CellRangeAddress getMergedRegion(int paramInt);
  
  public abstract List<CellRangeAddress> getMergedRegions();
  
  public abstract Iterator<Row> rowIterator();
  
  public abstract void setForceFormulaRecalculation(boolean paramBoolean);
  
  public abstract boolean getForceFormulaRecalculation();
  
  public abstract void setAutobreaks(boolean paramBoolean);
  
  public abstract void setDisplayGuts(boolean paramBoolean);
  
  public abstract void setDisplayZeros(boolean paramBoolean);
  
  public abstract boolean isDisplayZeros();
  
  public abstract void setFitToPage(boolean paramBoolean);
  
  public abstract void setRowSumsBelow(boolean paramBoolean);
  
  public abstract void setRowSumsRight(boolean paramBoolean);
  
  public abstract boolean getAutobreaks();
  
  public abstract boolean getDisplayGuts();
  
  public abstract boolean getFitToPage();
  
  public abstract boolean getRowSumsBelow();
  
  public abstract boolean getRowSumsRight();
  
  public abstract boolean isPrintGridlines();
  
  public abstract void setPrintGridlines(boolean paramBoolean);
  
  public abstract boolean isPrintRowAndColumnHeadings();
  
  public abstract void setPrintRowAndColumnHeadings(boolean paramBoolean);
  
  public abstract PrintSetup getPrintSetup();
  
  public abstract Header getHeader();
  
  public abstract Footer getFooter();
  
  public abstract void setSelected(boolean paramBoolean);
  
  public abstract double getMargin(short paramShort);
  
  public abstract void setMargin(short paramShort, double paramDouble);
  
  public abstract boolean getProtect();
  
  public abstract void protectSheet(String paramString);
  
  public abstract boolean getScenarioProtect();
  
  public abstract void setZoom(int paramInt);
  
  public abstract short getTopRow();
  
  public abstract short getLeftCol();
  
  public abstract void showInPane(int paramInt1, int paramInt2);
  
  public abstract void shiftRows(int paramInt1, int paramInt2, int paramInt3);
  
  public abstract void shiftRows(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract void createFreezePane(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract void createFreezePane(int paramInt1, int paramInt2);
  
  public abstract void createSplitPane(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  public abstract PaneInformation getPaneInformation();
  
  public abstract void setDisplayGridlines(boolean paramBoolean);
  
  public abstract boolean isDisplayGridlines();
  
  public abstract void setDisplayFormulas(boolean paramBoolean);
  
  public abstract boolean isDisplayFormulas();
  
  public abstract void setDisplayRowColHeadings(boolean paramBoolean);
  
  public abstract boolean isDisplayRowColHeadings();
  
  public abstract void setRowBreak(int paramInt);
  
  public abstract boolean isRowBroken(int paramInt);
  
  public abstract void removeRowBreak(int paramInt);
  
  public abstract int[] getRowBreaks();
  
  public abstract int[] getColumnBreaks();
  
  public abstract void setColumnBreak(int paramInt);
  
  public abstract boolean isColumnBroken(int paramInt);
  
  public abstract void removeColumnBreak(int paramInt);
  
  public abstract void setColumnGroupCollapsed(int paramInt, boolean paramBoolean);
  
  public abstract void groupColumn(int paramInt1, int paramInt2);
  
  public abstract void ungroupColumn(int paramInt1, int paramInt2);
  
  public abstract void groupRow(int paramInt1, int paramInt2);
  
  public abstract void ungroupRow(int paramInt1, int paramInt2);
  
  public abstract void setRowGroupCollapsed(int paramInt, boolean paramBoolean);
  
  public abstract void setDefaultColumnStyle(int paramInt, CellStyle paramCellStyle);
  
  public abstract void autoSizeColumn(int paramInt);
  
  public abstract void autoSizeColumn(int paramInt, boolean paramBoolean);
  
  public abstract Comment getCellComment(CellAddress paramCellAddress);
  
  public abstract Map<CellAddress, ? extends Comment> getCellComments();
  
  public abstract Drawing<?> getDrawingPatriarch();
  
  public abstract Drawing<?> createDrawingPatriarch();
  
  public abstract Workbook getWorkbook();
  
  public abstract String getSheetName();
  
  public abstract boolean isSelected();
  
  public abstract CellRange<? extends Cell> setArrayFormula(String paramString, CellRangeAddress paramCellRangeAddress);
  
  public abstract CellRange<? extends Cell> removeArrayFormula(Cell paramCell);
  
  public abstract DataValidationHelper getDataValidationHelper();
  
  public abstract List<? extends DataValidation> getDataValidations();
  
  public abstract void addValidationData(DataValidation paramDataValidation);
  
  public abstract AutoFilter setAutoFilter(CellRangeAddress paramCellRangeAddress);
  
  public abstract SheetConditionalFormatting getSheetConditionalFormatting();
  
  public abstract CellRangeAddress getRepeatingRows();
  
  public abstract CellRangeAddress getRepeatingColumns();
  
  public abstract void setRepeatingRows(CellRangeAddress paramCellRangeAddress);
  
  public abstract void setRepeatingColumns(CellRangeAddress paramCellRangeAddress);
  
  public abstract int getColumnOutlineLevel(int paramInt);
  
  public abstract Hyperlink getHyperlink(int paramInt1, int paramInt2);
  
  public abstract Hyperlink getHyperlink(CellAddress paramCellAddress);
  
  public abstract List<? extends Hyperlink> getHyperlinkList();
  
  public abstract CellAddress getActiveCell();
  
  public abstract void setActiveCell(CellAddress paramCellAddress);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.Sheet
 * JD-Core Version:    0.7.0.1
 */