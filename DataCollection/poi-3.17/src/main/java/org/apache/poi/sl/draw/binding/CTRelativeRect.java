/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   4:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   5:    */ import javax.xml.bind.annotation.XmlAttribute;
/*   6:    */ import javax.xml.bind.annotation.XmlType;
/*   7:    */ 
/*   8:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*   9:    */ @XmlType(name="CT_RelativeRect", namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  10:    */ public class CTRelativeRect
/*  11:    */ {
/*  12:    */   @XmlAttribute
/*  13:    */   protected Integer l;
/*  14:    */   @XmlAttribute
/*  15:    */   protected Integer t;
/*  16:    */   @XmlAttribute
/*  17:    */   protected Integer r;
/*  18:    */   @XmlAttribute
/*  19:    */   protected Integer b;
/*  20:    */   
/*  21:    */   public int getL()
/*  22:    */   {
/*  23: 68 */     if (this.l == null) {
/*  24: 69 */       return 0;
/*  25:    */     }
/*  26: 71 */     return this.l.intValue();
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setL(int value)
/*  30:    */   {
/*  31: 84 */     this.l = Integer.valueOf(value);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public boolean isSetL()
/*  35:    */   {
/*  36: 88 */     return this.l != null;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void unsetL()
/*  40:    */   {
/*  41: 92 */     this.l = null;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public int getT()
/*  45:    */   {
/*  46:104 */     if (this.t == null) {
/*  47:105 */       return 0;
/*  48:    */     }
/*  49:107 */     return this.t.intValue();
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setT(int value)
/*  53:    */   {
/*  54:120 */     this.t = Integer.valueOf(value);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public boolean isSetT()
/*  58:    */   {
/*  59:124 */     return this.t != null;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void unsetT()
/*  63:    */   {
/*  64:128 */     this.t = null;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public int getR()
/*  68:    */   {
/*  69:140 */     if (this.r == null) {
/*  70:141 */       return 0;
/*  71:    */     }
/*  72:143 */     return this.r.intValue();
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void setR(int value)
/*  76:    */   {
/*  77:156 */     this.r = Integer.valueOf(value);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public boolean isSetR()
/*  81:    */   {
/*  82:160 */     return this.r != null;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void unsetR()
/*  86:    */   {
/*  87:164 */     this.r = null;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public int getB()
/*  91:    */   {
/*  92:176 */     if (this.b == null) {
/*  93:177 */       return 0;
/*  94:    */     }
/*  95:179 */     return this.b.intValue();
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void setB(int value)
/*  99:    */   {
/* 100:192 */     this.b = Integer.valueOf(value);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public boolean isSetB()
/* 104:    */   {
/* 105:196 */     return this.b != null;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void unsetB()
/* 109:    */   {
/* 110:200 */     this.b = null;
/* 111:    */   }
/* 112:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTRelativeRect
 * JD-Core Version:    0.7.0.1
 */