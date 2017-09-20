/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   6:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   7:    */ import javax.xml.bind.annotation.XmlAttribute;
/*   8:    */ import javax.xml.bind.annotation.XmlElements;
/*   9:    */ import javax.xml.bind.annotation.XmlType;
/*  10:    */ 
/*  11:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*  12:    */ @XmlType(name="CT_Path2D", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"closeOrMoveToOrLnTo"})
/*  13:    */ public class CTPath2D
/*  14:    */ {
/*  15:    */   @XmlElements({@javax.xml.bind.annotation.XmlElement(name="lnTo", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=CTPath2DLineTo.class), @javax.xml.bind.annotation.XmlElement(name="close", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=CTPath2DClose.class), @javax.xml.bind.annotation.XmlElement(name="cubicBezTo", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=CTPath2DCubicBezierTo.class), @javax.xml.bind.annotation.XmlElement(name="quadBezTo", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=CTPath2DQuadBezierTo.class), @javax.xml.bind.annotation.XmlElement(name="arcTo", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=CTPath2DArcTo.class), @javax.xml.bind.annotation.XmlElement(name="moveTo", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=CTPath2DMoveTo.class)})
/*  16:    */   protected List<Object> closeOrMoveToOrLnTo;
/*  17:    */   @XmlAttribute
/*  18:    */   protected Long w;
/*  19:    */   @XmlAttribute
/*  20:    */   protected Long h;
/*  21:    */   @XmlAttribute
/*  22:    */   protected STPathFillMode fill;
/*  23:    */   @XmlAttribute
/*  24:    */   protected Boolean stroke;
/*  25:    */   @XmlAttribute
/*  26:    */   protected Boolean extrusionOk;
/*  27:    */   
/*  28:    */   public List<Object> getCloseOrMoveToOrLnTo()
/*  29:    */   {
/*  30:113 */     if (this.closeOrMoveToOrLnTo == null) {
/*  31:114 */       this.closeOrMoveToOrLnTo = new ArrayList();
/*  32:    */     }
/*  33:116 */     return this.closeOrMoveToOrLnTo;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public boolean isSetCloseOrMoveToOrLnTo()
/*  37:    */   {
/*  38:120 */     return (this.closeOrMoveToOrLnTo != null) && (!this.closeOrMoveToOrLnTo.isEmpty());
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void unsetCloseOrMoveToOrLnTo()
/*  42:    */   {
/*  43:124 */     this.closeOrMoveToOrLnTo = null;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public long getW()
/*  47:    */   {
/*  48:136 */     if (this.w == null) {
/*  49:137 */       return 0L;
/*  50:    */     }
/*  51:139 */     return this.w.longValue();
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setW(long value)
/*  55:    */   {
/*  56:152 */     this.w = Long.valueOf(value);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public boolean isSetW()
/*  60:    */   {
/*  61:156 */     return this.w != null;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void unsetW()
/*  65:    */   {
/*  66:160 */     this.w = null;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public long getH()
/*  70:    */   {
/*  71:172 */     if (this.h == null) {
/*  72:173 */       return 0L;
/*  73:    */     }
/*  74:175 */     return this.h.longValue();
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void setH(long value)
/*  78:    */   {
/*  79:188 */     this.h = Long.valueOf(value);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public boolean isSetH()
/*  83:    */   {
/*  84:192 */     return this.h != null;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void unsetH()
/*  88:    */   {
/*  89:196 */     this.h = null;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public STPathFillMode getFill()
/*  93:    */   {
/*  94:208 */     if (this.fill == null) {
/*  95:209 */       return STPathFillMode.NORM;
/*  96:    */     }
/*  97:211 */     return this.fill;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setFill(STPathFillMode value)
/* 101:    */   {
/* 102:224 */     this.fill = value;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public boolean isSetFill()
/* 106:    */   {
/* 107:228 */     return this.fill != null;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public boolean isStroke()
/* 111:    */   {
/* 112:240 */     if (this.stroke == null) {
/* 113:241 */       return true;
/* 114:    */     }
/* 115:243 */     return this.stroke.booleanValue();
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void setStroke(boolean value)
/* 119:    */   {
/* 120:256 */     this.stroke = Boolean.valueOf(value);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public boolean isSetStroke()
/* 124:    */   {
/* 125:260 */     return this.stroke != null;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public void unsetStroke()
/* 129:    */   {
/* 130:264 */     this.stroke = null;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public boolean isExtrusionOk()
/* 134:    */   {
/* 135:276 */     if (this.extrusionOk == null) {
/* 136:277 */       return true;
/* 137:    */     }
/* 138:279 */     return this.extrusionOk.booleanValue();
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void setExtrusionOk(boolean value)
/* 142:    */   {
/* 143:292 */     this.extrusionOk = Boolean.valueOf(value);
/* 144:    */   }
/* 145:    */   
/* 146:    */   public boolean isSetExtrusionOk()
/* 147:    */   {
/* 148:296 */     return this.extrusionOk != null;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public void unsetExtrusionOk()
/* 152:    */   {
/* 153:300 */     this.extrusionOk = null;
/* 154:    */   }
/* 155:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTPath2D
 * JD-Core Version:    0.7.0.1
 */