/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   6:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   7:    */ import javax.xml.bind.annotation.XmlElements;
/*   8:    */ import javax.xml.bind.annotation.XmlType;
/*   9:    */ 
/*  10:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*  11:    */ @XmlType(name="CT_ColorMRU", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"egColorChoice"})
/*  12:    */ public class CTColorMRU
/*  13:    */ {
/*  14:    */   @XmlElements({@javax.xml.bind.annotation.XmlElement(name="prstClr", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=CTPresetColor.class), @javax.xml.bind.annotation.XmlElement(name="sysClr", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=CTSystemColor.class), @javax.xml.bind.annotation.XmlElement(name="hslClr", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=CTHslColor.class), @javax.xml.bind.annotation.XmlElement(name="srgbClr", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=CTSRgbColor.class), @javax.xml.bind.annotation.XmlElement(name="scrgbClr", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=CTScRgbColor.class), @javax.xml.bind.annotation.XmlElement(name="schemeClr", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", type=CTSchemeColor.class)})
/*  15:    */   protected List<Object> egColorChoice;
/*  16:    */   
/*  17:    */   public List<Object> getEGColorChoice()
/*  18:    */   {
/*  19: 92 */     if (this.egColorChoice == null) {
/*  20: 93 */       this.egColorChoice = new ArrayList();
/*  21:    */     }
/*  22: 95 */     return this.egColorChoice;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public boolean isSetEGColorChoice()
/*  26:    */   {
/*  27: 99 */     return (this.egColorChoice != null) && (!this.egColorChoice.isEmpty());
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void unsetEGColorChoice()
/*  31:    */   {
/*  32:103 */     this.egColorChoice = null;
/*  33:    */   }
/*  34:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTColorMRU
 * JD-Core Version:    0.7.0.1
 */