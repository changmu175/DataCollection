/*  1:   */ package org.apache.poi.poifs.crypt;
/*  2:   */ 
/*  3:   */ public enum EncryptionMode
/*  4:   */ {
/*  5:30 */   binaryRC4("org.apache.poi.poifs.crypt.binaryrc4.BinaryRC4EncryptionInfoBuilder", 1, 1, 0),  cryptoAPI("org.apache.poi.poifs.crypt.cryptoapi.CryptoAPIEncryptionInfoBuilder", 4, 2, 4),  standard("org.apache.poi.poifs.crypt.standard.StandardEncryptionInfoBuilder", 4, 2, 36),  agile("org.apache.poi.poifs.crypt.agile.AgileEncryptionInfoBuilder", 4, 4, 64),  xor("org.apache.poi.poifs.crypt.xor.XOREncryptionInfoBuilder", 0, 0, 0);
/*  6:   */   
/*  7:   */   public final String builder;
/*  8:   */   public final int versionMajor;
/*  9:   */   public final int versionMinor;
/* 10:   */   public final int encryptionFlags;
/* 11:   */   
/* 12:   */   private EncryptionMode(String builder, int versionMajor, int versionMinor, int encryptionFlags)
/* 13:   */   {
/* 14:47 */     this.builder = builder;
/* 15:48 */     this.versionMajor = versionMajor;
/* 16:49 */     this.versionMinor = versionMinor;
/* 17:50 */     this.encryptionFlags = encryptionFlags;
/* 18:   */   }
/* 19:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.EncryptionMode
 * JD-Core Version:    0.7.0.1
 */