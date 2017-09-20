/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   4:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   5:    */ import javax.xml.bind.annotation.XmlAttribute;
/*   6:    */ import javax.xml.bind.annotation.XmlElement;
/*   7:    */ import javax.xml.bind.annotation.XmlType;
/*   8:    */ 
/*   9:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*  10:    */ @XmlType(name="CT_Transform2D", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"off", "ext"})
/*  11:    */ public class CTTransform2D
/*  12:    */ {
/*  13:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  14:    */   protected CTPoint2D off;
/*  15:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  16:    */   protected CTPositiveSize2D ext;
/*  17:    */   @XmlAttribute
/*  18:    */   protected Integer rot;
/*  19:    */   @XmlAttribute
/*  20:    */   protected Boolean flipH;
/*  21:    */   @XmlAttribute
/*  22:    */   protected Boolean flipV;
/*  23:    */   
/*  24:    */   public CTPoint2D getOff()
/*  25:    */   {
/*  26: 77 */     return this.off;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setOff(CTPoint2D value)
/*  30:    */   {
/*  31: 89 */     this.off = value;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public boolean isSetOff()
/*  35:    */   {
/*  36: 93 */     return this.off != null;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public CTPositiveSize2D getExt()
/*  40:    */   {
/*  41:105 */     return this.ext;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setExt(CTPositiveSize2D value)
/*  45:    */   {
/*  46:117 */     this.ext = value;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public boolean isSetExt()
/*  50:    */   {
/*  51:121 */     return this.ext != null;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public int getRot()
/*  55:    */   {
/*  56:133 */     if (this.rot == null) {
/*  57:134 */       return 0;
/*  58:    */     }
/*  59:136 */     return this.rot.intValue();
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setRot(int value)
/*  63:    */   {
/*  64:149 */     this.rot = Integer.valueOf(value);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public boolean isSetRot()
/*  68:    */   {
/*  69:153 */     return this.rot != null;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void unsetRot()
/*  73:    */   {
/*  74:157 */     this.rot = null;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public boolean isFlipH()
/*  78:    */   {
/*  79:169 */     if (this.flipH == null) {
/*  80:170 */       return false;
/*  81:    */     }
/*  82:172 */     return this.flipH.booleanValue();
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setFlipH(boolean value)
/*  86:    */   {
/*  87:185 */     this.flipH = Boolean.valueOf(value);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public boolean isSetFlipH()
/*  91:    */   {
/*  92:189 */     return this.flipH != null;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void unsetFlipH()
/*  96:    */   {
/*  97:193 */     this.flipH = null;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public boolean isFlipV()
/* 101:    */   {
/* 102:205 */     if (this.flipV == null) {
/* 103:206 */       return false;
/* 104:    */     }
/* 105:208 */     return this.flipV.booleanValue();
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void setFlipV(boolean value)
/* 109:    */   {
/* 110:221 */     this.flipV = Boolean.valueOf(value);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public boolean isSetFlipV()
/* 114:    */   {
/* 115:225 */     return this.flipV != null;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void unsetFlipV()
/* 119:    */   {
/* 120:229 */     this.flipV = null;
/* 121:    */   }
/* 122:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTTransform2D
 * JD-Core Version:    0.7.0.1
 */