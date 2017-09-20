/*   1:    */ package org.apache.poi.xdgf.usermodel;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.visio.x2012.main.DocumentSettingsType;
/*   4:    */ import com.microsoft.schemas.office.visio.x2012.main.StyleSheetType;
/*   5:    */ import com.microsoft.schemas.office.visio.x2012.main.StyleSheetsType;
/*   6:    */ import com.microsoft.schemas.office.visio.x2012.main.VisioDocumentType;
/*   7:    */ import java.util.HashMap;
/*   8:    */ import java.util.Map;
/*   9:    */ import org.apache.poi.POIXMLException;
/*  10:    */ import org.apache.poi.util.Internal;
/*  11:    */ 
/*  12:    */ public class XDGFDocument
/*  13:    */ {
/*  14:    */   protected VisioDocumentType _document;
/*  15: 40 */   Map<Long, XDGFStyleSheet> _styleSheets = new HashMap();
/*  16: 43 */   long _defaultFillStyle = 0L;
/*  17: 44 */   long _defaultGuideStyle = 0L;
/*  18: 45 */   long _defaultLineStyle = 0L;
/*  19: 46 */   long _defaultTextStyle = 0L;
/*  20:    */   
/*  21:    */   public XDGFDocument(VisioDocumentType document)
/*  22:    */   {
/*  23: 51 */     this._document = document;
/*  24: 53 */     if (!this._document.isSetDocumentSettings()) {
/*  25: 54 */       throw new POIXMLException("Document settings not found");
/*  26:    */     }
/*  27: 56 */     DocumentSettingsType docSettings = this._document.getDocumentSettings();
/*  28: 58 */     if (docSettings.isSetDefaultFillStyle()) {
/*  29: 59 */       this._defaultFillStyle = docSettings.getDefaultFillStyle();
/*  30:    */     }
/*  31: 61 */     if (docSettings.isSetDefaultGuideStyle()) {
/*  32: 62 */       this._defaultGuideStyle = docSettings.getDefaultGuideStyle();
/*  33:    */     }
/*  34: 64 */     if (docSettings.isSetDefaultLineStyle()) {
/*  35: 65 */       this._defaultLineStyle = docSettings.getDefaultLineStyle();
/*  36:    */     }
/*  37: 67 */     if (docSettings.isSetDefaultTextStyle()) {
/*  38: 68 */       this._defaultTextStyle = docSettings.getDefaultTextStyle();
/*  39:    */     }
/*  40: 70 */     if (this._document.isSetStyleSheets()) {
/*  41: 72 */       for (StyleSheetType styleSheet : this._document.getStyleSheets().getStyleSheetArray()) {
/*  42: 73 */         this._styleSheets.put(Long.valueOf(styleSheet.getID()), new XDGFStyleSheet(styleSheet, this));
/*  43:    */       }
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   @Internal
/*  48:    */   public VisioDocumentType getXmlObject()
/*  49:    */   {
/*  50: 81 */     return this._document;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public XDGFStyleSheet getStyleById(long id)
/*  54:    */   {
/*  55: 86 */     return (XDGFStyleSheet)this._styleSheets.get(Long.valueOf(id));
/*  56:    */   }
/*  57:    */   
/*  58:    */   public XDGFStyleSheet getDefaultFillStyle()
/*  59:    */   {
/*  60: 91 */     XDGFStyleSheet style = getStyleById(this._defaultFillStyle);
/*  61: 92 */     if (style == null) {
/*  62: 93 */       throw new POIXMLException("No default fill style found!");
/*  63:    */     }
/*  64: 94 */     return style;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public XDGFStyleSheet getDefaultGuideStyle()
/*  68:    */   {
/*  69: 98 */     XDGFStyleSheet style = getStyleById(this._defaultGuideStyle);
/*  70: 99 */     if (style == null) {
/*  71:100 */       throw new POIXMLException("No default guide style found!");
/*  72:    */     }
/*  73:101 */     return style;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public XDGFStyleSheet getDefaultLineStyle()
/*  77:    */   {
/*  78:105 */     XDGFStyleSheet style = getStyleById(this._defaultLineStyle);
/*  79:106 */     if (style == null) {
/*  80:107 */       throw new POIXMLException("No default line style found!");
/*  81:    */     }
/*  82:108 */     return style;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public XDGFStyleSheet getDefaultTextStyle()
/*  86:    */   {
/*  87:112 */     XDGFStyleSheet style = getStyleById(this._defaultTextStyle);
/*  88:113 */     if (style == null) {
/*  89:114 */       throw new POIXMLException("No default text style found!");
/*  90:    */     }
/*  91:115 */     return style;
/*  92:    */   }
/*  93:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.XDGFDocument
 * JD-Core Version:    0.7.0.1
 */