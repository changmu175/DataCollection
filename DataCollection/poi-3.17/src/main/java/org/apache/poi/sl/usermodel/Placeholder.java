/*   1:    */ package org.apache.poi.sl.usermodel;
/*   2:    */ 
/*   3:    */ public enum Placeholder
/*   4:    */ {
/*   5: 24 */   NONE(0, 0, 0, 0, 0),  TITLE(13, 1, 1, 1, 1),  BODY(14, 2, 12, 6, 2),  CENTERED_TITLE(15, 3, 3, 3, 3),  SUBTITLE(16, 4, 4, 4, 4),  DATETIME(7, 7, 7, 7, 5),  SLIDE_NUMBER(8, 8, 8, 8, 6),  FOOTER(9, 9, 9, 9, 7),  HEADER(10, 10, 10, 10, 8),  CONTENT(19, 19, 19, 19, 9),  CHART(20, 20, 20, 20, 10),  TABLE(21, 21, 21, 21, 11),  CLIP_ART(22, 22, 22, 22, 12),  DGM(23, 23, 23, 23, 13),  MEDIA(24, 24, 24, 24, 14),  SLIDE_IMAGE(11, 11, 11, 5, 15),  PICTURE(26, 26, 26, 26, 16),  VERTICAL_OBJECT(25, 25, 25, 25, -2),  VERTICAL_TEXT_TITLE(17, 17, 17, 17, -2),  VERTICAL_TEXT_BODY(18, 18, 18, 18, -2);
/*   6:    */   
/*   7:    */   public final int nativeSlideId;
/*   8:    */   public final int nativeSlideMasterId;
/*   9:    */   public final int nativeNotesId;
/*  10:    */   public final int nativeNotesMasterId;
/*  11:    */   public final int ooxmlId;
/*  12:    */   
/*  13:    */   private Placeholder(int nativeSlideId, int nativeSlideMasterId, int nativeNotesId, int nativeNotesMasterId, int ooxmlId)
/*  14:    */   {
/*  15:110 */     this.nativeSlideId = nativeSlideId;
/*  16:111 */     this.nativeSlideMasterId = nativeSlideMasterId;
/*  17:112 */     this.nativeNotesId = nativeNotesId;
/*  18:113 */     this.nativeNotesMasterId = nativeNotesMasterId;
/*  19:114 */     this.ooxmlId = ooxmlId;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public static Placeholder lookupNativeSlide(int nativeId)
/*  23:    */   {
/*  24:118 */     return lookupNative(nativeId, 0);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public static Placeholder lookupNativeSlideMaster(int nativeId)
/*  28:    */   {
/*  29:122 */     return lookupNative(nativeId, 1);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public static Placeholder lookupNativeNotes(int nativeId)
/*  33:    */   {
/*  34:126 */     return lookupNative(nativeId, 2);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public static Placeholder lookupNativeNotesMaster(int nativeId)
/*  38:    */   {
/*  39:130 */     return lookupNative(nativeId, 3);
/*  40:    */   }
/*  41:    */   
/*  42:    */   private static Placeholder lookupNative(int nativeId, int type)
/*  43:    */   {
/*  44:135 */     for (Placeholder ph : ) {
/*  45:136 */       if (((type == 0) && (ph.nativeSlideId == nativeId)) || ((type == 1) && (ph.nativeSlideMasterId == nativeId)) || ((type == 2) && (ph.nativeNotesId == nativeId)) || ((type == 3) && (ph.nativeNotesMasterId == nativeId))) {
/*  46:142 */         return ph;
/*  47:    */       }
/*  48:    */     }
/*  49:145 */     return null;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static Placeholder lookupOoxml(int ooxmlId)
/*  53:    */   {
/*  54:149 */     for (Placeholder ph : ) {
/*  55:150 */       if (ph.ooxmlId == ooxmlId) {
/*  56:151 */         return ph;
/*  57:    */       }
/*  58:    */     }
/*  59:154 */     return null;
/*  60:    */   }
/*  61:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.Placeholder
 * JD-Core Version:    0.7.0.1
 */