/*   1:    */ package org.apache.poi.xwpf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.POIXMLDocumentPart;
/*   4:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtPr;
/*   5:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
/*   6:    */ 
/*   7:    */ public abstract class AbstractXWPFSDT
/*   8:    */   implements ISDTContents
/*   9:    */ {
/*  10:    */   private final String title;
/*  11:    */   private final String tag;
/*  12:    */   private final IBody part;
/*  13:    */   
/*  14:    */   public AbstractXWPFSDT(CTSdtPr pr, IBody part)
/*  15:    */   {
/*  16: 36 */     if (pr == null)
/*  17:    */     {
/*  18: 37 */       this.title = "";
/*  19: 38 */       this.tag = "";
/*  20:    */     }
/*  21:    */     else
/*  22:    */     {
/*  23: 40 */       CTString[] aliases = pr.getAliasArray();
/*  24: 41 */       if ((aliases != null) && (aliases.length > 0)) {
/*  25: 42 */         this.title = aliases[0].getVal();
/*  26:    */       } else {
/*  27: 44 */         this.title = "";
/*  28:    */       }
/*  29: 46 */       CTString[] tags = pr.getTagArray();
/*  30: 47 */       if ((tags != null) && (tags.length > 0)) {
/*  31: 48 */         this.tag = tags[0].getVal();
/*  32:    */       } else {
/*  33: 50 */         this.tag = "";
/*  34:    */       }
/*  35:    */     }
/*  36: 53 */     this.part = part;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String getTitle()
/*  40:    */   {
/*  41: 61 */     return this.title;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public String getTag()
/*  45:    */   {
/*  46: 68 */     return this.tag;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public abstract ISDTContent getContent();
/*  50:    */   
/*  51:    */   public IBody getBody()
/*  52:    */   {
/*  53: 80 */     return null;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public POIXMLDocumentPart getPart()
/*  57:    */   {
/*  58: 87 */     return this.part.getPart();
/*  59:    */   }
/*  60:    */   
/*  61:    */   public BodyType getPartType()
/*  62:    */   {
/*  63: 94 */     return BodyType.CONTENTCONTROL;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public BodyElementType getElementType()
/*  67:    */   {
/*  68:101 */     return BodyElementType.CONTENTCONTROL;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public XWPFDocument getDocument()
/*  72:    */   {
/*  73:105 */     return this.part.getXWPFDocument();
/*  74:    */   }
/*  75:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.AbstractXWPFSDT
 * JD-Core Version:    0.7.0.1
 */