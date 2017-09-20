/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import org.apache.poi.POIXMLDocumentPart;
/*   5:    */ import org.apache.poi.POIXMLTypeLoader;
/*   6:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   7:    */ import org.apache.poi.sl.usermodel.MasterSheet;
/*   8:    */ import org.apache.xmlbeans.XmlException;
/*   9:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping;
/*  10:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMaster;
/*  11:    */ import org.openxmlformats.schemas.presentationml.x2006.main.NotesMasterDocument;
/*  12:    */ import org.openxmlformats.schemas.presentationml.x2006.main.NotesMasterDocument.Factory;
/*  13:    */ 
/*  14:    */ public class XSLFNotesMaster
/*  15:    */   extends XSLFSheet
/*  16:    */   implements MasterSheet<XSLFShape, XSLFTextParagraph>
/*  17:    */ {
/*  18:    */   private CTNotesMaster _slide;
/*  19:    */   private XSLFTheme _theme;
/*  20:    */   
/*  21:    */   XSLFNotesMaster()
/*  22:    */   {
/*  23: 58 */     this._slide = prototype();
/*  24:    */   }
/*  25:    */   
/*  26:    */   protected XSLFNotesMaster(PackagePart part)
/*  27:    */     throws IOException, XmlException
/*  28:    */   {
/*  29: 65 */     super(part);
/*  30: 66 */     NotesMasterDocument doc = NotesMasterDocument.Factory.parse(getPackagePart().getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  31:    */     
/*  32: 68 */     this._slide = doc.getNotesMaster();
/*  33: 69 */     setCommonSlideData(this._slide.getCSld());
/*  34:    */   }
/*  35:    */   
/*  36:    */   /* Error */
/*  37:    */   private static CTNotesMaster prototype()
/*  38:    */   {
/*  39:    */     // Byte code:
/*  40:    */     //   0: ldc_w 12
/*  41:    */     //   3: ldc 13
/*  42:    */     //   5: invokevirtual 14	java/lang/Class:getResourceAsStream	(Ljava/lang/String;)Ljava/io/InputStream;
/*  43:    */     //   8: astore_0
/*  44:    */     //   9: aload_0
/*  45:    */     //   10: ifnonnull +13 -> 23
/*  46:    */     //   13: new 15	org/apache/poi/POIXMLException
/*  47:    */     //   16: dup
/*  48:    */     //   17: ldc 16
/*  49:    */     //   19: invokespecial 17	org/apache/poi/POIXMLException:<init>	(Ljava/lang/String;)V
/*  50:    */     //   22: athrow
/*  51:    */     //   23: aload_0
/*  52:    */     //   24: getstatic 7	org/apache/poi/POIXMLTypeLoader:DEFAULT_XML_OPTIONS	Lorg/apache/xmlbeans/XmlOptions;
/*  53:    */     //   27: invokestatic 8	org/openxmlformats/schemas/presentationml/x2006/main/NotesMasterDocument$Factory:parse	(Ljava/io/InputStream;Lorg/apache/xmlbeans/XmlOptions;)Lorg/openxmlformats/schemas/presentationml/x2006/main/NotesMasterDocument;
/*  54:    */     //   30: astore_1
/*  55:    */     //   31: aload_1
/*  56:    */     //   32: invokeinterface 9 1 0
/*  57:    */     //   37: astore_2
/*  58:    */     //   38: aload_2
/*  59:    */     //   39: astore_3
/*  60:    */     //   40: aload_0
/*  61:    */     //   41: invokevirtual 18	java/io/InputStream:close	()V
/*  62:    */     //   44: aload_3
/*  63:    */     //   45: areturn
/*  64:    */     //   46: astore 4
/*  65:    */     //   48: aload_0
/*  66:    */     //   49: invokevirtual 18	java/io/InputStream:close	()V
/*  67:    */     //   52: aload 4
/*  68:    */     //   54: athrow
/*  69:    */     //   55: astore_1
/*  70:    */     //   56: new 15	org/apache/poi/POIXMLException
/*  71:    */     //   59: dup
/*  72:    */     //   60: ldc 20
/*  73:    */     //   62: aload_1
/*  74:    */     //   63: invokespecial 21	org/apache/poi/POIXMLException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
/*  75:    */     //   66: athrow
/*  76:    */     // Line number table:
/*  77:    */     //   Java source line #73	-> byte code offset #0
/*  78:    */     //   Java source line #74	-> byte code offset #9
/*  79:    */     //   Java source line #75	-> byte code offset #13
/*  80:    */     //   Java source line #80	-> byte code offset #23
/*  81:    */     //   Java source line #81	-> byte code offset #31
/*  82:    */     //   Java source line #82	-> byte code offset #38
/*  83:    */     //   Java source line #84	-> byte code offset #40
/*  84:    */     //   Java source line #86	-> byte code offset #55
/*  85:    */     //   Java source line #87	-> byte code offset #56
/*  86:    */     // Local variable table:
/*  87:    */     //   start	length	slot	name	signature
/*  88:    */     //   8	41	0	is	java.io.InputStream
/*  89:    */     //   30	2	1	doc	NotesMasterDocument
/*  90:    */     //   55	8	1	e	java.lang.Exception
/*  91:    */     //   37	2	2	slide	CTNotesMaster
/*  92:    */     //   46	7	4	localObject	java.lang.Object
/*  93:    */     // Exception table:
/*  94:    */     //   from	to	target	type
/*  95:    */     //   23	40	46	finally
/*  96:    */     //   46	48	46	finally
/*  97:    */     //   23	44	55	java/lang/Exception
/*  98:    */     //   46	55	55	java/lang/Exception
/*  99:    */   }
/* 100:    */   
/* 101:    */   public CTNotesMaster getXmlObject()
/* 102:    */   {
/* 103: 93 */     return this._slide;
/* 104:    */   }
/* 105:    */   
/* 106:    */   protected String getRootElementName()
/* 107:    */   {
/* 108: 98 */     return "notesMaster";
/* 109:    */   }
/* 110:    */   
/* 111:    */   public MasterSheet<XSLFShape, XSLFTextParagraph> getMasterSheet()
/* 112:    */   {
/* 113:103 */     return null;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public XSLFTheme getTheme()
/* 117:    */   {
/* 118:108 */     if (this._theme == null) {
/* 119:109 */       for (POIXMLDocumentPart p : getRelations()) {
/* 120:110 */         if ((p instanceof XSLFTheme))
/* 121:    */         {
/* 122:111 */           this._theme = ((XSLFTheme)p);
/* 123:112 */           CTColorMapping cmap = this._slide.getClrMap();
/* 124:113 */           if (cmap == null) {
/* 125:    */             break;
/* 126:    */           }
/* 127:114 */           this._theme.initColorMap(cmap); break;
/* 128:    */         }
/* 129:    */       }
/* 130:    */     }
/* 131:120 */     return this._theme;
/* 132:    */   }
/* 133:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFNotesMaster
 * JD-Core Version:    0.7.0.1
 */