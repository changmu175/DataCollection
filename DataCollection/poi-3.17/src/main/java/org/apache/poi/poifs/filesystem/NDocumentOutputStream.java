/*   1:    */ package org.apache.poi.poifs.filesystem;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.ByteArrayOutputStream;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.OutputStream;
/*   7:    */ import org.apache.poi.poifs.property.DocumentProperty;
/*   8:    */ 
/*   9:    */ public final class NDocumentOutputStream
/*  10:    */   extends OutputStream
/*  11:    */ {
/*  12:    */   private int _document_size;
/*  13:    */   private boolean _closed;
/*  14:    */   private NPOIFSDocument _document;
/*  15:    */   private DocumentProperty _property;
/*  16: 45 */   private ByteArrayOutputStream _buffer = new ByteArrayOutputStream(4096);
/*  17:    */   private NPOIFSStream _stream;
/*  18:    */   private OutputStream _stream_output;
/*  19:    */   
/*  20:    */   public NDocumentOutputStream(DocumentEntry document)
/*  21:    */     throws IOException
/*  22:    */   {
/*  23: 59 */     if (!(document instanceof DocumentNode)) {
/*  24: 60 */       throw new IOException("Cannot open internal document storage, " + document + " not a Document Node");
/*  25:    */     }
/*  26: 62 */     this._document_size = 0;
/*  27: 63 */     this._closed = false;
/*  28:    */     
/*  29: 65 */     this._property = ((DocumentProperty)((DocumentNode)document).getProperty());
/*  30:    */     
/*  31: 67 */     this._document = new NPOIFSDocument((DocumentNode)document);
/*  32: 68 */     this._document.free();
/*  33:    */   }
/*  34:    */   
/*  35:    */   public NDocumentOutputStream(DirectoryEntry parent, String name)
/*  36:    */     throws IOException
/*  37:    */   {
/*  38: 78 */     if (!(parent instanceof DirectoryNode)) {
/*  39: 79 */       throw new IOException("Cannot open internal directory storage, " + parent + " not a Directory Node");
/*  40:    */     }
/*  41: 81 */     this._document_size = 0;
/*  42: 82 */     this._closed = false;
/*  43:    */     
/*  44:    */ 
/*  45: 85 */     DocumentEntry doc = parent.createDocument(name, new ByteArrayInputStream(new byte[0]));
/*  46: 86 */     this._property = ((DocumentProperty)((DocumentNode)doc).getProperty());
/*  47: 87 */     this._document = new NPOIFSDocument((DocumentNode)doc);
/*  48:    */   }
/*  49:    */   
/*  50:    */   private void dieIfClosed()
/*  51:    */     throws IOException
/*  52:    */   {
/*  53: 91 */     if (this._closed) {
/*  54: 92 */       throw new IOException("cannot perform requested operation on a closed stream");
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   private void checkBufferSize()
/*  59:    */     throws IOException
/*  60:    */   {
/*  61: 98 */     if (this._buffer.size() > 4096)
/*  62:    */     {
/*  63:100 */       byte[] data = this._buffer.toByteArray();
/*  64:101 */       this._buffer = null;
/*  65:102 */       write(data, 0, data.length);
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void write(int b)
/*  70:    */     throws IOException
/*  71:    */   {
/*  72:109 */     dieIfClosed();
/*  73:111 */     if (this._buffer != null)
/*  74:    */     {
/*  75:112 */       this._buffer.write(b);
/*  76:113 */       checkBufferSize();
/*  77:    */     }
/*  78:    */     else
/*  79:    */     {
/*  80:115 */       write(new byte[] { (byte)b });
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void write(byte[] b)
/*  85:    */     throws IOException
/*  86:    */   {
/*  87:120 */     dieIfClosed();
/*  88:122 */     if (this._buffer != null)
/*  89:    */     {
/*  90:123 */       this._buffer.write(b);
/*  91:124 */       checkBufferSize();
/*  92:    */     }
/*  93:    */     else
/*  94:    */     {
/*  95:126 */       write(b, 0, b.length);
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void write(byte[] b, int off, int len)
/* 100:    */     throws IOException
/* 101:    */   {
/* 102:131 */     dieIfClosed();
/* 103:133 */     if (this._buffer != null)
/* 104:    */     {
/* 105:134 */       this._buffer.write(b, off, len);
/* 106:135 */       checkBufferSize();
/* 107:    */     }
/* 108:    */     else
/* 109:    */     {
/* 110:137 */       if (this._stream == null)
/* 111:    */       {
/* 112:138 */         this._stream = new NPOIFSStream(this._document.getFileSystem());
/* 113:139 */         this._stream_output = this._stream.getOutputStream();
/* 114:    */       }
/* 115:141 */       this._stream_output.write(b, off, len);
/* 116:142 */       this._document_size += len;
/* 117:    */     }
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void close()
/* 121:    */     throws IOException
/* 122:    */   {
/* 123:148 */     if (this._buffer != null)
/* 124:    */     {
/* 125:150 */       this._document.replaceContents(new ByteArrayInputStream(this._buffer.toByteArray()));
/* 126:    */     }
/* 127:    */     else
/* 128:    */     {
/* 129:155 */       this._stream_output.close();
/* 130:156 */       this._property.updateSize(this._document_size);
/* 131:157 */       this._property.setStartBlock(this._stream.getStartBlock());
/* 132:    */     }
/* 133:161 */     this._closed = true;
/* 134:    */   }
/* 135:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.NDocumentOutputStream
 * JD-Core Version:    0.7.0.1
 */