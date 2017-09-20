/*   1:    */ package org.apache.poi.poifs.property;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import java.nio.ByteBuffer;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.List;
/*   9:    */ import org.apache.poi.poifs.common.POIFSBigBlockSize;
/*  10:    */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/*  11:    */ import org.apache.poi.poifs.filesystem.NPOIFSStream;
/*  12:    */ import org.apache.poi.poifs.storage.HeaderBlock;
/*  13:    */ import org.apache.poi.util.POILogFactory;
/*  14:    */ import org.apache.poi.util.POILogger;
/*  15:    */ 
/*  16:    */ public final class NPropertyTable
/*  17:    */   extends PropertyTableBase
/*  18:    */ {
/*  19: 41 */   private static final POILogger _logger = POILogFactory.getLogger(NPropertyTable.class);
/*  20:    */   private POIFSBigBlockSize _bigBigBlockSize;
/*  21:    */   
/*  22:    */   public NPropertyTable(HeaderBlock headerBlock)
/*  23:    */   {
/*  24: 47 */     super(headerBlock);
/*  25: 48 */     this._bigBigBlockSize = headerBlock.getBigBlockSize();
/*  26:    */   }
/*  27:    */   
/*  28:    */   public NPropertyTable(HeaderBlock headerBlock, NPOIFSFileSystem filesystem)
/*  29:    */     throws IOException
/*  30:    */   {
/*  31: 66 */     super(headerBlock, buildProperties(new NPOIFSStream(filesystem, headerBlock.getPropertyStart()).iterator(), headerBlock.getBigBlockSize()));
/*  32:    */     
/*  33:    */ 
/*  34:    */ 
/*  35:    */ 
/*  36:    */ 
/*  37:    */ 
/*  38: 73 */     this._bigBigBlockSize = headerBlock.getBigBlockSize();
/*  39:    */   }
/*  40:    */   
/*  41:    */   private static List<Property> buildProperties(Iterator<ByteBuffer> dataSource, POIFSBigBlockSize bigBlockSize)
/*  42:    */     throws IOException
/*  43:    */   {
/*  44: 79 */     List<Property> properties = new ArrayList();
/*  45: 80 */     while (dataSource.hasNext())
/*  46:    */     {
/*  47: 81 */       ByteBuffer bb = (ByteBuffer)dataSource.next();
/*  48:    */       byte[] data;
/*  49:    */       byte[] data;
/*  50: 85 */       if ((bb.hasArray()) && (bb.arrayOffset() == 0) && (bb.array().length == bigBlockSize.getBigBlockSize()))
/*  51:    */       {
/*  52: 87 */         data = bb.array();
/*  53:    */       }
/*  54:    */       else
/*  55:    */       {
/*  56: 89 */         data = new byte[bigBlockSize.getBigBlockSize()];
/*  57:    */         
/*  58: 91 */         int toRead = data.length;
/*  59: 92 */         if (bb.remaining() < bigBlockSize.getBigBlockSize())
/*  60:    */         {
/*  61: 96 */           _logger.log(5, new Object[] { "Short Property Block, ", Integer.valueOf(bb.remaining()), " bytes instead of the expected " + bigBlockSize.getBigBlockSize() });
/*  62:    */           
/*  63: 98 */           toRead = bb.remaining();
/*  64:    */         }
/*  65:101 */         bb.get(data, 0, toRead);
/*  66:    */       }
/*  67:104 */       PropertyFactory.convertToProperties(data, properties);
/*  68:    */     }
/*  69:106 */     return properties;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public int countBlocks()
/*  73:    */   {
/*  74:116 */     long rawSize = this._properties.size() * 128L;
/*  75:117 */     int blkSize = this._bigBigBlockSize.getBigBlockSize();
/*  76:118 */     int numBlocks = (int)(rawSize / blkSize);
/*  77:119 */     if (rawSize % blkSize != 0L) {
/*  78:120 */       numBlocks++;
/*  79:    */     }
/*  80:122 */     return numBlocks;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void preWrite()
/*  84:    */   {
/*  85:129 */     List<Property> pList = new ArrayList();
/*  86:    */     
/*  87:131 */     int i = 0;
/*  88:132 */     for (Property p : this._properties) {
/*  89:134 */       if (p != null)
/*  90:    */       {
/*  91:135 */         p.setIndex(i++);
/*  92:136 */         pList.add(p);
/*  93:    */       }
/*  94:    */     }
/*  95:    */     Property p;
/*  96:140 */     for (Iterator i$ = pList.iterator(); i$.hasNext(); p.preWrite()) {
/*  97:140 */       p = (Property)i$.next();
/*  98:    */     }
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void write(NPOIFSStream stream)
/* 102:    */     throws IOException
/* 103:    */   {
/* 104:147 */     OutputStream os = stream.getOutputStream();
/* 105:148 */     for (Property property : this._properties) {
/* 106:149 */       if (property != null) {
/* 107:150 */         property.writeData(os);
/* 108:    */       }
/* 109:    */     }
/* 110:153 */     os.close();
/* 111:156 */     if (getStartBlock() != stream.getStartBlock()) {
/* 112:157 */       setStartBlock(stream.getStartBlock());
/* 113:    */     }
/* 114:    */   }
/* 115:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.property.NPropertyTable
 * JD-Core Version:    0.7.0.1
 */