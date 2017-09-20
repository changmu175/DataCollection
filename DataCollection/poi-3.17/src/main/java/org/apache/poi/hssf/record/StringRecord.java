/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.cont.ContinuableRecord;
/*  4:   */ import org.apache.poi.hssf.record.cont.ContinuableRecordOutput;
/*  5:   */ import org.apache.poi.util.StringUtil;
/*  6:   */ 
/*  7:   */ public final class StringRecord
/*  8:   */   extends ContinuableRecord
/*  9:   */ {
/* 10:   */   public static final short sid = 519;
/* 11:   */   private boolean _is16bitUnicode;
/* 12:   */   private String _text;
/* 13:   */   
/* 14:   */   public StringRecord() {}
/* 15:   */   
/* 16:   */   public StringRecord(RecordInputStream in)
/* 17:   */   {
/* 18:45 */     int field_1_string_length = in.readUShort();
/* 19:46 */     this._is16bitUnicode = (in.readByte() != 0);
/* 20:48 */     if (this._is16bitUnicode) {
/* 21:49 */       this._text = in.readUnicodeLEString(field_1_string_length);
/* 22:   */     } else {
/* 23:51 */       this._text = in.readCompressedUnicode(field_1_string_length);
/* 24:   */     }
/* 25:   */   }
/* 26:   */   
/* 27:   */   protected void serialize(ContinuableRecordOutput out)
/* 28:   */   {
/* 29:57 */     out.writeShort(this._text.length());
/* 30:58 */     out.writeStringData(this._text);
/* 31:   */   }
/* 32:   */   
/* 33:   */   public short getSid()
/* 34:   */   {
/* 35:64 */     return 519;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public String getString()
/* 39:   */   {
/* 40:72 */     return this._text;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public void setString(String string)
/* 44:   */   {
/* 45:80 */     this._text = string;
/* 46:81 */     this._is16bitUnicode = StringUtil.hasMultibyte(string);
/* 47:   */   }
/* 48:   */   
/* 49:   */   public String toString()
/* 50:   */   {
/* 51:86 */     StringBuffer buffer = new StringBuffer();
/* 52:   */     
/* 53:88 */     buffer.append("[STRING]\n");
/* 54:89 */     buffer.append("    .string            = ").append(this._text).append("\n");
/* 55:   */     
/* 56:91 */     buffer.append("[/STRING]\n");
/* 57:92 */     return buffer.toString();
/* 58:   */   }
/* 59:   */   
/* 60:   */   public Object clone()
/* 61:   */   {
/* 62:96 */     StringRecord rec = new StringRecord();
/* 63:97 */     rec._is16bitUnicode = this._is16bitUnicode;
/* 64:98 */     rec._text = this._text;
/* 65:99 */     return rec;
/* 66:   */   }
/* 67:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.StringRecord
 * JD-Core Version:    0.7.0.1
 */