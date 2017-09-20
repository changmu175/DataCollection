/*   1:    */ package org.apache.poi.xwpf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.List;
/*   6:    */ import javax.xml.namespace.QName;
/*   7:    */ import org.apache.poi.POIXMLDocumentPart;
/*   8:    */ import org.apache.xmlbeans.SchemaType;
/*   9:    */ import org.apache.xmlbeans.XmlCursor;
/*  10:    */ import org.apache.xmlbeans.XmlObject;
/*  11:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn;
/*  12:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
/*  13:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
/*  14:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtBlock;
/*  15:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
/*  16:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
/*  17:    */ 
/*  18:    */ public class XWPFFootnote
/*  19:    */   implements Iterable<XWPFParagraph>, IBody
/*  20:    */ {
/*  21: 35 */   private List<XWPFParagraph> paragraphs = new ArrayList();
/*  22: 36 */   private List<XWPFTable> tables = new ArrayList();
/*  23: 37 */   private List<XWPFPictureData> pictures = new ArrayList();
/*  24: 38 */   private List<IBodyElement> bodyElements = new ArrayList();
/*  25:    */   private CTFtnEdn ctFtnEdn;
/*  26:    */   private XWPFFootnotes footnotes;
/*  27:    */   private XWPFDocument document;
/*  28:    */   
/*  29:    */   public XWPFFootnote(CTFtnEdn note, XWPFFootnotes xFootnotes)
/*  30:    */   {
/*  31: 45 */     this.footnotes = xFootnotes;
/*  32: 46 */     this.ctFtnEdn = note;
/*  33: 47 */     this.document = xFootnotes.getXWPFDocument();
/*  34: 48 */     init();
/*  35:    */   }
/*  36:    */   
/*  37:    */   public XWPFFootnote(XWPFDocument document, CTFtnEdn body)
/*  38:    */   {
/*  39: 52 */     this.ctFtnEdn = body;
/*  40: 53 */     this.document = document;
/*  41: 54 */     init();
/*  42:    */   }
/*  43:    */   
/*  44:    */   private void init()
/*  45:    */   {
/*  46: 58 */     XmlCursor cursor = this.ctFtnEdn.newCursor();
/*  47:    */     
/*  48:    */ 
/*  49: 61 */     cursor.selectPath("./*");
/*  50: 62 */     while (cursor.toNextSelection())
/*  51:    */     {
/*  52: 63 */       XmlObject o = cursor.getObject();
/*  53: 64 */       if ((o instanceof CTP))
/*  54:    */       {
/*  55: 65 */         XWPFParagraph p = new XWPFParagraph((CTP)o, this);
/*  56: 66 */         this.bodyElements.add(p);
/*  57: 67 */         this.paragraphs.add(p);
/*  58:    */       }
/*  59: 68 */       else if ((o instanceof CTTbl))
/*  60:    */       {
/*  61: 69 */         XWPFTable t = new XWPFTable((CTTbl)o, this);
/*  62: 70 */         this.bodyElements.add(t);
/*  63: 71 */         this.tables.add(t);
/*  64:    */       }
/*  65: 72 */       else if ((o instanceof CTSdtBlock))
/*  66:    */       {
/*  67: 73 */         XWPFSDT c = new XWPFSDT((CTSdtBlock)o, this);
/*  68: 74 */         this.bodyElements.add(c);
/*  69:    */       }
/*  70:    */     }
/*  71: 78 */     cursor.dispose();
/*  72:    */   }
/*  73:    */   
/*  74:    */   public List<XWPFParagraph> getParagraphs()
/*  75:    */   {
/*  76: 82 */     return this.paragraphs;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public Iterator<XWPFParagraph> iterator()
/*  80:    */   {
/*  81: 86 */     return this.paragraphs.iterator();
/*  82:    */   }
/*  83:    */   
/*  84:    */   public List<XWPFTable> getTables()
/*  85:    */   {
/*  86: 90 */     return this.tables;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public List<XWPFPictureData> getPictures()
/*  90:    */   {
/*  91: 94 */     return this.pictures;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public List<IBodyElement> getBodyElements()
/*  95:    */   {
/*  96: 98 */     return this.bodyElements;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public CTFtnEdn getCTFtnEdn()
/* 100:    */   {
/* 101:102 */     return this.ctFtnEdn;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setCTFtnEdn(CTFtnEdn footnote)
/* 105:    */   {
/* 106:106 */     this.ctFtnEdn = footnote;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public XWPFTable getTableArray(int pos)
/* 110:    */   {
/* 111:115 */     if ((pos >= 0) && (pos < this.tables.size())) {
/* 112:116 */       return (XWPFTable)this.tables.get(pos);
/* 113:    */     }
/* 114:118 */     return null;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public void insertTable(int pos, XWPFTable table)
/* 118:    */   {
/* 119:129 */     this.bodyElements.add(pos, table);
/* 120:130 */     int i = 0;
/* 121:131 */     for (CTTbl tbl : this.ctFtnEdn.getTblArray())
/* 122:    */     {
/* 123:132 */       if (tbl == table.getCTTbl()) {
/* 124:    */         break;
/* 125:    */       }
/* 126:135 */       i++;
/* 127:    */     }
/* 128:137 */     this.tables.add(i, table);
/* 129:    */   }
/* 130:    */   
/* 131:    */   public XWPFTable getTable(CTTbl ctTable)
/* 132:    */   {
/* 133:150 */     for (XWPFTable table : this.tables)
/* 134:    */     {
/* 135:151 */       if (table == null) {
/* 136:152 */         return null;
/* 137:    */       }
/* 138:153 */       if (table.getCTTbl().equals(ctTable)) {
/* 139:154 */         return table;
/* 140:    */       }
/* 141:    */     }
/* 142:156 */     return null;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public XWPFParagraph getParagraph(CTP p)
/* 146:    */   {
/* 147:170 */     for (XWPFParagraph paragraph : this.paragraphs) {
/* 148:171 */       if (paragraph.getCTP().equals(p)) {
/* 149:172 */         return paragraph;
/* 150:    */       }
/* 151:    */     }
/* 152:174 */     return null;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public XWPFParagraph getParagraphArray(int pos)
/* 156:    */   {
/* 157:184 */     if ((pos >= 0) && (pos < this.paragraphs.size())) {
/* 158:185 */       return (XWPFParagraph)this.paragraphs.get(pos);
/* 159:    */     }
/* 160:187 */     return null;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public XWPFTableCell getTableCell(CTTc cell)
/* 164:    */   {
/* 165:197 */     XmlCursor cursor = cell.newCursor();
/* 166:198 */     cursor.toParent();
/* 167:199 */     XmlObject o = cursor.getObject();
/* 168:200 */     if (!(o instanceof CTRow)) {
/* 169:201 */       return null;
/* 170:    */     }
/* 171:203 */     CTRow row = (CTRow)o;
/* 172:204 */     cursor.toParent();
/* 173:205 */     o = cursor.getObject();
/* 174:206 */     cursor.dispose();
/* 175:207 */     if (!(o instanceof CTTbl)) {
/* 176:208 */       return null;
/* 177:    */     }
/* 178:210 */     CTTbl tbl = (CTTbl)o;
/* 179:211 */     XWPFTable table = getTable(tbl);
/* 180:212 */     if (table == null) {
/* 181:213 */       return null;
/* 182:    */     }
/* 183:215 */     XWPFTableRow tableRow = table.getRow(row);
/* 184:216 */     if (tableRow == null) {
/* 185:217 */       return null;
/* 186:    */     }
/* 187:219 */     return tableRow.getTableCell(cell);
/* 188:    */   }
/* 189:    */   
/* 190:    */   private boolean isCursorInFtn(XmlCursor cursor)
/* 191:    */   {
/* 192:228 */     XmlCursor verify = cursor.newCursor();
/* 193:229 */     verify.toParent();
/* 194:230 */     if (verify.getObject() == this.ctFtnEdn) {
/* 195:231 */       return true;
/* 196:    */     }
/* 197:233 */     return false;
/* 198:    */   }
/* 199:    */   
/* 200:    */   public POIXMLDocumentPart getOwner()
/* 201:    */   {
/* 202:237 */     return this.footnotes;
/* 203:    */   }
/* 204:    */   
/* 205:    */   public XWPFTable insertNewTbl(XmlCursor cursor)
/* 206:    */   {
/* 207:246 */     if (isCursorInFtn(cursor))
/* 208:    */     {
/* 209:247 */       String uri = CTTbl.type.getName().getNamespaceURI();
/* 210:248 */       String localPart = "tbl";
/* 211:249 */       cursor.beginElement(localPart, uri);
/* 212:250 */       cursor.toParent();
/* 213:251 */       CTTbl t = (CTTbl)cursor.getObject();
/* 214:252 */       XWPFTable newT = new XWPFTable(t, this);
/* 215:253 */       cursor.removeXmlContents();
/* 216:254 */       XmlObject o = null;
/* 217:255 */       while ((!(o instanceof CTTbl)) && (cursor.toPrevSibling())) {
/* 218:256 */         o = cursor.getObject();
/* 219:    */       }
/* 220:258 */       if (!(o instanceof CTTbl))
/* 221:    */       {
/* 222:259 */         this.tables.add(0, newT);
/* 223:    */       }
/* 224:    */       else
/* 225:    */       {
/* 226:261 */         int pos = this.tables.indexOf(getTable((CTTbl)o)) + 1;
/* 227:262 */         this.tables.add(pos, newT);
/* 228:    */       }
/* 229:264 */       int i = 0;
/* 230:265 */       cursor = t.newCursor();
/* 231:266 */       while (cursor.toPrevSibling())
/* 232:    */       {
/* 233:267 */         o = cursor.getObject();
/* 234:268 */         if (((o instanceof CTP)) || ((o instanceof CTTbl))) {
/* 235:269 */           i++;
/* 236:    */         }
/* 237:    */       }
/* 238:271 */       this.bodyElements.add(i, newT);
/* 239:272 */       XmlCursor c2 = t.newCursor();
/* 240:273 */       cursor.toCursor(c2);
/* 241:274 */       cursor.toEndToken();
/* 242:275 */       c2.dispose();
/* 243:276 */       return newT;
/* 244:    */     }
/* 245:278 */     return null;
/* 246:    */   }
/* 247:    */   
/* 248:    */   public XWPFParagraph insertNewParagraph(XmlCursor cursor)
/* 249:    */   {
/* 250:289 */     if (isCursorInFtn(cursor))
/* 251:    */     {
/* 252:290 */       String uri = CTP.type.getName().getNamespaceURI();
/* 253:291 */       String localPart = "p";
/* 254:292 */       cursor.beginElement(localPart, uri);
/* 255:293 */       cursor.toParent();
/* 256:294 */       CTP p = (CTP)cursor.getObject();
/* 257:295 */       XWPFParagraph newP = new XWPFParagraph(p, this);
/* 258:296 */       XmlObject o = null;
/* 259:297 */       while ((!(o instanceof CTP)) && (cursor.toPrevSibling())) {
/* 260:298 */         o = cursor.getObject();
/* 261:    */       }
/* 262:300 */       if ((!(o instanceof CTP)) || ((CTP)o == p))
/* 263:    */       {
/* 264:301 */         this.paragraphs.add(0, newP);
/* 265:    */       }
/* 266:    */       else
/* 267:    */       {
/* 268:303 */         int pos = this.paragraphs.indexOf(getParagraph((CTP)o)) + 1;
/* 269:304 */         this.paragraphs.add(pos, newP);
/* 270:    */       }
/* 271:306 */       int i = 0;
/* 272:307 */       XmlCursor p2 = p.newCursor();
/* 273:308 */       cursor.toCursor(p2);
/* 274:309 */       p2.dispose();
/* 275:310 */       while (cursor.toPrevSibling())
/* 276:    */       {
/* 277:311 */         o = cursor.getObject();
/* 278:312 */         if (((o instanceof CTP)) || ((o instanceof CTTbl))) {
/* 279:313 */           i++;
/* 280:    */         }
/* 281:    */       }
/* 282:315 */       this.bodyElements.add(i, newP);
/* 283:316 */       p2 = p.newCursor();
/* 284:317 */       cursor.toCursor(p2);
/* 285:318 */       cursor.toEndToken();
/* 286:319 */       p2.dispose();
/* 287:320 */       return newP;
/* 288:    */     }
/* 289:322 */     return null;
/* 290:    */   }
/* 291:    */   
/* 292:    */   public XWPFTable addNewTbl(CTTbl table)
/* 293:    */   {
/* 294:332 */     CTTbl newTable = this.ctFtnEdn.addNewTbl();
/* 295:333 */     newTable.set(table);
/* 296:334 */     XWPFTable xTable = new XWPFTable(newTable, this);
/* 297:335 */     this.tables.add(xTable);
/* 298:336 */     return xTable;
/* 299:    */   }
/* 300:    */   
/* 301:    */   public XWPFParagraph addNewParagraph(CTP paragraph)
/* 302:    */   {
/* 303:346 */     CTP newPara = this.ctFtnEdn.addNewP();
/* 304:347 */     newPara.set(paragraph);
/* 305:348 */     XWPFParagraph xPara = new XWPFParagraph(newPara, this);
/* 306:349 */     this.paragraphs.add(xPara);
/* 307:350 */     return xPara;
/* 308:    */   }
/* 309:    */   
/* 310:    */   public XWPFDocument getXWPFDocument()
/* 311:    */   {
/* 312:357 */     return this.document;
/* 313:    */   }
/* 314:    */   
/* 315:    */   public POIXMLDocumentPart getPart()
/* 316:    */   {
/* 317:366 */     return this.footnotes;
/* 318:    */   }
/* 319:    */   
/* 320:    */   public BodyType getPartType()
/* 321:    */   {
/* 322:375 */     return BodyType.FOOTNOTE;
/* 323:    */   }
/* 324:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFFootnote
 * JD-Core Version:    0.7.0.1
 */