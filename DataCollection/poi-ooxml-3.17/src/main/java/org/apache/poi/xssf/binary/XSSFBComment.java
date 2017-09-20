/*   1:    */ package org.apache.poi.xssf.binary;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.usermodel.ClientAnchor;
/*   4:    */ import org.apache.poi.ss.usermodel.RichTextString;
/*   5:    */ import org.apache.poi.ss.util.CellAddress;
/*   6:    */ import org.apache.poi.util.Internal;
/*   7:    */ import org.apache.poi.xssf.usermodel.XSSFComment;
/*   8:    */ 
/*   9:    */ @Internal
/*  10:    */ class XSSFBComment
/*  11:    */   extends XSSFComment
/*  12:    */ {
/*  13:    */   private final CellAddress cellAddress;
/*  14:    */   private final String author;
/*  15:    */   private final XSSFBRichTextString comment;
/*  16: 36 */   private boolean visible = true;
/*  17:    */   
/*  18:    */   XSSFBComment(CellAddress cellAddress, String author, String comment)
/*  19:    */   {
/*  20: 39 */     super(null, null, null);
/*  21: 40 */     this.cellAddress = cellAddress;
/*  22: 41 */     this.author = author;
/*  23: 42 */     this.comment = new XSSFBRichTextString(comment);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setVisible(boolean visible)
/*  27:    */   {
/*  28: 47 */     throw new IllegalArgumentException("XSSFBComment is read only.");
/*  29:    */   }
/*  30:    */   
/*  31:    */   public boolean isVisible()
/*  32:    */   {
/*  33: 52 */     return this.visible;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public CellAddress getAddress()
/*  37:    */   {
/*  38: 57 */     return this.cellAddress;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setAddress(CellAddress addr)
/*  42:    */   {
/*  43: 62 */     throw new IllegalArgumentException("XSSFBComment is read only");
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setAddress(int row, int col)
/*  47:    */   {
/*  48: 67 */     throw new IllegalArgumentException("XSSFBComment is read only");
/*  49:    */   }
/*  50:    */   
/*  51:    */   public int getRow()
/*  52:    */   {
/*  53: 73 */     return this.cellAddress.getRow();
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setRow(int row)
/*  57:    */   {
/*  58: 78 */     throw new IllegalArgumentException("XSSFBComment is read only");
/*  59:    */   }
/*  60:    */   
/*  61:    */   public int getColumn()
/*  62:    */   {
/*  63: 83 */     return this.cellAddress.getColumn();
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void setColumn(int col)
/*  67:    */   {
/*  68: 88 */     throw new IllegalArgumentException("XSSFBComment is read only");
/*  69:    */   }
/*  70:    */   
/*  71:    */   public String getAuthor()
/*  72:    */   {
/*  73: 93 */     return this.author;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void setAuthor(String author)
/*  77:    */   {
/*  78: 98 */     throw new IllegalArgumentException("XSSFBComment is read only");
/*  79:    */   }
/*  80:    */   
/*  81:    */   public XSSFBRichTextString getString()
/*  82:    */   {
/*  83:103 */     return this.comment;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setString(RichTextString string)
/*  87:    */   {
/*  88:108 */     throw new IllegalArgumentException("XSSFBComment is read only");
/*  89:    */   }
/*  90:    */   
/*  91:    */   public ClientAnchor getClientAnchor()
/*  92:    */   {
/*  93:113 */     return null;
/*  94:    */   }
/*  95:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.binary.XSSFBComment
 * JD-Core Version:    0.7.0.1
 */