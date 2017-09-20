/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.Formula;
/*   4:    */ import org.apache.poi.ss.formula.constant.ConstantValueParser;
/*   5:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ import org.apache.poi.util.StringUtil;
/*   8:    */ 
/*   9:    */ public final class ExternalNameRecord
/*  10:    */   extends StandardRecord
/*  11:    */ {
/*  12:    */   public static final short sid = 35;
/*  13:    */   private static final int OPT_BUILTIN_NAME = 1;
/*  14:    */   private static final int OPT_AUTOMATIC_LINK = 2;
/*  15:    */   private static final int OPT_PICTURE_LINK = 4;
/*  16:    */   private static final int OPT_STD_DOCUMENT_NAME = 8;
/*  17:    */   private static final int OPT_OLE_LINK = 16;
/*  18:    */   private static final int OPT_ICONIFIED_PICTURE_LINK = 32768;
/*  19:    */   private short field_1_option_flag;
/*  20:    */   private short field_2_ixals;
/*  21:    */   private short field_3_not_used;
/*  22:    */   private String field_4_name;
/*  23:    */   private Formula field_5_name_definition;
/*  24:    */   private Object[] _ddeValues;
/*  25:    */   private int _nColumns;
/*  26:    */   private int _nRows;
/*  27:    */   
/*  28:    */   public boolean isBuiltInName()
/*  29:    */   {
/*  30: 67 */     return (this.field_1_option_flag & 0x1) != 0;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public boolean isAutomaticLink()
/*  34:    */   {
/*  35: 75 */     return (this.field_1_option_flag & 0x2) != 0;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public boolean isPicureLink()
/*  39:    */   {
/*  40: 83 */     return (this.field_1_option_flag & 0x4) != 0;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public boolean isStdDocumentNameIdentifier()
/*  44:    */   {
/*  45: 91 */     return (this.field_1_option_flag & 0x8) != 0;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public boolean isOLELink()
/*  49:    */   {
/*  50: 94 */     return (this.field_1_option_flag & 0x10) != 0;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public boolean isIconifiedPictureLink()
/*  54:    */   {
/*  55: 97 */     return (this.field_1_option_flag & 0x8000) != 0;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public String getText()
/*  59:    */   {
/*  60:103 */     return this.field_4_name;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setText(String str)
/*  64:    */   {
/*  65:107 */     this.field_4_name = str;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public short getIx()
/*  69:    */   {
/*  70:119 */     return this.field_2_ixals;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void setIx(short ix)
/*  74:    */   {
/*  75:123 */     this.field_2_ixals = ix;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public Ptg[] getParsedExpression()
/*  79:    */   {
/*  80:127 */     return Formula.getTokens(this.field_5_name_definition);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void setParsedExpression(Ptg[] ptgs)
/*  84:    */   {
/*  85:130 */     this.field_5_name_definition = Formula.create(ptgs);
/*  86:    */   }
/*  87:    */   
/*  88:    */   protected int getDataSize()
/*  89:    */   {
/*  90:136 */     int result = 6;
/*  91:137 */     result += StringUtil.getEncodedSize(this.field_4_name) - 1;
/*  92:139 */     if ((!isOLELink()) && (!isStdDocumentNameIdentifier())) {
/*  93:140 */       if (isAutomaticLink())
/*  94:    */       {
/*  95:141 */         if (this._ddeValues != null)
/*  96:    */         {
/*  97:142 */           result += 3;
/*  98:143 */           result += ConstantValueParser.getEncodedSize(this._ddeValues);
/*  99:    */         }
/* 100:    */       }
/* 101:    */       else {
/* 102:146 */         result += this.field_5_name_definition.getEncodedSize();
/* 103:    */       }
/* 104:    */     }
/* 105:149 */     return result;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void serialize(LittleEndianOutput out)
/* 109:    */   {
/* 110:154 */     out.writeShort(this.field_1_option_flag);
/* 111:155 */     out.writeShort(this.field_2_ixals);
/* 112:156 */     out.writeShort(this.field_3_not_used);
/* 113:    */     
/* 114:158 */     out.writeByte(this.field_4_name.length());
/* 115:159 */     StringUtil.writeUnicodeStringFlagAndData(out, this.field_4_name);
/* 116:161 */     if ((!isOLELink()) && (!isStdDocumentNameIdentifier())) {
/* 117:162 */       if (isAutomaticLink())
/* 118:    */       {
/* 119:163 */         if (this._ddeValues != null)
/* 120:    */         {
/* 121:164 */           out.writeByte(this._nColumns - 1);
/* 122:165 */           out.writeShort(this._nRows - 1);
/* 123:166 */           ConstantValueParser.encode(out, this._ddeValues);
/* 124:    */         }
/* 125:    */       }
/* 126:    */       else {
/* 127:169 */         this.field_5_name_definition.serialize(out);
/* 128:    */       }
/* 129:    */     }
/* 130:    */   }
/* 131:    */   
/* 132:    */   public ExternalNameRecord()
/* 133:    */   {
/* 134:175 */     this.field_2_ixals = 0;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public ExternalNameRecord(RecordInputStream in)
/* 138:    */   {
/* 139:179 */     this.field_1_option_flag = in.readShort();
/* 140:180 */     this.field_2_ixals = in.readShort();
/* 141:181 */     this.field_3_not_used = in.readShort();
/* 142:    */     
/* 143:183 */     int numChars = in.readUByte();
/* 144:184 */     this.field_4_name = StringUtil.readUnicodeString(in, numChars);
/* 145:188 */     if ((!isOLELink()) && (!isStdDocumentNameIdentifier())) {
/* 146:191 */       if (isAutomaticLink())
/* 147:    */       {
/* 148:192 */         if (in.available() > 0)
/* 149:    */         {
/* 150:194 */           int nColumns = in.readUByte() + 1;
/* 151:195 */           int nRows = in.readShort() + 1;
/* 152:    */           
/* 153:197 */           int totalCount = nRows * nColumns;
/* 154:198 */           this._ddeValues = ConstantValueParser.parse(in, totalCount);
/* 155:199 */           this._nColumns = nColumns;
/* 156:200 */           this._nRows = nRows;
/* 157:    */         }
/* 158:    */       }
/* 159:    */       else
/* 160:    */       {
/* 161:204 */         int formulaLen = in.readUShort();
/* 162:205 */         this.field_5_name_definition = Formula.read(formulaLen, in);
/* 163:    */       }
/* 164:    */     }
/* 165:    */   }
/* 166:    */   
/* 167:    */   public short getSid()
/* 168:    */   {
/* 169:212 */     return 35;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public String toString()
/* 173:    */   {
/* 174:217 */     StringBuilder sb = new StringBuilder();
/* 175:218 */     sb.append("[EXTERNALNAME]\n");
/* 176:219 */     sb.append("    .options = ").append(this.field_1_option_flag).append("\n");
/* 177:220 */     sb.append("    .ix      = ").append(this.field_2_ixals).append("\n");
/* 178:221 */     sb.append("    .name    = ").append(this.field_4_name).append("\n");
/* 179:222 */     if (this.field_5_name_definition != null)
/* 180:    */     {
/* 181:223 */       Ptg[] ptgs = this.field_5_name_definition.getTokens();
/* 182:224 */       for (Ptg ptg : ptgs) {
/* 183:225 */         sb.append("    .namedef = ").append(ptg).append(ptg.getRVAType()).append("\n");
/* 184:    */       }
/* 185:    */     }
/* 186:228 */     sb.append("[/EXTERNALNAME]\n");
/* 187:229 */     return sb.toString();
/* 188:    */   }
/* 189:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.ExternalNameRecord
 * JD-Core Version:    0.7.0.1
 */