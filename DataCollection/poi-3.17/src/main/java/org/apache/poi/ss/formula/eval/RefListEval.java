/*  1:   */ package org.apache.poi.ss.formula.eval;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ 
/*  6:   */ public class RefListEval
/*  7:   */   implements ValueEval
/*  8:   */ {
/*  9:27 */   private final List<ValueEval> list = new ArrayList();
/* 10:   */   
/* 11:   */   public RefListEval(ValueEval v1, ValueEval v2)
/* 12:   */   {
/* 13:30 */     add(v1);
/* 14:31 */     add(v2);
/* 15:   */   }
/* 16:   */   
/* 17:   */   private void add(ValueEval v)
/* 18:   */   {
/* 19:36 */     if ((v instanceof RefListEval)) {
/* 20:37 */       this.list.addAll(((RefListEval)v).list);
/* 21:   */     } else {
/* 22:39 */       this.list.add(v);
/* 23:   */     }
/* 24:   */   }
/* 25:   */   
/* 26:   */   public List<ValueEval> getList()
/* 27:   */   {
/* 28:44 */     return this.list;
/* 29:   */   }
/* 30:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.RefListEval
 * JD-Core Version:    0.7.0.1
 */