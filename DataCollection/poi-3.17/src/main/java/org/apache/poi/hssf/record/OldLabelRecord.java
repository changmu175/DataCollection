/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.HexDump;
/*   4:    */ import org.apache.poi.util.POILogFactory;
/*   5:    */ import org.apache.poi.util.POILogger;
/*   6:    */ import org.apache.poi.util.RecordFormatException;
/*   7:    */ 
/*   8:    */ public final class OldLabelRecord
/*   9:    */   extends OldCellRecord
/*  10:    */ {
/*  11: 31 */   private static final POILogger logger = POILogFactory.getLogger(OldLabelRecord.class);
/*  12:    */   public static final short biff2_sid = 4;
/*  13:    */   public static final short biff345_sid = 516;
/*  14:    */   private short field_4_string_len;
/*  15:    */   private final byte[] field_5_bytes;
/*  16:    */   private CodepageRecord codepage;
/*  17:    */   
/*  18:    */   public OldLabelRecord(RecordInputStream in)
/*  19:    */   {
/*  20: 45 */     super(in, in.getSid() == 4);
/*  21: 47 */     if (isBiff2()) {
/*  22: 48 */       this.field_4_string_len = ((short)in.readUByte());
/*  23:    */     } else {
/*  24: 50 */       this.field_4_string_len = in.readShort();
/*  25:    */     }
/*  26: 54 */     this.field_5_bytes = new byte[this.field_4_string_len];
/*  27: 55 */     in.read(this.field_5_bytes, 0, this.field_4_string_len);
/*  28: 57 */     if (in.remaining() > 0) {
/*  29: 58 */       logger.log(3, new Object[] { "LabelRecord data remains: " + in.remaining() + " : " + HexDump.toHex(in.readRemainder()) });
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setCodePage(CodepageRecord codepage)
/*  34:    */   {
/*  35: 66 */     this.codepage = codepage;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public short getStringLength()
/*  39:    */   {
/*  40: 75 */     return this.field_4_string_len;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public String getValue()
/*  44:    */   {
/*  45: 85 */     return OldStringRecord.getString(this.field_5_bytes, this.codepage);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public int serialize(int offset, byte[] data)
/*  49:    */   {
/*  50: 96 */     throw new RecordFormatException("Old Label Records are supported READ ONLY");
/*  51:    */   }
/*  52:    */   
/*  53:    */   public int getRecordSize()
/*  54:    */   {
/*  55:100 */     throw new RecordFormatException("Old Label Records are supported READ ONLY");
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected void appendValueText(StringBuilder sb)
/*  59:    */   {
/*  60:105 */     sb.append("    .string_len= ").append(HexDump.shortToHex(this.field_4_string_len)).append("\n");
/*  61:106 */     sb.append("    .value       = ").append(getValue()).append("\n");
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected String getRecordName()
/*  65:    */   {
/*  66:111 */     return "OLD LABEL";
/*  67:    */   }
/*  68:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.OldLabelRecord
 * JD-Core Version:    0.7.0.1
 */