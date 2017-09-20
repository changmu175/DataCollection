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
/*  12:    */ @XmlType(name="CT_PolarAdjustHandle", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"pos"})
/*  13:    */ public class CTPolarAdjustHandle
/*  14:    */ {
/*  15:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", required=true)
/*  16:    */   protected CTAdjPoint2D pos;
/*  17:    */   @XmlAttribute
/*  18:    */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*  19:    */   protected String gdRefR;
/*  20:    */   @XmlAttribute
/*  21:    */   protected String minR;
/*  22:    */   @XmlAttribute
/*  23:    */   protected String maxR;
/*  24:    */   @XmlAttribute
/*  25:    */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*  26:    */   protected String gdRefAng;
/*  27:    */   @XmlAttribute
/*  28:    */   protected String minAng;
/*  29:    */   @XmlAttribute
/*  30:    */   protected String maxAng;
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
/*  47:    */   public String getGdRefR()
/*  48:    */   {
/*  49:114 */     return this.gdRefR;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setGdRefR(String value)
/*  53:    */   {
/*  54:126 */     this.gdRefR = value;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public boolean isSetGdRefR()
/*  58:    */   {
/*  59:130 */     return this.gdRefR != null;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public String getMinR()
/*  63:    */   {
/*  64:142 */     return this.minR;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setMinR(String value)
/*  68:    */   {
/*  69:154 */     this.minR = value;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public boolean isSetMinR()
/*  73:    */   {
/*  74:158 */     return this.minR != null;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public String getMaxR()
/*  78:    */   {
/*  79:170 */     return this.maxR;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void setMaxR(String value)
/*  83:    */   {
/*  84:182 */     this.maxR = value;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public boolean isSetMaxR()
/*  88:    */   {
/*  89:186 */     return this.maxR != null;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public String getGdRefAng()
/*  93:    */   {
/*  94:198 */     return this.gdRefAng;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void setGdRefAng(String value)
/*  98:    */   {
/*  99:210 */     this.gdRefAng = value;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public boolean isSetGdRefAng()
/* 103:    */   {
/* 104:214 */     return this.gdRefAng != null;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public String getMinAng()
/* 108:    */   {
/* 109:226 */     return this.minAng;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void setMinAng(String value)
/* 113:    */   {
/* 114:238 */     this.minAng = value;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public boolean isSetMinAng()
/* 118:    */   {
/* 119:242 */     return this.minAng != null;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public String getMaxAng()
/* 123:    */   {
/* 124:254 */     return this.maxAng;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public void setMaxAng(String value)
/* 128:    */   {
/* 129:266 */     this.maxAng = value;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public boolean isSetMaxAng()
/* 133:    */   {
/* 134:270 */     return this.maxAng != null;
/* 135:    */   }
/* 136:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTPolarAdjustHandle
 * JD-Core Version:    0.7.0.1
 */