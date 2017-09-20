/*   1:    */ package org.apache.poi.ss.formula;
/*   2:    */ 
/*   3:    */ import java.util.Stack;
/*   4:    */ import org.apache.poi.ss.formula.ptg.AttrPtg;
/*   5:    */ import org.apache.poi.ss.formula.ptg.MemAreaPtg;
/*   6:    */ import org.apache.poi.ss.formula.ptg.MemErrPtg;
/*   7:    */ import org.apache.poi.ss.formula.ptg.MemFuncPtg;
/*   8:    */ import org.apache.poi.ss.formula.ptg.OperationPtg;
/*   9:    */ import org.apache.poi.ss.formula.ptg.ParenthesisPtg;
/*  10:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  11:    */ 
/*  12:    */ public class FormulaRenderer
/*  13:    */ {
/*  14:    */   public static String toFormulaString(FormulaRenderingWorkbook book, Ptg[] ptgs)
/*  15:    */   {
/*  16: 47 */     if ((ptgs == null) || (ptgs.length == 0)) {
/*  17: 48 */       throw new IllegalArgumentException("ptgs must not be null");
/*  18:    */     }
/*  19: 50 */     Stack<String> stack = new Stack();
/*  20: 52 */     for (Ptg ptg : ptgs) {
/*  21: 54 */       if ((!(ptg instanceof MemAreaPtg)) && (!(ptg instanceof MemFuncPtg)) && (!(ptg instanceof MemErrPtg))) {
/*  22: 60 */         if ((ptg instanceof ParenthesisPtg))
/*  23:    */         {
/*  24: 61 */           String contents = (String)stack.pop();
/*  25: 62 */           stack.push("(" + contents + ")");
/*  26:    */         }
/*  27: 65 */         else if ((ptg instanceof AttrPtg))
/*  28:    */         {
/*  29: 66 */           AttrPtg attrPtg = (AttrPtg)ptg;
/*  30: 67 */           if ((!attrPtg.isOptimizedIf()) && (!attrPtg.isOptimizedChoose()) && (!attrPtg.isSkip())) {
/*  31: 70 */             if (!attrPtg.isSpace()) {
/*  32: 77 */               if (!attrPtg.isSemiVolatile()) {
/*  33: 81 */                 if (attrPtg.isSum())
/*  34:    */                 {
/*  35: 82 */                   String[] operands = getOperands(stack, attrPtg.getNumberOfOperands());
/*  36: 83 */                   stack.push(attrPtg.toFormulaString(operands));
/*  37:    */                 }
/*  38:    */                 else
/*  39:    */                 {
/*  40: 86 */                   throw new RuntimeException("Unexpected tAttr: " + attrPtg);
/*  41:    */                 }
/*  42:    */               }
/*  43:    */             }
/*  44:    */           }
/*  45:    */         }
/*  46: 89 */         else if ((ptg instanceof WorkbookDependentFormula))
/*  47:    */         {
/*  48: 90 */           WorkbookDependentFormula optg = (WorkbookDependentFormula)ptg;
/*  49: 91 */           stack.push(optg.toFormulaString(book));
/*  50:    */         }
/*  51: 94 */         else if (!(ptg instanceof OperationPtg))
/*  52:    */         {
/*  53: 95 */           stack.push(ptg.toFormulaString());
/*  54:    */         }
/*  55:    */         else
/*  56:    */         {
/*  57: 99 */           OperationPtg o = (OperationPtg)ptg;
/*  58:100 */           String[] operands = getOperands(stack, o.getNumberOfOperands());
/*  59:101 */           stack.push(o.toFormulaString(operands));
/*  60:    */         }
/*  61:    */       }
/*  62:    */     }
/*  63:103 */     if (stack.isEmpty()) {
/*  64:106 */       throw new IllegalStateException("Stack underflow");
/*  65:    */     }
/*  66:108 */     String result = (String)stack.pop();
/*  67:109 */     if (!stack.isEmpty()) {
/*  68:112 */       throw new IllegalStateException("too much stuff left on the stack");
/*  69:    */     }
/*  70:114 */     return result;
/*  71:    */   }
/*  72:    */   
/*  73:    */   private static String[] getOperands(Stack<String> stack, int nOperands)
/*  74:    */   {
/*  75:118 */     String[] operands = new String[nOperands];
/*  76:120 */     for (int j = nOperands - 1; j >= 0; j--)
/*  77:    */     {
/*  78:121 */       if (stack.isEmpty())
/*  79:    */       {
/*  80:122 */         String msg = "Too few arguments supplied to operation. Expected (" + nOperands + ") operands but got (" + (nOperands - j - 1) + ")";
/*  81:    */         
/*  82:124 */         throw new IllegalStateException(msg);
/*  83:    */       }
/*  84:126 */       operands[j] = ((String)stack.pop());
/*  85:    */     }
/*  86:128 */     return operands;
/*  87:    */   }
/*  88:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.FormulaRenderer
 * JD-Core Version:    0.7.0.1
 */