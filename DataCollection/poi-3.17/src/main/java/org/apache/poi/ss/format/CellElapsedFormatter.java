/*   1:    */ package org.apache.poi.ss.format;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Formatter;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.ListIterator;
/*   7:    */ import java.util.Locale;
/*   8:    */ import java.util.regex.Matcher;
/*   9:    */ import java.util.regex.Pattern;
/*  10:    */ 
/*  11:    */ public class CellElapsedFormatter
/*  12:    */   extends CellFormatter
/*  13:    */ {
/*  14:    */   private final List<TimeSpec> specs;
/*  15:    */   private TimeSpec topmost;
/*  16:    */   private final String printfFmt;
/*  17: 37 */   private static final Pattern PERCENTS = Pattern.compile("%");
/*  18:    */   private static final double HOUR__FACTOR = 0.04166666666666666D;
/*  19:    */   private static final double MIN__FACTOR = 0.0006944444444444444D;
/*  20:    */   private static final double SEC__FACTOR = 1.157407407407407E-005D;
/*  21:    */   
/*  22:    */   private static class TimeSpec
/*  23:    */   {
/*  24:    */     final char type;
/*  25:    */     final int pos;
/*  26:    */     final int len;
/*  27:    */     final double factor;
/*  28:    */     double modBy;
/*  29:    */     
/*  30:    */     public TimeSpec(char type, int pos, int len, double factor)
/*  31:    */     {
/*  32: 51 */       this.type = type;
/*  33: 52 */       this.pos = pos;
/*  34: 53 */       this.len = len;
/*  35: 54 */       this.factor = factor;
/*  36: 55 */       this.modBy = 0.0D;
/*  37:    */     }
/*  38:    */     
/*  39:    */     public long valueFor(double elapsed)
/*  40:    */     {
/*  41:    */       double val;
/*  42:    */       double val;
/*  43: 60 */       if (this.modBy == 0.0D) {
/*  44: 61 */         val = elapsed / this.factor;
/*  45:    */       } else {
/*  46: 63 */         val = elapsed / this.factor % this.modBy;
/*  47:    */       }
/*  48: 64 */       if (this.type == '0') {
/*  49: 65 */         return Math.round(val);
/*  50:    */       }
/*  51: 67 */       return val;
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:    */   private class ElapsedPartHandler
/*  56:    */     implements CellFormatPart.PartHandler
/*  57:    */   {
/*  58:    */     private ElapsedPartHandler() {}
/*  59:    */     
/*  60:    */     public String handlePart(Matcher m, String part, CellFormatType type, StringBuffer desc)
/*  61:    */     {
/*  62: 80 */       int pos = desc.length();
/*  63: 81 */       char firstCh = part.charAt(0);
/*  64: 82 */       switch (firstCh)
/*  65:    */       {
/*  66:    */       case '[': 
/*  67: 84 */         if (part.length() >= 3)
/*  68:    */         {
/*  69: 86 */           if (CellElapsedFormatter.this.topmost != null) {
/*  70: 87 */             throw new IllegalArgumentException("Duplicate '[' times in format");
/*  71:    */           }
/*  72: 89 */           part = part.toLowerCase(Locale.ROOT);
/*  73: 90 */           int specLen = part.length() - 2;
/*  74: 91 */           CellElapsedFormatter.this.topmost = CellElapsedFormatter.this.assignSpec(part.charAt(1), pos, specLen);
/*  75: 92 */           return part.substring(1, 1 + specLen);
/*  76:    */         }
/*  77:    */         break;
/*  78:    */       case '0': 
/*  79:    */       case 'h': 
/*  80:    */       case 'm': 
/*  81:    */       case 's': 
/*  82: 98 */         part = part.toLowerCase(Locale.ROOT);
/*  83: 99 */         CellElapsedFormatter.this.assignSpec(part.charAt(0), pos, part.length());
/*  84:100 */         return part;
/*  85:    */       case '\n': 
/*  86:103 */         return "%n";
/*  87:    */       case '"': 
/*  88:106 */         part = part.substring(1, part.length() - 1);
/*  89:107 */         break;
/*  90:    */       case '\\': 
/*  91:110 */         part = part.substring(1);
/*  92:111 */         break;
/*  93:    */       case '*': 
/*  94:114 */         if (part.length() > 1) {
/*  95:115 */           part = CellFormatPart.expandChar(part);
/*  96:    */         }
/*  97:    */         break;
/*  98:    */       case '_': 
/*  99:120 */         return null;
/* 100:    */       }
/* 101:123 */       return CellElapsedFormatter.PERCENTS.matcher(part).replaceAll("%%");
/* 102:    */     }
/* 103:    */   }
/* 104:    */   
/* 105:    */   public CellElapsedFormatter(String pattern)
/* 106:    */   {
/* 107:133 */     super(pattern);
/* 108:    */     
/* 109:135 */     this.specs = new ArrayList();
/* 110:    */     
/* 111:137 */     StringBuffer desc = CellFormatPart.parseFormat(pattern, CellFormatType.ELAPSED, new ElapsedPartHandler(null));
/* 112:    */     
/* 113:    */ 
/* 114:140 */     ListIterator<TimeSpec> it = this.specs.listIterator(this.specs.size());
/* 115:141 */     while (it.hasPrevious())
/* 116:    */     {
/* 117:142 */       TimeSpec spec = (TimeSpec)it.previous();
/* 118:143 */       desc.replace(spec.pos, spec.pos + spec.len, "%0" + spec.len + "d");
/* 119:144 */       if (spec.type != this.topmost.type) {
/* 120:145 */         spec.modBy = modFor(spec.type, spec.len);
/* 121:    */       }
/* 122:    */     }
/* 123:149 */     this.printfFmt = desc.toString();
/* 124:    */   }
/* 125:    */   
/* 126:    */   private TimeSpec assignSpec(char type, int pos, int len)
/* 127:    */   {
/* 128:153 */     TimeSpec spec = new TimeSpec(type, pos, len, factorFor(type, len));
/* 129:154 */     this.specs.add(spec);
/* 130:155 */     return spec;
/* 131:    */   }
/* 132:    */   
/* 133:    */   private static double factorFor(char type, int len)
/* 134:    */   {
/* 135:159 */     switch (type)
/* 136:    */     {
/* 137:    */     case 'h': 
/* 138:161 */       return 0.04166666666666666D;
/* 139:    */     case 'm': 
/* 140:163 */       return 0.0006944444444444444D;
/* 141:    */     case 's': 
/* 142:165 */       return 1.157407407407407E-005D;
/* 143:    */     case '0': 
/* 144:167 */       return 1.157407407407407E-005D / Math.pow(10.0D, len);
/* 145:    */     }
/* 146:169 */     throw new IllegalArgumentException("Uknown elapsed time spec: " + type);
/* 147:    */   }
/* 148:    */   
/* 149:    */   private static double modFor(char type, int len)
/* 150:    */   {
/* 151:175 */     switch (type)
/* 152:    */     {
/* 153:    */     case 'h': 
/* 154:177 */       return 24.0D;
/* 155:    */     case 'm': 
/* 156:179 */       return 60.0D;
/* 157:    */     case 's': 
/* 158:181 */       return 60.0D;
/* 159:    */     case '0': 
/* 160:183 */       return Math.pow(10.0D, len);
/* 161:    */     }
/* 162:185 */     throw new IllegalArgumentException("Uknown elapsed time spec: " + type);
/* 163:    */   }
/* 164:    */   
/* 165:    */   public void formatValue(StringBuffer toAppendTo, Object value)
/* 166:    */   {
/* 167:192 */     double elapsed = ((Number)value).doubleValue();
/* 168:194 */     if (elapsed < 0.0D)
/* 169:    */     {
/* 170:195 */       toAppendTo.append('-');
/* 171:196 */       elapsed = -elapsed;
/* 172:    */     }
/* 173:199 */     Object[] parts = new Long[this.specs.size()];
/* 174:200 */     for (int i = 0; i < this.specs.size(); i++) {
/* 175:201 */       parts[i] = Long.valueOf(((TimeSpec)this.specs.get(i)).valueFor(elapsed));
/* 176:    */     }
/* 177:204 */     Formatter formatter = new Formatter(toAppendTo, Locale.ROOT);
/* 178:    */     try
/* 179:    */     {
/* 180:206 */       formatter.format(this.printfFmt, parts);
/* 181:    */     }
/* 182:    */     finally
/* 183:    */     {
/* 184:208 */       formatter.close();
/* 185:    */     }
/* 186:    */   }
/* 187:    */   
/* 188:    */   public void simpleValue(StringBuffer toAppendTo, Object value)
/* 189:    */   {
/* 190:218 */     formatValue(toAppendTo, value);
/* 191:    */   }
/* 192:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.format.CellElapsedFormatter
 * JD-Core Version:    0.7.0.1
 */