/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndianOutput;
/*   4:    */ 
/*   5:    */ public final class PrintHeadersRecord
/*   6:    */   extends StandardRecord
/*   7:    */ {
/*   8:    */   public static final short sid = 42;
/*   9:    */   private short field_1_print_headers;
/*  10:    */   
/*  11:    */   public PrintHeadersRecord() {}
/*  12:    */   
/*  13:    */   public PrintHeadersRecord(RecordInputStream in)
/*  14:    */   {
/*  15: 46 */     this.field_1_print_headers = in.readShort();
/*  16:    */   }
/*  17:    */   
/*  18:    */   public void setPrintHeaders(boolean p)
/*  19:    */   {
/*  20: 56 */     if (p == true) {
/*  21: 58 */       this.field_1_print_headers = 1;
/*  22:    */     } else {
/*  23: 62 */       this.field_1_print_headers = 0;
/*  24:    */     }
/*  25:    */   }
/*  26:    */   
/*  27:    */   public boolean getPrintHeaders()
/*  28:    */   {
/*  29: 73 */     return this.field_1_print_headers == 1;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public String toString()
/*  33:    */   {
/*  34: 78 */     StringBuffer buffer = new StringBuffer();
/*  35:    */     
/*  36: 80 */     buffer.append("[PRINTHEADERS]\n");
/*  37: 81 */     buffer.append("    .printheaders   = ").append(getPrintHeaders()).append("\n");
/*  38:    */     
/*  39: 83 */     buffer.append("[/PRINTHEADERS]\n");
/*  40: 84 */     return buffer.toString();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void serialize(LittleEndianOutput out)
/*  44:    */   {
/*  45: 88 */     out.writeShort(this.field_1_print_headers);
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected int getDataSize()
/*  49:    */   {
/*  50: 92 */     return 2;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public short getSid()
/*  54:    */   {
/*  55: 97 */     return 42;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public Object clone()
/*  59:    */   {
/*  60:101 */     PrintHeadersRecord rec = new PrintHeadersRecord();
/*  61:102 */     rec.field_1_print_headers = this.field_1_print_headers;
/*  62:103 */     return rec;
/*  63:    */   }
/*  64:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.PrintHeadersRecord
 * JD-Core Version:    0.7.0.1
 */