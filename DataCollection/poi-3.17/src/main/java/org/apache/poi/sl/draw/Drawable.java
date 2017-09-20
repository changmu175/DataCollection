/*   1:    */ package org.apache.poi.sl.draw;
/*   2:    */ 
/*   3:    */ import java.awt.Graphics2D;
/*   4:    */ import java.awt.RenderingHints.Key;
/*   5:    */ import org.apache.poi.util.Internal;
/*   6:    */ 
/*   7:    */ public abstract interface Drawable
/*   8:    */ {
/*   9:    */   public abstract void applyTransform(Graphics2D paramGraphics2D);
/*  10:    */   
/*  11:    */   public abstract void draw(Graphics2D paramGraphics2D);
/*  12:    */   
/*  13:    */   public abstract void drawContent(Graphics2D paramGraphics2D);
/*  14:    */   
/*  15:    */   public static class DrawableHint
/*  16:    */     extends RenderingHints.Key
/*  17:    */   {
/*  18:    */     protected DrawableHint(int id)
/*  19:    */     {
/*  20: 29 */       super();
/*  21:    */     }
/*  22:    */     
/*  23:    */     public boolean isCompatibleValue(Object val)
/*  24:    */     {
/*  25: 33 */       return true;
/*  26:    */     }
/*  27:    */     
/*  28:    */     public String toString()
/*  29:    */     {
/*  30: 37 */       switch (intKey())
/*  31:    */       {
/*  32:    */       case 1: 
/*  33: 38 */         return "DRAW_FACTORY";
/*  34:    */       case 2: 
/*  35: 39 */         return "GROUP_TRANSFORM";
/*  36:    */       case 3: 
/*  37: 40 */         return "IMAGE_RENDERER";
/*  38:    */       case 4: 
/*  39: 41 */         return "TEXT_RENDERING_MODE";
/*  40:    */       case 5: 
/*  41: 42 */         return "GRADIENT_SHAPE";
/*  42:    */       case 6: 
/*  43: 43 */         return "PRESET_GEOMETRY_CACHE";
/*  44:    */       case 7: 
/*  45: 44 */         return "FONT_HANDLER";
/*  46:    */       case 8: 
/*  47: 45 */         return "FONT_FALLBACK";
/*  48:    */       case 9: 
/*  49: 46 */         return "FONT_MAP";
/*  50:    */       case 10: 
/*  51: 47 */         return "GSAVE";
/*  52:    */       case 11: 
/*  53: 48 */         return "GRESTORE";
/*  54:    */       }
/*  55: 49 */       return "UNKNOWN_ID " + intKey();
/*  56:    */     }
/*  57:    */   }
/*  58:    */   
/*  59: 57 */   public static final DrawableHint DRAW_FACTORY = new DrawableHint(1);
/*  60:    */   @Internal
/*  61: 63 */   public static final DrawableHint GROUP_TRANSFORM = new DrawableHint(2);
/*  62: 68 */   public static final DrawableHint IMAGE_RENDERER = new DrawableHint(3);
/*  63: 84 */   public static final DrawableHint TEXT_RENDERING_MODE = new DrawableHint(4);
/*  64: 90 */   public static final DrawableHint GRADIENT_SHAPE = new DrawableHint(5);
/*  65: 96 */   public static final DrawableHint PRESET_GEOMETRY_CACHE = new DrawableHint(6);
/*  66:    */   public static final int TEXT_AS_CHARACTERS = 1;
/*  67:    */   public static final int TEXT_AS_SHAPES = 2;
/*  68:114 */   public static final DrawableHint FONT_HANDLER = new DrawableHint(7);
/*  69:122 */   public static final DrawableHint FONT_FALLBACK = new DrawableHint(8);
/*  70:128 */   public static final DrawableHint FONT_MAP = new DrawableHint(9);
/*  71:130 */   public static final DrawableHint GSAVE = new DrawableHint(10);
/*  72:131 */   public static final DrawableHint GRESTORE = new DrawableHint(11);
/*  73:138 */   public static final DrawableHint CURRENT_SLIDE = new DrawableHint(12);
/*  74:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.Drawable
 * JD-Core Version:    0.7.0.1
 */