/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   4:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   5:    */ import javax.xml.bind.annotation.XmlAttribute;
/*   6:    */ import javax.xml.bind.annotation.XmlElement;
/*   7:    */ import javax.xml.bind.annotation.XmlType;
/*   8:    */ 
/*   9:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*  10:    */ @XmlType(name="CT_GroupTransform2D", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"off", "ext", "chOff", "chExt"})
/*  11:    */ public class CTGroupTransform2D
/*  12:    */ {
/*  13:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  14:    */   protected CTPoint2D off;
/*  15:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  16:    */   protected CTPositiveSize2D ext;
/*  17:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  18:    */   protected CTPoint2D chOff;
/*  19:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  20:    */   protected CTPositiveSize2D chExt;
/*  21:    */   @XmlAttribute
/*  22:    */   protected Integer rot;
/*  23:    */   @XmlAttribute
/*  24:    */   protected Boolean flipH;
/*  25:    */   @XmlAttribute
/*  26:    */   protected Boolean flipV;
/*  27:    */   
/*  28:    */   public CTPoint2D getOff()
/*  29:    */   {
/*  30: 85 */     return this.off;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setOff(CTPoint2D value)
/*  34:    */   {
/*  35: 97 */     this.off = value;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public boolean isSetOff()
/*  39:    */   {
/*  40:101 */     return this.off != null;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public CTPositiveSize2D getExt()
/*  44:    */   {
/*  45:113 */     return this.ext;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setExt(CTPositiveSize2D value)
/*  49:    */   {
/*  50:125 */     this.ext = value;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public boolean isSetExt()
/*  54:    */   {
/*  55:129 */     return this.ext != null;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public CTPoint2D getChOff()
/*  59:    */   {
/*  60:141 */     return this.chOff;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setChOff(CTPoint2D value)
/*  64:    */   {
/*  65:153 */     this.chOff = value;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public boolean isSetChOff()
/*  69:    */   {
/*  70:157 */     return this.chOff != null;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public CTPositiveSize2D getChExt()
/*  74:    */   {
/*  75:169 */     return this.chExt;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void setChExt(CTPositiveSize2D value)
/*  79:    */   {
/*  80:181 */     this.chExt = value;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public boolean isSetChExt()
/*  84:    */   {
/*  85:185 */     return this.chExt != null;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public int getRot()
/*  89:    */   {
/*  90:197 */     if (this.rot == null) {
/*  91:198 */       return 0;
/*  92:    */     }
/*  93:200 */     return this.rot.intValue();
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void setRot(int value)
/*  97:    */   {
/*  98:213 */     this.rot = Integer.valueOf(value);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public boolean isSetRot()
/* 102:    */   {
/* 103:217 */     return this.rot != null;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void unsetRot()
/* 107:    */   {
/* 108:221 */     this.rot = null;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public boolean isFlipH()
/* 112:    */   {
/* 113:233 */     if (this.flipH == null) {
/* 114:234 */       return false;
/* 115:    */     }
/* 116:236 */     return this.flipH.booleanValue();
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void setFlipH(boolean value)
/* 120:    */   {
/* 121:249 */     this.flipH = Boolean.valueOf(value);
/* 122:    */   }
/* 123:    */   
/* 124:    */   public boolean isSetFlipH()
/* 125:    */   {
/* 126:253 */     return this.flipH != null;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void unsetFlipH()
/* 130:    */   {
/* 131:257 */     this.flipH = null;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public boolean isFlipV()
/* 135:    */   {
/* 136:269 */     if (this.flipV == null) {
/* 137:270 */       return false;
/* 138:    */     }
/* 139:272 */     return this.flipV.booleanValue();
/* 140:    */   }
/* 141:    */   
/* 142:    */   public void setFlipV(boolean value)
/* 143:    */   {
/* 144:285 */     this.flipV = Boolean.valueOf(value);
/* 145:    */   }
/* 146:    */   
/* 147:    */   public boolean isSetFlipV()
/* 148:    */   {
/* 149:289 */     return this.flipV != null;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public void unsetFlipV()
/* 153:    */   {
/* 154:293 */     this.flipV = null;
/* 155:    */   }
/* 156:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTGroupTransform2D
 * JD-Core Version:    0.7.0.1
 */