/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   4:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   5:    */ import javax.xml.bind.annotation.XmlElement;
/*   6:    */ import javax.xml.bind.annotation.XmlType;
/*   7:    */ 
/*   8:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*   9:    */ @XmlType(name="CT_Color", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"scrgbClr", "srgbClr", "hslClr", "sysClr", "schemeClr", "prstClr"})
/*  10:    */ public class CTColor
/*  11:    */ {
/*  12:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  13:    */   protected CTScRgbColor scrgbClr;
/*  14:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  15:    */   protected CTSRgbColor srgbClr;
/*  16:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  17:    */   protected CTHslColor hslClr;
/*  18:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  19:    */   protected CTSystemColor sysClr;
/*  20:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  21:    */   protected CTSchemeColor schemeClr;
/*  22:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  23:    */   protected CTPresetColor prstClr;
/*  24:    */   
/*  25:    */   public CTScRgbColor getScrgbClr()
/*  26:    */   {
/*  27: 78 */     return this.scrgbClr;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setScrgbClr(CTScRgbColor value)
/*  31:    */   {
/*  32: 90 */     this.scrgbClr = value;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public boolean isSetScrgbClr()
/*  36:    */   {
/*  37: 94 */     return this.scrgbClr != null;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public CTSRgbColor getSrgbClr()
/*  41:    */   {
/*  42:106 */     return this.srgbClr;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setSrgbClr(CTSRgbColor value)
/*  46:    */   {
/*  47:118 */     this.srgbClr = value;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public boolean isSetSrgbClr()
/*  51:    */   {
/*  52:122 */     return this.srgbClr != null;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public CTHslColor getHslClr()
/*  56:    */   {
/*  57:134 */     return this.hslClr;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setHslClr(CTHslColor value)
/*  61:    */   {
/*  62:146 */     this.hslClr = value;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public boolean isSetHslClr()
/*  66:    */   {
/*  67:150 */     return this.hslClr != null;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public CTSystemColor getSysClr()
/*  71:    */   {
/*  72:162 */     return this.sysClr;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void setSysClr(CTSystemColor value)
/*  76:    */   {
/*  77:174 */     this.sysClr = value;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public boolean isSetSysClr()
/*  81:    */   {
/*  82:178 */     return this.sysClr != null;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public CTSchemeColor getSchemeClr()
/*  86:    */   {
/*  87:190 */     return this.schemeClr;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setSchemeClr(CTSchemeColor value)
/*  91:    */   {
/*  92:202 */     this.schemeClr = value;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public boolean isSetSchemeClr()
/*  96:    */   {
/*  97:206 */     return this.schemeClr != null;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public CTPresetColor getPrstClr()
/* 101:    */   {
/* 102:218 */     return this.prstClr;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void setPrstClr(CTPresetColor value)
/* 106:    */   {
/* 107:230 */     this.prstClr = value;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public boolean isSetPrstClr()
/* 111:    */   {
/* 112:234 */     return this.prstClr != null;
/* 113:    */   }
/* 114:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTColor
 * JD-Core Version:    0.7.0.1
 */