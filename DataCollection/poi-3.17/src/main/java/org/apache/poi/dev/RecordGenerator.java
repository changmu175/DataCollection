/*   1:    */ package org.apache.poi.dev;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileNotFoundException;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import java.util.Locale;
/*   8:    */ import java.util.Properties;
/*   9:    */ import javax.xml.parsers.DocumentBuilder;
/*  10:    */ import javax.xml.parsers.DocumentBuilderFactory;
/*  11:    */ import javax.xml.transform.Result;
/*  12:    */ import javax.xml.transform.Transformer;
/*  13:    */ import javax.xml.transform.TransformerException;
/*  14:    */ import javax.xml.transform.TransformerFactory;
/*  15:    */ import javax.xml.transform.stream.StreamResult;
/*  16:    */ import javax.xml.transform.stream.StreamSource;
/*  17:    */ import org.apache.poi.util.XMLHelper;
/*  18:    */ import org.w3c.dom.Document;
/*  19:    */ import org.w3c.dom.Element;
/*  20:    */ import org.w3c.dom.NamedNodeMap;
/*  21:    */ import org.w3c.dom.Node;
/*  22:    */ import org.w3c.dom.NodeList;
/*  23:    */ 
/*  24:    */ public class RecordGenerator
/*  25:    */ {
/*  26:    */   public static void main(String[] args)
/*  27:    */     throws Exception
/*  28:    */   {
/*  29: 57 */     Class.forName("org.apache.poi.generator.FieldIterator");
/*  30: 59 */     if (args.length != 4)
/*  31:    */     {
/*  32: 60 */       System.out.println("Usage:");
/*  33: 61 */       System.out.println("  java org.apache.poi.hssf.util.RecordGenerator RECORD_DEFINTIONS RECORD_STYLES DEST_SRC_PATH TEST_SRC_PATH");
/*  34:    */     }
/*  35:    */     else
/*  36:    */     {
/*  37: 63 */       generateRecords(args[0], args[1], args[2], args[3]);
/*  38:    */     }
/*  39:    */   }
/*  40:    */   
/*  41:    */   private static void generateRecords(String defintionsDir, String recordStyleDir, String destSrcPathDir, String testSrcPathDir)
/*  42:    */     throws Exception
/*  43:    */   {
/*  44: 70 */     File[] definitionsFiles = new File(defintionsDir).listFiles();
/*  45: 71 */     if (definitionsFiles == null)
/*  46:    */     {
/*  47: 72 */       System.err.println(defintionsDir + " is not a directory.");
/*  48: 73 */       return;
/*  49:    */     }
/*  50: 76 */     for (File file : definitionsFiles) {
/*  51: 77 */       if ((file.isFile()) && ((file.getName().endsWith("_record.xml")) || (file.getName().endsWith("_type.xml"))))
/*  52:    */       {
/*  53: 83 */         DocumentBuilderFactory factory = XMLHelper.getDocumentBuilderFactory();
/*  54: 84 */         DocumentBuilder builder = factory.newDocumentBuilder();
/*  55: 85 */         Document document = builder.parse(file);
/*  56: 86 */         Element record = document.getDocumentElement();
/*  57: 87 */         String extendstg = record.getElementsByTagName("extends").item(0).getFirstChild().getNodeValue();
/*  58: 88 */         String suffix = record.getElementsByTagName("suffix").item(0).getFirstChild().getNodeValue();
/*  59: 89 */         String recordName = record.getAttributes().getNamedItem("name").getNodeValue();
/*  60: 90 */         String packageName = record.getAttributes().getNamedItem("package").getNodeValue();
/*  61: 91 */         packageName = packageName.replace('.', '/');
/*  62:    */         
/*  63:    */ 
/*  64: 94 */         String destinationPath = destSrcPathDir + "/" + packageName;
/*  65: 95 */         File destinationPathFile = new File(destinationPath);
/*  66: 96 */         if (!destinationPathFile.mkdirs()) {
/*  67: 97 */           throw new IOException("Could not create directory " + destinationPathFile);
/*  68:    */         }
/*  69: 99 */         System.out.println("Created destination directory: " + destinationPath);
/*  70:    */         
/*  71:101 */         String destinationFilepath = destinationPath + "/" + recordName + suffix + ".java";
/*  72:102 */         transform(file, new File(destinationFilepath), new File(recordStyleDir + "/" + extendstg.toLowerCase(Locale.ROOT) + ".xsl"));
/*  73:    */         
/*  74:104 */         System.out.println("Generated " + suffix + ": " + destinationFilepath);
/*  75:    */         
/*  76:    */ 
/*  77:107 */         destinationPath = testSrcPathDir + "/" + packageName;
/*  78:108 */         destinationPathFile = new File(destinationPath);
/*  79:109 */         if (!destinationPathFile.mkdirs()) {
/*  80:110 */           throw new IOException("Could not create directory " + destinationPathFile);
/*  81:    */         }
/*  82:112 */         System.out.println("Created destination directory: " + destinationPath);
/*  83:    */         
/*  84:114 */         destinationFilepath = destinationPath + "/Test" + recordName + suffix + ".java";
/*  85:115 */         if (!new File(destinationFilepath).exists())
/*  86:    */         {
/*  87:116 */           String temp = recordStyleDir + "/" + extendstg.toLowerCase(Locale.ROOT) + "_test.xsl";
/*  88:117 */           transform(file, new File(destinationFilepath), new File(temp));
/*  89:118 */           System.out.println("Generated test: " + destinationFilepath);
/*  90:    */         }
/*  91:    */         else
/*  92:    */         {
/*  93:120 */           System.out.println("Skipped test generation: " + destinationFilepath);
/*  94:    */         }
/*  95:    */       }
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   private static void transform(File in, File out, File xslt)
/* 100:    */     throws FileNotFoundException, TransformerException
/* 101:    */   {
/* 102:141 */     StreamSource ss = new StreamSource(xslt);
/* 103:142 */     TransformerFactory tf = TransformerFactory.newInstance();
/* 104:    */     Transformer t;
/* 105:    */     try
/* 106:    */     {
/* 107:146 */       t = tf.newTransformer(ss);
/* 108:    */     }
/* 109:    */     catch (TransformerException ex)
/* 110:    */     {
/* 111:150 */       System.err.println("Error compiling XSL style sheet " + xslt);
/* 112:151 */       throw ex;
/* 113:    */     }
/* 114:153 */     Properties p = new Properties();
/* 115:154 */     p.setProperty("method", "text");
/* 116:155 */     t.setOutputProperties(p);
/* 117:156 */     Result result = new StreamResult(out);
/* 118:157 */     t.transform(new StreamSource(in), result);
/* 119:    */   }
/* 120:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.dev.RecordGenerator
 * JD-Core Version:    0.7.0.1
 */