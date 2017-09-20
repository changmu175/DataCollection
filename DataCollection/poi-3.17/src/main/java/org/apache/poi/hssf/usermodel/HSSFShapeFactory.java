/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.List;
/*   4:    */ import java.util.Map;
/*   5:    */ import org.apache.poi.ddf.EscherClientDataRecord;
/*   6:    */ import org.apache.poi.ddf.EscherContainerRecord;
/*   7:    */ import org.apache.poi.ddf.EscherOptRecord;
/*   8:    */ import org.apache.poi.ddf.EscherProperty;
/*   9:    */ import org.apache.poi.ddf.EscherRecord;
/*  10:    */ import org.apache.poi.hssf.record.CommonObjectDataSubRecord;
/*  11:    */ import org.apache.poi.hssf.record.EmbeddedObjectRefSubRecord;
/*  12:    */ import org.apache.poi.hssf.record.EscherAggregate;
/*  13:    */ import org.apache.poi.hssf.record.ObjRecord;
/*  14:    */ import org.apache.poi.hssf.record.Record;
/*  15:    */ import org.apache.poi.hssf.record.SubRecord;
/*  16:    */ import org.apache.poi.hssf.record.TextObjectRecord;
/*  17:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  18:    */ import org.apache.poi.util.RecordFormatException;
/*  19:    */ 
/*  20:    */ public class HSSFShapeFactory
/*  21:    */ {
/*  22:    */   public static void createShapeTree(EscherContainerRecord container, EscherAggregate agg, HSSFShapeContainer out, DirectoryNode root)
/*  23:    */   {
/*  24: 52 */     if (container.getRecordId() == -4093)
/*  25:    */     {
/*  26: 53 */       ObjRecord obj = null;
/*  27: 54 */       EscherClientDataRecord clientData = (EscherClientDataRecord)((EscherContainerRecord)container.getChild(0)).getChildById((short)-4079);
/*  28: 55 */       if (null != clientData) {
/*  29: 56 */         obj = (ObjRecord)agg.getShapeToObjMapping().get(clientData);
/*  30:    */       }
/*  31: 58 */       HSSFShapeGroup group = new HSSFShapeGroup(container, obj);
/*  32: 59 */       List<EscherContainerRecord> children = container.getChildContainers();
/*  33: 61 */       for (int i = 0; i < children.size(); i++)
/*  34:    */       {
/*  35: 62 */         EscherContainerRecord spContainer = (EscherContainerRecord)children.get(i);
/*  36: 63 */         if (i != 0) {
/*  37: 64 */           createShapeTree(spContainer, agg, group, root);
/*  38:    */         }
/*  39:    */       }
/*  40: 67 */       out.addShape(group);
/*  41:    */     }
/*  42: 68 */     else if (container.getRecordId() == -4092)
/*  43:    */     {
/*  44: 69 */       Map<EscherRecord, Record> shapeToObj = agg.getShapeToObjMapping();
/*  45: 70 */       ObjRecord objRecord = null;
/*  46: 71 */       TextObjectRecord txtRecord = null;
/*  47: 73 */       for (EscherRecord record : container) {
/*  48: 74 */         switch (record.getRecordId())
/*  49:    */         {
/*  50:    */         case -4079: 
/*  51: 76 */           objRecord = (ObjRecord)shapeToObj.get(record);
/*  52: 77 */           break;
/*  53:    */         case -4083: 
/*  54: 79 */           txtRecord = (TextObjectRecord)shapeToObj.get(record);
/*  55:    */         }
/*  56:    */       }
/*  57: 85 */       if (objRecord == null) {
/*  58: 86 */         throw new RecordFormatException("EscherClientDataRecord can't be found.");
/*  59:    */       }
/*  60: 88 */       if (isEmbeddedObject(objRecord))
/*  61:    */       {
/*  62: 89 */         HSSFObjectData objectData = new HSSFObjectData(container, objRecord, root);
/*  63: 90 */         out.addShape(objectData);
/*  64: 91 */         return;
/*  65:    */       }
/*  66: 93 */       CommonObjectDataSubRecord cmo = (CommonObjectDataSubRecord)objRecord.getSubRecords().get(0);
/*  67:    */       HSSFShape shape;
/*  68:    */       HSSFShape shape;
/*  69: 95 */       switch (cmo.getObjectType())
/*  70:    */       {
/*  71:    */       case 8: 
/*  72: 97 */         shape = new HSSFPicture(container, objRecord);
/*  73: 98 */         break;
/*  74:    */       case 2: 
/*  75:100 */         shape = new HSSFSimpleShape(container, objRecord, txtRecord);
/*  76:101 */         break;
/*  77:    */       case 1: 
/*  78:103 */         shape = new HSSFSimpleShape(container, objRecord);
/*  79:104 */         break;
/*  80:    */       case 20: 
/*  81:106 */         shape = new HSSFCombobox(container, objRecord);
/*  82:107 */         break;
/*  83:    */       case 30: 
/*  84:109 */         EscherOptRecord optRecord = (EscherOptRecord)container.getChildById((short)-4085);
/*  85:110 */         if (optRecord == null)
/*  86:    */         {
/*  87:111 */           shape = new HSSFSimpleShape(container, objRecord, txtRecord);
/*  88:    */         }
/*  89:    */         else
/*  90:    */         {
/*  91:113 */           EscherProperty property = optRecord.lookup(325);
/*  92:    */           HSSFShape shape;
/*  93:114 */           if (null != property) {
/*  94:115 */             shape = new HSSFPolygon(container, objRecord, txtRecord);
/*  95:    */           } else {
/*  96:117 */             shape = new HSSFSimpleShape(container, objRecord, txtRecord);
/*  97:    */           }
/*  98:    */         }
/*  99:120 */         break;
/* 100:    */       case 6: 
/* 101:122 */         shape = new HSSFTextbox(container, objRecord, txtRecord);
/* 102:123 */         break;
/* 103:    */       case 25: 
/* 104:125 */         shape = new HSSFComment(container, objRecord, txtRecord, agg.getNoteRecordByObj(objRecord));
/* 105:126 */         break;
/* 106:    */       default: 
/* 107:128 */         shape = new HSSFSimpleShape(container, objRecord, txtRecord);
/* 108:    */       }
/* 109:130 */       out.addShape(shape);
/* 110:    */     }
/* 111:    */   }
/* 112:    */   
/* 113:    */   private static boolean isEmbeddedObject(ObjRecord obj)
/* 114:    */   {
/* 115:135 */     for (SubRecord sub : obj.getSubRecords()) {
/* 116:136 */       if ((sub instanceof EmbeddedObjectRefSubRecord)) {
/* 117:137 */         return true;
/* 118:    */       }
/* 119:    */     }
/* 120:140 */     return false;
/* 121:    */   }
/* 122:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFShapeFactory
 * JD-Core Version:    0.7.0.1
 */