/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.function.FunctionMetadata;
/*  4:   */ import org.apache.poi.ss.formula.function.FunctionMetadataRegistry;
/*  5:   */ import org.apache.poi.util.LittleEndianInput;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class FuncVarPtg
/*  9:   */   extends AbstractFunctionPtg
/* 10:   */ {
/* 11:   */   public static final byte sid = 34;
/* 12:   */   private static final int SIZE = 4;
/* 13:35 */   public static final OperationPtg SUM = create("SUM", 1);
/* 14:   */   
/* 15:   */   private FuncVarPtg(int functionIndex, int returnClass, byte[] paramClasses, int numArgs)
/* 16:   */   {
/* 17:38 */     super(functionIndex, returnClass, paramClasses, numArgs);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public static FuncVarPtg create(LittleEndianInput in)
/* 21:   */   {
/* 22:45 */     return create(in.readByte(), in.readShort());
/* 23:   */   }
/* 24:   */   
/* 25:   */   public static FuncVarPtg create(String pName, int numArgs)
/* 26:   */   {
/* 27:52 */     return create(numArgs, lookupIndex(pName));
/* 28:   */   }
/* 29:   */   
/* 30:   */   private static FuncVarPtg create(int numArgs, int functionIndex)
/* 31:   */   {
/* 32:56 */     FunctionMetadata fm = FunctionMetadataRegistry.getFunctionByIndex(functionIndex);
/* 33:57 */     if (fm == null) {
/* 34:59 */       return new FuncVarPtg(functionIndex, 32, new byte[] { 32 }, numArgs);
/* 35:   */     }
/* 36:61 */     return new FuncVarPtg(functionIndex, fm.getReturnClassCode(), fm.getParameterClassCodes(), numArgs);
/* 37:   */   }
/* 38:   */   
/* 39:   */   public void write(LittleEndianOutput out)
/* 40:   */   {
/* 41:65 */     out.writeByte(34 + getPtgClass());
/* 42:66 */     out.writeByte(getNumberOfOperands());
/* 43:67 */     out.writeShort(getFunctionIndex());
/* 44:   */   }
/* 45:   */   
/* 46:   */   public int getSize()
/* 47:   */   {
/* 48:71 */     return 4;
/* 49:   */   }
/* 50:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.FuncVarPtg
 * JD-Core Version:    0.7.0.1
 */