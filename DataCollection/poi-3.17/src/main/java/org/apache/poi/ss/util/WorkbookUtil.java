/*   1:    */ package org.apache.poi.ss.util;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.Removal;
/*   4:    */ 
/*   5:    */ public class WorkbookUtil
/*   6:    */ {
/*   7:    */   public static final String createSafeSheetName(String nameProposal)
/*   8:    */   {
/*   9: 49 */     return createSafeSheetName(nameProposal, ' ');
/*  10:    */   }
/*  11:    */   
/*  12:    */   public static final String createSafeSheetName(String nameProposal, char replaceChar)
/*  13:    */   {
/*  14: 72 */     if (nameProposal == null) {
/*  15: 73 */       return "null";
/*  16:    */     }
/*  17: 75 */     if (nameProposal.length() < 1) {
/*  18: 76 */       return "empty";
/*  19:    */     }
/*  20: 78 */     int length = Math.min(31, nameProposal.length());
/*  21: 79 */     String shortenname = nameProposal.substring(0, length);
/*  22: 80 */     StringBuilder result = new StringBuilder(shortenname);
/*  23: 81 */     for (int i = 0; i < length; i++)
/*  24:    */     {
/*  25: 82 */       char ch = result.charAt(i);
/*  26: 83 */       switch (ch)
/*  27:    */       {
/*  28:    */       case '\000': 
/*  29:    */       case '\003': 
/*  30:    */       case '*': 
/*  31:    */       case '/': 
/*  32:    */       case ':': 
/*  33:    */       case '?': 
/*  34:    */       case '[': 
/*  35:    */       case '\\': 
/*  36:    */       case ']': 
/*  37: 93 */         result.setCharAt(i, replaceChar);
/*  38: 94 */         break;
/*  39:    */       case '\'': 
/*  40: 96 */         if ((i == 0) || (i == length - 1)) {
/*  41: 97 */           result.setCharAt(i, replaceChar);
/*  42:    */         }
/*  43:    */         break;
/*  44:    */       }
/*  45:    */     }
/*  46:104 */     return result.toString();
/*  47:    */   }
/*  48:    */   
/*  49:    */   public static void validateSheetName(String sheetName)
/*  50:    */   {
/*  51:131 */     if (sheetName == null) {
/*  52:132 */       throw new IllegalArgumentException("sheetName must not be null");
/*  53:    */     }
/*  54:134 */     int len = sheetName.length();
/*  55:135 */     if ((len < 1) || (len > 31)) {
/*  56:136 */       throw new IllegalArgumentException("sheetName '" + sheetName + "' is invalid - character count MUST be greater than or equal to 1 and less than or equal to 31");
/*  57:    */     }
/*  58:140 */     for (int i = 0; i < len; i++)
/*  59:    */     {
/*  60:141 */       char ch = sheetName.charAt(i);
/*  61:142 */       switch (ch)
/*  62:    */       {
/*  63:    */       case '*': 
/*  64:    */       case '/': 
/*  65:    */       case ':': 
/*  66:    */       case '?': 
/*  67:    */       case '[': 
/*  68:    */       case '\\': 
/*  69:    */       case ']': 
/*  70:    */         break;
/*  71:    */       }
/*  72:155 */       throw new IllegalArgumentException("Invalid char (" + ch + ") found at index (" + i + ") in sheet name '" + sheetName + "'");
/*  73:    */     }
/*  74:158 */     if ((sheetName.charAt(0) == '\'') || (sheetName.charAt(len - 1) == '\'')) {
/*  75:159 */       throw new IllegalArgumentException("Invalid sheet name '" + sheetName + "'. Sheet names must not begin or end with (').");
/*  76:    */     }
/*  77:    */   }
/*  78:    */   
/*  79:    */   @Removal(version="3.18")
/*  80:    */   @Deprecated
/*  81:    */   public static void validateSheetState(int state)
/*  82:    */   {
/*  83:178 */     switch (state)
/*  84:    */     {
/*  85:    */     case 0: 
/*  86:    */       break;
/*  87:    */     case 1: 
/*  88:    */       break;
/*  89:    */     case 2: 
/*  90:    */       break;
/*  91:    */     default: 
/*  92:182 */       throw new IllegalArgumentException("Invalid sheet state : " + state + "\n" + "Sheet state must be one of the Workbook.SHEET_STATE_* constants");
/*  93:    */     }
/*  94:    */   }
/*  95:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.WorkbookUtil
 * JD-Core Version:    0.7.0.1
 */