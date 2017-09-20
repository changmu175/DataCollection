/*   1:    */ package org.apache.poi.ss.format;
/*   2:    */ 
/*   3:    */ import java.text.AttributedCharacterIterator;
/*   4:    */ import java.text.DateFormat;
/*   5:    */ import java.text.DateFormat.Field;
/*   6:    */ import java.text.SimpleDateFormat;
/*   7:    */ import java.util.Calendar;
/*   8:    */ import java.util.Date;
/*   9:    */ import java.util.Formatter;
/*  10:    */ import java.util.Locale;
/*  11:    */ import java.util.regex.Matcher;
/*  12:    */ import org.apache.poi.util.LocaleUtil;
/*  13:    */ 
/*  14:    */ public class CellDateFormatter
/*  15:    */   extends CellFormatter
/*  16:    */ {
/*  17:    */   private boolean amPmUpper;
/*  18:    */   private boolean showM;
/*  19:    */   private boolean showAmPm;
/*  20:    */   private final DateFormat dateFmt;
/*  21:    */   private String sFmt;
/*  22: 41 */   private final Calendar EXCEL_EPOCH_CAL = LocaleUtil.getLocaleCalendar(1904, 0, 1);
/*  23: 44 */   private static CellDateFormatter SIMPLE_DATE = null;
/*  24:    */   
/*  25:    */   private class DatePartHandler
/*  26:    */     implements CellFormatPart.PartHandler
/*  27:    */   {
/*  28: 47 */     private int mStart = -1;
/*  29:    */     private int mLen;
/*  30: 49 */     private int hStart = -1;
/*  31:    */     private int hLen;
/*  32:    */     
/*  33:    */     private DatePartHandler() {}
/*  34:    */     
/*  35:    */     public String handlePart(Matcher m, String part, CellFormatType type, StringBuffer desc)
/*  36:    */     {
/*  37: 55 */       int pos = desc.length();
/*  38: 56 */       char firstCh = part.charAt(0);
/*  39: 57 */       switch (firstCh)
/*  40:    */       {
/*  41:    */       case 'S': 
/*  42:    */       case 's': 
/*  43: 60 */         if (this.mStart >= 0)
/*  44:    */         {
/*  45: 61 */           for (int i = 0; i < this.mLen; i++) {
/*  46: 62 */             desc.setCharAt(this.mStart + i, 'm');
/*  47:    */           }
/*  48: 63 */           this.mStart = -1;
/*  49:    */         }
/*  50: 65 */         return part.toLowerCase(Locale.ROOT);
/*  51:    */       case 'H': 
/*  52:    */       case 'h': 
/*  53: 69 */         this.mStart = -1;
/*  54: 70 */         this.hStart = pos;
/*  55: 71 */         this.hLen = part.length();
/*  56: 72 */         return part.toLowerCase(Locale.ROOT);
/*  57:    */       case 'D': 
/*  58:    */       case 'd': 
/*  59: 76 */         this.mStart = -1;
/*  60: 77 */         if (part.length() <= 2) {
/*  61: 78 */           return part.toLowerCase(Locale.ROOT);
/*  62:    */         }
/*  63: 80 */         return part.toLowerCase(Locale.ROOT).replace('d', 'E');
/*  64:    */       case 'M': 
/*  65:    */       case 'm': 
/*  66: 84 */         this.mStart = pos;
/*  67: 85 */         this.mLen = part.length();
/*  68: 87 */         if (this.hStart >= 0) {
/*  69: 88 */           return part.toLowerCase(Locale.ROOT);
/*  70:    */         }
/*  71: 90 */         return part.toUpperCase(Locale.ROOT);
/*  72:    */       case 'Y': 
/*  73:    */       case 'y': 
/*  74: 94 */         this.mStart = -1;
/*  75: 95 */         if (part.length() == 3) {
/*  76: 96 */           part = "yyyy";
/*  77:    */         }
/*  78: 97 */         return part.toLowerCase(Locale.ROOT);
/*  79:    */       case '0': 
/*  80:100 */         this.mStart = -1;
/*  81:101 */         int sLen = part.length();
/*  82:102 */         CellDateFormatter.this.sFmt = ("%0" + (sLen + 2) + "." + sLen + "f");
/*  83:103 */         return part.replace('0', 'S');
/*  84:    */       case 'A': 
/*  85:    */       case 'P': 
/*  86:    */       case 'a': 
/*  87:    */       case 'p': 
/*  88:109 */         if (part.length() > 1)
/*  89:    */         {
/*  90:111 */           this.mStart = -1;
/*  91:112 */           CellDateFormatter.this.showAmPm = true;
/*  92:113 */           CellDateFormatter.this.showM = (Character.toLowerCase(part.charAt(1)) == 'm');
/*  93:    */           
/*  94:115 */           CellDateFormatter.this.amPmUpper = ((CellDateFormatter.this.showM) || (Character.isUpperCase(part.charAt(0))));
/*  95:    */           
/*  96:117 */           return "a";
/*  97:    */         }
/*  98:    */         break;
/*  99:    */       }
/* 100:122 */       return null;
/* 101:    */     }
/* 102:    */     
/* 103:    */     public void finish(StringBuffer toAppendTo)
/* 104:    */     {
/* 105:127 */       if ((this.hStart >= 0) && (!CellDateFormatter.this.showAmPm)) {
/* 106:128 */         for (int i = 0; i < this.hLen; i++) {
/* 107:129 */           toAppendTo.setCharAt(this.hStart + i, 'H');
/* 108:    */         }
/* 109:    */       }
/* 110:    */     }
/* 111:    */   }
/* 112:    */   
/* 113:    */   public CellDateFormatter(String format)
/* 114:    */   {
/* 115:141 */     this(LocaleUtil.getUserLocale(), format);
/* 116:    */   }
/* 117:    */   
/* 118:    */   public CellDateFormatter(Locale locale, String format)
/* 119:    */   {
/* 120:151 */     super(format);
/* 121:152 */     DatePartHandler partHandler = new DatePartHandler(null);
/* 122:153 */     StringBuffer descBuf = CellFormatPart.parseFormat(format, CellFormatType.DATE, partHandler);
/* 123:    */     
/* 124:155 */     partHandler.finish(descBuf);
/* 125:    */     
/* 126:    */ 
/* 127:158 */     String ptrn = descBuf.toString().replaceAll("((y)(?!y))(?<!yy)", "yy");
/* 128:159 */     this.dateFmt = new SimpleDateFormat(ptrn, locale);
/* 129:160 */     this.dateFmt.setTimeZone(LocaleUtil.getUserTimeZone());
/* 130:    */   }
/* 131:    */   
/* 132:    */   public void formatValue(StringBuffer toAppendTo, Object value)
/* 133:    */   {
/* 134:165 */     if (value == null) {
/* 135:166 */       value = Double.valueOf(0.0D);
/* 136:    */     }
/* 137:167 */     if ((value instanceof Number))
/* 138:    */     {
/* 139:168 */       Number num = (Number)value;
/* 140:169 */       long v = num.longValue();
/* 141:170 */       if (v == 0L)
/* 142:    */       {
/* 143:171 */         value = this.EXCEL_EPOCH_CAL.getTime();
/* 144:    */       }
/* 145:    */       else
/* 146:    */       {
/* 147:173 */         Calendar c = (Calendar)this.EXCEL_EPOCH_CAL.clone();
/* 148:174 */         c.add(13, (int)(v / 1000L));
/* 149:175 */         c.add(14, (int)(v % 1000L));
/* 150:176 */         value = c.getTime();
/* 151:    */       }
/* 152:    */     }
/* 153:180 */     AttributedCharacterIterator it = this.dateFmt.formatToCharacterIterator(value);
/* 154:181 */     boolean doneAm = false;
/* 155:182 */     boolean doneMillis = false;
/* 156:    */     
/* 157:184 */     it.first();
/* 158:185 */     for (char ch = it.first(); ch != 65535; ch = it.next()) {
/* 159:188 */       if (it.getAttribute(Field.MILLISECOND) != null)
/* 160:    */       {
/* 161:189 */         if (!doneMillis)
/* 162:    */         {
/* 163:190 */           Date dateObj = (Date)value;
/* 164:191 */           int pos = toAppendTo.length();
/* 165:192 */           Formatter formatter = new Formatter(toAppendTo, Locale.ROOT);
/* 166:    */           try
/* 167:    */           {
/* 168:194 */             long msecs = dateObj.getTime() % 1000L;
/* 169:195 */             formatter.format(this.locale, this.sFmt, new Object[] { Double.valueOf(msecs / 1000.0D) });
/* 170:    */           }
/* 171:    */           finally
/* 172:    */           {
/* 173:197 */             formatter.close();
/* 174:    */           }
/* 175:199 */           toAppendTo.delete(pos, pos + 2);
/* 176:200 */           doneMillis = true;
/* 177:    */         }
/* 178:    */       }
/* 179:202 */       else if (it.getAttribute(Field.AM_PM) != null)
/* 180:    */       {
/* 181:203 */         if (!doneAm)
/* 182:    */         {
/* 183:204 */           if (this.showAmPm) {
/* 184:205 */             if (this.amPmUpper)
/* 185:    */             {
/* 186:206 */               toAppendTo.append(Character.toUpperCase(ch));
/* 187:207 */               if (this.showM) {
/* 188:208 */                 toAppendTo.append('M');
/* 189:    */               }
/* 190:    */             }
/* 191:    */             else
/* 192:    */             {
/* 193:210 */               toAppendTo.append(Character.toLowerCase(ch));
/* 194:211 */               if (this.showM) {
/* 195:212 */                 toAppendTo.append('m');
/* 196:    */               }
/* 197:    */             }
/* 198:    */           }
/* 199:215 */           doneAm = true;
/* 200:    */         }
/* 201:    */       }
/* 202:    */       else {
/* 203:218 */         toAppendTo.append(ch);
/* 204:    */       }
/* 205:    */     }
/* 206:    */   }
/* 207:    */   
/* 208:    */   public void simpleValue(StringBuffer toAppendTo, Object value)
/* 209:    */   {
/* 210:229 */     synchronized (CellDateFormatter.class)
/* 211:    */     {
/* 212:230 */       if ((SIMPLE_DATE == null) || (!SIMPLE_DATE.EXCEL_EPOCH_CAL.equals(this.EXCEL_EPOCH_CAL))) {
/* 213:231 */         SIMPLE_DATE = new CellDateFormatter("mm/d/y");
/* 214:    */       }
/* 215:    */     }
/* 216:234 */     SIMPLE_DATE.formatValue(toAppendTo, value);
/* 217:    */   }
/* 218:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.ss.format.CellDateFormatter

 * JD-Core Version:    0.7.0.1

 */