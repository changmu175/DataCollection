/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.util.CellReference;
/*  4:   */ import org.apache.poi.util.LittleEndianInput;
/*  5:   */ import org.apache.poi.util.LittleEndianOutput;
/*  6:   */ 
/*  7:   */ abstract class Ref2DPtgBase
/*  8:   */   extends RefPtgBase
/*  9:   */ {
/* 10:   */   private static final int SIZE = 5;
/* 11:   */   
/* 12:   */   protected Ref2DPtgBase(int row, int column, boolean isRowRelative, boolean isColumnRelative)
/* 13:   */   {
/* 14:32 */     setRow(row);
/* 15:33 */     setColumn(column);
/* 16:34 */     setRowRelative(isRowRelative);
/* 17:35 */     setColRelative(isColumnRelative);
/* 18:   */   }
/* 19:   */   
/* 20:   */   protected Ref2DPtgBase(LittleEndianInput in)
/* 21:   */   {
/* 22:39 */     readCoordinates(in);
/* 23:   */   }
/* 24:   */   
/* 25:   */   protected Ref2DPtgBase(CellReference cr)
/* 26:   */   {
/* 27:43 */     super(cr);
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void write(LittleEndianOutput out)
/* 31:   */   {
/* 32:48 */     out.writeByte(getSid() + getPtgClass());
/* 33:49 */     writeCoordinates(out);
/* 34:   */   }
/* 35:   */   
/* 36:   */   public final String toFormulaString()
/* 37:   */   {
/* 38:54 */     return formatReferenceAsString();
/* 39:   */   }
/* 40:   */   
/* 41:   */   protected abstract byte getSid();
/* 42:   */   
/* 43:   */   public final int getSize()
/* 44:   */   {
/* 45:61 */     return 5;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public final String toString()
/* 49:   */   {
/* 50:66 */     StringBuffer sb = new StringBuffer();
/* 51:67 */     sb.append(getClass().getName());
/* 52:68 */     sb.append(" [");
/* 53:69 */     sb.append(formatReferenceAsString());
/* 54:70 */     sb.append("]");
/* 55:71 */     return sb.toString();
/* 56:   */   }
/* 57:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.Ref2DPtgBase
 * JD-Core Version:    0.7.0.1
 */