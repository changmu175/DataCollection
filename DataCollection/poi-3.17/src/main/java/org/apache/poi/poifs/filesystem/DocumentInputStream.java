/*   1:    */ package org.apache.poi.poifs.filesystem;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import org.apache.poi.util.LittleEndianInput;
/*   6:    */ import org.apache.poi.util.SuppressForbidden;
/*   7:    */ 
/*   8:    */ public class DocumentInputStream
/*   9:    */   extends InputStream
/*  10:    */   implements LittleEndianInput
/*  11:    */ {
/*  12:    */   protected static final int EOF = -1;
/*  13:    */   protected static final int SIZE_SHORT = 2;
/*  14:    */   protected static final int SIZE_INT = 4;
/*  15:    */   protected static final int SIZE_LONG = 8;
/*  16:    */   private DocumentInputStream delegate;
/*  17:    */   
/*  18:    */   protected DocumentInputStream() {}
/*  19:    */   
/*  20:    */   public DocumentInputStream(DocumentEntry document)
/*  21:    */     throws IOException
/*  22:    */   {
/*  23: 54 */     if (!(document instanceof DocumentNode)) {
/*  24: 55 */       throw new IOException("Cannot open internal document storage");
/*  25:    */     }
/*  26: 57 */     DocumentNode documentNode = (DocumentNode)document;
/*  27: 58 */     DirectoryNode parentNode = (DirectoryNode)document.getParent();
/*  28: 60 */     if (documentNode.getDocument() != null) {
/*  29: 61 */       this.delegate = new ODocumentInputStream(document);
/*  30: 62 */     } else if (parentNode.getOFileSystem() != null) {
/*  31: 63 */       this.delegate = new ODocumentInputStream(document);
/*  32: 64 */     } else if (parentNode.getNFileSystem() != null) {
/*  33: 65 */       this.delegate = new NDocumentInputStream(document);
/*  34:    */     } else {
/*  35: 67 */       throw new IOException("No FileSystem bound on the parent, can't read contents");
/*  36:    */     }
/*  37:    */   }
/*  38:    */   
/*  39:    */   public DocumentInputStream(OPOIFSDocument document)
/*  40:    */   {
/*  41: 77 */     this.delegate = new ODocumentInputStream(document);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public DocumentInputStream(NPOIFSDocument document)
/*  45:    */   {
/*  46: 86 */     this.delegate = new NDocumentInputStream(document);
/*  47:    */   }
/*  48:    */   
/*  49:    */   @SuppressForbidden("just delegating")
/*  50:    */   public int available()
/*  51:    */   {
/*  52: 92 */     return this.delegate.available();
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void close()
/*  56:    */   {
/*  57: 97 */     this.delegate.close();
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void mark(int ignoredReadlimit)
/*  61:    */   {
/*  62:102 */     this.delegate.mark(ignoredReadlimit);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public boolean markSupported()
/*  66:    */   {
/*  67:112 */     return true;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public int read()
/*  71:    */     throws IOException
/*  72:    */   {
/*  73:117 */     return this.delegate.read();
/*  74:    */   }
/*  75:    */   
/*  76:    */   public int read(byte[] b)
/*  77:    */     throws IOException
/*  78:    */   {
/*  79:122 */     return read(b, 0, b.length);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public int read(byte[] b, int off, int len)
/*  83:    */     throws IOException
/*  84:    */   {
/*  85:127 */     return this.delegate.read(b, off, len);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void reset()
/*  89:    */   {
/*  90:137 */     this.delegate.reset();
/*  91:    */   }
/*  92:    */   
/*  93:    */   public long skip(long n)
/*  94:    */     throws IOException
/*  95:    */   {
/*  96:142 */     return this.delegate.skip(n);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public byte readByte()
/* 100:    */   {
/* 101:147 */     return this.delegate.readByte();
/* 102:    */   }
/* 103:    */   
/* 104:    */   public double readDouble()
/* 105:    */   {
/* 106:152 */     return this.delegate.readDouble();
/* 107:    */   }
/* 108:    */   
/* 109:    */   public short readShort()
/* 110:    */   {
/* 111:157 */     return (short)readUShort();
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void readFully(byte[] buf)
/* 115:    */   {
/* 116:162 */     readFully(buf, 0, buf.length);
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void readFully(byte[] buf, int off, int len)
/* 120:    */   {
/* 121:167 */     this.delegate.readFully(buf, off, len);
/* 122:    */   }
/* 123:    */   
/* 124:    */   public long readLong()
/* 125:    */   {
/* 126:172 */     return this.delegate.readLong();
/* 127:    */   }
/* 128:    */   
/* 129:    */   public int readInt()
/* 130:    */   {
/* 131:177 */     return this.delegate.readInt();
/* 132:    */   }
/* 133:    */   
/* 134:    */   public int readUShort()
/* 135:    */   {
/* 136:182 */     return this.delegate.readUShort();
/* 137:    */   }
/* 138:    */   
/* 139:    */   public int readUByte()
/* 140:    */   {
/* 141:187 */     return this.delegate.readUByte();
/* 142:    */   }
/* 143:    */   
/* 144:    */   public long readUInt()
/* 145:    */   {
/* 146:191 */     int i = readInt();
/* 147:192 */     return i & 0xFFFFFFFF;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public void readPlain(byte[] buf, int off, int len)
/* 151:    */   {
/* 152:197 */     readFully(buf, off, len);
/* 153:    */   }
/* 154:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.DocumentInputStream
 * JD-Core Version:    0.7.0.1
 */