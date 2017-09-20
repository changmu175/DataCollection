/*   1:    */ package org.apache.poi.sl.draw.geom;
/*   2:    */ 
/*   3:    */ import java.awt.geom.Path2D.Double;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.poi.sl.draw.binding.CTAdjPoint2D;
/*   7:    */ import org.apache.poi.sl.draw.binding.CTPath2D;
/*   8:    */ import org.apache.poi.sl.draw.binding.CTPath2DArcTo;
/*   9:    */ import org.apache.poi.sl.draw.binding.CTPath2DClose;
/*  10:    */ import org.apache.poi.sl.draw.binding.CTPath2DCubicBezierTo;
/*  11:    */ import org.apache.poi.sl.draw.binding.CTPath2DLineTo;
/*  12:    */ import org.apache.poi.sl.draw.binding.CTPath2DMoveTo;
/*  13:    */ import org.apache.poi.sl.draw.binding.CTPath2DQuadBezierTo;
/*  14:    */ import org.apache.poi.sl.usermodel.PaintStyle.PaintModifier;
/*  15:    */ 
/*  16:    */ public class Path
/*  17:    */ {
/*  18:    */   private final List<PathCommand> commands;
/*  19:    */   PaintStyle.PaintModifier _fill;
/*  20:    */   boolean _stroke;
/*  21:    */   long _w;
/*  22:    */   long _h;
/*  23:    */   
/*  24:    */   public Path()
/*  25:    */   {
/*  26: 48 */     this(true, true);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public Path(boolean fill, boolean stroke)
/*  30:    */   {
/*  31: 52 */     this.commands = new ArrayList();
/*  32: 53 */     this._w = -1L;
/*  33: 54 */     this._h = -1L;
/*  34: 55 */     this._fill = (fill ? PaintStyle.PaintModifier.NORM : PaintStyle.PaintModifier.NONE);
/*  35: 56 */     this._stroke = stroke;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public Path(CTPath2D spPath)
/*  39:    */   {
/*  40: 60 */     switch (1.$SwitchMap$org$apache$poi$sl$draw$binding$STPathFillMode[spPath.getFill().ordinal()])
/*  41:    */     {
/*  42:    */     case 1: 
/*  43: 61 */       this._fill = PaintStyle.PaintModifier.NONE; break;
/*  44:    */     case 2: 
/*  45: 62 */       this._fill = PaintStyle.PaintModifier.DARKEN; break;
/*  46:    */     case 3: 
/*  47: 63 */       this._fill = PaintStyle.PaintModifier.DARKEN_LESS; break;
/*  48:    */     case 4: 
/*  49: 64 */       this._fill = PaintStyle.PaintModifier.LIGHTEN; break;
/*  50:    */     case 5: 
/*  51: 65 */       this._fill = PaintStyle.PaintModifier.LIGHTEN_LESS; break;
/*  52:    */     case 6: 
/*  53:    */     default: 
/*  54: 67 */       this._fill = PaintStyle.PaintModifier.NORM;
/*  55:    */     }
/*  56: 69 */     this._stroke = spPath.isStroke();
/*  57: 70 */     this._w = (spPath.isSetW() ? spPath.getW() : -1L);
/*  58: 71 */     this._h = (spPath.isSetH() ? spPath.getH() : -1L);
/*  59:    */     
/*  60: 73 */     this.commands = new ArrayList();
/*  61: 75 */     for (Object ch : spPath.getCloseOrMoveToOrLnTo()) {
/*  62: 76 */       if ((ch instanceof CTPath2DMoveTo))
/*  63:    */       {
/*  64: 77 */         CTAdjPoint2D pt = ((CTPath2DMoveTo)ch).getPt();
/*  65: 78 */         this.commands.add(new MoveToCommand(pt));
/*  66:    */       }
/*  67: 79 */       else if ((ch instanceof CTPath2DLineTo))
/*  68:    */       {
/*  69: 80 */         CTAdjPoint2D pt = ((CTPath2DLineTo)ch).getPt();
/*  70: 81 */         this.commands.add(new LineToCommand(pt));
/*  71:    */       }
/*  72: 82 */       else if ((ch instanceof CTPath2DArcTo))
/*  73:    */       {
/*  74: 83 */         CTPath2DArcTo arc = (CTPath2DArcTo)ch;
/*  75: 84 */         this.commands.add(new ArcToCommand(arc));
/*  76:    */       }
/*  77: 85 */       else if ((ch instanceof CTPath2DQuadBezierTo))
/*  78:    */       {
/*  79: 86 */         CTPath2DQuadBezierTo bez = (CTPath2DQuadBezierTo)ch;
/*  80: 87 */         CTAdjPoint2D pt1 = (CTAdjPoint2D)bez.getPt().get(0);
/*  81: 88 */         CTAdjPoint2D pt2 = (CTAdjPoint2D)bez.getPt().get(1);
/*  82: 89 */         this.commands.add(new QuadToCommand(pt1, pt2));
/*  83:    */       }
/*  84: 90 */       else if ((ch instanceof CTPath2DCubicBezierTo))
/*  85:    */       {
/*  86: 91 */         CTPath2DCubicBezierTo bez = (CTPath2DCubicBezierTo)ch;
/*  87: 92 */         CTAdjPoint2D pt1 = (CTAdjPoint2D)bez.getPt().get(0);
/*  88: 93 */         CTAdjPoint2D pt2 = (CTAdjPoint2D)bez.getPt().get(1);
/*  89: 94 */         CTAdjPoint2D pt3 = (CTAdjPoint2D)bez.getPt().get(2);
/*  90: 95 */         this.commands.add(new CurveToCommand(pt1, pt2, pt3));
/*  91:    */       }
/*  92: 96 */       else if ((ch instanceof CTPath2DClose))
/*  93:    */       {
/*  94: 97 */         this.commands.add(new ClosePathCommand());
/*  95:    */       }
/*  96:    */       else
/*  97:    */       {
/*  98: 99 */         throw new IllegalStateException("Unsupported path segment: " + ch);
/*  99:    */       }
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void addCommand(PathCommand cmd)
/* 104:    */   {
/* 105:105 */     this.commands.add(cmd);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public Path2D.Double getPath(Context ctx)
/* 109:    */   {
/* 110:112 */     Path2D.Double path = new Path2D.Double();
/* 111:113 */     for (PathCommand cmd : this.commands) {
/* 112:114 */       cmd.execute(path, ctx);
/* 113:    */     }
/* 114:116 */     return path;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public boolean isStroked()
/* 118:    */   {
/* 119:120 */     return this._stroke;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public boolean isFilled()
/* 123:    */   {
/* 124:124 */     return this._fill != PaintStyle.PaintModifier.NONE;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public PaintStyle.PaintModifier getFill()
/* 128:    */   {
/* 129:128 */     return this._fill;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public long getW()
/* 133:    */   {
/* 134:132 */     return this._w;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public long getH()
/* 138:    */   {
/* 139:136 */     return this._h;
/* 140:    */   }
/* 141:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.geom.Path
 * JD-Core Version:    0.7.0.1
 */