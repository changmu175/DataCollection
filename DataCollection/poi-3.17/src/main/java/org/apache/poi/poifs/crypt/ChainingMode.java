/*  1:   */ package org.apache.poi.poifs.crypt;
/*  2:   */ 
/*  3:   */ public enum ChainingMode
/*  4:   */ {
/*  5:22 */   ecb("ECB", 1),  cbc("CBC", 2),  cfb("CFB8", 3);
/*  6:   */   
/*  7:   */   public final String jceId;
/*  8:   */   public final int ecmaId;
/*  9:   */   
/* 10:   */   private ChainingMode(String jceId, int ecmaId)
/* 11:   */   {
/* 12:30 */     this.jceId = jceId;
/* 13:31 */     this.ecmaId = ecmaId;
/* 14:   */   }
/* 15:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.ChainingMode
 * JD-Core Version:    0.7.0.1
 */