/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.HexDump;
/*   4:    */ import org.apache.poi.util.LittleEndianInput;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ import org.apache.poi.util.RecordFormatException;
/*   7:    */ 
/*   8:    */ public final class FtPioGrbitSubRecord
/*   9:    */   extends SubRecord
/*  10:    */   implements Cloneable
/*  11:    */ {
/*  12:    */   public static final short sid = 8;
/*  13:    */   public static final short length = 2;
/*  14:    */   public static final int AUTO_PICT_BIT = 1;
/*  15:    */   public static final int DDE_BIT = 2;
/*  16:    */   public static final int PRINT_CALC_BIT = 4;
/*  17:    */   public static final int ICON_BIT = 8;
/*  18:    */   public static final int CTL_BIT = 16;
/*  19:    */   public static final int PRSTM_BIT = 32;
/*  20:    */   public static final int CAMERA_BIT = 128;
/*  21:    */   public static final int DEFAULT_SIZE_BIT = 256;
/*  22:    */   public static final int AUTO_LOAD_BIT = 512;
/*  23: 86 */   private short flags = 0;
/*  24:    */   
/*  25:    */   public FtPioGrbitSubRecord() {}
/*  26:    */   
/*  27:    */   public FtPioGrbitSubRecord(LittleEndianInput in, int size)
/*  28:    */   {
/*  29: 96 */     if (size != 2) {
/*  30: 97 */       throw new RecordFormatException("Unexpected size (" + size + ")");
/*  31:    */     }
/*  32: 99 */     this.flags = in.readShort();
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setFlagByBit(int bitmask, boolean enabled)
/*  36:    */   {
/*  37:108 */     if (enabled) {
/*  38:109 */       this.flags = ((short)(this.flags | bitmask));
/*  39:    */     } else {
/*  40:111 */       this.flags = ((short)(this.flags & (0xFFFF ^ bitmask)));
/*  41:    */     }
/*  42:    */   }
/*  43:    */   
/*  44:    */   public boolean getFlagByBit(int bitmask)
/*  45:    */   {
/*  46:116 */     return (this.flags & bitmask) != 0;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public String toString()
/*  50:    */   {
/*  51:124 */     StringBuffer buffer = new StringBuffer();
/*  52:125 */     buffer.append("[FtPioGrbit ]\n");
/*  53:126 */     buffer.append("  size     = ").append(2).append("\n");
/*  54:127 */     buffer.append("  flags    = ").append(HexDump.toHex(this.flags)).append("\n");
/*  55:128 */     buffer.append("[/FtPioGrbit ]\n");
/*  56:129 */     return buffer.toString();
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void serialize(LittleEndianOutput out)
/*  60:    */   {
/*  61:138 */     out.writeShort(8);
/*  62:139 */     out.writeShort(2);
/*  63:140 */     out.writeShort(this.flags);
/*  64:    */   }
/*  65:    */   
/*  66:    */   protected int getDataSize()
/*  67:    */   {
/*  68:144 */     return 2;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public short getSid()
/*  72:    */   {
/*  73:152 */     return 8;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public FtPioGrbitSubRecord clone()
/*  77:    */   {
/*  78:157 */     FtPioGrbitSubRecord rec = new FtPioGrbitSubRecord();
/*  79:158 */     rec.flags = this.flags;
/*  80:159 */     return rec;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public short getFlags()
/*  84:    */   {
/*  85:163 */     return this.flags;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void setFlags(short flags)
/*  89:    */   {
/*  90:167 */     this.flags = flags;
/*  91:    */   }
/*  92:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.FtPioGrbitSubRecord
 * JD-Core Version:    0.7.0.1
 */