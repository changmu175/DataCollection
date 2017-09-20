/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.FileNotFoundException;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.HashSet;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.List;
/*   9:    */ import java.util.Map;
/*  10:    */ import java.util.Set;
/*  11:    */ import org.apache.poi.ddf.EscherComplexProperty;
/*  12:    */ import org.apache.poi.ddf.EscherContainerRecord;
/*  13:    */ import org.apache.poi.ddf.EscherDgRecord;
/*  14:    */ import org.apache.poi.ddf.EscherOptRecord;
/*  15:    */ import org.apache.poi.ddf.EscherProperty;
/*  16:    */ import org.apache.poi.ddf.EscherSpRecord;
/*  17:    */ import org.apache.poi.ddf.EscherSpgrRecord;
/*  18:    */ import org.apache.poi.hssf.model.DrawingManager2;
/*  19:    */ import org.apache.poi.hssf.model.InternalWorkbook;
/*  20:    */ import org.apache.poi.hssf.record.CommonObjectDataSubRecord;
/*  21:    */ import org.apache.poi.hssf.record.EmbeddedObjectRefSubRecord;
/*  22:    */ import org.apache.poi.hssf.record.EndSubRecord;
/*  23:    */ import org.apache.poi.hssf.record.EscherAggregate;
/*  24:    */ import org.apache.poi.hssf.record.FtCfSubRecord;
/*  25:    */ import org.apache.poi.hssf.record.FtPioGrbitSubRecord;
/*  26:    */ import org.apache.poi.hssf.record.NoteRecord;
/*  27:    */ import org.apache.poi.hssf.record.ObjRecord;
/*  28:    */ import org.apache.poi.hssf.util.CellReference;
/*  29:    */ import org.apache.poi.poifs.filesystem.DirectoryEntry;
/*  30:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  31:    */ import org.apache.poi.ss.usermodel.Chart;
/*  32:    */ import org.apache.poi.ss.usermodel.ClientAnchor;
/*  33:    */ import org.apache.poi.ss.usermodel.Drawing;
/*  34:    */ import org.apache.poi.util.HexDump;
/*  35:    */ import org.apache.poi.util.Internal;
/*  36:    */ import org.apache.poi.util.NotImplemented;
/*  37:    */ import org.apache.poi.util.StringUtil;
/*  38:    */ 
/*  39:    */ public final class HSSFPatriarch
/*  40:    */   implements HSSFShapeContainer, Drawing<HSSFShape>
/*  41:    */ {
/*  42: 63 */   private final List<HSSFShape> _shapes = new ArrayList();
/*  43:    */   private final EscherSpgrRecord _spgrRecord;
/*  44:    */   private final EscherContainerRecord _mainSpgrContainer;
/*  45:    */   private EscherAggregate _boundAggregate;
/*  46:    */   private final HSSFSheet _sheet;
/*  47:    */   
/*  48:    */   HSSFPatriarch(HSSFSheet sheet, EscherAggregate boundAggregate)
/*  49:    */   {
/*  50: 83 */     this._sheet = sheet;
/*  51: 84 */     this._boundAggregate = boundAggregate;
/*  52: 85 */     this._mainSpgrContainer = ((EscherContainerRecord)this._boundAggregate.getEscherContainer().getChildContainers().get(0));
/*  53: 86 */     EscherContainerRecord spContainer = (EscherContainerRecord)((EscherContainerRecord)this._boundAggregate.getEscherContainer().getChildContainers().get(0)).getChild(0);
/*  54:    */     
/*  55: 88 */     this._spgrRecord = ((EscherSpgrRecord)spContainer.getChildById((short)-4087));
/*  56: 89 */     buildShapeTree();
/*  57:    */   }
/*  58:    */   
/*  59:    */   static HSSFPatriarch createPatriarch(HSSFPatriarch patriarch, HSSFSheet sheet)
/*  60:    */   {
/*  61:101 */     HSSFPatriarch newPatriarch = new HSSFPatriarch(sheet, new EscherAggregate(true));
/*  62:102 */     newPatriarch.afterCreate();
/*  63:103 */     for (HSSFShape shape : patriarch.getChildren())
/*  64:    */     {
/*  65:    */       HSSFShape newShape;
/*  66:    */       HSSFShape newShape;
/*  67:105 */       if ((shape instanceof HSSFShapeGroup)) {
/*  68:106 */         newShape = ((HSSFShapeGroup)shape).cloneShape(newPatriarch);
/*  69:    */       } else {
/*  70:108 */         newShape = shape.cloneShape();
/*  71:    */       }
/*  72:110 */       newPatriarch.onCreate(newShape);
/*  73:111 */       newPatriarch.addShape(newShape);
/*  74:    */     }
/*  75:113 */     return newPatriarch;
/*  76:    */   }
/*  77:    */   
/*  78:    */   protected void preSerialize()
/*  79:    */   {
/*  80:121 */     Map<Integer, NoteRecord> tailRecords = this._boundAggregate.getTailRecords();
/*  81:    */     
/*  82:    */ 
/*  83:    */ 
/*  84:125 */     Set<String> coordinates = new HashSet(tailRecords.size());
/*  85:126 */     for (NoteRecord rec : tailRecords.values())
/*  86:    */     {
/*  87:127 */       String noteRef = new CellReference(rec.getRow(), rec.getColumn()).formatAsString();
/*  88:129 */       if (coordinates.contains(noteRef)) {
/*  89:130 */         throw new IllegalStateException("found multiple cell comments for cell " + noteRef);
/*  90:    */       }
/*  91:132 */       coordinates.add(noteRef);
/*  92:    */     }
/*  93:    */   }
/*  94:    */   
/*  95:    */   public boolean removeShape(HSSFShape shape)
/*  96:    */   {
/*  97:143 */     boolean isRemoved = this._mainSpgrContainer.removeChildRecord(shape.getEscherContainer());
/*  98:144 */     if (isRemoved)
/*  99:    */     {
/* 100:145 */       shape.afterRemove(this);
/* 101:146 */       this._shapes.remove(shape);
/* 102:    */     }
/* 103:148 */     return isRemoved;
/* 104:    */   }
/* 105:    */   
/* 106:    */   void afterCreate()
/* 107:    */   {
/* 108:152 */     DrawingManager2 drawingManager = this._sheet.getWorkbook().getWorkbook().getDrawingManager();
/* 109:153 */     short dgId = drawingManager.findNewDrawingGroupId();
/* 110:154 */     this._boundAggregate.setDgId(dgId);
/* 111:155 */     this._boundAggregate.setMainSpRecordId(newShapeId());
/* 112:156 */     drawingManager.incrementDrawingsSaved();
/* 113:    */   }
/* 114:    */   
/* 115:    */   public HSSFShapeGroup createGroup(HSSFClientAnchor anchor)
/* 116:    */   {
/* 117:167 */     HSSFShapeGroup group = new HSSFShapeGroup(null, anchor);
/* 118:168 */     addShape(group);
/* 119:169 */     onCreate(group);
/* 120:170 */     return group;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public HSSFSimpleShape createSimpleShape(HSSFClientAnchor anchor)
/* 124:    */   {
/* 125:186 */     HSSFSimpleShape shape = new HSSFSimpleShape(null, anchor);
/* 126:187 */     addShape(shape);
/* 127:    */     
/* 128:189 */     onCreate(shape);
/* 129:190 */     return shape;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public HSSFPicture createPicture(HSSFClientAnchor anchor, int pictureIndex)
/* 133:    */   {
/* 134:202 */     HSSFPicture shape = new HSSFPicture(null, anchor);
/* 135:203 */     shape.setPictureIndex(pictureIndex);
/* 136:204 */     addShape(shape);
/* 137:    */     
/* 138:206 */     onCreate(shape);
/* 139:207 */     return shape;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public HSSFPicture createPicture(ClientAnchor anchor, int pictureIndex)
/* 143:    */   {
/* 144:221 */     return createPicture((HSSFClientAnchor)anchor, pictureIndex);
/* 145:    */   }
/* 146:    */   
/* 147:    */   public HSSFObjectData createObjectData(ClientAnchor anchor, int storageId, int pictureIndex)
/* 148:    */   {
/* 149:226 */     ObjRecord obj = new ObjRecord();
/* 150:    */     
/* 151:228 */     CommonObjectDataSubRecord ftCmo = new CommonObjectDataSubRecord();
/* 152:229 */     ftCmo.setObjectType((short)8);
/* 153:    */     
/* 154:231 */     ftCmo.setLocked(true);
/* 155:232 */     ftCmo.setPrintable(true);
/* 156:233 */     ftCmo.setAutofill(true);
/* 157:234 */     ftCmo.setAutoline(true);
/* 158:235 */     ftCmo.setReserved1(0);
/* 159:236 */     ftCmo.setReserved2(0);
/* 160:237 */     ftCmo.setReserved3(0);
/* 161:238 */     obj.addSubRecord(ftCmo);
/* 162:    */     
/* 163:    */ 
/* 164:241 */     FtCfSubRecord ftCf = new FtCfSubRecord();
/* 165:242 */     HSSFPictureData pictData = (HSSFPictureData)getSheet().getWorkbook().getAllPictures().get(pictureIndex - 1);
/* 166:243 */     switch (pictData.getFormat())
/* 167:    */     {
/* 168:    */     case 2: 
/* 169:    */     case 3: 
/* 170:247 */       ftCf.setFlags((short)2);
/* 171:248 */       break;
/* 172:    */     case 4: 
/* 173:    */     case 5: 
/* 174:    */     case 6: 
/* 175:    */     case 7: 
/* 176:253 */       ftCf.setFlags((short)9);
/* 177:254 */       break;
/* 178:    */     default: 
/* 179:256 */       throw new IllegalStateException("Invalid picture type: " + pictData.getFormat());
/* 180:    */     }
/* 181:258 */     obj.addSubRecord(ftCf);
/* 182:    */     
/* 183:260 */     FtPioGrbitSubRecord ftPioGrbit = new FtPioGrbitSubRecord();
/* 184:261 */     ftPioGrbit.setFlagByBit(1, true);
/* 185:262 */     obj.addSubRecord(ftPioGrbit);
/* 186:    */     
/* 187:264 */     EmbeddedObjectRefSubRecord ftPictFmla = new EmbeddedObjectRefSubRecord();
/* 188:265 */     ftPictFmla.setUnknownFormulaData(new byte[] { 2, 0, 0, 0, 0 });
/* 189:266 */     ftPictFmla.setOleClassname("Paket");
/* 190:267 */     ftPictFmla.setStorageId(storageId);
/* 191:    */     
/* 192:269 */     obj.addSubRecord(ftPictFmla);
/* 193:270 */     obj.addSubRecord(new EndSubRecord());
/* 194:    */     
/* 195:272 */     String entryName = "MBD" + HexDump.toHex(storageId);
/* 196:    */     DirectoryEntry oleRoot;
/* 197:    */     try
/* 198:    */     {
/* 199:275 */       DirectoryNode dn = this._sheet.getWorkbook().getDirectory();
/* 200:276 */       if (dn == null) {
/* 201:277 */         throw new FileNotFoundException();
/* 202:    */       }
/* 203:279 */       oleRoot = (DirectoryEntry)dn.getEntry(entryName);
/* 204:    */     }
/* 205:    */     catch (FileNotFoundException e)
/* 206:    */     {
/* 207:281 */       throw new IllegalStateException("trying to add ole shape without actually adding data first - use HSSFWorkbook.addOlePackage first", e);
/* 208:    */     }
/* 209:285 */     HSSFPicture shape = new HSSFPicture(null, (HSSFClientAnchor)anchor);
/* 210:286 */     shape.setPictureIndex(pictureIndex);
/* 211:287 */     EscherContainerRecord spContainer = shape.getEscherContainer();
/* 212:288 */     EscherSpRecord spRecord = (EscherSpRecord)spContainer.getChildById((short)-4086);
/* 213:289 */     spRecord.setFlags(spRecord.getFlags() | 0x10);
/* 214:    */     
/* 215:291 */     HSSFObjectData oleShape = new HSSFObjectData(spContainer, obj, oleRoot);
/* 216:292 */     addShape(oleShape);
/* 217:293 */     onCreate(oleShape);
/* 218:    */     
/* 219:    */ 
/* 220:296 */     return oleShape;
/* 221:    */   }
/* 222:    */   
/* 223:    */   public HSSFPolygon createPolygon(HSSFClientAnchor anchor)
/* 224:    */   {
/* 225:307 */     HSSFPolygon shape = new HSSFPolygon(null, anchor);
/* 226:308 */     addShape(shape);
/* 227:309 */     onCreate(shape);
/* 228:310 */     return shape;
/* 229:    */   }
/* 230:    */   
/* 231:    */   public HSSFTextbox createTextbox(HSSFClientAnchor anchor)
/* 232:    */   {
/* 233:321 */     HSSFTextbox shape = new HSSFTextbox(null, anchor);
/* 234:322 */     addShape(shape);
/* 235:323 */     onCreate(shape);
/* 236:324 */     return shape;
/* 237:    */   }
/* 238:    */   
/* 239:    */   public HSSFComment createComment(HSSFAnchor anchor)
/* 240:    */   {
/* 241:335 */     HSSFComment shape = new HSSFComment(null, anchor);
/* 242:336 */     addShape(shape);
/* 243:337 */     onCreate(shape);
/* 244:338 */     return shape;
/* 245:    */   }
/* 246:    */   
/* 247:    */   HSSFSimpleShape createComboBox(HSSFAnchor anchor)
/* 248:    */   {
/* 249:347 */     HSSFCombobox shape = new HSSFCombobox(null, anchor);
/* 250:348 */     addShape(shape);
/* 251:349 */     onCreate(shape);
/* 252:350 */     return shape;
/* 253:    */   }
/* 254:    */   
/* 255:    */   public HSSFComment createCellComment(ClientAnchor anchor)
/* 256:    */   {
/* 257:355 */     return createComment((HSSFAnchor)anchor);
/* 258:    */   }
/* 259:    */   
/* 260:    */   public List<HSSFShape> getChildren()
/* 261:    */   {
/* 262:363 */     return Collections.unmodifiableList(this._shapes);
/* 263:    */   }
/* 264:    */   
/* 265:    */   @Internal
/* 266:    */   public void addShape(HSSFShape shape)
/* 267:    */   {
/* 268:372 */     shape.setPatriarch(this);
/* 269:373 */     this._shapes.add(shape);
/* 270:    */   }
/* 271:    */   
/* 272:    */   private void onCreate(HSSFShape shape)
/* 273:    */   {
/* 274:377 */     EscherContainerRecord spgrContainer = (EscherContainerRecord)this._boundAggregate.getEscherContainer().getChildContainers().get(0);
/* 275:    */     
/* 276:    */ 
/* 277:380 */     EscherContainerRecord spContainer = shape.getEscherContainer();
/* 278:381 */     int shapeId = newShapeId();
/* 279:382 */     shape.setShapeId(shapeId);
/* 280:    */     
/* 281:384 */     spgrContainer.addChildRecord(spContainer);
/* 282:385 */     shape.afterInsert(this);
/* 283:386 */     setFlipFlags(shape);
/* 284:    */   }
/* 285:    */   
/* 286:    */   public int countOfAllChildren()
/* 287:    */   {
/* 288:394 */     int count = this._shapes.size();
/* 289:395 */     for (Iterator<HSSFShape> iterator = this._shapes.iterator(); iterator.hasNext();)
/* 290:    */     {
/* 291:396 */       HSSFShape shape = (HSSFShape)iterator.next();
/* 292:397 */       count += shape.countOfAllChildren();
/* 293:    */     }
/* 294:399 */     return count;
/* 295:    */   }
/* 296:    */   
/* 297:    */   public void setCoordinates(int x1, int y1, int x2, int y2)
/* 298:    */   {
/* 299:408 */     this._spgrRecord.setRectY1(y1);
/* 300:409 */     this._spgrRecord.setRectY2(y2);
/* 301:410 */     this._spgrRecord.setRectX1(x1);
/* 302:411 */     this._spgrRecord.setRectX2(x2);
/* 303:    */   }
/* 304:    */   
/* 305:    */   public void clear()
/* 306:    */   {
/* 307:419 */     ArrayList<HSSFShape> copy = new ArrayList(this._shapes);
/* 308:420 */     for (HSSFShape shape : copy) {
/* 309:421 */       removeShape(shape);
/* 310:    */     }
/* 311:    */   }
/* 312:    */   
/* 313:    */   int newShapeId()
/* 314:    */   {
/* 315:429 */     DrawingManager2 dm = this._sheet.getWorkbook().getWorkbook().getDrawingManager();
/* 316:430 */     EscherDgRecord dg = (EscherDgRecord)this._boundAggregate.getEscherContainer().getChildById((short)-4088);
/* 317:    */     
/* 318:432 */     return dm.allocateShapeId(dg);
/* 319:    */   }
/* 320:    */   
/* 321:    */   public boolean containsChart()
/* 322:    */   {
/* 323:446 */     EscherOptRecord optRecord = (EscherOptRecord)this._boundAggregate.findFirstWithId((short)-4085);
/* 324:448 */     if (optRecord == null) {
/* 325:450 */       return false;
/* 326:    */     }
/* 327:453 */     for (Iterator<EscherProperty> it = optRecord.getEscherProperties().iterator(); it.hasNext();)
/* 328:    */     {
/* 329:454 */       EscherProperty prop = (EscherProperty)it.next();
/* 330:455 */       if ((prop.getPropertyNumber() == 896) && (prop.isComplex()))
/* 331:    */       {
/* 332:456 */         EscherComplexProperty cp = (EscherComplexProperty)prop;
/* 333:457 */         String str = StringUtil.getFromUnicodeLE(cp.getComplexData());
/* 334:459 */         if (str.equals("")) {
/* 335:460 */           return true;
/* 336:    */         }
/* 337:    */       }
/* 338:    */     }
/* 339:465 */     return false;
/* 340:    */   }
/* 341:    */   
/* 342:    */   public int getX1()
/* 343:    */   {
/* 344:473 */     return this._spgrRecord.getRectX1();
/* 345:    */   }
/* 346:    */   
/* 347:    */   public int getY1()
/* 348:    */   {
/* 349:481 */     return this._spgrRecord.getRectY1();
/* 350:    */   }
/* 351:    */   
/* 352:    */   public int getX2()
/* 353:    */   {
/* 354:489 */     return this._spgrRecord.getRectX2();
/* 355:    */   }
/* 356:    */   
/* 357:    */   public int getY2()
/* 358:    */   {
/* 359:497 */     return this._spgrRecord.getRectY2();
/* 360:    */   }
/* 361:    */   
/* 362:    */   @Internal
/* 363:    */   public EscherAggregate getBoundAggregate()
/* 364:    */   {
/* 365:506 */     return this._boundAggregate;
/* 366:    */   }
/* 367:    */   
/* 368:    */   public HSSFClientAnchor createAnchor(int dx1, int dy1, int dx2, int dy2, int col1, int row1, int col2, int row2)
/* 369:    */   {
/* 370:525 */     return new HSSFClientAnchor(dx1, dy1, dx2, dy2, (short)col1, row1, (short)col2, row2);
/* 371:    */   }
/* 372:    */   
/* 373:    */   @NotImplemented
/* 374:    */   public Chart createChart(ClientAnchor anchor)
/* 375:    */   {
/* 376:531 */     throw new RuntimeException("NotImplemented");
/* 377:    */   }
/* 378:    */   
/* 379:    */   void buildShapeTree()
/* 380:    */   {
/* 381:539 */     EscherContainerRecord dgContainer = this._boundAggregate.getEscherContainer();
/* 382:540 */     if (dgContainer == null) {
/* 383:541 */       return;
/* 384:    */     }
/* 385:543 */     EscherContainerRecord spgrConrainer = (EscherContainerRecord)dgContainer.getChildContainers().get(0);
/* 386:544 */     List<EscherContainerRecord> spgrChildren = spgrConrainer.getChildContainers();
/* 387:546 */     for (int i = 0; i < spgrChildren.size(); i++)
/* 388:    */     {
/* 389:547 */       EscherContainerRecord spContainer = (EscherContainerRecord)spgrChildren.get(i);
/* 390:548 */       if (i != 0) {
/* 391:549 */         HSSFShapeFactory.createShapeTree(spContainer, this._boundAggregate, this, this._sheet.getWorkbook().getDirectory());
/* 392:    */       }
/* 393:    */     }
/* 394:    */   }
/* 395:    */   
/* 396:    */   private void setFlipFlags(HSSFShape shape)
/* 397:    */   {
/* 398:555 */     EscherSpRecord sp = (EscherSpRecord)shape.getEscherContainer().getChildById((short)-4086);
/* 399:556 */     if (shape.getAnchor().isHorizontallyFlipped()) {
/* 400:557 */       sp.setFlags(sp.getFlags() | 0x40);
/* 401:    */     }
/* 402:559 */     if (shape.getAnchor().isVerticallyFlipped()) {
/* 403:560 */       sp.setFlags(sp.getFlags() | 0x80);
/* 404:    */     }
/* 405:    */   }
/* 406:    */   
/* 407:    */   public Iterator<HSSFShape> iterator()
/* 408:    */   {
/* 409:566 */     return this._shapes.iterator();
/* 410:    */   }
/* 411:    */   
/* 412:    */   protected HSSFSheet getSheet()
/* 413:    */   {
/* 414:570 */     return this._sheet;
/* 415:    */   }
/* 416:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFPatriarch
 * JD-Core Version:    0.7.0.1
 */