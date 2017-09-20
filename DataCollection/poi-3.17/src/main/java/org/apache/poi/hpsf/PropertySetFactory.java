/*   1:    */ package org.apache.poi.hpsf;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.UnsupportedEncodingException;
/*   6:    */ import org.apache.poi.hpsf.wellknown.SectionIDMap;
/*   7:    */ import org.apache.poi.util.LittleEndianInputStream;
/*   8:    */ 
/*   9:    */ public class PropertySetFactory
/*  10:    */ {
/*  11:    */   /* Error */
/*  12:    */   public static PropertySet create(org.apache.poi.poifs.filesystem.DirectoryEntry dir, String name)
/*  13:    */     throws java.io.FileNotFoundException, NoPropertySetStreamException, IOException, UnsupportedEncodingException
/*  14:    */   {
/*  15:    */     // Byte code:
/*  16:    */     //   0: aconst_null
/*  17:    */     //   1: astore_2
/*  18:    */     //   2: aload_0
/*  19:    */     //   3: aload_1
/*  20:    */     //   4: invokeinterface 2 2 0
/*  21:    */     //   9: checkcast 3	org/apache/poi/poifs/filesystem/DocumentEntry
/*  22:    */     //   12: astore_3
/*  23:    */     //   13: new 4	org/apache/poi/poifs/filesystem/DocumentInputStream
/*  24:    */     //   16: dup
/*  25:    */     //   17: aload_3
/*  26:    */     //   18: invokespecial 5	org/apache/poi/poifs/filesystem/DocumentInputStream:<init>	(Lorg/apache/poi/poifs/filesystem/DocumentEntry;)V
/*  27:    */     //   21: astore_2
/*  28:    */     //   22: aload_2
/*  29:    */     //   23: invokestatic 6	org/apache/poi/hpsf/PropertySetFactory:create	(Ljava/io/InputStream;)Lorg/apache/poi/hpsf/PropertySet;
/*  30:    */     //   26: astore 4
/*  31:    */     //   28: aload_2
/*  32:    */     //   29: ifnull +7 -> 36
/*  33:    */     //   32: aload_2
/*  34:    */     //   33: invokevirtual 7	java/io/InputStream:close	()V
/*  35:    */     //   36: aload 4
/*  36:    */     //   38: areturn
/*  37:    */     //   39: astore 4
/*  38:    */     //   41: aconst_null
/*  39:    */     //   42: astore 5
/*  40:    */     //   44: aload_2
/*  41:    */     //   45: ifnull +7 -> 52
/*  42:    */     //   48: aload_2
/*  43:    */     //   49: invokevirtual 7	java/io/InputStream:close	()V
/*  44:    */     //   52: aload 5
/*  45:    */     //   54: areturn
/*  46:    */     //   55: astore 6
/*  47:    */     //   57: aload_2
/*  48:    */     //   58: ifnull +7 -> 65
/*  49:    */     //   61: aload_2
/*  50:    */     //   62: invokevirtual 7	java/io/InputStream:close	()V
/*  51:    */     //   65: aload 6
/*  52:    */     //   67: athrow
/*  53:    */     // Line number table:
/*  54:    */     //   Java source line #56	-> byte code offset #0
/*  55:    */     //   Java source line #58	-> byte code offset #2
/*  56:    */     //   Java source line #59	-> byte code offset #13
/*  57:    */     //   Java source line #61	-> byte code offset #22
/*  58:    */     //   Java source line #66	-> byte code offset #28
/*  59:    */     //   Java source line #67	-> byte code offset #32
/*  60:    */     //   Java source line #62	-> byte code offset #39
/*  61:    */     //   Java source line #63	-> byte code offset #41
/*  62:    */     //   Java source line #66	-> byte code offset #44
/*  63:    */     //   Java source line #67	-> byte code offset #48
/*  64:    */     //   Java source line #66	-> byte code offset #55
/*  65:    */     //   Java source line #67	-> byte code offset #61
/*  66:    */     // Local variable table:
/*  67:    */     //   start	length	slot	name	signature
/*  68:    */     //   0	68	0	dir	org.apache.poi.poifs.filesystem.DirectoryEntry
/*  69:    */     //   0	68	1	name	java.lang.String
/*  70:    */     //   1	61	2	inp	InputStream
/*  71:    */     //   12	6	3	entry	org.apache.poi.poifs.filesystem.DocumentEntry
/*  72:    */     //   26	11	4	localPropertySet1	PropertySet
/*  73:    */     //   39	3	4	e	MarkUnsupportedException
/*  74:    */     //   42	11	5	localPropertySet2	PropertySet
/*  75:    */     //   55	11	6	localObject	Object
/*  76:    */     // Exception table:
/*  77:    */     //   from	to	target	type
/*  78:    */     //   22	28	39	org/apache/poi/hpsf/MarkUnsupportedException
/*  79:    */     //   2	28	55	finally
/*  80:    */     //   39	44	55	finally
/*  81:    */     //   55	57	55	finally
/*  82:    */   }
/*  83:    */   
/*  84:    */   public static PropertySet create(InputStream stream)
/*  85:    */     throws NoPropertySetStreamException, MarkUnsupportedException, UnsupportedEncodingException, IOException
/*  86:    */   {
/*  87: 92 */     stream.mark(45);
/*  88: 93 */     LittleEndianInputStream leis = new LittleEndianInputStream(stream);
/*  89: 94 */     int byteOrder = leis.readUShort();
/*  90: 95 */     int format = leis.readUShort();
/*  91: 96 */     int osVersion = (int)leis.readUInt();
/*  92: 97 */     byte[] clsIdBuf = new byte[16];
/*  93: 98 */     leis.readFully(clsIdBuf);
/*  94: 99 */     int sectionCount = (int)leis.readUInt();
/*  95:101 */     if ((byteOrder != 65534) || (format != 0) || (sectionCount < 0)) {
/*  96:104 */       throw new NoPropertySetStreamException();
/*  97:    */     }
/*  98:107 */     if (sectionCount > 0) {
/*  99:108 */       leis.readFully(clsIdBuf);
/* 100:    */     }
/* 101:110 */     stream.reset();
/* 102:    */     
/* 103:112 */     ClassID clsId = new ClassID(clsIdBuf, 0);
/* 104:113 */     if (sectionCount > 0) {
/* 105:113 */       if (PropertySet.matchesSummary(clsId, new ClassID[] { SectionIDMap.SUMMARY_INFORMATION_ID })) {
/* 106:114 */         return new SummaryInformation(stream);
/* 107:    */       }
/* 108:    */     }
/* 109:115 */     if ((sectionCount > 0) && (PropertySet.matchesSummary(clsId, SectionIDMap.DOCUMENT_SUMMARY_INFORMATION_ID))) {
/* 110:116 */       return new DocumentSummaryInformation(stream);
/* 111:    */     }
/* 112:118 */     return new PropertySet(stream);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public static SummaryInformation newSummaryInformation()
/* 116:    */   {
/* 117:128 */     return new SummaryInformation();
/* 118:    */   }
/* 119:    */   
/* 120:    */   public static DocumentSummaryInformation newDocumentSummaryInformation()
/* 121:    */   {
/* 122:137 */     return new DocumentSummaryInformation();
/* 123:    */   }
/* 124:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hpsf.PropertySetFactory

 * JD-Core Version:    0.7.0.1

 */