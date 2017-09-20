/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.List;
/*   7:    */ import org.apache.poi.ddf.DefaultEscherRecordFactory;
/*   8:    */ import org.apache.poi.ddf.EscherBoolProperty;
/*   9:    */ import org.apache.poi.ddf.EscherChildAnchorRecord;
/*  10:    */ import org.apache.poi.ddf.EscherClientAnchorRecord;
/*  11:    */ import org.apache.poi.ddf.EscherClientDataRecord;
/*  12:    */ import org.apache.poi.ddf.EscherContainerRecord;
/*  13:    */ import org.apache.poi.ddf.EscherOptRecord;
/*  14:    */ import org.apache.poi.ddf.EscherRecord;
/*  15:    */ import org.apache.poi.ddf.EscherSpRecord;
/*  16:    */ import org.apache.poi.ddf.EscherSpgrRecord;
/*  17:    */ import org.apache.poi.hssf.record.CommonObjectDataSubRecord;
/*  18:    */ import org.apache.poi.hssf.record.EndSubRecord;
/*  19:    */ import org.apache.poi.hssf.record.EscherAggregate;
/*  20:    */ import org.apache.poi.hssf.record.GroupMarkerSubRecord;
/*  21:    */ import org.apache.poi.hssf.record.ObjRecord;
/*  22:    */ 
/*  23:    */ public class HSSFShapeGroup
/*  24:    */   extends HSSFShape
/*  25:    */   implements HSSFShapeContainer
/*  26:    */ {
/*  27: 33 */   private final List<HSSFShape> shapes = new ArrayList();
/*  28:    */   private EscherSpgrRecord _spgrRecord;
/*  29:    */   
/*  30:    */   public HSSFShapeGroup(EscherContainerRecord spgrContainer, ObjRecord objRecord)
/*  31:    */   {
/*  32: 37 */     super(spgrContainer, objRecord);
/*  33:    */     
/*  34:    */ 
/*  35: 40 */     EscherContainerRecord spContainer = (EscherContainerRecord)spgrContainer.getChildContainers().get(0);
/*  36: 41 */     this._spgrRecord = ((EscherSpgrRecord)spContainer.getChild(0));
/*  37: 42 */     for (EscherRecord ch : spContainer.getChildRecords()) {
/*  38: 43 */       switch (ch.getRecordId())
/*  39:    */       {
/*  40:    */       case -4087: 
/*  41:    */         break;
/*  42:    */       case -4080: 
/*  43: 47 */         this.anchor = new HSSFClientAnchor((EscherClientAnchorRecord)ch);
/*  44: 48 */         break;
/*  45:    */       case -4081: 
/*  46: 50 */         this.anchor = new HSSFChildAnchor((EscherChildAnchorRecord)ch);
/*  47:    */       }
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   public HSSFShapeGroup(HSSFShape parent, HSSFAnchor anchor)
/*  52:    */   {
/*  53: 59 */     super(parent, anchor);
/*  54: 60 */     this._spgrRecord = ((EscherSpgrRecord)((EscherContainerRecord)getEscherContainer().getChild(0)).getChildById((short)-4087));
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected EscherContainerRecord createSpContainer()
/*  58:    */   {
/*  59: 65 */     EscherContainerRecord spgrContainer = new EscherContainerRecord();
/*  60: 66 */     EscherContainerRecord spContainer = new EscherContainerRecord();
/*  61: 67 */     EscherSpgrRecord spgr = new EscherSpgrRecord();
/*  62: 68 */     EscherSpRecord sp = new EscherSpRecord();
/*  63: 69 */     EscherOptRecord opt = new EscherOptRecord();
/*  64:    */     
/*  65: 71 */     EscherClientDataRecord clientData = new EscherClientDataRecord();
/*  66:    */     
/*  67: 73 */     spgrContainer.setRecordId((short)-4093);
/*  68: 74 */     spgrContainer.setOptions((short)15);
/*  69: 75 */     spContainer.setRecordId((short)-4092);
/*  70: 76 */     spContainer.setOptions((short)15);
/*  71: 77 */     spgr.setRecordId((short)-4087);
/*  72: 78 */     spgr.setOptions((short)1);
/*  73: 79 */     spgr.setRectX1(0);
/*  74: 80 */     spgr.setRectY1(0);
/*  75: 81 */     spgr.setRectX2(1023);
/*  76: 82 */     spgr.setRectY2(255);
/*  77: 83 */     sp.setRecordId((short)-4086);
/*  78: 84 */     sp.setOptions((short)2);
/*  79: 85 */     if ((getAnchor() instanceof HSSFClientAnchor)) {
/*  80: 86 */       sp.setFlags(513);
/*  81:    */     } else {
/*  82: 88 */       sp.setFlags(515);
/*  83:    */     }
/*  84: 90 */     opt.setRecordId((short)-4085);
/*  85: 91 */     opt.setOptions((short)35);
/*  86: 92 */     opt.addEscherProperty(new EscherBoolProperty((short)127, 262148));
/*  87: 93 */     opt.addEscherProperty(new EscherBoolProperty((short)959, 524288));
/*  88:    */     
/*  89: 95 */     EscherRecord anchor = getAnchor().getEscherAnchor();
/*  90: 96 */     clientData.setRecordId((short)-4079);
/*  91: 97 */     clientData.setOptions((short)0);
/*  92:    */     
/*  93: 99 */     spgrContainer.addChildRecord(spContainer);
/*  94:100 */     spContainer.addChildRecord(spgr);
/*  95:101 */     spContainer.addChildRecord(sp);
/*  96:102 */     spContainer.addChildRecord(opt);
/*  97:103 */     spContainer.addChildRecord(anchor);
/*  98:104 */     spContainer.addChildRecord(clientData);
/*  99:105 */     return spgrContainer;
/* 100:    */   }
/* 101:    */   
/* 102:    */   protected ObjRecord createObjRecord()
/* 103:    */   {
/* 104:110 */     ObjRecord obj = new ObjRecord();
/* 105:111 */     CommonObjectDataSubRecord cmo = new CommonObjectDataSubRecord();
/* 106:112 */     cmo.setObjectType((short)0);
/* 107:113 */     cmo.setLocked(true);
/* 108:114 */     cmo.setPrintable(true);
/* 109:115 */     cmo.setAutofill(true);
/* 110:116 */     cmo.setAutoline(true);
/* 111:117 */     GroupMarkerSubRecord gmo = new GroupMarkerSubRecord();
/* 112:118 */     EndSubRecord end = new EndSubRecord();
/* 113:119 */     obj.addSubRecord(cmo);
/* 114:120 */     obj.addSubRecord(gmo);
/* 115:121 */     obj.addSubRecord(end);
/* 116:122 */     return obj;
/* 117:    */   }
/* 118:    */   
/* 119:    */   protected void afterRemove(HSSFPatriarch patriarch)
/* 120:    */   {
/* 121:127 */     patriarch.getBoundAggregate().removeShapeToObjRecord(((EscherContainerRecord)getEscherContainer().getChildContainers().get(0)).getChildById((short)-4079));
/* 122:129 */     for (int i = 0; i < this.shapes.size(); i++)
/* 123:    */     {
/* 124:130 */       HSSFShape shape = (HSSFShape)this.shapes.get(i);
/* 125:131 */       removeShape(shape);
/* 126:132 */       shape.afterRemove(getPatriarch());
/* 127:    */     }
/* 128:134 */     this.shapes.clear();
/* 129:    */   }
/* 130:    */   
/* 131:    */   private void onCreate(HSSFShape shape)
/* 132:    */   {
/* 133:138 */     if (getPatriarch() != null)
/* 134:    */     {
/* 135:139 */       EscherContainerRecord spContainer = shape.getEscherContainer();
/* 136:140 */       int shapeId = getPatriarch().newShapeId();
/* 137:141 */       shape.setShapeId(shapeId);
/* 138:142 */       getEscherContainer().addChildRecord(spContainer);
/* 139:143 */       shape.afterInsert(getPatriarch());
/* 140:    */       EscherSpRecord sp;
/* 141:    */       EscherSpRecord sp;
/* 142:145 */       if ((shape instanceof HSSFShapeGroup)) {
/* 143:146 */         sp = (EscherSpRecord)((EscherContainerRecord)shape.getEscherContainer().getChildContainers().get(0)).getChildById((short)-4086);
/* 144:    */       } else {
/* 145:148 */         sp = (EscherSpRecord)shape.getEscherContainer().getChildById((short)-4086);
/* 146:    */       }
/* 147:150 */       sp.setFlags(sp.getFlags() | 0x2);
/* 148:    */     }
/* 149:    */   }
/* 150:    */   
/* 151:    */   public HSSFShapeGroup createGroup(HSSFChildAnchor anchor)
/* 152:    */   {
/* 153:161 */     HSSFShapeGroup group = new HSSFShapeGroup(this, anchor);
/* 154:162 */     group.setParent(this);
/* 155:163 */     group.setAnchor(anchor);
/* 156:164 */     this.shapes.add(group);
/* 157:165 */     onCreate(group);
/* 158:166 */     return group;
/* 159:    */   }
/* 160:    */   
/* 161:    */   public void addShape(HSSFShape shape)
/* 162:    */   {
/* 163:170 */     shape.setPatriarch(getPatriarch());
/* 164:171 */     shape.setParent(this);
/* 165:172 */     this.shapes.add(shape);
/* 166:    */   }
/* 167:    */   
/* 168:    */   public HSSFSimpleShape createShape(HSSFChildAnchor anchor)
/* 169:    */   {
/* 170:182 */     HSSFSimpleShape shape = new HSSFSimpleShape(this, anchor);
/* 171:183 */     shape.setParent(this);
/* 172:184 */     shape.setAnchor(anchor);
/* 173:185 */     this.shapes.add(shape);
/* 174:186 */     onCreate(shape);
/* 175:187 */     EscherSpRecord sp = (EscherSpRecord)shape.getEscherContainer().getChildById((short)-4086);
/* 176:188 */     if (shape.getAnchor().isHorizontallyFlipped()) {
/* 177:189 */       sp.setFlags(sp.getFlags() | 0x40);
/* 178:    */     }
/* 179:191 */     if (shape.getAnchor().isVerticallyFlipped()) {
/* 180:192 */       sp.setFlags(sp.getFlags() | 0x80);
/* 181:    */     }
/* 182:194 */     return shape;
/* 183:    */   }
/* 184:    */   
/* 185:    */   public HSSFTextbox createTextbox(HSSFChildAnchor anchor)
/* 186:    */   {
/* 187:204 */     HSSFTextbox shape = new HSSFTextbox(this, anchor);
/* 188:205 */     shape.setParent(this);
/* 189:206 */     shape.setAnchor(anchor);
/* 190:207 */     this.shapes.add(shape);
/* 191:208 */     onCreate(shape);
/* 192:209 */     return shape;
/* 193:    */   }
/* 194:    */   
/* 195:    */   public HSSFPolygon createPolygon(HSSFChildAnchor anchor)
/* 196:    */   {
/* 197:220 */     HSSFPolygon shape = new HSSFPolygon(this, anchor);
/* 198:221 */     shape.setParent(this);
/* 199:222 */     shape.setAnchor(anchor);
/* 200:223 */     this.shapes.add(shape);
/* 201:224 */     onCreate(shape);
/* 202:225 */     return shape;
/* 203:    */   }
/* 204:    */   
/* 205:    */   public HSSFPicture createPicture(HSSFChildAnchor anchor, int pictureIndex)
/* 206:    */   {
/* 207:236 */     HSSFPicture shape = new HSSFPicture(this, anchor);
/* 208:237 */     shape.setParent(this);
/* 209:238 */     shape.setAnchor(anchor);
/* 210:239 */     shape.setPictureIndex(pictureIndex);
/* 211:240 */     this.shapes.add(shape);
/* 212:241 */     onCreate(shape);
/* 213:242 */     EscherSpRecord sp = (EscherSpRecord)shape.getEscherContainer().getChildById((short)-4086);
/* 214:243 */     if (shape.getAnchor().isHorizontallyFlipped()) {
/* 215:244 */       sp.setFlags(sp.getFlags() | 0x40);
/* 216:    */     }
/* 217:246 */     if (shape.getAnchor().isVerticallyFlipped()) {
/* 218:247 */       sp.setFlags(sp.getFlags() | 0x80);
/* 219:    */     }
/* 220:249 */     return shape;
/* 221:    */   }
/* 222:    */   
/* 223:    */   public List<HSSFShape> getChildren()
/* 224:    */   {
/* 225:256 */     return Collections.unmodifiableList(this.shapes);
/* 226:    */   }
/* 227:    */   
/* 228:    */   public void setCoordinates(int x1, int y1, int x2, int y2)
/* 229:    */   {
/* 230:264 */     this._spgrRecord.setRectX1(x1);
/* 231:265 */     this._spgrRecord.setRectX2(x2);
/* 232:266 */     this._spgrRecord.setRectY1(y1);
/* 233:267 */     this._spgrRecord.setRectY2(y2);
/* 234:    */   }
/* 235:    */   
/* 236:    */   public void clear()
/* 237:    */   {
/* 238:271 */     ArrayList<HSSFShape> copy = new ArrayList(this.shapes);
/* 239:272 */     for (HSSFShape shape : copy) {
/* 240:273 */       removeShape(shape);
/* 241:    */     }
/* 242:    */   }
/* 243:    */   
/* 244:    */   public int getX1()
/* 245:    */   {
/* 246:281 */     return this._spgrRecord.getRectX1();
/* 247:    */   }
/* 248:    */   
/* 249:    */   public int getY1()
/* 250:    */   {
/* 251:288 */     return this._spgrRecord.getRectY1();
/* 252:    */   }
/* 253:    */   
/* 254:    */   public int getX2()
/* 255:    */   {
/* 256:295 */     return this._spgrRecord.getRectX2();
/* 257:    */   }
/* 258:    */   
/* 259:    */   public int getY2()
/* 260:    */   {
/* 261:302 */     return this._spgrRecord.getRectY2();
/* 262:    */   }
/* 263:    */   
/* 264:    */   public int countOfAllChildren()
/* 265:    */   {
/* 266:309 */     int count = this.shapes.size();
/* 267:310 */     for (Iterator<HSSFShape> iterator = this.shapes.iterator(); iterator.hasNext();)
/* 268:    */     {
/* 269:311 */       HSSFShape shape = (HSSFShape)iterator.next();
/* 270:312 */       count += shape.countOfAllChildren();
/* 271:    */     }
/* 272:314 */     return count;
/* 273:    */   }
/* 274:    */   
/* 275:    */   void afterInsert(HSSFPatriarch patriarch)
/* 276:    */   {
/* 277:319 */     EscherAggregate agg = patriarch.getBoundAggregate();
/* 278:320 */     EscherContainerRecord containerRecord = (EscherContainerRecord)getEscherContainer().getChildById((short)-4092);
/* 279:321 */     agg.associateShapeToObjRecord(containerRecord.getChildById((short)-4079), getObjRecord());
/* 280:    */   }
/* 281:    */   
/* 282:    */   void setShapeId(int shapeId)
/* 283:    */   {
/* 284:326 */     EscherContainerRecord containerRecord = (EscherContainerRecord)getEscherContainer().getChildById((short)-4092);
/* 285:327 */     EscherSpRecord spRecord = (EscherSpRecord)containerRecord.getChildById((short)-4086);
/* 286:328 */     spRecord.setShapeId(shapeId);
/* 287:329 */     CommonObjectDataSubRecord cod = (CommonObjectDataSubRecord)getObjRecord().getSubRecords().get(0);
/* 288:330 */     cod.setObjectId((short)(shapeId % 1024));
/* 289:    */   }
/* 290:    */   
/* 291:    */   int getShapeId()
/* 292:    */   {
/* 293:335 */     EscherContainerRecord containerRecord = (EscherContainerRecord)getEscherContainer().getChildById((short)-4092);
/* 294:336 */     return ((EscherSpRecord)containerRecord.getChildById((short)-4086)).getShapeId();
/* 295:    */   }
/* 296:    */   
/* 297:    */   protected HSSFShape cloneShape()
/* 298:    */   {
/* 299:341 */     throw new IllegalStateException("Use method cloneShape(HSSFPatriarch patriarch)");
/* 300:    */   }
/* 301:    */   
/* 302:    */   protected HSSFShape cloneShape(HSSFPatriarch patriarch)
/* 303:    */   {
/* 304:345 */     EscherContainerRecord spgrContainer = new EscherContainerRecord();
/* 305:346 */     spgrContainer.setRecordId((short)-4093);
/* 306:347 */     spgrContainer.setOptions((short)15);
/* 307:348 */     EscherContainerRecord spContainer = new EscherContainerRecord();
/* 308:349 */     EscherContainerRecord cont = (EscherContainerRecord)getEscherContainer().getChildById((short)-4092);
/* 309:350 */     byte[] inSp = cont.serialize();
/* 310:351 */     spContainer.fillFields(inSp, 0, new DefaultEscherRecordFactory());
/* 311:    */     
/* 312:353 */     spgrContainer.addChildRecord(spContainer);
/* 313:354 */     ObjRecord obj = null;
/* 314:355 */     if (null != getObjRecord()) {
/* 315:356 */       obj = (ObjRecord)getObjRecord().cloneViaReserialise();
/* 316:    */     }
/* 317:359 */     HSSFShapeGroup group = new HSSFShapeGroup(spgrContainer, obj);
/* 318:360 */     group.setPatriarch(patriarch);
/* 319:362 */     for (HSSFShape shape : getChildren())
/* 320:    */     {
/* 321:    */       HSSFShape newShape;
/* 322:    */       HSSFShape newShape;
/* 323:364 */       if ((shape instanceof HSSFShapeGroup)) {
/* 324:365 */         newShape = ((HSSFShapeGroup)shape).cloneShape(patriarch);
/* 325:    */       } else {
/* 326:367 */         newShape = shape.cloneShape();
/* 327:    */       }
/* 328:369 */       group.addShape(newShape);
/* 329:370 */       group.onCreate(newShape);
/* 330:    */     }
/* 331:372 */     return group;
/* 332:    */   }
/* 333:    */   
/* 334:    */   public boolean removeShape(HSSFShape shape)
/* 335:    */   {
/* 336:376 */     boolean isRemoved = getEscherContainer().removeChildRecord(shape.getEscherContainer());
/* 337:377 */     if (isRemoved)
/* 338:    */     {
/* 339:378 */       shape.afterRemove(getPatriarch());
/* 340:379 */       this.shapes.remove(shape);
/* 341:    */     }
/* 342:381 */     return isRemoved;
/* 343:    */   }
/* 344:    */   
/* 345:    */   public Iterator<HSSFShape> iterator()
/* 346:    */   {
/* 347:386 */     return this.shapes.iterator();
/* 348:    */   }
/* 349:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFShapeGroup
 * JD-Core Version:    0.7.0.1
 */