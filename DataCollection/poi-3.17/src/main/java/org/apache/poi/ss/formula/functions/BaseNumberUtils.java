/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ public class BaseNumberUtils
/*  4:   */ {
/*  5:   */   public static double convertToDecimal(String value, int base, int maxNumberOfPlaces)
/*  6:   */     throws IllegalArgumentException
/*  7:   */   {
/*  8:28 */     if ((value == null) || (value.length() == 0)) {
/*  9:29 */       return 0.0D;
/* 10:   */     }
/* 11:32 */     long stringLength = value.length();
/* 12:33 */     if (stringLength > maxNumberOfPlaces) {
/* 13:34 */       throw new IllegalArgumentException();
/* 14:   */     }
/* 15:37 */     double decimalValue = 0.0D;
/* 16:   */     
/* 17:39 */     long signedDigit = 0L;
/* 18:40 */     boolean hasSignedDigit = true;
/* 19:41 */     char[] characters = value.toCharArray();
/* 20:42 */     for (char character : characters)
/* 21:   */     {
/* 22:   */       long digit;
/* 23:   */       long digit;
/* 24:45 */       if (('0' <= character) && (character <= '9'))
/* 25:   */       {
/* 26:46 */         digit = character - '0';
/* 27:   */       }
/* 28:   */       else
/* 29:   */       {
/* 30:   */         long digit;
/* 31:47 */         if (('A' <= character) && (character <= 'Z'))
/* 32:   */         {
/* 33:48 */           digit = 10 + (character - 'A');
/* 34:   */         }
/* 35:   */         else
/* 36:   */         {
/* 37:   */           long digit;
/* 38:49 */           if (('a' <= character) && (character <= 'z')) {
/* 39:50 */             digit = 10 + (character - 'a');
/* 40:   */           } else {
/* 41:52 */             digit = base;
/* 42:   */           }
/* 43:   */         }
/* 44:   */       }
/* 45:55 */       if (digit < base)
/* 46:   */       {
/* 47:56 */         if (hasSignedDigit)
/* 48:   */         {
/* 49:57 */           hasSignedDigit = false;
/* 50:58 */           signedDigit = digit;
/* 51:   */         }
/* 52:60 */         decimalValue = decimalValue * base + digit;
/* 53:   */       }
/* 54:   */       else
/* 55:   */       {
/* 56:62 */         throw new IllegalArgumentException("character not allowed");
/* 57:   */       }
/* 58:   */     }
/* 59:66 */     boolean isNegative = (!hasSignedDigit) && (stringLength == maxNumberOfPlaces) && (signedDigit >= base / 2);
/* 60:67 */     if (isNegative)
/* 61:   */     {
/* 62:68 */       decimalValue = getTwoComplement(base, maxNumberOfPlaces, decimalValue);
/* 63:69 */       decimalValue *= -1.0D;
/* 64:   */     }
/* 65:72 */     return decimalValue;
/* 66:   */   }
/* 67:   */   
/* 68:   */   private static double getTwoComplement(double base, double maxNumberOfPlaces, double decimalValue)
/* 69:   */   {
/* 70:76 */     return Math.pow(base, maxNumberOfPlaces) - decimalValue;
/* 71:   */   }
/* 72:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.BaseNumberUtils
 * JD-Core Version:    0.7.0.1
 */