/*  1:   */ package org.apache.poi.sl.draw.binding;
/*  2:   */ 
/*  3:   */ import javax.xml.bind.annotation.XmlAccessType;
/*  4:   */ import javax.xml.bind.annotation.XmlAccessorType;
/*  5:   */ import javax.xml.bind.annotation.XmlAttribute;
/*  6:   */ import javax.xml.bind.annotation.XmlSchemaType;
/*  7:   */ import javax.xml.bind.annotation.XmlType;
/*  8:   */ 
/*  9:   */ @XmlAccessorType(XmlAccessType.FIELD)
/* 10:   */ @XmlType(name="CT_Connection", namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/* 11:   */ public class CTConnection
/* 12:   */ {
/* 13:   */   @XmlAttribute(required=true)
/* 14:   */   protected long id;
/* 15:   */   @XmlAttribute(required=true)
/* 16:   */   @XmlSchemaType(name="unsignedInt")
/* 17:   */   protected long idx;
/* 18:   */   
/* 19:   */   public long getId()
/* 20:   */   {
/* 21:60 */     return this.id;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void setId(long value)
/* 25:   */   {
/* 26:68 */     this.id = value;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public boolean isSetId()
/* 30:   */   {
/* 31:72 */     return true;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public long getIdx()
/* 35:   */   {
/* 36:80 */     return this.idx;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public void setIdx(long value)
/* 40:   */   {
/* 41:88 */     this.idx = value;
/* 42:   */   }
/* 43:   */   
/* 44:   */   public boolean isSetIdx()
/* 45:   */   {
/* 46:92 */     return true;
/* 47:   */   }
/* 48:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTConnection
 * JD-Core Version:    0.7.0.1
 */