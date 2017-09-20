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
/*  11:    */ import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
/*  12:    */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*  13:    */ 
/*  14:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*  15:    */ @XmlType(name="CT_SRgbColor", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"egColorTransform"})
/*  16:    */ public class CTSRgbColor
/*  17:    */ {
/*  18:    */   @XmlElementRefs({@javax.xml.bind.annotation.XmlElementRef(name="shade", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="blueOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="invGamma", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="redOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="lumOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="blueMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="hueMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="redMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="alpha", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="inv", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="red", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="hueOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="alphaMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="alphaOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="green", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="gray", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="sat", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="blue", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="lum", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="hue", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="gamma", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="tint", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="satMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="lumMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="greenOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="comp", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="satOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="greenMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class)})
/*  19:    */   protected List<JAXBElement<?>> egColorTransform;
/*  20:    */   @XmlAttribute(required=true)
/*  21:    */   @XmlJavaTypeAdapter(HexBinaryAdapter.class)
/*  22:    */   protected byte[] val;
/*  23:    */   
/*  24:    */   public List<JAXBElement<?>> getEGColorTransform()
/*  25:    */   {
/*  26:144 */     if (this.egColorTransform == null) {
/*  27:145 */       this.egColorTransform = new ArrayList();
/*  28:    */     }
/*  29:147 */     return this.egColorTransform;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public boolean isSetEGColorTransform()
/*  33:    */   {
/*  34:151 */     return (this.egColorTransform != null) && (!this.egColorTransform.isEmpty());
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void unsetEGColorTransform()
/*  38:    */   {
/*  39:155 */     this.egColorTransform = null;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public byte[] getVal()
/*  43:    */   {
/*  44:167 */     return this.val;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setVal(byte[] value)
/*  48:    */   {
/*  49:179 */     this.val = (value != null ? (byte[])value.clone() : null);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public boolean isSetVal()
/*  53:    */   {
/*  54:183 */     return this.val != null;
/*  55:    */   }
/*  56:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTSRgbColor
 * JD-Core Version:    0.7.0.1
 */