/*   1:    */ package org.apache.poi.ss.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.Iterator;
/*   4:    */ 
/*   5:    */ public abstract interface Row
/*   6:    */   extends Iterable<Cell>
/*   7:    */ {
/*   8:    */   public abstract Cell createCell(int paramInt);
/*   9:    */   
/*  10:    */   /**
/*  11:    */    * @deprecated
/*  12:    */    */
/*  13:    */   public abstract Cell createCell(int paramInt1, int paramInt2);
/*  14:    */   
/*  15:    */   public abstract Cell createCell(int paramInt, CellType paramCellType);
/*  16:    */   
/*  17:    */   public abstract void removeCell(Cell paramCell);
/*  18:    */   
/*  19:    */   public abstract void setRowNum(int paramInt);
/*  20:    */   
/*  21:    */   public abstract int getRowNum();
/*  22:    */   
/*  23:    */   public abstract Cell getCell(int paramInt);
/*  24:    */   
/*  25:    */   public abstract Cell getCell(int paramInt, MissingCellPolicy paramMissingCellPolicy);
/*  26:    */   
/*  27:    */   public abstract short getFirstCellNum();
/*  28:    */   
/*  29:    */   public abstract short getLastCellNum();
/*  30:    */   
/*  31:    */   public abstract int getPhysicalNumberOfCells();
/*  32:    */   
/*  33:    */   public abstract void setHeight(short paramShort);
/*  34:    */   
/*  35:    */   public abstract void setZeroHeight(boolean paramBoolean);
/*  36:    */   
/*  37:    */   public abstract boolean getZeroHeight();
/*  38:    */   
/*  39:    */   public abstract void setHeightInPoints(float paramFloat);
/*  40:    */   
/*  41:    */   public abstract short getHeight();
/*  42:    */   
/*  43:    */   public abstract float getHeightInPoints();
/*  44:    */   
/*  45:    */   public abstract boolean isFormatted();
/*  46:    */   
/*  47:    */   public abstract CellStyle getRowStyle();
/*  48:    */   
/*  49:    */   public abstract void setRowStyle(CellStyle paramCellStyle);
/*  50:    */   
/*  51:    */   public abstract Iterator<Cell> cellIterator();
/*  52:    */   
/*  53:    */   public abstract Sheet getSheet();
/*  54:    */   
/*  55:    */   public abstract int getOutlineLevel();
/*  56:    */   
/*  57:    */   public static enum MissingCellPolicy
/*  58:    */   {
/*  59:238 */     RETURN_NULL_AND_BLANK,  RETURN_BLANK_AS_NULL,  CREATE_NULL_AS_BLANK;
/*  60:    */     
/*  61:    */     private MissingCellPolicy() {}
/*  62:    */   }
/*  63:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.Row
 * JD-Core Version:    0.7.0.1
 */