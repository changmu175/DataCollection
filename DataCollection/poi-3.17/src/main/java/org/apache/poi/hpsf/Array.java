/*   1:    */ package org.apache.poi.hpsf;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.Internal;
/*   4:    */ import org.apache.poi.util.LittleEndianByteArrayInputStream;
/*   5:    */ 
/*   6:    */ @Internal
/*   7:    */ class Array
/*   8:    */ {
/*   9:    */   static class ArrayDimension
/*  10:    */   {
/*  11:    */     private long _size;
/*  12:    */     private int _indexOffset;
/*  13:    */     
/*  14:    */     void read(LittleEndianByteArrayInputStream lei)
/*  15:    */     {
/*  16: 31 */       this._size = lei.readUInt();
/*  17: 32 */       this._indexOffset = lei.readInt();
/*  18:    */     }
/*  19:    */   }
/*  20:    */   
/*  21:    */   static class ArrayHeader
/*  22:    */   {
/*  23:    */     private ArrayDimension[] _dimensions;
/*  24:    */     private int _type;
/*  25:    */     
/*  26:    */     void read(LittleEndianByteArrayInputStream lei)
/*  27:    */     {
/*  28: 42 */       this._type = lei.readInt();
/*  29:    */       
/*  30: 44 */       long numDimensionsUnsigned = lei.readUInt();
/*  31: 46 */       if ((1L > numDimensionsUnsigned) || (numDimensionsUnsigned > 31L))
/*  32:    */       {
/*  33: 47 */         String msg = "Array dimension number " + numDimensionsUnsigned + " is not in [1; 31] range";
/*  34: 48 */         throw new IllegalPropertySetDataException(msg);
/*  35:    */       }
/*  36: 51 */       int numDimensions = (int)numDimensionsUnsigned;
/*  37:    */       
/*  38: 53 */       this._dimensions = new ArrayDimension[numDimensions];
/*  39: 54 */       for (int i = 0; i < numDimensions; i++)
/*  40:    */       {
/*  41: 55 */         ArrayDimension ad = new ArrayDimension();
/*  42: 56 */         ad.read(lei);
/*  43: 57 */         this._dimensions[i] = ad;
/*  44:    */       }
/*  45:    */     }
/*  46:    */     
/*  47:    */     long getNumberOfScalarValues()
/*  48:    */     {
/*  49: 62 */       long result = 1L;
/*  50: 63 */       for (ArrayDimension dimension : this._dimensions) {
/*  51: 64 */         result *= ArrayDimension.access$000(dimension);
/*  52:    */       }
/*  53: 66 */       return result;
/*  54:    */     }
/*  55:    */     
/*  56:    */     int getType()
/*  57:    */     {
/*  58: 70 */       return this._type;
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62: 74 */   private final ArrayHeader _header = new ArrayHeader();
/*  63:    */   private TypedPropertyValue[] _values;
/*  64:    */   
/*  65:    */   void read(LittleEndianByteArrayInputStream lei)
/*  66:    */   {
/*  67: 80 */     this._header.read(lei);
/*  68:    */     
/*  69: 82 */     long numberOfScalarsLong = this._header.getNumberOfScalarValues();
/*  70: 83 */     if (numberOfScalarsLong > 2147483647L)
/*  71:    */     {
/*  72: 84 */       String msg = "Sorry, but POI can't store array of properties with size of " + numberOfScalarsLong + " in memory";
/*  73:    */       
/*  74:    */ 
/*  75: 87 */       throw new UnsupportedOperationException(msg);
/*  76:    */     }
/*  77: 89 */     int numberOfScalars = (int)numberOfScalarsLong;
/*  78:    */     
/*  79: 91 */     this._values = new TypedPropertyValue[numberOfScalars];
/*  80: 92 */     int paddedType = this._header._type == 12 ? 0 : this._header._type;
/*  81: 93 */     for (int i = 0; i < numberOfScalars; i++)
/*  82:    */     {
/*  83: 94 */       TypedPropertyValue typedPropertyValue = new TypedPropertyValue(paddedType, null);
/*  84: 95 */       typedPropertyValue.read(lei);
/*  85: 96 */       this._values[i] = typedPropertyValue;
/*  86: 97 */       if (paddedType != 0) {
/*  87: 98 */         TypedPropertyValue.skipPadding(lei);
/*  88:    */       }
/*  89:    */     }
/*  90:    */   }
/*  91:    */   
/*  92:    */   TypedPropertyValue[] getValues()
/*  93:    */   {
/*  94:104 */     return this._values;
/*  95:    */   }
/*  96:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hpsf.Array

 * JD-Core Version:    0.7.0.1

 */