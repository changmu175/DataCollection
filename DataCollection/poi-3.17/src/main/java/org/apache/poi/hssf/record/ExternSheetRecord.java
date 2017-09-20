/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ 
/*   7:    */ public class ExternSheetRecord
/*   8:    */   extends StandardRecord
/*   9:    */ {
/*  10:    */   public static final short sid = 23;
/*  11:    */   private final List<RefSubRecord> _list;
/*  12:    */   
/*  13:    */   private static final class RefSubRecord
/*  14:    */   {
/*  15:    */     public static final int ENCODED_SIZE = 6;
/*  16:    */     private final int _extBookIndex;
/*  17:    */     private int _firstSheetIndex;
/*  18:    */     private int _lastSheetIndex;
/*  19:    */     
/*  20:    */     public void adjustIndex(int offset)
/*  21:    */     {
/*  22: 43 */       this._firstSheetIndex += offset;
/*  23: 44 */       this._lastSheetIndex += offset;
/*  24:    */     }
/*  25:    */     
/*  26:    */     public RefSubRecord(int extBookIndex, int firstSheetIndex, int lastSheetIndex)
/*  27:    */     {
/*  28: 50 */       this._extBookIndex = extBookIndex;
/*  29: 51 */       this._firstSheetIndex = firstSheetIndex;
/*  30: 52 */       this._lastSheetIndex = lastSheetIndex;
/*  31:    */     }
/*  32:    */     
/*  33:    */     public RefSubRecord(RecordInputStream in)
/*  34:    */     {
/*  35: 59 */       this(in.readShort(), in.readShort(), in.readShort());
/*  36:    */     }
/*  37:    */     
/*  38:    */     public int getExtBookIndex()
/*  39:    */     {
/*  40: 62 */       return this._extBookIndex;
/*  41:    */     }
/*  42:    */     
/*  43:    */     public int getFirstSheetIndex()
/*  44:    */     {
/*  45: 65 */       return this._firstSheetIndex;
/*  46:    */     }
/*  47:    */     
/*  48:    */     public int getLastSheetIndex()
/*  49:    */     {
/*  50: 68 */       return this._lastSheetIndex;
/*  51:    */     }
/*  52:    */     
/*  53:    */     public String toString()
/*  54:    */     {
/*  55: 73 */       StringBuffer buffer = new StringBuffer();
/*  56: 74 */       buffer.append("extBook=").append(this._extBookIndex);
/*  57: 75 */       buffer.append(" firstSheet=").append(this._firstSheetIndex);
/*  58: 76 */       buffer.append(" lastSheet=").append(this._lastSheetIndex);
/*  59: 77 */       return buffer.toString();
/*  60:    */     }
/*  61:    */     
/*  62:    */     public void serialize(LittleEndianOutput out)
/*  63:    */     {
/*  64: 81 */       out.writeShort(this._extBookIndex);
/*  65: 82 */       out.writeShort(this._firstSheetIndex);
/*  66: 83 */       out.writeShort(this._lastSheetIndex);
/*  67:    */     }
/*  68:    */   }
/*  69:    */   
/*  70:    */   public ExternSheetRecord()
/*  71:    */   {
/*  72: 90 */     this._list = new ArrayList();
/*  73:    */   }
/*  74:    */   
/*  75:    */   public ExternSheetRecord(RecordInputStream in)
/*  76:    */   {
/*  77: 94 */     this._list = new ArrayList();
/*  78:    */     
/*  79: 96 */     int nItems = in.readShort();
/*  80: 98 */     for (int i = 0; i < nItems; i++)
/*  81:    */     {
/*  82: 99 */       RefSubRecord rec = new RefSubRecord(in);
/*  83:100 */       this._list.add(rec);
/*  84:    */     }
/*  85:    */   }
/*  86:    */   
/*  87:    */   public int getNumOfRefs()
/*  88:    */   {
/*  89:109 */     return this._list.size();
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void addREFRecord(RefSubRecord rec)
/*  93:    */   {
/*  94:117 */     this._list.add(rec);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public int getNumOfREFRecords()
/*  98:    */   {
/*  99:124 */     return this._list.size();
/* 100:    */   }
/* 101:    */   
/* 102:    */   public String toString()
/* 103:    */   {
/* 104:130 */     StringBuffer sb = new StringBuffer();
/* 105:131 */     int nItems = this._list.size();
/* 106:132 */     sb.append("[EXTERNSHEET]\n");
/* 107:133 */     sb.append("   numOfRefs     = ").append(nItems).append("\n");
/* 108:134 */     for (int i = 0; i < nItems; i++)
/* 109:    */     {
/* 110:135 */       sb.append("refrec         #").append(i).append(": ");
/* 111:136 */       sb.append(getRef(i));
/* 112:137 */       sb.append('\n');
/* 113:    */     }
/* 114:139 */     sb.append("[/EXTERNSHEET]\n");
/* 115:    */     
/* 116:    */ 
/* 117:142 */     return sb.toString();
/* 118:    */   }
/* 119:    */   
/* 120:    */   protected int getDataSize()
/* 121:    */   {
/* 122:147 */     return 2 + this._list.size() * 6;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public void serialize(LittleEndianOutput out)
/* 126:    */   {
/* 127:152 */     int nItems = this._list.size();
/* 128:    */     
/* 129:154 */     out.writeShort(nItems);
/* 130:156 */     for (int i = 0; i < nItems; i++) {
/* 131:157 */       getRef(i).serialize(out);
/* 132:    */     }
/* 133:    */   }
/* 134:    */   
/* 135:    */   private RefSubRecord getRef(int i)
/* 136:    */   {
/* 137:162 */     return (RefSubRecord)this._list.get(i);
/* 138:    */   }
/* 139:    */   
/* 140:    */   public void removeSheet(int sheetIdx)
/* 141:    */   {
/* 142:166 */     int nItems = this._list.size();
/* 143:167 */     for (int i = 0; i < nItems; i++)
/* 144:    */     {
/* 145:168 */       RefSubRecord refSubRecord = (RefSubRecord)this._list.get(i);
/* 146:169 */       if ((refSubRecord.getFirstSheetIndex() == sheetIdx) && (refSubRecord.getLastSheetIndex() == sheetIdx)) {
/* 147:172 */         this._list.set(i, new RefSubRecord(refSubRecord.getExtBookIndex(), -1, -1));
/* 148:173 */       } else if ((refSubRecord.getFirstSheetIndex() > sheetIdx) && (refSubRecord.getLastSheetIndex() > sheetIdx)) {
/* 149:175 */         this._list.set(i, new RefSubRecord(refSubRecord.getExtBookIndex(), refSubRecord.getFirstSheetIndex() - 1, refSubRecord.getLastSheetIndex() - 1));
/* 150:    */       }
/* 151:    */     }
/* 152:    */   }
/* 153:    */   
/* 154:    */   public short getSid()
/* 155:    */   {
/* 156:185 */     return 23;
/* 157:    */   }
/* 158:    */   
/* 159:    */   public int getExtbookIndexFromRefIndex(int refIndex)
/* 160:    */   {
/* 161:194 */     RefSubRecord refRec = getRef(refIndex);
/* 162:195 */     return refRec.getExtBookIndex();
/* 163:    */   }
/* 164:    */   
/* 165:    */   public int findRefIndexFromExtBookIndex(int extBookIndex)
/* 166:    */   {
/* 167:204 */     int nItems = this._list.size();
/* 168:205 */     for (int i = 0; i < nItems; i++) {
/* 169:206 */       if (getRef(i).getExtBookIndex() == extBookIndex) {
/* 170:207 */         return i;
/* 171:    */       }
/* 172:    */     }
/* 173:210 */     return -1;
/* 174:    */   }
/* 175:    */   
/* 176:    */   public int getFirstSheetIndexFromRefIndex(int extRefIndex)
/* 177:    */   {
/* 178:225 */     return getRef(extRefIndex).getFirstSheetIndex();
/* 179:    */   }
/* 180:    */   
/* 181:    */   public int getLastSheetIndexFromRefIndex(int extRefIndex)
/* 182:    */   {
/* 183:242 */     return getRef(extRefIndex).getLastSheetIndex();
/* 184:    */   }
/* 185:    */   
/* 186:    */   public int addRef(int extBookIndex, int firstSheetIndex, int lastSheetIndex)
/* 187:    */   {
/* 188:275 */     this._list.add(new RefSubRecord(extBookIndex, firstSheetIndex, lastSheetIndex));
/* 189:276 */     return this._list.size() - 1;
/* 190:    */   }
/* 191:    */   
/* 192:    */   public int getRefIxForSheet(int externalBookIndex, int firstSheetIndex, int lastSheetIndex)
/* 193:    */   {
/* 194:280 */     int nItems = this._list.size();
/* 195:281 */     for (int i = 0; i < nItems; i++)
/* 196:    */     {
/* 197:282 */       RefSubRecord ref = getRef(i);
/* 198:283 */       if (ref.getExtBookIndex() == externalBookIndex) {
/* 199:286 */         if ((ref.getFirstSheetIndex() == firstSheetIndex) && (ref.getLastSheetIndex() == lastSheetIndex)) {
/* 200:288 */           return i;
/* 201:    */         }
/* 202:    */       }
/* 203:    */     }
/* 204:291 */     return -1;
/* 205:    */   }
/* 206:    */   
/* 207:    */   public static ExternSheetRecord combine(ExternSheetRecord[] esrs)
/* 208:    */   {
/* 209:295 */     ExternSheetRecord result = new ExternSheetRecord();
/* 210:296 */     for (ExternSheetRecord esr : esrs)
/* 211:    */     {
/* 212:297 */       int nRefs = esr.getNumOfREFRecords();
/* 213:298 */       for (int j = 0; j < nRefs; j++) {
/* 214:299 */         result.addREFRecord(esr.getRef(j));
/* 215:    */       }
/* 216:    */     }
/* 217:302 */     return result;
/* 218:    */   }
/* 219:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.ExternSheetRecord
 * JD-Core Version:    0.7.0.1
 */