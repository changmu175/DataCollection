/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.usermodel.Shape;
/*   4:    */ import org.apache.poi.util.Removal;
/*   5:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties;
/*   6:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties.Factory;
/*   7:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetLineDashProperties;
/*   8:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetLineDashProperties.Factory;
/*   9:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor;
/*  10:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor.Factory;
/*  11:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties;
/*  13:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STPresetLineDashVal.Enum;
/*  14:    */ 
/*  15:    */ public abstract class XSSFShape
/*  16:    */   implements Shape
/*  17:    */ {
/*  18:    */   /**
/*  19:    */    * @deprecated
/*  20:    */    */
/*  21:    */   @Removal(version="3.19")
/*  22:    */   public static final int EMU_PER_PIXEL = 9525;
/*  23:    */   /**
/*  24:    */    * @deprecated
/*  25:    */    */
/*  26:    */   @Removal(version="3.19")
/*  27:    */   public static final int EMU_PER_POINT = 12700;
/*  28:    */   /**
/*  29:    */    * @deprecated
/*  30:    */    */
/*  31:    */   @Removal(version="3.19")
/*  32:    */   public static final int POINT_DPI = 72;
/*  33:    */   /**
/*  34:    */    * @deprecated
/*  35:    */    */
/*  36:    */   @Removal(version="3.19")
/*  37:    */   public static final int PIXEL_DPI = 96;
/*  38:    */   protected XSSFDrawing drawing;
/*  39:    */   protected XSSFShapeGroup parent;
/*  40:    */   protected XSSFAnchor anchor;
/*  41:    */   
/*  42:    */   public XSSFDrawing getDrawing()
/*  43:    */   {
/*  44: 84 */     return this.drawing;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public XSSFShapeGroup getParent()
/*  48:    */   {
/*  49: 90 */     return this.parent;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public XSSFAnchor getAnchor()
/*  53:    */   {
/*  54: 99 */     return this.anchor;
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected abstract CTShapeProperties getShapeProperties();
/*  58:    */   
/*  59:    */   public boolean isNoFill()
/*  60:    */   {
/*  61:111 */     return getShapeProperties().isSetNoFill();
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void setNoFill(boolean noFill)
/*  65:    */   {
/*  66:116 */     CTShapeProperties props = getShapeProperties();
/*  67:118 */     if (props.isSetPattFill()) {
/*  68:118 */       props.unsetPattFill();
/*  69:    */     }
/*  70:119 */     if (props.isSetSolidFill()) {
/*  71:119 */       props.unsetSolidFill();
/*  72:    */     }
/*  73:121 */     props.setNoFill(CTNoFillProperties.Factory.newInstance());
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void setFillColor(int red, int green, int blue)
/*  77:    */   {
/*  78:126 */     CTShapeProperties props = getShapeProperties();
/*  79:127 */     CTSolidColorFillProperties fill = props.isSetSolidFill() ? props.getSolidFill() : props.addNewSolidFill();
/*  80:128 */     CTSRgbColor rgb = CTSRgbColor.Factory.newInstance();
/*  81:129 */     rgb.setVal(new byte[] { (byte)red, (byte)green, (byte)blue });
/*  82:130 */     fill.setSrgbClr(rgb);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setLineStyleColor(int red, int green, int blue)
/*  86:    */   {
/*  87:135 */     CTShapeProperties props = getShapeProperties();
/*  88:136 */     CTLineProperties ln = props.isSetLn() ? props.getLn() : props.addNewLn();
/*  89:137 */     CTSolidColorFillProperties fill = ln.isSetSolidFill() ? ln.getSolidFill() : ln.addNewSolidFill();
/*  90:138 */     CTSRgbColor rgb = CTSRgbColor.Factory.newInstance();
/*  91:139 */     rgb.setVal(new byte[] { (byte)red, (byte)green, (byte)blue });
/*  92:140 */     fill.setSrgbClr(rgb);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setLineWidth(double lineWidth)
/*  96:    */   {
/*  97:149 */     CTShapeProperties props = getShapeProperties();
/*  98:150 */     CTLineProperties ln = props.isSetLn() ? props.getLn() : props.addNewLn();
/*  99:151 */     ln.setW((int)(lineWidth * 12700.0D));
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void setLineStyle(int lineStyle)
/* 103:    */   {
/* 104:160 */     CTShapeProperties props = getShapeProperties();
/* 105:161 */     CTLineProperties ln = props.isSetLn() ? props.getLn() : props.addNewLn();
/* 106:162 */     CTPresetLineDashProperties dashStyle = CTPresetLineDashProperties.Factory.newInstance();
/* 107:163 */     dashStyle.setVal(STPresetLineDashVal.Enum.forInt(lineStyle + 1));
/* 108:164 */     ln.setPrstDash(dashStyle);
/* 109:    */   }
/* 110:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFShape
 * JD-Core Version:    0.7.0.1
 */