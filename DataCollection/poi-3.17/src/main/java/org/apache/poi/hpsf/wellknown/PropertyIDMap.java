/*   1:    */ package org.apache.poi.hpsf.wellknown;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Map.Entry;
/*   9:    */ import java.util.Set;
/*  10:    */ 
/*  11:    */ public class PropertyIDMap
/*  12:    */   implements Map<Long, String>
/*  13:    */ {
/*  14:    */   public static final int PID_TITLE = 2;
/*  15:    */   public static final int PID_SUBJECT = 3;
/*  16:    */   public static final int PID_AUTHOR = 4;
/*  17:    */   public static final int PID_KEYWORDS = 5;
/*  18:    */   public static final int PID_COMMENTS = 6;
/*  19:    */   public static final int PID_TEMPLATE = 7;
/*  20:    */   public static final int PID_LASTAUTHOR = 8;
/*  21:    */   public static final int PID_REVNUMBER = 9;
/*  22:    */   public static final int PID_EDITTIME = 10;
/*  23:    */   public static final int PID_LASTPRINTED = 11;
/*  24:    */   public static final int PID_CREATE_DTM = 12;
/*  25:    */   public static final int PID_LASTSAVE_DTM = 13;
/*  26:    */   public static final int PID_PAGECOUNT = 14;
/*  27:    */   public static final int PID_WORDCOUNT = 15;
/*  28:    */   public static final int PID_CHARCOUNT = 16;
/*  29:    */   public static final int PID_THUMBNAIL = 17;
/*  30:    */   public static final int PID_APPNAME = 18;
/*  31:    */   public static final int PID_SECURITY = 19;
/*  32:    */   public static final int PID_DICTIONARY = 0;
/*  33:    */   public static final int PID_CODEPAGE = 1;
/*  34:    */   public static final int PID_CATEGORY = 2;
/*  35:    */   public static final int PID_PRESFORMAT = 3;
/*  36:    */   public static final int PID_BYTECOUNT = 4;
/*  37:    */   public static final int PID_LINECOUNT = 5;
/*  38:    */   public static final int PID_PARCOUNT = 6;
/*  39:    */   public static final int PID_SLIDECOUNT = 7;
/*  40:    */   public static final int PID_NOTECOUNT = 8;
/*  41:    */   public static final int PID_HIDDENCOUNT = 9;
/*  42:    */   public static final int PID_MMCLIPCOUNT = 10;
/*  43:    */   public static final int PID_SCALE = 11;
/*  44:    */   public static final int PID_HEADINGPAIR = 12;
/*  45:    */   public static final int PID_DOCPARTS = 13;
/*  46:    */   public static final int PID_MANAGER = 14;
/*  47:    */   public static final int PID_COMPANY = 15;
/*  48:    */   public static final int PID_LINKSDIRTY = 16;
/*  49:    */   public static final int PID_CCHWITHSPACES = 17;
/*  50:    */   public static final int PID_HYPERLINKSCHANGED = 22;
/*  51:    */   public static final int PID_VERSION = 23;
/*  52:    */   public static final int PID_DIGSIG = 24;
/*  53:    */   public static final int PID_CONTENTTYPE = 26;
/*  54:    */   public static final int PID_CONTENTSTATUS = 27;
/*  55:    */   public static final int PID_LANGUAGE = 28;
/*  56:    */   public static final int PID_DOCVERSION = 29;
/*  57:    */   public static final int PID_MAX = 31;
/*  58:    */   public static final int PID_LOCALE = -2147483648;
/*  59:    */   public static final int PID_BEHAVIOUR = -2147483645;
/*  60:    */   private static PropertyIDMap summaryInformationProperties;
/*  61:325 */   private static final Object[][] summaryInformationIdValues = { { Long.valueOf(2L), "PID_TITLE" }, { Long.valueOf(3L), "PID_SUBJECT" }, { Long.valueOf(4L), "PID_AUTHOR" }, { Long.valueOf(5L), "PID_KEYWORDS" }, { Long.valueOf(6L), "PID_COMMENTS" }, { Long.valueOf(7L), "PID_TEMPLATE" }, { Long.valueOf(8L), "PID_LASTAUTHOR" }, { Long.valueOf(9L), "PID_REVNUMBER" }, { Long.valueOf(10L), "PID_EDITTIME" }, { Long.valueOf(11L), "PID_LASTPRINTED" }, { Long.valueOf(12L), "PID_CREATE_DTM" }, { Long.valueOf(13L), "PID_LASTSAVE_DTM" }, { Long.valueOf(14L), "PID_PAGECOUNT" }, { Long.valueOf(15L), "PID_WORDCOUNT" }, { Long.valueOf(16L), "PID_CHARCOUNT" }, { Long.valueOf(17L), "PID_THUMBNAIL" }, { Long.valueOf(18L), "PID_APPNAME" }, { Long.valueOf(19L), "PID_SECURITY" } };
/*  62:    */   private static PropertyIDMap documentSummaryInformationProperties;
/*  63:352 */   private static final Object[][] documentSummaryInformationIdValues = { { Long.valueOf(0L), "PID_DICTIONARY" }, { Long.valueOf(1L), "PID_CODEPAGE" }, { Long.valueOf(2L), "PID_CATEGORY" }, { Long.valueOf(3L), "PID_PRESFORMAT" }, { Long.valueOf(4L), "PID_BYTECOUNT" }, { Long.valueOf(5L), "PID_LINECOUNT" }, { Long.valueOf(6L), "PID_PARCOUNT" }, { Long.valueOf(7L), "PID_SLIDECOUNT" }, { Long.valueOf(8L), "PID_NOTECOUNT" }, { Long.valueOf(9L), "PID_HIDDENCOUNT" }, { Long.valueOf(10L), "PID_MMCLIPCOUNT" }, { Long.valueOf(11L), "PID_SCALE" }, { Long.valueOf(12L), "PID_HEADINGPAIR" }, { Long.valueOf(13L), "PID_DOCPARTS" }, { Long.valueOf(14L), "PID_MANAGER" }, { Long.valueOf(15L), "PID_COMPANY" }, { Long.valueOf(16L), "PID_LINKSDIRTY" } };
/*  64:    */   private static PropertyIDMap fallbackProperties;
/*  65:378 */   private static final Object[][] fallbackIdValues = { { Long.valueOf(0L), "PID_DICTIONARY" }, { Long.valueOf(1L), "PID_CODEPAGE" }, { Long.valueOf(2L), "PID_CATEGORY" }, { Long.valueOf(3L), "PID_PRESFORMAT" }, { Long.valueOf(4L), "PID_BYTECOUNT" }, { Long.valueOf(5L), "PID_LINECOUNT" }, { Long.valueOf(6L), "PID_PARCOUNT" }, { Long.valueOf(7L), "PID_SLIDECOUNT" }, { Long.valueOf(8L), "PID_NOTECOUNT" }, { Long.valueOf(9L), "PID_HIDDENCOUNT" }, { Long.valueOf(10L), "PID_MMCLIPCOUNT" }, { Long.valueOf(11L), "PID_SCALE" }, { Long.valueOf(12L), "PID_HEADINGPAIR" }, { Long.valueOf(13L), "PID_DOCPARTS" }, { Long.valueOf(14L), "PID_MANAGER" }, { Long.valueOf(15L), "PID_COMPANY" }, { Long.valueOf(16L), "PID_LINKSDIRTY" }, { Long.valueOf(17L), "PID_CCHWITHSPACES" }, { Long.valueOf(22L), "PID_HYPERLINKSCHANGED" }, { Long.valueOf(23L), "PID_VERSION" }, { Long.valueOf(24L), "PID_DIGSIG" }, { Long.valueOf(26L), "PID_CONTENTTYPE" }, { Long.valueOf(27L), "PID_CONTENTSTATUS" }, { Long.valueOf(28L), "PID_LANGUAGE" }, { Long.valueOf(29L), "PID_DOCVERSION" }, { Long.valueOf(31L), "PID_MAX" }, { Long.valueOf(-2147483648L), "PID_LOCALE" }, { Long.valueOf(-2147483645L), "PID_BEHAVIOUR" } };
/*  66:    */   private final Map<Long, String> idMap;
/*  67:    */   
/*  68:    */   private PropertyIDMap(Object[][] idValues)
/*  69:    */   {
/*  70:423 */     Map<Long, String> m = new HashMap(idValues.length);
/*  71:424 */     for (Object[] idValue : idValues) {
/*  72:425 */       m.put((Long)idValue[0], (String)idValue[1]);
/*  73:    */     }
/*  74:427 */     this.idMap = Collections.unmodifiableMap(m);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public static synchronized PropertyIDMap getSummaryInformationProperties()
/*  78:    */   {
/*  79:434 */     if (summaryInformationProperties == null) {
/*  80:435 */       summaryInformationProperties = new PropertyIDMap(summaryInformationIdValues);
/*  81:    */     }
/*  82:437 */     return summaryInformationProperties;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public static synchronized PropertyIDMap getDocumentSummaryInformationProperties()
/*  86:    */   {
/*  87:444 */     if (documentSummaryInformationProperties == null) {
/*  88:445 */       documentSummaryInformationProperties = new PropertyIDMap(documentSummaryInformationIdValues);
/*  89:    */     }
/*  90:447 */     return documentSummaryInformationProperties;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public static synchronized PropertyIDMap getFallbackProperties()
/*  94:    */   {
/*  95:455 */     if (fallbackProperties == null) {
/*  96:456 */       fallbackProperties = new PropertyIDMap(fallbackIdValues);
/*  97:    */     }
/*  98:458 */     return fallbackProperties;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public int size()
/* 102:    */   {
/* 103:463 */     return this.idMap.size();
/* 104:    */   }
/* 105:    */   
/* 106:    */   public boolean isEmpty()
/* 107:    */   {
/* 108:468 */     return this.idMap.isEmpty();
/* 109:    */   }
/* 110:    */   
/* 111:    */   public boolean containsKey(Object key)
/* 112:    */   {
/* 113:473 */     return this.idMap.containsKey(key);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public boolean containsValue(Object value)
/* 117:    */   {
/* 118:478 */     return this.idMap.containsValue(value);
/* 119:    */   }
/* 120:    */   
/* 121:    */   public String get(Object key)
/* 122:    */   {
/* 123:483 */     return (String)this.idMap.get(key);
/* 124:    */   }
/* 125:    */   
/* 126:    */   public String put(Long key, String value)
/* 127:    */   {
/* 128:488 */     return (String)this.idMap.put(key, value);
/* 129:    */   }
/* 130:    */   
/* 131:    */   public String remove(Object key)
/* 132:    */   {
/* 133:493 */     return (String)this.idMap.remove(key);
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void putAll(Map<? extends Long, ? extends String> m)
/* 137:    */   {
/* 138:498 */     this.idMap.putAll(m);
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void clear()
/* 142:    */   {
/* 143:503 */     this.idMap.clear();
/* 144:    */   }
/* 145:    */   
/* 146:    */   public Set<Long> keySet()
/* 147:    */   {
/* 148:508 */     return this.idMap.keySet();
/* 149:    */   }
/* 150:    */   
/* 151:    */   public Collection<String> values()
/* 152:    */   {
/* 153:513 */     return this.idMap.values();
/* 154:    */   }
/* 155:    */   
/* 156:    */   public Set<Entry<Long, String>> entrySet()
/* 157:    */   {
/* 158:518 */     return this.idMap.entrySet();
/* 159:    */   }
/* 160:    */   
/* 161:    */   public static void main(String[] args)
/* 162:    */   {
/* 163:527 */     PropertyIDMap s1 = getSummaryInformationProperties();
/* 164:528 */     PropertyIDMap s2 = getDocumentSummaryInformationProperties();
/* 165:529 */     System.out.println("s1: " + s1);
/* 166:530 */     System.out.println("s2: " + s2);
/* 167:    */   }
/* 168:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hpsf.wellknown.PropertyIDMap

 * JD-Core Version:    0.7.0.1

 */