/*   1:    */ package org.apache.poi.xssf.model;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Vector;
/*   8:    */ import org.apache.poi.POIXMLDocumentPart;
/*   9:    */ import org.apache.poi.POIXMLTypeLoader;
/*  10:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  11:    */ import org.apache.poi.xssf.usermodel.XSSFSheet;
/*  12:    */ import org.apache.poi.xssf.usermodel.helpers.XSSFSingleXmlCell;
/*  13:    */ import org.apache.xmlbeans.XmlException;
/*  14:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell;
/*  15:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCells;
/*  16:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCells.Factory;
/*  17:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.SingleXmlCellsDocument;
/*  18:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.SingleXmlCellsDocument.Factory;
/*  19:    */ 
/*  20:    */ public class SingleXmlCells
/*  21:    */   extends POIXMLDocumentPart
/*  22:    */ {
/*  23:    */   private CTSingleXmlCells singleXMLCells;
/*  24:    */   
/*  25:    */   public SingleXmlCells()
/*  26:    */   {
/*  27: 53 */     this.singleXMLCells = CTSingleXmlCells.Factory.newInstance();
/*  28:    */   }
/*  29:    */   
/*  30:    */   public SingleXmlCells(PackagePart part)
/*  31:    */     throws IOException
/*  32:    */   {
/*  33: 61 */     super(part);
/*  34: 62 */     readFrom(part.getInputStream());
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void readFrom(InputStream is)
/*  38:    */     throws IOException
/*  39:    */   {
/*  40:    */     try
/*  41:    */     {
/*  42: 67 */       SingleXmlCellsDocument doc = SingleXmlCellsDocument.Factory.parse(is, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  43: 68 */       this.singleXMLCells = doc.getSingleXmlCells();
/*  44:    */     }
/*  45:    */     catch (XmlException e)
/*  46:    */     {
/*  47: 70 */       throw new IOException(e.getLocalizedMessage());
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   public XSSFSheet getXSSFSheet()
/*  52:    */   {
/*  53: 75 */     return (XSSFSheet)getParent();
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected void writeTo(OutputStream out)
/*  57:    */     throws IOException
/*  58:    */   {
/*  59: 79 */     SingleXmlCellsDocument doc = SingleXmlCellsDocument.Factory.newInstance();
/*  60: 80 */     doc.setSingleXmlCells(this.singleXMLCells);
/*  61: 81 */     doc.save(out, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected void commit()
/*  65:    */     throws IOException
/*  66:    */   {
/*  67: 86 */     PackagePart part = getPackagePart();
/*  68: 87 */     OutputStream out = part.getOutputStream();
/*  69: 88 */     writeTo(out);
/*  70: 89 */     out.close();
/*  71:    */   }
/*  72:    */   
/*  73:    */   public CTSingleXmlCells getCTSingleXMLCells()
/*  74:    */   {
/*  75: 93 */     return this.singleXMLCells;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public List<XSSFSingleXmlCell> getAllSimpleXmlCell()
/*  79:    */   {
/*  80:101 */     List<XSSFSingleXmlCell> list = new Vector();
/*  81:103 */     for (CTSingleXmlCell singleXmlCell : this.singleXMLCells.getSingleXmlCellArray()) {
/*  82:104 */       list.add(new XSSFSingleXmlCell(singleXmlCell, this));
/*  83:    */     }
/*  84:106 */     return list;
/*  85:    */   }
/*  86:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.model.SingleXmlCells
 * JD-Core Version:    0.7.0.1
 */