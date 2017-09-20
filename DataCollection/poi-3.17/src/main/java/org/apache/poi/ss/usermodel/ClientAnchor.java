/*   1:    */ package org.apache.poi.ss.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.Internal;
/*   4:    */ 
/*   5:    */ public abstract interface ClientAnchor
/*   6:    */ {
/*   7:    */   public abstract short getCol1();
/*   8:    */   
/*   9:    */   public abstract void setCol1(int paramInt);
/*  10:    */   
/*  11:    */   public abstract short getCol2();
/*  12:    */   
/*  13:    */   public abstract void setCol2(int paramInt);
/*  14:    */   
/*  15:    */   public abstract int getRow1();
/*  16:    */   
/*  17:    */   public abstract void setRow1(int paramInt);
/*  18:    */   
/*  19:    */   public abstract int getRow2();
/*  20:    */   
/*  21:    */   public abstract void setRow2(int paramInt);
/*  22:    */   
/*  23:    */   public abstract int getDx1();
/*  24:    */   
/*  25:    */   public abstract void setDx1(int paramInt);
/*  26:    */   
/*  27:    */   public abstract int getDy1();
/*  28:    */   
/*  29:    */   public abstract void setDy1(int paramInt);
/*  30:    */   
/*  31:    */   public abstract int getDy2();
/*  32:    */   
/*  33:    */   public abstract void setDy2(int paramInt);
/*  34:    */   
/*  35:    */   public abstract int getDx2();
/*  36:    */   
/*  37:    */   public abstract void setDx2(int paramInt);
/*  38:    */   
/*  39:    */   public abstract void setAnchorType(AnchorType paramAnchorType);
/*  40:    */   
/*  41:    */   public abstract AnchorType getAnchorType();
/*  42:    */   
/*  43:    */   public static enum AnchorType
/*  44:    */   {
/*  45: 48 */     MOVE_AND_RESIZE(0),  DONT_MOVE_DO_RESIZE(1),  MOVE_DONT_RESIZE(2),  DONT_MOVE_AND_RESIZE(3);
/*  46:    */     
/*  47:    */     public final short value;
/*  48:    */     
/*  49:    */     private AnchorType(int value)
/*  50:    */     {
/*  51: 94 */       this.value = ((short)value);
/*  52:    */     }
/*  53:    */     
/*  54:    */     @Internal
/*  55:    */     public static AnchorType byId(int value)
/*  56:    */     {
/*  57:105 */       return values()[value];
/*  58:    */     }
/*  59:    */   }
/*  60:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.ClientAnchor
 * JD-Core Version:    0.7.0.1
 */