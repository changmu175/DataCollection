/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.HexDump;
/*  4:   */ 
/*  5:   */ public class IllegalVariantTypeException
/*  6:   */   extends VariantTypeException
/*  7:   */ {
/*  8:   */   public IllegalVariantTypeException(long variantType, Object value, String msg)
/*  9:   */   {
/* 10:39 */     super(variantType, value, msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public IllegalVariantTypeException(long variantType, Object value)
/* 14:   */   {
/* 15:51 */     this(variantType, value, "The variant type " + variantType + " (" + Variant.getVariantName(variantType) + ", " + HexDump.toHex(variantType) + ") is illegal in this context.");
/* 16:   */   }
/* 17:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.IllegalVariantTypeException
 * JD-Core Version:    0.7.0.1
 */