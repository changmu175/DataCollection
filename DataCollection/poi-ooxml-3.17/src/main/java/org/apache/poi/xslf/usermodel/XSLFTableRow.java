/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.List;
/*   7:    */ import org.apache.poi.util.Units;
/*   8:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTable;
/*   9:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell;
/*  10:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol;
/*  11:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableGrid;
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow;
/*  13:    */ 
/*  14:    */ public class XSLFTableRow
/*  15:    */   implements Iterable<XSLFTableCell>
/*  16:    */ {
/*  17:    */   private final CTTableRow _row;
/*  18:    */   private final List<XSLFTableCell> _cells;
/*  19:    */   private final XSLFTable _table;
/*  20:    */   
/*  21:    */   XSLFTableRow(CTTableRow row, XSLFTable table)
/*  22:    */   {
/*  23: 40 */     this._row = row;
/*  24: 41 */     this._table = table;
/*  25: 42 */     CTTableCell[] tcArray = this._row.getTcArray();
/*  26: 43 */     this._cells = new ArrayList(tcArray.length);
/*  27: 44 */     for (CTTableCell cell : tcArray) {
/*  28: 45 */       this._cells.add(new XSLFTableCell(cell, table));
/*  29:    */     }
/*  30:    */   }
/*  31:    */   
/*  32:    */   public CTTableRow getXmlObject()
/*  33:    */   {
/*  34: 50 */     return this._row;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public Iterator<XSLFTableCell> iterator()
/*  38:    */   {
/*  39: 54 */     return this._cells.iterator();
/*  40:    */   }
/*  41:    */   
/*  42:    */   public List<XSLFTableCell> getCells()
/*  43:    */   {
/*  44: 58 */     return Collections.unmodifiableList(this._cells);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public double getHeight()
/*  48:    */   {
/*  49: 62 */     return Units.toPoints(this._row.getH());
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setHeight(double height)
/*  53:    */   {
/*  54: 66 */     this._row.setH(Units.toEMU(height));
/*  55:    */   }
/*  56:    */   
/*  57:    */   public XSLFTableCell addCell()
/*  58:    */   {
/*  59: 70 */     CTTableCell c = this._row.addNewTc();
/*  60: 71 */     c.set(XSLFTableCell.prototype());
/*  61: 72 */     XSLFTableCell cell = new XSLFTableCell(c, this._table);
/*  62: 73 */     this._cells.add(cell);
/*  63: 75 */     if (this._table.getNumberOfColumns() < this._row.sizeOfTcArray()) {
/*  64: 76 */       this._table.getCTTable().getTblGrid().addNewGridCol().setW(Units.toEMU(100.0D));
/*  65:    */     }
/*  66: 78 */     this._table.updateRowColIndexes();
/*  67: 79 */     return cell;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void mergeCells(int firstCol, int lastCol)
/*  71:    */   {
/*  72: 91 */     if (firstCol >= lastCol) {
/*  73: 92 */       throw new IllegalArgumentException("Cannot merge, first column >= last column : " + firstCol + " >= " + lastCol);
/*  74:    */     }
/*  75: 98 */     int colSpan = lastCol - firstCol + 1;
/*  76:    */     
/*  77:100 */     ((XSLFTableCell)this._cells.get(firstCol)).setGridSpan(colSpan);
/*  78:101 */     for (XSLFTableCell cell : this._cells.subList(firstCol + 1, lastCol + 1)) {
/*  79:102 */       cell.setHMerge(true);
/*  80:    */     }
/*  81:    */   }
/*  82:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFTableRow
 * JD-Core Version:    0.7.0.1
 */