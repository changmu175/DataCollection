/*  1:   */ package org.apache.poi.ddf;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import org.apache.poi.util.LittleEndian;
/*  6:   */ 
/*  7:   */ public final class EscherPropertyFactory
/*  8:   */ {
/*  9:   */   public List<EscherProperty> createProperties(byte[] data, int offset, short numProperties)
/* 10:   */   {
/* 11:38 */     List<EscherProperty> results = new ArrayList();
/* 12:   */     
/* 13:40 */     int pos = offset;
/* 14:42 */     for (int i = 0; i < numProperties; i++)
/* 15:   */     {
/* 16:45 */       short propId = LittleEndian.getShort(data, pos);
/* 17:46 */       int propData = LittleEndian.getInt(data, pos + 2);
/* 18:47 */       short propNumber = (short)(propId & 0x3FFF);
/* 19:48 */       boolean isComplex = (propId & 0xFFFF8000) != 0;
/* 20:   */       
/* 21:   */ 
/* 22:51 */       byte propertyType = EscherProperties.getPropertyType(propNumber);
/* 23:   */       EscherProperty ep;
/* 24:   */       EscherProperty ep;
/* 25:53 */       switch (propertyType)
/* 26:   */       {
/* 27:   */       case 1: 
/* 28:55 */         ep = new EscherBoolProperty(propId, propData);
/* 29:56 */         break;
/* 30:   */       case 2: 
/* 31:58 */         ep = new EscherRGBProperty(propId, propData);
/* 32:59 */         break;
/* 33:   */       case 3: 
/* 34:61 */         ep = new EscherShapePathProperty(propId, propData);
/* 35:62 */         break;
/* 36:   */       default: 
/* 37:64 */         if (!isComplex)
/* 38:   */         {
/* 39:65 */           ep = new EscherSimpleProperty(propId, propData);
/* 40:   */         }
/* 41:   */         else
/* 42:   */         {
/* 43:   */           EscherProperty ep;
/* 44:66 */           if (propertyType == 5) {
/* 45:67 */             ep = new EscherArrayProperty(propId, new byte[propData]);
/* 46:   */           } else {
/* 47:69 */             ep = new EscherComplexProperty(propId, new byte[propData]);
/* 48:   */           }
/* 49:   */         }
/* 50:   */         break;
/* 51:   */       }
/* 52:73 */       results.add(ep);
/* 53:74 */       pos += 6;
/* 54:   */     }
/* 55:78 */     for (EscherProperty p : results) {
/* 56:79 */       if ((p instanceof EscherComplexProperty)) {
/* 57:80 */         if ((p instanceof EscherArrayProperty))
/* 58:   */         {
/* 59:81 */           pos += ((EscherArrayProperty)p).setArrayData(data, pos);
/* 60:   */         }
/* 61:   */         else
/* 62:   */         {
/* 63:83 */           byte[] complexData = ((EscherComplexProperty)p).getComplexData();
/* 64:   */           
/* 65:85 */           int leftover = data.length - pos;
/* 66:86 */           if (leftover < complexData.length) {
/* 67:87 */             throw new IllegalStateException("Could not read complex escher property, length was " + complexData.length + ", but had only " + leftover + " bytes left");
/* 68:   */           }
/* 69:91 */           System.arraycopy(data, pos, complexData, 0, complexData.length);
/* 70:92 */           pos += complexData.length;
/* 71:   */         }
/* 72:   */       }
/* 73:   */     }
/* 74:96 */     return results;
/* 75:   */   }
/* 76:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherPropertyFactory
 * JD-Core Version:    0.7.0.1
 */