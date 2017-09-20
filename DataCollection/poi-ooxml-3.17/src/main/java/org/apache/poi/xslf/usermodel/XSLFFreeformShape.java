/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.geom.AffineTransform;
/*   4:    */ import java.awt.geom.Path2D.Double;
/*   5:    */ import java.awt.geom.PathIterator;
/*   6:    */ import java.awt.geom.Rectangle2D;
/*   7:    */ import org.apache.poi.sl.usermodel.FreeformShape;
/*   8:    */ import org.apache.poi.util.Units;
/*   9:    */ import org.apache.xmlbeans.XmlObject;
/*  10:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D;
/*  11:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTCustomGeometry2D;
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect;
/*  13:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
/*  14:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D.Factory;
/*  16:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose;
/*  17:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo;
/*  18:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo;
/*  21:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo;
/*  22:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*  23:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;
/*  24:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTShape.Factory;
/*  25:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTShapeNonVisual;
/*  26:    */ 
/*  27:    */ public class XSLFFreeformShape
/*  28:    */   extends XSLFAutoShape
/*  29:    */   implements FreeformShape<XSLFShape, XSLFTextParagraph>
/*  30:    */ {
/*  31:    */   XSLFFreeformShape(CTShape shape, XSLFSheet sheet)
/*  32:    */   {
/*  33: 54 */     super(shape, sheet);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public int setPath(Path2D.Double path)
/*  37:    */   {
/*  38: 59 */     CTPath2D ctPath = CTPath2D.Factory.newInstance();
/*  39:    */     
/*  40: 61 */     Rectangle2D bounds = path.getBounds2D();
/*  41: 62 */     int x0 = Units.toEMU(bounds.getX());
/*  42: 63 */     int y0 = Units.toEMU(bounds.getY());
/*  43: 64 */     PathIterator it = path.getPathIterator(new AffineTransform());
/*  44: 65 */     int numPoints = 0;
/*  45: 66 */     ctPath.setH(Units.toEMU(bounds.getHeight()));
/*  46: 67 */     ctPath.setW(Units.toEMU(bounds.getWidth()));
/*  47: 68 */     while (!it.isDone())
/*  48:    */     {
/*  49: 69 */       double[] vals = new double[6];
/*  50: 70 */       int type = it.currentSegment(vals);
/*  51: 71 */       switch (type)
/*  52:    */       {
/*  53:    */       case 0: 
/*  54: 73 */         CTAdjPoint2D mv = ctPath.addNewMoveTo().addNewPt();
/*  55: 74 */         mv.setX(Integer.valueOf(Units.toEMU(vals[0]) - x0));
/*  56: 75 */         mv.setY(Integer.valueOf(Units.toEMU(vals[1]) - y0));
/*  57: 76 */         numPoints++;
/*  58: 77 */         break;
/*  59:    */       case 1: 
/*  60: 79 */         CTAdjPoint2D ln = ctPath.addNewLnTo().addNewPt();
/*  61: 80 */         ln.setX(Integer.valueOf(Units.toEMU(vals[0]) - x0));
/*  62: 81 */         ln.setY(Integer.valueOf(Units.toEMU(vals[1]) - y0));
/*  63: 82 */         numPoints++;
/*  64: 83 */         break;
/*  65:    */       case 2: 
/*  66: 85 */         CTPath2DQuadBezierTo qbez = ctPath.addNewQuadBezTo();
/*  67: 86 */         CTAdjPoint2D qp1 = qbez.addNewPt();
/*  68: 87 */         qp1.setX(Integer.valueOf(Units.toEMU(vals[0]) - x0));
/*  69: 88 */         qp1.setY(Integer.valueOf(Units.toEMU(vals[1]) - y0));
/*  70: 89 */         CTAdjPoint2D qp2 = qbez.addNewPt();
/*  71: 90 */         qp2.setX(Integer.valueOf(Units.toEMU(vals[2]) - x0));
/*  72: 91 */         qp2.setY(Integer.valueOf(Units.toEMU(vals[3]) - y0));
/*  73: 92 */         numPoints += 2;
/*  74: 93 */         break;
/*  75:    */       case 3: 
/*  76: 95 */         CTPath2DCubicBezierTo bez = ctPath.addNewCubicBezTo();
/*  77: 96 */         CTAdjPoint2D p1 = bez.addNewPt();
/*  78: 97 */         p1.setX(Integer.valueOf(Units.toEMU(vals[0]) - x0));
/*  79: 98 */         p1.setY(Integer.valueOf(Units.toEMU(vals[1]) - y0));
/*  80: 99 */         CTAdjPoint2D p2 = bez.addNewPt();
/*  81:100 */         p2.setX(Integer.valueOf(Units.toEMU(vals[2]) - x0));
/*  82:101 */         p2.setY(Integer.valueOf(Units.toEMU(vals[3]) - y0));
/*  83:102 */         CTAdjPoint2D p3 = bez.addNewPt();
/*  84:103 */         p3.setX(Integer.valueOf(Units.toEMU(vals[4]) - x0));
/*  85:104 */         p3.setY(Integer.valueOf(Units.toEMU(vals[5]) - y0));
/*  86:105 */         numPoints += 3;
/*  87:106 */         break;
/*  88:    */       case 4: 
/*  89:108 */         numPoints++;
/*  90:109 */         ctPath.addNewClose();
/*  91:110 */         break;
/*  92:    */       default: 
/*  93:112 */         throw new IllegalStateException("Unrecognized path segment type: " + type);
/*  94:    */       }
/*  95:114 */       it.next();
/*  96:    */     }
/*  97:117 */     XmlObject xo = getShapeProperties();
/*  98:118 */     if (!(xo instanceof CTShapeProperties)) {
/*  99:119 */       return -1;
/* 100:    */     }
/* 101:122 */     ((CTShapeProperties)xo).getCustGeom().getPathLst().setPathArray(new CTPath2D[] { ctPath });
/* 102:123 */     setAnchor(bounds);
/* 103:124 */     return numPoints;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public Path2D.Double getPath()
/* 107:    */   {
/* 108:129 */     Path2D.Double path = new Path2D.Double();
/* 109:130 */     Rectangle2D bounds = getAnchor();
/* 110:    */     
/* 111:132 */     XmlObject xo = getShapeProperties();
/* 112:133 */     if (!(xo instanceof CTShapeProperties)) {
/* 113:134 */       return null;
/* 114:    */     }
/* 115:137 */     CTCustomGeometry2D geom = ((CTShapeProperties)xo).getCustGeom();
/* 116:138 */     for (CTPath2D spPath : geom.getPathLst().getPathArray())
/* 117:    */     {
/* 118:139 */       double scaleW = bounds.getWidth() / Units.toPoints(spPath.getW());
/* 119:140 */       double scaleH = bounds.getHeight() / Units.toPoints(spPath.getH());
/* 120:141 */       for (XmlObject ch : spPath.selectPath("*")) {
/* 121:142 */         if ((ch instanceof CTPath2DMoveTo))
/* 122:    */         {
/* 123:143 */           CTAdjPoint2D pt = ((CTPath2DMoveTo)ch).getPt();
/* 124:144 */           path.moveTo((float)(Units.toPoints(((Long)pt.getX()).longValue()) * scaleW), (float)(Units.toPoints(((Long)pt.getY()).longValue()) * scaleH));
/* 125:    */         }
/* 126:147 */         else if ((ch instanceof CTPath2DLineTo))
/* 127:    */         {
/* 128:148 */           CTAdjPoint2D pt = ((CTPath2DLineTo)ch).getPt();
/* 129:149 */           path.lineTo((float)Units.toPoints(((Long)pt.getX()).longValue()), (float)Units.toPoints(((Long)pt.getY()).longValue()));
/* 130:    */         }
/* 131:151 */         else if ((ch instanceof CTPath2DQuadBezierTo))
/* 132:    */         {
/* 133:152 */           CTPath2DQuadBezierTo bez = (CTPath2DQuadBezierTo)ch;
/* 134:153 */           CTAdjPoint2D pt1 = bez.getPtArray(0);
/* 135:154 */           CTAdjPoint2D pt2 = bez.getPtArray(1);
/* 136:155 */           path.quadTo((float)(Units.toPoints(((Long)pt1.getX()).longValue()) * scaleW), (float)(Units.toPoints(((Long)pt1.getY()).longValue()) * scaleH), (float)(Units.toPoints(((Long)pt2.getX()).longValue()) * scaleW), (float)(Units.toPoints(((Long)pt2.getY()).longValue()) * scaleH));
/* 137:    */         }
/* 138:160 */         else if ((ch instanceof CTPath2DCubicBezierTo))
/* 139:    */         {
/* 140:161 */           CTPath2DCubicBezierTo bez = (CTPath2DCubicBezierTo)ch;
/* 141:162 */           CTAdjPoint2D pt1 = bez.getPtArray(0);
/* 142:163 */           CTAdjPoint2D pt2 = bez.getPtArray(1);
/* 143:164 */           CTAdjPoint2D pt3 = bez.getPtArray(2);
/* 144:165 */           path.curveTo((float)(Units.toPoints(((Long)pt1.getX()).longValue()) * scaleW), (float)(Units.toPoints(((Long)pt1.getY()).longValue()) * scaleH), (float)(Units.toPoints(((Long)pt2.getX()).longValue()) * scaleW), (float)(Units.toPoints(((Long)pt2.getY()).longValue()) * scaleH), (float)(Units.toPoints(((Long)pt3.getX()).longValue()) * scaleW), (float)(Units.toPoints(((Long)pt3.getY()).longValue()) * scaleH));
/* 145:    */         }
/* 146:172 */         else if ((ch instanceof CTPath2DClose))
/* 147:    */         {
/* 148:173 */           path.closePath();
/* 149:    */         }
/* 150:    */       }
/* 151:    */     }
/* 152:180 */     AffineTransform at = new AffineTransform();
/* 153:181 */     at.translate(bounds.getX(), bounds.getY());
/* 154:182 */     return new Path2D.Double(at.createTransformedShape(path));
/* 155:    */   }
/* 156:    */   
/* 157:    */   static CTShape prototype(int shapeId)
/* 158:    */   {
/* 159:188 */     CTShape ct = CTShape.Factory.newInstance();
/* 160:189 */     CTShapeNonVisual nvSpPr = ct.addNewNvSpPr();
/* 161:190 */     CTNonVisualDrawingProps cnv = nvSpPr.addNewCNvPr();
/* 162:191 */     cnv.setName("Freeform " + shapeId);
/* 163:192 */     cnv.setId(shapeId + 1);
/* 164:193 */     nvSpPr.addNewCNvSpPr();
/* 165:194 */     nvSpPr.addNewNvPr();
/* 166:195 */     CTShapeProperties spPr = ct.addNewSpPr();
/* 167:196 */     CTCustomGeometry2D geom = spPr.addNewCustGeom();
/* 168:197 */     geom.addNewAvLst();
/* 169:198 */     geom.addNewGdLst();
/* 170:199 */     geom.addNewAhLst();
/* 171:200 */     geom.addNewCxnLst();
/* 172:201 */     CTGeomRect rect = geom.addNewRect();
/* 173:202 */     rect.setR("r");
/* 174:203 */     rect.setB("b");
/* 175:204 */     rect.setT("t");
/* 176:205 */     rect.setL("l");
/* 177:206 */     geom.addNewPathLst();
/* 178:207 */     return ct;
/* 179:    */   }
/* 180:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFFreeformShape
 * JD-Core Version:    0.7.0.1
 */