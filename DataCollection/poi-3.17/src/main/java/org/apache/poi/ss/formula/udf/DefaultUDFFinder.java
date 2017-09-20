/*  1:   */ package org.apache.poi.ss.formula.udf;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ import java.util.Locale;
/*  5:   */ import java.util.Map;
/*  6:   */ import org.apache.poi.ss.formula.functions.FreeRefFunction;
/*  7:   */ 
/*  8:   */ public final class DefaultUDFFinder
/*  9:   */   implements UDFFinder
/* 10:   */ {
/* 11:   */   private final Map<String, FreeRefFunction> _functionsByName;
/* 12:   */   
/* 13:   */   public DefaultUDFFinder(String[] functionNames, FreeRefFunction[] functionImpls)
/* 14:   */   {
/* 15:35 */     int nFuncs = functionNames.length;
/* 16:36 */     if (functionImpls.length != nFuncs) {
/* 17:37 */       throw new IllegalArgumentException("Mismatch in number of function names and implementations");
/* 18:   */     }
/* 19:40 */     HashMap<String, FreeRefFunction> m = new HashMap(nFuncs * 3 / 2);
/* 20:41 */     for (int i = 0; i < functionImpls.length; i++) {
/* 21:42 */       m.put(functionNames[i].toUpperCase(Locale.ROOT), functionImpls[i]);
/* 22:   */     }
/* 23:44 */     this._functionsByName = m;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public FreeRefFunction findFunction(String name)
/* 27:   */   {
/* 28:49 */     return (FreeRefFunction)this._functionsByName.get(name.toUpperCase(Locale.ROOT));
/* 29:   */   }
/* 30:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.udf.DefaultUDFFinder
 * JD-Core Version:    0.7.0.1
 */