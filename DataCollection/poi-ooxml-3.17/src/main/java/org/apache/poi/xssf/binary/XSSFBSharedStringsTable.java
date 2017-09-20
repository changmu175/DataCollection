/*   1:    */ package org.apache.poi.xssf.binary;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.List;
/*   7:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*   8:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   9:    */ import org.apache.poi.util.Internal;
/*  10:    */ import org.apache.poi.util.LittleEndian;
/*  11:    */ import org.xml.sax.SAXException;
/*  12:    */ 
/*  13:    */ @Internal
/*  14:    */ public class XSSFBSharedStringsTable
/*  15:    */ {
/*  16:    */   private int count;
/*  17:    */   private int uniqueCount;
/*  18: 53 */   private List<String> strings = new ArrayList();
/*  19:    */   
/*  20:    */   public XSSFBSharedStringsTable(OPCPackage pkg)
/*  21:    */     throws IOException, SAXException
/*  22:    */   {
/*  23: 62 */     ArrayList<PackagePart> parts = pkg.getPartsByContentType(XSSFBRelation.SHARED_STRINGS_BINARY.getContentType());
/*  24: 66 */     if (parts.size() > 0)
/*  25:    */     {
/*  26: 67 */       PackagePart sstPart = (PackagePart)parts.get(0);
/*  27:    */       
/*  28: 69 */       readFrom(sstPart.getInputStream());
/*  29:    */     }
/*  30:    */   }
/*  31:    */   
/*  32:    */   XSSFBSharedStringsTable(PackagePart part)
/*  33:    */     throws IOException, SAXException
/*  34:    */   {
/*  35: 77 */     readFrom(part.getInputStream());
/*  36:    */   }
/*  37:    */   
/*  38:    */   private void readFrom(InputStream inputStream)
/*  39:    */     throws IOException
/*  40:    */   {
/*  41: 81 */     SSTBinaryReader reader = new SSTBinaryReader(inputStream);
/*  42: 82 */     reader.parse();
/*  43:    */   }
/*  44:    */   
/*  45:    */   public List<String> getItems()
/*  46:    */   {
/*  47: 90 */     List<String> ret = new ArrayList(this.strings.size());
/*  48: 91 */     ret.addAll(this.strings);
/*  49: 92 */     return ret;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public String getEntryAt(int i)
/*  53:    */   {
/*  54: 96 */     return (String)this.strings.get(i);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public int getCount()
/*  58:    */   {
/*  59:106 */     return this.count;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public int getUniqueCount()
/*  63:    */   {
/*  64:117 */     return this.uniqueCount;
/*  65:    */   }
/*  66:    */   
/*  67:    */   private class SSTBinaryReader
/*  68:    */     extends XSSFBParser
/*  69:    */   {
/*  70:    */     SSTBinaryReader(InputStream is)
/*  71:    */     {
/*  72:123 */       super();
/*  73:    */     }
/*  74:    */     
/*  75:    */     public void handleRecord(int recordType, byte[] data)
/*  76:    */       throws XSSFBParseException
/*  77:    */     {
/*  78:128 */       XSSFBRecordType type = XSSFBRecordType.lookup(recordType);
/*  79:130 */       switch (XSSFBSharedStringsTable.1.$SwitchMap$org$apache$poi$xssf$binary$XSSFBRecordType[type.ordinal()])
/*  80:    */       {
/*  81:    */       case 1: 
/*  82:132 */         XSSFBRichStr rstr = XSSFBRichStr.build(data, 0);
/*  83:133 */         XSSFBSharedStringsTable.this.strings.add(rstr.getString());
/*  84:134 */         break;
/*  85:    */       case 2: 
/*  86:136 */         XSSFBSharedStringsTable.this.count = XSSFBUtils.castToInt(LittleEndian.getUInt(data, 0));
/*  87:137 */         XSSFBSharedStringsTable.this.uniqueCount = XSSFBUtils.castToInt(LittleEndian.getUInt(data, 4));
/*  88:    */       }
/*  89:    */     }
/*  90:    */   }
/*  91:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.binary.XSSFBSharedStringsTable
 * JD-Core Version:    0.7.0.1
 */