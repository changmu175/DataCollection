/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.io.OutputStream;
/*   7:    */ 
/*   8:    */ public abstract class LZWDecompresser
/*   9:    */ {
/*  10:    */   private final boolean maskMeansCompressed;
/*  11:    */   private final int codeLengthIncrease;
/*  12:    */   private final boolean positionIsBigEndian;
/*  13:    */   
/*  14:    */   protected LZWDecompresser(boolean maskMeansCompressed, int codeLengthIncrease, boolean positionIsBigEndian)
/*  15:    */   {
/*  16: 54 */     this.maskMeansCompressed = maskMeansCompressed;
/*  17: 55 */     this.codeLengthIncrease = codeLengthIncrease;
/*  18: 56 */     this.positionIsBigEndian = positionIsBigEndian;
/*  19:    */   }
/*  20:    */   
/*  21:    */   protected abstract int populateDictionary(byte[] paramArrayOfByte);
/*  22:    */   
/*  23:    */   protected abstract int adjustDictionaryOffset(int paramInt);
/*  24:    */   
/*  25:    */   public byte[] decompress(InputStream src)
/*  26:    */     throws IOException
/*  27:    */   {
/*  28: 80 */     ByteArrayOutputStream res = new ByteArrayOutputStream();
/*  29: 81 */     decompress(src, res);
/*  30: 82 */     return res.toByteArray();
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void decompress(InputStream src, OutputStream res)
/*  34:    */     throws IOException
/*  35:    */   {
/*  36:116 */     byte[] buffer = new byte[4096];
/*  37:117 */     int pos = populateDictionary(buffer);
/*  38:    */     
/*  39:    */ 
/*  40:    */ 
/*  41:    */ 
/*  42:122 */     byte[] dataB = new byte[16 + this.codeLengthIncrease];
/*  43:    */     int flag;
/*  44:    */     label268:
/*  45:132 */     while ((flag = src.read()) != -1) {
/*  46:134 */       for (int mask = 1;; mask <<= 1)
/*  47:    */       {
/*  48:134 */         if (mask >= 256) {
/*  49:    */           break label268;
/*  50:    */         }
/*  51:137 */         boolean isMaskSet = (flag & mask) > 0;
/*  52:138 */         if ((isMaskSet ^ this.maskMeansCompressed))
/*  53:    */         {
/*  54:    */           int dataI;
/*  55:140 */           if ((dataI = src.read()) != -1)
/*  56:    */           {
/*  57:142 */             buffer[(pos & 0xFFF)] = fromInt(dataI);
/*  58:143 */             pos++;
/*  59:    */             
/*  60:145 */             res.write(new byte[] { fromInt(dataI) });
/*  61:    */           }
/*  62:    */         }
/*  63:    */         else
/*  64:    */         {
/*  65:150 */           int dataIPt1 = src.read();
/*  66:151 */           int dataIPt2 = src.read();
/*  67:152 */           if ((dataIPt1 == -1) || (dataIPt2 == -1)) {
/*  68:    */             break;
/*  69:    */           }
/*  70:158 */           int len = (dataIPt2 & 0xF) + this.codeLengthIncrease;
/*  71:    */           int pntr;
/*  72:159 */           if (this.positionIsBigEndian) {
/*  73:160 */             pntr = (dataIPt1 << 4) + (dataIPt2 >> 4);
/*  74:    */           } else {
/*  75:162 */             pntr = dataIPt1 + ((dataIPt2 & 0xF0) << 4);
/*  76:    */           }
/*  77:166 */           int pntr = adjustDictionaryOffset(pntr);
/*  78:169 */           for (int i = 0; i < len; i++)
/*  79:    */           {
/*  80:170 */             dataB[i] = buffer[(pntr + i & 0xFFF)];
/*  81:171 */             buffer[(pos + i & 0xFFF)] = dataB[i];
/*  82:    */           }
/*  83:173 */           res.write(dataB, 0, len);
/*  84:    */           
/*  85:    */ 
/*  86:176 */           pos += len;
/*  87:    */         }
/*  88:    */       }
/*  89:    */     }
/*  90:    */   }
/*  91:    */   
/*  92:    */   public static byte fromInt(int b)
/*  93:    */   {
/*  94:188 */     if (b < 128) {
/*  95:188 */       return (byte)b;
/*  96:    */     }
/*  97:189 */     return (byte)(b - 256);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public static int fromByte(byte b)
/* 101:    */   {
/* 102:197 */     if (b >= 0) {
/* 103:198 */       return b;
/* 104:    */     }
/* 105:200 */     return b + 256;
/* 106:    */   }
/* 107:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.LZWDecompresser
 * JD-Core Version:    0.7.0.1
 */