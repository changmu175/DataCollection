/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   4:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   5:    */ import javax.xml.bind.annotation.XmlAttribute;
/*   6:    */ import javax.xml.bind.annotation.XmlElement;
/*   7:    */ import javax.xml.bind.annotation.XmlType;
/*   8:    */ import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
/*   9:    */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*  10:    */ 
/*  11:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*  12:    */ @XmlType(name="CT_XYAdjustHandle", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"pos"})
/*  13:    */ public class CTXYAdjustHandle
/*  14:    */ {
/*  15:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", required=true)
/*  16:    */   protected CTAdjPoint2D pos;
/*  17:    */   @XmlAttribute
/*  18:    */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*  19:    */   protected String gdRefX;
/*  20:    */   @XmlAttribute
/*  21:    */   protected String minX;
/*  22:    */   @XmlAttribute
/*  23:    */   protected String maxX;
/*  24:    */   @XmlAttribute
/*  25:    */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*  26:    */   protected String gdRefY;
/*  27:    */   @XmlAttribute
/*  28:    */   protected String minY;
/*  29:    */   @XmlAttribute
/*  30:    */   protected String maxY;
/*  31:    */   
/*  32:    */   public CTAdjPoint2D getPos()
/*  33:    */   {
/*  34: 86 */     return this.pos;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setPos(CTAdjPoint2D value)
/*  38:    */   {
/*  39: 98 */     this.pos = value;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public boolean isSetPos()
/*  43:    */   {
/*  44:102 */     return this.pos != null;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public String getGdRefX()
/*  48:    */   {
/*  49:114 */     return this.gdRefX;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setGdRefX(String value)
/*  53:    */   {
/*  54:126 */     this.gdRefX = value;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public boolean isSetGdRefX()
/*  58:    */   {
/*  59:130 */     return this.gdRefX != null;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public String getMinX()
/*  63:    */   {
/*  64:142 */     return this.minX;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setMinX(String value)
/*  68:    */   {
/*  69:154 */     this.minX = value;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public boolean isSetMinX()
/*  73:    */   {
/*  74:158 */     return this.minX != null;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public String getMaxX()
/*  78:    */   {
/*  79:170 */     return this.maxX;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void setMaxX(String value)
/*  83:    */   {
/*  84:182 */     this.maxX = value;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public boolean isSetMaxX()
/*  88:    */   {
/*  89:186 */     return this.maxX != null;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public String getGdRefY()
/*  93:    */   {
/*  94:198 */     return this.gdRefY;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void setGdRefY(String value)
/*  98:    */   {
/*  99:210 */     this.gdRefY = value;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public boolean isSetGdRefY()
/* 103:    */   {
/* 104:214 */     return this.gdRefY != null;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public String getMinY()
/* 108:    */   {
/* 109:226 */     return this.minY;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void setMinY(String value)
/* 113:    */   {
/* 114:238 */     this.minY = value;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public boolean isSetMinY()
/* 118:    */   {
/* 119:242 */     return this.minY != null;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public String getMaxY()
/* 123:    */   {
/* 124:254 */     return this.maxY;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public void setMaxY(String value)
/* 128:    */   {
/* 129:266 */     this.maxY = value;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public boolean isSetMaxY()
/* 133:    */   {
/* 134:270 */     return this.maxY != null;
/* 135:    */   }
/* 136:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTXYAdjustHandle
 * JD-Core Version:    0.7.0.1
 */