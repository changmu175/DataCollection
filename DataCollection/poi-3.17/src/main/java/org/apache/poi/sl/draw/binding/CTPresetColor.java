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
/*  13:    */ @XmlType(name="CT_PresetColor", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"egColorTransform"})
/*  14:    */ public class CTPresetColor
/*  15:    */ {
/*  16:    */   @XmlElementRefs({@javax.xml.bind.annotation.XmlElementRef(name="shade", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="hue", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="blue", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="satOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="alphaMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="blueOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="green", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="red", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="gray", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="lum", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="invGamma", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="comp", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="lumOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="greenOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="sat", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="redMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="greenMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="lumMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="alpha", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="alphaOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="hueMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="inv", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="hueOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="gamma", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="tint", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="satMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="redOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="blueMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class)})
/*  17:    */   protected List<JAXBElement<?>> egColorTransform;
/*  18:    */   @XmlAttribute
/*  19:    */   protected STPresetColorVal val;
/*  20:    */   
/*  21:    */   public List<JAXBElement<?>> getEGColorTransform()
/*  22:    */   {
/*  23:141 */     if (this.egColorTransform == null) {
/*  24:142 */       this.egColorTransform = new ArrayList();
/*  25:    */     }
/*  26:144 */     return this.egColorTransform;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public boolean isSetEGColorTransform()
/*  30:    */   {
/*  31:148 */     return (this.egColorTransform != null) && (!this.egColorTransform.isEmpty());
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void unsetEGColorTransform()
/*  35:    */   {
/*  36:152 */     this.egColorTransform = null;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public STPresetColorVal getVal()
/*  40:    */   {
/*  41:164 */     return this.val;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setVal(STPresetColorVal value)
/*  45:    */   {
/*  46:176 */     this.val = value;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public boolean isSetVal()
/*  50:    */   {
/*  51:180 */     return this.val != null;
/*  52:    */   }
/*  53:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTPresetColor
 * JD-Core Version:    0.7.0.1
 */