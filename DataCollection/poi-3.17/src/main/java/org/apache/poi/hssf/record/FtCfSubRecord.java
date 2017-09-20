/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.HexDump;
/*   4:    */ import org.apache.poi.util.LittleEndianInput;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ import org.apache.poi.util.RecordFormatException;
/*   7:    */ 
/*   8:    */ public final class FtCfSubRecord
/*   9:    */   extends SubRecord
/*  10:    */   implements Cloneable
/*  11:    */ {
/*  12:    */   public static final short sid = 7;
/*  13:    */   public static final short length = 2;
/*  14:    */   public static final short METAFILE_BIT = 2;
/*  15:    */   public static final short BITMAP_BIT = 9;
/*  16:    */   public static final short UNSPECIFIED_BIT = -1;
/*  17: 49 */   private short flags = 0;
/*  18:    */   
/*  19:    */   public FtCfSubRecord() {}
/*  20:    */   
/*  21:    */   public FtCfSubRecord(LittleEndianInput in, int size)
/*  22:    */   {
/*  23: 59 */     if (size != 2) {
/*  24: 60 */       throw new RecordFormatException("Unexpected size (" + size + ")");
/*  25:    */     }
/*  26: 62 */     this.flags = in.readShort();
/*  27:    */   }
/*  28:    */   
/*  29:    */   public String toString()
/*  30:    */   {
/*  31: 70 */     StringBuffer buffer = new StringBuffer();
/*  32: 71 */     buffer.append("[FtCf ]\n");
/*  33: 72 */     buffer.append("  size     = ").append(2).append("\n");
/*  34: 73 */     buffer.append("  flags    = ").append(HexDump.toHex(this.flags)).append("\n");
/*  35: 74 */     buffer.append("[/FtCf ]\n");
/*  36: 75 */     return buffer.toString();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void serialize(LittleEndianOutput out)
/*  40:    */   {
/*  41: 84 */     out.writeShort(7);
/*  42: 85 */     out.writeShort(2);
/*  43: 86 */     out.writeShort(this.flags);
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected int getDataSize()
/*  47:    */   {
/*  48: 90 */     return 2;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public short getSid()
/*  52:    */   {
/*  53: 98 */     return 7;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public FtCfSubRecord clone()
/*  57:    */   {
/*  58:103 */     FtCfSubRecord rec = new FtCfSubRecord();
/*  59:104 */     rec.flags = this.flags;
/*  60:105 */     return rec;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public short getFlags()
/*  64:    */   {
/*  65:109 */     return this.flags;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setFlags(short flags)
/*  69:    */   {
/*  70:113 */     this.flags = flags;
/*  71:    */   }
/*  72:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.FtCfSubRecord
 * JD-Core Version:    0.7.0.1
 */