/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   4:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   5:    */ import javax.xml.bind.annotation.XmlAttribute;
/*   6:    */ import javax.xml.bind.annotation.XmlType;
/*   7:    */ 
/*   8:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*   9:    */ @XmlType(name="CT_EmbeddedWAVAudioFile", namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  10:    */ public class CTEmbeddedWAVAudioFile
/*  11:    */ {
/*  12:    */   @XmlAttribute(namespace="http://schemas.openxmlformats.org/officeDocument/2006/relationships", required=true)
/*  13:    */   protected String embed;
/*  14:    */   @XmlAttribute
/*  15:    */   protected String name;
/*  16:    */   @XmlAttribute
/*  17:    */   protected Boolean builtIn;
/*  18:    */   
/*  19:    */   public String getEmbed()
/*  20:    */   {
/*  21: 65 */     return this.embed;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setEmbed(String value)
/*  25:    */   {
/*  26: 77 */     this.embed = value;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public boolean isSetEmbed()
/*  30:    */   {
/*  31: 81 */     return this.embed != null;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public String getName()
/*  35:    */   {
/*  36: 93 */     if (this.name == null) {
/*  37: 94 */       return "";
/*  38:    */     }
/*  39: 96 */     return this.name;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setName(String value)
/*  43:    */   {
/*  44:109 */     this.name = value;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public boolean isSetName()
/*  48:    */   {
/*  49:113 */     return this.name != null;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public boolean isBuiltIn()
/*  53:    */   {
/*  54:125 */     if (this.builtIn == null) {
/*  55:126 */       return false;
/*  56:    */     }
/*  57:128 */     return this.builtIn.booleanValue();
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setBuiltIn(boolean value)
/*  61:    */   {
/*  62:141 */     this.builtIn = Boolean.valueOf(value);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public boolean isSetBuiltIn()
/*  66:    */   {
/*  67:145 */     return this.builtIn != null;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void unsetBuiltIn()
/*  71:    */   {
/*  72:149 */     this.builtIn = null;
/*  73:    */   }
/*  74:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTEmbeddedWAVAudioFile
 * JD-Core Version:    0.7.0.1
 */