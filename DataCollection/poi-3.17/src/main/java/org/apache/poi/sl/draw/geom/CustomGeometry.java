/*  1:   */ package org.apache.poi.sl.draw.geom;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.Iterator;
/*  5:   */ import java.util.List;
/*  6:   */ import org.apache.poi.sl.draw.binding.CTCustomGeometry2D;
/*  7:   */ import org.apache.poi.sl.draw.binding.CTGeomGuide;
/*  8:   */ import org.apache.poi.sl.draw.binding.CTGeomGuideList;
/*  9:   */ import org.apache.poi.sl.draw.binding.CTGeomRect;
/* 10:   */ import org.apache.poi.sl.draw.binding.CTPath2D;
/* 11:   */ import org.apache.poi.sl.draw.binding.CTPath2DList;
/* 12:   */ 
/* 13:   */ public class CustomGeometry
/* 14:   */   implements Iterable<Path>
/* 15:   */ {
/* 16:37 */   final List<Guide> adjusts = new ArrayList();
/* 17:38 */   final List<Guide> guides = new ArrayList();
/* 18:39 */   final List<Path> paths = new ArrayList();
/* 19:   */   Path textBounds;
/* 20:   */   
/* 21:   */   public CustomGeometry(CTCustomGeometry2D geom)
/* 22:   */   {
/* 23:43 */     CTGeomGuideList avLst = geom.getAvLst();
/* 24:44 */     if (avLst != null) {
/* 25:45 */       for (CTGeomGuide gd : avLst.getGd()) {
/* 26:46 */         this.adjusts.add(new AdjustValue(gd));
/* 27:   */       }
/* 28:   */     }
/* 29:50 */     CTGeomGuideList gdLst = geom.getGdLst();
/* 30:51 */     if (gdLst != null) {
/* 31:52 */       for (CTGeomGuide gd : gdLst.getGd()) {
/* 32:53 */         this.guides.add(new Guide(gd));
/* 33:   */       }
/* 34:   */     }
/* 35:57 */     CTPath2DList pathLst = geom.getPathLst();
/* 36:58 */     if (pathLst != null) {
/* 37:59 */       for (CTPath2D spPath : pathLst.getPath()) {
/* 38:60 */         this.paths.add(new Path(spPath));
/* 39:   */       }
/* 40:   */     }
/* 41:64 */     CTGeomRect rect = geom.getRect();
/* 42:65 */     if (rect != null)
/* 43:   */     {
/* 44:66 */       this.textBounds = new Path();
/* 45:67 */       this.textBounds.addCommand(new MoveToCommand(rect.getL(), rect.getT()));
/* 46:68 */       this.textBounds.addCommand(new LineToCommand(rect.getR(), rect.getT()));
/* 47:69 */       this.textBounds.addCommand(new LineToCommand(rect.getR(), rect.getB()));
/* 48:70 */       this.textBounds.addCommand(new LineToCommand(rect.getL(), rect.getB()));
/* 49:71 */       this.textBounds.addCommand(new ClosePathCommand());
/* 50:   */     }
/* 51:   */   }
/* 52:   */   
/* 53:   */   public Iterator<Path> iterator()
/* 54:   */   {
/* 55:77 */     return this.paths.iterator();
/* 56:   */   }
/* 57:   */   
/* 58:   */   public Path getTextBounds()
/* 59:   */   {
/* 60:81 */     return this.textBounds;
/* 61:   */   }
/* 62:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.geom.CustomGeometry
 * JD-Core Version:    0.7.0.1
 */