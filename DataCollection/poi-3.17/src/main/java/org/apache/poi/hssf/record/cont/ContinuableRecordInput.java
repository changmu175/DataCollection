/*   1:    */ package org.apache.poi.hssf.record.cont;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.util.LittleEndianInput;
/*   5:    */ 
/*   6:    */ public class ContinuableRecordInput
/*   7:    */   implements LittleEndianInput
/*   8:    */ {
/*   9:    */   private final RecordInputStream _in;
/*  10:    */   
/*  11:    */   public ContinuableRecordInput(RecordInputStream in)
/*  12:    */   {
/*  13: 55 */     this._in = in;
/*  14:    */   }
/*  15:    */   
/*  16:    */   public int available()
/*  17:    */   {
/*  18: 59 */     return this._in.available();
/*  19:    */   }
/*  20:    */   
/*  21:    */   public byte readByte()
/*  22:    */   {
/*  23: 64 */     return this._in.readByte();
/*  24:    */   }
/*  25:    */   
/*  26:    */   public int readUByte()
/*  27:    */   {
/*  28: 69 */     return this._in.readUByte();
/*  29:    */   }
/*  30:    */   
/*  31:    */   public short readShort()
/*  32:    */   {
/*  33: 74 */     return this._in.readShort();
/*  34:    */   }
/*  35:    */   
/*  36:    */   public int readUShort()
/*  37:    */   {
/*  38: 79 */     int ch1 = readUByte();
/*  39: 80 */     int ch2 = readUByte();
/*  40: 81 */     return (ch2 << 8) + (ch1 << 0);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public int readInt()
/*  44:    */   {
/*  45: 86 */     int ch1 = this._in.readUByte();
/*  46: 87 */     int ch2 = this._in.readUByte();
/*  47: 88 */     int ch3 = this._in.readUByte();
/*  48: 89 */     int ch4 = this._in.readUByte();
/*  49: 90 */     return (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public long readLong()
/*  53:    */   {
/*  54: 95 */     int b0 = this._in.readUByte();
/*  55: 96 */     int b1 = this._in.readUByte();
/*  56: 97 */     int b2 = this._in.readUByte();
/*  57: 98 */     int b3 = this._in.readUByte();
/*  58: 99 */     int b4 = this._in.readUByte();
/*  59:100 */     int b5 = this._in.readUByte();
/*  60:101 */     int b6 = this._in.readUByte();
/*  61:102 */     int b7 = this._in.readUByte();
/*  62:103 */     return (b7 << 56) + (b6 << 48) + (b5 << 40) + (b4 << 32) + (b3 << 24) + (b2 << 16) + (b1 << 8) + (b0 << 0);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public double readDouble()
/*  66:    */   {
/*  67:115 */     return this._in.readDouble();
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void readFully(byte[] buf)
/*  71:    */   {
/*  72:120 */     this._in.readFully(buf);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void readFully(byte[] buf, int off, int len)
/*  76:    */   {
/*  77:125 */     this._in.readFully(buf, off, len);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void readPlain(byte[] buf, int off, int len)
/*  81:    */   {
/*  82:130 */     readFully(buf, off, len);
/*  83:    */   }
/*  84:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.cont.ContinuableRecordInput
 * JD-Core Version:    0.7.0.1
 */