/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.FileInputStream;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.InputStream;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ import java.util.List;
/*  10:    */ 
/*  11:    */ public class HexRead
/*  12:    */ {
/*  13:    */   public static byte[] readData(String filename)
/*  14:    */     throws IOException
/*  15:    */   {
/*  16: 38 */     File file = new File(filename);
/*  17: 39 */     InputStream stream = new FileInputStream(file);
/*  18:    */     try
/*  19:    */     {
/*  20: 41 */       return readData(stream, -1);
/*  21:    */     }
/*  22:    */     finally
/*  23:    */     {
/*  24: 43 */       stream.close();
/*  25:    */     }
/*  26:    */   }
/*  27:    */   
/*  28:    */   public static byte[] readData(InputStream stream, String section)
/*  29:    */     throws IOException
/*  30:    */   {
/*  31:    */     try
/*  32:    */     {
/*  33: 58 */       StringBuffer sectionText = new StringBuffer();
/*  34: 59 */       boolean inSection = false;
/*  35: 60 */       int c = stream.read();
/*  36: 61 */       while (c != -1)
/*  37:    */       {
/*  38: 62 */         switch (c)
/*  39:    */         {
/*  40:    */         case 91: 
/*  41: 64 */           inSection = true;
/*  42: 65 */           break;
/*  43:    */         case 10: 
/*  44:    */         case 13: 
/*  45: 68 */           inSection = false;
/*  46: 69 */           sectionText = new StringBuffer();
/*  47: 70 */           break;
/*  48:    */         case 93: 
/*  49: 72 */           inSection = false;
/*  50: 73 */           if (sectionText.toString().equals(section)) {
/*  51: 73 */             return readData(stream, 91);
/*  52:    */           }
/*  53: 74 */           sectionText = new StringBuffer();
/*  54: 75 */           break;
/*  55:    */         default: 
/*  56: 77 */           if (inSection) {
/*  57: 77 */             sectionText.append((char)c);
/*  58:    */           }
/*  59:    */           break;
/*  60:    */         }
/*  61: 79 */         c = stream.read();
/*  62:    */       }
/*  63:    */     }
/*  64:    */     finally
/*  65:    */     {
/*  66: 82 */       stream.close();
/*  67:    */     }
/*  68: 85 */     throw new IOException("Section '" + section + "' not found");
/*  69:    */   }
/*  70:    */   
/*  71:    */   public static byte[] readData(String filename, String section)
/*  72:    */     throws IOException
/*  73:    */   {
/*  74: 89 */     return readData(new FileInputStream(filename), section);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public static byte[] readData(InputStream stream, int eofChar)
/*  78:    */     throws IOException
/*  79:    */   {
/*  80: 96 */     int characterCount = 0;
/*  81: 97 */     byte b = 0;
/*  82: 98 */     List<Byte> bytes = new ArrayList();
/*  83: 99 */     char a = 'W';
/*  84:100 */     char A = '7';
/*  85:    */     for (;;)
/*  86:    */     {
/*  87:102 */       int count = stream.read();
/*  88:103 */       int digitValue = -1;
/*  89:104 */       if ((48 <= count) && (count <= 57)) {
/*  90:105 */         digitValue = count - 48;
/*  91:106 */       } else if ((65 <= count) && (count <= 70)) {
/*  92:107 */         digitValue = count - 55;
/*  93:108 */       } else if ((97 <= count) && (count <= 102)) {
/*  94:109 */         digitValue = count - 87;
/*  95:110 */       } else if (35 == count) {
/*  96:111 */         readToEOL(stream);
/*  97:    */       } else {
/*  98:112 */         if ((-1 == count) || (eofChar == count)) {
/*  99:    */           break;
/* 100:    */         }
/* 101:    */       }
/* 102:117 */       if (digitValue != -1)
/* 103:    */       {
/* 104:118 */         b = (byte)(b << 4);
/* 105:119 */         b = (byte)(b + (byte)digitValue);
/* 106:120 */         characterCount++;
/* 107:121 */         if (characterCount == 2)
/* 108:    */         {
/* 109:122 */           bytes.add(Byte.valueOf(b));
/* 110:123 */           characterCount = 0;
/* 111:124 */           b = 0;
/* 112:    */         }
/* 113:    */       }
/* 114:    */     }
/* 115:128 */     Byte[] polished = (Byte[])bytes.toArray(new Byte[bytes.size()]);
/* 116:129 */     byte[] rval = new byte[polished.length];
/* 117:130 */     for (int j = 0; j < polished.length; j++) {
/* 118:131 */       rval[j] = polished[j].byteValue();
/* 119:    */     }
/* 120:133 */     return rval;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public static byte[] readFromString(String data)
/* 124:    */   {
/* 125:    */     try
/* 126:    */     {
/* 127:138 */       return readData(new ByteArrayInputStream(data.getBytes(StringUtil.UTF8)), -1);
/* 128:    */     }
/* 129:    */     catch (IOException e)
/* 130:    */     {
/* 131:140 */       throw new RuntimeException(e);
/* 132:    */     }
/* 133:    */   }
/* 134:    */   
/* 135:    */   private static void readToEOL(InputStream stream)
/* 136:    */     throws IOException
/* 137:    */   {
/* 138:145 */     int c = stream.read();
/* 139:146 */     while ((c != -1) && (c != 10) && (c != 13)) {
/* 140:147 */       c = stream.read();
/* 141:    */     }
/* 142:    */   }
/* 143:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.HexRead
 * JD-Core Version:    0.7.0.1
 */