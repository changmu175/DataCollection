/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.function.FunctionMetadata;
/*  4:   */ import org.apache.poi.ss.formula.function.FunctionMetadataRegistry;
/*  5:   */ import org.apache.poi.util.LittleEndianInput;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class FuncPtg
/*  9:   */   extends AbstractFunctionPtg
/* 10:   */ {
/* 11:   */   public static final byte sid = 33;
/* 12:   */   public static final int SIZE = 3;
/* 13:   */   
/* 14:   */   public static FuncPtg create(LittleEndianInput in)
/* 15:   */   {
/* 16:36 */     return create(in.readUShort());
/* 17:   */   }
/* 18:   */   
/* 19:   */   private FuncPtg(int funcIndex, FunctionMetadata fm)
/* 20:   */   {
/* 21:40 */     super(funcIndex, fm.getReturnClassCode(), fm.getParameterClassCodes(), fm.getMinParams());
/* 22:   */   }
/* 23:   */   
/* 24:   */   public static FuncPtg create(int functionIndex)
/* 25:   */   {
/* 26:44 */     FunctionMetadata fm = FunctionMetadataRegistry.getFunctionByIndex(functionIndex);
/* 27:45 */     if (fm == null) {
/* 28:46 */       throw new RuntimeException("Invalid built-in function index (" + functionIndex + ")");
/* 29:   */     }
/* 30:48 */     return new FuncPtg(functionIndex, fm);
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void write(LittleEndianOutput out)
/* 34:   */   {
/* 35:53 */     out.writeByte(33 + getPtgClass());
/* 36:54 */     out.writeShort(getFunctionIndex());
/* 37:   */   }
/* 38:   */   
/* 39:   */   public int getSize()
/* 40:   */   {
/* 41:58 */     return 3;
/* 42:   */   }
/* 43:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.FuncPtg
 * JD-Core Version:    0.7.0.1
 */