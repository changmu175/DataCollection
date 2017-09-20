/*   1:    */ package org.apache.poi.poifs.macros;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.ByteArrayOutputStream;
/*   5:    */ import java.io.Closeable;
/*   6:    */ import java.io.File;
/*   7:    */ import java.io.FileInputStream;
/*   8:    */ import java.io.IOException;
/*   9:    */ import java.io.InputStream;
/*  10:    */ import java.nio.charset.Charset;
/*  11:    */ import java.util.HashMap;
/*  12:    */ import java.util.Map;
/*  13:    */ import java.util.Map.Entry;
/*  14:    */ import java.util.zip.ZipEntry;
/*  15:    */ import java.util.zip.ZipInputStream;
/*  16:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  17:    */ import org.apache.poi.poifs.filesystem.DocumentInputStream;
/*  18:    */ import org.apache.poi.poifs.filesystem.DocumentNode;
/*  19:    */ import org.apache.poi.poifs.filesystem.Entry;
/*  20:    */ import org.apache.poi.poifs.filesystem.FileMagic;
/*  21:    */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/*  22:    */ import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
/*  23:    */ import org.apache.poi.util.CodePageUtil;
/*  24:    */ import org.apache.poi.util.HexDump;
/*  25:    */ import org.apache.poi.util.IOUtils;
/*  26:    */ import org.apache.poi.util.RLEDecompressingInputStream;
/*  27:    */ import org.apache.poi.util.StringUtil;
/*  28:    */ 
/*  29:    */ public class VBAMacroReader
/*  30:    */   implements Closeable
/*  31:    */ {
/*  32:    */   protected static final String VBA_PROJECT_OOXML = "vbaProject.bin";
/*  33:    */   protected static final String VBA_PROJECT_POIFS = "VBA";
/*  34: 65 */   private static final Charset UTF_16LE = Charset.forName("UTF-16LE");
/*  35:    */   private NPOIFSFileSystem fs;
/*  36:    */   private static final int EOF = -1;
/*  37:    */   private static final int VERSION_INDEPENDENT_TERMINATOR = 16;
/*  38:    */   private static final int VERSION_DEPENDENT_TERMINATOR = 43;
/*  39:    */   private static final int PROJECTVERSION = 9;
/*  40:    */   private static final int PROJECTCODEPAGE = 3;
/*  41:    */   private static final int STREAMNAME = 26;
/*  42:    */   private static final int MODULEOFFSET = 49;
/*  43:    */   private static final int MODULETYPE_PROCEDURAL = 33;
/*  44:    */   private static final int MODULETYPE_DOCUMENT_CLASS_OR_DESIGNER = 34;
/*  45:    */   private static final int PROJECTLCID = 2;
/*  46:    */   private static final int MODULE_NAME = 25;
/*  47:    */   private static final int MODULE_NAME_UNICODE = 71;
/*  48:    */   private static final int MODULE_DOC_STRING = 28;
/*  49:    */   private static final int STREAMNAME_RESERVED = 50;
/*  50:    */   
/*  51:    */   public VBAMacroReader(InputStream rstream)
/*  52:    */     throws IOException
/*  53:    */   {
/*  54: 70 */     InputStream is = FileMagic.prepareToCheckMagic(rstream);
/*  55: 71 */     FileMagic fm = FileMagic.valueOf(is);
/*  56: 72 */     if (fm == FileMagic.OLE2) {
/*  57: 73 */       this.fs = new NPOIFSFileSystem(is);
/*  58:    */     } else {
/*  59: 75 */       openOOXML(is);
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   public VBAMacroReader(File file)
/*  64:    */     throws IOException
/*  65:    */   {
/*  66:    */     try
/*  67:    */     {
/*  68: 81 */       this.fs = new NPOIFSFileSystem(file);
/*  69:    */     }
/*  70:    */     catch (OfficeXmlFileException e)
/*  71:    */     {
/*  72: 83 */       openOOXML(new FileInputStream(file));
/*  73:    */     }
/*  74:    */   }
/*  75:    */   
/*  76:    */   public VBAMacroReader(NPOIFSFileSystem fs)
/*  77:    */   {
/*  78: 87 */     this.fs = fs;
/*  79:    */   }
/*  80:    */   
/*  81:    */   private void openOOXML(InputStream zipFile)
/*  82:    */     throws IOException
/*  83:    */   {
/*  84: 91 */     ZipInputStream zis = new ZipInputStream(zipFile);
/*  85:    */     ZipEntry zipEntry;
/*  86: 93 */     while ((zipEntry = zis.getNextEntry()) != null) {
/*  87: 94 */       if (StringUtil.endsWithIgnoreCase(zipEntry.getName(), "vbaProject.bin")) {
/*  88:    */         try
/*  89:    */         {
/*  90: 97 */           this.fs = new NPOIFSFileSystem(zis);
/*  91: 98 */           return;
/*  92:    */         }
/*  93:    */         catch (IOException e)
/*  94:    */         {
/*  95:101 */           zis.close();
/*  96:    */           
/*  97:    */ 
/*  98:104 */           throw e;
/*  99:    */         }
/* 100:    */       }
/* 101:    */     }
/* 102:108 */     zis.close();
/* 103:109 */     throw new IllegalArgumentException("No VBA project found");
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void close()
/* 107:    */     throws IOException
/* 108:    */   {
/* 109:113 */     this.fs.close();
/* 110:114 */     this.fs = null;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public Map<String, String> readMacros()
/* 114:    */     throws IOException
/* 115:    */   {
/* 116:124 */     ModuleMap modules = new ModuleMap();
/* 117:125 */     findMacros(this.fs.getRoot(), modules);
/* 118:    */     
/* 119:127 */     Map<String, String> moduleSources = new HashMap();
/* 120:128 */     for (Entry<String, Module> entry : modules.entrySet())
/* 121:    */     {
/* 122:129 */       Module module = (Module)entry.getValue();
/* 123:130 */       if ((module.buf != null) && (module.buf.length > 0)) {
/* 124:131 */         moduleSources.put(entry.getKey(), new String(module.buf, modules.charset));
/* 125:    */       }
/* 126:    */     }
/* 127:134 */     return moduleSources;
/* 128:    */   }
/* 129:    */   
/* 130:    */   protected static class Module
/* 131:    */   {
/* 132:    */     Integer offset;
/* 133:    */     byte[] buf;
/* 134:    */     
/* 135:    */     void read(InputStream in)
/* 136:    */       throws IOException
/* 137:    */     {
/* 138:141 */       ByteArrayOutputStream out = new ByteArrayOutputStream();
/* 139:142 */       IOUtils.copy(in, out);
/* 140:143 */       out.close();
/* 141:144 */       this.buf = out.toByteArray();
/* 142:    */     }
/* 143:    */   }
/* 144:    */   
/* 145:    */   protected static class ModuleMap
/* 146:    */     extends HashMap<String, Module>
/* 147:    */   {
/* 148:148 */     Charset charset = Charset.forName("Cp1252");
/* 149:    */   }
/* 150:    */   
/* 151:    */   protected void findMacros(DirectoryNode dir, ModuleMap modules)
/* 152:    */     throws IOException
/* 153:    */   {
/* 154:162 */     if ("VBA".equalsIgnoreCase(dir.getName())) {
/* 155:164 */       readMacros(dir, modules);
/* 156:    */     } else {
/* 157:167 */       for (Entry child : dir) {
/* 158:168 */         if ((child instanceof DirectoryNode)) {
/* 159:169 */           findMacros((DirectoryNode)child, modules);
/* 160:    */         }
/* 161:    */       }
/* 162:    */     }
/* 163:    */   }
/* 164:    */   
/* 165:    */   private static String readString(InputStream stream, int length, Charset charset)
/* 166:    */     throws IOException
/* 167:    */   {
/* 168:185 */     byte[] buffer = new byte[length];
/* 169:186 */     int count = stream.read(buffer);
/* 170:187 */     return new String(buffer, 0, count, charset);
/* 171:    */   }
/* 172:    */   
/* 173:    */   private static void readModule(RLEDecompressingInputStream in, String streamName, ModuleMap modules)
/* 174:    */     throws IOException
/* 175:    */   {
/* 176:203 */     int moduleOffset = in.readInt();
/* 177:204 */     Module module = (Module)modules.get(streamName);
/* 178:205 */     if (module == null)
/* 179:    */     {
/* 180:207 */       module = new Module();
/* 181:208 */       module.offset = Integer.valueOf(moduleOffset);
/* 182:209 */       modules.put(streamName, module);
/* 183:    */     }
/* 184:    */     else
/* 185:    */     {
/* 186:213 */       InputStream stream = new RLEDecompressingInputStream(new ByteArrayInputStream(module.buf, moduleOffset, module.buf.length - moduleOffset));
/* 187:    */       
/* 188:    */ 
/* 189:216 */       module.read(stream);
/* 190:217 */       stream.close();
/* 191:    */     }
/* 192:    */   }
/* 193:    */   
/* 194:    */   private static void readModule(DocumentInputStream dis, String name, ModuleMap modules)
/* 195:    */     throws IOException
/* 196:    */   {
/* 197:222 */     Module module = (Module)modules.get(name);
/* 198:224 */     if (module == null)
/* 199:    */     {
/* 200:226 */       module = new Module();
/* 201:227 */       modules.put(name, module);
/* 202:228 */       module.read(dis);
/* 203:    */     }
/* 204:229 */     else if (module.buf == null)
/* 205:    */     {
/* 206:230 */       if (module.offset == null) {
/* 207:232 */         throw new IOException("Module offset for '" + name + "' was never read.");
/* 208:    */       }
/* 209:235 */       long skippedBytes = dis.skip(module.offset.intValue());
/* 210:236 */       if (skippedBytes != module.offset.intValue()) {
/* 211:237 */         throw new IOException("tried to skip " + module.offset + " bytes, but actually skipped " + skippedBytes + " bytes");
/* 212:    */       }
/* 213:239 */       InputStream stream = new RLEDecompressingInputStream(dis);
/* 214:240 */       module.read(stream);
/* 215:241 */       stream.close();
/* 216:    */     }
/* 217:    */   }
/* 218:    */   
/* 219:    */   private static void trySkip(InputStream in, long n)
/* 220:    */     throws IOException
/* 221:    */   {
/* 222:252 */     long skippedBytes = in.skip(n);
/* 223:253 */     if (skippedBytes != n)
/* 224:    */     {
/* 225:254 */       if (skippedBytes < 0L) {
/* 226:255 */         throw new IOException("Tried skipping " + n + " bytes, but no bytes were skipped. " + "The end of the stream has been reached or the stream is closed.");
/* 227:    */       }
/* 228:259 */       throw new IOException("Tried skipping " + n + " bytes, but only " + skippedBytes + " bytes were skipped. " + "This should never happen.");
/* 229:    */     }
/* 230:    */   }
/* 231:    */   
/* 232:    */   protected void readMacros(DirectoryNode macroDir, ModuleMap modules)
/* 233:    */     throws IOException
/* 234:    */   {
/* 235:296 */     for (Entry entry : macroDir) {
/* 236:297 */       if ((entry instanceof DocumentNode))
/* 237:    */       {
/* 238:299 */         String name = entry.getName();
/* 239:300 */         DocumentNode document = (DocumentNode)entry;
/* 240:301 */         DocumentInputStream dis = new DocumentInputStream(document);
/* 241:    */         try
/* 242:    */         {
/* 243:303 */           if ("dir".equalsIgnoreCase(name))
/* 244:    */           {
/* 245:305 */             RLEDecompressingInputStream in = new RLEDecompressingInputStream(dis);
/* 246:306 */             String streamName = null;
/* 247:307 */             int recordId = 0;
/* 248:    */             try
/* 249:    */             {
/* 250:    */               for (;;)
/* 251:    */               {
/* 252:310 */                 recordId = in.readShort();
/* 253:311 */                 if ((-1 == recordId) || (16 == recordId)) {
/* 254:    */                   break;
/* 255:    */                 }
/* 256:315 */                 int recordLength = in.readInt();
/* 257:316 */                 switch (recordId)
/* 258:    */                 {
/* 259:    */                 case 9: 
/* 260:318 */                   trySkip(in, 6L);
/* 261:319 */                   break;
/* 262:    */                 case 3: 
/* 263:321 */                   int codepage = in.readShort();
/* 264:322 */                   modules.charset = Charset.forName(CodePageUtil.codepageToEncoding(codepage, true));
/* 265:323 */                   break;
/* 266:    */                 case 26: 
/* 267:325 */                   streamName = readString(in, recordLength, modules.charset);
/* 268:326 */                   int reserved = in.readShort();
/* 269:327 */                   if (reserved != 50) {
/* 270:328 */                     throw new IOException("Expected x0032 after stream name before Unicode stream name, but found: " + Integer.toHexString(reserved));
/* 271:    */                   }
/* 272:331 */                   int unicodeNameRecordLength = in.readInt();
/* 273:332 */                   readUnicodeString(in, unicodeNameRecordLength);
/* 274:    */                   
/* 275:334 */                   break;
/* 276:    */                 case 49: 
/* 277:336 */                   readModule(in, streamName, modules);
/* 278:337 */                   break;
/* 279:    */                 default: 
/* 280:339 */                   trySkip(in, recordLength);
/* 281:    */                 }
/* 282:    */               }
/* 283:    */             }
/* 284:    */             catch (IOException e)
/* 285:    */             {
/* 286:344 */               throw new IOException("Error occurred while reading macros at section id " + recordId + " (" + HexDump.shortToHex(recordId) + ")", e);
/* 287:    */             }
/* 288:    */             finally {}
/* 289:    */           }
/* 290:351 */           else if ((!StringUtil.startsWithIgnoreCase(name, "__SRP")) && (!StringUtil.startsWithIgnoreCase(name, "_VBA_PROJECT")))
/* 291:    */           {
/* 292:354 */             readModule(dis, name, modules);
/* 293:    */           }
/* 294:    */         }
/* 295:    */         finally
/* 296:    */         {
/* 297:358 */           dis.close();
/* 298:    */         }
/* 299:    */       }
/* 300:    */     }
/* 301:    */   }
/* 302:    */   
/* 303:    */   private String readUnicodeString(RLEDecompressingInputStream in, int unicodeNameRecordLength)
/* 304:    */     throws IOException
/* 305:    */   {
/* 306:364 */     byte[] buffer = new byte[unicodeNameRecordLength];
/* 307:365 */     IOUtils.readFully(in, buffer);
/* 308:366 */     return new String(buffer, UTF_16LE);
/* 309:    */   }
/* 310:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.poifs.macros.VBAMacroReader

 * JD-Core Version:    0.7.0.1

 */