/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   4:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   5:    */ import javax.xml.bind.annotation.XmlElement;
/*   6:    */ import javax.xml.bind.annotation.XmlType;
/*   7:    */ 
/*   8:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*   9:    */ @XmlType(name="CT_CustomGeometry2D", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"avLst", "gdLst", "ahLst", "cxnLst", "rect", "pathLst"})
/*  10:    */ public class CTCustomGeometry2D
/*  11:    */ {
/*  12:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  13:    */   protected CTGeomGuideList avLst;
/*  14:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  15:    */   protected CTGeomGuideList gdLst;
/*  16:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  17:    */   protected CTAdjustHandleList ahLst;
/*  18:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  19:    */   protected CTConnectionSiteList cxnLst;
/*  20:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  21:    */   protected CTGeomRect rect;
/*  22:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", required=true)
/*  23:    */   protected CTPath2DList pathLst;
/*  24:    */   
/*  25:    */   public CTGeomGuideList getAvLst()
/*  26:    */   {
/*  27: 83 */     return this.avLst;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setAvLst(CTGeomGuideList value)
/*  31:    */   {
/*  32: 95 */     this.avLst = value;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public boolean isSetAvLst()
/*  36:    */   {
/*  37: 99 */     return this.avLst != null;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public CTGeomGuideList getGdLst()
/*  41:    */   {
/*  42:111 */     return this.gdLst;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setGdLst(CTGeomGuideList value)
/*  46:    */   {
/*  47:123 */     this.gdLst = value;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public boolean isSetGdLst()
/*  51:    */   {
/*  52:127 */     return this.gdLst != null;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public CTAdjustHandleList getAhLst()
/*  56:    */   {
/*  57:139 */     return this.ahLst;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setAhLst(CTAdjustHandleList value)
/*  61:    */   {
/*  62:151 */     this.ahLst = value;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public boolean isSetAhLst()
/*  66:    */   {
/*  67:155 */     return this.ahLst != null;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public CTConnectionSiteList getCxnLst()
/*  71:    */   {
/*  72:167 */     return this.cxnLst;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void setCxnLst(CTConnectionSiteList value)
/*  76:    */   {
/*  77:179 */     this.cxnLst = value;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public boolean isSetCxnLst()
/*  81:    */   {
/*  82:183 */     return this.cxnLst != null;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public CTGeomRect getRect()
/*  86:    */   {
/*  87:195 */     return this.rect;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setRect(CTGeomRect value)
/*  91:    */   {
/*  92:207 */     this.rect = value;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public boolean isSetRect()
/*  96:    */   {
/*  97:211 */     return this.rect != null;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public CTPath2DList getPathLst()
/* 101:    */   {
/* 102:223 */     return this.pathLst;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void setPathLst(CTPath2DList value)
/* 106:    */   {
/* 107:235 */     this.pathLst = value;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public boolean isSetPathLst()
/* 111:    */   {
/* 112:239 */     return this.pathLst != null;
/* 113:    */   }
/* 114:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTCustomGeometry2D
 * JD-Core Version:    0.7.0.1
 */