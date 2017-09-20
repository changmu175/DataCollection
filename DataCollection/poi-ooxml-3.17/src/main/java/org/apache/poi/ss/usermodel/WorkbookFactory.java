/*   1:    */ package org.apache.poi.ss.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import org.apache.poi.EncryptedDocumentException;
/*   7:    */ import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
/*   8:    */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*   9:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  10:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  11:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  12:    */ import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
/*  13:    */ import org.apache.poi.poifs.filesystem.FileMagic;
/*  14:    */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/*  15:    */ import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/*  16:    */ import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/*  17:    */ 
/*  18:    */ public class WorkbookFactory
/*  19:    */ {
/*  20:    */   public static Workbook create(POIFSFileSystem fs)
/*  21:    */     throws IOException
/*  22:    */   {
/*  23: 54 */     return new HSSFWorkbook(fs);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public static Workbook create(NPOIFSFileSystem fs)
/*  27:    */     throws IOException
/*  28:    */   {
/*  29:    */     try
/*  30:    */     {
/*  31: 64 */       return create(fs, null);
/*  32:    */     }
/*  33:    */     catch (InvalidFormatException e)
/*  34:    */     {
/*  35: 67 */       throw new IOException(e);
/*  36:    */     }
/*  37:    */   }
/*  38:    */   
/*  39:    */   private static Workbook create(NPOIFSFileSystem fs, String password)
/*  40:    */     throws IOException, InvalidFormatException
/*  41:    */   {
/*  42: 84 */     DirectoryNode root = fs.getRoot();
/*  43:    */     OPCPackage pkg;
/*  44: 87 */     if (root.hasEntry("EncryptedPackage"))
/*  45:    */     {
/*  46: 88 */       InputStream stream = DocumentFactoryHelper.getDecryptedStream(fs, password);
/*  47:    */       
/*  48: 90 */       pkg = OPCPackage.open(stream);
/*  49: 91 */       return create(pkg);
/*  50:    */     }
/*  51: 96 */     boolean passwordSet = false;
/*  52: 97 */     if (password != null)
/*  53:    */     {
/*  54: 98 */       Biff8EncryptionKey.setCurrentUserPassword(password);
/*  55: 99 */       passwordSet = true;
/*  56:    */     }
/*  57:    */     try
/*  58:    */     {
/*  59:102 */       return new HSSFWorkbook(root, true);
/*  60:    */     }
/*  61:    */     finally
/*  62:    */     {
/*  63:104 */       if (passwordSet) {
/*  64:105 */         Biff8EncryptionKey.setCurrentUserPassword(null);
/*  65:    */       }
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   public static Workbook create(OPCPackage pkg)
/*  70:    */     throws IOException
/*  71:    */   {
/*  72:123 */     return new XSSFWorkbook(pkg);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public static Workbook create(InputStream inp)
/*  76:    */     throws IOException, InvalidFormatException, EncryptedDocumentException
/*  77:    */   {
/*  78:149 */     return create(inp, null);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static Workbook create(InputStream inp, String password)
/*  82:    */     throws IOException, InvalidFormatException, EncryptedDocumentException
/*  83:    */   {
/*  84:175 */     InputStream is = FileMagic.prepareToCheckMagic(inp);
/*  85:    */     
/*  86:177 */     FileMagic fm = FileMagic.valueOf(is);
/*  87:179 */     switch (1.$SwitchMap$org$apache$poi$poifs$filesystem$FileMagic[fm.ordinal()])
/*  88:    */     {
/*  89:    */     case 1: 
/*  90:181 */       NPOIFSFileSystem fs = new NPOIFSFileSystem(is);
/*  91:182 */       return create(fs, password);
/*  92:    */     case 2: 
/*  93:184 */       return new XSSFWorkbook(OPCPackage.open(is));
/*  94:    */     }
/*  95:186 */     throw new InvalidFormatException("Your InputStream was neither an OLE2 stream, nor an OOXML stream");
/*  96:    */   }
/*  97:    */   
/*  98:    */   public static Workbook create(File file)
/*  99:    */     throws IOException, InvalidFormatException, EncryptedDocumentException
/* 100:    */   {
/* 101:205 */     return create(file, null);
/* 102:    */   }
/* 103:    */   
/* 104:    */   public static Workbook create(File file, String password)
/* 105:    */     throws IOException, InvalidFormatException, EncryptedDocumentException
/* 106:    */   {
/* 107:226 */     return create(file, password, false);
/* 108:    */   }
/* 109:    */   
/* 110:    */   /* Error */
/* 111:    */   public static Workbook create(File file, String password, boolean readOnly)
/* 112:    */     throws IOException, InvalidFormatException, EncryptedDocumentException
/* 113:    */   {
/* 114:    */     // Byte code:
/* 115:    */     //   0: aload_0
/* 116:    */     //   1: invokevirtual 29	java/io/File:exists	()Z
/* 117:    */     //   4: ifne +15 -> 19
/* 118:    */     //   7: new 30	java/io/FileNotFoundException
/* 119:    */     //   10: dup
/* 120:    */     //   11: aload_0
/* 121:    */     //   12: invokevirtual 31	java/io/File:toString	()Ljava/lang/String;
/* 122:    */     //   15: invokespecial 32	java/io/FileNotFoundException:<init>	(Ljava/lang/String;)V
/* 123:    */     //   18: athrow
/* 124:    */     //   19: new 23	org/apache/poi/poifs/filesystem/NPOIFSFileSystem
/* 125:    */     //   22: dup
/* 126:    */     //   23: aload_0
/* 127:    */     //   24: iload_2
/* 128:    */     //   25: invokespecial 33	org/apache/poi/poifs/filesystem/NPOIFSFileSystem:<init>	(Ljava/io/File;Z)V
/* 129:    */     //   28: astore_3
/* 130:    */     //   29: aload_3
/* 131:    */     //   30: aload_1
/* 132:    */     //   31: invokestatic 4	org/apache/poi/ss/usermodel/WorkbookFactory:create	(Lorg/apache/poi/poifs/filesystem/NPOIFSFileSystem;Ljava/lang/String;)Lorg/apache/poi/ss/usermodel/Workbook;
/* 133:    */     //   34: areturn
/* 134:    */     //   35: astore 4
/* 135:    */     //   37: aload_3
/* 136:    */     //   38: invokestatic 35	org/apache/poi/util/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
/* 137:    */     //   41: aload 4
/* 138:    */     //   43: athrow
/* 139:    */     //   44: astore_3
/* 140:    */     //   45: aload_0
/* 141:    */     //   46: iload_2
/* 142:    */     //   47: ifeq +9 -> 56
/* 143:    */     //   50: getstatic 37	org/apache/poi/openxml4j/opc/PackageAccess:READ	Lorg/apache/poi/openxml4j/opc/PackageAccess;
/* 144:    */     //   53: goto +6 -> 59
/* 145:    */     //   56: getstatic 38	org/apache/poi/openxml4j/opc/PackageAccess:READ_WRITE	Lorg/apache/poi/openxml4j/opc/PackageAccess;
/* 146:    */     //   59: invokestatic 39	org/apache/poi/openxml4j/opc/OPCPackage:open	(Ljava/io/File;Lorg/apache/poi/openxml4j/opc/PackageAccess;)Lorg/apache/poi/openxml4j/opc/OPCPackage;
/* 147:    */     //   62: astore 4
/* 148:    */     //   64: new 16	org/apache/poi/xssf/usermodel/XSSFWorkbook
/* 149:    */     //   67: dup
/* 150:    */     //   68: aload 4
/* 151:    */     //   70: invokespecial 17	org/apache/poi/xssf/usermodel/XSSFWorkbook:<init>	(Lorg/apache/poi/openxml4j/opc/OPCPackage;)V
/* 152:    */     //   73: areturn
/* 153:    */     //   74: astore 5
/* 154:    */     //   76: aload 4
/* 155:    */     //   78: invokevirtual 41	org/apache/poi/openxml4j/opc/OPCPackage:revert	()V
/* 156:    */     //   81: aload 5
/* 157:    */     //   83: instanceof 6
/* 158:    */     //   86: ifeq +9 -> 95
/* 159:    */     //   89: aload 5
/* 160:    */     //   91: checkcast 6	java/io/IOException
/* 161:    */     //   94: athrow
/* 162:    */     //   95: aload 5
/* 163:    */     //   97: instanceof 34
/* 164:    */     //   100: ifeq +9 -> 109
/* 165:    */     //   103: aload 5
/* 166:    */     //   105: checkcast 34	java/lang/RuntimeException
/* 167:    */     //   108: athrow
/* 168:    */     //   109: new 6	java/io/IOException
/* 169:    */     //   112: dup
/* 170:    */     //   113: aload 5
/* 171:    */     //   115: invokespecial 7	java/io/IOException:<init>	(Ljava/lang/Throwable;)V
/* 172:    */     //   118: athrow
/* 173:    */     // Line number table:
/* 174:    */     //   Java source line #249	-> byte code offset #0
/* 175:    */     //   Java source line #250	-> byte code offset #7
/* 176:    */     //   Java source line #254	-> byte code offset #19
/* 177:    */     //   Java source line #256	-> byte code offset #29
/* 178:    */     //   Java source line #257	-> byte code offset #35
/* 179:    */     //   Java source line #259	-> byte code offset #37
/* 180:    */     //   Java source line #260	-> byte code offset #41
/* 181:    */     //   Java source line #262	-> byte code offset #44
/* 182:    */     //   Java source line #264	-> byte code offset #45
/* 183:    */     //   Java source line #266	-> byte code offset #64
/* 184:    */     //   Java source line #267	-> byte code offset #74
/* 185:    */     //   Java source line #269	-> byte code offset #76
/* 186:    */     //   Java source line #272	-> byte code offset #81
/* 187:    */     //   Java source line #273	-> byte code offset #89
/* 188:    */     //   Java source line #274	-> byte code offset #95
/* 189:    */     //   Java source line #275	-> byte code offset #103
/* 190:    */     //   Java source line #277	-> byte code offset #109
/* 191:    */     // Local variable table:
/* 192:    */     //   start	length	slot	name	signature
/* 193:    */     //   0	119	0	file	File
/* 194:    */     //   0	119	1	password	String
/* 195:    */     //   0	119	2	readOnly	boolean
/* 196:    */     //   28	10	3	fs	NPOIFSFileSystem
/* 197:    */     //   44	2	3	e	org.apache.poi.poifs.filesystem.OfficeXmlFileException
/* 198:    */     //   35	7	4	e	java.lang.RuntimeException
/* 199:    */     //   62	15	4	pkg	OPCPackage
/* 200:    */     //   74	40	5	ioe	java.lang.Exception
/* 201:    */     // Exception table:
/* 202:    */     //   from	to	target	type
/* 203:    */     //   29	34	35	java/lang/RuntimeException
/* 204:    */     //   19	34	44	org/apache/poi/poifs/filesystem/OfficeXmlFileException
/* 205:    */     //   35	44	44	org/apache/poi/poifs/filesystem/OfficeXmlFileException
/* 206:    */     //   64	73	74	java/lang/Exception
/* 207:    */   }
/* 208:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.WorkbookFactory
 * JD-Core Version:    0.7.0.1
 */