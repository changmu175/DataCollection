/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.List;
/*   6:    */ import javax.xml.namespace.QName;
/*   7:    */ import org.apache.poi.POIXMLException;
/*   8:    */ import org.apache.poi.POIXMLTypeLoader;
/*   9:    */ import org.apache.poi.util.Removal;
/*  10:    */ import org.apache.xmlbeans.SchemaType;
/*  11:    */ import org.apache.xmlbeans.XmlCursor;
/*  12:    */ import org.apache.xmlbeans.XmlException;
/*  13:    */ import org.apache.xmlbeans.XmlObject;
/*  14:    */ import org.apache.xmlbeans.impl.values.XmlAnyTypeImpl;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
/*  16:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectData;
/*  17:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTable;
/*  18:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTable.Factory;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
/*  20:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTApplicationNonVisualDrawingProps;
/*  21:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData;
/*  22:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrame;
/*  23:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;
/*  24:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;
/*  25:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTShapeNonVisual;
/*  26:    */ 
/*  27:    */ @Removal(version="3.18")
/*  28:    */ public class XSLFCommonSlideData
/*  29:    */ {
/*  30:    */   private final CTCommonSlideData data;
/*  31:    */   
/*  32:    */   public XSLFCommonSlideData(CTCommonSlideData data)
/*  33:    */   {
/*  34: 51 */     this.data = data;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public List<DrawingTextBody> getDrawingText()
/*  38:    */   {
/*  39: 55 */     CTGroupShape gs = this.data.getSpTree();
/*  40:    */     
/*  41: 57 */     List<DrawingTextBody> out = new ArrayList();
/*  42:    */     
/*  43: 59 */     processShape(gs, out);
/*  44: 61 */     for (CTGroupShape shape : gs.getGrpSpArray()) {
/*  45: 62 */       processShape(shape, out);
/*  46:    */     }
/*  47: 65 */     for (CTGraphicalObjectFrame frame : gs.getGraphicFrameArray())
/*  48:    */     {
/*  49: 66 */       CTGraphicalObjectData data = frame.getGraphic().getGraphicData();
/*  50: 67 */       XmlCursor c = data.newCursor();
/*  51: 68 */       c.selectPath("declare namespace pic='" + CTTable.type.getName().getNamespaceURI() + "' .//pic:tbl");
/*  52: 70 */       while (c.toNextSelection())
/*  53:    */       {
/*  54: 71 */         XmlObject o = c.getObject();
/*  55: 73 */         if ((o instanceof XmlAnyTypeImpl)) {
/*  56:    */           try
/*  57:    */           {
/*  58: 76 */             o = CTTable.Factory.parse(o.toString(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  59:    */           }
/*  60:    */           catch (XmlException e)
/*  61:    */           {
/*  62: 78 */             throw new POIXMLException(e);
/*  63:    */           }
/*  64:    */         }
/*  65: 82 */         if ((o instanceof CTTable))
/*  66:    */         {
/*  67: 83 */           DrawingTable table = new DrawingTable((CTTable)o);
/*  68: 85 */           for (DrawingTableRow row : table.getRows()) {
/*  69: 86 */             for (DrawingTableCell cell : row.getCells())
/*  70:    */             {
/*  71: 87 */               DrawingTextBody textBody = cell.getTextBody();
/*  72: 88 */               out.add(textBody);
/*  73:    */             }
/*  74:    */           }
/*  75:    */         }
/*  76:    */       }
/*  77: 94 */       c.dispose();
/*  78:    */     }
/*  79: 97 */     return out;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public List<DrawingParagraph> getText()
/*  83:    */   {
/*  84:100 */     List<DrawingParagraph> paragraphs = new ArrayList();
/*  85:101 */     for (DrawingTextBody textBody : getDrawingText()) {
/*  86:102 */       paragraphs.addAll(Arrays.asList(textBody.getParagraphs()));
/*  87:    */     }
/*  88:104 */     return paragraphs;
/*  89:    */   }
/*  90:    */   
/*  91:    */   private void processShape(CTGroupShape gs, List<DrawingTextBody> out)
/*  92:    */   {
/*  93:108 */     for (CTShape shape : gs.getSpArray())
/*  94:    */     {
/*  95:109 */       CTTextBody ctTextBody = shape.getTxBody();
/*  96:110 */       if (ctTextBody != null)
/*  97:    */       {
/*  98:115 */         CTApplicationNonVisualDrawingProps nvpr = shape.getNvSpPr().getNvPr();
/*  99:    */         DrawingTextBody textBody;
/* 100:    */         DrawingTextBody textBody;
/* 101:116 */         if (nvpr.isSetPh()) {
/* 102:117 */           textBody = new DrawingTextPlaceholder(ctTextBody, nvpr.getPh());
/* 103:    */         } else {
/* 104:119 */           textBody = new DrawingTextBody(ctTextBody);
/* 105:    */         }
/* 106:122 */         out.add(textBody);
/* 107:    */       }
/* 108:    */     }
/* 109:    */   }
/* 110:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFCommonSlideData
 * JD-Core Version:    0.7.0.1
 */