/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.common.UnicodeString;
/*  4:   */ import org.apache.poi.util.IntMapper;
/*  5:   */ import org.apache.poi.util.POILogFactory;
/*  6:   */ import org.apache.poi.util.POILogger;
/*  7:   */ 
/*  8:   */ class SSTDeserializer
/*  9:   */ {
/* 10:35 */   private static POILogger logger = POILogFactory.getLogger(SSTDeserializer.class);
/* 11:   */   private IntMapper<UnicodeString> strings;
/* 12:   */   
/* 13:   */   public SSTDeserializer(IntMapper<UnicodeString> strings)
/* 14:   */   {
/* 15:40 */     this.strings = strings;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void manufactureStrings(int stringCount, RecordInputStream in)
/* 19:   */   {
/* 20:50 */     for (int i = 0; i < stringCount; i++)
/* 21:   */     {
/* 22:   */       UnicodeString str;
/* 23:   */       UnicodeString str;
/* 24:53 */       if ((in.available() == 0) && (!in.hasNextRecord()))
/* 25:   */       {
/* 26:54 */         logger.log(7, new Object[] { "Ran out of data before creating all the strings! String at index " + i + "" });
/* 27:55 */         str = new UnicodeString("");
/* 28:   */       }
/* 29:   */       else
/* 30:   */       {
/* 31:57 */         str = new UnicodeString(in);
/* 32:   */       }
/* 33:59 */       addToStringTable(this.strings, str);
/* 34:   */     }
/* 35:   */   }
/* 36:   */   
/* 37:   */   public static void addToStringTable(IntMapper<UnicodeString> strings, UnicodeString string)
/* 38:   */   {
/* 39:65 */     strings.add(string);
/* 40:   */   }
/* 41:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.SSTDeserializer
 * JD-Core Version:    0.7.0.1
 */