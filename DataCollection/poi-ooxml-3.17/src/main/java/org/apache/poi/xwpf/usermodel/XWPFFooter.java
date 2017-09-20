/*   1:    */ package org.apache.poi.xwpf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.util.List;
/*   7:    */ import javax.xml.namespace.QName;
/*   8:    */ import org.apache.poi.POIXMLDocumentPart;
/*   9:    */ import org.apache.poi.POIXMLException;
/*  10:    */ import org.apache.poi.POIXMLTypeLoader;
/*  11:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  12:    */ import org.apache.xmlbeans.SchemaType;
/*  13:    */ import org.apache.xmlbeans.XmlCursor;
/*  14:    */ import org.apache.xmlbeans.XmlObject;
/*  15:    */ import org.apache.xmlbeans.XmlOptions;
/*  16:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr;
/*  17:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering;
/*  18:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
/*  19:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtBlock;
/*  20:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
/*  21:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.FtrDocument;
/*  22:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.FtrDocument.Factory;
/*  23:    */ 
/*  24:    */ public class XWPFFooter
/*  25:    */   extends XWPFHeaderFooter
/*  26:    */ {
/*  27:    */   public XWPFFooter() {}
/*  28:    */   
/*  29:    */   public XWPFFooter(XWPFDocument doc, CTHdrFtr hdrFtr)
/*  30:    */     throws IOException
/*  31:    */   {
/*  32: 49 */     super(doc, hdrFtr);
/*  33: 50 */     XmlCursor cursor = this.headerFooter.newCursor();
/*  34: 51 */     cursor.selectPath("./*");
/*  35: 52 */     while (cursor.toNextSelection())
/*  36:    */     {
/*  37: 53 */       XmlObject o = cursor.getObject();
/*  38: 54 */       if ((o instanceof CTP))
/*  39:    */       {
/*  40: 55 */         XWPFParagraph p = new XWPFParagraph((CTP)o, this);
/*  41: 56 */         this.paragraphs.add(p);
/*  42: 57 */         this.bodyElements.add(p);
/*  43:    */       }
/*  44: 59 */       if ((o instanceof CTTbl))
/*  45:    */       {
/*  46: 60 */         XWPFTable t = new XWPFTable((CTTbl)o, this);
/*  47: 61 */         this.tables.add(t);
/*  48: 62 */         this.bodyElements.add(t);
/*  49:    */       }
/*  50:    */     }
/*  51: 66 */     cursor.dispose();
/*  52:    */   }
/*  53:    */   
/*  54:    */   public XWPFFooter(POIXMLDocumentPart parent, PackagePart part)
/*  55:    */     throws IOException
/*  56:    */   {
/*  57: 73 */     super(parent, part);
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected void commit()
/*  61:    */     throws IOException
/*  62:    */   {
/*  63: 81 */     XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  64: 82 */     xmlOptions.setSaveSyntheticDocumentElement(new QName(CTNumbering.type.getName().getNamespaceURI(), "ftr"));
/*  65: 83 */     PackagePart part = getPackagePart();
/*  66: 84 */     OutputStream out = part.getOutputStream();
/*  67: 85 */     super._getHdrFtr().save(out, xmlOptions);
/*  68: 86 */     out.close();
/*  69:    */   }
/*  70:    */   
/*  71:    */   protected void onDocumentRead()
/*  72:    */     throws IOException
/*  73:    */   {
/*  74: 91 */     super.onDocumentRead();
/*  75: 92 */     FtrDocument ftrDocument = null;
/*  76: 93 */     InputStream is = null;
/*  77:    */     try
/*  78:    */     {
/*  79: 95 */       is = getPackagePart().getInputStream();
/*  80: 96 */       ftrDocument = FtrDocument.Factory.parse(is, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  81: 97 */       this.headerFooter = ftrDocument.getFtr();
/*  82:    */       
/*  83:    */ 
/*  84:100 */       XmlCursor cursor = this.headerFooter.newCursor();
/*  85:101 */       cursor.selectPath("./*");
/*  86:102 */       while (cursor.toNextSelection())
/*  87:    */       {
/*  88:103 */         XmlObject o = cursor.getObject();
/*  89:104 */         if ((o instanceof CTP))
/*  90:    */         {
/*  91:105 */           XWPFParagraph p = new XWPFParagraph((CTP)o, this);
/*  92:106 */           this.paragraphs.add(p);
/*  93:107 */           this.bodyElements.add(p);
/*  94:    */         }
/*  95:109 */         if ((o instanceof CTTbl))
/*  96:    */         {
/*  97:110 */           XWPFTable t = new XWPFTable((CTTbl)o, this);
/*  98:111 */           this.tables.add(t);
/*  99:112 */           this.bodyElements.add(t);
/* 100:    */         }
/* 101:114 */         if ((o instanceof CTSdtBlock))
/* 102:    */         {
/* 103:115 */           XWPFSDT c = new XWPFSDT((CTSdtBlock)o, this);
/* 104:116 */           this.bodyElements.add(c);
/* 105:    */         }
/* 106:    */       }
/* 107:119 */       cursor.dispose();
/* 108:    */     }
/* 109:    */     catch (Exception e)
/* 110:    */     {
/* 111:121 */       throw new POIXMLException(e);
/* 112:    */     }
/* 113:    */     finally
/* 114:    */     {
/* 115:123 */       if (is != null) {
/* 116:124 */         is.close();
/* 117:    */       }
/* 118:    */     }
/* 119:    */   }
/* 120:    */   
/* 121:    */   public BodyType getPartType()
/* 122:    */   {
/* 123:135 */     return BodyType.FOOTER;
/* 124:    */   }
/* 125:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFFooter
 * JD-Core Version:    0.7.0.1
 */