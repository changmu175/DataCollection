/*  1:   */ package org.apache.poi.xdgf.geom;
/*  2:   */ 
/*  3:   */ import com.graphbuilder.curve.ControlPath;
/*  4:   */ import com.graphbuilder.curve.ShapeMultiPath;
/*  5:   */ import com.graphbuilder.curve.ValueVector;
/*  6:   */ import com.graphbuilder.geom.PointFactory;
/*  7:   */ import java.awt.geom.Path2D.Double;
/*  8:   */ import java.awt.geom.Point2D;
/*  9:   */ import java.util.ArrayList;
/* 10:   */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/* 11:   */ import org.apache.poi.xdgf.usermodel.section.geometry.SplineKnot;
/* 12:   */ import org.apache.poi.xdgf.usermodel.section.geometry.SplineStart;
/* 13:   */ 
/* 14:   */ public class SplineCollector
/* 15:   */ {
/* 16:   */   SplineStart _start;
/* 17:35 */   ArrayList<SplineKnot> _knots = new ArrayList();
/* 18:   */   
/* 19:   */   public SplineCollector(SplineStart start)
/* 20:   */   {
/* 21:38 */     this._start = start;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void addKnot(SplineKnot knot)
/* 25:   */   {
/* 26:42 */     if (!knot.getDel()) {
/* 27:43 */       this._knots.add(knot);
/* 28:   */     }
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void addToPath(Path2D.Double path, XDGFShape parent)
/* 32:   */   {
/* 33:49 */     Point2D last = path.getCurrentPoint();
/* 34:   */     
/* 35:   */ 
/* 36:52 */     ControlPath controlPath = new ControlPath();
/* 37:53 */     ValueVector knots = new ValueVector(this._knots.size() + 3);
/* 38:   */     
/* 39:55 */     double firstKnot = this._start.getB().doubleValue();
/* 40:56 */     double lastKnot = this._start.getC().doubleValue();
/* 41:57 */     int degree = this._start.getD().intValue();
/* 42:   */     
/* 43:   */ 
/* 44:60 */     knots.add(firstKnot);
/* 45:61 */     knots.add(this._start.getA().doubleValue());
/* 46:   */     
/* 47:   */ 
/* 48:64 */     controlPath.addPoint(PointFactory.create(last.getX(), last.getY()));
/* 49:65 */     controlPath.addPoint(PointFactory.create(this._start.getX().doubleValue(), this._start.getY().doubleValue()));
/* 50:68 */     for (SplineKnot knot : this._knots)
/* 51:   */     {
/* 52:69 */       knots.add(knot.getA().doubleValue());
/* 53:70 */       controlPath.addPoint(PointFactory.create(knot.getX().doubleValue(), knot.getY().doubleValue()));
/* 54:   */     }
/* 55:74 */     knots.add(lastKnot);
/* 56:   */     
/* 57:76 */     ShapeMultiPath shape = SplineRenderer.createNurbsSpline(controlPath, knots, null, degree);
/* 58:77 */     path.append(shape, true);
/* 59:   */   }
/* 60:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.geom.SplineCollector
 * JD-Core Version:    0.7.0.1
 */