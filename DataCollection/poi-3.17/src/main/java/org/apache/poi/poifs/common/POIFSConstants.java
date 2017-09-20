/*  1:   */ package org.apache.poi.poifs.common;
/*  2:   */ 
/*  3:   */ public abstract interface POIFSConstants
/*  4:   */ {
/*  5:   */   public static final int SMALLER_BIG_BLOCK_SIZE = 512;
/*  6:29 */   public static final POIFSBigBlockSize SMALLER_BIG_BLOCK_SIZE_DETAILS = new POIFSBigBlockSize(512, (short)9);
/*  7:   */   public static final int LARGER_BIG_BLOCK_SIZE = 4096;
/*  8:33 */   public static final POIFSBigBlockSize LARGER_BIG_BLOCK_SIZE_DETAILS = new POIFSBigBlockSize(4096, (short)12);
/*  9:   */   public static final int SMALL_BLOCK_SIZE = 64;
/* 10:   */   public static final int PROPERTY_SIZE = 128;
/* 11:   */   public static final int BIG_BLOCK_MINIMUM_DOCUMENT_SIZE = 4096;
/* 12:   */   public static final int LARGEST_REGULAR_SECTOR_NUMBER = -5;
/* 13:   */   public static final int DIFAT_SECTOR_BLOCK = -4;
/* 14:   */   public static final int FAT_SECTOR_BLOCK = -3;
/* 15:   */   public static final int END_OF_CHAIN = -2;
/* 16:   */   public static final int UNUSED_BLOCK = -1;
/* 17:62 */   public static final byte[] OOXML_FILE_HEADER = { 80, 75, 3, 4 };
/* 18:65 */   public static final byte[] RAW_XML_FILE_HEADER = { 60, 63, 120, 109, 108 };
/* 19:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.common.POIFSConstants
 * JD-Core Version:    0.7.0.1
 */