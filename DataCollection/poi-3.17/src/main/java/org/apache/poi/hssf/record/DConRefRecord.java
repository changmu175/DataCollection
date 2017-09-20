/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import org.apache.poi.util.LittleEndian;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ import org.apache.poi.util.RecordFormatException;
/*   7:    */ import org.apache.poi.util.StringUtil;
/*   8:    */ 
/*   9:    */ public class DConRefRecord
/*  10:    */   extends StandardRecord
/*  11:    */ {
/*  12:    */   public static final short sid = 81;
/*  13:    */   private int firstRow;
/*  14:    */   private int lastRow;
/*  15:    */   private int firstCol;
/*  16:    */   private int lastCol;
/*  17:    */   private int charCount;
/*  18:    */   private int charType;
/*  19:    */   private byte[] path;
/*  20:    */   private byte[] _unused;
/*  21:    */   
/*  22:    */   public DConRefRecord(byte[] data)
/*  23:    */   {
/*  24:114 */     int offset = 0;
/*  25:115 */     if (LittleEndian.getShort(data, offset) != 81) {
/*  26:116 */       throw new RecordFormatException("incompatible sid.");
/*  27:    */     }
/*  28:117 */     offset += 2;
/*  29:    */     
/*  30:    */ 
/*  31:120 */     offset += 2;
/*  32:    */     
/*  33:122 */     this.firstRow = LittleEndian.getUShort(data, offset);
/*  34:123 */     offset += 2;
/*  35:124 */     this.lastRow = LittleEndian.getUShort(data, offset);
/*  36:125 */     offset += 2;
/*  37:126 */     this.firstCol = LittleEndian.getUByte(data, offset);
/*  38:127 */     offset++;
/*  39:128 */     this.lastCol = LittleEndian.getUByte(data, offset);
/*  40:129 */     offset++;
/*  41:130 */     this.charCount = LittleEndian.getUShort(data, offset);
/*  42:131 */     offset += 2;
/*  43:132 */     if (this.charCount < 2) {
/*  44:133 */       throw new RecordFormatException("Character count must be >= 2");
/*  45:    */     }
/*  46:135 */     this.charType = LittleEndian.getUByte(data, offset);
/*  47:136 */     offset++;
/*  48:    */     
/*  49:    */ 
/*  50:    */ 
/*  51:    */ 
/*  52:    */ 
/*  53:    */ 
/*  54:143 */     int byteLength = this.charCount * ((this.charType & 0x1) + 1);
/*  55:    */     
/*  56:145 */     this.path = LittleEndian.getByteArray(data, offset, byteLength);
/*  57:146 */     offset += byteLength;
/*  58:152 */     if (this.path[0] == 2) {
/*  59:153 */       this._unused = LittleEndian.getByteArray(data, offset, this.charType + 1);
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   public DConRefRecord(RecordInputStream inStream)
/*  64:    */   {
/*  65:164 */     if (inStream.getSid() != 81) {
/*  66:165 */       throw new RecordFormatException("Wrong sid: " + inStream.getSid());
/*  67:    */     }
/*  68:167 */     this.firstRow = inStream.readUShort();
/*  69:168 */     this.lastRow = inStream.readUShort();
/*  70:169 */     this.firstCol = inStream.readUByte();
/*  71:170 */     this.lastCol = inStream.readUByte();
/*  72:    */     
/*  73:172 */     this.charCount = inStream.readUShort();
/*  74:173 */     this.charType = (inStream.readUByte() & 0x1);
/*  75:    */     
/*  76:    */ 
/*  77:176 */     int byteLength = this.charCount * (this.charType + 1);
/*  78:    */     
/*  79:178 */     this.path = new byte[byteLength];
/*  80:179 */     inStream.readFully(this.path);
/*  81:181 */     if (this.path[0] == 2) {
/*  82:182 */       this._unused = inStream.readRemainder();
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected int getDataSize()
/*  87:    */   {
/*  88:193 */     int sz = 9 + this.path.length;
/*  89:194 */     if (this.path[0] == 2) {
/*  90:195 */       sz += this._unused.length;
/*  91:    */     }
/*  92:196 */     return sz;
/*  93:    */   }
/*  94:    */   
/*  95:    */   protected void serialize(LittleEndianOutput out)
/*  96:    */   {
/*  97:202 */     out.writeShort(this.firstRow);
/*  98:203 */     out.writeShort(this.lastRow);
/*  99:204 */     out.writeByte(this.firstCol);
/* 100:205 */     out.writeByte(this.lastCol);
/* 101:206 */     out.writeShort(this.charCount);
/* 102:207 */     out.writeByte(this.charType);
/* 103:208 */     out.write(this.path);
/* 104:209 */     if (this.path[0] == 2) {
/* 105:210 */       out.write(this._unused);
/* 106:    */     }
/* 107:    */   }
/* 108:    */   
/* 109:    */   public short getSid()
/* 110:    */   {
/* 111:216 */     return 81;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public int getFirstColumn()
/* 115:    */   {
/* 116:224 */     return this.firstCol;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public int getFirstRow()
/* 120:    */   {
/* 121:232 */     return this.firstRow;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public int getLastColumn()
/* 125:    */   {
/* 126:240 */     return this.lastCol;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public int getLastRow()
/* 130:    */   {
/* 131:248 */     return this.lastRow;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public String toString()
/* 135:    */   {
/* 136:254 */     StringBuilder b = new StringBuilder();
/* 137:255 */     b.append("[DCONREF]\n");
/* 138:256 */     b.append("    .ref\n");
/* 139:257 */     b.append("        .firstrow   = ").append(this.firstRow).append("\n");
/* 140:258 */     b.append("        .lastrow    = ").append(this.lastRow).append("\n");
/* 141:259 */     b.append("        .firstcol   = ").append(this.firstCol).append("\n");
/* 142:260 */     b.append("        .lastcol    = ").append(this.lastCol).append("\n");
/* 143:261 */     b.append("    .cch            = ").append(this.charCount).append("\n");
/* 144:262 */     b.append("    .stFile\n");
/* 145:263 */     b.append("        .h          = ").append(this.charType).append("\n");
/* 146:264 */     b.append("        .rgb        = ").append(getReadablePath()).append("\n");
/* 147:265 */     b.append("[/DCONREF]\n");
/* 148:    */     
/* 149:267 */     return b.toString();
/* 150:    */   }
/* 151:    */   
/* 152:    */   public byte[] getPath()
/* 153:    */   {
/* 154:276 */     return Arrays.copyOf(this.path, this.path.length);
/* 155:    */   }
/* 156:    */   
/* 157:    */   public String getReadablePath()
/* 158:    */   {
/* 159:285 */     if (this.path != null)
/* 160:    */     {
/* 161:289 */       int offset = 1;
/* 162:290 */       while ((this.path[offset] < 32) && (offset < this.path.length)) {
/* 163:292 */         offset++;
/* 164:    */       }
/* 165:294 */       String out = new String(Arrays.copyOfRange(this.path, offset, this.path.length), StringUtil.UTF8);
/* 166:    */       
/* 167:296 */       out = out.replaceAll("\003", "/");
/* 168:297 */       return out;
/* 169:    */     }
/* 170:299 */     return null;
/* 171:    */   }
/* 172:    */   
/* 173:    */   public boolean isExternalRef()
/* 174:    */   {
/* 175:309 */     if (this.path[0] == 1) {
/* 176:310 */       return true;
/* 177:    */     }
/* 178:311 */     return false;
/* 179:    */   }
/* 180:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.DConRefRecord
 * JD-Core Version:    0.7.0.1
 */