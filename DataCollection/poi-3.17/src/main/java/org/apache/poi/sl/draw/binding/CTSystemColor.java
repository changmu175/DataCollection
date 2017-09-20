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
/*  11:    */ import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
/*  12:    */ import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
/*  13:    */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*  14:    */ 
/*  15:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*  16:    */ @XmlType(name="CT_SystemColor", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"egColorTransform"})
/*  17:    */ public class CTSystemColor
/*  18:    */ {
/*  19:    */   @XmlElementRefs({@javax.xml.bind.annotation.XmlElementRef(name="gray", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="green", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="shade", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="blueOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="lumMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="alphaMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="redMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="blue", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="sat", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="alphaOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="red", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="greenOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="hueMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="satOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="gamma", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="redOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="hue", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="satMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="lum", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="comp", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="invGamma", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="hueOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="alpha", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="greenMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="inv", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="blueMod", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="tint", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class), @javax.xml.bind.annotation.XmlElementRef(name="lumOff", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=JAXBElement.class)})
/*  20:    */   protected List<JAXBElement<?>> egColorTransform;
/*  21:    */   @XmlAttribute(required=true)
/*  22:    */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*  23:    */   protected String val;
/*  24:    */   @XmlAttribute
/*  25:    */   @XmlJavaTypeAdapter(HexBinaryAdapter.class)
/*  26:    */   protected byte[] lastClr;
/*  27:    */   
/*  28:    */   public List<JAXBElement<?>> getEGColorTransform()
/*  29:    */   {
/*  30:149 */     if (this.egColorTransform == null) {
/*  31:150 */       this.egColorTransform = new ArrayList();
/*  32:    */     }
/*  33:152 */     return this.egColorTransform;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public boolean isSetEGColorTransform()
/*  37:    */   {
/*  38:156 */     return (this.egColorTransform != null) && (!this.egColorTransform.isEmpty());
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void unsetEGColorTransform()
/*  42:    */   {
/*  43:160 */     this.egColorTransform = null;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public String getVal()
/*  47:    */   {
/*  48:172 */     return this.val;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setVal(String value)
/*  52:    */   {
/*  53:184 */     this.val = value;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public boolean isSetVal()
/*  57:    */   {
/*  58:188 */     return this.val != null;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public byte[] getLastClr()
/*  62:    */   {
/*  63:200 */     return this.lastClr;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void setLastClr(byte[] value)
/*  67:    */   {
/*  68:212 */     this.lastClr = (value != null ? (byte[])value.clone() : null);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public boolean isSetLastClr()
/*  72:    */   {
/*  73:216 */     return this.lastClr != null;
/*  74:    */   }
/*  75:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTSystemColor
 * JD-Core Version:    0.7.0.1
 */