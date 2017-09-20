/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.poi.ddf.DefaultEscherRecordFactory;
/*   7:    */ import org.apache.poi.ddf.EscherBSERecord;
/*   8:    */ import org.apache.poi.ddf.EscherContainerRecord;
/*   9:    */ import org.apache.poi.hssf.model.InternalWorkbook;
/*  10:    */ import org.apache.poi.hssf.record.EmbeddedObjectRefSubRecord;
/*  11:    */ import org.apache.poi.hssf.record.EscherAggregate;
/*  12:    */ import org.apache.poi.hssf.record.ObjRecord;
/*  13:    */ import org.apache.poi.hssf.record.SubRecord;
/*  14:    */ import org.apache.poi.poifs.filesystem.DirectoryEntry;
/*  15:    */ import org.apache.poi.poifs.filesystem.Entry;
/*  16:    */ import org.apache.poi.ss.usermodel.ObjectData;
/*  17:    */ import org.apache.poi.util.HexDump;
/*  18:    */ 
/*  19:    */ public final class HSSFObjectData
/*  20:    */   extends HSSFPicture
/*  21:    */   implements ObjectData
/*  22:    */ {
/*  23:    */   private final DirectoryEntry _root;
/*  24:    */   
/*  25:    */   public HSSFObjectData(EscherContainerRecord spContainer, ObjRecord objRecord, DirectoryEntry _root)
/*  26:    */   {
/*  27: 43 */     super(spContainer, objRecord);
/*  28: 44 */     this._root = _root;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public String getOLE2ClassName()
/*  32:    */   {
/*  33: 49 */     return findObjectRecord().getOLEClassName();
/*  34:    */   }
/*  35:    */   
/*  36:    */   public DirectoryEntry getDirectory()
/*  37:    */     throws IOException
/*  38:    */   {
/*  39: 54 */     EmbeddedObjectRefSubRecord subRecord = findObjectRecord();
/*  40:    */     
/*  41: 56 */     int streamId = subRecord.getStreamId().intValue();
/*  42: 57 */     String streamName = "MBD" + HexDump.toHex(streamId);
/*  43:    */     
/*  44: 59 */     Entry entry = this._root.getEntry(streamName);
/*  45: 60 */     if ((entry instanceof DirectoryEntry)) {
/*  46: 61 */       return (DirectoryEntry)entry;
/*  47:    */     }
/*  48: 63 */     throw new IOException("Stream " + streamName + " was not an OLE2 directory");
/*  49:    */   }
/*  50:    */   
/*  51:    */   public byte[] getObjectData()
/*  52:    */   {
/*  53: 68 */     return findObjectRecord().getObjectData();
/*  54:    */   }
/*  55:    */   
/*  56:    */   public boolean hasDirectoryEntry()
/*  57:    */   {
/*  58: 73 */     EmbeddedObjectRefSubRecord subRecord = findObjectRecord();
/*  59:    */     
/*  60:    */ 
/*  61: 76 */     Integer streamId = subRecord.getStreamId();
/*  62: 77 */     return (streamId != null) && (streamId.intValue() != 0);
/*  63:    */   }
/*  64:    */   
/*  65:    */   protected EmbeddedObjectRefSubRecord findObjectRecord()
/*  66:    */   {
/*  67: 85 */     Iterator<SubRecord> subRecordIter = getObjRecord().getSubRecords().iterator();
/*  68: 87 */     while (subRecordIter.hasNext())
/*  69:    */     {
/*  70: 88 */       Object subRecord = subRecordIter.next();
/*  71: 89 */       if ((subRecord instanceof EmbeddedObjectRefSubRecord)) {
/*  72: 90 */         return (EmbeddedObjectRefSubRecord)subRecord;
/*  73:    */       }
/*  74:    */     }
/*  75: 94 */     throw new IllegalStateException("Object data does not contain a reference to an embedded object OLE2 directory");
/*  76:    */   }
/*  77:    */   
/*  78:    */   protected EscherContainerRecord createSpContainer()
/*  79:    */   {
/*  80: 99 */     throw new IllegalStateException("HSSFObjectData cannot be created from scratch");
/*  81:    */   }
/*  82:    */   
/*  83:    */   protected ObjRecord createObjRecord()
/*  84:    */   {
/*  85:104 */     throw new IllegalStateException("HSSFObjectData cannot be created from scratch");
/*  86:    */   }
/*  87:    */   
/*  88:    */   protected void afterRemove(HSSFPatriarch patriarch)
/*  89:    */   {
/*  90:109 */     throw new IllegalStateException("HSSFObjectData cannot be created from scratch");
/*  91:    */   }
/*  92:    */   
/*  93:    */   void afterInsert(HSSFPatriarch patriarch)
/*  94:    */   {
/*  95:114 */     EscherAggregate agg = patriarch.getBoundAggregate();
/*  96:115 */     agg.associateShapeToObjRecord(getEscherContainer().getChildById((short)-4079), getObjRecord());
/*  97:116 */     EscherBSERecord bse = patriarch.getSheet().getWorkbook().getWorkbook().getBSERecord(getPictureIndex());
/*  98:    */     
/*  99:118 */     bse.setRef(bse.getRef() + 1);
/* 100:    */   }
/* 101:    */   
/* 102:    */   protected HSSFShape cloneShape()
/* 103:    */   {
/* 104:123 */     EscherContainerRecord spContainer = new EscherContainerRecord();
/* 105:124 */     byte[] inSp = getEscherContainer().serialize();
/* 106:125 */     spContainer.fillFields(inSp, 0, new DefaultEscherRecordFactory());
/* 107:126 */     ObjRecord obj = (ObjRecord)getObjRecord().cloneViaReserialise();
/* 108:127 */     return new HSSFObjectData(spContainer, obj, this._root);
/* 109:    */   }
/* 110:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFObjectData
 * JD-Core Version:    0.7.0.1
 */