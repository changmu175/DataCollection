/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import java.io.UnsupportedEncodingException;
/*  4:   */ import org.apache.poi.util.CodePageUtil;
/*  5:   */ 
/*  6:   */ public final class OldStringRecord
/*  7:   */ {
/*  8:   */   public static final short biff2_sid = 7;
/*  9:   */   public static final short biff345_sid = 519;
/* 10:   */   private short sid;
/* 11:   */   private short field_1_string_len;
/* 12:   */   private byte[] field_2_bytes;
/* 13:   */   private CodepageRecord codepage;
/* 14:   */   
/* 15:   */   public OldStringRecord(RecordInputStream in)
/* 16:   */   {
/* 17:43 */     this.sid = in.getSid();
/* 18:45 */     if (in.getSid() == 7) {
/* 19:46 */       this.field_1_string_len = ((short)in.readUByte());
/* 20:   */     } else {
/* 21:48 */       this.field_1_string_len = in.readShort();
/* 22:   */     }
/* 23:52 */     this.field_2_bytes = new byte[this.field_1_string_len];
/* 24:53 */     in.read(this.field_2_bytes, 0, this.field_1_string_len);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public boolean isBiff2()
/* 28:   */   {
/* 29:57 */     return this.sid == 7;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public short getSid()
/* 33:   */   {
/* 34:61 */     return this.sid;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public void setCodePage(CodepageRecord codepage)
/* 38:   */   {
/* 39:65 */     this.codepage = codepage;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public String getString()
/* 43:   */   {
/* 44:73 */     return getString(this.field_2_bytes, this.codepage);
/* 45:   */   }
/* 46:   */   
/* 47:   */   protected static String getString(byte[] data, CodepageRecord codepage)
/* 48:   */   {
/* 49:77 */     int cp = 1252;
/* 50:78 */     if (codepage != null) {
/* 51:79 */       cp = codepage.getCodepage() & 0xFFFF;
/* 52:   */     }
/* 53:   */     try
/* 54:   */     {
/* 55:82 */       return CodePageUtil.getStringFromCodePage(data, cp);
/* 56:   */     }
/* 57:   */     catch (UnsupportedEncodingException uee)
/* 58:   */     {
/* 59:84 */       throw new IllegalArgumentException("Unsupported codepage requested", uee);
/* 60:   */     }
/* 61:   */   }
/* 62:   */   
/* 63:   */   public String toString()
/* 64:   */   {
/* 65:90 */     StringBuffer buffer = new StringBuffer();
/* 66:   */     
/* 67:92 */     buffer.append("[OLD STRING]\n");
/* 68:93 */     buffer.append("    .string            = ").append(getString()).append("\n");
/* 69:   */     
/* 70:95 */     buffer.append("[/OLD STRING]\n");
/* 71:96 */     return buffer.toString();
/* 72:   */   }
/* 73:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.OldStringRecord
 * JD-Core Version:    0.7.0.1
 */