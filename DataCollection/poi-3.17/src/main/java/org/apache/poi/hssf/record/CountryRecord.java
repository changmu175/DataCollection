/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndianOutput;
/*   4:    */ 
/*   5:    */ public final class CountryRecord
/*   6:    */   extends StandardRecord
/*   7:    */ {
/*   8:    */   public static final short sid = 140;
/*   9:    */   private short field_1_default_country;
/*  10:    */   private short field_2_current_country;
/*  11:    */   
/*  12:    */   public CountryRecord() {}
/*  13:    */   
/*  14:    */   public CountryRecord(RecordInputStream in)
/*  15:    */   {
/*  16: 49 */     this.field_1_default_country = in.readShort();
/*  17: 50 */     this.field_2_current_country = in.readShort();
/*  18:    */   }
/*  19:    */   
/*  20:    */   public void setDefaultCountry(short country)
/*  21:    */   {
/*  22: 61 */     this.field_1_default_country = country;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void setCurrentCountry(short country)
/*  26:    */   {
/*  27: 72 */     this.field_2_current_country = country;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public short getDefaultCountry()
/*  31:    */   {
/*  32: 83 */     return this.field_1_default_country;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public short getCurrentCountry()
/*  36:    */   {
/*  37: 94 */     return this.field_2_current_country;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public String toString()
/*  41:    */   {
/*  42: 99 */     StringBuffer buffer = new StringBuffer();
/*  43:    */     
/*  44:101 */     buffer.append("[COUNTRY]\n");
/*  45:102 */     buffer.append("    .defaultcountry  = ").append(Integer.toHexString(getDefaultCountry())).append("\n");
/*  46:    */     
/*  47:104 */     buffer.append("    .currentcountry  = ").append(Integer.toHexString(getCurrentCountry())).append("\n");
/*  48:    */     
/*  49:106 */     buffer.append("[/COUNTRY]\n");
/*  50:107 */     return buffer.toString();
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void serialize(LittleEndianOutput out)
/*  54:    */   {
/*  55:111 */     out.writeShort(getDefaultCountry());
/*  56:112 */     out.writeShort(getCurrentCountry());
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected int getDataSize()
/*  60:    */   {
/*  61:116 */     return 4;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public short getSid()
/*  65:    */   {
/*  66:121 */     return 140;
/*  67:    */   }
/*  68:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.CountryRecord
 * JD-Core Version:    0.7.0.1
 */