/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianInput;
/*  4:   */ import org.apache.poi.util.LittleEndianOutput;
/*  5:   */ 
/*  6:   */ public final class TblPtg
/*  7:   */   extends ControlPtg
/*  8:   */ {
/*  9:   */   private static final int SIZE = 5;
/* 10:   */   public static final short sid = 2;
/* 11:   */   private final int field_1_first_row;
/* 12:   */   private final int field_2_first_col;
/* 13:   */   
/* 14:   */   public TblPtg(LittleEndianInput in)
/* 15:   */   {
/* 16:46 */     this.field_1_first_row = in.readUShort();
/* 17:47 */     this.field_2_first_col = in.readUShort();
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void write(LittleEndianOutput out)
/* 21:   */   {
/* 22:51 */     out.writeByte(2 + getPtgClass());
/* 23:52 */     out.writeShort(this.field_1_first_row);
/* 24:53 */     out.writeShort(this.field_2_first_col);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public int getSize()
/* 28:   */   {
/* 29:57 */     return 5;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public int getRow()
/* 33:   */   {
/* 34:61 */     return this.field_1_first_row;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public int getColumn()
/* 38:   */   {
/* 39:65 */     return this.field_2_first_col;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public String toFormulaString()
/* 43:   */   {
/* 44:71 */     throw new RuntimeException("Table and Arrays are not yet supported");
/* 45:   */   }
/* 46:   */   
/* 47:   */   public String toString()
/* 48:   */   {
/* 49:75 */     StringBuffer buffer = new StringBuffer("[Data Table - Parent cell is an interior cell in a data table]\n");
/* 50:76 */     buffer.append("top left row = ").append(getRow()).append("\n");
/* 51:77 */     buffer.append("top left col = ").append(getColumn()).append("\n");
/* 52:78 */     return buffer.toString();
/* 53:   */   }
/* 54:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.TblPtg
 * JD-Core Version:    0.7.0.1
 */