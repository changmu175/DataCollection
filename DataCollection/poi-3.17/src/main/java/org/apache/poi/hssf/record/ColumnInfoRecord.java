/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.BitField;
/*   4:    */ import org.apache.poi.util.BitFieldFactory;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ 
/*   8:    */ public final class ColumnInfoRecord
/*   9:    */   extends StandardRecord
/*  10:    */   implements Cloneable
/*  11:    */ {
/*  12:    */   public static final short sid = 125;
/*  13:    */   private int _firstCol;
/*  14:    */   private int _lastCol;
/*  15:    */   private int _colWidth;
/*  16:    */   private int _xfIndex;
/*  17:    */   private int _options;
/*  18: 38 */   private static final BitField hidden = BitFieldFactory.getInstance(1);
/*  19: 39 */   private static final BitField outlevel = BitFieldFactory.getInstance(1792);
/*  20: 40 */   private static final BitField collapsed = BitFieldFactory.getInstance(4096);
/*  21:    */   private int field_6_reserved;
/*  22:    */   
/*  23:    */   public ColumnInfoRecord()
/*  24:    */   {
/*  25: 48 */     setColumnWidth(2275);
/*  26: 49 */     this._options = 2;
/*  27: 50 */     this._xfIndex = 15;
/*  28: 51 */     this.field_6_reserved = 2;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public ColumnInfoRecord(RecordInputStream in)
/*  32:    */   {
/*  33: 55 */     this._firstCol = in.readUShort();
/*  34: 56 */     this._lastCol = in.readUShort();
/*  35: 57 */     this._colWidth = in.readUShort();
/*  36: 58 */     this._xfIndex = in.readUShort();
/*  37: 59 */     this._options = in.readUShort();
/*  38: 60 */     switch (in.remaining())
/*  39:    */     {
/*  40:    */     case 2: 
/*  41: 62 */       this.field_6_reserved = in.readUShort();
/*  42: 63 */       break;
/*  43:    */     case 1: 
/*  44: 67 */       this.field_6_reserved = in.readByte();
/*  45: 68 */       break;
/*  46:    */     case 0: 
/*  47: 73 */       this.field_6_reserved = 0;
/*  48: 74 */       break;
/*  49:    */     default: 
/*  50: 76 */       throw new RuntimeException("Unusual record size remaining=(" + in.remaining() + ")");
/*  51:    */     }
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setFirstColumn(int fc)
/*  55:    */   {
/*  56: 85 */     this._firstCol = fc;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void setLastColumn(int lc)
/*  60:    */   {
/*  61: 93 */     this._lastCol = lc;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void setColumnWidth(int cw)
/*  65:    */   {
/*  66:101 */     this._colWidth = cw;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setXFIndex(int xfi)
/*  70:    */   {
/*  71:110 */     this._xfIndex = xfi;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void setHidden(boolean ishidden)
/*  75:    */   {
/*  76:118 */     this._options = hidden.setBoolean(this._options, ishidden);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setOutlineLevel(int olevel)
/*  80:    */   {
/*  81:126 */     this._options = outlevel.setValue(this._options, olevel);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void setCollapsed(boolean isCollapsed)
/*  85:    */   {
/*  86:134 */     this._options = collapsed.setBoolean(this._options, isCollapsed);
/*  87:    */   }
/*  88:    */   
/*  89:    */   public int getFirstColumn()
/*  90:    */   {
/*  91:142 */     return this._firstCol;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public int getLastColumn()
/*  95:    */   {
/*  96:150 */     return this._lastCol;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public int getColumnWidth()
/* 100:    */   {
/* 101:157 */     return this._colWidth;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public int getXFIndex()
/* 105:    */   {
/* 106:166 */     return this._xfIndex;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public boolean getHidden()
/* 110:    */   {
/* 111:173 */     return hidden.isSet(this._options);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public int getOutlineLevel()
/* 115:    */   {
/* 116:180 */     return outlevel.getValue(this._options);
/* 117:    */   }
/* 118:    */   
/* 119:    */   public boolean getCollapsed()
/* 120:    */   {
/* 121:187 */     return collapsed.isSet(this._options);
/* 122:    */   }
/* 123:    */   
/* 124:    */   public boolean containsColumn(int columnIndex)
/* 125:    */   {
/* 126:191 */     return (this._firstCol <= columnIndex) && (columnIndex <= this._lastCol);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public boolean isAdjacentBefore(ColumnInfoRecord other)
/* 130:    */   {
/* 131:194 */     return this._lastCol == other._firstCol - 1;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public boolean formatMatches(ColumnInfoRecord other)
/* 135:    */   {
/* 136:203 */     if (this._xfIndex != other._xfIndex) {
/* 137:204 */       return false;
/* 138:    */     }
/* 139:206 */     if (this._options != other._options) {
/* 140:207 */       return false;
/* 141:    */     }
/* 142:209 */     if (this._colWidth != other._colWidth) {
/* 143:210 */       return false;
/* 144:    */     }
/* 145:212 */     return true;
/* 146:    */   }
/* 147:    */   
/* 148:    */   public short getSid()
/* 149:    */   {
/* 150:216 */     return 125;
/* 151:    */   }
/* 152:    */   
/* 153:    */   public void serialize(LittleEndianOutput out)
/* 154:    */   {
/* 155:220 */     out.writeShort(getFirstColumn());
/* 156:221 */     out.writeShort(getLastColumn());
/* 157:222 */     out.writeShort(getColumnWidth());
/* 158:223 */     out.writeShort(getXFIndex());
/* 159:224 */     out.writeShort(this._options);
/* 160:225 */     out.writeShort(this.field_6_reserved);
/* 161:    */   }
/* 162:    */   
/* 163:    */   protected int getDataSize()
/* 164:    */   {
/* 165:229 */     return 12;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public String toString()
/* 169:    */   {
/* 170:233 */     StringBuilder sb = new StringBuilder();
/* 171:    */     
/* 172:235 */     sb.append("[COLINFO]\n");
/* 173:236 */     sb.append("  colfirst = ").append(getFirstColumn()).append("\n");
/* 174:237 */     sb.append("  collast  = ").append(getLastColumn()).append("\n");
/* 175:238 */     sb.append("  colwidth = ").append(getColumnWidth()).append("\n");
/* 176:239 */     sb.append("  xfindex  = ").append(getXFIndex()).append("\n");
/* 177:240 */     sb.append("  options  = ").append(HexDump.shortToHex(this._options)).append("\n");
/* 178:241 */     sb.append("    hidden   = ").append(getHidden()).append("\n");
/* 179:242 */     sb.append("    olevel   = ").append(getOutlineLevel()).append("\n");
/* 180:243 */     sb.append("    collapsed= ").append(getCollapsed()).append("\n");
/* 181:244 */     sb.append("[/COLINFO]\n");
/* 182:245 */     return sb.toString();
/* 183:    */   }
/* 184:    */   
/* 185:    */   public ColumnInfoRecord clone()
/* 186:    */   {
/* 187:250 */     ColumnInfoRecord rec = new ColumnInfoRecord();
/* 188:251 */     rec._firstCol = this._firstCol;
/* 189:252 */     rec._lastCol = this._lastCol;
/* 190:253 */     rec._colWidth = this._colWidth;
/* 191:254 */     rec._xfIndex = this._xfIndex;
/* 192:255 */     rec._options = this._options;
/* 193:256 */     rec.field_6_reserved = this.field_6_reserved;
/* 194:257 */     return rec;
/* 195:    */   }
/* 196:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.ColumnInfoRecord
 * JD-Core Version:    0.7.0.1
 */