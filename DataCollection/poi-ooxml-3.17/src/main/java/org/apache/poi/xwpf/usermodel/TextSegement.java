/*  1:   */ package org.apache.poi.xwpf.usermodel;
/*  2:   */ 
/*  3:   */ public class TextSegement
/*  4:   */ {
/*  5:   */   private PositionInParagraph beginPos;
/*  6:   */   private PositionInParagraph endPos;
/*  7:   */   
/*  8:   */   public TextSegement()
/*  9:   */   {
/* 10:28 */     this.beginPos = new PositionInParagraph();
/* 11:29 */     this.endPos = new PositionInParagraph();
/* 12:   */   }
/* 13:   */   
/* 14:   */   public TextSegement(int beginRun, int endRun, int beginText, int endText, int beginChar, int endChar)
/* 15:   */   {
/* 16:33 */     PositionInParagraph beginPos = new PositionInParagraph(beginRun, beginText, beginChar);
/* 17:34 */     PositionInParagraph endPos = new PositionInParagraph(endRun, endText, endChar);
/* 18:35 */     this.beginPos = beginPos;
/* 19:36 */     this.endPos = endPos;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public TextSegement(PositionInParagraph beginPos, PositionInParagraph endPos)
/* 23:   */   {
/* 24:40 */     this.beginPos = beginPos;
/* 25:41 */     this.endPos = endPos;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public PositionInParagraph getBeginPos()
/* 29:   */   {
/* 30:45 */     return this.beginPos;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public PositionInParagraph getEndPos()
/* 34:   */   {
/* 35:49 */     return this.endPos;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public int getBeginRun()
/* 39:   */   {
/* 40:53 */     return this.beginPos.getRun();
/* 41:   */   }
/* 42:   */   
/* 43:   */   public void setBeginRun(int beginRun)
/* 44:   */   {
/* 45:57 */     this.beginPos.setRun(beginRun);
/* 46:   */   }
/* 47:   */   
/* 48:   */   public int getBeginText()
/* 49:   */   {
/* 50:61 */     return this.beginPos.getText();
/* 51:   */   }
/* 52:   */   
/* 53:   */   public void setBeginText(int beginText)
/* 54:   */   {
/* 55:65 */     this.beginPos.setText(beginText);
/* 56:   */   }
/* 57:   */   
/* 58:   */   public int getBeginChar()
/* 59:   */   {
/* 60:69 */     return this.beginPos.getChar();
/* 61:   */   }
/* 62:   */   
/* 63:   */   public void setBeginChar(int beginChar)
/* 64:   */   {
/* 65:73 */     this.beginPos.setChar(beginChar);
/* 66:   */   }
/* 67:   */   
/* 68:   */   public int getEndRun()
/* 69:   */   {
/* 70:77 */     return this.endPos.getRun();
/* 71:   */   }
/* 72:   */   
/* 73:   */   public void setEndRun(int endRun)
/* 74:   */   {
/* 75:81 */     this.endPos.setRun(endRun);
/* 76:   */   }
/* 77:   */   
/* 78:   */   public int getEndText()
/* 79:   */   {
/* 80:85 */     return this.endPos.getText();
/* 81:   */   }
/* 82:   */   
/* 83:   */   public void setEndText(int endText)
/* 84:   */   {
/* 85:89 */     this.endPos.setText(endText);
/* 86:   */   }
/* 87:   */   
/* 88:   */   public int getEndChar()
/* 89:   */   {
/* 90:93 */     return this.endPos.getChar();
/* 91:   */   }
/* 92:   */   
/* 93:   */   public void setEndChar(int endChar)
/* 94:   */   {
/* 95:97 */     this.endPos.setChar(endChar);
/* 96:   */   }
/* 97:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.TextSegement
 * JD-Core Version:    0.7.0.1
 */