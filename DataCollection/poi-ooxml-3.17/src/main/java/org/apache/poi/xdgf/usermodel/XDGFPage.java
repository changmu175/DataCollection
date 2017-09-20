/*   1:    */ package org.apache.poi.xdgf.usermodel;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.visio.x2012.main.PageType;
/*   4:    */ import java.awt.geom.Point2D.Double;
/*   5:    */ import java.awt.geom.Rectangle2D;
/*   6:    */ import java.awt.geom.Rectangle2D.Double;
/*   7:    */ import java.util.List;
/*   8:    */ import org.apache.poi.POIXMLException;
/*   9:    */ import org.apache.poi.util.Internal;
/*  10:    */ import org.apache.poi.xdgf.geom.Dimension2dDouble;
/*  11:    */ 
/*  12:    */ public class XDGFPage
/*  13:    */ {
/*  14:    */   private PageType _page;
/*  15:    */   protected XDGFPageContents _content;
/*  16:    */   protected XDGFPages _pages;
/*  17: 37 */   protected XDGFSheet _pageSheet = null;
/*  18:    */   
/*  19:    */   public XDGFPage(PageType page, XDGFPageContents content, XDGFDocument document, XDGFPages pages)
/*  20:    */   {
/*  21: 41 */     this._page = page;
/*  22: 42 */     this._content = content;
/*  23: 43 */     this._pages = pages;
/*  24: 44 */     content.setPage(this);
/*  25: 46 */     if (page.isSetPageSheet()) {
/*  26: 47 */       this._pageSheet = new XDGFPageSheet(page.getPageSheet(), document);
/*  27:    */     }
/*  28:    */   }
/*  29:    */   
/*  30:    */   @Internal
/*  31:    */   protected PageType getXmlObject()
/*  32:    */   {
/*  33: 52 */     return this._page;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public long getID()
/*  37:    */   {
/*  38: 56 */     return this._page.getID();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public String getName()
/*  42:    */   {
/*  43: 60 */     return this._page.getName();
/*  44:    */   }
/*  45:    */   
/*  46:    */   public XDGFPageContents getContent()
/*  47:    */   {
/*  48: 64 */     return this._content;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public XDGFSheet getPageSheet()
/*  52:    */   {
/*  53: 68 */     return this._pageSheet;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public long getPageNumber()
/*  57:    */   {
/*  58: 72 */     return this._pages.getPageList().indexOf(this) + 1;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public Dimension2dDouble getPageSize()
/*  62:    */   {
/*  63: 79 */     XDGFCell w = this._pageSheet.getCell("PageWidth");
/*  64: 80 */     XDGFCell h = this._pageSheet.getCell("PageHeight");
/*  65: 82 */     if ((w == null) || (h == null)) {
/*  66: 83 */       throw new POIXMLException("Cannot determine page size");
/*  67:    */     }
/*  68: 85 */     return new Dimension2dDouble(Double.parseDouble(w.getValue()), Double.parseDouble(h.getValue()));
/*  69:    */   }
/*  70:    */   
/*  71:    */   public Point2D.Double getPageOffset()
/*  72:    */   {
/*  73: 93 */     XDGFCell xoffcell = this._pageSheet.getCell("XRulerOrigin");
/*  74: 94 */     XDGFCell yoffcell = this._pageSheet.getCell("YRulerOrigin");
/*  75:    */     
/*  76: 96 */     double xoffset = 0.0D;
/*  77: 97 */     double yoffset = 0.0D;
/*  78: 99 */     if (xoffcell != null) {
/*  79:100 */       xoffset = Double.parseDouble(xoffcell.getValue());
/*  80:    */     }
/*  81:102 */     if (xoffcell != null) {
/*  82:103 */       yoffset = Double.parseDouble(yoffcell.getValue());
/*  83:    */     }
/*  84:105 */     return new Point2D.Double(xoffset, yoffset);
/*  85:    */   }
/*  86:    */   
/*  87:    */   public Rectangle2D getBoundingBox()
/*  88:    */   {
/*  89:112 */     Dimension2dDouble sz = getPageSize();
/*  90:113 */     Point2D.Double offset = getPageOffset();
/*  91:    */     
/*  92:115 */     return new Rectangle2D.Double(-offset.getX(), -offset.getY(), sz.getWidth(), sz.getHeight());
/*  93:    */   }
/*  94:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.XDGFPage
 * JD-Core Version:    0.7.0.1
 */