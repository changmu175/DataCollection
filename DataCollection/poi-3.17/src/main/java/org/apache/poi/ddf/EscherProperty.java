/*  1:   */ package org.apache.poi.ddf;
/*  2:   */ 
/*  3:   */ public abstract class EscherProperty
/*  4:   */ {
/*  5:   */   private short _id;
/*  6:   */   
/*  7:   */   public EscherProperty(short id)
/*  8:   */   {
/*  9:35 */     this._id = id;
/* 10:   */   }
/* 11:   */   
/* 12:   */   public EscherProperty(short propertyNumber, boolean isComplex, boolean isBlipId)
/* 13:   */   {
/* 14:47 */     this._id = ((short)(propertyNumber + (isComplex ? 32768 : 0) + (isBlipId ? 16384 : 0)));
/* 15:   */   }
/* 16:   */   
/* 17:   */   public short getId()
/* 18:   */   {
/* 19:53 */     return this._id;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public short getPropertyNumber()
/* 23:   */   {
/* 24:57 */     return (short)(this._id & 0x3FFF);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public boolean isComplex()
/* 28:   */   {
/* 29:61 */     return (this._id & 0xFFFF8000) != 0;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public boolean isBlipId()
/* 33:   */   {
/* 34:65 */     return (this._id & 0x4000) != 0;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public String getName()
/* 38:   */   {
/* 39:69 */     return EscherProperties.getPropertyName(getPropertyNumber());
/* 40:   */   }
/* 41:   */   
/* 42:   */   public int getPropertySize()
/* 43:   */   {
/* 44:79 */     return 6;
/* 45:   */   }
/* 46:   */   
/* 47:   */   public String toXml(String tab)
/* 48:   */   {
/* 49:83 */     StringBuilder builder = new StringBuilder();
/* 50:84 */     builder.append(tab).append("<").append(getClass().getSimpleName()).append(" id=\"").append(getId()).append("\" name=\"").append(getName()).append("\" blipId=\"").append(isBlipId()).append("\"/>\n");
/* 51:   */     
/* 52:86 */     return builder.toString();
/* 53:   */   }
/* 54:   */   
/* 55:   */   public abstract int serializeSimplePart(byte[] paramArrayOfByte, int paramInt);
/* 56:   */   
/* 57:   */   public abstract int serializeComplexPart(byte[] paramArrayOfByte, int paramInt);
/* 58:   */   
/* 59:   */   public abstract String toString();
/* 60:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherProperty
 * JD-Core Version:    0.7.0.1
 */