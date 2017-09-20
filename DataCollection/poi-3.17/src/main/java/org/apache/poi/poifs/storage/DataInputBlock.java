/*   1:    */ package org.apache.poi.poifs.storage;
/*   2:    */ 
/*   3:    */ public final class DataInputBlock
/*   4:    */ {
/*   5:    */   private final byte[] _buf;
/*   6:    */   private int _readIndex;
/*   7:    */   private int _maxIndex;
/*   8:    */   
/*   9:    */   DataInputBlock(byte[] data, int startOffset)
/*  10:    */   {
/*  11: 51 */     this._buf = data;
/*  12: 52 */     this._readIndex = startOffset;
/*  13: 53 */     this._maxIndex = this._buf.length;
/*  14:    */   }
/*  15:    */   
/*  16:    */   public int available()
/*  17:    */   {
/*  18: 56 */     return this._maxIndex - this._readIndex;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public int readUByte()
/*  22:    */   {
/*  23: 60 */     return this._buf[(this._readIndex++)] & 0xFF;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public int readUShortLE()
/*  27:    */   {
/*  28: 67 */     int i = this._readIndex;
/*  29:    */     
/*  30: 69 */     int b0 = this._buf[(i++)] & 0xFF;
/*  31: 70 */     int b1 = this._buf[(i++)] & 0xFF;
/*  32: 71 */     this._readIndex = i;
/*  33: 72 */     return (b1 << 8) + (b0 << 0);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public int readUShortLE(DataInputBlock prevBlock)
/*  37:    */   {
/*  38: 80 */     int i = prevBlock._buf.length - 1;
/*  39:    */     
/*  40: 82 */     int b0 = prevBlock._buf[i] & 0xFF;
/*  41: 83 */     int b1 = this._buf[(this._readIndex++)] & 0xFF;
/*  42: 84 */     return (b1 << 8) + (b0 << 0);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public int readIntLE()
/*  46:    */   {
/*  47: 91 */     int i = this._readIndex;
/*  48:    */     
/*  49: 93 */     int b0 = this._buf[(i++)] & 0xFF;
/*  50: 94 */     int b1 = this._buf[(i++)] & 0xFF;
/*  51: 95 */     int b2 = this._buf[(i++)] & 0xFF;
/*  52: 96 */     int b3 = this._buf[(i++)] & 0xFF;
/*  53: 97 */     this._readIndex = i;
/*  54: 98 */     return (b3 << 24) + (b2 << 16) + (b1 << 8) + (b0 << 0);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public int readIntLE(DataInputBlock prevBlock, int prevBlockAvailable)
/*  58:    */   {
/*  59:105 */     byte[] buf = new byte[4];
/*  60:    */     
/*  61:107 */     readSpanning(prevBlock, prevBlockAvailable, buf);
/*  62:108 */     int b0 = buf[0] & 0xFF;
/*  63:109 */     int b1 = buf[1] & 0xFF;
/*  64:110 */     int b2 = buf[2] & 0xFF;
/*  65:111 */     int b3 = buf[3] & 0xFF;
/*  66:112 */     return (b3 << 24) + (b2 << 16) + (b1 << 8) + (b0 << 0);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public long readLongLE()
/*  70:    */   {
/*  71:119 */     int i = this._readIndex;
/*  72:    */     
/*  73:121 */     int b0 = this._buf[(i++)] & 0xFF;
/*  74:122 */     int b1 = this._buf[(i++)] & 0xFF;
/*  75:123 */     int b2 = this._buf[(i++)] & 0xFF;
/*  76:124 */     int b3 = this._buf[(i++)] & 0xFF;
/*  77:125 */     int b4 = this._buf[(i++)] & 0xFF;
/*  78:126 */     int b5 = this._buf[(i++)] & 0xFF;
/*  79:127 */     int b6 = this._buf[(i++)] & 0xFF;
/*  80:128 */     int b7 = this._buf[(i++)] & 0xFF;
/*  81:129 */     this._readIndex = i;
/*  82:130 */     return (b7 << 56) + (b6 << 48) + (b5 << 40) + (b4 << 32) + (b3 << 24) + (b2 << 16) + (b1 << 8) + (b0 << 0);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public long readLongLE(DataInputBlock prevBlock, int prevBlockAvailable)
/*  86:    */   {
/*  87:144 */     byte[] buf = new byte[8];
/*  88:    */     
/*  89:146 */     readSpanning(prevBlock, prevBlockAvailable, buf);
/*  90:    */     
/*  91:148 */     int b0 = buf[0] & 0xFF;
/*  92:149 */     int b1 = buf[1] & 0xFF;
/*  93:150 */     int b2 = buf[2] & 0xFF;
/*  94:151 */     int b3 = buf[3] & 0xFF;
/*  95:152 */     int b4 = buf[4] & 0xFF;
/*  96:153 */     int b5 = buf[5] & 0xFF;
/*  97:154 */     int b6 = buf[6] & 0xFF;
/*  98:155 */     int b7 = buf[7] & 0xFF;
/*  99:156 */     return (b7 << 56) + (b6 << 48) + (b5 << 40) + (b4 << 32) + (b3 << 24) + (b2 << 16) + (b1 << 8) + (b0 << 0);
/* 100:    */   }
/* 101:    */   
/* 102:    */   private void readSpanning(DataInputBlock prevBlock, int prevBlockAvailable, byte[] buf)
/* 103:    */   {
/* 104:173 */     System.arraycopy(prevBlock._buf, prevBlock._readIndex, buf, 0, prevBlockAvailable);
/* 105:174 */     int secondReadLen = buf.length - prevBlockAvailable;
/* 106:175 */     System.arraycopy(this._buf, 0, buf, prevBlockAvailable, secondReadLen);
/* 107:176 */     this._readIndex = secondReadLen;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void readFully(byte[] buf, int off, int len)
/* 111:    */   {
/* 112:183 */     System.arraycopy(this._buf, this._readIndex, buf, off, len);
/* 113:184 */     this._readIndex += len;
/* 114:    */   }
/* 115:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.storage.DataInputBlock
 * JD-Core Version:    0.7.0.1
 */