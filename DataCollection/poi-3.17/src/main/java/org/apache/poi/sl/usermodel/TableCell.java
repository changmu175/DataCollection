/*  1:   */ package org.apache.poi.sl.usermodel;
/*  2:   */ 
/*  3:   */ import java.awt.Color;
/*  4:   */ 
/*  5:   */ public abstract interface TableCell<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>>
/*  6:   */   extends TextShape<S, P>
/*  7:   */ {
/*  8:   */   public abstract StrokeStyle getBorderStyle(BorderEdge paramBorderEdge);
/*  9:   */   
/* 10:   */   public abstract void setBorderStyle(BorderEdge paramBorderEdge, StrokeStyle paramStrokeStyle);
/* 11:   */   
/* 12:   */   public abstract void setBorderWidth(BorderEdge paramBorderEdge, double paramDouble);
/* 13:   */   
/* 14:   */   public abstract void setBorderColor(BorderEdge paramBorderEdge, Color paramColor);
/* 15:   */   
/* 16:   */   public abstract void setBorderCompound(BorderEdge paramBorderEdge, StrokeStyle.LineCompound paramLineCompound);
/* 17:   */   
/* 18:   */   public abstract void setBorderDash(BorderEdge paramBorderEdge, StrokeStyle.LineDash paramLineDash);
/* 19:   */   
/* 20:   */   public abstract void removeBorder(BorderEdge paramBorderEdge);
/* 21:   */   
/* 22:   */   public abstract int getGridSpan();
/* 23:   */   
/* 24:   */   public abstract int getRowSpan();
/* 25:   */   
/* 26:   */   public abstract boolean isMerged();
/* 27:   */   
/* 28:   */   public static enum BorderEdge
/* 29:   */   {
/* 30:29 */     bottom,  left,  top,  right;
/* 31:   */     
/* 32:   */     private BorderEdge() {}
/* 33:   */   }
/* 34:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.TableCell
 * JD-Core Version:    0.7.0.1
 */