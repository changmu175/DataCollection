/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.HexDump;
/*   4:    */ import org.apache.poi.util.LittleEndianInput;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ import org.apache.poi.util.RecordFormatException;
/*   7:    */ 
/*   8:    */ public final class FtCblsSubRecord
/*   9:    */   extends SubRecord
/*  10:    */   implements Cloneable
/*  11:    */ {
/*  12:    */   public static final short sid = 12;
/*  13:    */   private static final int ENCODED_SIZE = 20;
/*  14:    */   private byte[] reserved;
/*  15:    */   
/*  16:    */   public FtCblsSubRecord()
/*  17:    */   {
/*  18: 43 */     this.reserved = new byte[20];
/*  19:    */   }
/*  20:    */   
/*  21:    */   public FtCblsSubRecord(LittleEndianInput in, int size)
/*  22:    */   {
/*  23: 47 */     if (size != 20) {
/*  24: 48 */       throw new RecordFormatException("Unexpected size (" + size + ")");
/*  25:    */     }
/*  26: 51 */     byte[] buf = new byte[size];
/*  27: 52 */     in.readFully(buf);
/*  28: 53 */     this.reserved = buf;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public String toString()
/*  32:    */   {
/*  33: 62 */     StringBuffer buffer = new StringBuffer();
/*  34:    */     
/*  35: 64 */     buffer.append("[FtCbls ]").append("\n");
/*  36: 65 */     buffer.append("  size     = ").append(getDataSize()).append("\n");
/*  37: 66 */     buffer.append("  reserved = ").append(HexDump.toHex(this.reserved)).append("\n");
/*  38: 67 */     buffer.append("[/FtCbls ]").append("\n");
/*  39: 68 */     return buffer.toString();
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void serialize(LittleEndianOutput out)
/*  43:    */   {
/*  44: 77 */     out.writeShort(12);
/*  45: 78 */     out.writeShort(this.reserved.length);
/*  46: 79 */     out.write(this.reserved);
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected int getDataSize()
/*  50:    */   {
/*  51: 83 */     return this.reserved.length;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public short getSid()
/*  55:    */   {
/*  56: 91 */     return 12;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public FtCblsSubRecord clone()
/*  60:    */   {
/*  61: 96 */     FtCblsSubRecord rec = new FtCblsSubRecord();
/*  62: 97 */     byte[] recdata = new byte[this.reserved.length];
/*  63: 98 */     System.arraycopy(this.reserved, 0, recdata, 0, recdata.length);
/*  64: 99 */     rec.reserved = recdata;
/*  65:100 */     return rec;
/*  66:    */   }
/*  67:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.FtCblsSubRecord
 * JD-Core Version:    0.7.0.1
 */