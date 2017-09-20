/*   1:    */ package org.apache.poi.ss.formula;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   5:    */ import org.apache.poi.ss.util.CellReference;
/*   6:    */ import org.apache.poi.util.LittleEndian;
/*   7:    */ import org.apache.poi.util.LittleEndianByteArrayInputStream;
/*   8:    */ import org.apache.poi.util.LittleEndianInput;
/*   9:    */ import org.apache.poi.util.LittleEndianOutput;
/*  10:    */ 
/*  11:    */ public class Formula
/*  12:    */ {
/*  13: 38 */   private static final Formula EMPTY = new Formula(new byte[0], 0);
/*  14:    */   private final byte[] _byteEncoding;
/*  15:    */   private final int _encodedTokenLen;
/*  16:    */   
/*  17:    */   private Formula(byte[] byteEncoding, int encodedTokenLen)
/*  18:    */   {
/*  19: 45 */     this._byteEncoding = ((byte[])byteEncoding.clone());
/*  20: 46 */     this._encodedTokenLen = encodedTokenLen;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public static Formula read(int encodedTokenLen, LittleEndianInput in)
/*  24:    */   {
/*  25: 65 */     return read(encodedTokenLen, in, encodedTokenLen);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public static Formula read(int encodedTokenLen, LittleEndianInput in, int totalEncodedLen)
/*  29:    */   {
/*  30: 75 */     byte[] byteEncoding = new byte[totalEncodedLen];
/*  31: 76 */     in.readFully(byteEncoding);
/*  32: 77 */     return new Formula(byteEncoding, encodedTokenLen);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public Ptg[] getTokens()
/*  36:    */   {
/*  37: 81 */     LittleEndianInput in = new LittleEndianByteArrayInputStream(this._byteEncoding);
/*  38: 82 */     return Ptg.readTokens(this._encodedTokenLen, in);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void serialize(LittleEndianOutput out)
/*  42:    */   {
/*  43: 93 */     out.writeShort(this._encodedTokenLen);
/*  44: 94 */     out.write(this._byteEncoding);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void serializeTokens(LittleEndianOutput out)
/*  48:    */   {
/*  49: 98 */     out.write(this._byteEncoding, 0, this._encodedTokenLen);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void serializeArrayConstantData(LittleEndianOutput out)
/*  53:    */   {
/*  54:101 */     int len = this._byteEncoding.length - this._encodedTokenLen;
/*  55:102 */     out.write(this._byteEncoding, this._encodedTokenLen, len);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public int getEncodedSize()
/*  59:    */   {
/*  60:116 */     return 2 + this._byteEncoding.length;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public int getEncodedTokenSize()
/*  64:    */   {
/*  65:126 */     return this._encodedTokenLen;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public static Formula create(Ptg[] ptgs)
/*  69:    */   {
/*  70:136 */     if ((ptgs == null) || (ptgs.length < 1)) {
/*  71:137 */       return EMPTY;
/*  72:    */     }
/*  73:139 */     int totalSize = Ptg.getEncodedSize(ptgs);
/*  74:140 */     byte[] encodedData = new byte[totalSize];
/*  75:141 */     Ptg.serializePtgs(ptgs, encodedData, 0);
/*  76:142 */     int encodedTokenLen = Ptg.getEncodedSizeWithoutArrayData(ptgs);
/*  77:143 */     return new Formula(encodedData, encodedTokenLen);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public static Ptg[] getTokens(Formula formula)
/*  81:    */   {
/*  82:153 */     if (formula == null) {
/*  83:154 */       return null;
/*  84:    */     }
/*  85:156 */     return formula.getTokens();
/*  86:    */   }
/*  87:    */   
/*  88:    */   public Formula copy()
/*  89:    */   {
/*  90:161 */     return this;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public CellReference getExpReference()
/*  94:    */   {
/*  95:175 */     byte[] data = this._byteEncoding;
/*  96:176 */     if (data.length != 5) {
/*  97:178 */       return null;
/*  98:    */     }
/*  99:180 */     switch (data[0])
/* 100:    */     {
/* 101:    */     case 1: 
/* 102:    */       break;
/* 103:    */     case 2: 
/* 104:    */       break;
/* 105:    */     default: 
/* 106:186 */       return null;
/* 107:    */     }
/* 108:188 */     int firstRow = LittleEndian.getUShort(data, 1);
/* 109:189 */     int firstColumn = LittleEndian.getUShort(data, 3);
/* 110:190 */     return new CellReference(firstRow, firstColumn);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public boolean isSame(Formula other)
/* 114:    */   {
/* 115:193 */     return Arrays.equals(this._byteEncoding, other._byteEncoding);
/* 116:    */   }
/* 117:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.Formula
 * JD-Core Version:    0.7.0.1
 */