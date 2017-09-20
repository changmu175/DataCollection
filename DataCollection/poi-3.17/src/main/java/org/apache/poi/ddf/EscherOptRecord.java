/*  1:   */ package org.apache.poi.ddf;
/*  2:   */ 
/*  3:   */ import java.util.List;
/*  4:   */ import org.apache.poi.util.Internal;
/*  5:   */ 
/*  6:   */ public class EscherOptRecord
/*  7:   */   extends AbstractEscherOptRecord
/*  8:   */ {
/*  9:   */   public static final String RECORD_DESCRIPTION = "msofbtOPT";
/* 10:   */   public static final short RECORD_ID = -4085;
/* 11:   */   
/* 12:   */   public short getInstance()
/* 13:   */   {
/* 14:35 */     setInstance((short)getEscherProperties().size());
/* 15:36 */     return super.getInstance();
/* 16:   */   }
/* 17:   */   
/* 18:   */   @Internal
/* 19:   */   public short getOptions()
/* 20:   */   {
/* 21:47 */     getInstance();
/* 22:48 */     getVersion();
/* 23:49 */     return super.getOptions();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public String getRecordName()
/* 27:   */   {
/* 28:55 */     return "Opt";
/* 29:   */   }
/* 30:   */   
/* 31:   */   public short getVersion()
/* 32:   */   {
/* 33:61 */     setVersion((short)3);
/* 34:62 */     return super.getVersion();
/* 35:   */   }
/* 36:   */   
/* 37:   */   public void setVersion(short value)
/* 38:   */   {
/* 39:68 */     if (value != 3) {
/* 40:69 */       throw new IllegalArgumentException("msofbtOPT can have only '0x3' version");
/* 41:   */     }
/* 42:73 */     super.setVersion(value);
/* 43:   */   }
/* 44:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherOptRecord
 * JD-Core Version:    0.7.0.1
 */