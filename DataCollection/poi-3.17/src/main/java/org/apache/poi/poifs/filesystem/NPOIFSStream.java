/*   1:    */ package org.apache.poi.poifs.filesystem;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import java.nio.ByteBuffer;
/*   6:    */ import java.util.Iterator;
/*   7:    */ 
/*   8:    */ public class NPOIFSStream
/*   9:    */   implements Iterable<ByteBuffer>
/*  10:    */ {
/*  11:    */   private BlockStore blockStore;
/*  12:    */   private int startBlock;
/*  13:    */   private OutputStream outStream;
/*  14:    */   
/*  15:    */   public NPOIFSStream(BlockStore blockStore, int startBlock)
/*  16:    */   {
/*  17: 59 */     this.blockStore = blockStore;
/*  18: 60 */     this.startBlock = startBlock;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public NPOIFSStream(BlockStore blockStore)
/*  22:    */   {
/*  23: 68 */     this.blockStore = blockStore;
/*  24: 69 */     this.startBlock = -2;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public int getStartBlock()
/*  28:    */   {
/*  29: 78 */     return this.startBlock;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public Iterator<ByteBuffer> iterator()
/*  33:    */   {
/*  34: 86 */     return getBlockIterator();
/*  35:    */   }
/*  36:    */   
/*  37:    */   public Iterator<ByteBuffer> getBlockIterator()
/*  38:    */   {
/*  39: 90 */     if (this.startBlock == -2) {
/*  40: 91 */       throw new IllegalStateException("Can't read from a new stream before it has been written to");
/*  41:    */     }
/*  42: 95 */     return new StreamBlockByteBufferIterator(this.startBlock);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void updateContents(byte[] contents)
/*  46:    */     throws IOException
/*  47:    */   {
/*  48:105 */     OutputStream os = getOutputStream();
/*  49:106 */     os.write(contents);
/*  50:107 */     os.close();
/*  51:    */   }
/*  52:    */   
/*  53:    */   public OutputStream getOutputStream()
/*  54:    */     throws IOException
/*  55:    */   {
/*  56:111 */     if (this.outStream == null) {
/*  57:112 */       this.outStream = new StreamBlockByteBuffer();
/*  58:    */     }
/*  59:114 */     return this.outStream;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void free()
/*  63:    */     throws IOException
/*  64:    */   {
/*  65:125 */     BlockStore.ChainLoopDetector loopDetector = this.blockStore.getChainLoopDetector();
/*  66:126 */     free(loopDetector);
/*  67:    */   }
/*  68:    */   
/*  69:    */   private void free(BlockStore.ChainLoopDetector loopDetector)
/*  70:    */   {
/*  71:129 */     int nextBlock = this.startBlock;
/*  72:130 */     while (nextBlock != -2)
/*  73:    */     {
/*  74:131 */       int thisBlock = nextBlock;
/*  75:132 */       loopDetector.claim(thisBlock);
/*  76:133 */       nextBlock = this.blockStore.getNextBlock(thisBlock);
/*  77:134 */       this.blockStore.setNextBlock(thisBlock, -1);
/*  78:    */     }
/*  79:136 */     this.startBlock = -2;
/*  80:    */   }
/*  81:    */   
/*  82:    */   protected class StreamBlockByteBufferIterator
/*  83:    */     implements Iterator<ByteBuffer>
/*  84:    */   {
/*  85:    */     private BlockStore.ChainLoopDetector loopDetector;
/*  86:    */     private int nextBlock;
/*  87:    */     
/*  88:    */     protected StreamBlockByteBufferIterator(int firstBlock)
/*  89:    */     {
/*  90:147 */       this.nextBlock = firstBlock;
/*  91:    */       try
/*  92:    */       {
/*  93:149 */         this.loopDetector = NPOIFSStream.this.blockStore.getChainLoopDetector();
/*  94:    */       }
/*  95:    */       catch (IOException e)
/*  96:    */       {
/*  97:151 */         throw new RuntimeException(e);
/*  98:    */       }
/*  99:    */     }
/* 100:    */     
/* 101:    */     public boolean hasNext()
/* 102:    */     {
/* 103:156 */       if (this.nextBlock == -2) {
/* 104:157 */         return false;
/* 105:    */       }
/* 106:159 */       return true;
/* 107:    */     }
/* 108:    */     
/* 109:    */     public ByteBuffer next()
/* 110:    */     {
/* 111:163 */       if (this.nextBlock == -2) {
/* 112:164 */         throw new IndexOutOfBoundsException("Can't read past the end of the stream");
/* 113:    */       }
/* 114:    */       try
/* 115:    */       {
/* 116:168 */         this.loopDetector.claim(this.nextBlock);
/* 117:169 */         ByteBuffer data = NPOIFSStream.this.blockStore.getBlockAt(this.nextBlock);
/* 118:170 */         this.nextBlock = NPOIFSStream.this.blockStore.getNextBlock(this.nextBlock);
/* 119:171 */         return data;
/* 120:    */       }
/* 121:    */       catch (IOException e)
/* 122:    */       {
/* 123:173 */         throw new RuntimeException(e);
/* 124:    */       }
/* 125:    */     }
/* 126:    */     
/* 127:    */     public void remove()
/* 128:    */     {
/* 129:178 */       throw new UnsupportedOperationException();
/* 130:    */     }
/* 131:    */   }
/* 132:    */   
/* 133:    */   protected class StreamBlockByteBuffer
/* 134:    */     extends OutputStream
/* 135:    */   {
/* 136:183 */     byte[] oneByte = new byte[1];
/* 137:    */     ByteBuffer buffer;
/* 138:    */     BlockStore.ChainLoopDetector loopDetector;
/* 139:    */     int prevBlock;
/* 140:    */     int nextBlock;
/* 141:    */     
/* 142:    */     protected StreamBlockByteBuffer()
/* 143:    */       throws IOException
/* 144:    */     {
/* 145:191 */       this.loopDetector = NPOIFSStream.this.blockStore.getChainLoopDetector();
/* 146:192 */       this.prevBlock = -2;
/* 147:193 */       this.nextBlock = NPOIFSStream.this.startBlock;
/* 148:    */     }
/* 149:    */     
/* 150:    */     protected void createBlockIfNeeded()
/* 151:    */       throws IOException
/* 152:    */     {
/* 153:197 */       if ((this.buffer != null) && (this.buffer.hasRemaining())) {
/* 154:197 */         return;
/* 155:    */       }
/* 156:199 */       int thisBlock = this.nextBlock;
/* 157:203 */       if (thisBlock == -2)
/* 158:    */       {
/* 159:204 */         thisBlock = NPOIFSStream.this.blockStore.getFreeBlock();
/* 160:205 */         this.loopDetector.claim(thisBlock);
/* 161:    */         
/* 162:    */ 
/* 163:208 */         this.nextBlock = -2;
/* 164:211 */         if (this.prevBlock != -2) {
/* 165:212 */           NPOIFSStream.this.blockStore.setNextBlock(this.prevBlock, thisBlock);
/* 166:    */         }
/* 167:214 */         NPOIFSStream.this.blockStore.setNextBlock(thisBlock, -2);
/* 168:218 */         if (NPOIFSStream.this.startBlock == -2) {
/* 169:219 */           NPOIFSStream.this.startBlock = thisBlock;
/* 170:    */         }
/* 171:    */       }
/* 172:    */       else
/* 173:    */       {
/* 174:222 */         this.loopDetector.claim(thisBlock);
/* 175:223 */         this.nextBlock = NPOIFSStream.this.blockStore.getNextBlock(thisBlock);
/* 176:    */       }
/* 177:226 */       this.buffer = NPOIFSStream.this.blockStore.createBlockIfNeeded(thisBlock);
/* 178:    */       
/* 179:    */ 
/* 180:229 */       this.prevBlock = thisBlock;
/* 181:    */     }
/* 182:    */     
/* 183:    */     public void write(int b)
/* 184:    */       throws IOException
/* 185:    */     {
/* 186:233 */       this.oneByte[0] = ((byte)(b & 0xFF));
/* 187:234 */       write(this.oneByte);
/* 188:    */     }
/* 189:    */     
/* 190:    */     public void write(byte[] b, int off, int len)
/* 191:    */       throws IOException
/* 192:    */     {
/* 193:238 */       if ((off < 0) || (off > b.length) || (len < 0) || (off + len > b.length) || (off + len < 0)) {
/* 194:240 */         throw new IndexOutOfBoundsException();
/* 195:    */       }
/* 196:241 */       if (len == 0) {
/* 197:    */         return;
/* 198:    */       }
/* 199:    */       do
/* 200:    */       {
/* 201:246 */         createBlockIfNeeded();
/* 202:247 */         int writeBytes = Math.min(this.buffer.remaining(), len);
/* 203:248 */         this.buffer.put(b, off, writeBytes);
/* 204:249 */         off += writeBytes;
/* 205:250 */         len -= writeBytes;
/* 206:251 */       } while (len > 0);
/* 207:    */     }
/* 208:    */     
/* 209:    */     public void close()
/* 210:    */       throws IOException
/* 211:    */     {
/* 212:256 */       NPOIFSStream toFree = new NPOIFSStream(NPOIFSStream.this.blockStore, this.nextBlock);
/* 213:257 */       toFree.free(this.loopDetector);
/* 214:260 */       if (this.prevBlock != -2) {
/* 215:261 */         NPOIFSStream.this.blockStore.setNextBlock(this.prevBlock, -2);
/* 216:    */       }
/* 217:    */     }
/* 218:    */   }
/* 219:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.NPOIFSStream
 * JD-Core Version:    0.7.0.1
 */