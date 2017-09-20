/*   1:    */ package org.apache.poi.hssf.record.crypto;
/*   2:    */ 
/*   3:    */ import java.io.InputStream;
/*   4:    */ import java.io.PushbackInputStream;
/*   5:    */ import org.apache.poi.hssf.record.BiffHeaderInput;
/*   6:    */ import org.apache.poi.poifs.crypt.ChunkedCipherInputStream;
/*   7:    */ import org.apache.poi.poifs.crypt.Decryptor;
/*   8:    */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*   9:    */ import org.apache.poi.util.LittleEndian;
/*  10:    */ import org.apache.poi.util.LittleEndianInput;
/*  11:    */ import org.apache.poi.util.RecordFormatException;
/*  12:    */ 
/*  13:    */ public final class Biff8DecryptingStream
/*  14:    */   implements BiffHeaderInput, LittleEndianInput
/*  15:    */ {
/*  16:    */   public static final int RC4_REKEYING_INTERVAL = 1024;
/*  17:    */   private final EncryptionInfo info;
/*  18:    */   private ChunkedCipherInputStream ccis;
/*  19: 41 */   private final byte[] buffer = new byte[8];
/*  20: 42 */   private boolean shouldSkipEncryptionOnCurrentRecord = false;
/*  21:    */   
/*  22:    */   public Biff8DecryptingStream(InputStream in, int initialOffset, EncryptionInfo info)
/*  23:    */     throws RecordFormatException
/*  24:    */   {
/*  25:    */     try
/*  26:    */     {
/*  27: 46 */       byte[] initialBuf = new byte[initialOffset];
/*  28:    */       InputStream stream;
/*  29:    */       InputStream stream;
/*  30: 48 */       if (initialOffset == 0)
/*  31:    */       {
/*  32: 49 */         stream = in;
/*  33:    */       }
/*  34:    */       else
/*  35:    */       {
/*  36: 51 */         stream = new PushbackInputStream(in, initialOffset);
/*  37: 52 */         ((PushbackInputStream)stream).unread(initialBuf);
/*  38:    */       }
/*  39: 55 */       this.info = info;
/*  40: 56 */       Decryptor dec = this.info.getDecryptor();
/*  41: 57 */       dec.setChunkSize(1024);
/*  42: 58 */       this.ccis = ((ChunkedCipherInputStream)dec.getDataStream(stream, 2147483647, 0));
/*  43: 60 */       if (initialOffset > 0) {
/*  44: 61 */         this.ccis.readFully(initialBuf);
/*  45:    */       }
/*  46:    */     }
/*  47:    */     catch (Exception e)
/*  48:    */     {
/*  49: 64 */       throw new RecordFormatException(e);
/*  50:    */     }
/*  51:    */   }
/*  52:    */   
/*  53:    */   public int available()
/*  54:    */   {
/*  55: 70 */     return this.ccis.available();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public int readRecordSID()
/*  59:    */   {
/*  60: 78 */     readPlain(this.buffer, 0, 2);
/*  61: 79 */     int sid = LittleEndian.getUShort(this.buffer, 0);
/*  62: 80 */     this.shouldSkipEncryptionOnCurrentRecord = isNeverEncryptedRecord(sid);
/*  63: 81 */     return sid;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public int readDataSize()
/*  67:    */   {
/*  68: 89 */     readPlain(this.buffer, 0, 2);
/*  69: 90 */     int dataSize = LittleEndian.getUShort(this.buffer, 0);
/*  70: 91 */     this.ccis.setNextRecordSize(dataSize);
/*  71: 92 */     return dataSize;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public double readDouble()
/*  75:    */   {
/*  76: 97 */     long valueLongBits = readLong();
/*  77: 98 */     double result = Double.longBitsToDouble(valueLongBits);
/*  78: 99 */     if (Double.isNaN(result)) {
/*  79:101 */       throw new RuntimeException("Did not expect to read NaN");
/*  80:    */     }
/*  81:103 */     return result;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void readFully(byte[] buf)
/*  85:    */   {
/*  86:108 */     readFully(buf, 0, buf.length);
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void readFully(byte[] buf, int off, int len)
/*  90:    */   {
/*  91:113 */     if (this.shouldSkipEncryptionOnCurrentRecord) {
/*  92:114 */       readPlain(buf, off, buf.length);
/*  93:    */     } else {
/*  94:116 */       this.ccis.readFully(buf, off, len);
/*  95:    */     }
/*  96:    */   }
/*  97:    */   
/*  98:    */   public int readUByte()
/*  99:    */   {
/* 100:122 */     return readByte() & 0xFF;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public byte readByte()
/* 104:    */   {
/* 105:127 */     if (this.shouldSkipEncryptionOnCurrentRecord)
/* 106:    */     {
/* 107:128 */       readPlain(this.buffer, 0, 1);
/* 108:129 */       return this.buffer[0];
/* 109:    */     }
/* 110:131 */     return this.ccis.readByte();
/* 111:    */   }
/* 112:    */   
/* 113:    */   public int readUShort()
/* 114:    */   {
/* 115:137 */     return readShort() & 0xFFFF;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public short readShort()
/* 119:    */   {
/* 120:142 */     if (this.shouldSkipEncryptionOnCurrentRecord)
/* 121:    */     {
/* 122:143 */       readPlain(this.buffer, 0, 2);
/* 123:144 */       return LittleEndian.getShort(this.buffer);
/* 124:    */     }
/* 125:146 */     return this.ccis.readShort();
/* 126:    */   }
/* 127:    */   
/* 128:    */   public int readInt()
/* 129:    */   {
/* 130:152 */     if (this.shouldSkipEncryptionOnCurrentRecord)
/* 131:    */     {
/* 132:153 */       readPlain(this.buffer, 0, 4);
/* 133:154 */       return LittleEndian.getInt(this.buffer);
/* 134:    */     }
/* 135:156 */     return this.ccis.readInt();
/* 136:    */   }
/* 137:    */   
/* 138:    */   public long readLong()
/* 139:    */   {
/* 140:162 */     if (this.shouldSkipEncryptionOnCurrentRecord)
/* 141:    */     {
/* 142:163 */       readPlain(this.buffer, 0, 8);
/* 143:164 */       return LittleEndian.getLong(this.buffer);
/* 144:    */     }
/* 145:166 */     return this.ccis.readLong();
/* 146:    */   }
/* 147:    */   
/* 148:    */   public long getPosition()
/* 149:    */   {
/* 150:174 */     return this.ccis.getPos();
/* 151:    */   }
/* 152:    */   
/* 153:    */   public static boolean isNeverEncryptedRecord(int sid)
/* 154:    */   {
/* 155:183 */     switch (sid)
/* 156:    */     {
/* 157:    */     case 47: 
/* 158:    */     case 225: 
/* 159:    */     case 2057: 
/* 160:199 */       return true;
/* 161:    */     }
/* 162:202 */     return false;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public void readPlain(byte[] b, int off, int len)
/* 166:    */   {
/* 167:208 */     this.ccis.readPlain(b, off, len);
/* 168:    */   }
/* 169:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.crypto.Biff8DecryptingStream
 * JD-Core Version:    0.7.0.1
 */