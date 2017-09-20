/*   1:    */ package org.apache.poi.hssf.record.cont;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.DelayableLittleEndianOutput;
/*   4:    */ import org.apache.poi.util.LittleEndianOutput;
/*   5:    */ import org.apache.poi.util.StringUtil;
/*   6:    */ 
/*   7:    */ public final class ContinuableRecordOutput
/*   8:    */   implements LittleEndianOutput
/*   9:    */ {
/*  10:    */   private final LittleEndianOutput _out;
/*  11:    */   private UnknownLengthRecordOutput _ulrOutput;
/*  12:    */   private int _totalPreviousRecordsSize;
/*  13:    */   
/*  14:    */   public ContinuableRecordOutput(LittleEndianOutput out, int sid)
/*  15:    */   {
/*  16: 37 */     this._ulrOutput = new UnknownLengthRecordOutput(out, sid);
/*  17: 38 */     this._out = out;
/*  18: 39 */     this._totalPreviousRecordsSize = 0;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public static ContinuableRecordOutput createForCountingOnly()
/*  22:    */   {
/*  23: 43 */     return new ContinuableRecordOutput(NOPOutput, -777);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public int getTotalSize()
/*  27:    */   {
/*  28: 50 */     return this._totalPreviousRecordsSize + this._ulrOutput.getTotalSize();
/*  29:    */   }
/*  30:    */   
/*  31:    */   void terminate()
/*  32:    */   {
/*  33: 56 */     this._ulrOutput.terminate();
/*  34:    */   }
/*  35:    */   
/*  36:    */   public int getAvailableSpace()
/*  37:    */   {
/*  38: 62 */     return this._ulrOutput.getAvailableSpace();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void writeContinue()
/*  42:    */   {
/*  43: 70 */     this._ulrOutput.terminate();
/*  44: 71 */     this._totalPreviousRecordsSize += this._ulrOutput.getTotalSize();
/*  45: 72 */     this._ulrOutput = new UnknownLengthRecordOutput(this._out, 60);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void writeContinueIfRequired(int requiredContinuousSize)
/*  49:    */   {
/*  50: 80 */     if (this._ulrOutput.getAvailableSpace() < requiredContinuousSize) {
/*  51: 81 */       writeContinue();
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void writeStringData(String text)
/*  56:    */   {
/*  57:103 */     boolean is16bitEncoded = StringUtil.hasMultibyte(text);
/*  58:    */     
/*  59:105 */     int keepTogetherSize = 2;
/*  60:106 */     int optionFlags = 0;
/*  61:107 */     if (is16bitEncoded)
/*  62:    */     {
/*  63:108 */       optionFlags |= 0x1;
/*  64:109 */       keepTogetherSize++;
/*  65:    */     }
/*  66:111 */     writeContinueIfRequired(keepTogetherSize);
/*  67:112 */     writeByte(optionFlags);
/*  68:113 */     writeCharacterData(text, is16bitEncoded);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void writeString(String text, int numberOfRichTextRuns, int extendedDataSize)
/*  72:    */   {
/*  73:142 */     boolean is16bitEncoded = StringUtil.hasMultibyte(text);
/*  74:    */     
/*  75:144 */     int keepTogetherSize = 4;
/*  76:145 */     int optionFlags = 0;
/*  77:146 */     if (is16bitEncoded)
/*  78:    */     {
/*  79:147 */       optionFlags |= 0x1;
/*  80:148 */       keepTogetherSize++;
/*  81:    */     }
/*  82:150 */     if (numberOfRichTextRuns > 0)
/*  83:    */     {
/*  84:151 */       optionFlags |= 0x8;
/*  85:152 */       keepTogetherSize += 2;
/*  86:    */     }
/*  87:154 */     if (extendedDataSize > 0)
/*  88:    */     {
/*  89:155 */       optionFlags |= 0x4;
/*  90:156 */       keepTogetherSize += 4;
/*  91:    */     }
/*  92:158 */     writeContinueIfRequired(keepTogetherSize);
/*  93:159 */     writeShort(text.length());
/*  94:160 */     writeByte(optionFlags);
/*  95:161 */     if (numberOfRichTextRuns > 0) {
/*  96:162 */       writeShort(numberOfRichTextRuns);
/*  97:    */     }
/*  98:164 */     if (extendedDataSize > 0) {
/*  99:165 */       writeInt(extendedDataSize);
/* 100:    */     }
/* 101:167 */     writeCharacterData(text, is16bitEncoded);
/* 102:    */   }
/* 103:    */   
/* 104:    */   private void writeCharacterData(String text, boolean is16bitEncoded)
/* 105:    */   {
/* 106:172 */     int nChars = text.length();
/* 107:173 */     int i = 0;
/* 108:174 */     if (is16bitEncoded) {
/* 109:    */       for (;;)
/* 110:    */       {
/* 111:176 */         for (int nWritableChars = Math.min(nChars - i, this._ulrOutput.getAvailableSpace() / 2); nWritableChars > 0; nWritableChars--) {
/* 112:178 */           this._ulrOutput.writeShort(text.charAt(i++));
/* 113:    */         }
/* 114:180 */         if (i >= nChars) {
/* 115:    */           break;
/* 116:    */         }
/* 117:183 */         writeContinue();
/* 118:184 */         writeByte(1);
/* 119:    */       }
/* 120:    */     }
/* 121:    */     for (;;)
/* 122:    */     {
/* 123:188 */       for (int nWritableChars = Math.min(nChars - i, this._ulrOutput.getAvailableSpace() / 1); nWritableChars > 0; nWritableChars--) {
/* 124:190 */         this._ulrOutput.writeByte(text.charAt(i++));
/* 125:    */       }
/* 126:192 */       if (i >= nChars) {
/* 127:    */         break;
/* 128:    */       }
/* 129:195 */       writeContinue();
/* 130:196 */       writeByte(0);
/* 131:    */     }
/* 132:    */   }
/* 133:    */   
/* 134:    */   public void write(byte[] b)
/* 135:    */   {
/* 136:202 */     writeContinueIfRequired(b.length);
/* 137:203 */     this._ulrOutput.write(b);
/* 138:    */   }
/* 139:    */   
/* 140:    */   public void write(byte[] b, int offset, int len)
/* 141:    */   {
/* 142:208 */     int i = 0;
/* 143:    */     for (;;)
/* 144:    */     {
/* 145:210 */       for (int nWritableChars = Math.min(len - i, this._ulrOutput.getAvailableSpace() / 1); nWritableChars > 0; nWritableChars--) {
/* 146:212 */         this._ulrOutput.writeByte(b[(offset + i++)]);
/* 147:    */       }
/* 148:214 */       if (i >= len) {
/* 149:    */         break;
/* 150:    */       }
/* 151:217 */       writeContinue();
/* 152:    */     }
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void writeByte(int v)
/* 156:    */   {
/* 157:222 */     writeContinueIfRequired(1);
/* 158:223 */     this._ulrOutput.writeByte(v);
/* 159:    */   }
/* 160:    */   
/* 161:    */   public void writeDouble(double v)
/* 162:    */   {
/* 163:226 */     writeContinueIfRequired(8);
/* 164:227 */     this._ulrOutput.writeDouble(v);
/* 165:    */   }
/* 166:    */   
/* 167:    */   public void writeInt(int v)
/* 168:    */   {
/* 169:230 */     writeContinueIfRequired(4);
/* 170:231 */     this._ulrOutput.writeInt(v);
/* 171:    */   }
/* 172:    */   
/* 173:    */   public void writeLong(long v)
/* 174:    */   {
/* 175:234 */     writeContinueIfRequired(8);
/* 176:235 */     this._ulrOutput.writeLong(v);
/* 177:    */   }
/* 178:    */   
/* 179:    */   public void writeShort(int v)
/* 180:    */   {
/* 181:238 */     writeContinueIfRequired(2);
/* 182:239 */     this._ulrOutput.writeShort(v);
/* 183:    */   }
/* 184:    */   
/* 185:245 */   private static final LittleEndianOutput NOPOutput = new DelayableLittleEndianOutput()
/* 186:    */   {
/* 187:    */     public LittleEndianOutput createDelayedOutput(int size)
/* 188:    */     {
/* 189:248 */       return this;
/* 190:    */     }
/* 191:    */     
/* 192:    */     public void write(byte[] b) {}
/* 193:    */     
/* 194:    */     public void write(byte[] b, int offset, int len) {}
/* 195:    */     
/* 196:    */     public void writeByte(int v) {}
/* 197:    */     
/* 198:    */     public void writeDouble(double v) {}
/* 199:    */     
/* 200:    */     public void writeInt(int v) {}
/* 201:    */     
/* 202:    */     public void writeLong(long v) {}
/* 203:    */     
/* 204:    */     public void writeShort(int v) {}
/* 205:    */   };
/* 206:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.cont.ContinuableRecordOutput
 * JD-Core Version:    0.7.0.1
 */