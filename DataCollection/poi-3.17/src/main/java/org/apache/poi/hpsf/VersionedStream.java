/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Internal;
/*  4:   */ import org.apache.poi.util.LittleEndianByteArrayInputStream;
/*  5:   */ 
/*  6:   */ @Internal
/*  7:   */ class VersionedStream
/*  8:   */ {
/*  9:27 */   private final GUID _versionGuid = new GUID();
/* 10:28 */   private final IndirectPropertyName _streamName = new IndirectPropertyName();
/* 11:   */   
/* 12:   */   void read(LittleEndianByteArrayInputStream lei)
/* 13:   */   {
/* 14:33 */     this._versionGuid.read(lei);
/* 15:34 */     this._streamName.read(lei);
/* 16:   */   }
/* 17:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.VersionedStream
 * JD-Core Version:    0.7.0.1
 */