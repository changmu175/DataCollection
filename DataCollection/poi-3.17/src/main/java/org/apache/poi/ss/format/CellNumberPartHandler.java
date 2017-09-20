/*   1:    */ package org.apache.poi.ss.format;
/*   2:    */ 
/*   3:    */ import java.util.LinkedList;
/*   4:    */ import java.util.List;
/*   5:    */ import java.util.ListIterator;
/*   6:    */ import java.util.regex.Matcher;
/*   7:    */ import org.apache.poi.util.Internal;
/*   8:    */ 
/*   9:    */ @Internal
/*  10:    */ public class CellNumberPartHandler
/*  11:    */   implements CellFormatPart.PartHandler
/*  12:    */ {
/*  13:    */   private char insertSignForExponent;
/*  14: 34 */   private double scale = 1.0D;
/*  15:    */   private CellNumberFormatter.Special decimalPoint;
/*  16:    */   private CellNumberFormatter.Special slash;
/*  17:    */   private CellNumberFormatter.Special exponent;
/*  18:    */   private CellNumberFormatter.Special numerator;
/*  19: 39 */   private final List<CellNumberFormatter.Special> specials = new LinkedList();
/*  20:    */   private boolean improperFraction;
/*  21:    */   
/*  22:    */   public String handlePart(Matcher m, String part, CellFormatType type, StringBuffer descBuf)
/*  23:    */   {
/*  24: 43 */     int pos = descBuf.length();
/*  25: 44 */     char firstCh = part.charAt(0);
/*  26: 45 */     switch (firstCh)
/*  27:    */     {
/*  28:    */     case 'E': 
/*  29:    */     case 'e': 
/*  30: 51 */       if ((this.exponent == null) && (this.specials.size() > 0))
/*  31:    */       {
/*  32: 52 */         this.exponent = new CellNumberFormatter.Special('.', pos);
/*  33: 53 */         this.specials.add(this.exponent);
/*  34: 54 */         this.insertSignForExponent = part.charAt(1);
/*  35: 55 */         return part.substring(0, 1);
/*  36:    */       }
/*  37:    */       break;
/*  38:    */     case '#': 
/*  39:    */     case '0': 
/*  40:    */     case '?': 
/*  41: 62 */       if (this.insertSignForExponent != 0)
/*  42:    */       {
/*  43: 63 */         this.specials.add(new CellNumberFormatter.Special(this.insertSignForExponent, pos));
/*  44: 64 */         descBuf.append(this.insertSignForExponent);
/*  45: 65 */         this.insertSignForExponent = '\000';
/*  46: 66 */         pos++;
/*  47:    */       }
/*  48: 68 */       for (int i = 0; i < part.length(); i++)
/*  49:    */       {
/*  50: 69 */         char ch = part.charAt(i);
/*  51: 70 */         this.specials.add(new CellNumberFormatter.Special(ch, pos + i));
/*  52:    */       }
/*  53: 72 */       break;
/*  54:    */     case '.': 
/*  55: 75 */       if ((this.decimalPoint == null) && (this.specials.size() > 0))
/*  56:    */       {
/*  57: 76 */         this.decimalPoint = new CellNumberFormatter.Special('.', pos);
/*  58: 77 */         this.specials.add(this.decimalPoint);
/*  59:    */       }
/*  60:    */       break;
/*  61:    */     case '/': 
/*  62: 83 */       if ((this.slash == null) && (this.specials.size() > 0))
/*  63:    */       {
/*  64: 84 */         this.numerator = previousNumber();
/*  65:    */         
/*  66:    */ 
/*  67: 87 */         this.improperFraction |= this.numerator == firstDigit(this.specials);
/*  68: 88 */         this.slash = new CellNumberFormatter.Special('.', pos);
/*  69: 89 */         this.specials.add(this.slash);
/*  70:    */       }
/*  71:    */       break;
/*  72:    */     case '%': 
/*  73: 95 */       this.scale *= 100.0D;
/*  74: 96 */       break;
/*  75:    */     default: 
/*  76: 99 */       return null;
/*  77:    */     }
/*  78:101 */     return part;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public double getScale()
/*  82:    */   {
/*  83:105 */     return this.scale;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public CellNumberFormatter.Special getDecimalPoint()
/*  87:    */   {
/*  88:109 */     return this.decimalPoint;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public CellNumberFormatter.Special getSlash()
/*  92:    */   {
/*  93:113 */     return this.slash;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public CellNumberFormatter.Special getExponent()
/*  97:    */   {
/*  98:117 */     return this.exponent;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public CellNumberFormatter.Special getNumerator()
/* 102:    */   {
/* 103:121 */     return this.numerator;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public List<CellNumberFormatter.Special> getSpecials()
/* 107:    */   {
/* 108:125 */     return this.specials;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public boolean isImproperFraction()
/* 112:    */   {
/* 113:129 */     return this.improperFraction;
/* 114:    */   }
/* 115:    */   
/* 116:    */   private CellNumberFormatter.Special previousNumber()
/* 117:    */   {
/* 118:133 */     ListIterator<CellNumberFormatter.Special> it = this.specials.listIterator(this.specials.size());
/* 119:134 */     while (it.hasPrevious())
/* 120:    */     {
/* 121:135 */       CellNumberFormatter.Special s = (CellNumberFormatter.Special)it.previous();
/* 122:136 */       if (isDigitFmt(s))
/* 123:    */       {
/* 124:137 */         CellNumberFormatter.Special last = s;
/* 125:138 */         while (it.hasPrevious())
/* 126:    */         {
/* 127:139 */           s = (CellNumberFormatter.Special)it.previous();
/* 128:141 */           if ((last.pos - s.pos > 1) || (!isDigitFmt(s))) {
/* 129:    */             break;
/* 130:    */           }
/* 131:144 */           last = s;
/* 132:    */         }
/* 133:146 */         return last;
/* 134:    */       }
/* 135:    */     }
/* 136:149 */     return null;
/* 137:    */   }
/* 138:    */   
/* 139:    */   private static boolean isDigitFmt(CellNumberFormatter.Special s)
/* 140:    */   {
/* 141:153 */     return (s.ch == '0') || (s.ch == '?') || (s.ch == '#');
/* 142:    */   }
/* 143:    */   
/* 144:    */   private static CellNumberFormatter.Special firstDigit(List<CellNumberFormatter.Special> specials)
/* 145:    */   {
/* 146:157 */     for (CellNumberFormatter.Special s : specials) {
/* 147:158 */       if (isDigitFmt(s)) {
/* 148:159 */         return s;
/* 149:    */       }
/* 150:    */     }
/* 151:162 */     return null;
/* 152:    */   }
/* 153:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.format.CellNumberPartHandler
 * JD-Core Version:    0.7.0.1
 */