/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   4:    */ import org.apache.poi.util.HexDump;
/*   5:    */ import org.apache.poi.util.LittleEndianInput;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ import org.apache.poi.util.RecordFormatException;
/*   8:    */ import org.apache.poi.util.StringUtil;
/*   9:    */ 
/*  10:    */ public class LbsDataSubRecord
/*  11:    */   extends SubRecord
/*  12:    */ {
/*  13:    */   public static final int sid = 19;
/*  14:    */   private int _cbFContinued;
/*  15:    */   private int _unknownPreFormulaInt;
/*  16:    */   private Ptg _linkPtg;
/*  17:    */   private Byte _unknownPostFormulaByte;
/*  18:    */   private int _cLines;
/*  19:    */   private int _iSel;
/*  20:    */   private int _flags;
/*  21:    */   private int _idEdit;
/*  22:    */   private LbsDropData _dropData;
/*  23:    */   private String[] _rgLines;
/*  24:    */   private boolean[] _bsels;
/*  25:    */   
/*  26:    */   public LbsDataSubRecord(LittleEndianInput in, int cbFContinued, int cmoOt)
/*  27:    */   {
/*  28: 98 */     this._cbFContinued = cbFContinued;
/*  29:    */     
/*  30:100 */     int encodedTokenLen = in.readUShort();
/*  31:101 */     if (encodedTokenLen > 0)
/*  32:    */     {
/*  33:102 */       int formulaSize = in.readUShort();
/*  34:103 */       this._unknownPreFormulaInt = in.readInt();
/*  35:    */       
/*  36:105 */       Ptg[] ptgs = Ptg.readTokens(formulaSize, in);
/*  37:106 */       if (ptgs.length != 1) {
/*  38:107 */         throw new RecordFormatException("Read " + ptgs.length + " tokens but expected exactly 1");
/*  39:    */       }
/*  40:110 */       this._linkPtg = ptgs[0];
/*  41:111 */       switch (encodedTokenLen - formulaSize - 6)
/*  42:    */       {
/*  43:    */       case 1: 
/*  44:113 */         this._unknownPostFormulaByte = Byte.valueOf(in.readByte());
/*  45:114 */         break;
/*  46:    */       case 0: 
/*  47:116 */         this._unknownPostFormulaByte = null;
/*  48:117 */         break;
/*  49:    */       default: 
/*  50:119 */         throw new RecordFormatException("Unexpected leftover bytes");
/*  51:    */       }
/*  52:    */     }
/*  53:123 */     this._cLines = in.readUShort();
/*  54:124 */     this._iSel = in.readUShort();
/*  55:125 */     this._flags = in.readUShort();
/*  56:126 */     this._idEdit = in.readUShort();
/*  57:130 */     if (cmoOt == 20) {
/*  58:131 */       this._dropData = new LbsDropData(in);
/*  59:    */     }
/*  60:136 */     if ((this._flags & 0x2) != 0)
/*  61:    */     {
/*  62:137 */       this._rgLines = new String[this._cLines];
/*  63:138 */       for (int i = 0; i < this._cLines; i++) {
/*  64:139 */         this._rgLines[i] = StringUtil.readUnicodeString(in);
/*  65:    */       }
/*  66:    */     }
/*  67:148 */     if ((this._flags >> 4 & 0x2) != 0)
/*  68:    */     {
/*  69:149 */       this._bsels = new boolean[this._cLines];
/*  70:150 */       for (int i = 0; i < this._cLines; i++) {
/*  71:151 */         this._bsels[i] = (in.readByte() == 1 ? 1 : false);
/*  72:    */       }
/*  73:    */     }
/*  74:    */   }
/*  75:    */   
/*  76:    */   LbsDataSubRecord() {}
/*  77:    */   
/*  78:    */   public static LbsDataSubRecord newAutoFilterInstance()
/*  79:    */   {
/*  80:167 */     LbsDataSubRecord lbs = new LbsDataSubRecord();
/*  81:168 */     lbs._cbFContinued = 8174;
/*  82:169 */     lbs._iSel = 0;
/*  83:    */     
/*  84:171 */     lbs._flags = 769;
/*  85:172 */     lbs._dropData = new LbsDropData();
/*  86:173 */     lbs._dropData._wStyle = 2;
/*  87:    */     
/*  88:    */ 
/*  89:176 */     lbs._dropData._cLine = 8;
/*  90:177 */     return lbs;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public boolean isTerminating()
/*  94:    */   {
/*  95:185 */     return true;
/*  96:    */   }
/*  97:    */   
/*  98:    */   protected int getDataSize()
/*  99:    */   {
/* 100:190 */     int result = 2;
/* 101:193 */     if (this._linkPtg != null)
/* 102:    */     {
/* 103:194 */       result += 2;
/* 104:195 */       result += 4;
/* 105:196 */       result += this._linkPtg.getSize();
/* 106:197 */       if (this._unknownPostFormulaByte != null) {
/* 107:198 */         result++;
/* 108:    */       }
/* 109:    */     }
/* 110:202 */     result += 8;
/* 111:203 */     if (this._dropData != null) {
/* 112:204 */       result += this._dropData.getDataSize();
/* 113:    */     }
/* 114:206 */     if (this._rgLines != null) {
/* 115:207 */       for (String str : this._rgLines) {
/* 116:208 */         result += StringUtil.getEncodedSize(str);
/* 117:    */       }
/* 118:    */     }
/* 119:211 */     if (this._bsels != null) {
/* 120:212 */       result += this._bsels.length;
/* 121:    */     }
/* 122:214 */     return result;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public void serialize(LittleEndianOutput out)
/* 126:    */   {
/* 127:219 */     out.writeShort(19);
/* 128:220 */     out.writeShort(this._cbFContinued);
/* 129:222 */     if (this._linkPtg == null)
/* 130:    */     {
/* 131:223 */       out.writeShort(0);
/* 132:    */     }
/* 133:    */     else
/* 134:    */     {
/* 135:225 */       int formulaSize = this._linkPtg.getSize();
/* 136:226 */       int linkSize = formulaSize + 6;
/* 137:227 */       if (this._unknownPostFormulaByte != null) {
/* 138:228 */         linkSize++;
/* 139:    */       }
/* 140:230 */       out.writeShort(linkSize);
/* 141:231 */       out.writeShort(formulaSize);
/* 142:232 */       out.writeInt(this._unknownPreFormulaInt);
/* 143:233 */       this._linkPtg.write(out);
/* 144:234 */       if (this._unknownPostFormulaByte != null) {
/* 145:235 */         out.writeByte(this._unknownPostFormulaByte.intValue());
/* 146:    */       }
/* 147:    */     }
/* 148:239 */     out.writeShort(this._cLines);
/* 149:240 */     out.writeShort(this._iSel);
/* 150:241 */     out.writeShort(this._flags);
/* 151:242 */     out.writeShort(this._idEdit);
/* 152:244 */     if (this._dropData != null) {
/* 153:245 */       this._dropData.serialize(out);
/* 154:    */     }
/* 155:248 */     if (this._rgLines != null) {
/* 156:249 */       for (String str : this._rgLines) {
/* 157:250 */         StringUtil.writeUnicodeString(out, str);
/* 158:    */       }
/* 159:    */     }
/* 160:254 */     if (this._bsels != null) {
/* 161:255 */       for (boolean val : this._bsels) {
/* 162:256 */         out.writeByte(val ? 1 : 0);
/* 163:    */       }
/* 164:    */     }
/* 165:    */   }
/* 166:    */   
/* 167:    */   public LbsDataSubRecord clone()
/* 168:    */   {
/* 169:264 */     return this;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public String toString()
/* 173:    */   {
/* 174:269 */     StringBuffer sb = new StringBuffer(256);
/* 175:    */     
/* 176:271 */     sb.append("[ftLbsData]\n");
/* 177:272 */     sb.append("    .unknownShort1 =").append(HexDump.shortToHex(this._cbFContinued)).append("\n");
/* 178:273 */     sb.append("    .formula        = ").append('\n');
/* 179:274 */     if (this._linkPtg != null) {
/* 180:275 */       sb.append(this._linkPtg).append(this._linkPtg.getRVAType()).append('\n');
/* 181:    */     }
/* 182:277 */     sb.append("    .nEntryCount   =").append(HexDump.shortToHex(this._cLines)).append("\n");
/* 183:278 */     sb.append("    .selEntryIx    =").append(HexDump.shortToHex(this._iSel)).append("\n");
/* 184:279 */     sb.append("    .style         =").append(HexDump.shortToHex(this._flags)).append("\n");
/* 185:280 */     sb.append("    .unknownShort10=").append(HexDump.shortToHex(this._idEdit)).append("\n");
/* 186:281 */     if (this._dropData != null) {
/* 187:282 */       sb.append('\n').append(this._dropData);
/* 188:    */     }
/* 189:284 */     sb.append("[/ftLbsData]\n");
/* 190:285 */     return sb.toString();
/* 191:    */   }
/* 192:    */   
/* 193:    */   public Ptg getFormula()
/* 194:    */   {
/* 195:293 */     return this._linkPtg;
/* 196:    */   }
/* 197:    */   
/* 198:    */   public int getNumberOfItems()
/* 199:    */   {
/* 200:300 */     return this._cLines;
/* 201:    */   }
/* 202:    */   
/* 203:    */   public static class LbsDropData
/* 204:    */   {
/* 205:    */     public static final int STYLE_COMBO_DROPDOWN = 0;
/* 206:    */     public static final int STYLE_COMBO_EDIT_DROPDOWN = 1;
/* 207:    */     public static final int STYLE_COMBO_SIMPLE_DROPDOWN = 2;
/* 208:    */     private int _wStyle;
/* 209:    */     private int _cLine;
/* 210:    */     private int _dxMin;
/* 211:    */     private final String _str;
/* 212:    */     private Byte _unused;
/* 213:    */     
/* 214:    */     public LbsDropData()
/* 215:    */     {
/* 216:347 */       this._str = "";
/* 217:348 */       this._unused = Byte.valueOf((byte)0);
/* 218:    */     }
/* 219:    */     
/* 220:    */     public LbsDropData(LittleEndianInput in)
/* 221:    */     {
/* 222:352 */       this._wStyle = in.readUShort();
/* 223:353 */       this._cLine = in.readUShort();
/* 224:354 */       this._dxMin = in.readUShort();
/* 225:355 */       this._str = StringUtil.readUnicodeString(in);
/* 226:356 */       if (StringUtil.getEncodedSize(this._str) % 2 != 0) {
/* 227:357 */         this._unused = Byte.valueOf(in.readByte());
/* 228:    */       }
/* 229:    */     }
/* 230:    */     
/* 231:    */     public void setStyle(int style)
/* 232:    */     {
/* 233:374 */       this._wStyle = style;
/* 234:    */     }
/* 235:    */     
/* 236:    */     public void setNumLines(int num)
/* 237:    */     {
/* 238:383 */       this._cLine = num;
/* 239:    */     }
/* 240:    */     
/* 241:    */     public void serialize(LittleEndianOutput out)
/* 242:    */     {
/* 243:387 */       out.writeShort(this._wStyle);
/* 244:388 */       out.writeShort(this._cLine);
/* 245:389 */       out.writeShort(this._dxMin);
/* 246:390 */       StringUtil.writeUnicodeString(out, this._str);
/* 247:391 */       if (this._unused != null) {
/* 248:392 */         out.writeByte(this._unused.byteValue());
/* 249:    */       }
/* 250:    */     }
/* 251:    */     
/* 252:    */     public int getDataSize()
/* 253:    */     {
/* 254:397 */       int size = 6;
/* 255:398 */       size += StringUtil.getEncodedSize(this._str);
/* 256:399 */       if (this._unused != null) {
/* 257:400 */         size++;
/* 258:    */       }
/* 259:402 */       return size;
/* 260:    */     }
/* 261:    */     
/* 262:    */     public String toString()
/* 263:    */     {
/* 264:407 */       StringBuffer sb = new StringBuffer();
/* 265:408 */       sb.append("[LbsDropData]\n");
/* 266:409 */       sb.append("  ._wStyle:  ").append(this._wStyle).append('\n');
/* 267:410 */       sb.append("  ._cLine:  ").append(this._cLine).append('\n');
/* 268:411 */       sb.append("  ._dxMin:  ").append(this._dxMin).append('\n');
/* 269:412 */       sb.append("  ._str:  ").append(this._str).append('\n');
/* 270:413 */       if (this._unused != null) {
/* 271:414 */         sb.append("  ._unused:  ").append(this._unused).append('\n');
/* 272:    */       }
/* 273:416 */       sb.append("[/LbsDropData]\n");
/* 274:    */       
/* 275:418 */       return sb.toString();
/* 276:    */     }
/* 277:    */   }
/* 278:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.LbsDataSubRecord
 * JD-Core Version:    0.7.0.1
 */