/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndianOutput;
/*   4:    */ 
/*   5:    */ public final class PrintGridlinesRecord
/*   6:    */   extends StandardRecord
/*   7:    */ {
/*   8:    */   public static final short sid = 43;
/*   9:    */   private short field_1_print_gridlines;
/*  10:    */   
/*  11:    */   public PrintGridlinesRecord() {}
/*  12:    */   
/*  13:    */   public PrintGridlinesRecord(RecordInputStream in)
/*  14:    */   {
/*  15: 45 */     this.field_1_print_gridlines = in.readShort();
/*  16:    */   }
/*  17:    */   
/*  18:    */   public void setPrintGridlines(boolean pg)
/*  19:    */   {
/*  20: 56 */     if (pg == true) {
/*  21: 58 */       this.field_1_print_gridlines = 1;
/*  22:    */     } else {
/*  23: 62 */       this.field_1_print_gridlines = 0;
/*  24:    */     }
/*  25:    */   }
/*  26:    */   
/*  27:    */   public boolean getPrintGridlines()
/*  28:    */   {
/*  29: 74 */     return this.field_1_print_gridlines == 1;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public String toString()
/*  33:    */   {
/*  34: 79 */     StringBuffer buffer = new StringBuffer();
/*  35:    */     
/*  36: 81 */     buffer.append("[PRINTGRIDLINES]\n");
/*  37: 82 */     buffer.append("    .printgridlines = ").append(getPrintGridlines()).append("\n");
/*  38:    */     
/*  39: 84 */     buffer.append("[/PRINTGRIDLINES]\n");
/*  40: 85 */     return buffer.toString();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void serialize(LittleEndianOutput out)
/*  44:    */   {
/*  45: 89 */     out.writeShort(this.field_1_print_gridlines);
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected int getDataSize()
/*  49:    */   {
/*  50: 93 */     return 2;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public short getSid()
/*  54:    */   {
/*  55: 98 */     return 43;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public Object clone()
/*  59:    */   {
/*  60:102 */     PrintGridlinesRecord rec = new PrintGridlinesRecord();
/*  61:103 */     rec.field_1_print_gridlines = this.field_1_print_gridlines;
/*  62:104 */     return rec;
/*  63:    */   }
/*  64:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.PrintGridlinesRecord
 * JD-Core Version:    0.7.0.1
 */