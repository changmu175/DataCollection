/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.HexDump;
/*  4:   */ 
/*  5:   */ public abstract class UnsupportedVariantTypeException
/*  6:   */   extends VariantTypeException
/*  7:   */ {
/*  8:   */   public UnsupportedVariantTypeException(long variantType, Object value)
/*  9:   */   {
/* 10:42 */     super(variantType, value, "HPSF does not yet support the variant type " + variantType + " (" + Variant.getVariantName(variantType) + ", " + HexDump.toHex(variantType) + "). If you want support for " + "this variant type in one of the next POI releases please " + "submit a request for enhancement (RFE) to " + "<http://issues.apache.org/bugzilla/>! Thank you!");
/* 11:   */   }
/* 12:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.UnsupportedVariantTypeException
 * JD-Core Version:    0.7.0.1
 */