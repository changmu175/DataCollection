/*  1:   */ package org.apache.poi.xwpf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.POIXMLDocumentPart;
/*  4:   */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  5:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBlip;
/*  6:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties;
/*  7:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
/*  8:   */ import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
/*  9:   */ import org.openxmlformats.schemas.drawingml.x2006.picture.CTPictureNonVisual;
/* 10:   */ 
/* 11:   */ public class XWPFPicture
/* 12:   */ {
/* 13:   */   private CTPicture ctPic;
/* 14:   */   private String description;
/* 15:   */   private XWPFRun run;
/* 16:   */   
/* 17:   */   public XWPFPicture(CTPicture ctPic, XWPFRun run)
/* 18:   */   {
/* 19:35 */     this.run = run;
/* 20:36 */     this.ctPic = ctPic;
/* 21:37 */     this.description = ctPic.getNvPicPr().getCNvPr().getDescr();
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void setPictureReference(PackageRelationship rel)
/* 25:   */   {
/* 26:46 */     this.ctPic.getBlipFill().getBlip().setEmbed(rel.getId());
/* 27:   */   }
/* 28:   */   
/* 29:   */   public CTPicture getCTPicture()
/* 30:   */   {
/* 31:55 */     return this.ctPic;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public XWPFPictureData getPictureData()
/* 35:   */   {
/* 36:63 */     CTBlipFillProperties blipProps = this.ctPic.getBlipFill();
/* 37:65 */     if ((blipProps == null) || (!blipProps.isSetBlip())) {
/* 38:67 */       return null;
/* 39:   */     }
/* 40:70 */     String blipId = blipProps.getBlip().getEmbed();
/* 41:71 */     POIXMLDocumentPart part = this.run.getParent().getPart();
/* 42:72 */     if (part != null)
/* 43:   */     {
/* 44:73 */       POIXMLDocumentPart relatedPart = part.getRelationById(blipId);
/* 45:74 */       if ((relatedPart instanceof XWPFPictureData)) {
/* 46:75 */         return (XWPFPictureData)relatedPart;
/* 47:   */       }
/* 48:   */     }
/* 49:78 */     return null;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public String getDescription()
/* 53:   */   {
/* 54:82 */     return this.description;
/* 55:   */   }
/* 56:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFPicture
 * JD-Core Version:    0.7.0.1
 */