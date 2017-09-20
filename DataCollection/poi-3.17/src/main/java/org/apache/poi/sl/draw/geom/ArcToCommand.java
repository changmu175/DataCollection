/*   1:    */ package org.apache.poi.sl.draw.geom;
/*   2:    */ 
/*   3:    */ import java.awt.geom.Arc2D;
/*   4:    */ import java.awt.geom.Arc2D.Double;
/*   5:    */ import java.awt.geom.Path2D.Double;
/*   6:    */ import java.awt.geom.Point2D;
/*   7:    */ import org.apache.poi.sl.draw.binding.CTPath2DArcTo;
/*   8:    */ 
/*   9:    */ public class ArcToCommand
/*  10:    */   implements PathCommand
/*  11:    */ {
/*  12:    */   private String hr;
/*  13:    */   private String wr;
/*  14:    */   private String stAng;
/*  15:    */   private String swAng;
/*  16:    */   
/*  17:    */   ArcToCommand(CTPath2DArcTo arc)
/*  18:    */   {
/*  19: 44 */     this.hr = arc.getHR();
/*  20: 45 */     this.wr = arc.getWR();
/*  21: 46 */     this.stAng = arc.getStAng();
/*  22: 47 */     this.swAng = arc.getSwAng();
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void execute(Path2D.Double path, Context ctx)
/*  26:    */   {
/*  27: 52 */     double rx = ctx.getValue(this.wr);
/*  28: 53 */     double ry = ctx.getValue(this.hr);
/*  29: 54 */     double ooStart = ctx.getValue(this.stAng) / 60000.0D;
/*  30: 55 */     double ooExtent = ctx.getValue(this.swAng) / 60000.0D;
/*  31:    */     
/*  32:    */ 
/*  33: 58 */     double awtStart = convertOoxml2AwtAngle(ooStart, rx, ry);
/*  34: 59 */     double awtSweep = convertOoxml2AwtAngle(ooStart + ooExtent, rx, ry) - awtStart;
/*  35:    */     
/*  36:    */ 
/*  37: 62 */     double radStart = Math.toRadians(ooStart);
/*  38: 63 */     double invStart = Math.atan2(rx * Math.sin(radStart), ry * Math.cos(radStart));
/*  39:    */     
/*  40: 65 */     Point2D pt = path.getCurrentPoint();
/*  41:    */     
/*  42: 67 */     double x0 = pt.getX() - rx * Math.cos(invStart) - rx;
/*  43: 68 */     double y0 = pt.getY() - ry * Math.sin(invStart) - ry;
/*  44:    */     
/*  45: 70 */     Arc2D arc = new Arc2D.Double(x0, y0, 2.0D * rx, 2.0D * ry, awtStart, awtSweep, 0);
/*  46: 71 */     path.append(arc, true);
/*  47:    */   }
/*  48:    */   
/*  49:    */   private double convertOoxml2AwtAngle(double ooAngle, double width, double height)
/*  50:    */   {
/*  51:101 */     double aspect = height / width;
/*  52:    */     
/*  53:103 */     double awtAngle = -ooAngle;
/*  54:    */     
/*  55:105 */     double awtAngle2 = awtAngle % 360.0D;
/*  56:106 */     double awtAngle3 = awtAngle - awtAngle2;
/*  57:109 */     switch ((int)(awtAngle2 / 90.0D))
/*  58:    */     {
/*  59:    */     case -3: 
/*  60:112 */       awtAngle3 -= 360.0D;
/*  61:113 */       awtAngle2 += 360.0D;
/*  62:114 */       break;
/*  63:    */     case -2: 
/*  64:    */     case -1: 
/*  65:118 */       awtAngle3 -= 180.0D;
/*  66:119 */       awtAngle2 += 180.0D;
/*  67:120 */       break;
/*  68:    */     case 0: 
/*  69:    */     default: 
/*  70:    */       break;
/*  71:    */     case 1: 
/*  72:    */     case 2: 
/*  73:128 */       awtAngle3 += 180.0D;
/*  74:129 */       awtAngle2 -= 180.0D;
/*  75:130 */       break;
/*  76:    */     case 3: 
/*  77:133 */       awtAngle3 += 360.0D;
/*  78:134 */       awtAngle2 -= 360.0D;
/*  79:    */     }
/*  80:139 */     awtAngle = Math.toDegrees(Math.atan2(Math.tan(Math.toRadians(awtAngle2)), aspect)) + awtAngle3;
/*  81:140 */     return awtAngle;
/*  82:    */   }
/*  83:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.geom.ArcToCommand
 * JD-Core Version:    0.7.0.1
 */