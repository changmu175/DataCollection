/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.HexDump;
/*   4:    */ import org.apache.poi.util.LittleEndian;
/*   5:    */ 
/*   6:    */ public class EscherSimpleProperty
/*   7:    */   extends EscherProperty
/*   8:    */ {
/*   9:    */   private int propertyValue;
/*  10:    */   
/*  11:    */   public EscherSimpleProperty(short id, int propertyValue)
/*  12:    */   {
/*  13: 41 */     super(id);
/*  14: 42 */     this.propertyValue = propertyValue;
/*  15:    */   }
/*  16:    */   
/*  17:    */   public EscherSimpleProperty(short propertyNumber, boolean isComplex, boolean isBlipId, int propertyValue)
/*  18:    */   {
/*  19: 55 */     super(propertyNumber, isComplex, isBlipId);
/*  20: 56 */     this.propertyValue = propertyValue;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public int serializeSimplePart(byte[] data, int offset)
/*  24:    */   {
/*  25: 67 */     LittleEndian.putShort(data, offset, getId());
/*  26: 68 */     LittleEndian.putInt(data, offset + 2, this.propertyValue);
/*  27: 69 */     return 6;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public int serializeComplexPart(byte[] data, int pos)
/*  31:    */   {
/*  32: 79 */     return 0;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public int getPropertyValue()
/*  36:    */   {
/*  37: 87 */     return this.propertyValue;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public boolean equals(Object o)
/*  41:    */   {
/*  42: 96 */     if (this == o) {
/*  43: 97 */       return true;
/*  44:    */     }
/*  45: 99 */     if (!(o instanceof EscherSimpleProperty)) {
/*  46:100 */       return false;
/*  47:    */     }
/*  48:103 */     EscherSimpleProperty escherSimpleProperty = (EscherSimpleProperty)o;
/*  49:105 */     if (this.propertyValue != escherSimpleProperty.propertyValue) {
/*  50:106 */       return false;
/*  51:    */     }
/*  52:108 */     if (getId() != escherSimpleProperty.getId()) {
/*  53:109 */       return false;
/*  54:    */     }
/*  55:112 */     return true;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public int hashCode()
/*  59:    */   {
/*  60:122 */     return this.propertyValue;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public String toString()
/*  64:    */   {
/*  65:131 */     return "propNum: " + getPropertyNumber() + ", RAW: 0x" + HexDump.toHex(getId()) + ", propName: " + EscherProperties.getPropertyName(getPropertyNumber()) + ", complex: " + isComplex() + ", blipId: " + isBlipId() + ", value: " + this.propertyValue + " (0x" + HexDump.toHex(this.propertyValue) + ")";
/*  66:    */   }
/*  67:    */   
/*  68:    */   public String toXml(String tab)
/*  69:    */   {
/*  70:141 */     StringBuilder builder = new StringBuilder();
/*  71:142 */     builder.append(tab).append("<").append(getClass().getSimpleName()).append(" id=\"0x").append(HexDump.toHex(getId())).append("\" name=\"").append(getName()).append("\" blipId=\"").append(isBlipId()).append("\" complex=\"").append(isComplex()).append("\" value=\"").append("0x").append(HexDump.toHex(this.propertyValue)).append("\"/>");
/*  72:    */     
/*  73:    */ 
/*  74:    */ 
/*  75:146 */     return builder.toString();
/*  76:    */   }
/*  77:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherSimpleProperty
 * JD-Core Version:    0.7.0.1
 */