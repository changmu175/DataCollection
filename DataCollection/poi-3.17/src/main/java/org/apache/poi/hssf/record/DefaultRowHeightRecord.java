/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndianOutput;
/*   4:    */ 
/*   5:    */ public final class DefaultRowHeightRecord
/*   6:    */   extends StandardRecord
/*   7:    */   implements Cloneable
/*   8:    */ {
/*   9:    */   public static final short sid = 549;
/*  10:    */   private short field_1_option_flags;
/*  11:    */   private short field_2_row_height;
/*  12:    */   public static final short DEFAULT_ROW_HEIGHT = 255;
/*  13:    */   
/*  14:    */   public DefaultRowHeightRecord()
/*  15:    */   {
/*  16: 46 */     this.field_1_option_flags = 0;
/*  17: 47 */     this.field_2_row_height = 255;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public DefaultRowHeightRecord(RecordInputStream in)
/*  21:    */   {
/*  22: 52 */     this.field_1_option_flags = in.readShort();
/*  23: 53 */     this.field_2_row_height = in.readShort();
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setOptionFlags(short flags)
/*  27:    */   {
/*  28: 63 */     this.field_1_option_flags = flags;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setRowHeight(short height)
/*  32:    */   {
/*  33: 73 */     this.field_2_row_height = height;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public short getOptionFlags()
/*  37:    */   {
/*  38: 83 */     return this.field_1_option_flags;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public short getRowHeight()
/*  42:    */   {
/*  43: 93 */     return this.field_2_row_height;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public String toString()
/*  47:    */   {
/*  48: 98 */     StringBuffer buffer = new StringBuffer();
/*  49:    */     
/*  50:100 */     buffer.append("[DEFAULTROWHEIGHT]\n");
/*  51:101 */     buffer.append("    .optionflags    = ").append(Integer.toHexString(getOptionFlags())).append("\n");
/*  52:    */     
/*  53:103 */     buffer.append("    .rowheight      = ").append(Integer.toHexString(getRowHeight())).append("\n");
/*  54:    */     
/*  55:105 */     buffer.append("[/DEFAULTROWHEIGHT]\n");
/*  56:106 */     return buffer.toString();
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void serialize(LittleEndianOutput out)
/*  60:    */   {
/*  61:110 */     out.writeShort(getOptionFlags());
/*  62:111 */     out.writeShort(getRowHeight());
/*  63:    */   }
/*  64:    */   
/*  65:    */   protected int getDataSize()
/*  66:    */   {
/*  67:115 */     return 4;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public short getSid()
/*  71:    */   {
/*  72:120 */     return 549;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public DefaultRowHeightRecord clone()
/*  76:    */   {
/*  77:125 */     DefaultRowHeightRecord rec = new DefaultRowHeightRecord();
/*  78:126 */     rec.field_1_option_flags = this.field_1_option_flags;
/*  79:127 */     rec.field_2_row_height = this.field_2_row_height;
/*  80:128 */     return rec;
/*  81:    */   }
/*  82:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.DefaultRowHeightRecord
 * JD-Core Version:    0.7.0.1
 */