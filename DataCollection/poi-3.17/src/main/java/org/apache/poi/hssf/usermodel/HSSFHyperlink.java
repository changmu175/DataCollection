/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.common.usermodel.HyperlinkType;
/*   4:    */ import org.apache.poi.hssf.record.HyperlinkRecord;
/*   5:    */ import org.apache.poi.ss.usermodel.Hyperlink;
/*   6:    */ import org.apache.poi.util.Internal;
/*   7:    */ 
/*   8:    */ public class HSSFHyperlink
/*   9:    */   implements Hyperlink
/*  10:    */ {
/*  11:    */   protected final HyperlinkRecord record;
/*  12:    */   protected final HyperlinkType link_type;
/*  13:    */   
/*  14:    */   @Internal(since="3.15 beta 3")
/*  15:    */   protected HSSFHyperlink(HyperlinkType type)
/*  16:    */   {
/*  17: 50 */     this.link_type = type;
/*  18: 51 */     this.record = new HyperlinkRecord();
/*  19: 52 */     switch (1.$SwitchMap$org$apache$poi$common$usermodel$HyperlinkType[type.ordinal()])
/*  20:    */     {
/*  21:    */     case 1: 
/*  22:    */     case 2: 
/*  23: 55 */       this.record.newUrlLink();
/*  24: 56 */       break;
/*  25:    */     case 3: 
/*  26: 58 */       this.record.newFileLink();
/*  27: 59 */       break;
/*  28:    */     case 4: 
/*  29: 61 */       this.record.newDocumentLink();
/*  30: 62 */       break;
/*  31:    */     default: 
/*  32: 64 */       throw new IllegalArgumentException("Invalid type: " + type);
/*  33:    */     }
/*  34:    */   }
/*  35:    */   
/*  36:    */   protected HSSFHyperlink(HyperlinkRecord record)
/*  37:    */   {
/*  38: 75 */     this.record = record;
/*  39: 76 */     this.link_type = getType(record);
/*  40:    */   }
/*  41:    */   
/*  42:    */   private static HyperlinkType getType(HyperlinkRecord record)
/*  43:    */   {
/*  44:    */     HyperlinkType link_type;
/*  45:    */     HyperlinkType link_type;
/*  46: 82 */     if (record.isFileLink())
/*  47:    */     {
/*  48: 83 */       link_type = HyperlinkType.FILE;
/*  49:    */     }
/*  50:    */     else
/*  51:    */     {
/*  52:    */       HyperlinkType link_type;
/*  53: 84 */       if (record.isDocumentLink())
/*  54:    */       {
/*  55: 85 */         link_type = HyperlinkType.DOCUMENT;
/*  56:    */       }
/*  57:    */       else
/*  58:    */       {
/*  59:    */         HyperlinkType link_type;
/*  60: 87 */         if ((record.getAddress() != null) && (record.getAddress().startsWith("mailto:"))) {
/*  61: 89 */           link_type = HyperlinkType.EMAIL;
/*  62:    */         } else {
/*  63: 91 */           link_type = HyperlinkType.URL;
/*  64:    */         }
/*  65:    */       }
/*  66:    */     }
/*  67: 94 */     return link_type;
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected HSSFHyperlink(Hyperlink other)
/*  71:    */   {
/*  72: 98 */     if ((other instanceof HSSFHyperlink))
/*  73:    */     {
/*  74: 99 */       HSSFHyperlink hlink = (HSSFHyperlink)other;
/*  75:100 */       this.record = hlink.record.clone();
/*  76:101 */       this.link_type = getType(this.record);
/*  77:    */     }
/*  78:    */     else
/*  79:    */     {
/*  80:104 */       this.link_type = other.getTypeEnum();
/*  81:105 */       this.record = new HyperlinkRecord();
/*  82:106 */       setFirstRow(other.getFirstRow());
/*  83:107 */       setFirstColumn(other.getFirstColumn());
/*  84:108 */       setLastRow(other.getLastRow());
/*  85:109 */       setLastColumn(other.getLastColumn());
/*  86:    */     }
/*  87:    */   }
/*  88:    */   
/*  89:    */   public int getFirstRow()
/*  90:    */   {
/*  91:120 */     return this.record.getFirstRow();
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void setFirstRow(int row)
/*  95:    */   {
/*  96:130 */     this.record.setFirstRow(row);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public int getLastRow()
/* 100:    */   {
/* 101:140 */     return this.record.getLastRow();
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setLastRow(int row)
/* 105:    */   {
/* 106:150 */     this.record.setLastRow(row);
/* 107:    */   }
/* 108:    */   
/* 109:    */   public int getFirstColumn()
/* 110:    */   {
/* 111:160 */     return this.record.getFirstColumn();
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void setFirstColumn(int col)
/* 115:    */   {
/* 116:170 */     this.record.setFirstColumn((short)col);
/* 117:    */   }
/* 118:    */   
/* 119:    */   public int getLastColumn()
/* 120:    */   {
/* 121:180 */     return this.record.getLastColumn();
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void setLastColumn(int col)
/* 125:    */   {
/* 126:190 */     this.record.setLastColumn((short)col);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public String getAddress()
/* 130:    */   {
/* 131:200 */     return this.record.getAddress();
/* 132:    */   }
/* 133:    */   
/* 134:    */   public String getTextMark()
/* 135:    */   {
/* 136:203 */     return this.record.getTextMark();
/* 137:    */   }
/* 138:    */   
/* 139:    */   public void setTextMark(String textMark)
/* 140:    */   {
/* 141:212 */     this.record.setTextMark(textMark);
/* 142:    */   }
/* 143:    */   
/* 144:    */   public String getShortFilename()
/* 145:    */   {
/* 146:215 */     return this.record.getShortFilename();
/* 147:    */   }
/* 148:    */   
/* 149:    */   public void setShortFilename(String shortFilename)
/* 150:    */   {
/* 151:223 */     this.record.setShortFilename(shortFilename);
/* 152:    */   }
/* 153:    */   
/* 154:    */   public void setAddress(String address)
/* 155:    */   {
/* 156:233 */     this.record.setAddress(address);
/* 157:    */   }
/* 158:    */   
/* 159:    */   public String getLabel()
/* 160:    */   {
/* 161:243 */     return this.record.getLabel();
/* 162:    */   }
/* 163:    */   
/* 164:    */   public void setLabel(String label)
/* 165:    */   {
/* 166:253 */     this.record.setLabel(label);
/* 167:    */   }
/* 168:    */   
/* 169:    */   /**
/* 170:    */    * @deprecated
/* 171:    */    */
/* 172:    */   public int getType()
/* 173:    */   {
/* 174:266 */     return this.link_type.getCode();
/* 175:    */   }
/* 176:    */   
/* 177:    */   public HyperlinkType getTypeEnum()
/* 178:    */   {
/* 179:276 */     return this.link_type;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public boolean equals(Object other)
/* 183:    */   {
/* 184:284 */     if (this == other) {
/* 185:284 */       return true;
/* 186:    */     }
/* 187:285 */     if (!(other instanceof HSSFHyperlink)) {
/* 188:285 */       return false;
/* 189:    */     }
/* 190:286 */     HSSFHyperlink otherLink = (HSSFHyperlink)other;
/* 191:287 */     return this.record == otherLink.record;
/* 192:    */   }
/* 193:    */   
/* 194:    */   public int hashCode()
/* 195:    */   {
/* 196:292 */     return this.record.hashCode();
/* 197:    */   }
/* 198:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFHyperlink
 * JD-Core Version:    0.7.0.1
 */