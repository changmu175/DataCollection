/*  1:   */ package org.apache.poi.ss.formula;
/*  2:   */ 
/*  3:   */ public class NameIdentifier
/*  4:   */ {
/*  5:   */   private final String _name;
/*  6:   */   private final boolean _isQuoted;
/*  7:   */   
/*  8:   */   public NameIdentifier(String name, boolean isQuoted)
/*  9:   */   {
/* 10:25 */     this._name = name;
/* 11:26 */     this._isQuoted = isQuoted;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public String getName()
/* 15:   */   {
/* 16:29 */     return this._name;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public boolean isQuoted()
/* 20:   */   {
/* 21:32 */     return this._isQuoted;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public String toString()
/* 25:   */   {
/* 26:35 */     StringBuffer sb = new StringBuffer(64);
/* 27:36 */     sb.append(getClass().getName());
/* 28:37 */     sb.append(" [");
/* 29:38 */     if (this._isQuoted) {
/* 30:39 */       sb.append("'").append(this._name).append("'");
/* 31:   */     } else {
/* 32:41 */       sb.append(this._name);
/* 33:   */     }
/* 34:43 */     sb.append("]");
/* 35:44 */     return sb.toString();
/* 36:   */   }
/* 37:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.NameIdentifier
 * JD-Core Version:    0.7.0.1
 */