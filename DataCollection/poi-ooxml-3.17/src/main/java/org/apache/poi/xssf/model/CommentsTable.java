/*   1:    */ package org.apache.poi.xssf.model;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Map.Entry;
/*   9:    */ import java.util.TreeMap;
/*  10:    */ import org.apache.poi.POIXMLDocumentPart;
/*  11:    */ import org.apache.poi.POIXMLTypeLoader;
/*  12:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  13:    */ import org.apache.poi.ss.util.CellAddress;
/*  14:    */ import org.apache.poi.util.Internal;
/*  15:    */ import org.apache.poi.xssf.usermodel.XSSFComment;
/*  16:    */ import org.apache.xmlbeans.XmlException;
/*  17:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAuthors;
/*  18:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComment;
/*  19:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCommentList;
/*  20:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments;
/*  21:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments.Factory;
/*  22:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CommentsDocument;
/*  23:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CommentsDocument.Factory;
/*  24:    */ 
/*  25:    */ @Internal
/*  26:    */ public class CommentsTable
/*  27:    */   extends POIXMLDocumentPart
/*  28:    */ {
/*  29:    */   public static final String DEFAULT_AUTHOR = "";
/*  30:    */   public static final int DEFAULT_AUTHOR_ID = 0;
/*  31:    */   private CTComments comments;
/*  32:    */   private Map<CellAddress, CTComment> commentRefs;
/*  33:    */   
/*  34:    */   public CommentsTable()
/*  35:    */   {
/*  36: 57 */     this.comments = CTComments.Factory.newInstance();
/*  37: 58 */     this.comments.addNewCommentList();
/*  38: 59 */     this.comments.addNewAuthors().addAuthor("");
/*  39:    */   }
/*  40:    */   
/*  41:    */   public CommentsTable(PackagePart part)
/*  42:    */     throws IOException
/*  43:    */   {
/*  44: 66 */     super(part);
/*  45: 67 */     readFrom(part.getInputStream());
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void readFrom(InputStream is)
/*  49:    */     throws IOException
/*  50:    */   {
/*  51:    */     try
/*  52:    */     {
/*  53: 72 */       CommentsDocument doc = CommentsDocument.Factory.parse(is, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  54: 73 */       this.comments = doc.getComments();
/*  55:    */     }
/*  56:    */     catch (XmlException e)
/*  57:    */     {
/*  58: 75 */       throw new IOException(e.getLocalizedMessage());
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void writeTo(OutputStream out)
/*  63:    */     throws IOException
/*  64:    */   {
/*  65: 79 */     CommentsDocument doc = CommentsDocument.Factory.newInstance();
/*  66: 80 */     doc.setComments(this.comments);
/*  67: 81 */     doc.save(out, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected void commit()
/*  71:    */     throws IOException
/*  72:    */   {
/*  73: 86 */     PackagePart part = getPackagePart();
/*  74: 87 */     OutputStream out = part.getOutputStream();
/*  75: 88 */     writeTo(out);
/*  76: 89 */     out.close();
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void referenceUpdated(CellAddress oldReference, CTComment comment)
/*  80:    */   {
/*  81: 99 */     if (this.commentRefs != null)
/*  82:    */     {
/*  83:100 */       this.commentRefs.remove(oldReference);
/*  84:101 */       this.commentRefs.put(new CellAddress(comment.getRef()), comment);
/*  85:    */     }
/*  86:    */   }
/*  87:    */   
/*  88:    */   public int getNumberOfComments()
/*  89:    */   {
/*  90:106 */     return this.comments.getCommentList().sizeOfCommentArray();
/*  91:    */   }
/*  92:    */   
/*  93:    */   public int getNumberOfAuthors()
/*  94:    */   {
/*  95:110 */     return this.comments.getAuthors().sizeOfAuthorArray();
/*  96:    */   }
/*  97:    */   
/*  98:    */   public String getAuthor(long authorId)
/*  99:    */   {
/* 100:114 */     return this.comments.getAuthors().getAuthorArray((int)authorId);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public int findAuthor(String author)
/* 104:    */   {
/* 105:118 */     String[] authorArray = this.comments.getAuthors().getAuthorArray();
/* 106:119 */     for (int i = 0; i < authorArray.length; i++) {
/* 107:120 */       if (authorArray[i].equals(author)) {
/* 108:121 */         return i;
/* 109:    */       }
/* 110:    */     }
/* 111:124 */     return addNewAuthor(author);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public XSSFComment findCellComment(CellAddress cellAddress)
/* 115:    */   {
/* 116:134 */     CTComment ct = getCTComment(cellAddress);
/* 117:135 */     return ct == null ? null : new XSSFComment(this, ct, null);
/* 118:    */   }
/* 119:    */   
/* 120:    */   @Internal
/* 121:    */   public CTComment getCTComment(CellAddress cellRef)
/* 122:    */   {
/* 123:147 */     prepareCTCommentCache();
/* 124:    */     
/* 125:    */ 
/* 126:150 */     return (CTComment)this.commentRefs.get(cellRef);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public Map<CellAddress, XSSFComment> getCellComments()
/* 130:    */   {
/* 131:159 */     prepareCTCommentCache();
/* 132:160 */     TreeMap<CellAddress, XSSFComment> map = new TreeMap();
/* 133:162 */     for (Entry<CellAddress, CTComment> e : this.commentRefs.entrySet()) {
/* 134:163 */       map.put(e.getKey(), new XSSFComment(this, (CTComment)e.getValue(), null));
/* 135:    */     }
/* 136:166 */     return map;
/* 137:    */   }
/* 138:    */   
/* 139:    */   private void prepareCTCommentCache()
/* 140:    */   {
/* 141:176 */     if (this.commentRefs == null)
/* 142:    */     {
/* 143:177 */       this.commentRefs = new HashMap();
/* 144:178 */       for (CTComment comment : this.comments.getCommentList().getCommentArray()) {
/* 145:179 */         this.commentRefs.put(new CellAddress(comment.getRef()), comment);
/* 146:    */       }
/* 147:    */     }
/* 148:    */   }
/* 149:    */   
/* 150:    */   @Internal
/* 151:    */   public CTComment newComment(CellAddress ref)
/* 152:    */   {
/* 153:192 */     CTComment ct = this.comments.getCommentList().addNewComment();
/* 154:193 */     ct.setRef(ref.formatAsString());
/* 155:194 */     ct.setAuthorId(0L);
/* 156:196 */     if (this.commentRefs != null) {
/* 157:197 */       this.commentRefs.put(ref, ct);
/* 158:    */     }
/* 159:199 */     return ct;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public boolean removeComment(CellAddress cellRef)
/* 163:    */   {
/* 164:209 */     String stringRef = cellRef.formatAsString();
/* 165:210 */     CTCommentList lst = this.comments.getCommentList();
/* 166:211 */     if (lst != null)
/* 167:    */     {
/* 168:212 */       CTComment[] commentArray = lst.getCommentArray();
/* 169:213 */       for (int i = 0; i < commentArray.length; i++)
/* 170:    */       {
/* 171:214 */         CTComment comment = commentArray[i];
/* 172:215 */         if (stringRef.equals(comment.getRef()))
/* 173:    */         {
/* 174:216 */           lst.removeComment(i);
/* 175:218 */           if (this.commentRefs != null) {
/* 176:219 */             this.commentRefs.remove(cellRef);
/* 177:    */           }
/* 178:221 */           return true;
/* 179:    */         }
/* 180:    */       }
/* 181:    */     }
/* 182:225 */     return false;
/* 183:    */   }
/* 184:    */   
/* 185:    */   private int addNewAuthor(String author)
/* 186:    */   {
/* 187:236 */     int index = this.comments.getAuthors().sizeOfAuthorArray();
/* 188:237 */     this.comments.getAuthors().insertAuthor(index, author);
/* 189:238 */     return index;
/* 190:    */   }
/* 191:    */   
/* 192:    */   @Internal
/* 193:    */   public CTComments getCTComments()
/* 194:    */   {
/* 195:248 */     return this.comments;
/* 196:    */   }
/* 197:    */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.xssf.model.CommentsTable

 * JD-Core Version:    0.7.0.1

 */