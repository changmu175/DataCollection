/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import org.apache.poi.util.HexDump;
/*   5:    */ import org.apache.poi.util.LittleEndianInput;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ import org.apache.poi.util.LittleEndianOutputStream;
/*   8:    */ 
/*   9:    */ public abstract class SubRecord
/*  10:    */ {
/*  11:    */   public static SubRecord createSubRecord(LittleEndianInput in, int cmoOt)
/*  12:    */   {
/*  13: 44 */     int sid = in.readUShort();
/*  14: 45 */     int secondUShort = in.readUShort();
/*  15: 47 */     switch (sid)
/*  16:    */     {
/*  17:    */     case 21: 
/*  18: 49 */       return new CommonObjectDataSubRecord(in, secondUShort);
/*  19:    */     case 9: 
/*  20: 51 */       return new EmbeddedObjectRefSubRecord(in, secondUShort);
/*  21:    */     case 6: 
/*  22: 53 */       return new GroupMarkerSubRecord(in, secondUShort);
/*  23:    */     case 0: 
/*  24: 55 */       return new EndSubRecord(in, secondUShort);
/*  25:    */     case 13: 
/*  26: 57 */       return new NoteStructureSubRecord(in, secondUShort);
/*  27:    */     case 19: 
/*  28: 59 */       return new LbsDataSubRecord(in, secondUShort, cmoOt);
/*  29:    */     case 12: 
/*  30: 61 */       return new FtCblsSubRecord(in, secondUShort);
/*  31:    */     case 8: 
/*  32: 63 */       return new FtPioGrbitSubRecord(in, secondUShort);
/*  33:    */     case 7: 
/*  34: 65 */       return new FtCfSubRecord(in, secondUShort);
/*  35:    */     }
/*  36: 67 */     return new UnknownSubRecord(in, sid, secondUShort);
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected abstract int getDataSize();
/*  40:    */   
/*  41:    */   public byte[] serialize()
/*  42:    */   {
/*  43: 77 */     int size = getDataSize() + 4;
/*  44: 78 */     ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
/*  45: 79 */     serialize(new LittleEndianOutputStream(baos));
/*  46: 80 */     if (baos.size() != size) {
/*  47: 81 */       throw new RuntimeException("write size mismatch");
/*  48:    */     }
/*  49: 83 */     return baos.toByteArray();
/*  50:    */   }
/*  51:    */   
/*  52:    */   public abstract void serialize(LittleEndianOutput paramLittleEndianOutput);
/*  53:    */   
/*  54:    */   public abstract SubRecord clone();
/*  55:    */   
/*  56:    */   public boolean isTerminating()
/*  57:    */   {
/*  58:100 */     return false;
/*  59:    */   }
/*  60:    */   
/*  61:    */   private static final class UnknownSubRecord
/*  62:    */     extends SubRecord
/*  63:    */   {
/*  64:    */     private final int _sid;
/*  65:    */     private final byte[] _data;
/*  66:    */     
/*  67:    */     public UnknownSubRecord(LittleEndianInput in, int sid, int size)
/*  68:    */     {
/*  69:109 */       this._sid = sid;
/*  70:110 */       byte[] buf = new byte[size];
/*  71:111 */       in.readFully(buf);
/*  72:112 */       this._data = buf;
/*  73:    */     }
/*  74:    */     
/*  75:    */     protected int getDataSize()
/*  76:    */     {
/*  77:116 */       return this._data.length;
/*  78:    */     }
/*  79:    */     
/*  80:    */     public void serialize(LittleEndianOutput out)
/*  81:    */     {
/*  82:120 */       out.writeShort(this._sid);
/*  83:121 */       out.writeShort(this._data.length);
/*  84:122 */       out.write(this._data);
/*  85:    */     }
/*  86:    */     
/*  87:    */     public UnknownSubRecord clone()
/*  88:    */     {
/*  89:126 */       return this;
/*  90:    */     }
/*  91:    */     
/*  92:    */     public String toString()
/*  93:    */     {
/*  94:130 */       StringBuilder sb = new StringBuilder(64);
/*  95:131 */       sb.append(getClass().getName()).append(" [");
/*  96:132 */       sb.append("sid=").append(HexDump.shortToHex(this._sid));
/*  97:133 */       sb.append(" size=").append(this._data.length);
/*  98:134 */       sb.append(" : ").append(HexDump.toHex(this._data));
/*  99:135 */       sb.append("]\n");
/* 100:136 */       return sb.toString();
/* 101:    */     }
/* 102:    */   }
/* 103:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.SubRecord
 * JD-Core Version:    0.7.0.1
 */