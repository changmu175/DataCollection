/*   1:    */ package org.apache.poi.hssf.model;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.poi.ddf.EscherDgRecord;
/*   6:    */ import org.apache.poi.ddf.EscherDggRecord;
/*   7:    */ import org.apache.poi.util.Removal;
/*   8:    */ 
/*   9:    */ public class DrawingManager2
/*  10:    */ {
/*  11:    */   private final EscherDggRecord dgg;
/*  12: 33 */   private final List<EscherDgRecord> drawingGroups = new ArrayList();
/*  13:    */   
/*  14:    */   public DrawingManager2(EscherDggRecord dgg)
/*  15:    */   {
/*  16: 37 */     this.dgg = dgg;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public void clearDrawingGroups()
/*  20:    */   {
/*  21: 44 */     this.drawingGroups.clear();
/*  22:    */   }
/*  23:    */   
/*  24:    */   public EscherDgRecord createDgRecord()
/*  25:    */   {
/*  26: 53 */     EscherDgRecord dg = new EscherDgRecord();
/*  27: 54 */     dg.setRecordId((short)-4088);
/*  28: 55 */     short dgId = findNewDrawingGroupId();
/*  29: 56 */     dg.setOptions((short)(dgId << 4));
/*  30: 57 */     dg.setNumShapes(0);
/*  31: 58 */     dg.setLastMSOSPID(-1);
/*  32: 59 */     this.drawingGroups.add(dg);
/*  33: 60 */     this.dgg.addCluster(dgId, 0);
/*  34: 61 */     this.dgg.setDrawingsSaved(this.dgg.getDrawingsSaved() + 1);
/*  35: 62 */     return dg;
/*  36:    */   }
/*  37:    */   
/*  38:    */   @Deprecated
/*  39:    */   @Removal(version="4.0")
/*  40:    */   public int allocateShapeId(short drawingGroupId)
/*  41:    */   {
/*  42: 77 */     for (EscherDgRecord dg : this.drawingGroups) {
/*  43: 78 */       if (dg.getDrawingGroupId() == drawingGroupId) {
/*  44: 79 */         return allocateShapeId(dg);
/*  45:    */       }
/*  46:    */     }
/*  47: 82 */     throw new IllegalStateException("Drawing group id " + drawingGroupId + " doesn't exist.");
/*  48:    */   }
/*  49:    */   
/*  50:    */   @Deprecated
/*  51:    */   @Removal(version="4.0")
/*  52:    */   public int allocateShapeId(short drawingGroupId, EscherDgRecord dg)
/*  53:    */   {
/*  54: 98 */     return allocateShapeId(dg);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public int allocateShapeId(EscherDgRecord dg)
/*  58:    */   {
/*  59:109 */     return this.dgg.allocateShapeId(dg, true);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public short findNewDrawingGroupId()
/*  63:    */   {
/*  64:118 */     return this.dgg.findNewDrawingGroupId();
/*  65:    */   }
/*  66:    */   
/*  67:    */   public EscherDggRecord getDgg()
/*  68:    */   {
/*  69:127 */     return this.dgg;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void incrementDrawingsSaved()
/*  73:    */   {
/*  74:134 */     this.dgg.setDrawingsSaved(this.dgg.getDrawingsSaved() + 1);
/*  75:    */   }
/*  76:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.model.DrawingManager2
 * JD-Core Version:    0.7.0.1
 */