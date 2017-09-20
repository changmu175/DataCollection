/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.awt.Dimension;
/*   5:    */ import java.awt.geom.Rectangle2D;
/*   6:    */ import java.awt.geom.Rectangle2D.Double;
/*   7:    */ import org.apache.poi.POIXMLException;
/*   8:    */ import org.apache.poi.sl.usermodel.Background;
/*   9:    */ import org.apache.poi.sl.usermodel.Placeholder;
/*  10:    */ import org.apache.xmlbeans.XmlObject;
/*  11:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties;
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D;
/*  13:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTBackground;
/*  14:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTBackgroundProperties;
/*  15:    */ 
/*  16:    */ public class XSLFBackground
/*  17:    */   extends XSLFSimpleShape
/*  18:    */   implements Background<XSLFShape, XSLFTextParagraph>
/*  19:    */ {
/*  20:    */   XSLFBackground(CTBackground shape, XSLFSheet sheet)
/*  21:    */   {
/*  22: 40 */     super(shape, sheet);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public Rectangle2D getAnchor()
/*  26:    */   {
/*  27: 45 */     Dimension pg = getSheet().getSlideShow().getPageSize();
/*  28: 46 */     return new Double(0.0D, 0.0D, pg.getWidth(), pg.getHeight());
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected CTTransform2D getXfrm(boolean create)
/*  32:    */   {
/*  33: 58 */     return null;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setPlaceholder(Placeholder placeholder)
/*  37:    */   {
/*  38: 64 */     throw new POIXMLException("Can't set a placeholder for a background");
/*  39:    */   }
/*  40:    */   
/*  41:    */   protected CTBackgroundProperties getBgPr(boolean create)
/*  42:    */   {
/*  43: 68 */     CTBackground bg = (CTBackground)getXmlObject();
/*  44: 69 */     if ((!bg.isSetBgPr()) && (create))
/*  45:    */     {
/*  46: 70 */       if (bg.isSetBgRef()) {
/*  47: 71 */         bg.unsetBgRef();
/*  48:    */       }
/*  49: 73 */       return bg.addNewBgPr();
/*  50:    */     }
/*  51: 75 */     return bg.getBgPr();
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setFillColor(Color color)
/*  55:    */   {
/*  56: 79 */     CTBackgroundProperties bgPr = getBgPr(true);
/*  57: 81 */     if (color == null)
/*  58:    */     {
/*  59: 82 */       if (bgPr.isSetSolidFill()) {
/*  60: 83 */         bgPr.unsetSolidFill();
/*  61:    */       }
/*  62: 86 */       if (!bgPr.isSetNoFill()) {
/*  63: 87 */         bgPr.addNewNoFill();
/*  64:    */       }
/*  65:    */     }
/*  66:    */     else
/*  67:    */     {
/*  68: 90 */       if (bgPr.isSetNoFill()) {
/*  69: 91 */         bgPr.unsetNoFill();
/*  70:    */       }
/*  71: 94 */       CTSolidColorFillProperties fill = bgPr.isSetSolidFill() ? bgPr.getSolidFill() : bgPr.addNewSolidFill();
/*  72:    */       
/*  73: 96 */       XSLFColor col = new XSLFColor(fill, getSheet().getTheme(), fill.getSchemeClr());
/*  74: 97 */       col.setColor(color);
/*  75:    */     }
/*  76:    */   }
/*  77:    */   
/*  78:    */   protected XmlObject getShapeProperties()
/*  79:    */   {
/*  80:103 */     CTBackground bg = (CTBackground)getXmlObject();
/*  81:104 */     if (bg.isSetBgPr()) {
/*  82:105 */       return bg.getBgPr();
/*  83:    */     }
/*  84:106 */     if (bg.isSetBgRef()) {
/*  85:107 */       return bg.getBgRef();
/*  86:    */     }
/*  87:109 */     return null;
/*  88:    */   }
/*  89:    */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFBackground

 * JD-Core Version:    0.7.0.1

 */