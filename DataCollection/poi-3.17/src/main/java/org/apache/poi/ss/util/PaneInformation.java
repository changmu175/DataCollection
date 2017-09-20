/*   1:    */ package org.apache.poi.ss.util;
/*   2:    */ 
/*   3:    */ public class PaneInformation
/*   4:    */ {
/*   5:    */   public static final byte PANE_LOWER_RIGHT = 0;
/*   6:    */   public static final byte PANE_UPPER_RIGHT = 1;
/*   7:    */   public static final byte PANE_LOWER_LEFT = 2;
/*   8:    */   public static final byte PANE_UPPER_LEFT = 3;
/*   9:    */   private final short x;
/*  10:    */   private final short y;
/*  11:    */   private final short topRow;
/*  12:    */   private final short leftColumn;
/*  13:    */   private final byte activePane;
/*  14:    */   private final boolean frozen;
/*  15:    */   
/*  16:    */   public PaneInformation(short x, short y, short top, short left, byte active, boolean frozen)
/*  17:    */   {
/*  18: 43 */     this.x = x;
/*  19: 44 */     this.y = y;
/*  20: 45 */     this.topRow = top;
/*  21: 46 */     this.leftColumn = left;
/*  22: 47 */     this.activePane = active;
/*  23: 48 */     this.frozen = frozen;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public short getVerticalSplitPosition()
/*  27:    */   {
/*  28: 59 */     return this.x;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public short getHorizontalSplitPosition()
/*  32:    */   {
/*  33: 69 */     return this.y;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public short getHorizontalSplitTopRow()
/*  37:    */   {
/*  38: 77 */     return this.topRow;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public short getVerticalSplitLeftColumn()
/*  42:    */   {
/*  43: 85 */     return this.leftColumn;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public byte getActivePane()
/*  47:    */   {
/*  48: 97 */     return this.activePane;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public boolean isFreezePane()
/*  52:    */   {
/*  53:103 */     return this.frozen;
/*  54:    */   }
/*  55:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.PaneInformation
 * JD-Core Version:    0.7.0.1
 */