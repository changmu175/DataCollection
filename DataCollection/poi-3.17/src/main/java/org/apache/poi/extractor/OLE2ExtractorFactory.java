/*   1:    */ package org.apache.poi.extractor;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.List;
/*   9:    */ import org.apache.poi.EncryptedDocumentException;
/*  10:    */ import org.apache.poi.POIOLE2TextExtractor;
/*  11:    */ import org.apache.poi.POITextExtractor;
/*  12:    */ import org.apache.poi.hssf.OldExcelFormatException;
/*  13:    */ import org.apache.poi.hssf.extractor.EventBasedExcelExtractor;
/*  14:    */ import org.apache.poi.hssf.extractor.ExcelExtractor;
/*  15:    */ import org.apache.poi.hssf.model.InternalWorkbook;
/*  16:    */ import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
/*  17:    */ import org.apache.poi.poifs.crypt.Decryptor;
/*  18:    */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*  19:    */ import org.apache.poi.poifs.filesystem.DirectoryEntry;
/*  20:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  21:    */ import org.apache.poi.poifs.filesystem.Entry;
/*  22:    */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/*  23:    */ import org.apache.poi.poifs.filesystem.OPOIFSFileSystem;
/*  24:    */ import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/*  25:    */ import org.apache.poi.util.IOUtils;
/*  26:    */ import org.apache.poi.util.POILogFactory;
/*  27:    */ import org.apache.poi.util.POILogger;
/*  28:    */ 
/*  29:    */ public class OLE2ExtractorFactory
/*  30:    */ {
/*  31: 62 */   private static final POILogger LOGGER = POILogFactory.getLogger(OLE2ExtractorFactory.class);
/*  32: 65 */   private static final ThreadLocal<Boolean> threadPreferEventExtractors = new ThreadLocal()
/*  33:    */   {
/*  34:    */     protected Boolean initialValue()
/*  35:    */     {
/*  36: 67 */       return Boolean.FALSE;
/*  37:    */     }
/*  38:    */   };
/*  39:    */   private static Boolean allPreferEventExtractors;
/*  40:    */   
/*  41:    */   public static boolean getThreadPrefersEventExtractors()
/*  42:    */   {
/*  43: 79 */     return ((Boolean)threadPreferEventExtractors.get()).booleanValue();
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static Boolean getAllThreadsPreferEventExtractors()
/*  47:    */   {
/*  48: 88 */     return allPreferEventExtractors;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public static void setThreadPrefersEventExtractors(boolean preferEventExtractors)
/*  52:    */   {
/*  53: 96 */     threadPreferEventExtractors.set(Boolean.valueOf(preferEventExtractors));
/*  54:    */   }
/*  55:    */   
/*  56:    */   public static void setAllThreadsPreferEventExtractors(Boolean preferEventExtractors)
/*  57:    */   {
/*  58:104 */     allPreferEventExtractors = preferEventExtractors;
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected static boolean getPreferEventExtractor()
/*  62:    */   {
/*  63:112 */     if (allPreferEventExtractors != null) {
/*  64:113 */       return allPreferEventExtractors.booleanValue();
/*  65:    */     }
/*  66:115 */     return ((Boolean)threadPreferEventExtractors.get()).booleanValue();
/*  67:    */   }
/*  68:    */   
/*  69:    */   public static POIOLE2TextExtractor createExtractor(POIFSFileSystem fs)
/*  70:    */     throws IOException
/*  71:    */   {
/*  72:120 */     return (POIOLE2TextExtractor)createExtractor(fs.getRoot());
/*  73:    */   }
/*  74:    */   
/*  75:    */   public static POIOLE2TextExtractor createExtractor(NPOIFSFileSystem fs)
/*  76:    */     throws IOException
/*  77:    */   {
/*  78:124 */     return (POIOLE2TextExtractor)createExtractor(fs.getRoot());
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static POIOLE2TextExtractor createExtractor(OPOIFSFileSystem fs)
/*  82:    */     throws IOException
/*  83:    */   {
/*  84:128 */     return (POIOLE2TextExtractor)createExtractor(fs.getRoot());
/*  85:    */   }
/*  86:    */   
/*  87:    */   public static POITextExtractor createExtractor(InputStream input)
/*  88:    */     throws IOException
/*  89:    */   {
/*  90:132 */     Class<?> cls = getOOXMLClass();
/*  91:133 */     if (cls != null) {
/*  92:    */       try
/*  93:    */       {
/*  94:136 */         Method m = cls.getDeclaredMethod("createExtractor", new Class[] { InputStream.class });
/*  95:137 */         return (POITextExtractor)m.invoke(null, new Object[] { input });
/*  96:    */       }
/*  97:    */       catch (IllegalArgumentException iae)
/*  98:    */       {
/*  99:139 */         throw iae;
/* 100:    */       }
/* 101:    */       catch (Exception e)
/* 102:    */       {
/* 103:141 */         throw new IllegalArgumentException("Error creating Extractor for InputStream", e);
/* 104:    */       }
/* 105:    */     }
/* 106:145 */     return createExtractor(new NPOIFSFileSystem(input));
/* 107:    */   }
/* 108:    */   
/* 109:    */   private static Class<?> getOOXMLClass()
/* 110:    */   {
/* 111:    */     try
/* 112:    */     {
/* 113:151 */       return OLE2ExtractorFactory.class.getClassLoader().loadClass("org.apache.poi.extractor.ExtractorFactory");
/* 114:    */     }
/* 115:    */     catch (ClassNotFoundException e)
/* 116:    */     {
/* 117:155 */       LOGGER.log(5, new Object[] { "POI OOXML jar missing" });
/* 118:    */     }
/* 119:156 */     return null;
/* 120:    */   }
/* 121:    */   
/* 122:    */   private static Class<?> getScratchpadClass()
/* 123:    */   {
/* 124:    */     try
/* 125:    */     {
/* 126:161 */       return OLE2ExtractorFactory.class.getClassLoader().loadClass("org.apache.poi.extractor.OLE2ScratchpadExtractorFactory");
/* 127:    */     }
/* 128:    */     catch (ClassNotFoundException e)
/* 129:    */     {
/* 130:165 */       LOGGER.log(7, new Object[] { "POI Scratchpad jar missing" });
/* 131:166 */       throw new IllegalStateException("POI Scratchpad jar missing, required for ExtractorFactory");
/* 132:    */     }
/* 133:    */   }
/* 134:    */   
/* 135:    */   public static POITextExtractor createExtractor(DirectoryNode poifsDir)
/* 136:    */     throws IOException
/* 137:    */   {
/* 138:178 */     for (String workbookName : InternalWorkbook.WORKBOOK_DIR_ENTRY_NAMES) {
/* 139:179 */       if (poifsDir.hasEntry(workbookName))
/* 140:    */       {
/* 141:180 */         if (getPreferEventExtractor()) {
/* 142:181 */           return new EventBasedExcelExtractor(poifsDir);
/* 143:    */         }
/* 144:183 */         return new ExcelExtractor(poifsDir);
/* 145:    */       }
/* 146:    */     }
/* 147:186 */     if (poifsDir.hasEntry("Book")) {
/* 148:187 */       throw new OldExcelFormatException("Old Excel Spreadsheet format (1-95) found. Please call OldExcelExtractor directly for basic text extraction");
/* 149:    */     }
/* 150:192 */     Class<?> cls = getScratchpadClass();
/* 151:    */     try
/* 152:    */     {
/* 153:194 */       Method m = cls.getDeclaredMethod("createExtractor", new Class[] { DirectoryNode.class });
/* 154:195 */       POITextExtractor ext = (POITextExtractor)m.invoke(null, new Object[] { poifsDir });
/* 155:196 */       if (ext != null) {
/* 156:196 */         return ext;
/* 157:    */       }
/* 158:    */     }
/* 159:    */     catch (IllegalArgumentException iae)
/* 160:    */     {
/* 161:198 */       throw iae;
/* 162:    */     }
/* 163:    */     catch (Exception e)
/* 164:    */     {
/* 165:200 */       throw new IllegalArgumentException("Error creating Scratchpad Extractor", e);
/* 166:    */     }
/* 167:203 */     throw new IllegalArgumentException("No supported documents found in the OLE2 stream");
/* 168:    */   }
/* 169:    */   
/* 170:    */   public static POITextExtractor[] getEmbededDocsTextExtractors(POIOLE2TextExtractor ext)
/* 171:    */     throws IOException
/* 172:    */   {
/* 173:217 */     List<Entry> dirs = new ArrayList();
/* 174:    */     
/* 175:219 */     List<InputStream> nonPOIFS = new ArrayList();
/* 176:    */     
/* 177:    */ 
/* 178:222 */     DirectoryEntry root = ext.getRoot();
/* 179:223 */     if (root == null) {
/* 180:224 */       throw new IllegalStateException("The extractor didn't know which POIFS it came from!");
/* 181:    */     }
/* 182:227 */     if ((ext instanceof ExcelExtractor))
/* 183:    */     {
/* 184:229 */       Iterator<Entry> it = root.getEntries();
/* 185:230 */       while (it.hasNext())
/* 186:    */       {
/* 187:231 */         Entry entry = (Entry)it.next();
/* 188:232 */         if (entry.getName().startsWith("MBD")) {
/* 189:233 */           dirs.add(entry);
/* 190:    */         }
/* 191:    */       }
/* 192:    */     }
/* 193:    */     else
/* 194:    */     {
/* 195:238 */       Class<?> cls = getScratchpadClass();
/* 196:    */       try
/* 197:    */       {
/* 198:240 */         Method m = cls.getDeclaredMethod("identifyEmbeddedResources", new Class[] { POIOLE2TextExtractor.class, List.class, List.class });
/* 199:    */         
/* 200:242 */         m.invoke(null, new Object[] { ext, dirs, nonPOIFS });
/* 201:    */       }
/* 202:    */       catch (Exception e)
/* 203:    */       {
/* 204:244 */         throw new IllegalArgumentException("Error checking for Scratchpad embedded resources", e);
/* 205:    */       }
/* 206:    */     }
/* 207:249 */     if ((dirs.size() == 0) && (nonPOIFS.size() == 0)) {
/* 208:250 */       return new POITextExtractor[0];
/* 209:    */     }
/* 210:253 */     ArrayList<POITextExtractor> e = new ArrayList();
/* 211:254 */     for (Entry dir : dirs) {
/* 212:255 */       e.add(createExtractor((DirectoryNode)dir));
/* 213:    */     }
/* 214:259 */     for (InputStream nonPOIF : nonPOIFS) {
/* 215:    */       try
/* 216:    */       {
/* 217:261 */         e.add(createExtractor(nonPOIF));
/* 218:    */       }
/* 219:    */       catch (IllegalArgumentException ie)
/* 220:    */       {
/* 221:265 */         LOGGER.log(5, new Object[] { ie });
/* 222:    */       }
/* 223:    */       catch (Exception xe)
/* 224:    */       {
/* 225:268 */         LOGGER.log(5, new Object[] { xe });
/* 226:    */       }
/* 227:    */     }
/* 228:271 */     return (POITextExtractor[])e.toArray(new POITextExtractor[e.size()]);
/* 229:    */   }
/* 230:    */   
/* 231:    */   private static POITextExtractor createEncyptedOOXMLExtractor(DirectoryNode poifsDir)
/* 232:    */     throws IOException
/* 233:    */   {
/* 234:276 */     String pass = Biff8EncryptionKey.getCurrentUserPassword();
/* 235:277 */     if (pass == null) {
/* 236:278 */       pass = "VelvetSweatshop";
/* 237:    */     }
/* 238:281 */     EncryptionInfo ei = new EncryptionInfo(poifsDir);
/* 239:282 */     Decryptor dec = ei.getDecryptor();
/* 240:283 */     InputStream is = null;
/* 241:    */     try
/* 242:    */     {
/* 243:285 */       if (!dec.verifyPassword(pass)) {
/* 244:286 */         throw new EncryptedDocumentException("Invalid password specified - use Biff8EncryptionKey.setCurrentUserPassword() before calling extractor");
/* 245:    */       }
/* 246:288 */       is = dec.getDataStream(poifsDir);
/* 247:289 */       return createExtractor(is);
/* 248:    */     }
/* 249:    */     catch (IOException e)
/* 250:    */     {
/* 251:291 */       throw e;
/* 252:    */     }
/* 253:    */     catch (Exception e)
/* 254:    */     {
/* 255:293 */       throw new IOException(e);
/* 256:    */     }
/* 257:    */     finally
/* 258:    */     {
/* 259:295 */       IOUtils.closeQuietly(is);
/* 260:    */     }
/* 261:    */   }
/* 262:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.extractor.OLE2ExtractorFactory
 * JD-Core Version:    0.7.0.1
 */