/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.Iterator;
/*   4:    */ import org.apache.poi.hssf.model.InternalWorkbook;
/*   5:    */ import org.apache.poi.hssf.record.LabelSSTRecord;
/*   6:    */ import org.apache.poi.hssf.record.common.UnicodeString;
/*   7:    */ import org.apache.poi.hssf.record.common.UnicodeString.FormatRun;
/*   8:    */ import org.apache.poi.ss.usermodel.Font;
/*   9:    */ import org.apache.poi.ss.usermodel.RichTextString;
/*  10:    */ 
/*  11:    */ public final class HSSFRichTextString
/*  12:    */   implements Comparable<HSSFRichTextString>, RichTextString
/*  13:    */ {
/*  14:    */   public static final short NO_FONT = 0;
/*  15:    */   private UnicodeString _string;
/*  16:    */   private InternalWorkbook _book;
/*  17:    */   private LabelSSTRecord _record;
/*  18:    */   
/*  19:    */   public HSSFRichTextString()
/*  20:    */   {
/*  21: 79 */     this("");
/*  22:    */   }
/*  23:    */   
/*  24:    */   public HSSFRichTextString(String string)
/*  25:    */   {
/*  26: 83 */     if (string == null) {
/*  27: 84 */       this._string = new UnicodeString("");
/*  28:    */     } else {
/*  29: 86 */       this._string = new UnicodeString(string);
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   HSSFRichTextString(InternalWorkbook book, LabelSSTRecord record)
/*  34:    */   {
/*  35: 91 */     setWorkbookReferences(book, record);
/*  36:    */     
/*  37: 93 */     this._string = book.getSSTString(record.getSSTIndex());
/*  38:    */   }
/*  39:    */   
/*  40:    */   void setWorkbookReferences(InternalWorkbook book, LabelSSTRecord record)
/*  41:    */   {
/*  42:100 */     this._book = book;
/*  43:101 */     this._record = record;
/*  44:    */   }
/*  45:    */   
/*  46:    */   private UnicodeString cloneStringIfRequired()
/*  47:    */   {
/*  48:109 */     if (this._book == null) {
/*  49:110 */       return this._string;
/*  50:    */     }
/*  51:111 */     UnicodeString s = (UnicodeString)this._string.clone();
/*  52:112 */     return s;
/*  53:    */   }
/*  54:    */   
/*  55:    */   private void addToSSTIfRequired()
/*  56:    */   {
/*  57:116 */     if (this._book != null)
/*  58:    */     {
/*  59:117 */       int index = this._book.addSSTString(this._string);
/*  60:118 */       this._record.setSSTIndex(index);
/*  61:    */       
/*  62:    */ 
/*  63:121 */       this._string = this._book.getSSTString(index);
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void applyFont(int startIndex, int endIndex, short fontIndex)
/*  68:    */   {
/*  69:135 */     if (startIndex > endIndex) {
/*  70:136 */       throw new IllegalArgumentException("Start index must be less than end index.");
/*  71:    */     }
/*  72:137 */     if ((startIndex < 0) || (endIndex > length())) {
/*  73:138 */       throw new IllegalArgumentException("Start and end index not in range.");
/*  74:    */     }
/*  75:139 */     if (startIndex == endIndex) {
/*  76:140 */       return;
/*  77:    */     }
/*  78:144 */     short currentFont = 0;
/*  79:145 */     if (endIndex != length()) {
/*  80:146 */       currentFont = getFontAtIndex(endIndex);
/*  81:    */     }
/*  82:150 */     this._string = cloneStringIfRequired();
/*  83:151 */     Iterator<FormatRun> formatting = this._string.formatIterator();
/*  84:152 */     if (formatting != null) {
/*  85:153 */       while (formatting.hasNext())
/*  86:    */       {
/*  87:154 */         FormatRun r = (FormatRun)formatting.next();
/*  88:155 */         if ((r.getCharacterPos() >= startIndex) && (r.getCharacterPos() < endIndex)) {
/*  89:156 */           formatting.remove();
/*  90:    */         }
/*  91:    */       }
/*  92:    */     }
/*  93:161 */     this._string.addFormatRun(new FormatRun((short)startIndex, fontIndex));
/*  94:162 */     if (endIndex != length()) {
/*  95:163 */       this._string.addFormatRun(new FormatRun((short)endIndex, currentFont));
/*  96:    */     }
/*  97:165 */     addToSSTIfRequired();
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void applyFont(int startIndex, int endIndex, Font font)
/* 101:    */   {
/* 102:177 */     applyFont(startIndex, endIndex, ((HSSFFont)font).getIndex());
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void applyFont(Font font)
/* 106:    */   {
/* 107:186 */     applyFont(0, this._string.getCharCount(), font);
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void clearFormatting()
/* 111:    */   {
/* 112:193 */     this._string = cloneStringIfRequired();
/* 113:194 */     this._string.clearFormatting();
/* 114:195 */     addToSSTIfRequired();
/* 115:    */   }
/* 116:    */   
/* 117:    */   public String getString()
/* 118:    */   {
/* 119:203 */     return this._string.getString();
/* 120:    */   }
/* 121:    */   
/* 122:    */   UnicodeString getUnicodeString()
/* 123:    */   {
/* 124:212 */     return cloneStringIfRequired();
/* 125:    */   }
/* 126:    */   
/* 127:    */   UnicodeString getRawUnicodeString()
/* 128:    */   {
/* 129:223 */     return this._string;
/* 130:    */   }
/* 131:    */   
/* 132:    */   void setUnicodeString(UnicodeString str)
/* 133:    */   {
/* 134:228 */     this._string = str;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public int length()
/* 138:    */   {
/* 139:236 */     return this._string.getCharCount();
/* 140:    */   }
/* 141:    */   
/* 142:    */   public short getFontAtIndex(int index)
/* 143:    */   {
/* 144:249 */     int size = this._string.getFormatRunCount();
/* 145:250 */     FormatRun currentRun = null;
/* 146:251 */     for (int i = 0; i < size; i++)
/* 147:    */     {
/* 148:252 */       FormatRun r = this._string.getFormatRun(i);
/* 149:253 */       if (r.getCharacterPos() > index) {
/* 150:    */         break;
/* 151:    */       }
/* 152:256 */       currentRun = r;
/* 153:    */     }
/* 154:258 */     if (currentRun == null) {
/* 155:259 */       return 0;
/* 156:    */     }
/* 157:261 */     return currentRun.getFontIndex();
/* 158:    */   }
/* 159:    */   
/* 160:    */   public int numFormattingRuns()
/* 161:    */   {
/* 162:272 */     return this._string.getFormatRunCount();
/* 163:    */   }
/* 164:    */   
/* 165:    */   public int getIndexOfFormattingRun(int index)
/* 166:    */   {
/* 167:282 */     FormatRun r = this._string.getFormatRun(index);
/* 168:283 */     return r.getCharacterPos();
/* 169:    */   }
/* 170:    */   
/* 171:    */   public short getFontOfFormattingRun(int index)
/* 172:    */   {
/* 173:294 */     FormatRun r = this._string.getFormatRun(index);
/* 174:295 */     return r.getFontIndex();
/* 175:    */   }
/* 176:    */   
/* 177:    */   public int compareTo(HSSFRichTextString r)
/* 178:    */   {
/* 179:302 */     return this._string.compareTo(r._string);
/* 180:    */   }
/* 181:    */   
/* 182:    */   public boolean equals(Object o)
/* 183:    */   {
/* 184:307 */     if ((o instanceof HSSFRichTextString)) {
/* 185:308 */       return this._string.equals(((HSSFRichTextString)o)._string);
/* 186:    */     }
/* 187:310 */     return false;
/* 188:    */   }
/* 189:    */   
/* 190:    */   public int hashCode()
/* 191:    */   {
/* 192:316 */     if (!$assertionsDisabled) {
/* 193:316 */       throw new AssertionError("hashCode not designed");
/* 194:    */     }
/* 195:317 */     return 42;
/* 196:    */   }
/* 197:    */   
/* 198:    */   public String toString()
/* 199:    */   {
/* 200:326 */     return this._string.toString();
/* 201:    */   }
/* 202:    */   
/* 203:    */   public void applyFont(short fontIndex)
/* 204:    */   {
/* 205:336 */     applyFont(0, this._string.getCharCount(), fontIndex);
/* 206:    */   }
/* 207:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFRichTextString

 * JD-Core Version:    0.7.0.1

 */