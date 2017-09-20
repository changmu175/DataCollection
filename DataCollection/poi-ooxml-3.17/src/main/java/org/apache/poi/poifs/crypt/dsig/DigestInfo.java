/*  1:   */ package org.apache.poi.poifs.crypt.dsig;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  5:   */ 
/*  6:   */ public class DigestInfo
/*  7:   */   implements Serializable
/*  8:   */ {
/*  9:   */   private static final long serialVersionUID = 1L;
/* 10:   */   public final byte[] digestValue;
/* 11:   */   public final String description;
/* 12:   */   public final HashAlgorithm hashAlgo;
/* 13:   */   
/* 14:   */   public DigestInfo(byte[] digestValue, HashAlgorithm hashAlgo, String description)
/* 15:   */   {
/* 16:46 */     this.digestValue = ((byte[])digestValue.clone());
/* 17:47 */     this.hashAlgo = hashAlgo;
/* 18:48 */     this.description = description;
/* 19:   */   }
/* 20:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.dsig.DigestInfo
 * JD-Core Version:    0.7.0.1
 */