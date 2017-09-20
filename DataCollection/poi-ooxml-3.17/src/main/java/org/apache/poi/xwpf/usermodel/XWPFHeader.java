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
/*  14:    */ import org.apache.xmlbeans.XmlException;
/*  15:    */ import org.apache.xmlbeans.XmlObject;
/*  16:    */ import org.apache.xmlbeans.XmlOptions;
/*  17:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr;
/*  18:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering;
/*  19:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
/*  20:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtBlock;
/*  21:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
/*  22:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.HdrDocument;
/*  23:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.HdrDocument.Factory;
/*  24:    */ 
/*  25:    */ public class XWPFHeader
/*  26:    */   extends XWPFHeaderFooter
/*  27:    */ {
/*  28:    */   public XWPFHeader() {}
/*  29:    */   
/*  30:    */   public XWPFHeader(POIXMLDocumentPart parent, PackagePart part)
/*  31:    */     throws IOException
/*  32:    */   {
/*  33: 53 */     super(parent, part);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public XWPFHeader(XWPFDocument doc, CTHdrFtr hdrFtr)
/*  37:    */   {
/*  38: 57 */     super(doc, hdrFtr);
/*  39: 58 */     XmlCursor cursor = this.headerFooter.newCursor();
/*  40: 59 */     cursor.selectPath("./*");
/*  41: 60 */     while (cursor.toNextSelection())
/*  42:    */     {
/*  43: 61 */       XmlObject o = cursor.getObject();
/*  44: 62 */       if ((o instanceof CTP))
/*  45:    */       {
/*  46: 63 */         XWPFParagraph p = new XWPFParagraph((CTP)o, this);
/*  47: 64 */         this.paragraphs.add(p);
/*  48:    */       }
/*  49: 66 */       if ((o instanceof CTTbl))
/*  50:    */       {
/*  51: 67 */         XWPFTable t = new XWPFTable((CTTbl)o, this);
/*  52: 68 */         this.tables.add(t);
/*  53:    */       }
/*  54:    */     }
/*  55: 71 */     cursor.dispose();
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected void commit()
/*  59:    */     throws IOException
/*  60:    */   {
/*  61: 79 */     XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  62: 80 */     xmlOptions.setSaveSyntheticDocumentElement(new QName(CTNumbering.type.getName().getNamespaceURI(), "hdr"));
/*  63: 81 */     PackagePart part = getPackagePart();
/*  64: 82 */     OutputStream out = part.getOutputStream();
/*  65: 83 */     super._getHdrFtr().save(out, xmlOptions);
/*  66: 84 */     out.close();
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected void onDocumentRead()
/*  70:    */     throws IOException
/*  71:    */   {
/*  72: 94 */     super.onDocumentRead();
/*  73: 95 */     HdrDocument hdrDocument = null;
/*  74: 96 */     InputStream is = null;
/*  75:    */     try
/*  76:    */     {
/*  77: 98 */       is = getPackagePart().getInputStream();
/*  78: 99 */       hdrDocument = HdrDocument.Factory.parse(is, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  79:100 */       this.headerFooter = hdrDocument.getHdr();
/*  80:    */       
/*  81:    */ 
/*  82:103 */       XmlCursor cursor = this.headerFooter.newCursor();
/*  83:104 */       cursor.selectPath("./*");
/*  84:105 */       while (cursor.toNextSelection())
/*  85:    */       {
/*  86:106 */         XmlObject o = cursor.getObject();
/*  87:107 */         if ((o instanceof CTP))
/*  88:    */         {
/*  89:108 */           XWPFParagraph p = new XWPFParagraph((CTP)o, this);
/*  90:109 */           this.paragraphs.add(p);
/*  91:110 */           this.bodyElements.add(p);
/*  92:    */         }
/*  93:112 */         if ((o instanceof CTTbl))
/*  94:    */         {
/*  95:113 */           XWPFTable t = new XWPFTable((CTTbl)o, this);
/*  96:114 */           this.tables.add(t);
/*  97:115 */           this.bodyElements.add(t);
/*  98:    */         }
/*  99:117 */         if ((o instanceof CTSdtBlock))
/* 100:    */         {
/* 101:118 */           XWPFSDT c = new XWPFSDT((CTSdtBlock)o, this);
/* 102:119 */           this.bodyElements.add(c);
/* 103:    */         }
/* 104:    */       }
/* 105:122 */       cursor.dispose();
/* 106:    */     }
/* 107:    */     catch (XmlException e)
/* 108:    */     {
/* 109:124 */       throw new POIXMLException(e);
/* 110:    */     }
/* 111:    */     finally
/* 112:    */     {
/* 113:126 */       if (is != null) {
/* 114:127 */         is.close();
/* 115:    */       }
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   public BodyType getPartType()
/* 120:    */   {
/* 121:138 */     return BodyType.HEADER;
/* 122:    */   }
/* 123:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFHeader
 * JD-Core Version:    0.7.0.1
 */