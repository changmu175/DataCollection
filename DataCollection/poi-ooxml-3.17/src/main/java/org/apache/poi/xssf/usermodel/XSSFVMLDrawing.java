/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.excel.CTClientData;
/*   4:    */ import com.microsoft.schemas.office.excel.STObjectType;
/*   5:    */ import com.microsoft.schemas.office.excel.STTrueFalseBlank;
/*   6:    */ import com.microsoft.schemas.office.office.CTIdMap;
/*   7:    */ import com.microsoft.schemas.office.office.CTShapeLayout;
/*   8:    */ import com.microsoft.schemas.office.office.CTShapeLayout.Factory;
/*   9:    */ import com.microsoft.schemas.office.office.STConnectType;
/*  10:    */ import com.microsoft.schemas.office.office.STInsetMode;
/*  11:    */ import com.microsoft.schemas.vml.CTFill;
/*  12:    */ import com.microsoft.schemas.vml.CTPath;
/*  13:    */ import com.microsoft.schemas.vml.CTShadow;
/*  14:    */ import com.microsoft.schemas.vml.CTShape;
/*  15:    */ import com.microsoft.schemas.vml.CTShape.Factory;
/*  16:    */ import com.microsoft.schemas.vml.CTShapetype;
/*  17:    */ import com.microsoft.schemas.vml.CTShapetype.Factory;
/*  18:    */ import com.microsoft.schemas.vml.CTStroke;
/*  19:    */ import com.microsoft.schemas.vml.CTTextbox;
/*  20:    */ import com.microsoft.schemas.vml.STExt;
/*  21:    */ import com.microsoft.schemas.vml.STStrokeJoinStyle;
/*  22:    */ import com.microsoft.schemas.vml.STTrueFalse;
/*  23:    */ import java.io.IOException;
/*  24:    */ import java.io.InputStream;
/*  25:    */ import java.io.OutputStream;
/*  26:    */ import java.io.StringReader;
/*  27:    */ import java.math.BigInteger;
/*  28:    */ import java.util.ArrayList;
/*  29:    */ import java.util.List;
/*  30:    */ import java.util.regex.Matcher;
/*  31:    */ import java.util.regex.Pattern;
/*  32:    */ import javax.xml.namespace.QName;
/*  33:    */ import org.apache.poi.POIXMLDocumentPart;
/*  34:    */ import org.apache.poi.POIXMLTypeLoader;
/*  35:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  36:    */ import org.apache.poi.util.DocumentHelper;
/*  37:    */ import org.apache.poi.util.ReplacingInputStream;
/*  38:    */ import org.apache.xmlbeans.XmlCursor;
/*  39:    */ import org.apache.xmlbeans.XmlCursor.TokenType;
/*  40:    */ import org.apache.xmlbeans.XmlException;
/*  41:    */ import org.apache.xmlbeans.XmlInteger;
/*  42:    */ import org.apache.xmlbeans.XmlObject;
/*  43:    */ import org.apache.xmlbeans.XmlObject.Factory;
/*  44:    */ import org.apache.xmlbeans.XmlString;
/*  45:    */ import org.w3c.dom.Document;
/*  46:    */ import org.w3c.dom.Node;
/*  47:    */ import org.xml.sax.InputSource;
/*  48:    */ import org.xml.sax.SAXException;
/*  49:    */ 
/*  50:    */ public final class XSSFVMLDrawing
/*  51:    */   extends POIXMLDocumentPart
/*  52:    */ {
/*  53: 85 */   private static final QName QNAME_SHAPE_LAYOUT = new QName("urn:schemas-microsoft-com:office:office", "shapelayout");
/*  54: 86 */   private static final QName QNAME_SHAPE_TYPE = new QName("urn:schemas-microsoft-com:vml", "shapetype");
/*  55: 87 */   private static final QName QNAME_SHAPE = new QName("urn:schemas-microsoft-com:vml", "shape");
/*  56:    */   private static final String COMMENT_SHAPE_TYPE_ID = "_x0000_t202";
/*  57: 93 */   private static final Pattern ptrn_shapeId = Pattern.compile("_x0000_s(\\d+)");
/*  58: 95 */   private List<QName> _qnames = new ArrayList();
/*  59: 96 */   private List<XmlObject> _items = new ArrayList();
/*  60:    */   private String _shapeTypeId;
/*  61: 98 */   private int _shapeId = 1024;
/*  62:    */   
/*  63:    */   protected XSSFVMLDrawing()
/*  64:    */   {
/*  65:107 */     newDrawing();
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected XSSFVMLDrawing(PackagePart part)
/*  69:    */     throws IOException, XmlException
/*  70:    */   {
/*  71:119 */     super(part);
/*  72:120 */     read(getPackagePart().getInputStream());
/*  73:    */   }
/*  74:    */   
/*  75:    */   protected void read(InputStream is)
/*  76:    */     throws IOException, XmlException
/*  77:    */   {
/*  78:    */     Document doc;
/*  79:    */     try
/*  80:    */     {
/*  81:132 */       doc = DocumentHelper.readDocument(new ReplacingInputStream(is, "<br>", "<br/>"));
/*  82:    */     }
/*  83:    */     catch (SAXException e)
/*  84:    */     {
/*  85:134 */       throw new XmlException(e.getMessage(), e);
/*  86:    */     }
/*  87:136 */     XmlObject root = XmlObject.Factory.parse(doc, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  88:    */     
/*  89:138 */     this._qnames = new ArrayList();
/*  90:139 */     this._items = new ArrayList();
/*  91:140 */     for (XmlObject obj : root.selectPath("$this/xml/*"))
/*  92:    */     {
/*  93:141 */       Node nd = obj.getDomNode();
/*  94:142 */       QName qname = new QName(nd.getNamespaceURI(), nd.getLocalName());
/*  95:143 */       if (qname.equals(QNAME_SHAPE_LAYOUT))
/*  96:    */       {
/*  97:144 */         this._items.add(CTShapeLayout.Factory.parse(obj.xmlText(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS));
/*  98:    */       }
/*  99:145 */       else if (qname.equals(QNAME_SHAPE_TYPE))
/* 100:    */       {
/* 101:146 */         CTShapetype st = CTShapetype.Factory.parse(obj.xmlText(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 102:147 */         this._items.add(st);
/* 103:148 */         this._shapeTypeId = st.getId();
/* 104:    */       }
/* 105:149 */       else if (qname.equals(QNAME_SHAPE))
/* 106:    */       {
/* 107:150 */         CTShape shape = CTShape.Factory.parse(obj.xmlText(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 108:151 */         String id = shape.getId();
/* 109:152 */         if (id != null)
/* 110:    */         {
/* 111:153 */           Matcher m = ptrn_shapeId.matcher(id);
/* 112:154 */           if (m.find()) {
/* 113:155 */             this._shapeId = Math.max(this._shapeId, Integer.parseInt(m.group(1)));
/* 114:    */           }
/* 115:    */         }
/* 116:158 */         this._items.add(shape);
/* 117:    */       }
/* 118:    */       else
/* 119:    */       {
/* 120:    */         Document doc2;
/* 121:    */         try
/* 122:    */         {
/* 123:162 */           InputSource is2 = new InputSource(new StringReader(obj.xmlText()));
/* 124:163 */           doc2 = DocumentHelper.readDocument(is2);
/* 125:    */         }
/* 126:    */         catch (SAXException e)
/* 127:    */         {
/* 128:165 */           throw new XmlException(e.getMessage(), e);
/* 129:    */         }
/* 130:168 */         this._items.add(XmlObject.Factory.parse(doc2, POIXMLTypeLoader.DEFAULT_XML_OPTIONS));
/* 131:    */       }
/* 132:170 */       this._qnames.add(qname);
/* 133:    */     }
/* 134:    */   }
/* 135:    */   
/* 136:    */   protected List<XmlObject> getItems()
/* 137:    */   {
/* 138:175 */     return this._items;
/* 139:    */   }
/* 140:    */   
/* 141:    */   protected void write(OutputStream out)
/* 142:    */     throws IOException
/* 143:    */   {
/* 144:179 */     XmlObject rootObject = XmlObject.Factory.newInstance();
/* 145:180 */     XmlCursor rootCursor = rootObject.newCursor();
/* 146:181 */     rootCursor.toNextToken();
/* 147:182 */     rootCursor.beginElement("xml");
/* 148:184 */     for (int i = 0; i < this._items.size(); i++)
/* 149:    */     {
/* 150:185 */       XmlCursor xc = ((XmlObject)this._items.get(i)).newCursor();
/* 151:186 */       rootCursor.beginElement((QName)this._qnames.get(i));
/* 152:187 */       while (xc.toNextToken() == XmlCursor.TokenType.ATTR)
/* 153:    */       {
/* 154:188 */         Node anode = xc.getDomNode();
/* 155:189 */         rootCursor.insertAttributeWithValue(anode.getLocalName(), anode.getNamespaceURI(), anode.getNodeValue());
/* 156:    */       }
/* 157:191 */       xc.toStartDoc();
/* 158:192 */       xc.copyXmlContents(rootCursor);
/* 159:193 */       rootCursor.toNextToken();
/* 160:194 */       xc.dispose();
/* 161:    */     }
/* 162:196 */     rootCursor.dispose();
/* 163:    */     
/* 164:198 */     rootObject.save(out, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 165:    */   }
/* 166:    */   
/* 167:    */   protected void commit()
/* 168:    */     throws IOException
/* 169:    */   {
/* 170:203 */     PackagePart part = getPackagePart();
/* 171:204 */     OutputStream out = part.getOutputStream();
/* 172:205 */     write(out);
/* 173:206 */     out.close();
/* 174:    */   }
/* 175:    */   
/* 176:    */   private void newDrawing()
/* 177:    */   {
/* 178:213 */     CTShapeLayout layout = CTShapeLayout.Factory.newInstance();
/* 179:214 */     layout.setExt(STExt.EDIT);
/* 180:215 */     CTIdMap idmap = layout.addNewIdmap();
/* 181:216 */     idmap.setExt(STExt.EDIT);
/* 182:217 */     idmap.setData("1");
/* 183:218 */     this._items.add(layout);
/* 184:219 */     this._qnames.add(QNAME_SHAPE_LAYOUT);
/* 185:    */     
/* 186:221 */     CTShapetype shapetype = CTShapetype.Factory.newInstance();
/* 187:222 */     this._shapeTypeId = "_x0000_t202";
/* 188:223 */     shapetype.setId(this._shapeTypeId);
/* 189:224 */     shapetype.setCoordsize("21600,21600");
/* 190:225 */     shapetype.setSpt(202.0F);
/* 191:226 */     shapetype.setPath2("m,l,21600r21600,l21600,xe");
/* 192:227 */     shapetype.addNewStroke().setJoinstyle(STStrokeJoinStyle.MITER);
/* 193:228 */     CTPath path = shapetype.addNewPath();
/* 194:229 */     path.setGradientshapeok(STTrueFalse.T);
/* 195:230 */     path.setConnecttype(STConnectType.RECT);
/* 196:231 */     this._items.add(shapetype);
/* 197:232 */     this._qnames.add(QNAME_SHAPE_TYPE);
/* 198:    */   }
/* 199:    */   
/* 200:    */   protected CTShape newCommentShape()
/* 201:    */   {
/* 202:236 */     CTShape shape = CTShape.Factory.newInstance();
/* 203:237 */     shape.setId("_x0000_s" + ++this._shapeId);
/* 204:238 */     shape.setType("#" + this._shapeTypeId);
/* 205:239 */     shape.setStyle("position:absolute; visibility:hidden");
/* 206:240 */     shape.setFillcolor("#ffffe1");
/* 207:241 */     shape.setInsetmode(STInsetMode.AUTO);
/* 208:242 */     shape.addNewFill().setColor("#ffffe1");
/* 209:243 */     CTShadow shadow = shape.addNewShadow();
/* 210:244 */     shadow.setOn(STTrueFalse.T);
/* 211:245 */     shadow.setColor("black");
/* 212:246 */     shadow.setObscured(STTrueFalse.T);
/* 213:247 */     shape.addNewPath().setConnecttype(STConnectType.NONE);
/* 214:248 */     shape.addNewTextbox().setStyle("mso-direction-alt:auto");
/* 215:249 */     CTClientData cldata = shape.addNewClientData();
/* 216:250 */     cldata.setObjectType(STObjectType.NOTE);
/* 217:251 */     cldata.addNewMoveWithCells();
/* 218:252 */     cldata.addNewSizeWithCells();
/* 219:253 */     cldata.addNewAnchor().setStringValue("1, 15, 0, 2, 3, 15, 3, 16");
/* 220:254 */     cldata.addNewAutoFill().setStringValue("False");
/* 221:255 */     cldata.addNewRow().setBigIntegerValue(new BigInteger("0"));
/* 222:256 */     cldata.addNewColumn().setBigIntegerValue(new BigInteger("0"));
/* 223:257 */     this._items.add(shape);
/* 224:258 */     this._qnames.add(QNAME_SHAPE);
/* 225:259 */     return shape;
/* 226:    */   }
/* 227:    */   
/* 228:    */   protected CTShape findCommentShape(int row, int col)
/* 229:    */   {
/* 230:268 */     for (XmlObject itm : this._items) {
/* 231:269 */       if ((itm instanceof CTShape))
/* 232:    */       {
/* 233:270 */         CTShape sh = (CTShape)itm;
/* 234:271 */         if (sh.sizeOfClientDataArray() > 0)
/* 235:    */         {
/* 236:272 */           CTClientData cldata = sh.getClientDataArray(0);
/* 237:273 */           if (cldata.getObjectType() == STObjectType.NOTE)
/* 238:    */           {
/* 239:274 */             int crow = cldata.getRowArray(0).intValue();
/* 240:275 */             int ccol = cldata.getColumnArray(0).intValue();
/* 241:276 */             if ((crow == row) && (ccol == col)) {
/* 242:277 */               return sh;
/* 243:    */             }
/* 244:    */           }
/* 245:    */         }
/* 246:    */       }
/* 247:    */     }
/* 248:283 */     return null;
/* 249:    */   }
/* 250:    */   
/* 251:    */   protected boolean removeCommentShape(int row, int col)
/* 252:    */   {
/* 253:287 */     CTShape shape = findCommentShape(row, col);
/* 254:288 */     return (shape != null) && (this._items.remove(shape));
/* 255:    */   }
/* 256:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFVMLDrawing
 * JD-Core Version:    0.7.0.1
 */