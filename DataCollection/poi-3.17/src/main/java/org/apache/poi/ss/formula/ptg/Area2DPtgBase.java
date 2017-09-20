/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.util.AreaReference;
/*  4:   */ import org.apache.poi.util.LittleEndianInput;
/*  5:   */ import org.apache.poi.util.LittleEndianOutput;
/*  6:   */ 
/*  7:   */ public abstract class Area2DPtgBase
/*  8:   */   extends AreaPtgBase
/*  9:   */ {
/* 10:   */   private static final int SIZE = 9;
/* 11:   */   
/* 12:   */   protected Area2DPtgBase(int firstRow, int lastRow, int firstColumn, int lastColumn, boolean firstRowRelative, boolean lastRowRelative, boolean firstColRelative, boolean lastColRelative)
/* 13:   */   {
/* 14:31 */     super(firstRow, lastRow, firstColumn, lastColumn, firstRowRelative, lastRowRelative, firstColRelative, lastColRelative);
/* 15:   */   }
/* 16:   */   
/* 17:   */   protected Area2DPtgBase(AreaReference ar)
/* 18:   */   {
/* 19:34 */     super(ar);
/* 20:   */   }
/* 21:   */   
/* 22:   */   protected Area2DPtgBase(LittleEndianInput in)
/* 23:   */   {
/* 24:38 */     readCoordinates(in);
/* 25:   */   }
/* 26:   */   
/* 27:   */   protected abstract byte getSid();
/* 28:   */   
/* 29:   */   public final void write(LittleEndianOutput out)
/* 30:   */   {
/* 31:44 */     out.writeByte(getSid() + getPtgClass());
/* 32:45 */     writeCoordinates(out);
/* 33:   */   }
/* 34:   */   
/* 35:   */   public final int getSize()
/* 36:   */   {
/* 37:49 */     return 9;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public final String toFormulaString()
/* 41:   */   {
/* 42:53 */     return formatReferenceAsString();
/* 43:   */   }
/* 44:   */   
/* 45:   */   public final String toString()
/* 46:   */   {
/* 47:57 */     StringBuffer sb = new StringBuffer();
/* 48:58 */     sb.append(getClass().getName());
/* 49:59 */     sb.append(" [");
/* 50:60 */     sb.append(formatReferenceAsString());
/* 51:61 */     sb.append("]");
/* 52:62 */     return sb.toString();
/* 53:   */   }
/* 54:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.Area2DPtgBase
 * JD-Core Version:    0.7.0.1
 */