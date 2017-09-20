/*  1:   */ package org.apache.poi.ss.usermodel;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ import java.util.Map;
/*  5:   */ 
/*  6:   */ public class ConditionType
/*  7:   */ {
/*  8:29 */   private static Map<Integer, ConditionType> lookup = new HashMap();
/*  9:35 */   public static final ConditionType CELL_VALUE_IS = new ConditionType(1, "cellIs");
/* 10:42 */   public static final ConditionType FORMULA = new ConditionType(2, "expression");
/* 11:49 */   public static final ConditionType COLOR_SCALE = new ConditionType(3, "colorScale");
/* 12:56 */   public static final ConditionType DATA_BAR = new ConditionType(4, "dataBar");
/* 13:62 */   public static final ConditionType FILTER = new ConditionType(5, null);
/* 14:69 */   public static final ConditionType ICON_SET = new ConditionType(6, "iconSet");
/* 15:   */   public final byte id;
/* 16:   */   public final String type;
/* 17:   */   
/* 18:   */   public String toString()
/* 19:   */   {
/* 20:77 */     return this.id + " - " + this.type;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public static ConditionType forId(byte id)
/* 24:   */   {
/* 25:82 */     return forId(id);
/* 26:   */   }
/* 27:   */   
/* 28:   */   public static ConditionType forId(int id)
/* 29:   */   {
/* 30:85 */     return (ConditionType)lookup.get(Integer.valueOf(id));
/* 31:   */   }
/* 32:   */   
/* 33:   */   private ConditionType(int id, String type)
/* 34:   */   {
/* 35:89 */     this.id = ((byte)id);this.type = type;
/* 36:90 */     lookup.put(Integer.valueOf(id), this);
/* 37:   */   }
/* 38:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.ConditionType
 * JD-Core Version:    0.7.0.1
 */