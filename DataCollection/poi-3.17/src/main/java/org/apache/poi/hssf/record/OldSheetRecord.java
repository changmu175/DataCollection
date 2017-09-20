/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.HexDump;
/*  4:   */ 
/*  5:   */ public final class OldSheetRecord
/*  6:   */ {
/*  7:   */   public static final short sid = 133;
/*  8:   */   private int field_1_position_of_BOF;
/*  9:   */   private int field_2_visibility;
/* 10:   */   private int field_3_type;
/* 11:   */   private byte[] field_5_sheetname;
/* 12:   */   private CodepageRecord codepage;
/* 13:   */   
/* 14:   */   public OldSheetRecord(RecordInputStream in)
/* 15:   */   {
/* 16:38 */     this.field_1_position_of_BOF = in.readInt();
/* 17:39 */     this.field_2_visibility = in.readUByte();
/* 18:40 */     this.field_3_type = in.readUByte();
/* 19:41 */     int field_4_sheetname_length = in.readUByte();
/* 20:42 */     this.field_5_sheetname = new byte[field_4_sheetname_length];
/* 21:43 */     in.read(this.field_5_sheetname, 0, field_4_sheetname_length);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void setCodePage(CodepageRecord codepage)
/* 25:   */   {
/* 26:47 */     this.codepage = codepage;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public short getSid()
/* 30:   */   {
/* 31:51 */     return 133;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public int getPositionOfBof()
/* 35:   */   {
/* 36:60 */     return this.field_1_position_of_BOF;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public String getSheetname()
/* 40:   */   {
/* 41:68 */     return OldStringRecord.getString(this.field_5_sheetname, this.codepage);
/* 42:   */   }
/* 43:   */   
/* 44:   */   public String toString()
/* 45:   */   {
/* 46:72 */     StringBuffer buffer = new StringBuffer();
/* 47:   */     
/* 48:74 */     buffer.append("[BOUNDSHEET]\n");
/* 49:75 */     buffer.append("    .bof        = ").append(HexDump.intToHex(getPositionOfBof())).append("\n");
/* 50:76 */     buffer.append("    .visibility = ").append(HexDump.shortToHex(this.field_2_visibility)).append("\n");
/* 51:77 */     buffer.append("    .type       = ").append(HexDump.byteToHex(this.field_3_type)).append("\n");
/* 52:78 */     buffer.append("    .sheetname  = ").append(getSheetname()).append("\n");
/* 53:79 */     buffer.append("[/BOUNDSHEET]\n");
/* 54:80 */     return buffer.toString();
/* 55:   */   }
/* 56:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.OldSheetRecord
 * JD-Core Version:    0.7.0.1
 */