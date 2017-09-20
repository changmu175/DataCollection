/*   1:    */ package org.apache.poi.ss.formula;
/*   2:    */ 
/*   3:    */ import java.util.regex.Matcher;
/*   4:    */ import java.util.regex.Pattern;
/*   5:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*   6:    */ import org.apache.poi.ss.util.CellReference;
/*   7:    */ 
/*   8:    */ public final class SheetNameFormatter
/*   9:    */ {
/*  10:    */   private static final char DELIMITER = '\'';
/*  11: 38 */   private static final Pattern CELL_REF_PATTERN = Pattern.compile("([A-Za-z]+)([0-9]+)");
/*  12:    */   
/*  13:    */   public static String format(String rawSheetName)
/*  14:    */   {
/*  15: 50 */     StringBuffer sb = new StringBuffer(rawSheetName.length() + 2);
/*  16: 51 */     appendFormat(sb, rawSheetName);
/*  17: 52 */     return sb.toString();
/*  18:    */   }
/*  19:    */   
/*  20:    */   public static void appendFormat(StringBuffer out, String rawSheetName)
/*  21:    */   {
/*  22: 61 */     boolean needsQuotes = needsDelimiting(rawSheetName);
/*  23: 62 */     if (needsQuotes)
/*  24:    */     {
/*  25: 63 */       out.append('\'');
/*  26: 64 */       appendAndEscape(out, rawSheetName);
/*  27: 65 */       out.append('\'');
/*  28:    */     }
/*  29:    */     else
/*  30:    */     {
/*  31: 67 */       out.append(rawSheetName);
/*  32:    */     }
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static void appendFormat(StringBuffer out, String workbookName, String rawSheetName)
/*  36:    */   {
/*  37: 71 */     boolean needsQuotes = (needsDelimiting(workbookName)) || (needsDelimiting(rawSheetName));
/*  38: 72 */     if (needsQuotes)
/*  39:    */     {
/*  40: 73 */       out.append('\'');
/*  41: 74 */       out.append('[');
/*  42: 75 */       appendAndEscape(out, workbookName.replace('[', '(').replace(']', ')'));
/*  43: 76 */       out.append(']');
/*  44: 77 */       appendAndEscape(out, rawSheetName);
/*  45: 78 */       out.append('\'');
/*  46:    */     }
/*  47:    */     else
/*  48:    */     {
/*  49: 80 */       out.append('[');
/*  50: 81 */       out.append(workbookName);
/*  51: 82 */       out.append(']');
/*  52: 83 */       out.append(rawSheetName);
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   private static void appendAndEscape(StringBuffer sb, String rawSheetName)
/*  57:    */   {
/*  58: 88 */     int len = rawSheetName.length();
/*  59: 89 */     for (int i = 0; i < len; i++)
/*  60:    */     {
/*  61: 90 */       char ch = rawSheetName.charAt(i);
/*  62: 91 */       if (ch == '\'') {
/*  63: 93 */         sb.append('\'');
/*  64:    */       }
/*  65: 95 */       sb.append(ch);
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   private static boolean needsDelimiting(String rawSheetName)
/*  70:    */   {
/*  71:100 */     int len = rawSheetName.length();
/*  72:101 */     if (len < 1) {
/*  73:102 */       throw new RuntimeException("Zero length string is an invalid sheet name");
/*  74:    */     }
/*  75:104 */     if (Character.isDigit(rawSheetName.charAt(0))) {
/*  76:106 */       return true;
/*  77:    */     }
/*  78:108 */     for (int i = 0; i < len; i++)
/*  79:    */     {
/*  80:109 */       char ch = rawSheetName.charAt(i);
/*  81:110 */       if (isSpecialChar(ch)) {
/*  82:111 */         return true;
/*  83:    */       }
/*  84:    */     }
/*  85:114 */     if ((Character.isLetter(rawSheetName.charAt(0))) && (Character.isDigit(rawSheetName.charAt(len - 1)))) {
/*  86:117 */       if (nameLooksLikePlainCellReference(rawSheetName)) {
/*  87:118 */         return true;
/*  88:    */       }
/*  89:    */     }
/*  90:121 */     if (nameLooksLikeBooleanLiteral(rawSheetName)) {
/*  91:122 */       return true;
/*  92:    */     }
/*  93:126 */     return false;
/*  94:    */   }
/*  95:    */   
/*  96:    */   private static boolean nameLooksLikeBooleanLiteral(String rawSheetName)
/*  97:    */   {
/*  98:130 */     switch (rawSheetName.charAt(0))
/*  99:    */     {
/* 100:    */     case 'T': 
/* 101:    */     case 't': 
/* 102:132 */       return "TRUE".equalsIgnoreCase(rawSheetName);
/* 103:    */     case 'F': 
/* 104:    */     case 'f': 
/* 105:134 */       return "FALSE".equalsIgnoreCase(rawSheetName);
/* 106:    */     }
/* 107:136 */     return false;
/* 108:    */   }
/* 109:    */   
/* 110:    */   static boolean isSpecialChar(char ch)
/* 111:    */   {
/* 112:145 */     if (Character.isLetterOrDigit(ch)) {
/* 113:146 */       return false;
/* 114:    */     }
/* 115:148 */     switch (ch)
/* 116:    */     {
/* 117:    */     case '.': 
/* 118:    */     case '_': 
/* 119:151 */       return false;
/* 120:    */     case '\t': 
/* 121:    */     case '\n': 
/* 122:    */     case '\r': 
/* 123:155 */       throw new RuntimeException("Illegal character (0x" + Integer.toHexString(ch) + ") found in sheet name");
/* 124:    */     }
/* 125:158 */     return true;
/* 126:    */   }
/* 127:    */   
/* 128:    */   static boolean cellReferenceIsWithinRange(String lettersPrefix, String numbersSuffix)
/* 129:    */   {
/* 130:187 */     return CellReference.cellReferenceIsWithinRange(lettersPrefix, numbersSuffix, SpreadsheetVersion.EXCEL97);
/* 131:    */   }
/* 132:    */   
/* 133:    */   static boolean nameLooksLikePlainCellReference(String rawSheetName)
/* 134:    */   {
/* 135:213 */     Matcher matcher = CELL_REF_PATTERN.matcher(rawSheetName);
/* 136:214 */     if (!matcher.matches()) {
/* 137:215 */       return false;
/* 138:    */     }
/* 139:219 */     String lettersPrefix = matcher.group(1);
/* 140:220 */     String numbersSuffix = matcher.group(2);
/* 141:221 */     return cellReferenceIsWithinRange(lettersPrefix, numbersSuffix);
/* 142:    */   }
/* 143:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.SheetNameFormatter
 * JD-Core Version:    0.7.0.1
 */