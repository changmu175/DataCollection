/*   1:    */ package org.apache.poi.hssf.record.pivottable;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ import org.apache.poi.util.RecordFormatException;
/*   8:    */ 
/*   9:    */ public final class PageItemRecord
/*  10:    */   extends StandardRecord
/*  11:    */ {
/*  12:    */   public static final short sid = 182;
/*  13:    */   private final FieldInfo[] _fieldInfos;
/*  14:    */   
/*  15:    */   private static final class FieldInfo
/*  16:    */   {
/*  17:    */     public static final int ENCODED_SIZE = 6;
/*  18:    */     private int _isxvi;
/*  19:    */     private int _isxvd;
/*  20:    */     private int _idObj;
/*  21:    */     
/*  22:    */     public FieldInfo(RecordInputStream in)
/*  23:    */     {
/*  24: 44 */       this._isxvi = in.readShort();
/*  25: 45 */       this._isxvd = in.readShort();
/*  26: 46 */       this._idObj = in.readShort();
/*  27:    */     }
/*  28:    */     
/*  29:    */     protected void serialize(LittleEndianOutput out)
/*  30:    */     {
/*  31: 50 */       out.writeShort(this._isxvi);
/*  32: 51 */       out.writeShort(this._isxvd);
/*  33: 52 */       out.writeShort(this._idObj);
/*  34:    */     }
/*  35:    */     
/*  36:    */     public void appendDebugInfo(StringBuffer sb)
/*  37:    */     {
/*  38: 56 */       sb.append('(');
/*  39: 57 */       sb.append("isxvi=").append(HexDump.shortToHex(this._isxvi));
/*  40: 58 */       sb.append(" isxvd=").append(HexDump.shortToHex(this._isxvd));
/*  41: 59 */       sb.append(" idObj=").append(HexDump.shortToHex(this._idObj));
/*  42: 60 */       sb.append(')');
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   public PageItemRecord(RecordInputStream in)
/*  47:    */   {
/*  48: 67 */     int dataSize = in.remaining();
/*  49: 68 */     if (dataSize % 6 != 0) {
/*  50: 69 */       throw new RecordFormatException("Bad data size " + dataSize);
/*  51:    */     }
/*  52: 72 */     int nItems = dataSize / 6;
/*  53:    */     
/*  54: 74 */     FieldInfo[] fis = new FieldInfo[nItems];
/*  55: 75 */     for (int i = 0; i < fis.length; i++) {
/*  56: 76 */       fis[i] = new FieldInfo(in);
/*  57:    */     }
/*  58: 78 */     this._fieldInfos = fis;
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected void serialize(LittleEndianOutput out)
/*  62:    */   {
/*  63: 83 */     for (int i = 0; i < this._fieldInfos.length; i++) {
/*  64: 84 */       this._fieldInfos[i].serialize(out);
/*  65:    */     }
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected int getDataSize()
/*  69:    */   {
/*  70: 90 */     return this._fieldInfos.length * 6;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public short getSid()
/*  74:    */   {
/*  75: 95 */     return 182;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public String toString()
/*  79:    */   {
/*  80:100 */     StringBuffer sb = new StringBuffer();
/*  81:    */     
/*  82:102 */     sb.append("[SXPI]\n");
/*  83:103 */     for (int i = 0; i < this._fieldInfos.length; i++)
/*  84:    */     {
/*  85:104 */       sb.append("    item[").append(i).append("]=");
/*  86:105 */       this._fieldInfos[i].appendDebugInfo(sb);
/*  87:106 */       sb.append('\n');
/*  88:    */     }
/*  89:108 */     sb.append("[/SXPI]\n");
/*  90:109 */     return sb.toString();
/*  91:    */   }
/*  92:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.pivottable.PageItemRecord
 * JD-Core Version:    0.7.0.1
 */