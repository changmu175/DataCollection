/*  1:   */ package org.apache.poi.sl.draw.geom;
/*  2:   */ 
/*  3:   */ import java.awt.geom.Rectangle2D;
/*  4:   */ import java.util.HashMap;
/*  5:   */ import java.util.Map;
/*  6:   */ 
/*  7:   */ public class Context
/*  8:   */ {
/*  9:27 */   final Map<String, Double> _ctx = new HashMap();
/* 10:   */   final IAdjustableShape _props;
/* 11:   */   final Rectangle2D _anchor;
/* 12:   */   
/* 13:   */   public Context(CustomGeometry geom, Rectangle2D anchor, IAdjustableShape props)
/* 14:   */   {
/* 15:32 */     this._props = props;
/* 16:33 */     this._anchor = anchor;
/* 17:34 */     for (Guide gd : geom.adjusts) {
/* 18:35 */       evaluate(gd);
/* 19:   */     }
/* 20:37 */     for (Guide gd : geom.guides) {
/* 21:38 */       evaluate(gd);
/* 22:   */     }
/* 23:   */   }
/* 24:   */   
/* 25:   */   public Rectangle2D getShapeAnchor()
/* 26:   */   {
/* 27:43 */     return this._anchor;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public Guide getAdjustValue(String name)
/* 31:   */   {
/* 32:47 */     return this._props.getAdjustValue(name);
/* 33:   */   }
/* 34:   */   
/* 35:   */   public double getValue(String key)
/* 36:   */   {
/* 37:51 */     if (key.matches("(\\+|-)?\\d+")) {
/* 38:52 */       return Double.parseDouble(key);
/* 39:   */     }
/* 40:55 */     Double val = (Double)this._ctx.get(key);
/* 41:   */     
/* 42:57 */     return val != null ? val.doubleValue() : evaluate(BuiltInGuide.valueOf("_" + key));
/* 43:   */   }
/* 44:   */   
/* 45:   */   public double evaluate(Formula fmla)
/* 46:   */   {
/* 47:61 */     double result = fmla.evaluate(this);
/* 48:62 */     if ((fmla instanceof Guide))
/* 49:   */     {
/* 50:63 */       String key = ((Guide)fmla).getName();
/* 51:64 */       if (key != null) {
/* 52:65 */         this._ctx.put(key, Double.valueOf(result));
/* 53:   */       }
/* 54:   */     }
/* 55:68 */     return result;
/* 56:   */   }
/* 57:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.geom.Context
 * JD-Core Version:    0.7.0.1
 */