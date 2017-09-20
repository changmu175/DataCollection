/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   4:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   5:    */ import javax.xml.bind.annotation.XmlAnyElement;
/*   6:    */ import javax.xml.bind.annotation.XmlAttribute;
/*   7:    */ import javax.xml.bind.annotation.XmlSchemaType;
/*   8:    */ import javax.xml.bind.annotation.XmlType;
/*   9:    */ import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
/*  10:    */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*  11:    */ 
/*  12:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*  13:    */ @XmlType(name="CT_OfficeArtExtension", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"any"})
/*  14:    */ public class CTOfficeArtExtension
/*  15:    */ {
/*  16:    */   @XmlAnyElement(lax=true)
/*  17:    */   protected Object any;
/*  18:    */   @XmlAttribute
/*  19:    */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*  20:    */   @XmlSchemaType(name="token")
/*  21:    */   protected String uri;
/*  22:    */   
/*  23:    */   public Object getAny()
/*  24:    */   {
/*  25: 74 */     return this.any;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setAny(Object value)
/*  29:    */   {
/*  30: 87 */     this.any = value;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public boolean isSetAny()
/*  34:    */   {
/*  35: 91 */     return this.any != null;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public String getUri()
/*  39:    */   {
/*  40:103 */     return this.uri;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setUri(String value)
/*  44:    */   {
/*  45:115 */     this.uri = value;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public boolean isSetUri()
/*  49:    */   {
/*  50:119 */     return this.uri != null;
/*  51:    */   }
/*  52:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTOfficeArtExtension
 * JD-Core Version:    0.7.0.1
 */