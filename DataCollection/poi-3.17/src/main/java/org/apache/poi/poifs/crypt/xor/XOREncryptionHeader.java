/*  1:   */ package org.apache.poi.poifs.crypt.xor;
/*  2:   */ 
/*  3:   */ import org.apache.poi.poifs.crypt.EncryptionHeader;
/*  4:   */ import org.apache.poi.poifs.crypt.standard.EncryptionRecord;
/*  5:   */ import org.apache.poi.util.LittleEndianByteArrayOutputStream;
/*  6:   */ 
/*  7:   */ public class XOREncryptionHeader
/*  8:   */   extends EncryptionHeader
/*  9:   */   implements EncryptionRecord, Cloneable
/* 10:   */ {
/* 11:   */   public void write(LittleEndianByteArrayOutputStream leos) {}
/* 12:   */   
/* 13:   */   public XOREncryptionHeader clone()
/* 14:   */     throws CloneNotSupportedException
/* 15:   */   {
/* 16:35 */     return (XOREncryptionHeader)super.clone();
/* 17:   */   }
/* 18:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.xor.XOREncryptionHeader
 * JD-Core Version:    0.7.0.1
 */