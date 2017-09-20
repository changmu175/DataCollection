/*   1:    */ package org.apache.poi.poifs.property;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.poi.poifs.storage.ListManagedBlock;
/*   7:    */ 
/*   8:    */ class PropertyFactory
/*   9:    */ {
/*  10:    */   static List<Property> convertToProperties(ListManagedBlock[] blocks)
/*  11:    */     throws IOException
/*  12:    */   {
/*  13: 60 */     List<Property> properties = new ArrayList();
/*  14: 62 */     for (ListManagedBlock block : blocks)
/*  15:    */     {
/*  16: 63 */       byte[] data = block.getData();
/*  17: 64 */       convertToProperties(data, properties);
/*  18:    */     }
/*  19: 66 */     return properties;
/*  20:    */   }
/*  21:    */   
/*  22:    */   static void convertToProperties(byte[] data, List<Property> properties)
/*  23:    */     throws IOException
/*  24:    */   {
/*  25: 72 */     int property_count = data.length / 128;
/*  26: 73 */     int offset = 0;
/*  27: 75 */     for (int k = 0; k < property_count; k++)
/*  28:    */     {
/*  29: 76 */       switch (data[(offset + 66)])
/*  30:    */       {
/*  31:    */       case 1: 
/*  32: 78 */         properties.add(new DirectoryProperty(properties.size(), data, offset));
/*  33:    */         
/*  34:    */ 
/*  35: 81 */         break;
/*  36:    */       case 2: 
/*  37: 84 */         properties.add(new DocumentProperty(properties.size(), data, offset));
/*  38:    */         
/*  39:    */ 
/*  40: 87 */         break;
/*  41:    */       case 5: 
/*  42: 90 */         properties.add(new RootProperty(properties.size(), data, offset));
/*  43:    */         
/*  44:    */ 
/*  45: 93 */         break;
/*  46:    */       case 3: 
/*  47:    */       case 4: 
/*  48:    */       default: 
/*  49: 96 */         properties.add(null);
/*  50:    */       }
/*  51:100 */       offset += 128;
/*  52:    */     }
/*  53:    */   }
/*  54:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.property.PropertyFactory
 * JD-Core Version:    0.7.0.1
 */