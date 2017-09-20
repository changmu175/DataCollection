/*   1:    */ package org.apache.poi.xssf.binary;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import org.apache.poi.util.Internal;
/*   6:    */ 
/*   7:    */ @Internal
/*   8:    */ public enum XSSFBRecordType
/*   9:    */ {
/*  10: 31 */   BrtCellBlank(1),  BrtCellRk(2),  BrtCellError(3),  BrtCellBool(4),  BrtCellReal(5),  BrtCellSt(6),  BrtCellIsst(7),  BrtFmlaString(8),  BrtFmlaNum(9),  BrtFmlaBool(10),  BrtFmlaError(11),  BrtRowHdr(0),  BrtCellRString(62),  BrtBeginSheet(129),  BrtWsProp(147),  BrtWsDim(148),  BrtColInfo(60),  BrtBeginSheetData(145),  BrtEndSheetData(146),  BrtHLink(494),  BrtBeginHeaderFooter(479),  BrtBeginCommentAuthors(630),  BrtEndCommentAuthors(631),  BrtCommentAuthor(632),  BrtBeginComment(635),  BrtCommentText(637),  BrtEndComment(636),  BrtXf(47),  BrtFmt(44),  BrtBeginFmts(615),  BrtEndFmts(616),  BrtBeginCellXFs(617),  BrtEndCellXFs(618),  BrtBeginCellStyleXFS(626),  BrtEndCellStyleXFS(627),  BrtSstItem(19),  BrtBeginSst(159),  BrtEndSst(160),  BrtBundleSh(156),  BrtAbsPath15(2071),  Unimplemented(-1);
/*  11:    */   
/*  12:    */   private static final Map<Integer, XSSFBRecordType> TYPE_MAP;
/*  13:    */   private final int id;
/*  14:    */   
/*  15:    */   static
/*  16:    */   {
/*  17: 84 */     TYPE_MAP = new HashMap();
/*  18: 88 */     for (XSSFBRecordType type : values()) {
/*  19: 89 */       TYPE_MAP.put(Integer.valueOf(type.getId()), type);
/*  20:    */     }
/*  21:    */   }
/*  22:    */   
/*  23:    */   private XSSFBRecordType(int id)
/*  24:    */   {
/*  25: 96 */     this.id = id;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public int getId()
/*  29:    */   {
/*  30:100 */     return this.id;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public static XSSFBRecordType lookup(int id)
/*  34:    */   {
/*  35:104 */     XSSFBRecordType type = (XSSFBRecordType)TYPE_MAP.get(Integer.valueOf(id));
/*  36:105 */     if (type == null) {
/*  37:106 */       return Unimplemented;
/*  38:    */     }
/*  39:108 */     return type;
/*  40:    */   }
/*  41:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.binary.XSSFBRecordType
 * JD-Core Version:    0.7.0.1
 */