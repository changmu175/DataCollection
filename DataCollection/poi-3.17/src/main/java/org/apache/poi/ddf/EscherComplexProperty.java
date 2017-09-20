/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import org.apache.poi.util.HexDump;
/*   5:    */ import org.apache.poi.util.LittleEndian;
/*   6:    */ 
/*   7:    */ public class EscherComplexProperty
/*   8:    */   extends EscherProperty
/*   9:    */ {
/*  10:    */   private byte[] _complexData;
/*  11:    */   
/*  12:    */   public EscherComplexProperty(short id, byte[] complexData)
/*  13:    */   {
/*  14: 42 */     super(id);
/*  15: 43 */     if (complexData == null) {
/*  16: 44 */       throw new IllegalArgumentException("complexData can't be null");
/*  17:    */     }
/*  18: 46 */     this._complexData = ((byte[])complexData.clone());
/*  19:    */   }
/*  20:    */   
/*  21:    */   public EscherComplexProperty(short propertyNumber, boolean isBlipId, byte[] complexData)
/*  22:    */   {
/*  23: 58 */     super(propertyNumber, true, isBlipId);
/*  24: 59 */     if (complexData == null) {
/*  25: 60 */       throw new IllegalArgumentException("complexData can't be null");
/*  26:    */     }
/*  27: 62 */     this._complexData = ((byte[])complexData.clone());
/*  28:    */   }
/*  29:    */   
/*  30:    */   public int serializeSimplePart(byte[] data, int pos)
/*  31:    */   {
/*  32: 70 */     LittleEndian.putShort(data, pos, getId());
/*  33: 71 */     LittleEndian.putInt(data, pos + 2, this._complexData.length);
/*  34: 72 */     return 6;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public int serializeComplexPart(byte[] data, int pos)
/*  38:    */   {
/*  39: 84 */     System.arraycopy(this._complexData, 0, data, pos, this._complexData.length);
/*  40: 85 */     return this._complexData.length;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public byte[] getComplexData()
/*  44:    */   {
/*  45: 94 */     return this._complexData;
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected void setComplexData(byte[] _complexData)
/*  49:    */   {
/*  50: 98 */     this._complexData = _complexData;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public boolean equals(Object o)
/*  54:    */   {
/*  55:109 */     if (this == o) {
/*  56:110 */       return true;
/*  57:    */     }
/*  58:112 */     if ((o == null) || (!(o instanceof EscherComplexProperty))) {
/*  59:113 */       return false;
/*  60:    */     }
/*  61:116 */     EscherComplexProperty escherComplexProperty = (EscherComplexProperty)o;
/*  62:    */     
/*  63:118 */     return Arrays.equals(this._complexData, escherComplexProperty._complexData);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public int getPropertySize()
/*  67:    */   {
/*  68:129 */     return 6 + this._complexData.length;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public int hashCode()
/*  72:    */   {
/*  73:134 */     return getId() * 11;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public String toString()
/*  77:    */   {
/*  78:142 */     String dataStr = HexDump.toHex(this._complexData, 32);
/*  79:    */     
/*  80:144 */     return "propNum: " + getPropertyNumber() + ", propName: " + EscherProperties.getPropertyName(getPropertyNumber()) + ", complex: " + isComplex() + ", blipId: " + isBlipId() + ", data: " + System.getProperty("line.separator") + dataStr;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public String toXml(String tab)
/*  84:    */   {
/*  85:153 */     return tab + "<" + getClass().getSimpleName() + " id=\"0x" + HexDump.toHex(getId()) + "\" name=\"" + getName() + "\" blipId=\"" + isBlipId() + "\">\n" + tab + "</" + getClass().getSimpleName() + ">";
/*  86:    */   }
/*  87:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherComplexProperty
 * JD-Core Version:    0.7.0.1
 */