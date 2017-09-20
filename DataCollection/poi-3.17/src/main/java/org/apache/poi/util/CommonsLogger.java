/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import org.apache.commons.logging.Log;
/*   4:    */ import org.apache.commons.logging.LogFactory;
/*   5:    */ 
/*   6:    */ public class CommonsLogger
/*   7:    */   extends POILogger
/*   8:    */ {
/*   9: 33 */   private static final LogFactory _creator = ;
/*  10: 34 */   private Log log = null;
/*  11:    */   
/*  12:    */   public void initialize(String cat)
/*  13:    */   {
/*  14: 39 */     this.log = _creator.getInstance(cat);
/*  15:    */   }
/*  16:    */   
/*  17:    */   protected void _log(int level, Object obj1)
/*  18:    */   {
/*  19: 53 */     if (level == 9)
/*  20:    */     {
/*  21: 55 */       if (this.log.isFatalEnabled()) {
/*  22: 57 */         this.log.fatal(obj1);
/*  23:    */       }
/*  24:    */     }
/*  25: 60 */     else if (level == 7)
/*  26:    */     {
/*  27: 62 */       if (this.log.isErrorEnabled()) {
/*  28: 64 */         this.log.error(obj1);
/*  29:    */       }
/*  30:    */     }
/*  31: 67 */     else if (level == 5)
/*  32:    */     {
/*  33: 69 */       if (this.log.isWarnEnabled()) {
/*  34: 71 */         this.log.warn(obj1);
/*  35:    */       }
/*  36:    */     }
/*  37: 74 */     else if (level == 3)
/*  38:    */     {
/*  39: 76 */       if (this.log.isInfoEnabled()) {
/*  40: 78 */         this.log.info(obj1);
/*  41:    */       }
/*  42:    */     }
/*  43: 81 */     else if (level == 1)
/*  44:    */     {
/*  45: 83 */       if (this.log.isDebugEnabled()) {
/*  46: 85 */         this.log.debug(obj1);
/*  47:    */       }
/*  48:    */     }
/*  49: 90 */     else if (this.log.isTraceEnabled()) {
/*  50: 92 */       this.log.trace(obj1);
/*  51:    */     }
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected void _log(int level, Object obj1, Throwable exception)
/*  55:    */   {
/*  56:110 */     if (level == 9)
/*  57:    */     {
/*  58:112 */       if (this.log.isFatalEnabled()) {
/*  59:114 */         if (obj1 != null) {
/*  60:115 */           this.log.fatal(obj1, exception);
/*  61:    */         } else {
/*  62:117 */           this.log.fatal(exception);
/*  63:    */         }
/*  64:    */       }
/*  65:    */     }
/*  66:120 */     else if (level == 7)
/*  67:    */     {
/*  68:122 */       if (this.log.isErrorEnabled()) {
/*  69:124 */         if (obj1 != null) {
/*  70:125 */           this.log.error(obj1, exception);
/*  71:    */         } else {
/*  72:127 */           this.log.error(exception);
/*  73:    */         }
/*  74:    */       }
/*  75:    */     }
/*  76:130 */     else if (level == 5)
/*  77:    */     {
/*  78:132 */       if (this.log.isWarnEnabled()) {
/*  79:134 */         if (obj1 != null) {
/*  80:135 */           this.log.warn(obj1, exception);
/*  81:    */         } else {
/*  82:137 */           this.log.warn(exception);
/*  83:    */         }
/*  84:    */       }
/*  85:    */     }
/*  86:140 */     else if (level == 3)
/*  87:    */     {
/*  88:142 */       if (this.log.isInfoEnabled()) {
/*  89:144 */         if (obj1 != null) {
/*  90:145 */           this.log.info(obj1, exception);
/*  91:    */         } else {
/*  92:147 */           this.log.info(exception);
/*  93:    */         }
/*  94:    */       }
/*  95:    */     }
/*  96:150 */     else if (level == 1)
/*  97:    */     {
/*  98:152 */       if (this.log.isDebugEnabled()) {
/*  99:154 */         if (obj1 != null) {
/* 100:155 */           this.log.debug(obj1, exception);
/* 101:    */         } else {
/* 102:157 */           this.log.debug(exception);
/* 103:    */         }
/* 104:    */       }
/* 105:    */     }
/* 106:162 */     else if (this.log.isTraceEnabled()) {
/* 107:164 */       if (obj1 != null) {
/* 108:165 */         this.log.trace(obj1, exception);
/* 109:    */       } else {
/* 110:167 */         this.log.trace(exception);
/* 111:    */       }
/* 112:    */     }
/* 113:    */   }
/* 114:    */   
/* 115:    */   public boolean check(int level)
/* 116:    */   {
/* 117:183 */     if (level == 9)
/* 118:    */     {
/* 119:185 */       if (this.log.isFatalEnabled()) {
/* 120:187 */         return true;
/* 121:    */       }
/* 122:    */     }
/* 123:190 */     else if (level == 7)
/* 124:    */     {
/* 125:192 */       if (this.log.isErrorEnabled()) {
/* 126:194 */         return true;
/* 127:    */       }
/* 128:    */     }
/* 129:197 */     else if (level == 5)
/* 130:    */     {
/* 131:199 */       if (this.log.isWarnEnabled()) {
/* 132:201 */         return true;
/* 133:    */       }
/* 134:    */     }
/* 135:204 */     else if (level == 3)
/* 136:    */     {
/* 137:206 */       if (this.log.isInfoEnabled()) {
/* 138:208 */         return true;
/* 139:    */       }
/* 140:    */     }
/* 141:211 */     else if (level == 1) {
/* 142:213 */       if (this.log.isDebugEnabled()) {
/* 143:215 */         return true;
/* 144:    */       }
/* 145:    */     }
/* 146:219 */     return false;
/* 147:    */   }
/* 148:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.CommonsLogger
 * JD-Core Version:    0.7.0.1
 */