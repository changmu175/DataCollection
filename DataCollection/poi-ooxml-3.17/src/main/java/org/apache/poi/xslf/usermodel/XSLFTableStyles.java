/*  1:   */ package org.apache.poi.xslf.usermodel;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.InputStream;
/*  5:   */ import java.util.ArrayList;
/*  6:   */ import java.util.Collections;
/*  7:   */ import java.util.Iterator;
/*  8:   */ import java.util.List;
/*  9:   */ import org.apache.poi.POIXMLDocumentPart;
/* 10:   */ import org.apache.poi.openxml4j.opc.PackagePart;
/* 11:   */ import org.apache.xmlbeans.XmlException;
/* 12:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle;
/* 13:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleList;
/* 14:   */ import org.openxmlformats.schemas.drawingml.x2006.main.TblStyleLstDocument;
/* 15:   */ import org.openxmlformats.schemas.drawingml.x2006.main.TblStyleLstDocument.Factory;
/* 16:   */ 
/* 17:   */ public class XSLFTableStyles
/* 18:   */   extends POIXMLDocumentPart
/* 19:   */   implements Iterable<XSLFTableStyle>
/* 20:   */ {
/* 21:   */   private CTTableStyleList _tblStyleLst;
/* 22:   */   private List<XSLFTableStyle> _styles;
/* 23:   */   
/* 24:   */   public XSLFTableStyles() {}
/* 25:   */   
/* 26:   */   public XSLFTableStyles(PackagePart part)
/* 27:   */     throws IOException, XmlException
/* 28:   */   {
/* 29:47 */     super(part);
/* 30:   */     
/* 31:49 */     InputStream is = getPackagePart().getInputStream();
/* 32:50 */     TblStyleLstDocument styleDoc = TblStyleLstDocument.Factory.parse(is);
/* 33:51 */     is.close();
/* 34:52 */     this._tblStyleLst = styleDoc.getTblStyleLst();
/* 35:53 */     CTTableStyle[] tblStyleArray = this._tblStyleLst.getTblStyleArray();
/* 36:54 */     this._styles = new ArrayList(tblStyleArray.length);
/* 37:55 */     for (CTTableStyle c : tblStyleArray) {
/* 38:56 */       this._styles.add(new XSLFTableStyle(c));
/* 39:   */     }
/* 40:   */   }
/* 41:   */   
/* 42:   */   public CTTableStyleList getXmlObject()
/* 43:   */   {
/* 44:61 */     return this._tblStyleLst;
/* 45:   */   }
/* 46:   */   
/* 47:   */   public Iterator<XSLFTableStyle> iterator()
/* 48:   */   {
/* 49:65 */     return this._styles.iterator();
/* 50:   */   }
/* 51:   */   
/* 52:   */   public List<XSLFTableStyle> getStyles()
/* 53:   */   {
/* 54:69 */     return Collections.unmodifiableList(this._styles);
/* 55:   */   }
/* 56:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFTableStyles
 * JD-Core Version:    0.7.0.1
 */