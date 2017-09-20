/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.HexDump;
/*   4:    */ import org.apache.poi.util.LittleEndianInput;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ 
/*   7:    */ public final class DrawingSelectionRecord
/*   8:    */   extends StandardRecord
/*   9:    */   implements Cloneable
/*  10:    */ {
/*  11:    */   public static final short sid = 237;
/*  12:    */   private OfficeArtRecordHeader _header;
/*  13:    */   private int _cpsp;
/*  14:    */   private int _dgslk;
/*  15:    */   private int _spidFocus;
/*  16:    */   private int[] _shapeIds;
/*  17:    */   
/*  18:    */   private static final class OfficeArtRecordHeader
/*  19:    */   {
/*  20:    */     public static final int ENCODED_SIZE = 8;
/*  21:    */     private final int _verAndInstance;
/*  22:    */     private final int _type;
/*  23:    */     private final int _length;
/*  24:    */     
/*  25:    */     public OfficeArtRecordHeader(LittleEndianInput in)
/*  26:    */     {
/*  27: 48 */       this._verAndInstance = in.readUShort();
/*  28: 49 */       this._type = in.readUShort();
/*  29: 50 */       this._length = in.readInt();
/*  30:    */     }
/*  31:    */     
/*  32:    */     public void serialize(LittleEndianOutput out)
/*  33:    */     {
/*  34: 54 */       out.writeShort(this._verAndInstance);
/*  35: 55 */       out.writeShort(this._type);
/*  36: 56 */       out.writeInt(this._length);
/*  37:    */     }
/*  38:    */     
/*  39:    */     public String debugFormatAsString()
/*  40:    */     {
/*  41: 60 */       StringBuffer sb = new StringBuffer(32);
/*  42: 61 */       sb.append("ver+inst=").append(HexDump.shortToHex(this._verAndInstance));
/*  43: 62 */       sb.append(" type=").append(HexDump.shortToHex(this._type));
/*  44: 63 */       sb.append(" len=").append(HexDump.intToHex(this._length));
/*  45: 64 */       return sb.toString();
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public DrawingSelectionRecord(RecordInputStream in)
/*  50:    */   {
/*  51: 79 */     this._header = new OfficeArtRecordHeader(in);
/*  52: 80 */     this._cpsp = in.readInt();
/*  53: 81 */     this._dgslk = in.readInt();
/*  54: 82 */     this._spidFocus = in.readInt();
/*  55: 83 */     int nShapes = in.available() / 4;
/*  56: 84 */     int[] shapeIds = new int[nShapes];
/*  57: 85 */     for (int i = 0; i < nShapes; i++) {
/*  58: 86 */       shapeIds[i] = in.readInt();
/*  59:    */     }
/*  60: 88 */     this._shapeIds = shapeIds;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public short getSid()
/*  64:    */   {
/*  65: 92 */     return 237;
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected int getDataSize()
/*  69:    */   {
/*  70: 96 */     return 20 + this._shapeIds.length * 4;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void serialize(LittleEndianOutput out)
/*  74:    */   {
/*  75:102 */     this._header.serialize(out);
/*  76:103 */     out.writeInt(this._cpsp);
/*  77:104 */     out.writeInt(this._dgslk);
/*  78:105 */     out.writeInt(this._spidFocus);
/*  79:106 */     for (int i = 0; i < this._shapeIds.length; i++) {
/*  80:107 */       out.writeInt(this._shapeIds[i]);
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   public DrawingSelectionRecord clone()
/*  85:    */   {
/*  86:114 */     return this;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public String toString()
/*  90:    */   {
/*  91:118 */     StringBuffer sb = new StringBuffer();
/*  92:    */     
/*  93:120 */     sb.append("[MSODRAWINGSELECTION]\n");
/*  94:121 */     sb.append("    .rh       =(").append(this._header.debugFormatAsString()).append(")\n");
/*  95:122 */     sb.append("    .cpsp     =").append(HexDump.intToHex(this._cpsp)).append('\n');
/*  96:123 */     sb.append("    .dgslk    =").append(HexDump.intToHex(this._dgslk)).append('\n');
/*  97:124 */     sb.append("    .spidFocus=").append(HexDump.intToHex(this._spidFocus)).append('\n');
/*  98:125 */     sb.append("    .shapeIds =(");
/*  99:126 */     for (int i = 0; i < this._shapeIds.length; i++)
/* 100:    */     {
/* 101:127 */       if (i > 0) {
/* 102:128 */         sb.append(", ");
/* 103:    */       }
/* 104:130 */       sb.append(HexDump.intToHex(this._shapeIds[i]));
/* 105:    */     }
/* 106:132 */     sb.append(")\n");
/* 107:    */     
/* 108:134 */     sb.append("[/MSODRAWINGSELECTION]\n");
/* 109:135 */     return sb.toString();
/* 110:    */   }
/* 111:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.DrawingSelectionRecord
 * JD-Core Version:    0.7.0.1
 */