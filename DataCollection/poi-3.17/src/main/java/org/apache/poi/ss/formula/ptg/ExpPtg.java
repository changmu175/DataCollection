/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianInput;
/*  4:   */ import org.apache.poi.util.LittleEndianOutput;
/*  5:   */ 
/*  6:   */ public final class ExpPtg
/*  7:   */   extends ControlPtg
/*  8:   */ {
/*  9:   */   private static final int SIZE = 5;
/* 10:   */   public static final short sid = 1;
/* 11:   */   private final int field_1_first_row;
/* 12:   */   private final int field_2_first_col;
/* 13:   */   
/* 14:   */   public ExpPtg(LittleEndianInput in)
/* 15:   */   {
/* 16:36 */     this.field_1_first_row = in.readShort();
/* 17:37 */     this.field_2_first_col = in.readShort();
/* 18:   */   }
/* 19:   */   
/* 20:   */   public ExpPtg(int firstRow, int firstCol)
/* 21:   */   {
/* 22:41 */     this.field_1_first_row = firstRow;
/* 23:42 */     this.field_2_first_col = firstCol;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void write(LittleEndianOutput out)
/* 27:   */   {
/* 28:47 */     out.writeByte(1 + getPtgClass());
/* 29:48 */     out.writeShort(this.field_1_first_row);
/* 30:49 */     out.writeShort(this.field_2_first_col);
/* 31:   */   }
/* 32:   */   
/* 33:   */   public int getSize()
/* 34:   */   {
/* 35:54 */     return 5;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public int getRow()
/* 39:   */   {
/* 40:58 */     return this.field_1_first_row;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public int getColumn()
/* 44:   */   {
/* 45:62 */     return this.field_2_first_col;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public String toFormulaString()
/* 49:   */   {
/* 50:67 */     throw new RuntimeException("Coding Error: Expected ExpPtg to be converted from Shared to Non-Shared Formula by ValueRecordsAggregate, but it wasn't");
/* 51:   */   }
/* 52:   */   
/* 53:   */   public String toString()
/* 54:   */   {
/* 55:72 */     StringBuffer buffer = new StringBuffer("[Array Formula or Shared Formula]\n");
/* 56:73 */     buffer.append("row = ").append(getRow()).append("\n");
/* 57:74 */     buffer.append("col = ").append(getColumn()).append("\n");
/* 58:75 */     return buffer.toString();
/* 59:   */   }
/* 60:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.ExpPtg
 * JD-Core Version:    0.7.0.1
 */