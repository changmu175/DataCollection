/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.HexDump;
/*   4:    */ import org.apache.poi.util.LittleEndianInput;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ import org.apache.poi.util.RecordFormatException;
/*   7:    */ 
/*   8:    */ public final class NoteStructureSubRecord
/*   9:    */   extends SubRecord
/*  10:    */   implements Cloneable
/*  11:    */ {
/*  12:    */   public static final short sid = 13;
/*  13:    */   private static final int ENCODED_SIZE = 22;
/*  14:    */   private byte[] reserved;
/*  15:    */   
/*  16:    */   public NoteStructureSubRecord()
/*  17:    */   {
/*  18: 44 */     this.reserved = new byte[22];
/*  19:    */   }
/*  20:    */   
/*  21:    */   public NoteStructureSubRecord(LittleEndianInput in, int size)
/*  22:    */   {
/*  23: 54 */     if (size != 22) {
/*  24: 55 */       throw new RecordFormatException("Unexpected size (" + size + ")");
/*  25:    */     }
/*  26: 58 */     byte[] buf = new byte[size];
/*  27: 59 */     in.readFully(buf);
/*  28: 60 */     this.reserved = buf;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public String toString()
/*  32:    */   {
/*  33: 70 */     StringBuffer buffer = new StringBuffer();
/*  34:    */     
/*  35: 72 */     buffer.append("[ftNts ]").append("\n");
/*  36: 73 */     buffer.append("  size     = ").append(getDataSize()).append("\n");
/*  37: 74 */     buffer.append("  reserved = ").append(HexDump.toHex(this.reserved)).append("\n");
/*  38: 75 */     buffer.append("[/ftNts ]").append("\n");
/*  39: 76 */     return buffer.toString();
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void serialize(LittleEndianOutput out)
/*  43:    */   {
/*  44: 86 */     out.writeShort(13);
/*  45: 87 */     out.writeShort(this.reserved.length);
/*  46: 88 */     out.write(this.reserved);
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected int getDataSize()
/*  50:    */   {
/*  51: 93 */     return this.reserved.length;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public short getSid()
/*  55:    */   {
/*  56:101 */     return 13;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public NoteStructureSubRecord clone()
/*  60:    */   {
/*  61:106 */     NoteStructureSubRecord rec = new NoteStructureSubRecord();
/*  62:107 */     byte[] recdata = new byte[this.reserved.length];
/*  63:108 */     System.arraycopy(this.reserved, 0, recdata, 0, recdata.length);
/*  64:109 */     rec.reserved = recdata;
/*  65:110 */     return rec;
/*  66:    */   }
/*  67:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.NoteStructureSubRecord
 * JD-Core Version:    0.7.0.1
 */