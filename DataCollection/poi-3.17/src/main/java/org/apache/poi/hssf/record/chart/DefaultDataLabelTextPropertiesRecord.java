/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ 
/*   8:    */ public final class DefaultDataLabelTextPropertiesRecord
/*   9:    */   extends StandardRecord
/*  10:    */   implements Cloneable
/*  11:    */ {
/*  12:    */   public static final short sid = 4132;
/*  13:    */   private short field_1_categoryDataType;
/*  14:    */   public static final short CATEGORY_DATA_TYPE_SHOW_LABELS_CHARACTERISTIC = 0;
/*  15:    */   public static final short CATEGORY_DATA_TYPE_VALUE_AND_PERCENTAGE_CHARACTERISTIC = 1;
/*  16:    */   public static final short CATEGORY_DATA_TYPE_ALL_TEXT_CHARACTERISTIC = 2;
/*  17:    */   
/*  18:    */   public DefaultDataLabelTextPropertiesRecord() {}
/*  19:    */   
/*  20:    */   public DefaultDataLabelTextPropertiesRecord(RecordInputStream in)
/*  21:    */   {
/*  22: 43 */     this.field_1_categoryDataType = in.readShort();
/*  23:    */   }
/*  24:    */   
/*  25:    */   public String toString()
/*  26:    */   {
/*  27: 48 */     StringBuffer buffer = new StringBuffer();
/*  28:    */     
/*  29: 50 */     buffer.append("[DEFAULTTEXT]\n");
/*  30: 51 */     buffer.append("    .categoryDataType     = ").append("0x").append(HexDump.toHex(getCategoryDataType())).append(" (").append(getCategoryDataType()).append(" )");
/*  31:    */     
/*  32:    */ 
/*  33: 54 */     buffer.append(System.getProperty("line.separator"));
/*  34:    */     
/*  35: 56 */     buffer.append("[/DEFAULTTEXT]\n");
/*  36: 57 */     return buffer.toString();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void serialize(LittleEndianOutput out)
/*  40:    */   {
/*  41: 61 */     out.writeShort(this.field_1_categoryDataType);
/*  42:    */   }
/*  43:    */   
/*  44:    */   protected int getDataSize()
/*  45:    */   {
/*  46: 65 */     return 2;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public short getSid()
/*  50:    */   {
/*  51: 70 */     return 4132;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public DefaultDataLabelTextPropertiesRecord clone()
/*  55:    */   {
/*  56: 75 */     DefaultDataLabelTextPropertiesRecord rec = new DefaultDataLabelTextPropertiesRecord();
/*  57:    */     
/*  58: 77 */     rec.field_1_categoryDataType = this.field_1_categoryDataType;
/*  59: 78 */     return rec;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public short getCategoryDataType()
/*  63:    */   {
/*  64: 94 */     return this.field_1_categoryDataType;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setCategoryDataType(short field_1_categoryDataType)
/*  68:    */   {
/*  69:108 */     this.field_1_categoryDataType = field_1_categoryDataType;
/*  70:    */   }
/*  71:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.DefaultDataLabelTextPropertiesRecord
 * JD-Core Version:    0.7.0.1
 */