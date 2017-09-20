/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianInput;
/*  4:   */ import org.apache.poi.util.LittleEndianOutput;
/*  5:   */ 
/*  6:   */ public final class MemAreaPtg
/*  7:   */   extends OperandPtg
/*  8:   */ {
/*  9:   */   public static final short sid = 38;
/* 10:   */   private static final int SIZE = 7;
/* 11:   */   private final int field_1_reserved;
/* 12:   */   private final int field_2_subex_len;
/* 13:   */   
/* 14:   */   public MemAreaPtg(int subexLen)
/* 15:   */   {
/* 16:35 */     this.field_1_reserved = 0;
/* 17:36 */     this.field_2_subex_len = subexLen;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public MemAreaPtg(LittleEndianInput in)
/* 21:   */   {
/* 22:40 */     this.field_1_reserved = in.readInt();
/* 23:41 */     this.field_2_subex_len = in.readShort();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public int getLenRefSubexpression()
/* 27:   */   {
/* 28:45 */     return this.field_2_subex_len;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void write(LittleEndianOutput out)
/* 32:   */   {
/* 33:49 */     out.writeByte(38 + getPtgClass());
/* 34:50 */     out.writeInt(this.field_1_reserved);
/* 35:51 */     out.writeShort(this.field_2_subex_len);
/* 36:   */   }
/* 37:   */   
/* 38:   */   public int getSize()
/* 39:   */   {
/* 40:55 */     return 7;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public String toFormulaString()
/* 44:   */   {
/* 45:59 */     return "";
/* 46:   */   }
/* 47:   */   
/* 48:   */   public byte getDefaultOperandClass()
/* 49:   */   {
/* 50:63 */     return 32;
/* 51:   */   }
/* 52:   */   
/* 53:   */   public final String toString()
/* 54:   */   {
/* 55:68 */     StringBuffer sb = new StringBuffer(64);
/* 56:69 */     sb.append(getClass().getName()).append(" [len=");
/* 57:70 */     sb.append(this.field_2_subex_len);
/* 58:71 */     sb.append("]");
/* 59:72 */     return sb.toString();
/* 60:   */   }
/* 61:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.MemAreaPtg
 * JD-Core Version:    0.7.0.1
 */