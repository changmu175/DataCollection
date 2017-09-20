/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.HexDump;
/*  4:   */ import org.apache.poi.util.LittleEndianInput;
/*  5:   */ import org.apache.poi.util.LittleEndianOutput;
/*  6:   */ 
/*  7:   */ public final class GroupMarkerSubRecord
/*  8:   */   extends SubRecord
/*  9:   */   implements Cloneable
/* 10:   */ {
/* 11:   */   public static final short sid = 6;
/* 12:31 */   private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/* 13:   */   private byte[] reserved;
/* 14:   */   
/* 15:   */   public GroupMarkerSubRecord()
/* 16:   */   {
/* 17:37 */     this.reserved = EMPTY_BYTE_ARRAY;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public GroupMarkerSubRecord(LittleEndianInput in, int size)
/* 21:   */   {
/* 22:41 */     byte[] buf = new byte[size];
/* 23:42 */     in.readFully(buf);
/* 24:43 */     this.reserved = buf;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public String toString()
/* 28:   */   {
/* 29:48 */     StringBuffer buffer = new StringBuffer();
/* 30:   */     
/* 31:50 */     String nl = System.getProperty("line.separator");
/* 32:51 */     buffer.append("[ftGmo]" + nl);
/* 33:52 */     buffer.append("  reserved = ").append(HexDump.toHex(this.reserved)).append(nl);
/* 34:53 */     buffer.append("[/ftGmo]" + nl);
/* 35:54 */     return buffer.toString();
/* 36:   */   }
/* 37:   */   
/* 38:   */   public void serialize(LittleEndianOutput out)
/* 39:   */   {
/* 40:58 */     out.writeShort(6);
/* 41:59 */     out.writeShort(this.reserved.length);
/* 42:60 */     out.write(this.reserved);
/* 43:   */   }
/* 44:   */   
/* 45:   */   protected int getDataSize()
/* 46:   */   {
/* 47:64 */     return this.reserved.length;
/* 48:   */   }
/* 49:   */   
/* 50:   */   public short getSid()
/* 51:   */   {
/* 52:69 */     return 6;
/* 53:   */   }
/* 54:   */   
/* 55:   */   public GroupMarkerSubRecord clone()
/* 56:   */   {
/* 57:74 */     GroupMarkerSubRecord rec = new GroupMarkerSubRecord();
/* 58:75 */     rec.reserved = new byte[this.reserved.length];
/* 59:76 */     for (int i = 0; i < this.reserved.length; i++) {
/* 60:77 */       rec.reserved[i] = this.reserved[i];
/* 61:   */     }
/* 62:78 */     return rec;
/* 63:   */   }
/* 64:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.GroupMarkerSubRecord
 * JD-Core Version:    0.7.0.1
 */