/*   1:    */ package org.apache.poi.hpsf.wellknown;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import org.apache.poi.hpsf.ClassID;
/*   6:    */ import org.apache.poi.util.Internal;
/*   7:    */ 
/*   8:    */ @Internal
/*   9:    */ public class SectionIDMap
/*  10:    */ {
/*  11: 47 */   private static ThreadLocal<Map<ClassID, PropertyIDMap>> defaultMap = new ThreadLocal();
/*  12: 53 */   public static final ClassID SUMMARY_INFORMATION_ID = new ClassID("{F29F85E0-4FF9-1068-AB91-08002B27B3D9}");
/*  13: 59 */   private static final ClassID DOC_SUMMARY_INFORMATION = new ClassID("{D5CDD502-2E9C-101B-9397-08002B2CF9AE}");
/*  14: 61 */   private static final ClassID USER_DEFINED_PROPERTIES = new ClassID("{D5CDD505-2E9C-101B-9397-08002B2CF9AE}");
/*  15: 64 */   public static final ClassID[] DOCUMENT_SUMMARY_INFORMATION_ID = { DOC_SUMMARY_INFORMATION, USER_DEFINED_PROPERTIES };
/*  16:    */   public static final String UNDEFINED = "[undefined]";
/*  17:    */   
/*  18:    */   public static SectionIDMap getInstance()
/*  19:    */   {
/*  20: 80 */     Map<ClassID, PropertyIDMap> m = (Map)defaultMap.get();
/*  21: 81 */     if (m == null)
/*  22:    */     {
/*  23: 82 */       m = new HashMap();
/*  24: 83 */       m.put(SUMMARY_INFORMATION_ID, PropertyIDMap.getSummaryInformationProperties());
/*  25: 84 */       m.put(DOCUMENT_SUMMARY_INFORMATION_ID[0], PropertyIDMap.getDocumentSummaryInformationProperties());
/*  26: 85 */       defaultMap.set(m);
/*  27:    */     }
/*  28: 87 */     return new SectionIDMap();
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static String getPIDString(ClassID sectionFormatID, long pid)
/*  32:    */   {
/*  33:106 */     PropertyIDMap m = getInstance().get(sectionFormatID);
/*  34:107 */     if (m == null) {
/*  35:108 */       return "[undefined]";
/*  36:    */     }
/*  37:110 */     String s = m.get(Long.valueOf(pid));
/*  38:111 */     if (s == null) {
/*  39:112 */       return "[undefined]";
/*  40:    */     }
/*  41:114 */     return s;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public PropertyIDMap get(ClassID sectionFormatID)
/*  45:    */   {
/*  46:127 */     return getInstance().get(sectionFormatID);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public PropertyIDMap put(ClassID sectionFormatID, PropertyIDMap propertyIDMap)
/*  50:    */   {
/*  51:138 */     return getInstance().put(sectionFormatID, propertyIDMap);
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected PropertyIDMap put(String key, PropertyIDMap value)
/*  55:    */   {
/*  56:150 */     return put(new ClassID(key), value);
/*  57:    */   }
/*  58:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.wellknown.SectionIDMap
 * JD-Core Version:    0.7.0.1
 */