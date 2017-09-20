/*   1:    */ package org.apache.poi.ss.format;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.Internal;
/*   4:    */ 
/*   5:    */ @Internal
/*   6:    */ public class CellNumberStringMod
/*   7:    */   implements Comparable<CellNumberStringMod>
/*   8:    */ {
/*   9:    */   public static final int BEFORE = 1;
/*  10:    */   public static final int AFTER = 2;
/*  11:    */   public static final int REPLACE = 3;
/*  12:    */   private final CellNumberFormatter.Special special;
/*  13:    */   private final int op;
/*  14:    */   private CharSequence toAdd;
/*  15:    */   private CellNumberFormatter.Special end;
/*  16:    */   private boolean startInclusive;
/*  17:    */   private boolean endInclusive;
/*  18:    */   
/*  19:    */   public CellNumberStringMod(CellNumberFormatter.Special special, CharSequence toAdd, int op)
/*  20:    */   {
/*  21: 47 */     this.special = special;
/*  22: 48 */     this.toAdd = toAdd;
/*  23: 49 */     this.op = op;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public CellNumberStringMod(CellNumberFormatter.Special start, boolean startInclusive, CellNumberFormatter.Special end, boolean endInclusive, char toAdd)
/*  27:    */   {
/*  28: 53 */     this(start, startInclusive, end, endInclusive);
/*  29: 54 */     this.toAdd = (toAdd + "");
/*  30:    */   }
/*  31:    */   
/*  32:    */   public CellNumberStringMod(CellNumberFormatter.Special start, boolean startInclusive, CellNumberFormatter.Special end, boolean endInclusive)
/*  33:    */   {
/*  34: 58 */     this.special = start;
/*  35: 59 */     this.startInclusive = startInclusive;
/*  36: 60 */     this.end = end;
/*  37: 61 */     this.endInclusive = endInclusive;
/*  38: 62 */     this.op = 3;
/*  39: 63 */     this.toAdd = "";
/*  40:    */   }
/*  41:    */   
/*  42:    */   public int compareTo(CellNumberStringMod that)
/*  43:    */   {
/*  44: 68 */     int diff = this.special.pos - that.special.pos;
/*  45: 69 */     return diff != 0 ? diff : this.op - that.op;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public boolean equals(Object that)
/*  49:    */   {
/*  50: 74 */     return ((that instanceof CellNumberStringMod)) && (compareTo((CellNumberStringMod)that) == 0);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public int hashCode()
/*  54:    */   {
/*  55: 79 */     return this.special.hashCode() + this.op;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public CellNumberFormatter.Special getSpecial()
/*  59:    */   {
/*  60: 83 */     return this.special;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public int getOp()
/*  64:    */   {
/*  65: 87 */     return this.op;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public CharSequence getToAdd()
/*  69:    */   {
/*  70: 91 */     return this.toAdd;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public CellNumberFormatter.Special getEnd()
/*  74:    */   {
/*  75: 95 */     return this.end;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public boolean isStartInclusive()
/*  79:    */   {
/*  80: 99 */     return this.startInclusive;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public boolean isEndInclusive()
/*  84:    */   {
/*  85:103 */     return this.endInclusive;
/*  86:    */   }
/*  87:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.format.CellNumberStringMod
 * JD-Core Version:    0.7.0.1
 */