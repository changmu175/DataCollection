/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndianOutput;
/*   4:    */ 
/*   5:    */ public final class CodepageRecord
/*   6:    */   extends StandardRecord
/*   7:    */ {
/*   8:    */   public static final short sid = 66;
/*   9:    */   private short field_1_codepage;
/*  10:    */   public static final short CODEPAGE = 1200;
/*  11:    */   
/*  12:    */   public CodepageRecord() {}
/*  13:    */   
/*  14:    */   public CodepageRecord(RecordInputStream in)
/*  15:    */   {
/*  16: 53 */     this.field_1_codepage = in.readShort();
/*  17:    */   }
/*  18:    */   
/*  19:    */   public void setCodepage(short cp)
/*  20:    */   {
/*  21: 65 */     this.field_1_codepage = cp;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public short getCodepage()
/*  25:    */   {
/*  26: 77 */     return this.field_1_codepage;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public String toString()
/*  30:    */   {
/*  31: 82 */     StringBuffer buffer = new StringBuffer();
/*  32:    */     
/*  33: 84 */     buffer.append("[CODEPAGE]\n");
/*  34: 85 */     buffer.append("    .codepage        = ").append(Integer.toHexString(getCodepage())).append("\n");
/*  35:    */     
/*  36: 87 */     buffer.append("[/CODEPAGE]\n");
/*  37: 88 */     return buffer.toString();
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void serialize(LittleEndianOutput out)
/*  41:    */   {
/*  42: 92 */     out.writeShort(getCodepage());
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected int getDataSize()
/*  46:    */   {
/*  47: 96 */     return 2;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public short getSid()
/*  51:    */   {
/*  52:101 */     return 66;
/*  53:    */   }
/*  54:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.CodepageRecord
 * JD-Core Version:    0.7.0.1
 */