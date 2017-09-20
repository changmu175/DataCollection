/*  1:   */ package org.apache.poi.xdgf.geom;
/*  2:   */ 
/*  3:   */ import com.graphbuilder.curve.ControlPath;
/*  4:   */ import com.graphbuilder.curve.GroupIterator;
/*  5:   */ import com.graphbuilder.curve.NURBSpline;
/*  6:   */ import com.graphbuilder.curve.ShapeMultiPath;
/*  7:   */ import com.graphbuilder.curve.ValueVector;
/*  8:   */ 
/*  9:   */ public class SplineRenderer
/* 10:   */ {
/* 11:   */   public static ShapeMultiPath createNurbsSpline(ControlPath controlPoints, ValueVector knots, ValueVector weights, int degree)
/* 12:   */   {
/* 13:32 */     double firstKnot = knots.get(0);
/* 14:33 */     int count = knots.size();
/* 15:34 */     double lastKnot = knots.get(count - 1);
/* 16:37 */     for (int i = 0; i < count; i++) {
/* 17:38 */       knots.set((knots.get(i) - firstKnot) / lastKnot, i);
/* 18:   */     }
/* 19:42 */     int knotsToAdd = controlPoints.numPoints() + degree + 1;
/* 20:43 */     for (int i = count; i < knotsToAdd; i++) {
/* 21:44 */       knots.add(1.0D);
/* 22:   */     }
/* 23:47 */     GroupIterator gi = new GroupIterator("0:n-1", controlPoints.numPoints());
/* 24:   */     
/* 25:49 */     NURBSpline spline = new NURBSpline(controlPoints, gi);
/* 26:   */     
/* 27:51 */     spline.setDegree(degree);
/* 28:52 */     spline.setKnotVectorType(2);
/* 29:53 */     spline.setKnotVector(knots);
/* 30:55 */     if (weights == null) {
/* 31:56 */       spline.setUseWeightVector(false);
/* 32:   */     } else {
/* 33:58 */       spline.setWeightVector(weights);
/* 34:   */     }
/* 35:62 */     ShapeMultiPath shape = new ShapeMultiPath();
/* 36:63 */     shape.setFlatness(0.01D);
/* 37:   */     
/* 38:65 */     spline.appendTo(shape);
/* 39:66 */     return shape;
/* 40:   */   }
/* 41:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.geom.SplineRenderer
 * JD-Core Version:    0.7.0.1
 */