/*  1:   */ package org.apache.poi.ss.formula.udf;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ import org.apache.poi.ss.formula.functions.FreeRefFunction;
/*  5:   */ import org.apache.poi.util.Internal;
/*  6:   */ 
/*  7:   */ @Internal
/*  8:   */ public class IndexedUDFFinder
/*  9:   */   extends AggregatingUDFFinder
/* 10:   */ {
/* 11:   */   private final HashMap<Integer, String> _funcMap;
/* 12:   */   
/* 13:   */   public IndexedUDFFinder(UDFFinder... usedToolPacks)
/* 14:   */   {
/* 15:34 */     super(usedToolPacks);
/* 16:35 */     this._funcMap = new HashMap();
/* 17:   */   }
/* 18:   */   
/* 19:   */   public FreeRefFunction findFunction(String name)
/* 20:   */   {
/* 21:40 */     FreeRefFunction func = super.findFunction(name);
/* 22:41 */     if (func != null)
/* 23:   */     {
/* 24:42 */       int idx = getFunctionIndex(name);
/* 25:43 */       this._funcMap.put(Integer.valueOf(idx), name);
/* 26:   */     }
/* 27:45 */     return func;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public String getFunctionName(int idx)
/* 31:   */   {
/* 32:49 */     return (String)this._funcMap.get(Integer.valueOf(idx));
/* 33:   */   }
/* 34:   */   
/* 35:   */   public int getFunctionIndex(String name)
/* 36:   */   {
/* 37:53 */     return name.hashCode();
/* 38:   */   }
/* 39:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.udf.IndexedUDFFinder
 * JD-Core Version:    0.7.0.1
 */