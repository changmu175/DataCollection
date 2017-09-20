/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import javax.xml.namespace.QName;
/*   4:    */ import org.apache.poi.POIXMLDocumentPart;
/*   5:    */ import org.apache.poi.util.Internal;
/*   6:    */ import org.apache.xmlbeans.SchemaType;
/*   7:    */ import org.apache.xmlbeans.XmlCursor;
/*   8:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
/*   9:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectData;
/*  10:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
/*  11:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
/*  13:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*  14:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame;
/*  16:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame.Factory;
/*  17:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrameNonVisual;
/*  18:    */ import org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId;
/*  19:    */ import org.w3c.dom.NamedNodeMap;
/*  20:    */ import org.w3c.dom.Node;
/*  21:    */ import org.w3c.dom.NodeList;
/*  22:    */ 
/*  23:    */ public final class XSSFGraphicFrame
/*  24:    */   extends XSSFShape
/*  25:    */ {
/*  26: 46 */   private static CTGraphicalObjectFrame prototype = null;
/*  27:    */   private CTGraphicalObjectFrame graphicFrame;
/*  28:    */   
/*  29:    */   protected XSSFGraphicFrame(XSSFDrawing drawing, CTGraphicalObjectFrame ctGraphicFrame)
/*  30:    */   {
/*  31: 57 */     this.drawing = drawing;
/*  32: 58 */     this.graphicFrame = ctGraphicFrame;
/*  33:    */     
/*  34: 60 */     CTGraphicalObjectData graphicData = this.graphicFrame.getGraphic().getGraphicData();
/*  35: 61 */     if (graphicData != null)
/*  36:    */     {
/*  37: 62 */       NodeList nodes = graphicData.getDomNode().getChildNodes();
/*  38: 63 */       for (int i = 0; i < nodes.getLength(); i++)
/*  39:    */       {
/*  40: 64 */         Node node = nodes.item(i);
/*  41: 66 */         if (node.getNodeName().equals("c:chart"))
/*  42:    */         {
/*  43: 68 */           POIXMLDocumentPart relation = drawing.getRelationById(node.getAttributes().getNamedItem("r:id").getNodeValue());
/*  44: 70 */           if ((relation instanceof XSSFChart)) {
/*  45: 71 */             ((XSSFChart)relation).setGraphicFrame(this);
/*  46:    */           }
/*  47:    */         }
/*  48:    */       }
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   @Internal
/*  53:    */   public CTGraphicalObjectFrame getCTGraphicalObjectFrame()
/*  54:    */   {
/*  55: 80 */     return this.graphicFrame;
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected static CTGraphicalObjectFrame prototype()
/*  59:    */   {
/*  60: 87 */     if (prototype == null)
/*  61:    */     {
/*  62: 88 */       CTGraphicalObjectFrame graphicFrame = CTGraphicalObjectFrame.Factory.newInstance();
/*  63:    */       
/*  64: 90 */       CTGraphicalObjectFrameNonVisual nvGraphic = graphicFrame.addNewNvGraphicFramePr();
/*  65: 91 */       CTNonVisualDrawingProps props = nvGraphic.addNewCNvPr();
/*  66: 92 */       props.setId(0L);
/*  67: 93 */       props.setName("Diagramm 1");
/*  68: 94 */       nvGraphic.addNewCNvGraphicFramePr();
/*  69:    */       
/*  70: 96 */       CTTransform2D transform = graphicFrame.addNewXfrm();
/*  71: 97 */       CTPositiveSize2D extPoint = transform.addNewExt();
/*  72: 98 */       CTPoint2D offPoint = transform.addNewOff();
/*  73:    */       
/*  74:100 */       extPoint.setCx(0L);
/*  75:101 */       extPoint.setCy(0L);
/*  76:102 */       offPoint.setX(0L);
/*  77:103 */       offPoint.setY(0L);
/*  78:    */       
/*  79:105 */       graphicFrame.addNewGraphic();
/*  80:    */       
/*  81:107 */       prototype = graphicFrame;
/*  82:    */     }
/*  83:109 */     return prototype;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setMacro(String macro)
/*  87:    */   {
/*  88:116 */     this.graphicFrame.setMacro(macro);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void setName(String name)
/*  92:    */   {
/*  93:123 */     getNonVisualProperties().setName(name);
/*  94:    */   }
/*  95:    */   
/*  96:    */   public String getName()
/*  97:    */   {
/*  98:131 */     return getNonVisualProperties().getName();
/*  99:    */   }
/* 100:    */   
/* 101:    */   private CTNonVisualDrawingProps getNonVisualProperties()
/* 102:    */   {
/* 103:135 */     CTGraphicalObjectFrameNonVisual nvGraphic = this.graphicFrame.getNvGraphicFramePr();
/* 104:136 */     return nvGraphic.getCNvPr();
/* 105:    */   }
/* 106:    */   
/* 107:    */   protected void setAnchor(XSSFClientAnchor anchor)
/* 108:    */   {
/* 109:143 */     this.anchor = anchor;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public XSSFClientAnchor getAnchor()
/* 113:    */   {
/* 114:151 */     return (XSSFClientAnchor)this.anchor;
/* 115:    */   }
/* 116:    */   
/* 117:    */   protected void setChart(XSSFChart chart, String relId)
/* 118:    */   {
/* 119:158 */     CTGraphicalObjectData data = this.graphicFrame.getGraphic().addNewGraphicData();
/* 120:159 */     appendChartElement(data, relId);
/* 121:160 */     chart.setGraphicFrame(this);
/* 122:    */   }
/* 123:    */   
/* 124:    */   public long getId()
/* 125:    */   {
/* 126:168 */     return this.graphicFrame.getNvGraphicFramePr().getCNvPr().getId();
/* 127:    */   }
/* 128:    */   
/* 129:    */   protected void setId(long id)
/* 130:    */   {
/* 131:175 */     this.graphicFrame.getNvGraphicFramePr().getCNvPr().setId(id);
/* 132:    */   }
/* 133:    */   
/* 134:    */   private void appendChartElement(CTGraphicalObjectData data, String id)
/* 135:    */   {
/* 136:195 */     String r_namespaceUri = STRelationshipId.type.getName().getNamespaceURI();
/* 137:196 */     String c_namespaceUri = "http://schemas.openxmlformats.org/drawingml/2006/chart";
/* 138:197 */     XmlCursor cursor = data.newCursor();
/* 139:198 */     cursor.toNextToken();
/* 140:199 */     cursor.beginElement(new QName(c_namespaceUri, "chart", "c"));
/* 141:200 */     cursor.insertAttributeWithValue(new QName(r_namespaceUri, "id", "r"), id);
/* 142:201 */     cursor.dispose();
/* 143:202 */     data.setUri(c_namespaceUri);
/* 144:    */   }
/* 145:    */   
/* 146:    */   protected CTShapeProperties getShapeProperties()
/* 147:    */   {
/* 148:207 */     return null;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public String getShapeName()
/* 152:    */   {
/* 153:212 */     return this.graphicFrame.getNvGraphicFramePr().getCNvPr().getName();
/* 154:    */   }
/* 155:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFGraphicFrame
 * JD-Core Version:    0.7.0.1
 */