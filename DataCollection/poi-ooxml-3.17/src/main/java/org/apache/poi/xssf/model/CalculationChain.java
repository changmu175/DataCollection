/*   1:    */ package org.apache.poi.xssf.model;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import org.apache.poi.POIXMLDocumentPart;
/*   7:    */ import org.apache.poi.POIXMLTypeLoader;
/*   8:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   9:    */ import org.apache.xmlbeans.XmlException;
/*  10:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcCell;
/*  11:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcChain;
/*  12:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcChain.Factory;
/*  13:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CalcChainDocument;
/*  14:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CalcChainDocument.Factory;
/*  15:    */ 
/*  16:    */ public class CalculationChain
/*  17:    */   extends POIXMLDocumentPart
/*  18:    */ {
/*  19:    */   private CTCalcChain chain;
/*  20:    */   
/*  21:    */   public CalculationChain()
/*  22:    */   {
/*  23: 43 */     this.chain = CTCalcChain.Factory.newInstance();
/*  24:    */   }
/*  25:    */   
/*  26:    */   public CalculationChain(PackagePart part)
/*  27:    */     throws IOException
/*  28:    */   {
/*  29: 50 */     super(part);
/*  30: 51 */     readFrom(part.getInputStream());
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void readFrom(InputStream is)
/*  34:    */     throws IOException
/*  35:    */   {
/*  36:    */     try
/*  37:    */     {
/*  38: 56 */       CalcChainDocument doc = CalcChainDocument.Factory.parse(is, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  39: 57 */       this.chain = doc.getCalcChain();
/*  40:    */     }
/*  41:    */     catch (XmlException e)
/*  42:    */     {
/*  43: 59 */       throw new IOException(e.getLocalizedMessage());
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void writeTo(OutputStream out)
/*  48:    */     throws IOException
/*  49:    */   {
/*  50: 63 */     CalcChainDocument doc = CalcChainDocument.Factory.newInstance();
/*  51: 64 */     doc.setCalcChain(this.chain);
/*  52: 65 */     doc.save(out, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  53:    */   }
/*  54:    */   
/*  55:    */   protected void commit()
/*  56:    */     throws IOException
/*  57:    */   {
/*  58: 70 */     PackagePart part = getPackagePart();
/*  59: 71 */     OutputStream out = part.getOutputStream();
/*  60: 72 */     writeTo(out);
/*  61: 73 */     out.close();
/*  62:    */   }
/*  63:    */   
/*  64:    */   public CTCalcChain getCTCalcChain()
/*  65:    */   {
/*  66: 78 */     return this.chain;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void removeItem(int sheetId, String ref)
/*  70:    */   {
/*  71: 89 */     int id = -1;
/*  72: 90 */     CTCalcCell[] c = this.chain.getCArray();
/*  73: 92 */     for (int i = 0; i < c.length; i++)
/*  74:    */     {
/*  75: 94 */       if (c[i].isSetI()) {
/*  76: 94 */         id = c[i].getI();
/*  77:    */       }
/*  78: 96 */       if ((id == sheetId) && (c[i].getR().equals(ref)))
/*  79:    */       {
/*  80: 97 */         if ((c[i].isSetI()) && (i < c.length - 1) && (!c[(i + 1)].isSetI())) {
/*  81: 98 */           c[(i + 1)].setI(id);
/*  82:    */         }
/*  83:100 */         this.chain.removeC(i);
/*  84:101 */         break;
/*  85:    */       }
/*  86:    */     }
/*  87:    */   }
/*  88:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.model.CalculationChain
 * JD-Core Version:    0.7.0.1
 */