/*   1:    */ package org.apache.poi.dev;
/*   2:    */ 
/*   3:    */ import java.io.BufferedOutputStream;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.FileOutputStream;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.OutputStream;
/*   8:    */ import java.io.PrintStream;
/*   9:    */ import java.util.Enumeration;
/*  10:    */ import java.util.zip.ZipEntry;
/*  11:    */ import java.util.zip.ZipException;
/*  12:    */ import java.util.zip.ZipFile;
/*  13:    */ import java.util.zip.ZipOutputStream;
/*  14:    */ import javax.xml.parsers.DocumentBuilder;
/*  15:    */ import javax.xml.parsers.DocumentBuilderFactory;
/*  16:    */ import javax.xml.parsers.ParserConfigurationException;
/*  17:    */ import javax.xml.transform.Result;
/*  18:    */ import javax.xml.transform.Source;
/*  19:    */ import javax.xml.transform.Transformer;
/*  20:    */ import javax.xml.transform.TransformerException;
/*  21:    */ import javax.xml.transform.TransformerFactory;
/*  22:    */ import javax.xml.transform.dom.DOMSource;
/*  23:    */ import javax.xml.transform.stream.StreamResult;
/*  24:    */ import org.apache.poi.openxml4j.opc.internal.ZipHelper;
/*  25:    */ import org.apache.poi.openxml4j.util.ZipSecureFile;
/*  26:    */ import org.apache.poi.util.IOUtils;
/*  27:    */ import org.w3c.dom.Document;
/*  28:    */ import org.xml.sax.InputSource;
/*  29:    */ 
/*  30:    */ public class OOXMLPrettyPrint
/*  31:    */ {
/*  32: 56 */   private final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
/*  33:    */   private final DocumentBuilder documentBuilder;
/*  34:    */   
/*  35:    */   public OOXMLPrettyPrint()
/*  36:    */     throws ParserConfigurationException
/*  37:    */   {
/*  38: 61 */     ZipSecureFile.setMinInflateRatio(1.E-005D);
/*  39:    */     
/*  40: 63 */     this.documentBuilder = this.documentBuilderFactory.newDocumentBuilder();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public static void main(String[] args)
/*  44:    */     throws Exception
/*  45:    */   {
/*  46: 67 */     if ((args.length <= 1) || (args.length % 2 != 0))
/*  47:    */     {
/*  48: 68 */       System.err.println("Use:");
/*  49: 69 */       System.err.println("\tjava OOXMLPrettyPrint [<filename> <outfilename>] ...");
/*  50: 70 */       System.exit(1);
/*  51:    */     }
/*  52: 73 */     for (int i = 0; i < args.length; i += 2)
/*  53:    */     {
/*  54: 74 */       File f = new File(args[i]);
/*  55: 75 */       if (!f.exists())
/*  56:    */       {
/*  57: 76 */         System.err.println("Error, file not found!");
/*  58: 77 */         System.err.println("\t" + f);
/*  59: 78 */         System.exit(2);
/*  60:    */       }
/*  61: 81 */       handleFile(f, new File(args[(i + 1)]));
/*  62:    */     }
/*  63: 83 */     System.out.println("Done.");
/*  64:    */   }
/*  65:    */   
/*  66:    */   private static void handleFile(File file, File outFile)
/*  67:    */     throws ZipException, IOException, TransformerException, ParserConfigurationException
/*  68:    */   {
/*  69: 88 */     System.out.println("Reading zip-file " + file + " and writing pretty-printed XML to " + outFile);
/*  70:    */     
/*  71: 90 */     ZipFile zipFile = ZipHelper.openZipFile(file);
/*  72:    */     try
/*  73:    */     {
/*  74: 92 */       ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)));
/*  75:    */       try
/*  76:    */       {
/*  77: 94 */         new OOXMLPrettyPrint().handle(zipFile, out);
/*  78:    */       }
/*  79:    */       finally
/*  80:    */       {
/*  81: 96 */         out.close();
/*  82:    */       }
/*  83:    */     }
/*  84:    */     finally
/*  85:    */     {
/*  86: 99 */       zipFile.close();
/*  87:    */       
/*  88:101 */       System.out.println();
/*  89:    */     }
/*  90:    */   }
/*  91:    */   
/*  92:    */   private void handle(ZipFile file, ZipOutputStream out)
/*  93:    */     throws IOException, TransformerException
/*  94:    */   {
/*  95:106 */     Enumeration<? extends ZipEntry> entries = file.entries();
/*  96:107 */     while (entries.hasMoreElements())
/*  97:    */     {
/*  98:108 */       ZipEntry entry = (ZipEntry)entries.nextElement();
/*  99:    */       
/* 100:110 */       String name = entry.getName();
/* 101:111 */       out.putNextEntry(new ZipEntry(name));
/* 102:    */       try
/* 103:    */       {
/* 104:113 */         if ((name.endsWith(".xml")) || (name.endsWith(".rels")))
/* 105:    */         {
/* 106:114 */           Document document = this.documentBuilder.parse(new InputSource(file.getInputStream(entry)));
/* 107:115 */           document.setXmlStandalone(true);
/* 108:116 */           pretty(document, out, 2);
/* 109:    */         }
/* 110:    */         else
/* 111:    */         {
/* 112:118 */           System.out.println("Not pretty-printing non-XML file " + name);
/* 113:119 */           IOUtils.copy(file.getInputStream(entry), out);
/* 114:    */         }
/* 115:    */       }
/* 116:    */       catch (Exception e)
/* 117:    */       {
/* 118:122 */         throw new IOException("While handling entry " + name, e);
/* 119:    */       }
/* 120:    */       finally
/* 121:    */       {
/* 122:124 */         out.closeEntry();
/* 123:    */       }
/* 124:126 */       System.out.print(".");
/* 125:    */     }
/* 126:    */   }
/* 127:    */   
/* 128:    */   private static void pretty(Document document, OutputStream outputStream, int indent)
/* 129:    */     throws TransformerException
/* 130:    */   {
/* 131:131 */     TransformerFactory transformerFactory = TransformerFactory.newInstance();
/* 132:132 */     Transformer transformer = transformerFactory.newTransformer();
/* 133:133 */     transformer.setOutputProperty("encoding", "UTF-8");
/* 134:134 */     if (indent > 0)
/* 135:    */     {
/* 136:136 */       transformer.setOutputProperty("indent", "yes");
/* 137:137 */       transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString(indent));
/* 138:    */     }
/* 139:139 */     Result result = new StreamResult(outputStream);
/* 140:140 */     Source source = new DOMSource(document);
/* 141:141 */     transformer.transform(source, result);
/* 142:    */   }
/* 143:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.dev.OOXMLPrettyPrint
 * JD-Core Version:    0.7.0.1
 */