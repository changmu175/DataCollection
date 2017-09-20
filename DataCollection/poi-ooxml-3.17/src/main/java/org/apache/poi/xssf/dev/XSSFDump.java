/*   1:    */ package org.apache.poi.xssf.dev;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileOutputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import java.util.Enumeration;
/*   8:    */ import java.util.zip.ZipEntry;
/*   9:    */ import java.util.zip.ZipFile;
/*  10:    */ import org.apache.poi.POIXMLTypeLoader;
/*  11:    */ import org.apache.poi.openxml4j.opc.internal.ZipHelper;
/*  12:    */ import org.apache.poi.util.DocumentHelper;
/*  13:    */ import org.apache.poi.util.IOUtils;
/*  14:    */ import org.apache.xmlbeans.XmlException;
/*  15:    */ import org.apache.xmlbeans.XmlObject;
/*  16:    */ import org.apache.xmlbeans.XmlObject.Factory;
/*  17:    */ import org.apache.xmlbeans.XmlOptions;
/*  18:    */ import org.w3c.dom.Document;
/*  19:    */ 
/*  20:    */ public final class XSSFDump
/*  21:    */ {
/*  22:    */   public static void main(String[] args)
/*  23:    */     throws Exception
/*  24:    */   {
/*  25: 45 */     for (int i = 0; i < args.length; i++)
/*  26:    */     {
/*  27: 46 */       System.out.println("Dumping " + args[i]);
/*  28: 47 */       ZipFile zip = ZipHelper.openZipFile(args[i]);
/*  29:    */       try
/*  30:    */       {
/*  31: 49 */         dump(zip);
/*  32:    */       }
/*  33:    */       finally
/*  34:    */       {
/*  35: 51 */         zip.close();
/*  36:    */       }
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40:    */   private static void createDirIfMissing(File directory)
/*  41:    */     throws RuntimeException
/*  42:    */   {
/*  43: 57 */     if (!directory.exists())
/*  44:    */     {
/*  45: 58 */       boolean dirWasCreated = directory.mkdir();
/*  46: 59 */       if (!dirWasCreated) {
/*  47: 60 */         throw new RuntimeException("Unable to create directory: " + directory);
/*  48:    */       }
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   private static void recursivelyCreateDirIfMissing(File directory)
/*  53:    */     throws RuntimeException
/*  54:    */   {
/*  55: 66 */     if (!directory.exists())
/*  56:    */     {
/*  57: 67 */       boolean dirsWereCreated = directory.mkdirs();
/*  58: 68 */       if (!dirsWereCreated) {
/*  59: 69 */         throw new RuntimeException("Unable to recursively create directory: " + directory);
/*  60:    */       }
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   public static void dump(ZipFile zip)
/*  65:    */     throws Exception
/*  66:    */   {
/*  67: 76 */     String zipname = zip.getName();
/*  68: 77 */     int sep = zipname.lastIndexOf('.');
/*  69: 78 */     File root = new File(zipname.substring(0, sep));
/*  70: 79 */     createDirIfMissing(root);
/*  71: 80 */     System.out.println("Dumping to directory " + root);
/*  72:    */     
/*  73: 82 */     Enumeration<? extends ZipEntry> en = zip.entries();
/*  74: 83 */     while (en.hasMoreElements())
/*  75:    */     {
/*  76: 84 */       ZipEntry entry = (ZipEntry)en.nextElement();
/*  77: 85 */       String name = entry.getName();
/*  78: 86 */       int idx = name.lastIndexOf('/');
/*  79: 87 */       if (idx != -1)
/*  80:    */       {
/*  81: 88 */         File bs = new File(root, name.substring(0, idx));
/*  82: 89 */         recursivelyCreateDirIfMissing(bs);
/*  83:    */       }
/*  84: 92 */       File f = new File(root, entry.getName());
/*  85: 93 */       OutputStream out = new FileOutputStream(f);
/*  86:    */       try
/*  87:    */       {
/*  88: 95 */         if ((entry.getName().endsWith(".xml")) || (entry.getName().endsWith(".vml")) || (entry.getName().endsWith(".rels"))) {
/*  89:    */           try
/*  90:    */           {
/*  91: 97 */             Document doc = DocumentHelper.readDocument(zip.getInputStream(entry));
/*  92: 98 */             XmlObject xml = XmlObject.Factory.parse(doc, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  93: 99 */             XmlOptions options = new XmlOptions();
/*  94:100 */             options.setSavePrettyPrint();
/*  95:101 */             xml.save(out, options);
/*  96:    */           }
/*  97:    */           catch (XmlException e)
/*  98:    */           {
/*  99:103 */             System.err.println("Failed to parse " + entry.getName() + ", dumping raw content");
/* 100:104 */             IOUtils.copy(zip.getInputStream(entry), out);
/* 101:    */           }
/* 102:    */         } else {
/* 103:107 */           IOUtils.copy(zip.getInputStream(entry), out);
/* 104:    */         }
/* 105:    */       }
/* 106:    */       finally
/* 107:    */       {
/* 108:110 */         out.close();
/* 109:    */       }
/* 110:    */     }
/* 111:    */   }
/* 112:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.dev.XSSFDump
 * JD-Core Version:    0.7.0.1
 */