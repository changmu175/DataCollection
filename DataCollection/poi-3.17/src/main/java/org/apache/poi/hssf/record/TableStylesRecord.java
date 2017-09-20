/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.HexDump;
/*  4:   */ import org.apache.poi.util.LittleEndianOutput;
/*  5:   */ import org.apache.poi.util.StringUtil;
/*  6:   */ 
/*  7:   */ public final class TableStylesRecord
/*  8:   */   extends StandardRecord
/*  9:   */ {
/* 10:   */   public static final short sid = 2190;
/* 11:   */   private int rt;
/* 12:   */   private int grbitFrt;
/* 13:32 */   private byte[] unused = new byte[8];
/* 14:   */   private int cts;
/* 15:   */   private String rgchDefListStyle;
/* 16:   */   private String rgchDefPivotStyle;
/* 17:   */   
/* 18:   */   public TableStylesRecord(RecordInputStream in)
/* 19:   */   {
/* 20:40 */     this.rt = in.readUShort();
/* 21:41 */     this.grbitFrt = in.readUShort();
/* 22:42 */     in.readFully(this.unused);
/* 23:43 */     this.cts = in.readInt();
/* 24:44 */     int cchDefListStyle = in.readUShort();
/* 25:45 */     int cchDefPivotStyle = in.readUShort();
/* 26:   */     
/* 27:47 */     this.rgchDefListStyle = in.readUnicodeLEString(cchDefListStyle);
/* 28:48 */     this.rgchDefPivotStyle = in.readUnicodeLEString(cchDefPivotStyle);
/* 29:   */   }
/* 30:   */   
/* 31:   */   protected void serialize(LittleEndianOutput out)
/* 32:   */   {
/* 33:53 */     out.writeShort(this.rt);
/* 34:54 */     out.writeShort(this.grbitFrt);
/* 35:55 */     out.write(this.unused);
/* 36:56 */     out.writeInt(this.cts);
/* 37:   */     
/* 38:58 */     out.writeShort(this.rgchDefListStyle.length());
/* 39:59 */     out.writeShort(this.rgchDefPivotStyle.length());
/* 40:   */     
/* 41:61 */     StringUtil.putUnicodeLE(this.rgchDefListStyle, out);
/* 42:62 */     StringUtil.putUnicodeLE(this.rgchDefPivotStyle, out);
/* 43:   */   }
/* 44:   */   
/* 45:   */   protected int getDataSize()
/* 46:   */   {
/* 47:67 */     return 20 + 2 * this.rgchDefListStyle.length() + 2 * this.rgchDefPivotStyle.length();
/* 48:   */   }
/* 49:   */   
/* 50:   */   public short getSid()
/* 51:   */   {
/* 52:73 */     return 2190;
/* 53:   */   }
/* 54:   */   
/* 55:   */   public String toString()
/* 56:   */   {
/* 57:79 */     StringBuffer buffer = new StringBuffer();
/* 58:   */     
/* 59:81 */     buffer.append("[TABLESTYLES]\n");
/* 60:82 */     buffer.append("    .rt      =").append(HexDump.shortToHex(this.rt)).append('\n');
/* 61:83 */     buffer.append("    .grbitFrt=").append(HexDump.shortToHex(this.grbitFrt)).append('\n');
/* 62:84 */     buffer.append("    .unused  =").append(HexDump.toHex(this.unused)).append('\n');
/* 63:85 */     buffer.append("    .cts=").append(HexDump.intToHex(this.cts)).append('\n');
/* 64:86 */     buffer.append("    .rgchDefListStyle=").append(this.rgchDefListStyle).append('\n');
/* 65:87 */     buffer.append("    .rgchDefPivotStyle=").append(this.rgchDefPivotStyle).append('\n');
/* 66:   */     
/* 67:89 */     buffer.append("[/TABLESTYLES]\n");
/* 68:90 */     return buffer.toString();
/* 69:   */   }
/* 70:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.TableStylesRecord
 * JD-Core Version:    0.7.0.1
 */