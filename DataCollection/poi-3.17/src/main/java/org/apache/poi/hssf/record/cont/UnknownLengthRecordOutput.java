/*   1:    */ package org.apache.poi.hssf.record.cont;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.DelayableLittleEndianOutput;
/*   4:    */ import org.apache.poi.util.LittleEndianByteArrayOutputStream;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ 
/*   7:    */ final class UnknownLengthRecordOutput
/*   8:    */   implements LittleEndianOutput
/*   9:    */ {
/*  10:    */   private static final int MAX_DATA_SIZE = 8224;
/*  11:    */   private final LittleEndianOutput _originalOut;
/*  12:    */   private final LittleEndianOutput _dataSizeOutput;
/*  13:    */   private final byte[] _byteBuffer;
/*  14:    */   private LittleEndianOutput _out;
/*  15:    */   private int _size;
/*  16:    */   
/*  17:    */   public UnknownLengthRecordOutput(LittleEndianOutput out, int sid)
/*  18:    */   {
/*  19: 40 */     this._originalOut = out;
/*  20: 41 */     out.writeShort(sid);
/*  21: 42 */     if ((out instanceof DelayableLittleEndianOutput))
/*  22:    */     {
/*  23: 44 */       DelayableLittleEndianOutput dleo = (DelayableLittleEndianOutput)out;
/*  24: 45 */       this._dataSizeOutput = dleo.createDelayedOutput(2);
/*  25: 46 */       this._byteBuffer = null;
/*  26: 47 */       this._out = out;
/*  27:    */     }
/*  28:    */     else
/*  29:    */     {
/*  30: 50 */       this._dataSizeOutput = out;
/*  31: 51 */       this._byteBuffer = new byte[8224];
/*  32: 52 */       this._out = new LittleEndianByteArrayOutputStream(this._byteBuffer, 0);
/*  33:    */     }
/*  34:    */   }
/*  35:    */   
/*  36:    */   public int getTotalSize()
/*  37:    */   {
/*  38: 59 */     return 4 + this._size;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public int getAvailableSpace()
/*  42:    */   {
/*  43: 62 */     if (this._out == null) {
/*  44: 63 */       throw new IllegalStateException("Record already terminated");
/*  45:    */     }
/*  46: 65 */     return 8224 - this._size;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void terminate()
/*  50:    */   {
/*  51: 72 */     if (this._out == null) {
/*  52: 73 */       throw new IllegalStateException("Record already terminated");
/*  53:    */     }
/*  54: 75 */     this._dataSizeOutput.writeShort(this._size);
/*  55: 76 */     if (this._byteBuffer != null)
/*  56:    */     {
/*  57: 77 */       this._originalOut.write(this._byteBuffer, 0, this._size);
/*  58: 78 */       this._out = null;
/*  59: 79 */       return;
/*  60:    */     }
/*  61: 81 */     this._out = null;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void write(byte[] b)
/*  65:    */   {
/*  66: 85 */     this._out.write(b);
/*  67: 86 */     this._size += b.length;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void write(byte[] b, int offset, int len)
/*  71:    */   {
/*  72: 89 */     this._out.write(b, offset, len);
/*  73: 90 */     this._size += len;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void writeByte(int v)
/*  77:    */   {
/*  78: 93 */     this._out.writeByte(v);
/*  79: 94 */     this._size += 1;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void writeDouble(double v)
/*  83:    */   {
/*  84: 97 */     this._out.writeDouble(v);
/*  85: 98 */     this._size += 8;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void writeInt(int v)
/*  89:    */   {
/*  90:101 */     this._out.writeInt(v);
/*  91:102 */     this._size += 4;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void writeLong(long v)
/*  95:    */   {
/*  96:105 */     this._out.writeLong(v);
/*  97:106 */     this._size += 8;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void writeShort(int v)
/* 101:    */   {
/* 102:109 */     this._out.writeShort(v);
/* 103:110 */     this._size += 2;
/* 104:    */   }
/* 105:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.cont.UnknownLengthRecordOutput
 * JD-Core Version:    0.7.0.1
 */