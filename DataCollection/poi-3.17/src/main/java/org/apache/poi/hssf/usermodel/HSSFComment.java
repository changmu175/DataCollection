/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.List;
/*   4:    */ import org.apache.poi.ddf.DefaultEscherRecordFactory;
/*   5:    */ import org.apache.poi.ddf.EscherBSERecord;
/*   6:    */ import org.apache.poi.ddf.EscherContainerRecord;
/*   7:    */ import org.apache.poi.ddf.EscherOptRecord;
/*   8:    */ import org.apache.poi.ddf.EscherSimpleProperty;
/*   9:    */ import org.apache.poi.hssf.model.InternalWorkbook;
/*  10:    */ import org.apache.poi.hssf.record.CommonObjectDataSubRecord;
/*  11:    */ import org.apache.poi.hssf.record.EndSubRecord;
/*  12:    */ import org.apache.poi.hssf.record.EscherAggregate;
/*  13:    */ import org.apache.poi.hssf.record.NoteRecord;
/*  14:    */ import org.apache.poi.hssf.record.NoteStructureSubRecord;
/*  15:    */ import org.apache.poi.hssf.record.ObjRecord;
/*  16:    */ import org.apache.poi.hssf.record.TextObjectRecord;
/*  17:    */ import org.apache.poi.ss.usermodel.ClientAnchor;
/*  18:    */ import org.apache.poi.ss.usermodel.Comment;
/*  19:    */ import org.apache.poi.ss.util.CellAddress;
/*  20:    */ 
/*  21:    */ public class HSSFComment
/*  22:    */   extends HSSFTextbox
/*  23:    */   implements Comment
/*  24:    */ {
/*  25:    */   private static final int FILL_TYPE_SOLID = 0;
/*  26:    */   private static final int FILL_TYPE_PICTURE = 3;
/*  27:    */   private static final int GROUP_SHAPE_PROPERTY_DEFAULT_VALUE = 655362;
/*  28:    */   private static final int GROUP_SHAPE_HIDDEN_MASK = 16777218;
/*  29:    */   private static final int GROUP_SHAPE_NOT_HIDDEN_MASK = -16777219;
/*  30:    */   private final NoteRecord _note;
/*  31:    */   
/*  32:    */   public HSSFComment(EscherContainerRecord spContainer, ObjRecord objRecord, TextObjectRecord textObjectRecord, NoteRecord note)
/*  33:    */   {
/*  34: 60 */     super(spContainer, objRecord, textObjectRecord);
/*  35: 61 */     this._note = note;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public HSSFComment(HSSFShape parent, HSSFAnchor anchor)
/*  39:    */   {
/*  40: 71 */     this(parent, anchor, createNoteRecord());
/*  41:    */   }
/*  42:    */   
/*  43:    */   private HSSFComment(HSSFShape parent, HSSFAnchor anchor, NoteRecord note)
/*  44:    */   {
/*  45: 75 */     super(parent, anchor);
/*  46: 76 */     this._note = note;
/*  47:    */     
/*  48: 78 */     setFillColor(134217808);
/*  49:    */     
/*  50:    */ 
/*  51: 81 */     setVisible(false);
/*  52: 82 */     setAuthor("");
/*  53: 83 */     CommonObjectDataSubRecord cod = (CommonObjectDataSubRecord)getObjRecord().getSubRecords().get(0);
/*  54: 84 */     cod.setObjectType((short)25);
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected HSSFComment(NoteRecord note, TextObjectRecord txo)
/*  58:    */   {
/*  59: 88 */     this(null, new HSSFClientAnchor(), note);
/*  60:    */   }
/*  61:    */   
/*  62:    */   void afterInsert(HSSFPatriarch patriarch)
/*  63:    */   {
/*  64: 93 */     super.afterInsert(patriarch);
/*  65: 94 */     patriarch.getBoundAggregate().addTailRecord(getNoteRecord());
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected EscherContainerRecord createSpContainer()
/*  69:    */   {
/*  70: 99 */     EscherContainerRecord spContainer = super.createSpContainer();
/*  71:100 */     EscherOptRecord opt = (EscherOptRecord)spContainer.getChildById((short)-4085);
/*  72:101 */     opt.removeEscherProperty(129);
/*  73:102 */     opt.removeEscherProperty(131);
/*  74:103 */     opt.removeEscherProperty(130);
/*  75:104 */     opt.removeEscherProperty(132);
/*  76:105 */     opt.setEscherProperty(new EscherSimpleProperty((short)959, false, false, 655362));
/*  77:106 */     return spContainer;
/*  78:    */   }
/*  79:    */   
/*  80:    */   protected ObjRecord createObjRecord()
/*  81:    */   {
/*  82:111 */     ObjRecord obj = new ObjRecord();
/*  83:112 */     CommonObjectDataSubRecord c = new CommonObjectDataSubRecord();
/*  84:113 */     c.setObjectType((short)202);
/*  85:114 */     c.setLocked(true);
/*  86:115 */     c.setPrintable(true);
/*  87:116 */     c.setAutofill(false);
/*  88:117 */     c.setAutoline(true);
/*  89:    */     
/*  90:119 */     NoteStructureSubRecord u = new NoteStructureSubRecord();
/*  91:120 */     EndSubRecord e = new EndSubRecord();
/*  92:121 */     obj.addSubRecord(c);
/*  93:122 */     obj.addSubRecord(u);
/*  94:123 */     obj.addSubRecord(e);
/*  95:124 */     return obj;
/*  96:    */   }
/*  97:    */   
/*  98:    */   private static NoteRecord createNoteRecord()
/*  99:    */   {
/* 100:128 */     NoteRecord note = new NoteRecord();
/* 101:129 */     note.setFlags((short)0);
/* 102:130 */     note.setAuthor("");
/* 103:131 */     return note;
/* 104:    */   }
/* 105:    */   
/* 106:    */   void setShapeId(int shapeId)
/* 107:    */   {
/* 108:136 */     if (shapeId > 65535) {
/* 109:137 */       throw new IllegalArgumentException("Cannot add more than 65535 shapes");
/* 110:    */     }
/* 111:139 */     super.setShapeId(shapeId);
/* 112:140 */     CommonObjectDataSubRecord cod = (CommonObjectDataSubRecord)getObjRecord().getSubRecords().get(0);
/* 113:141 */     cod.setObjectId(shapeId);
/* 114:142 */     this._note.setShapeId(shapeId);
/* 115:    */   }
/* 116:    */   
/* 117:    */   public void setVisible(boolean visible)
/* 118:    */   {
/* 119:152 */     this._note.setFlags((short)(visible ? 2 : 0));
/* 120:153 */     setHidden(!visible);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public boolean isVisible()
/* 124:    */   {
/* 125:163 */     return this._note.getFlags() == 2;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public CellAddress getAddress()
/* 129:    */   {
/* 130:168 */     return new CellAddress(getRow(), getColumn());
/* 131:    */   }
/* 132:    */   
/* 133:    */   public void setAddress(CellAddress address)
/* 134:    */   {
/* 135:173 */     setRow(address.getRow());
/* 136:174 */     setColumn(address.getColumn());
/* 137:    */   }
/* 138:    */   
/* 139:    */   public void setAddress(int row, int col)
/* 140:    */   {
/* 141:179 */     setRow(row);
/* 142:180 */     setColumn(col);
/* 143:    */   }
/* 144:    */   
/* 145:    */   public int getRow()
/* 146:    */   {
/* 147:190 */     return this._note.getRow();
/* 148:    */   }
/* 149:    */   
/* 150:    */   public void setRow(int row)
/* 151:    */   {
/* 152:200 */     this._note.setRow(row);
/* 153:    */   }
/* 154:    */   
/* 155:    */   public int getColumn()
/* 156:    */   {
/* 157:210 */     return this._note.getColumn();
/* 158:    */   }
/* 159:    */   
/* 160:    */   public void setColumn(int col)
/* 161:    */   {
/* 162:220 */     this._note.setColumn(col);
/* 163:    */   }
/* 164:    */   
/* 165:    */   public String getAuthor()
/* 166:    */   {
/* 167:230 */     return this._note.getAuthor();
/* 168:    */   }
/* 169:    */   
/* 170:    */   public void setAuthor(String author)
/* 171:    */   {
/* 172:240 */     if (this._note != null) {
/* 173:240 */       this._note.setAuthor(author);
/* 174:    */     }
/* 175:    */   }
/* 176:    */   
/* 177:    */   protected NoteRecord getNoteRecord()
/* 178:    */   {
/* 179:247 */     return this._note;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public boolean hasPosition()
/* 183:    */   {
/* 184:254 */     if (this._note == null) {
/* 185:254 */       return false;
/* 186:    */     }
/* 187:255 */     if ((getColumn() < 0) || (getRow() < 0)) {
/* 188:255 */       return false;
/* 189:    */     }
/* 190:256 */     return true;
/* 191:    */   }
/* 192:    */   
/* 193:    */   public ClientAnchor getClientAnchor()
/* 194:    */   {
/* 195:261 */     HSSFAnchor ha = super.getAnchor();
/* 196:262 */     if ((ha instanceof ClientAnchor)) {
/* 197:263 */       return (ClientAnchor)ha;
/* 198:    */     }
/* 199:266 */     throw new IllegalStateException("Anchor can not be changed in " + ClientAnchor.class.getSimpleName());
/* 200:    */   }
/* 201:    */   
/* 202:    */   public void setShapeType(int shapeType)
/* 203:    */   {
/* 204:272 */     throw new IllegalStateException("Shape type can not be changed in " + getClass().getSimpleName());
/* 205:    */   }
/* 206:    */   
/* 207:    */   public void afterRemove(HSSFPatriarch patriarch)
/* 208:    */   {
/* 209:277 */     super.afterRemove(patriarch);
/* 210:278 */     patriarch.getBoundAggregate().removeTailRecord(getNoteRecord());
/* 211:    */   }
/* 212:    */   
/* 213:    */   protected HSSFShape cloneShape()
/* 214:    */   {
/* 215:283 */     TextObjectRecord txo = (TextObjectRecord)getTextObjectRecord().cloneViaReserialise();
/* 216:284 */     EscherContainerRecord spContainer = new EscherContainerRecord();
/* 217:285 */     byte[] inSp = getEscherContainer().serialize();
/* 218:286 */     spContainer.fillFields(inSp, 0, new DefaultEscherRecordFactory());
/* 219:287 */     ObjRecord obj = (ObjRecord)getObjRecord().cloneViaReserialise();
/* 220:288 */     NoteRecord note = (NoteRecord)getNoteRecord().cloneViaReserialise();
/* 221:289 */     return new HSSFComment(spContainer, obj, txo, note);
/* 222:    */   }
/* 223:    */   
/* 224:    */   public void setBackgroundImage(int pictureIndex)
/* 225:    */   {
/* 226:293 */     setPropertyValue(new EscherSimpleProperty((short)390, false, true, pictureIndex));
/* 227:294 */     setPropertyValue(new EscherSimpleProperty((short)384, false, false, 3));
/* 228:295 */     EscherBSERecord bse = getPatriarch().getSheet().getWorkbook().getWorkbook().getBSERecord(pictureIndex);
/* 229:296 */     bse.setRef(bse.getRef() + 1);
/* 230:    */   }
/* 231:    */   
/* 232:    */   public void resetBackgroundImage()
/* 233:    */   {
/* 234:300 */     EscherSimpleProperty property = (EscherSimpleProperty)getOptRecord().lookup(390);
/* 235:301 */     if (null != property)
/* 236:    */     {
/* 237:302 */       EscherBSERecord bse = getPatriarch().getSheet().getWorkbook().getWorkbook().getBSERecord(property.getPropertyValue());
/* 238:303 */       bse.setRef(bse.getRef() - 1);
/* 239:304 */       getOptRecord().removeEscherProperty(390);
/* 240:    */     }
/* 241:306 */     setPropertyValue(new EscherSimpleProperty((short)384, false, false, 0));
/* 242:    */   }
/* 243:    */   
/* 244:    */   public int getBackgroundImageId()
/* 245:    */   {
/* 246:310 */     EscherSimpleProperty property = (EscherSimpleProperty)getOptRecord().lookup(390);
/* 247:311 */     return property == null ? 0 : property.getPropertyValue();
/* 248:    */   }
/* 249:    */   
/* 250:    */   private void setHidden(boolean value)
/* 251:    */   {
/* 252:315 */     EscherSimpleProperty property = (EscherSimpleProperty)getOptRecord().lookup(959);
/* 253:317 */     if (value) {
/* 254:318 */       setPropertyValue(new EscherSimpleProperty((short)959, false, false, property.getPropertyValue() | 0x1000002));
/* 255:    */     } else {
/* 256:320 */       setPropertyValue(new EscherSimpleProperty((short)959, false, false, property.getPropertyValue() & 0xFEFFFFFD));
/* 257:    */     }
/* 258:    */   }
/* 259:    */   
/* 260:    */   public boolean equals(Object obj)
/* 261:    */   {
/* 262:326 */     if (!(obj instanceof HSSFComment)) {
/* 263:327 */       return false;
/* 264:    */     }
/* 265:329 */     HSSFComment other = (HSSFComment)obj;
/* 266:330 */     return getNoteRecord().equals(other.getNoteRecord());
/* 267:    */   }
/* 268:    */   
/* 269:    */   public int hashCode()
/* 270:    */   {
/* 271:335 */     return (getRow() * 17 + getColumn()) * 31;
/* 272:    */   }
/* 273:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFComment
 * JD-Core Version:    0.7.0.1
 */