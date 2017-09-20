/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import java.util.Iterator;
/*   4:    */ import java.util.NoSuchElementException;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndian;
/*   7:    */ 
/*   8:    */ public final class EscherArrayProperty
/*   9:    */   extends EscherComplexProperty
/*  10:    */   implements Iterable<byte[]>
/*  11:    */ {
/*  12:    */   private static final int FIXED_SIZE = 6;
/*  13: 41 */   private boolean sizeIncludesHeaderSize = true;
/*  14:    */   private boolean emptyComplexPart;
/*  15:    */   
/*  16:    */   public EscherArrayProperty(short id, byte[] complexData)
/*  17:    */   {
/*  18: 49 */     super(id, checkComplexData(complexData));
/*  19: 50 */     this.emptyComplexPart = ((complexData == null) || (complexData.length == 0));
/*  20:    */   }
/*  21:    */   
/*  22:    */   public EscherArrayProperty(short propertyNumber, boolean isBlipId, byte[] complexData)
/*  23:    */   {
/*  24: 54 */     super(propertyNumber, isBlipId, checkComplexData(complexData));
/*  25:    */   }
/*  26:    */   
/*  27:    */   private static byte[] checkComplexData(byte[] complexData)
/*  28:    */   {
/*  29: 58 */     if ((complexData == null) || (complexData.length == 0)) {
/*  30: 59 */       return new byte[6];
/*  31:    */     }
/*  32: 62 */     return complexData;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public int getNumberOfElementsInArray()
/*  36:    */   {
/*  37: 66 */     return this.emptyComplexPart ? 0 : LittleEndian.getUShort(getComplexData(), 0);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setNumberOfElementsInArray(int numberOfElements)
/*  41:    */   {
/*  42: 70 */     int expectedArraySize = numberOfElements * getActualSizeOfElements(getSizeOfElements()) + 6;
/*  43: 71 */     if (expectedArraySize != getComplexData().length)
/*  44:    */     {
/*  45: 72 */       byte[] newArray = new byte[expectedArraySize];
/*  46: 73 */       System.arraycopy(getComplexData(), 0, newArray, 0, getComplexData().length);
/*  47: 74 */       setComplexData(newArray);
/*  48:    */     }
/*  49: 76 */     LittleEndian.putShort(getComplexData(), 0, (short)numberOfElements);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public int getNumberOfElementsInMemory()
/*  53:    */   {
/*  54: 80 */     return this.emptyComplexPart ? 0 : LittleEndian.getUShort(getComplexData(), 2);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setNumberOfElementsInMemory(int numberOfElements)
/*  58:    */   {
/*  59: 84 */     int expectedArraySize = numberOfElements * getActualSizeOfElements(getSizeOfElements()) + 6;
/*  60: 85 */     if (expectedArraySize != getComplexData().length)
/*  61:    */     {
/*  62: 86 */       byte[] newArray = new byte[expectedArraySize];
/*  63: 87 */       System.arraycopy(getComplexData(), 0, newArray, 0, expectedArraySize);
/*  64: 88 */       setComplexData(newArray);
/*  65:    */     }
/*  66: 90 */     LittleEndian.putShort(getComplexData(), 2, (short)numberOfElements);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public short getSizeOfElements()
/*  70:    */   {
/*  71: 94 */     return this.emptyComplexPart ? 0 : LittleEndian.getShort(getComplexData(), 4);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void setSizeOfElements(int sizeOfElements)
/*  75:    */   {
/*  76: 98 */     LittleEndian.putShort(getComplexData(), 4, (short)sizeOfElements);
/*  77:    */     
/*  78:100 */     int expectedArraySize = getNumberOfElementsInArray() * getActualSizeOfElements(getSizeOfElements()) + 6;
/*  79:101 */     if (expectedArraySize != getComplexData().length)
/*  80:    */     {
/*  81:103 */       byte[] newArray = new byte[expectedArraySize];
/*  82:104 */       System.arraycopy(getComplexData(), 0, newArray, 0, 6);
/*  83:105 */       setComplexData(newArray);
/*  84:    */     }
/*  85:    */   }
/*  86:    */   
/*  87:    */   public byte[] getElement(int index)
/*  88:    */   {
/*  89:110 */     int actualSize = getActualSizeOfElements(getSizeOfElements());
/*  90:111 */     byte[] result = new byte[actualSize];
/*  91:112 */     System.arraycopy(getComplexData(), 6 + index * actualSize, result, 0, result.length);
/*  92:113 */     return result;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setElement(int index, byte[] element)
/*  96:    */   {
/*  97:117 */     int actualSize = getActualSizeOfElements(getSizeOfElements());
/*  98:118 */     System.arraycopy(element, 0, getComplexData(), 6 + index * actualSize, actualSize);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public String toString()
/* 102:    */   {
/* 103:123 */     StringBuilder results = new StringBuilder();
/* 104:124 */     results.append("propNum: ").append(getPropertyNumber());
/* 105:125 */     results.append(", propName: ").append(EscherProperties.getPropertyName(getPropertyNumber()));
/* 106:126 */     results.append(", complex: ").append(isComplex());
/* 107:127 */     results.append(", blipId: ").append(isBlipId());
/* 108:128 */     results.append(", data: \n");
/* 109:129 */     results.append("    {EscherArrayProperty:\n");
/* 110:130 */     results.append("     Num Elements: ").append(getNumberOfElementsInArray()).append('\n');
/* 111:131 */     results.append("     Num Elements In Memory: ").append(getNumberOfElementsInMemory()).append('\n');
/* 112:132 */     results.append("     Size of elements: ").append(getSizeOfElements()).append('\n');
/* 113:133 */     for (int i = 0; i < getNumberOfElementsInArray(); i++) {
/* 114:134 */       results.append("     Element ").append(i).append(": ").append(HexDump.toHex(getElement(i))).append('\n');
/* 115:    */     }
/* 116:136 */     results.append("}\n");
/* 117:    */     
/* 118:138 */     return results.toString();
/* 119:    */   }
/* 120:    */   
/* 121:    */   public String toXml(String tab)
/* 122:    */   {
/* 123:143 */     StringBuilder builder = new StringBuilder();
/* 124:144 */     builder.append(tab).append("<").append(getClass().getSimpleName()).append(" id=\"0x").append(HexDump.toHex(getId())).append("\" name=\"").append(getName()).append("\" blipId=\"").append(isBlipId()).append("\">\n");
/* 125:147 */     for (int i = 0; i < getNumberOfElementsInArray(); i++) {
/* 126:148 */       builder.append("\t").append(tab).append("<Element>").append(HexDump.toHex(getElement(i))).append("</Element>\n");
/* 127:    */     }
/* 128:150 */     builder.append(tab).append("</").append(getClass().getSimpleName()).append(">");
/* 129:151 */     return builder.toString();
/* 130:    */   }
/* 131:    */   
/* 132:    */   public int setArrayData(byte[] data, int offset)
/* 133:    */   {
/* 134:165 */     if (this.emptyComplexPart)
/* 135:    */     {
/* 136:166 */       setComplexData(new byte[0]);
/* 137:    */     }
/* 138:    */     else
/* 139:    */     {
/* 140:168 */       short numElements = LittleEndian.getShort(data, offset);
/* 141:    */       
/* 142:170 */       short sizeOfElements = LittleEndian.getShort(data, offset + 4);
/* 143:    */       
/* 144:    */ 
/* 145:    */ 
/* 146:174 */       int arraySize = getActualSizeOfElements(sizeOfElements) * numElements;
/* 147:175 */       if (arraySize == getComplexData().length)
/* 148:    */       {
/* 149:177 */         setComplexData(new byte[arraySize + 6]);
/* 150:178 */         this.sizeIncludesHeaderSize = false;
/* 151:    */       }
/* 152:180 */       System.arraycopy(data, offset, getComplexData(), 0, getComplexData().length);
/* 153:    */     }
/* 154:182 */     return getComplexData().length;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public int serializeSimplePart(byte[] data, int pos)
/* 158:    */   {
/* 159:193 */     LittleEndian.putShort(data, pos, getId());
/* 160:194 */     int recordSize = getComplexData().length;
/* 161:195 */     if (!this.sizeIncludesHeaderSize) {
/* 162:196 */       recordSize -= 6;
/* 163:    */     }
/* 164:198 */     LittleEndian.putInt(data, pos + 2, recordSize);
/* 165:199 */     return 6;
/* 166:    */   }
/* 167:    */   
/* 168:    */   private static int getActualSizeOfElements(short sizeOfElements)
/* 169:    */   {
/* 170:207 */     if (sizeOfElements < 0) {
/* 171:208 */       return (short)(-sizeOfElements >> 2);
/* 172:    */     }
/* 173:210 */     return sizeOfElements;
/* 174:    */   }
/* 175:    */   
/* 176:    */   public Iterator<byte[]> iterator()
/* 177:    */   {
/* 178:215 */     new Iterator()
/* 179:    */     {
/* 180:    */       int idx;
/* 181:    */       
/* 182:    */       public boolean hasNext()
/* 183:    */       {
/* 184:219 */         return this.idx < EscherArrayProperty.this.getNumberOfElementsInArray();
/* 185:    */       }
/* 186:    */       
/* 187:    */       public byte[] next()
/* 188:    */       {
/* 189:224 */         if (!hasNext()) {
/* 190:225 */           throw new NoSuchElementException();
/* 191:    */         }
/* 192:227 */         return EscherArrayProperty.this.getElement(this.idx++);
/* 193:    */       }
/* 194:    */       
/* 195:    */       public void remove()
/* 196:    */       {
/* 197:232 */         throw new UnsupportedOperationException("not yet implemented");
/* 198:    */       }
/* 199:    */     };
/* 200:    */   }
/* 201:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherArrayProperty
 * JD-Core Version:    0.7.0.1
 */