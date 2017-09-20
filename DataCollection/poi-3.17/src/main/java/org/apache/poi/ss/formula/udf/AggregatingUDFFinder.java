/*  1:   */ package org.apache.poi.ss.formula.udf;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.Arrays;
/*  5:   */ import java.util.Collection;
/*  6:   */ import org.apache.poi.ss.formula.atp.AnalysisToolPak;
/*  7:   */ import org.apache.poi.ss.formula.functions.FreeRefFunction;
/*  8:   */ 
/*  9:   */ public class AggregatingUDFFinder
/* 10:   */   implements UDFFinder
/* 11:   */ {
/* 12:37 */   public static final UDFFinder DEFAULT = new AggregatingUDFFinder(new UDFFinder[] { AnalysisToolPak.instance });
/* 13:   */   private final Collection<UDFFinder> _usedToolPacks;
/* 14:   */   
/* 15:   */   public AggregatingUDFFinder(UDFFinder... usedToolPacks)
/* 16:   */   {
/* 17:42 */     this._usedToolPacks = new ArrayList(usedToolPacks.length);
/* 18:43 */     this._usedToolPacks.addAll(Arrays.asList(usedToolPacks));
/* 19:   */   }
/* 20:   */   
/* 21:   */   public FreeRefFunction findFunction(String name)
/* 22:   */   {
/* 23:56 */     for (UDFFinder pack : this._usedToolPacks)
/* 24:   */     {
/* 25:57 */       FreeRefFunction evaluatorForFunction = pack.findFunction(name);
/* 26:58 */       if (evaluatorForFunction != null) {
/* 27:59 */         return evaluatorForFunction;
/* 28:   */       }
/* 29:   */     }
/* 30:62 */     return null;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void add(UDFFinder toolPack)
/* 34:   */   {
/* 35:71 */     this._usedToolPacks.add(toolPack);
/* 36:   */   }
/* 37:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.udf.AggregatingUDFFinder
 * JD-Core Version:    0.7.0.1
 */