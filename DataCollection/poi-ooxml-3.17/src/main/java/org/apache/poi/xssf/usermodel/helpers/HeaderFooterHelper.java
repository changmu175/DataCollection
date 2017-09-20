/*   1:    */ package org.apache.poi.xssf.usermodel.helpers;
/*   2:    */ 
/*   3:    */ public class HeaderFooterHelper
/*   4:    */ {
/*   5:    */   private static final String HeaderFooterEntity_L = "&L";
/*   6:    */   private static final String HeaderFooterEntity_C = "&C";
/*   7:    */   private static final String HeaderFooterEntity_R = "&R";
/*   8:    */   public static final String HeaderFooterEntity_File = "&F";
/*   9:    */   public static final String HeaderFooterEntity_Date = "&D";
/*  10:    */   public static final String HeaderFooterEntity_Time = "&T";
/*  11:    */   
/*  12:    */   public String getLeftSection(String string)
/*  13:    */   {
/*  14: 35 */     return getParts(string)[0];
/*  15:    */   }
/*  16:    */   
/*  17:    */   public String getCenterSection(String string)
/*  18:    */   {
/*  19: 38 */     return getParts(string)[1];
/*  20:    */   }
/*  21:    */   
/*  22:    */   public String getRightSection(String string)
/*  23:    */   {
/*  24: 41 */     return getParts(string)[2];
/*  25:    */   }
/*  26:    */   
/*  27:    */   public String setLeftSection(String string, String newLeft)
/*  28:    */   {
/*  29: 45 */     String[] parts = getParts(string);
/*  30: 46 */     parts[0] = newLeft;
/*  31: 47 */     return joinParts(parts);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public String setCenterSection(String string, String newCenter)
/*  35:    */   {
/*  36: 50 */     String[] parts = getParts(string);
/*  37: 51 */     parts[1] = newCenter;
/*  38: 52 */     return joinParts(parts);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public String setRightSection(String string, String newRight)
/*  42:    */   {
/*  43: 55 */     String[] parts = getParts(string);
/*  44: 56 */     parts[2] = newRight;
/*  45: 57 */     return joinParts(parts);
/*  46:    */   }
/*  47:    */   
/*  48:    */   private String[] getParts(String string)
/*  49:    */   {
/*  50: 64 */     String[] parts = { "", "", "" };
/*  51: 65 */     if (string == null) {
/*  52: 66 */       return parts;
/*  53:    */     }
/*  54: 71 */     int lAt = 0;
/*  55: 72 */     int cAt = 0;
/*  56: 73 */     int rAt = 0;
/*  57: 79 */     while (((lAt = string.indexOf("&L")) > -2) && ((cAt = string.indexOf("&C")) > -2) && ((rAt = string.indexOf("&R")) > -2) && ((lAt > -1) || (cAt > -1) || (rAt > -1))) {
/*  58: 83 */       if ((rAt > cAt) && (rAt > lAt))
/*  59:    */       {
/*  60: 84 */         parts[2] = string.substring(rAt + "&R".length());
/*  61: 85 */         string = string.substring(0, rAt);
/*  62:    */       }
/*  63: 86 */       else if ((cAt > rAt) && (cAt > lAt))
/*  64:    */       {
/*  65: 87 */         parts[1] = string.substring(cAt + "&C".length());
/*  66: 88 */         string = string.substring(0, cAt);
/*  67:    */       }
/*  68:    */       else
/*  69:    */       {
/*  70: 90 */         parts[0] = string.substring(lAt + "&L".length());
/*  71: 91 */         string = string.substring(0, lAt);
/*  72:    */       }
/*  73:    */     }
/*  74: 95 */     return parts;
/*  75:    */   }
/*  76:    */   
/*  77:    */   private String joinParts(String[] parts)
/*  78:    */   {
/*  79: 98 */     return joinParts(parts[0], parts[1], parts[2]);
/*  80:    */   }
/*  81:    */   
/*  82:    */   private String joinParts(String l, String c, String r)
/*  83:    */   {
/*  84:101 */     StringBuffer ret = new StringBuffer();
/*  85:104 */     if (c.length() > 0)
/*  86:    */     {
/*  87:105 */       ret.append("&C");
/*  88:106 */       ret.append(c);
/*  89:    */     }
/*  90:108 */     if (l.length() > 0)
/*  91:    */     {
/*  92:109 */       ret.append("&L");
/*  93:110 */       ret.append(l);
/*  94:    */     }
/*  95:112 */     if (r.length() > 0)
/*  96:    */     {
/*  97:113 */       ret.append("&R");
/*  98:114 */       ret.append(r);
/*  99:    */     }
/* 100:117 */     return ret.toString();
/* 101:    */   }
/* 102:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.helpers.HeaderFooterHelper
 * JD-Core Version:    0.7.0.1
 */