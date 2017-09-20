/*  1:   */ package org.apache.poi.hssf.record.pivottable;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.RecordInputStream;
/*  4:   */ import org.apache.poi.hssf.record.StandardRecord;
/*  5:   */ import org.apache.poi.util.HexDump;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ import org.apache.poi.util.StringUtil;
/*  8:   */ 
/*  9:   */ public final class DataItemRecord
/* 10:   */   extends StandardRecord
/* 11:   */ {
/* 12:   */   public static final short sid = 197;
/* 13:   */   private int isxvdData;
/* 14:   */   private int iiftab;
/* 15:   */   private int df;
/* 16:   */   private int isxvd;
/* 17:   */   private int isxvi;
/* 18:   */   private int ifmt;
/* 19:   */   private String name;
/* 20:   */   
/* 21:   */   public DataItemRecord(RecordInputStream in)
/* 22:   */   {
/* 23:43 */     this.isxvdData = in.readUShort();
/* 24:44 */     this.iiftab = in.readUShort();
/* 25:45 */     this.df = in.readUShort();
/* 26:46 */     this.isxvd = in.readUShort();
/* 27:47 */     this.isxvi = in.readUShort();
/* 28:48 */     this.ifmt = in.readUShort();
/* 29:   */     
/* 30:50 */     this.name = in.readString();
/* 31:   */   }
/* 32:   */   
/* 33:   */   protected void serialize(LittleEndianOutput out)
/* 34:   */   {
/* 35:56 */     out.writeShort(this.isxvdData);
/* 36:57 */     out.writeShort(this.iiftab);
/* 37:58 */     out.writeShort(this.df);
/* 38:59 */     out.writeShort(this.isxvd);
/* 39:60 */     out.writeShort(this.isxvi);
/* 40:61 */     out.writeShort(this.ifmt);
/* 41:   */     
/* 42:63 */     StringUtil.writeUnicodeString(out, this.name);
/* 43:   */   }
/* 44:   */   
/* 45:   */   protected int getDataSize()
/* 46:   */   {
/* 47:68 */     return 12 + StringUtil.getEncodedSize(this.name);
/* 48:   */   }
/* 49:   */   
/* 50:   */   public short getSid()
/* 51:   */   {
/* 52:73 */     return 197;
/* 53:   */   }
/* 54:   */   
/* 55:   */   public String toString()
/* 56:   */   {
/* 57:78 */     StringBuffer buffer = new StringBuffer();
/* 58:   */     
/* 59:80 */     buffer.append("[SXDI]\n");
/* 60:81 */     buffer.append("  .isxvdData = ").append(HexDump.shortToHex(this.isxvdData)).append("\n");
/* 61:82 */     buffer.append("  .iiftab = ").append(HexDump.shortToHex(this.iiftab)).append("\n");
/* 62:83 */     buffer.append("  .df = ").append(HexDump.shortToHex(this.df)).append("\n");
/* 63:84 */     buffer.append("  .isxvd = ").append(HexDump.shortToHex(this.isxvd)).append("\n");
/* 64:85 */     buffer.append("  .isxvi = ").append(HexDump.shortToHex(this.isxvi)).append("\n");
/* 65:86 */     buffer.append("  .ifmt = ").append(HexDump.shortToHex(this.ifmt)).append("\n");
/* 66:87 */     buffer.append("[/SXDI]\n");
/* 67:88 */     return buffer.toString();
/* 68:   */   }
/* 69:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.pivottable.DataItemRecord
 * JD-Core Version:    0.7.0.1
 */