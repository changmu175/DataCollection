/*  1:   */ package org.apache.poi.xdgf.usermodel.section.geometry;
/*  2:   */ 
/*  3:   */ import com.microsoft.schemas.office.visio.x2012.main.RowType;
/*  4:   */ import org.apache.poi.POIXMLException;
/*  5:   */ import org.apache.poi.xdgf.util.ObjectFactory;
/*  6:   */ 
/*  7:   */ public class GeometryRowFactory
/*  8:   */ {
/*  9:30 */   static final ObjectFactory<GeometryRow, RowType> _rowTypes = new ObjectFactory();
/* 10:   */   
/* 11:   */   static
/* 12:   */   {
/* 13:   */     try
/* 14:   */     {
/* 15:32 */       _rowTypes.put("ArcTo", ArcTo.class, new Class[] { RowType.class });
/* 16:33 */       _rowTypes.put("Ellipse", Ellipse.class, new Class[] { RowType.class });
/* 17:34 */       _rowTypes.put("EllipticalArcTo", EllipticalArcTo.class, new Class[] { RowType.class });
/* 18:   */       
/* 19:36 */       _rowTypes.put("InfiniteLine", InfiniteLine.class, new Class[] { RowType.class });
/* 20:37 */       _rowTypes.put("LineTo", LineTo.class, new Class[] { RowType.class });
/* 21:38 */       _rowTypes.put("MoveTo", MoveTo.class, new Class[] { RowType.class });
/* 22:39 */       _rowTypes.put("NURBSTo", NURBSTo.class, new Class[] { RowType.class });
/* 23:   */       
/* 24:41 */       _rowTypes.put("PolylineTo", PolyLineTo.class, new Class[] { RowType.class });
/* 25:42 */       _rowTypes.put("PolyLineTo", PolyLineTo.class, new Class[] { RowType.class });
/* 26:43 */       _rowTypes.put("RelCubBezTo", RelCubBezTo.class, new Class[] { RowType.class });
/* 27:44 */       _rowTypes.put("RelEllipticalArcTo", RelEllipticalArcTo.class, new Class[] { RowType.class });
/* 28:   */       
/* 29:46 */       _rowTypes.put("RelLineTo", RelLineTo.class, new Class[] { RowType.class });
/* 30:47 */       _rowTypes.put("RelMoveTo", RelMoveTo.class, new Class[] { RowType.class });
/* 31:48 */       _rowTypes.put("RelQuadBezTo", RelQuadBezTo.class, new Class[] { RowType.class });
/* 32:49 */       _rowTypes.put("SplineKnot", SplineKnot.class, new Class[] { RowType.class });
/* 33:50 */       _rowTypes.put("SplineStart", SplineStart.class, new Class[] { RowType.class });
/* 34:   */     }
/* 35:   */     catch (NoSuchMethodException e)
/* 36:   */     {
/* 37:52 */       throw new POIXMLException("Internal error", e);
/* 38:   */     }
/* 39:   */     catch (SecurityException e)
/* 40:   */     {
/* 41:54 */       throw new POIXMLException("Internal error", e);
/* 42:   */     }
/* 43:   */   }
/* 44:   */   
/* 45:   */   public static GeometryRow load(RowType row)
/* 46:   */   {
/* 47:60 */     return (GeometryRow)_rowTypes.load(row.getT(), new Object[] { row });
/* 48:   */   }
/* 49:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.geometry.GeometryRowFactory
 * JD-Core Version:    0.7.0.1
 */