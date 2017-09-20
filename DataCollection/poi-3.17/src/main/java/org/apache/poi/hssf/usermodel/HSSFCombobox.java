/*  1:   */ package org.apache.poi.hssf.usermodel;
/*  2:   */ 
/*  3:   */ import java.util.List;
/*  4:   */ import org.apache.poi.ddf.EscherBoolProperty;
/*  5:   */ import org.apache.poi.ddf.EscherClientDataRecord;
/*  6:   */ import org.apache.poi.ddf.EscherContainerRecord;
/*  7:   */ import org.apache.poi.ddf.EscherOptRecord;
/*  8:   */ import org.apache.poi.ddf.EscherRecord;
/*  9:   */ import org.apache.poi.ddf.EscherSimpleProperty;
/* 10:   */ import org.apache.poi.ddf.EscherSpRecord;
/* 11:   */ import org.apache.poi.hssf.record.CommonObjectDataSubRecord;
/* 12:   */ import org.apache.poi.hssf.record.EndSubRecord;
/* 13:   */ import org.apache.poi.hssf.record.FtCblsSubRecord;
/* 14:   */ import org.apache.poi.hssf.record.LbsDataSubRecord;
/* 15:   */ import org.apache.poi.hssf.record.ObjRecord;
/* 16:   */ import org.apache.poi.hssf.record.TextObjectRecord;
/* 17:   */ import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
/* 18:   */ 
/* 19:   */ public class HSSFCombobox
/* 20:   */   extends HSSFSimpleShape
/* 21:   */ {
/* 22:   */   public HSSFCombobox(EscherContainerRecord spContainer, ObjRecord objRecord)
/* 23:   */   {
/* 24:30 */     super(spContainer, objRecord);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public HSSFCombobox(HSSFShape parent, HSSFAnchor anchor)
/* 28:   */   {
/* 29:34 */     super(parent, anchor);
/* 30:35 */     super.setShapeType(201);
/* 31:36 */     CommonObjectDataSubRecord cod = (CommonObjectDataSubRecord)getObjRecord().getSubRecords().get(0);
/* 32:37 */     cod.setObjectType((short)20);
/* 33:   */   }
/* 34:   */   
/* 35:   */   protected TextObjectRecord createTextObjRecord()
/* 36:   */   {
/* 37:42 */     return null;
/* 38:   */   }
/* 39:   */   
/* 40:   */   protected EscherContainerRecord createSpContainer()
/* 41:   */   {
/* 42:47 */     EscherContainerRecord spContainer = new EscherContainerRecord();
/* 43:48 */     EscherSpRecord sp = new EscherSpRecord();
/* 44:49 */     EscherOptRecord opt = new EscherOptRecord();
/* 45:50 */     EscherClientDataRecord clientData = new EscherClientDataRecord();
/* 46:   */     
/* 47:52 */     spContainer.setRecordId((short)-4092);
/* 48:53 */     spContainer.setOptions((short)15);
/* 49:54 */     sp.setRecordId((short)-4086);
/* 50:55 */     sp.setOptions((short)3218);
/* 51:   */     
/* 52:57 */     sp.setFlags(2560);
/* 53:58 */     opt.setRecordId((short)-4085);
/* 54:59 */     opt.addEscherProperty(new EscherBoolProperty((short)127, 17039620));
/* 55:60 */     opt.addEscherProperty(new EscherBoolProperty((short)191, 524296));
/* 56:61 */     opt.addEscherProperty(new EscherBoolProperty((short)511, 524288));
/* 57:62 */     opt.addEscherProperty(new EscherSimpleProperty((short)959, 131072));
/* 58:   */     
/* 59:64 */     HSSFClientAnchor userAnchor = (HSSFClientAnchor)getAnchor();
/* 60:65 */     userAnchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_DO_RESIZE);
/* 61:66 */     EscherRecord anchor = userAnchor.getEscherAnchor();
/* 62:67 */     clientData.setRecordId((short)-4079);
/* 63:68 */     clientData.setOptions((short)0);
/* 64:   */     
/* 65:70 */     spContainer.addChildRecord(sp);
/* 66:71 */     spContainer.addChildRecord(opt);
/* 67:72 */     spContainer.addChildRecord(anchor);
/* 68:73 */     spContainer.addChildRecord(clientData);
/* 69:   */     
/* 70:75 */     return spContainer;
/* 71:   */   }
/* 72:   */   
/* 73:   */   protected ObjRecord createObjRecord()
/* 74:   */   {
/* 75:80 */     ObjRecord obj = new ObjRecord();
/* 76:81 */     CommonObjectDataSubRecord c = new CommonObjectDataSubRecord();
/* 77:82 */     c.setObjectType((short)201);
/* 78:83 */     c.setLocked(true);
/* 79:84 */     c.setPrintable(false);
/* 80:85 */     c.setAutofill(true);
/* 81:86 */     c.setAutoline(false);
/* 82:87 */     FtCblsSubRecord f = new FtCblsSubRecord();
/* 83:88 */     LbsDataSubRecord l = LbsDataSubRecord.newAutoFilterInstance();
/* 84:89 */     EndSubRecord e = new EndSubRecord();
/* 85:90 */     obj.addSubRecord(c);
/* 86:91 */     obj.addSubRecord(f);
/* 87:92 */     obj.addSubRecord(l);
/* 88:93 */     obj.addSubRecord(e);
/* 89:94 */     return obj;
/* 90:   */   }
/* 91:   */   
/* 92:   */   public void setShapeType(int shapeType)
/* 93:   */   {
/* 94:99 */     throw new IllegalStateException("Shape type can not be changed in " + getClass().getSimpleName());
/* 95:   */   }
/* 96:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFCombobox
 * JD-Core Version:    0.7.0.1
 */