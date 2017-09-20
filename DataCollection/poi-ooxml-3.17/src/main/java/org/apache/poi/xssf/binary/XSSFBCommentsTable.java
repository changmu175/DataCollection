/*  1:   */ package org.apache.poi.xssf.binary;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.InputStream;
/*  5:   */ import java.util.ArrayList;
/*  6:   */ import java.util.LinkedList;
/*  7:   */ import java.util.List;
/*  8:   */ import java.util.Map;
/*  9:   */ import java.util.Queue;
/* 10:   */ import java.util.TreeMap;
/* 11:   */ import org.apache.poi.ss.util.CellAddress;
/* 12:   */ import org.apache.poi.util.Internal;
/* 13:   */ import org.apache.poi.util.LittleEndian;
/* 14:   */ 
/* 15:   */ @Internal
/* 16:   */ public class XSSFBCommentsTable
/* 17:   */   extends XSSFBParser
/* 18:   */ {
/* 19:39 */   private Map<CellAddress, XSSFBComment> comments = new TreeMap();
/* 20:40 */   private Queue<CellAddress> commentAddresses = new LinkedList();
/* 21:41 */   private List<String> authors = new ArrayList();
/* 22:44 */   private int authorId = -1;
/* 23:45 */   private CellAddress cellAddress = null;
/* 24:46 */   private XSSFBCellRange cellRange = null;
/* 25:47 */   private String comment = null;
/* 26:48 */   private StringBuilder authorBuffer = new StringBuilder();
/* 27:   */   
/* 28:   */   public XSSFBCommentsTable(InputStream is)
/* 29:   */     throws IOException
/* 30:   */   {
/* 31:52 */     super(is);
/* 32:53 */     parse();
/* 33:54 */     this.commentAddresses.addAll(this.comments.keySet());
/* 34:   */   }
/* 35:   */   
/* 36:   */   public void handleRecord(int id, byte[] data)
/* 37:   */     throws XSSFBParseException
/* 38:   */   {
/* 39:59 */     XSSFBRecordType recordType = XSSFBRecordType.lookup(id);
/* 40:60 */     switch (1.$SwitchMap$org$apache$poi$xssf$binary$XSSFBRecordType[recordType.ordinal()])
/* 41:   */     {
/* 42:   */     case 1: 
/* 43:62 */       int offset = 0;
/* 44:63 */       this.authorId = XSSFBUtils.castToInt(LittleEndian.getUInt(data));offset += 4;
/* 45:64 */       this.cellRange = XSSFBCellRange.parse(data, offset, this.cellRange);
/* 46:65 */       offset += 16;
/* 47:   */       
/* 48:67 */       this.cellAddress = new CellAddress(this.cellRange.firstRow, this.cellRange.firstCol);
/* 49:68 */       break;
/* 50:   */     case 2: 
/* 51:70 */       XSSFBRichStr xssfbRichStr = XSSFBRichStr.build(data, 0);
/* 52:71 */       this.comment = xssfbRichStr.getString();
/* 53:72 */       break;
/* 54:   */     case 3: 
/* 55:74 */       this.comments.put(this.cellAddress, new XSSFBComment(this.cellAddress, (String)this.authors.get(this.authorId), this.comment));
/* 56:75 */       this.authorId = -1;
/* 57:76 */       this.cellAddress = null;
/* 58:77 */       break;
/* 59:   */     case 4: 
/* 60:79 */       this.authorBuffer.setLength(0);
/* 61:80 */       XSSFBUtils.readXLWideString(data, 0, this.authorBuffer);
/* 62:81 */       this.authors.add(this.authorBuffer.toString());
/* 63:   */     }
/* 64:   */   }
/* 65:   */   
/* 66:   */   public Queue<CellAddress> getAddresses()
/* 67:   */   {
/* 68:88 */     return this.commentAddresses;
/* 69:   */   }
/* 70:   */   
/* 71:   */   public XSSFBComment get(CellAddress cellAddress)
/* 72:   */   {
/* 73:92 */     if (cellAddress == null) {
/* 74:93 */       return null;
/* 75:   */     }
/* 76:95 */     return (XSSFBComment)this.comments.get(cellAddress);
/* 77:   */   }
/* 78:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.binary.XSSFBCommentsTable
 * JD-Core Version:    0.7.0.1
 */