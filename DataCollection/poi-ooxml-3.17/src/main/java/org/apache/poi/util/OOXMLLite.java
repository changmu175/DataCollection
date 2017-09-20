/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileOutputStream;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.io.OutputStream;
/*   8:    */ import java.io.PrintStream;
/*   9:    */ import java.lang.reflect.Field;
/*  10:    */ import java.lang.reflect.Method;
/*  11:    */ import java.net.URL;
/*  12:    */ import java.security.AccessController;
/*  13:    */ import java.security.CodeSource;
/*  14:    */ import java.security.PrivilegedAction;
/*  15:    */ import java.security.ProtectionDomain;
/*  16:    */ import java.util.ArrayList;
/*  17:    */ import java.util.Enumeration;
/*  18:    */ import java.util.HashMap;
/*  19:    */ import java.util.List;
/*  20:    */ import java.util.Map;
/*  21:    */ import java.util.Vector;
/*  22:    */ import java.util.jar.JarEntry;
/*  23:    */ import java.util.jar.JarFile;
/*  24:    */ import java.util.regex.Matcher;
/*  25:    */ import java.util.regex.Pattern;
/*  26:    */ import junit.framework.TestCase;
/*  27:    */ import org.junit.Test;
/*  28:    */ import org.junit.internal.TextListener;
/*  29:    */ import org.junit.runner.JUnitCore;
/*  30:    */ import org.junit.runner.Result;
/*  31:    */ 
/*  32:    */ public final class OOXMLLite
/*  33:    */ {
/*  34:    */   private File _destDest;
/*  35:    */   private File _testDir;
/*  36:    */   private File _ooxmlJar;
/*  37:    */   
/*  38:    */   OOXMLLite(String dest, String test, String ooxmlJar)
/*  39:    */   {
/*  40: 73 */     this._destDest = new File(dest);
/*  41: 74 */     this._testDir = new File(test);
/*  42: 75 */     this._ooxmlJar = new File(ooxmlJar);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public static void main(String[] args)
/*  46:    */     throws IOException, ClassNotFoundException
/*  47:    */   {
/*  48: 80 */     String dest = null;String test = null;String ooxml = null;
/*  49: 82 */     for (int i = 0; i < args.length; i++) {
/*  50: 83 */       if (args[i].equals("-dest")) {
/*  51: 83 */         dest = args[(++i)];
/*  52: 84 */       } else if (args[i].equals("-test")) {
/*  53: 84 */         test = args[(++i)];
/*  54: 85 */       } else if (args[i].equals("-ooxml")) {
/*  55: 85 */         ooxml = args[(++i)];
/*  56:    */       }
/*  57:    */     }
/*  58: 87 */     OOXMLLite builder = new OOXMLLite(dest, test, ooxml);
/*  59: 88 */     builder.build();
/*  60:    */   }
/*  61:    */   
/*  62:    */   void build()
/*  63:    */     throws IOException, ClassNotFoundException
/*  64:    */   {
/*  65: 92 */     List<Class<?>> lst = new ArrayList();
/*  66:    */     
/*  67: 94 */     String exclude = StringUtil.join("|", new Object[] { "BaseTestXWorkbook", "BaseTestXSheet", "BaseTestXRow", "BaseTestXCell", "BaseTestXSSFPivotTable", "TestSXSSFWorkbook\\$\\d", "TestUnfixedBugs", "MemoryUsage", "TestDataProvider", "TestDataSamples", "All.+Tests", "ZipFileAssert", "AesZipFileZipEntrySource", "TempFileRecordingSXSSFWorkbookWithCustomZipEntrySource", "PkiTestUtils", "TestCellFormatPart\\$\\d", "TestSignatureInfo\\$\\d", "TestCertificateEncryption\\$CertData", "TestPOIXMLDocument\\$OPCParser", "TestPOIXMLDocument\\$TestFactory", "TestXSLFTextParagraph\\$DrawTextParagraphProxy", "TestXSSFExportToXML\\$\\d", "TestXSSFExportToXML\\$DummyEntityResolver", "TestFormulaEvaluatorOnXSSF\\$Result", "TestFormulaEvaluatorOnXSSF\\$SS", "TestMultiSheetFormulaEvaluatorOnXSSF\\$Result", "TestMultiSheetFormulaEvaluatorOnXSSF\\$SS", "TestXSSFBugs\\$\\d", "AddImageBench", "AddImageBench_jmhType_B\\d", "AddImageBench_benchCreatePicture_jmhTest", "TestEvilUnclosedBRFixingInputStream\\$EvilUnclosedBRFixingInputStream", "TempFileRecordingSXSSFWorkbookWithCustomZipEntrySource\\$TempFileRecordingSheetDataWriterWithDecorator", "TestXSSFBReader\\$1", "TestXSSFBReader\\$TestSheetHandler", "TestFormulaEvaluatorOnXSSF\\$1", "TestMultiSheetFormulaEvaluatorOnXSSF\\$1", "TestZipPackagePropertiesMarshaller\\$1", "SLCommonUtils", "TestPPTX2PNG\\$1" });
/*  68:    */     
/*  69:    */ 
/*  70:    */ 
/*  71:    */ 
/*  72:    */ 
/*  73:    */ 
/*  74:    */ 
/*  75:    */ 
/*  76:    */ 
/*  77:    */ 
/*  78:    */ 
/*  79:    */ 
/*  80:    */ 
/*  81:    */ 
/*  82:    */ 
/*  83:    */ 
/*  84:    */ 
/*  85:    */ 
/*  86:    */ 
/*  87:    */ 
/*  88:    */ 
/*  89:    */ 
/*  90:    */ 
/*  91:    */ 
/*  92:    */ 
/*  93:    */ 
/*  94:    */ 
/*  95:    */ 
/*  96:    */ 
/*  97:    */ 
/*  98:    */ 
/*  99:    */ 
/* 100:    */ 
/* 101:    */ 
/* 102:    */ 
/* 103:    */ 
/* 104:    */ 
/* 105:    */ 
/* 106:    */ 
/* 107:    */ 
/* 108:    */ 
/* 109:136 */     System.out.println("Collecting unit tests from " + this._testDir);
/* 110:137 */     collectTests(this._testDir, this._testDir, lst, ".+.class$", ".+(" + exclude + ").class");
/* 111:138 */     System.out.println("Found " + lst.size() + " classes");
/* 112:    */     
/* 113:    */ 
/* 114:141 */     JUnitCore jUnitCore = new JUnitCore();
/* 115:142 */     jUnitCore.addListener(new TextListener(System.out));
/* 116:143 */     Result result = jUnitCore.run((Class[])lst.toArray(new Class[lst.size()]));
/* 117:144 */     if (!result.wasSuccessful()) {
/* 118:145 */       throw new RuntimeException("Tests did not succeed, cannot build ooxml-lite jar");
/* 119:    */     }
/* 120:149 */     System.out.println("Copying classes to " + this._destDest);
/* 121:150 */     Map<String, Class<?>> classes = getLoadedClasses(this._ooxmlJar.getName());
/* 122:151 */     for (Class<?> cls : classes.values())
/* 123:    */     {
/* 124:152 */       String className = cls.getName();
/* 125:153 */       String classRef = className.replace('.', '/') + ".class";
/* 126:154 */       File destFile = new File(this._destDest, classRef);
/* 127:155 */       copyFile(cls.getResourceAsStream('/' + classRef), destFile);
/* 128:157 */       if (cls.isInterface()) {
/* 129:159 */         for (Class<?> fc : cls.getDeclaredClasses())
/* 130:    */         {
/* 131:160 */           className = fc.getName();
/* 132:161 */           classRef = className.replace('.', '/') + ".class";
/* 133:162 */           destFile = new File(this._destDest, classRef);
/* 134:163 */           copyFile(fc.getResourceAsStream('/' + classRef), destFile);
/* 135:    */         }
/* 136:    */       }
/* 137:    */     }
/* 138:169 */     System.out.println("Copying .xsb resources");
/* 139:170 */     JarFile jar = new JarFile(this._ooxmlJar);
/* 140:171 */     Pattern p = Pattern.compile("schemaorg_apache_xmlbeans/(system|element)/.*\\.xsb");
/* 141:    */     try
/* 142:    */     {
/* 143:173 */       for (e = jar.entries(); e.hasMoreElements();)
/* 144:    */       {
/* 145:174 */         JarEntry je = (JarEntry)e.nextElement();
/* 146:175 */         if (p.matcher(je.getName()).matches())
/* 147:    */         {
/* 148:176 */           File destFile = new File(this._destDest, je.getName());
/* 149:177 */           copyFile(jar.getInputStream(je), destFile);
/* 150:    */         }
/* 151:    */       }
/* 152:    */     }
/* 153:    */     finally
/* 154:    */     {
/* 155:    */       Enumeration<JarEntry> e;
/* 156:181 */       jar.close();
/* 157:    */     }
/* 158:    */   }
/* 159:    */   
/* 160:    */   private static boolean checkForTestAnnotation(Class<?> testclass)
/* 161:    */   {
/* 162:186 */     for (Method m : testclass.getDeclaredMethods()) {
/* 163:187 */       if (m.isAnnotationPresent(Test.class)) {
/* 164:188 */         return true;
/* 165:    */       }
/* 166:    */     }
/* 167:193 */     if (testclass.getSuperclass() != null) {
/* 168:194 */       for (Method m : testclass.getSuperclass().getDeclaredMethods()) {
/* 169:195 */         if (m.isAnnotationPresent(Test.class)) {
/* 170:196 */           return true;
/* 171:    */         }
/* 172:    */       }
/* 173:    */     }
/* 174:201 */     System.out.println("Class " + testclass.getName() + " does not derive from TestCase and does not have a @Test annotation");
/* 175:    */     
/* 176:    */ 
/* 177:    */ 
/* 178:    */ 
/* 179:    */ 
/* 180:207 */     return false;
/* 181:    */   }
/* 182:    */   
/* 183:    */   private static void collectTests(File root, File arg, List<Class<?>> out, String ptrn, String exclude)
/* 184:    */     throws ClassNotFoundException
/* 185:    */   {
/* 186:219 */     if (arg.isDirectory())
/* 187:    */     {
/* 188:220 */       File[] files = arg.listFiles();
/* 189:221 */       if (files != null) {
/* 190:222 */         for (File f : files) {
/* 191:223 */           collectTests(root, f, out, ptrn, exclude);
/* 192:    */         }
/* 193:    */       }
/* 194:    */     }
/* 195:    */     else
/* 196:    */     {
/* 197:227 */       String path = arg.getAbsolutePath();
/* 198:228 */       String prefix = root.getAbsolutePath();
/* 199:229 */       String cls = path.substring(prefix.length() + 1).replace(File.separator, ".");
/* 200:230 */       if (!cls.matches(ptrn)) {
/* 201:230 */         return;
/* 202:    */       }
/* 203:231 */       if (cls.matches(exclude)) {
/* 204:231 */         return;
/* 205:    */       }
/* 206:233 */       if (cls.indexOf('$') != -1)
/* 207:    */       {
/* 208:234 */         System.out.println("Inner class " + cls + " not included");
/* 209:235 */         return;
/* 210:    */       }
/* 211:238 */       cls = cls.replace(".class", "");
/* 212:    */       try
/* 213:    */       {
/* 214:241 */         Class<?> testclass = Class.forName(cls);
/* 215:242 */         if ((TestCase.class.isAssignableFrom(testclass)) || (checkForTestAnnotation(testclass))) {
/* 216:244 */           out.add(testclass);
/* 217:    */         }
/* 218:    */       }
/* 219:    */       catch (Throwable e)
/* 220:    */       {
/* 221:247 */         System.out.println("Class " + cls + " is not in classpath");
/* 222:    */       }
/* 223:    */     }
/* 224:    */   }
/* 225:    */   
/* 226:    */   private static Map<String, Class<?>> getLoadedClasses(String ptrn)
/* 227:    */   {
/* 228:262 */     Field _classes = (Field)AccessController.doPrivileged(new PrivilegedAction()
/* 229:    */     {
/* 230:    */       @SuppressForbidden("TODO: Reflection works until Java 8 on Oracle/Sun JDKs, but breaks afterwards (different classloader types, access checks)")
/* 231:    */       public Field run()
/* 232:    */       {
/* 233:    */         try
/* 234:    */         {
/* 235:266 */           Field fld = ClassLoader.class.getDeclaredField("classes");
/* 236:267 */           fld.setAccessible(true);
/* 237:268 */           return fld;
/* 238:    */         }
/* 239:    */         catch (Exception e)
/* 240:    */         {
/* 241:270 */           throw new RuntimeException(e);
/* 242:    */         }
/* 243:    */       }
/* 244:275 */     });
/* 245:276 */     ClassLoader appLoader = ClassLoader.getSystemClassLoader();
/* 246:    */     try
/* 247:    */     {
/* 248:278 */       Vector<Class<?>> classes = (Vector)_classes.get(appLoader);
/* 249:279 */       Map<String, Class<?>> map = new HashMap();
/* 250:280 */       for (Class<?> cls : classes)
/* 251:    */       {
/* 252:282 */         ProtectionDomain pd = cls.getProtectionDomain();
/* 253:283 */         if (pd != null)
/* 254:    */         {
/* 255:284 */           CodeSource cs = pd.getCodeSource();
/* 256:285 */           if (cs != null)
/* 257:    */           {
/* 258:286 */             URL loc = cs.getLocation();
/* 259:287 */             if (loc != null)
/* 260:    */             {
/* 261:289 */               String jar = loc.toString();
/* 262:290 */               if (jar.contains(ptrn)) {
/* 263:291 */                 map.put(cls.getName(), cls);
/* 264:    */               }
/* 265:    */             }
/* 266:    */           }
/* 267:    */         }
/* 268:    */       }
/* 269:294 */       return map;
/* 270:    */     }
/* 271:    */     catch (IllegalAccessException e)
/* 272:    */     {
/* 273:296 */       throw new RuntimeException(e);
/* 274:    */     }
/* 275:    */   }
/* 276:    */   
/* 277:    */   private static void copyFile(InputStream srcStream, File destFile)
/* 278:    */     throws IOException
/* 279:    */   {
/* 280:301 */     File destDirectory = destFile.getParentFile();
/* 281:302 */     if ((!destDirectory.exists()) && (!destDirectory.mkdirs())) {
/* 282:303 */       throw new RuntimeException("Can't create destination directory: " + destDirectory);
/* 283:    */     }
/* 284:305 */     OutputStream destStream = new FileOutputStream(destFile);
/* 285:    */     try
/* 286:    */     {
/* 287:307 */       IOUtils.copy(srcStream, destStream);
/* 288:    */     }
/* 289:    */     finally
/* 290:    */     {
/* 291:309 */       destStream.close();
/* 292:    */     }
/* 293:    */   }
/* 294:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.util.OOXMLLite
 * JD-Core Version:    0.7.0.1
 */