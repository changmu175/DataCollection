/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import javax.xml.bind.JAXBElement;
/*   6:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   7:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   8:    */ import javax.xml.bind.annotation.XmlAttribute;
/*   9:    */ import javax.xml.bind.annotation.XmlElementRefs;
/*  10:    */ import javax.xml.bind.annotation.XmlType;
/*  11:    */ 
/*  12:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*  13:    */ @XmlType(name="CT_HslColor", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"egColorTransform"})
/*  14:    */ public class CTHslColor
/*  15:    */ {
/*  16:    */   @XmlElementRefs({@javax.xml.bind.annotation.XmlElementRef(name="comp", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="satMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="tint", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="satOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="lum", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="gray", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="gamma", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="inv", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="red", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="alpha", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="greenOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="green", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="greenMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="blueOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="lumMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="redOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="hueOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="invGamma", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="alphaOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="hue", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="redMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="alphaMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="lumOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="blue", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="hueMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="blueMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="sat", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="shade", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class)})
/*  17:    */   protected List<JAXBElement<?>> egColorTransform;
/*  18:    */   @XmlAttribute(required=true)
/*  19:    */   protected int hue;
/*  20:    */   @XmlAttribute(required=true)
/*  21:    */   protected int sat;
/*  22:    */   @XmlAttribute(required=true)
/*  23:    */   protected int lum;
/*  24:    */   
/*  25:    */   public List<JAXBElement<?>> getEGColorTransform()
/*  26:    */   {
/*  27:147 */     if (this.egColorTransform == null) {
/*  28:148 */       this.egColorTransform = new ArrayList();
/*  29:    */     }
/*  30:150 */     return this.egColorTransform;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public boolean isSetEGColorTransform()
/*  34:    */   {
/*  35:154 */     return (this.egColorTransform != null) && (!this.egColorTransform.isEmpty());
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void unsetEGColorTransform()
/*  39:    */   {
/*  40:158 */     this.egColorTransform = null;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public int getHue()
/*  44:    */   {
/*  45:166 */     return this.hue;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setHue(int value)
/*  49:    */   {
/*  50:174 */     this.hue = value;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public boolean isSetHue()
/*  54:    */   {
/*  55:178 */     return true;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public int getSat()
/*  59:    */   {
/*  60:186 */     return this.sat;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setSat(int value)
/*  64:    */   {
/*  65:194 */     this.sat = value;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public boolean isSetSat()
/*  69:    */   {
/*  70:198 */     return true;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public int getLum()
/*  74:    */   {
/*  75:206 */     return this.lum;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void setLum(int value)
/*  79:    */   {
/*  80:214 */     this.lum = value;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public boolean isSetLum()
/*  84:    */   {
/*  85:218 */     return true;
/*  86:    */   }
/*  87:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTHslColor
 * JD-Core Version:    0.7.0.1
 */