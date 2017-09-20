/*   1:    */ package org.apache.poi.openxml4j.opc.internal;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileInputStream;
/*   5:    */ import java.io.FileNotFoundException;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.InputStream;
/*   8:    */ import java.net.URI;
/*   9:    */ import java.net.URISyntaxException;
/*  10:    */ import java.util.Enumeration;
/*  11:    */ import java.util.zip.ZipEntry;
/*  12:    */ import java.util.zip.ZipFile;
/*  13:    */ import java.util.zip.ZipInputStream;
/*  14:    */ import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
/*  15:    */ import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
/*  16:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  17:    */ import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
/*  18:    */ import org.apache.poi.openxml4j.opc.ZipPackage;
/*  19:    */ import org.apache.poi.openxml4j.util.ZipEntrySource;
/*  20:    */ import org.apache.poi.openxml4j.util.ZipSecureFile;
/*  21:    */ import org.apache.poi.openxml4j.util.ZipSecureFile.ThresholdInputStream;
/*  22:    */ import org.apache.poi.poifs.filesystem.FileMagic;
/*  23:    */ import org.apache.poi.util.Internal;
/*  24:    */ import org.apache.poi.util.Removal;
/*  25:    */ 
/*  26:    */ @Internal
/*  27:    */ public final class ZipHelper
/*  28:    */ {
/*  29:    */   private static final String FORWARD_SLASH = "/";
/*  30:    */   @Deprecated
/*  31:    */   @Removal(version="3.18")
/*  32:    */   public static final int READ_WRITE_FILE_BUFFER_SIZE = 8192;
/*  33:    */   
/*  34:    */   public static ZipEntry getCorePropertiesZipEntry(ZipPackage pkg)
/*  35:    */   {
/*  36: 77 */     PackageRelationship corePropsRel = pkg.getRelationshipsByType("http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties").getRelationship(0);
/*  37: 80 */     if (corePropsRel == null) {
/*  38: 81 */       return null;
/*  39:    */     }
/*  40: 84 */     return new ZipEntry(corePropsRel.getTargetURI().getPath());
/*  41:    */   }
/*  42:    */   
/*  43:    */   public static ZipEntry getContentTypeZipEntry(ZipPackage pkg)
/*  44:    */   {
/*  45: 91 */     Enumeration<? extends ZipEntry> entries = pkg.getZipArchive().getEntries();
/*  46: 95 */     while (entries.hasMoreElements())
/*  47:    */     {
/*  48: 96 */       ZipEntry entry = (ZipEntry)entries.nextElement();
/*  49: 97 */       if (entry.getName().equals("[Content_Types].xml")) {
/*  50: 99 */         return entry;
/*  51:    */       }
/*  52:    */     }
/*  53:102 */     return null;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public static String getOPCNameFromZipItemName(String zipItemName)
/*  57:    */   {
/*  58:114 */     if (zipItemName == null) {
/*  59:115 */       throw new IllegalArgumentException("zipItemName cannot be null");
/*  60:    */     }
/*  61:117 */     if (zipItemName.startsWith("/")) {
/*  62:118 */       return zipItemName;
/*  63:    */     }
/*  64:120 */     return "/" + zipItemName;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public static String getZipItemNameFromOPCName(String opcItemName)
/*  68:    */   {
/*  69:132 */     if (opcItemName == null) {
/*  70:133 */       throw new IllegalArgumentException("opcItemName cannot be null");
/*  71:    */     }
/*  72:136 */     String retVal = opcItemName;
/*  73:137 */     while (retVal.startsWith("/")) {
/*  74:138 */       retVal = retVal.substring(1);
/*  75:    */     }
/*  76:140 */     return retVal;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public static URI getZipURIFromOPCName(String opcItemName)
/*  80:    */   {
/*  81:152 */     if (opcItemName == null) {
/*  82:153 */       throw new IllegalArgumentException("opcItemName");
/*  83:    */     }
/*  84:156 */     String retVal = opcItemName;
/*  85:157 */     while (retVal.startsWith("/")) {
/*  86:158 */       retVal = retVal.substring(1);
/*  87:    */     }
/*  88:    */     try
/*  89:    */     {
/*  90:161 */       return new URI(retVal);
/*  91:    */     }
/*  92:    */     catch (URISyntaxException e) {}
/*  93:163 */     return null;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public static void verifyZipHeader(InputStream stream)
/*  97:    */     throws NotOfficeXmlFileException, IOException
/*  98:    */   {
/*  99:174 */     InputStream is = FileMagic.prepareToCheckMagic(stream);
/* 100:175 */     FileMagic fm = FileMagic.valueOf(is);
/* 101:177 */     switch (1.$SwitchMap$org$apache$poi$poifs$filesystem$FileMagic[fm.ordinal()])
/* 102:    */     {
/* 103:    */     case 1: 
/* 104:179 */       throw new OLE2NotOfficeXmlFileException("The supplied data appears to be in the OLE2 Format. You are calling the part of POI that deals with OOXML (Office Open XML) Documents. You need to call a different part of POI to process this data (eg HSSF instead of XSSF)");
/* 105:    */     case 2: 
/* 106:185 */       throw new NotOfficeXmlFileException("The supplied data appears to be a raw XML file. Formats such as Office 2003 XML are not supported");
/* 107:    */     }
/* 108:    */   }
/* 109:    */   
/* 110:    */   public static ZipSecureFile.ThresholdInputStream openZipStream(InputStream stream)
/* 111:    */     throws IOException
/* 112:    */   {
/* 113:208 */     InputStream checkedStream = FileMagic.prepareToCheckMagic(stream);
/* 114:209 */     verifyZipHeader(checkedStream);
/* 115:    */     
/* 116:    */ 
/* 117:212 */     InputStream zis = new ZipInputStream(checkedStream);
/* 118:213 */     return ZipSecureFile.addThreshold(zis);
/* 119:    */   }
/* 120:    */   
/* 121:    */   public static ZipFile openZipFile(File file)
/* 122:    */     throws IOException, NotOfficeXmlFileException
/* 123:    */   {
/* 124:227 */     if (!file.exists()) {
/* 125:228 */       throw new FileNotFoundException("File does not exist");
/* 126:    */     }
/* 127:230 */     if (file.isDirectory()) {
/* 128:231 */       throw new IOException("File is a directory");
/* 129:    */     }
/* 130:235 */     FileInputStream input = new FileInputStream(file);
/* 131:    */     try
/* 132:    */     {
/* 133:237 */       verifyZipHeader(input);
/* 134:    */     }
/* 135:    */     finally
/* 136:    */     {
/* 137:239 */       input.close();
/* 138:    */     }
/* 139:243 */     return new ZipSecureFile(file);
/* 140:    */   }
/* 141:    */   
/* 142:    */   public static ZipFile openZipFile(String path)
/* 143:    */     throws IOException
/* 144:    */   {
/* 145:254 */     return openZipFile(new File(path));
/* 146:    */   }
/* 147:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.internal.ZipHelper
 * JD-Core Version:    0.7.0.1
 */