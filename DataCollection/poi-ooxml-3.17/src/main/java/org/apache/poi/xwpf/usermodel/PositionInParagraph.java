/*  1:   */ package org.apache.poi.xwpf.usermodel;
/*  2:   */ 
/*  3:   */ public class PositionInParagraph
/*  4:   */ {
/*  5:28 */   private int posChar = 0;
/*  6:28 */   private int posText = 0;
/*  7:28 */   private int posRun = 0;
/*  8:   */   
/*  9:   */   public PositionInParagraph() {}
/* 10:   */   
/* 11:   */   public PositionInParagraph(int posRun, int posText, int posChar)
/* 12:   */   {
/* 13:34 */     this.posRun = posRun;
/* 14:35 */     this.posChar = posChar;
/* 15:36 */     this.posText = posText;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public int getRun()
/* 19:   */   {
/* 20:40 */     return this.posRun;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void setRun(int beginRun)
/* 24:   */   {
/* 25:44 */     this.posRun = beginRun;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public int getText()
/* 29:   */   {
/* 30:48 */     return this.posText;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void setText(int beginText)
/* 34:   */   {
/* 35:52 */     this.posText = beginText;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public int getChar()
/* 39:   */   {
/* 40:56 */     return this.posChar;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public void setChar(int beginChar)
/* 44:   */   {
/* 45:60 */     this.posChar = beginChar;
/* 46:   */   }
/* 47:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.PositionInParagraph
 * JD-Core Version:    0.7.0.1
 */