/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.geom.Rectangle2D;
/*   4:    */ import java.awt.geom.Rectangle2D.Double;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Collections;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.List;
/*   9:    */ import javax.xml.namespace.QName;
/*  10:    */ import org.apache.poi.sl.draw.DrawFactory;
/*  11:    */ import org.apache.poi.sl.draw.DrawTextShape;
/*  12:    */ import org.apache.poi.sl.usermodel.TableShape;
/*  13:    */ import org.apache.poi.util.Internal;
/*  14:    */ import org.apache.poi.util.Units;
/*  15:    */ import org.apache.xmlbeans.XmlCursor;
/*  16:    */ import org.apache.xmlbeans.XmlObject;
/*  17:    */ import org.apache.xmlbeans.impl.values.XmlAnyTypeImpl;
/*  18:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectData;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking;
/*  21:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
/*  22:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties;
/*  23:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTable;
/*  24:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTable.Factory;
/*  25:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableCol;
/*  26:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableGrid;
/*  27:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableProperties;
/*  28:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow;
/*  29:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrame;
/*  30:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrame.Factory;
/*  31:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrameNonVisual;
/*  32:    */ 
/*  33:    */ public class XSLFTable
/*  34:    */   extends XSLFGraphicFrame
/*  35:    */   implements Iterable<XSLFTableRow>, TableShape<XSLFShape, XSLFTextParagraph>
/*  36:    */ {
/*  37:    */   static final String TABLE_URI = "http://schemas.openxmlformats.org/drawingml/2006/table";
/*  38:    */   static final String DRAWINGML_URI = "http://schemas.openxmlformats.org/drawingml/2006/main";
/*  39:    */   private CTTable _table;
/*  40:    */   private List<XSLFTableRow> _rows;
/*  41:    */   
/*  42:    */   XSLFTable(CTGraphicalObjectFrame shape, XSLFSheet sheet)
/*  43:    */   {
/*  44: 58 */     super(shape, sheet);
/*  45:    */     
/*  46: 60 */     CTGraphicalObjectData god = shape.getGraphic().getGraphicData();
/*  47: 61 */     XmlCursor xc = god.newCursor();
/*  48: 62 */     if (!xc.toChild("http://schemas.openxmlformats.org/drawingml/2006/main", "tbl")) {
/*  49: 63 */       throw new IllegalStateException("a:tbl element was not found in\n " + god);
/*  50:    */     }
/*  51: 66 */     XmlObject xo = xc.getObject();
/*  52: 69 */     if ((xo instanceof XmlAnyTypeImpl))
/*  53:    */     {
/*  54: 70 */       String errStr = "Schemas (*.xsb) for CTTable can't be loaded - usually this happens when OSGI loading is used and the thread context classloader has no reference to the xmlbeans classes - use POIXMLTypeLoader.setClassLoader() to set the loader, e.g. with CTTable.class.getClassLoader()";
/*  55:    */       
/*  56:    */ 
/*  57:    */ 
/*  58:    */ 
/*  59:    */ 
/*  60: 76 */       throw new IllegalStateException(errStr);
/*  61:    */     }
/*  62: 78 */     this._table = ((CTTable)xo);
/*  63: 79 */     xc.dispose();
/*  64:    */     
/*  65: 81 */     this._rows = new ArrayList(this._table.sizeOfTrArray());
/*  66: 82 */     for (CTTableRow row : this._table.getTrArray()) {
/*  67: 83 */       this._rows.add(new XSLFTableRow(row, this));
/*  68:    */     }
/*  69: 85 */     updateRowColIndexes();
/*  70:    */   }
/*  71:    */   
/*  72:    */   public XSLFTableCell getCell(int row, int col)
/*  73:    */   {
/*  74: 90 */     List<XSLFTableRow> rows = getRows();
/*  75: 91 */     if ((row < 0) || (rows.size() <= row)) {
/*  76: 92 */       return null;
/*  77:    */     }
/*  78: 94 */     XSLFTableRow r = (XSLFTableRow)rows.get(row);
/*  79: 95 */     if (r == null) {
/*  80: 97 */       return null;
/*  81:    */     }
/*  82: 99 */     List<XSLFTableCell> cells = r.getCells();
/*  83:100 */     if ((col < 0) || (cells.size() <= col)) {
/*  84:101 */       return null;
/*  85:    */     }
/*  86:104 */     return (XSLFTableCell)cells.get(col);
/*  87:    */   }
/*  88:    */   
/*  89:    */   @Internal
/*  90:    */   public CTTable getCTTable()
/*  91:    */   {
/*  92:109 */     return this._table;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public int getNumberOfColumns()
/*  96:    */   {
/*  97:114 */     return this._table.getTblGrid().sizeOfGridColArray();
/*  98:    */   }
/*  99:    */   
/* 100:    */   public int getNumberOfRows()
/* 101:    */   {
/* 102:119 */     return this._table.sizeOfTrArray();
/* 103:    */   }
/* 104:    */   
/* 105:    */   public double getColumnWidth(int idx)
/* 106:    */   {
/* 107:124 */     return Units.toPoints(this._table.getTblGrid().getGridColArray(idx).getW());
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void setColumnWidth(int idx, double width)
/* 111:    */   {
/* 112:130 */     this._table.getTblGrid().getGridColArray(idx).setW(Units.toEMU(width));
/* 113:    */   }
/* 114:    */   
/* 115:    */   public double getRowHeight(int row)
/* 116:    */   {
/* 117:135 */     return Units.toPoints(this._table.getTrArray(row).getH());
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void setRowHeight(int row, double height)
/* 121:    */   {
/* 122:140 */     this._table.getTrArray(row).setH(Units.toEMU(height));
/* 123:    */   }
/* 124:    */   
/* 125:    */   public Iterator<XSLFTableRow> iterator()
/* 126:    */   {
/* 127:144 */     return this._rows.iterator();
/* 128:    */   }
/* 129:    */   
/* 130:    */   public List<XSLFTableRow> getRows()
/* 131:    */   {
/* 132:148 */     return Collections.unmodifiableList(this._rows);
/* 133:    */   }
/* 134:    */   
/* 135:    */   public XSLFTableRow addRow()
/* 136:    */   {
/* 137:152 */     CTTableRow tr = this._table.addNewTr();
/* 138:153 */     XSLFTableRow row = new XSLFTableRow(tr, this);
/* 139:    */     
/* 140:155 */     row.setHeight(20.0D);
/* 141:156 */     this._rows.add(row);
/* 142:157 */     updateRowColIndexes();
/* 143:158 */     return row;
/* 144:    */   }
/* 145:    */   
/* 146:    */   static CTGraphicalObjectFrame prototype(int shapeId)
/* 147:    */   {
/* 148:162 */     CTGraphicalObjectFrame frame = CTGraphicalObjectFrame.Factory.newInstance();
/* 149:163 */     CTGraphicalObjectFrameNonVisual nvGr = frame.addNewNvGraphicFramePr();
/* 150:    */     
/* 151:165 */     CTNonVisualDrawingProps cnv = nvGr.addNewCNvPr();
/* 152:166 */     cnv.setName("Table " + shapeId);
/* 153:167 */     cnv.setId(shapeId + 1);
/* 154:168 */     nvGr.addNewCNvGraphicFramePr().addNewGraphicFrameLocks().setNoGrp(true);
/* 155:169 */     nvGr.addNewNvPr();
/* 156:    */     
/* 157:171 */     frame.addNewXfrm();
/* 158:172 */     CTGraphicalObjectData gr = frame.addNewGraphic().addNewGraphicData();
/* 159:173 */     XmlCursor grCur = gr.newCursor();
/* 160:174 */     grCur.toNextToken();
/* 161:175 */     grCur.beginElement(new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tbl"));
/* 162:    */     
/* 163:177 */     CTTable tbl = CTTable.Factory.newInstance();
/* 164:178 */     tbl.addNewTblPr();
/* 165:179 */     tbl.addNewTblGrid();
/* 166:180 */     XmlCursor tblCur = tbl.newCursor();
/* 167:    */     
/* 168:182 */     tblCur.moveXmlContents(grCur);
/* 169:183 */     tblCur.dispose();
/* 170:184 */     grCur.dispose();
/* 171:185 */     gr.setUri("http://schemas.openxmlformats.org/drawingml/2006/table");
/* 172:186 */     return frame;
/* 173:    */   }
/* 174:    */   
/* 175:    */   public void mergeCells(int firstRow, int lastRow, int firstCol, int lastCol)
/* 176:    */   {
/* 177:194 */     if (firstRow > lastRow) {
/* 178:195 */       throw new IllegalArgumentException("Cannot merge, first row > last row : " + firstRow + " > " + lastRow);
/* 179:    */     }
/* 180:201 */     if (firstCol > lastCol) {
/* 181:202 */       throw new IllegalArgumentException("Cannot merge, first column > last column : " + firstCol + " > " + lastCol);
/* 182:    */     }
/* 183:208 */     int rowSpan = lastRow - firstRow + 1;
/* 184:209 */     boolean mergeRowRequired = rowSpan > 1;
/* 185:    */     
/* 186:211 */     int colSpan = lastCol - firstCol + 1;
/* 187:212 */     boolean mergeColumnRequired = colSpan > 1;
/* 188:214 */     for (int i = firstRow; i <= lastRow; i++)
/* 189:    */     {
/* 190:216 */       XSLFTableRow row = (XSLFTableRow)this._rows.get(i);
/* 191:218 */       for (int colPos = firstCol; colPos <= lastCol; colPos++)
/* 192:    */       {
/* 193:220 */         XSLFTableCell cell = (XSLFTableCell)row.getCells().get(colPos);
/* 194:222 */         if (mergeRowRequired) {
/* 195:223 */           if (i == firstRow) {
/* 196:224 */             cell.setRowSpan(rowSpan);
/* 197:    */           } else {
/* 198:226 */             cell.setVMerge(true);
/* 199:    */           }
/* 200:    */         }
/* 201:229 */         if (mergeColumnRequired) {
/* 202:230 */           if (colPos == firstCol) {
/* 203:231 */             cell.setGridSpan(colSpan);
/* 204:    */           } else {
/* 205:233 */             cell.setHMerge(true);
/* 206:    */           }
/* 207:    */         }
/* 208:    */       }
/* 209:    */     }
/* 210:    */   }
/* 211:    */   
/* 212:    */   protected XSLFTableStyle getTableStyle()
/* 213:    */   {
/* 214:248 */     CTTable tab = getCTTable();
/* 215:250 */     if ((!tab.isSetTblPr()) || (!tab.getTblPr().isSetTableStyleId())) {
/* 216:251 */       return null;
/* 217:    */     }
/* 218:254 */     String styleId = tab.getTblPr().getTableStyleId();
/* 219:255 */     XSLFTableStyles styles = getSheet().getSlideShow().getTableStyles();
/* 220:256 */     for (XSLFTableStyle style : styles.getStyles()) {
/* 221:257 */       if (style.getStyleId().equals(styleId)) {
/* 222:258 */         return style;
/* 223:    */       }
/* 224:    */     }
/* 225:261 */     return null;
/* 226:    */   }
/* 227:    */   
/* 228:    */   void updateRowColIndexes()
/* 229:    */   {
/* 230:265 */     int rowIdx = 0;
/* 231:266 */     for (XSLFTableRow xr : this)
/* 232:    */     {
/* 233:267 */       int colIdx = 0;
/* 234:268 */       for (XSLFTableCell tc : xr)
/* 235:    */       {
/* 236:269 */         tc.setRowColIndex(rowIdx, colIdx);
/* 237:270 */         colIdx++;
/* 238:    */       }
/* 239:272 */       rowIdx++;
/* 240:    */     }
/* 241:    */   }
/* 242:    */   
/* 243:    */   void updateCellAnchor()
/* 244:    */   {
/* 245:277 */     int rows = getNumberOfRows();
/* 246:278 */     int cols = getNumberOfColumns();
/* 247:    */     
/* 248:280 */     double[] colWidths = new double[cols];
/* 249:281 */     double[] rowHeights = new double[rows];
/* 250:283 */     for (int row = 0; row < rows; row++) {
/* 251:284 */       rowHeights[row] = getRowHeight(row);
/* 252:    */     }
/* 253:286 */     for (int col = 0; col < cols; col++) {
/* 254:287 */       colWidths[col] = getColumnWidth(col);
/* 255:    */     }
/* 256:290 */     Rectangle2D tblAnc = getAnchor();
/* 257:291 */     DrawFactory df = DrawFactory.getInstance(null);
/* 258:    */     
/* 259:293 */     double newY = tblAnc.getY();
/* 260:296 */     for (int row = 0; row < rows; row++)
/* 261:    */     {
/* 262:297 */       double maxHeight = 0.0D;
/* 263:298 */       for (int col = 0; col < cols; col++)
/* 264:    */       {
/* 265:299 */         XSLFTableCell tc = getCell(row, col);
/* 266:300 */         if ((tc != null) && (tc.getGridSpan() == 1) && (tc.getRowSpan() == 1))
/* 267:    */         {
/* 268:304 */           tc.setAnchor(new Double(0.0D, 0.0D, colWidths[col], 0.0D));
/* 269:305 */           DrawTextShape dts = df.getDrawable(tc);
/* 270:306 */           maxHeight = Math.max(maxHeight, dts.getTextHeight());
/* 271:    */         }
/* 272:    */       }
/* 273:308 */       rowHeights[row] = Math.max(rowHeights[row], maxHeight);
/* 274:    */     }
/* 275:312 */     for (int row = 0; row < rows; row++)
/* 276:    */     {
/* 277:313 */       double newX = tblAnc.getX();
/* 278:314 */       for (int col = 0; col < cols; col++)
/* 279:    */       {
/* 280:315 */         Rectangle2D bounds = new Double(newX, newY, colWidths[col], rowHeights[row]);
/* 281:316 */         XSLFTableCell tc = getCell(row, col);
/* 282:317 */         if (tc != null)
/* 283:    */         {
/* 284:318 */           tc.setAnchor(bounds);
/* 285:319 */           newX += colWidths[col] + 2.0D;
/* 286:    */         }
/* 287:    */       }
/* 288:322 */       newY += rowHeights[row] + 2.0D;
/* 289:    */     }
/* 290:326 */     for (int row = 0; row < rows; row++) {
/* 291:327 */       for (int col = 0; col < cols; col++)
/* 292:    */       {
/* 293:328 */         XSLFTableCell tc = getCell(row, col);
/* 294:329 */         if (tc != null)
/* 295:    */         {
/* 296:332 */           Rectangle2D mergedBounds = tc.getAnchor();
/* 297:333 */           for (int col2 = col + 1; col2 < col + tc.getGridSpan(); col2++)
/* 298:    */           {
/* 299:334 */             assert (col2 < cols);
/* 300:335 */             XSLFTableCell tc2 = getCell(row, col2);
/* 301:336 */             assert ((tc2.getGridSpan() == 1) && (tc2.getRowSpan() == 1));
/* 302:337 */             mergedBounds.add(tc2.getAnchor());
/* 303:    */           }
/* 304:339 */           for (int row2 = row + 1; row2 < row + tc.getRowSpan(); row2++)
/* 305:    */           {
/* 306:340 */             assert (row2 < rows);
/* 307:341 */             XSLFTableCell tc2 = getCell(row2, col);
/* 308:342 */             assert ((tc2.getGridSpan() == 1) && (tc2.getRowSpan() == 1));
/* 309:343 */             mergedBounds.add(tc2.getAnchor());
/* 310:    */           }
/* 311:345 */           tc.setAnchor(mergedBounds);
/* 312:    */         }
/* 313:    */       }
/* 314:    */     }
/* 315:    */   }
/* 316:    */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFTable

 * JD-Core Version:    0.7.0.1

 */