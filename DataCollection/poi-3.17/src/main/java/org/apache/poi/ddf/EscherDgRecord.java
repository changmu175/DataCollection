/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndian;
/*   4:    */ 
/*   5:    */ public class EscherDgRecord
/*   6:    */   extends EscherRecord
/*   7:    */ {
/*   8:    */   public static final short RECORD_ID = -4088;
/*   9:    */   public static final String RECORD_DESCRIPTION = "MsofbtDg";
/*  10:    */   private int field_1_numShapes;
/*  11:    */   private int field_2_lastMSOSPID;
/*  12:    */   
/*  13:    */   public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory)
/*  14:    */   {
/*  15: 38 */     readHeader(data, offset);
/*  16: 39 */     int pos = offset + 8;
/*  17: 40 */     int size = 0;
/*  18: 41 */     this.field_1_numShapes = LittleEndian.getInt(data, pos + size);size += 4;
/*  19: 42 */     this.field_2_lastMSOSPID = LittleEndian.getInt(data, pos + size);size += 4;
/*  20:    */     
/*  21:    */ 
/*  22:    */ 
/*  23: 46 */     return getRecordSize();
/*  24:    */   }
/*  25:    */   
/*  26:    */   public int serialize(int offset, byte[] data, EscherSerializationListener listener)
/*  27:    */   {
/*  28: 52 */     listener.beforeRecordSerialize(offset, getRecordId(), this);
/*  29:    */     
/*  30: 54 */     LittleEndian.putShort(data, offset, getOptions());
/*  31: 55 */     LittleEndian.putShort(data, offset + 2, getRecordId());
/*  32: 56 */     LittleEndian.putInt(data, offset + 4, 8);
/*  33: 57 */     LittleEndian.putInt(data, offset + 8, this.field_1_numShapes);
/*  34: 58 */     LittleEndian.putInt(data, offset + 12, this.field_2_lastMSOSPID);
/*  35:    */     
/*  36:    */ 
/*  37:    */ 
/*  38: 62 */     listener.afterRecordSerialize(offset + 16, getRecordId(), getRecordSize(), this);
/*  39: 63 */     return getRecordSize();
/*  40:    */   }
/*  41:    */   
/*  42:    */   public int getRecordSize()
/*  43:    */   {
/*  44: 74 */     return 16;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public short getRecordId()
/*  48:    */   {
/*  49: 79 */     return -4088;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public String getRecordName()
/*  53:    */   {
/*  54: 84 */     return "Dg";
/*  55:    */   }
/*  56:    */   
/*  57:    */   public int getNumShapes()
/*  58:    */   {
/*  59: 94 */     return this.field_1_numShapes;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setNumShapes(int field_1_numShapes)
/*  63:    */   {
/*  64:104 */     this.field_1_numShapes = field_1_numShapes;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public int getLastMSOSPID()
/*  68:    */   {
/*  69:114 */     return this.field_2_lastMSOSPID;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setLastMSOSPID(int field_2_lastMSOSPID)
/*  73:    */   {
/*  74:124 */     this.field_2_lastMSOSPID = field_2_lastMSOSPID;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public short getDrawingGroupId()
/*  78:    */   {
/*  79:135 */     return (short)(getOptions() >> 4);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void incrementShapeCount()
/*  83:    */   {
/*  84:143 */     this.field_1_numShapes += 1;
/*  85:    */   }
/*  86:    */   
/*  87:    */   protected Object[][] getAttributeMap()
/*  88:    */   {
/*  89:148 */     return new Object[][] { { "NumShapes", Integer.valueOf(this.field_1_numShapes) }, { "LastMSOSPID", Integer.valueOf(this.field_2_lastMSOSPID) } };
/*  90:    */   }
/*  91:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherDgRecord
 * JD-Core Version:    0.7.0.1
 */