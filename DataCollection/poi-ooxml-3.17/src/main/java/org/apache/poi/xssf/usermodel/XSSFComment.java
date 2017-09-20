/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.excel.CTClientData;
/*   4:    */ import com.microsoft.schemas.vml.CTShape;
/*   5:    */ import java.math.BigInteger;
/*   6:    */ import org.apache.poi.ss.usermodel.ClientAnchor;
/*   7:    */ import org.apache.poi.ss.usermodel.Comment;
/*   8:    */ import org.apache.poi.ss.usermodel.RichTextString;
/*   9:    */ import org.apache.poi.ss.util.CellAddress;
/*  10:    */ import org.apache.poi.ss.util.CellReference;
/*  11:    */ import org.apache.poi.xssf.model.CommentsTable;
/*  12:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComment;
/*  13:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst;
/*  14:    */ 
/*  15:    */ public class XSSFComment
/*  16:    */   implements Comment
/*  17:    */ {
/*  18:    */   private final CTComment _comment;
/*  19:    */   private final CommentsTable _comments;
/*  20:    */   private final CTShape _vmlShape;
/*  21:    */   private XSSFRichTextString _str;
/*  22:    */   
/*  23:    */   public XSSFComment(CommentsTable comments, CTComment comment, CTShape vmlShape)
/*  24:    */   {
/*  25: 51 */     this._comment = comment;
/*  26: 52 */     this._comments = comments;
/*  27: 53 */     this._vmlShape = vmlShape;
/*  28: 57 */     if ((vmlShape != null) && (vmlShape.sizeOfClientDataArray() > 0))
/*  29:    */     {
/*  30: 58 */       CellReference ref = new CellReference(comment.getRef());
/*  31: 59 */       CTClientData clientData = vmlShape.getClientDataArray(0);
/*  32: 60 */       clientData.setRowArray(0, new BigInteger(String.valueOf(ref.getRow())));
/*  33: 61 */       clientData.setColumnArray(0, new BigInteger(String.valueOf(ref.getCol())));
/*  34:    */       
/*  35: 63 */       avoidXmlbeansCorruptPointer(vmlShape);
/*  36:    */     }
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String getAuthor()
/*  40:    */   {
/*  41: 73 */     return this._comments.getAuthor((int)this._comment.getAuthorId());
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setAuthor(String author)
/*  45:    */   {
/*  46: 83 */     this._comment.setAuthorId(this._comments.findAuthor(author));
/*  47:    */   }
/*  48:    */   
/*  49:    */   public int getColumn()
/*  50:    */   {
/*  51: 93 */     return getAddress().getColumn();
/*  52:    */   }
/*  53:    */   
/*  54:    */   public int getRow()
/*  55:    */   {
/*  56:101 */     return getAddress().getRow();
/*  57:    */   }
/*  58:    */   
/*  59:    */   public boolean isVisible()
/*  60:    */   {
/*  61:111 */     boolean visible = false;
/*  62:112 */     if (this._vmlShape != null)
/*  63:    */     {
/*  64:113 */       String style = this._vmlShape.getStyle();
/*  65:114 */       visible = (style != null) && (style.contains("visibility:visible"));
/*  66:    */     }
/*  67:116 */     return visible;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void setVisible(boolean visible)
/*  71:    */   {
/*  72:126 */     if (this._vmlShape != null)
/*  73:    */     {
/*  74:    */       String style;
/*  75:    */       String style;
/*  76:128 */       if (visible) {
/*  77:128 */         style = "position:absolute;visibility:visible";
/*  78:    */       } else {
/*  79:129 */         style = "position:absolute;visibility:hidden";
/*  80:    */       }
/*  81:130 */       this._vmlShape.setStyle(style);
/*  82:    */     }
/*  83:    */   }
/*  84:    */   
/*  85:    */   public CellAddress getAddress()
/*  86:    */   {
/*  87:136 */     return new CellAddress(this._comment.getRef());
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setAddress(int row, int col)
/*  91:    */   {
/*  92:141 */     setAddress(new CellAddress(row, col));
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setAddress(CellAddress address)
/*  96:    */   {
/*  97:146 */     CellAddress oldRef = new CellAddress(this._comment.getRef());
/*  98:147 */     if (address.equals(oldRef)) {
/*  99:149 */       return;
/* 100:    */     }
/* 101:152 */     this._comment.setRef(address.formatAsString());
/* 102:153 */     this._comments.referenceUpdated(oldRef, this._comment);
/* 103:155 */     if (this._vmlShape != null)
/* 104:    */     {
/* 105:156 */       CTClientData clientData = this._vmlShape.getClientDataArray(0);
/* 106:157 */       clientData.setRowArray(0, new BigInteger(String.valueOf(address.getRow())));
/* 107:158 */       clientData.setColumnArray(0, new BigInteger(String.valueOf(address.getColumn())));
/* 108:    */       
/* 109:160 */       avoidXmlbeansCorruptPointer(this._vmlShape);
/* 110:    */     }
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void setColumn(int col)
/* 114:    */   {
/* 115:173 */     setAddress(getRow(), col);
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void setRow(int row)
/* 119:    */   {
/* 120:185 */     setAddress(row, getColumn());
/* 121:    */   }
/* 122:    */   
/* 123:    */   public XSSFRichTextString getString()
/* 124:    */   {
/* 125:193 */     if (this._str == null)
/* 126:    */     {
/* 127:194 */       CTRst rst = this._comment.getText();
/* 128:195 */       if (rst != null) {
/* 129:195 */         this._str = new XSSFRichTextString(this._comment.getText());
/* 130:    */       }
/* 131:    */     }
/* 132:197 */     return this._str;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void setString(RichTextString string)
/* 136:    */   {
/* 137:207 */     if (!(string instanceof XSSFRichTextString)) {
/* 138:208 */       throw new IllegalArgumentException("Only XSSFRichTextString argument is supported");
/* 139:    */     }
/* 140:210 */     this._str = ((XSSFRichTextString)string);
/* 141:211 */     this._comment.setText(this._str.getCTRst());
/* 142:    */   }
/* 143:    */   
/* 144:    */   public void setString(String string)
/* 145:    */   {
/* 146:215 */     setString(new XSSFRichTextString(string));
/* 147:    */   }
/* 148:    */   
/* 149:    */   public ClientAnchor getClientAnchor()
/* 150:    */   {
/* 151:220 */     String position = this._vmlShape.getClientDataArray(0).getAnchorArray(0);
/* 152:221 */     int[] pos = new int[8];
/* 153:222 */     int i = 0;
/* 154:223 */     for (String s : position.split(",")) {
/* 155:224 */       pos[(i++)] = Integer.parseInt(s.trim());
/* 156:    */     }
/* 157:226 */     XSSFClientAnchor ca = new XSSFClientAnchor(pos[1] * 9525, pos[3] * 9525, pos[5] * 9525, pos[7] * 9525, pos[0], pos[2], pos[4], pos[6]);
/* 158:227 */     return ca;
/* 159:    */   }
/* 160:    */   
/* 161:    */   protected CTComment getCTComment()
/* 162:    */   {
/* 163:234 */     return this._comment;
/* 164:    */   }
/* 165:    */   
/* 166:    */   protected CTShape getCTShape()
/* 167:    */   {
/* 168:238 */     return this._vmlShape;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public boolean equals(Object obj)
/* 172:    */   {
/* 173:243 */     if (!(obj instanceof XSSFComment)) {
/* 174:244 */       return false;
/* 175:    */     }
/* 176:246 */     XSSFComment other = (XSSFComment)obj;
/* 177:247 */     return (getCTComment() == other.getCTComment()) && (getCTShape() == other.getCTShape());
/* 178:    */   }
/* 179:    */   
/* 180:    */   public int hashCode()
/* 181:    */   {
/* 182:253 */     return (getRow() * 17 + getColumn()) * 31;
/* 183:    */   }
/* 184:    */   
/* 185:    */   private static void avoidXmlbeansCorruptPointer(CTShape vmlShape)
/* 186:    */   {
/* 187:260 */     vmlShape.getClientDataList().toString();
/* 188:    */   }
/* 189:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFComment
 * JD-Core Version:    0.7.0.1
 */