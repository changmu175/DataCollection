/*   1:    */ package org.apache.poi.sl.draw.geom;
/*   2:    */ 
/*   3:    */ import org.apache.poi.sl.draw.binding.CTGeomGuide;
/*   4:    */ 
/*   5:    */ public class Guide
/*   6:    */   implements Formula
/*   7:    */ {
/*   8:    */   private final String name;
/*   9:    */   private final String fmla;
/*  10:    */   private final Op op;
/*  11:    */   private final String[] operands;
/*  12:    */   
/*  13:    */   static enum Op
/*  14:    */   {
/*  15: 40 */     muldiv,  addsub,  adddiv,  ifelse,  val,  abs,  sqrt,  max,  min,  at2,  sin,  cos,  tan,  cat2,  sat2,  pin,  mod;
/*  16:    */     
/*  17:    */     private Op() {}
/*  18:    */   }
/*  19:    */   
/*  20:    */   public Guide(CTGeomGuide gd)
/*  21:    */   {
/*  22: 49 */     this(gd.getName(), gd.getFmla());
/*  23:    */   }
/*  24:    */   
/*  25:    */   public Guide(String nm, String fm)
/*  26:    */   {
/*  27: 53 */     this.name = nm;
/*  28: 54 */     this.fmla = fm;
/*  29: 55 */     this.operands = fm.split("\\s+");
/*  30: 56 */     this.op = Op.valueOf(this.operands[0].replace("*", "mul").replace("/", "div").replace("+", "add").replace("-", "sub").replace("?:", "ifelse"));
/*  31:    */   }
/*  32:    */   
/*  33:    */   public String getName()
/*  34:    */   {
/*  35: 60 */     return this.name;
/*  36:    */   }
/*  37:    */   
/*  38:    */   String getFormula()
/*  39:    */   {
/*  40: 64 */     return this.fmla;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public double evaluate(Context ctx)
/*  44:    */   {
/*  45: 69 */     double x = this.operands.length > 1 ? ctx.getValue(this.operands[1]) : 0.0D;
/*  46: 70 */     double y = this.operands.length > 2 ? ctx.getValue(this.operands[2]) : 0.0D;
/*  47: 71 */     double z = this.operands.length > 3 ? ctx.getValue(this.operands[3]) : 0.0D;
/*  48: 72 */     switch (1.$SwitchMap$org$apache$poi$sl$draw$geom$Guide$Op[this.op.ordinal()])
/*  49:    */     {
/*  50:    */     case 1: 
/*  51: 75 */       return Math.abs(x);
/*  52:    */     case 2: 
/*  53: 78 */       return (x + y) / z;
/*  54:    */     case 3: 
/*  55: 81 */       return x + y - z;
/*  56:    */     case 4: 
/*  57: 84 */       return Math.toDegrees(Math.atan2(y, x)) * 60000.0D;
/*  58:    */     case 5: 
/*  59: 87 */       return x * Math.cos(Math.toRadians(y / 60000.0D));
/*  60:    */     case 6: 
/*  61: 90 */       return x * Math.cos(Math.atan2(z, y));
/*  62:    */     case 7: 
/*  63: 94 */       return x > 0.0D ? y : z;
/*  64:    */     case 8: 
/*  65: 97 */       return x;
/*  66:    */     case 9: 
/*  67:100 */       return Math.max(x, y);
/*  68:    */     case 10: 
/*  69:103 */       return Math.min(x, y);
/*  70:    */     case 11: 
/*  71:106 */       return Math.sqrt(x * x + y * y + z * z);
/*  72:    */     case 12: 
/*  73:109 */       return x * y / z;
/*  74:    */     case 13: 
/*  75:114 */       if (y < x) {
/*  76:115 */         return x;
/*  77:    */       }
/*  78:116 */       if (y > z) {
/*  79:117 */         return z;
/*  80:    */       }
/*  81:119 */       return y;
/*  82:    */     case 14: 
/*  83:123 */       return x * Math.sin(Math.atan2(z, y));
/*  84:    */     case 15: 
/*  85:126 */       return x * Math.sin(Math.toRadians(y / 60000.0D));
/*  86:    */     case 16: 
/*  87:129 */       return Math.sqrt(x);
/*  88:    */     case 17: 
/*  89:132 */       return x * Math.tan(Math.toRadians(y / 60000.0D));
/*  90:    */     }
/*  91:134 */     return 0.0D;
/*  92:    */   }
/*  93:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.geom.Guide
 * JD-Core Version:    0.7.0.1
 */