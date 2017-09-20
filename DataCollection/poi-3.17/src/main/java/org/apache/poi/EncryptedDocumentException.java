/*  1:   */ package org.apache.poi;
/*  2:   */ 
/*  3:   */ public class EncryptedDocumentException
/*  4:   */   extends IllegalStateException
/*  5:   */ {
/*  6:   */   private static final long serialVersionUID = 7276950444540469193L;
/*  7:   */   
/*  8:   */   public EncryptedDocumentException(String s)
/*  9:   */   {
/* 10:24 */     super(s);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public EncryptedDocumentException(String message, Throwable cause)
/* 14:   */   {
/* 15:28 */     super(message, cause);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public EncryptedDocumentException(Throwable cause)
/* 19:   */   {
/* 20:32 */     super(cause);
/* 21:   */   }
/* 22:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.EncryptedDocumentException
 * JD-Core Version:    0.7.0.1
 */