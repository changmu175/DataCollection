/*   1:    */ package org.apache.poi.common.usermodel.fonts;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import java.util.Map.Entry;
/*   6:    */ import java.util.NavigableMap;
/*   7:    */ import java.util.TreeMap;
/*   8:    */ 
/*   9:    */ public enum FontGroup
/*  10:    */ {
/*  11: 36 */   LATIN,  EAST_ASIAN,  SYMBOL,  COMPLEX_SCRIPT;
/*  12:    */   
/*  13:    */   private static NavigableMap<Integer, Range> UCS_RANGES;
/*  14:    */   
/*  15:    */   public static class FontGroupRange
/*  16:    */   {
/*  17:    */     private int len;
/*  18:    */     private FontGroup fontGroup;
/*  19:    */     
/*  20:    */     public int getLength()
/*  21:    */     {
/*  22: 50 */       return this.len;
/*  23:    */     }
/*  24:    */     
/*  25:    */     public FontGroup getFontGroup()
/*  26:    */     {
/*  27: 53 */       return this.fontGroup;
/*  28:    */     }
/*  29:    */   }
/*  30:    */   
/*  31:    */   private static class Range
/*  32:    */   {
/*  33:    */     int upper;
/*  34:    */     FontGroup fontGroup;
/*  35:    */     
/*  36:    */     Range(int upper, FontGroup fontGroup)
/*  37:    */     {
/*  38: 61 */       this.upper = upper;
/*  39: 62 */       this.fontGroup = fontGroup;
/*  40:    */     }
/*  41:    */   }
/*  42:    */   
/*  43:    */   static
/*  44:    */   {
/*  45: 69 */     UCS_RANGES = new TreeMap();
/*  46: 70 */     UCS_RANGES.put(Integer.valueOf(0), new Range(127, LATIN));
/*  47: 71 */     UCS_RANGES.put(Integer.valueOf(128), new Range(166, LATIN));
/*  48: 72 */     UCS_RANGES.put(Integer.valueOf(169), new Range(175, LATIN));
/*  49: 73 */     UCS_RANGES.put(Integer.valueOf(178), new Range(179, LATIN));
/*  50: 74 */     UCS_RANGES.put(Integer.valueOf(181), new Range(214, LATIN));
/*  51: 75 */     UCS_RANGES.put(Integer.valueOf(216), new Range(246, LATIN));
/*  52: 76 */     UCS_RANGES.put(Integer.valueOf(248), new Range(1423, LATIN));
/*  53: 77 */     UCS_RANGES.put(Integer.valueOf(1424), new Range(1871, COMPLEX_SCRIPT));
/*  54: 78 */     UCS_RANGES.put(Integer.valueOf(1920), new Range(1983, COMPLEX_SCRIPT));
/*  55: 79 */     UCS_RANGES.put(Integer.valueOf(2304), new Range(4255, COMPLEX_SCRIPT));
/*  56: 80 */     UCS_RANGES.put(Integer.valueOf(4256), new Range(4351, LATIN));
/*  57: 81 */     UCS_RANGES.put(Integer.valueOf(4608), new Range(4991, LATIN));
/*  58: 82 */     UCS_RANGES.put(Integer.valueOf(5024), new Range(6015, LATIN));
/*  59: 83 */     UCS_RANGES.put(Integer.valueOf(7424), new Range(7551, LATIN));
/*  60: 84 */     UCS_RANGES.put(Integer.valueOf(7680), new Range(8191, LATIN));
/*  61: 85 */     UCS_RANGES.put(Integer.valueOf(6016), new Range(6319, COMPLEX_SCRIPT));
/*  62: 86 */     UCS_RANGES.put(Integer.valueOf(8192), new Range(8203, LATIN));
/*  63: 87 */     UCS_RANGES.put(Integer.valueOf(8204), new Range(8207, COMPLEX_SCRIPT));
/*  64:    */     
/*  65:    */ 
/*  66:    */ 
/*  67: 91 */     UCS_RANGES.put(Integer.valueOf(8208), new Range(8233, LATIN));
/*  68: 92 */     UCS_RANGES.put(Integer.valueOf(8234), new Range(8239, COMPLEX_SCRIPT));
/*  69: 93 */     UCS_RANGES.put(Integer.valueOf(8240), new Range(8262, LATIN));
/*  70: 94 */     UCS_RANGES.put(Integer.valueOf(8266), new Range(9311, LATIN));
/*  71: 95 */     UCS_RANGES.put(Integer.valueOf(9840), new Range(9841, COMPLEX_SCRIPT));
/*  72: 96 */     UCS_RANGES.put(Integer.valueOf(10176), new Range(11263, LATIN));
/*  73: 97 */     UCS_RANGES.put(Integer.valueOf(12441), new Range(12442, EAST_ASIAN));
/*  74: 98 */     UCS_RANGES.put(Integer.valueOf(55349), new Range(55349, LATIN));
/*  75: 99 */     UCS_RANGES.put(Integer.valueOf(61440), new Range(61695, SYMBOL));
/*  76:100 */     UCS_RANGES.put(Integer.valueOf(64256), new Range(64279, LATIN));
/*  77:101 */     UCS_RANGES.put(Integer.valueOf(64285), new Range(64335, COMPLEX_SCRIPT));
/*  78:102 */     UCS_RANGES.put(Integer.valueOf(65104), new Range(65135, LATIN));
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static List<FontGroupRange> getFontGroupRanges(String runText)
/*  82:    */   {
/*  83:114 */     List<FontGroupRange> ttrList = new ArrayList();
/*  84:115 */     FontGroupRange ttrLast = null;
/*  85:116 */     int rlen = runText != null ? runText.length() : 0;
/*  86:    */     int charCount;
/*  87:117 */     for (int i = 0; i < rlen; i += charCount)
/*  88:    */     {
/*  89:118 */       int cp = runText.codePointAt(i);
/*  90:119 */       charCount = Character.charCount(cp);
/*  91:    */       FontGroup tt;
/*  92:    */       FontGroup tt;
/*  93:123 */       if ((ttrLast != null) && (" \n\r".indexOf(cp) > -1)) {
/*  94:124 */         tt = ttrLast.fontGroup;
/*  95:    */       } else {
/*  96:126 */         tt = lookup(cp);
/*  97:    */       }
/*  98:129 */       if ((ttrLast == null) || (ttrLast.fontGroup != tt))
/*  99:    */       {
/* 100:130 */         ttrLast = new FontGroupRange();
/* 101:131 */         ttrLast.fontGroup = tt;
/* 102:132 */         ttrList.add(ttrLast);
/* 103:    */       }
/* 104:134 */       FontGroupRange.access$112(ttrLast, charCount);
/* 105:    */     }
/* 106:136 */     return ttrList;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public static FontGroup getFontGroupFirst(String runText)
/* 110:    */   {
/* 111:140 */     return (runText == null) || (runText.isEmpty()) ? LATIN : lookup(runText.codePointAt(0));
/* 112:    */   }
/* 113:    */   
/* 114:    */   private static FontGroup lookup(int codepoint)
/* 115:    */   {
/* 116:145 */     Map.Entry<Integer, Range> entry = UCS_RANGES.floorEntry(Integer.valueOf(codepoint));
/* 117:146 */     Range range = entry != null ? (Range)entry.getValue() : null;
/* 118:147 */     return (range != null) && (codepoint <= range.upper) ? range.fontGroup : EAST_ASIAN;
/* 119:    */   }
/* 120:    */   
/* 121:    */   private FontGroup() {}
/* 122:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.common.usermodel.fonts.FontGroup
 * JD-Core Version:    0.7.0.1
 */